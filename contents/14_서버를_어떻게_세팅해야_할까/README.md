# 14. 서버를 어떻게 세팅해야할까?

- 설정해야 하는 대상
- 아파치 웹 서버의 설정
- 웹 서버의 Keep Alive
- DB Connection Pool 수 및 스레드 개수 설정
- WAS 인스턴스 개수 설정
- Session Timeout 설정

---

## 설정해야 하는 대상

가장 중요한 것은 성능 테스트를 통해 병목 지점을 파악해두는 것

- 웹 서버 세팅
- WAS 서버 세팅
- DB 서버 세팅
- 장비 세팅

## 아파치 웹 서버의 설정

- WAS를 Web Server 앞에 두고, Web Server가 WAS로 요청을 전달하는 방식
    - **WAS를 Web Server 로 사용하면 안됨**
    - 이러면 WAS 스레드를 정적인 리소스에 낭비하게됨

### Apache MPM (Multi-Processing Module)

- Apache 웹 서버는 MPM을 통해 요청을 처리
- 여러개의 프로세싱 모듈로 요청을 처리

httpd.conf

```conf
ThreadsPerChild 250
MaxRequestsPerChild 0
```

- `ThreadsPerChild` : 웹서버가 사용할 스레드 수
- `MaxRequestsPerChild` : 하나의 스레드가 처리할 수 있는 최대 요청 수
    - 0 : 무한대

conf/extra/httpd-mpm.conf

```conf
<IfModule mpm_prefork_module>
    StartServers 2
    MaxClients 150
    MinSpareThreads 25
    MaxSpareThreads 75
    ThreadsPerChild 25
    MaxRequestsPerChild 0
</IfModule>
```

- thread 수 세부 설정
- `StartServers` : 서버 시작시 생성할 프로세스 수 (child process)
- `MaxClients` : 최대 동시 접속자 수
- `MinSpareThreads` : 최소 여유 스레드 수
- `MaxSpareThreads` : 최대 여유 스레드 수
- `ThreadsPerChild` : 프로세스 당 스레드 수
- `MaxRequestsPerChild` : 하나의 스레드가 처리할 수 있는 최대 요청 수
    - 0 : 무한대

#### 해석

- 기본 요청 처리 수 = `StartServers` * `ThreadsPerChild` = 50
- 최대 요청 수 = `StartServers` * `MaxSpareThreads` = 150
    - 서버 리소스가 팡팡 놀아도 최대 150개의 요청만 처리

#### Webserver, WAS에 접속자가 몰리면? (150명 이상)

1. 초당 150명 처리중
2. WAS GC 발생 (2초 delay)
3. Webserver에서 300명이 WAS GC 대기
4. Tomcat (WAS)은 AjpConnector의 설정값 `backlog`를 사용
    - `backlog` : 최대 대기 요청 수 (default : 100)
    - 해석 : WAS 응답이 없을때 100개까지만 queue에 쌓음, 나머지는 **503 service unavailable**

#### 503 service unavailable 을 피하는 방법 (해결 방법)

- 방법 1. 서버 증설 (가장 마지막에 해야함!)
    - 금전적인 여유가 있을 떄
- 방법 2. 서비스 튜닝
    - 즉각적인 조치가 힘듬
- 방법 3. GC 튜닝
    - GC가 WAS 응답 지연의 원인일 경우
- 방법 4. 설정값 조정
    - 가장 간단하지만 영향력과 리스크가 매우 큼
    - WAS, Webserver 전문가 필요
    - 서버 리소스가 충분하다면 httpd.conf, httpd-mpm.conf 파일을 수정

## 웹 서버의 Keep Alive

- 웹 서버 설정시 가장 중요한 옵션
- `KeeppAlive On`
- Http 연결을 끊지않고 계속 재사용 (리소스 요청마다)
- CDN을 사용해 정적인 컨텐츠를 별도의 서버에 두는 방법도 있음
- `KeepAliveTimeout 15`
    - 15초 동안 요청이 없으면 연결을 끊음

## DB Connection Pool 수 및 스레드 개수 설정

- DB Connection Pool과 스레드는 많을수록 메모리 점유
- 스레드 수 : 티켓 수
- DB Connection Pool : 공연장 좌석 수
- 가장 적합한 값은?
    - 성능 테스트로 구하세요

#### DB Connection Pool

- 대부분의 WAS에서 최소치, 증가치, 최대치 지정 가능
- 최소치 : 서버 가동 시 연결 수행 개수
    - 많을수록 서버 가동시간 증가
    - 개발자 local에서는 많을 필요 없음
- 상용서버는 최소치 = 최대치 권장
    - 접속자수가 몰려 최소치가 올라갈 때 지연시간 발생 가능성이 있기 때문
- 대부분의 WAS의 default = 최소치, 최대치를 10 ~ 20으로 설정
    - 운영 서버에서 부족한 값
- 보통 40 ~ 50개로 지정

#### 스레드 개수

- 스레드 수는 DB Connection Pool보다 10정도 더 지정

#### DB Connection Pool과 스레드 개수 설정

- 스레드 수 < DB Conneciton Pool 일 때,
    - 스레드는 대기하고, pool에 노는 Conneciton 발생
- 스레드 수 > DB Connection Pool 일 때,
    - 다른 스레드들을 DB Connection Pool아닌 곳에서 사용할 수 있음

#### DB CPU가 100%일 때

- 이미 DB Connection Pool을 모두 사용할 수 있도록 설정했다는 전제
- CPU 점유 쿼리 튜닝
- DB Connection Pool을 늘리면 DB CPU는 한계에 도달 (이미 도달)

#### DB CPU는 50%인데, WAS CPU가 100%일 때

- application의 문제
- DB Connection Pool의 여유 수를 두기위해 5~10개 더 늘림

### wait time (대기 시간)

- DB Connection Framework에서 사용하는 옵션
- DB Connection Pool에 여유가 없을 때, 대기 시간을 설정
- MyBatis의 `poolTimeToWait` 옵션 (default : 20000ms)
    - Conneciton Pool이 모두 사용중이면 사용자는 최대 20초를 기다림

## WAS 인스턴스 개수 설정

- 하나의 장비에 필요한 WAS 인스턴스 수
- 많을 수록 CPU 점유
- 보통 1~2개의 CPU당 하나의 인스턴스
- 그래도! 성능 테스트로 구하세요

#### 예시 : CPU core가 36개인 서버

- 일반적으로 18 ~ 36개를 띄운다?
- TPS도 고려하자
- TPS가 많지도 않은데 Core수만큼 인스턴스를 띄운다면, 관리 포인트만 늘어남
    - 배포 시간, 로그 관리 등

#### 예시 : 서버의 메모리가 4GB일 때

- 인스턴스가 4GB를 차지하면 안됨
    - FullGC시 많은 시간 소요
    - 다른 OS 프로세스에 영향을 줌
- 가급적 512MB ~ 2GB로 설정

#### 예시 : 인스턴스가 하나

- 안좋음, 서버의 장애 발생시 서비스 중단 (HA)
- 두개 이상의 인스턴스가 서로 다른 서버에서 클러스터링하도록 설정
- DB Connection Pool, 스레드 수가 100개 이상 필요? -> 인스턴스 분리

## Session Timeout 설정

- WAS 설정이 아닌, 애플리케이션 설정
- WEB-INF/web.xml

```xml

<session-config>
    <session-timeout>30</session-timeout>
</session-config>
```

- 30분 동안 요청이 없으면 세션을 메모리에서 제거
- 설정 값이 없거나 `invalidate()` 호출이 없으면 세션은 메모리에 계속 존재

