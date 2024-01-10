# 19. GC 튜닝을 항상 할 필요는 없다

- GC 튜닝을 꼭 해야 할까?
- GC의 성능을 결정하는 옵션들
- GC 튜닝의 절차
- 1,2 단계 : GC 상황 모니터링 및 결과 분석하기
- 3-1 단계 : GC 방식 지정
- 3-2 단계 : 메모리 크기
- 4단계 : GC 튜닝 결과 분석

---

- 꼭 필요한 경우에 하는게 좋음
- **WAS 띄울 때 아무 옵션을 주지 말라는게 아님**
- 기본적인 메모리 크기정도만 지정하면 웬만큼 사용량이 많지 않은 시스템에서는 튜닝 필요 없음
- Silver Bullet은 없음

## GC 튜닝을 꼭 해야 할까?

- 결론 : Java 기반의 모든 서비스에서 GC 튜닝을 진행할 필요 없음
- GC 튜닝은 가장 마지막에 하는 작업
- 튜닝 목적
    - 목적 1. Old 영역으로 넘어가는 객체수 최소화
    - 목적 2. Full GC 실행 시간 줄이기

#### GC 튜닝이 필요 없는 경우

- `-Xms`, -`Xmx` 옵션으로 메모리 크기 지정
- `-server` dhqtus vhgka
- 시스템 로그에 타임아웃 관련 로그가 없음
    - DB 작업 관련
    - 다른 서버와 통신 관련
- 타임아웃이 있다는 것은 사용자 일부가 정상적인 응답을 못받았다는 것

#### GC 튜닝이 필요한 경우

- JVM 메모리 크기 지정 안함
- Timeout이 지속적으로 발생

### Old 영역으로 넘어가는 객체수 최소화하기

- Oracle JVM은 대부분 Generational GC 방식을 사용
- Eden에서 만들어져 Survivor을 오ㄱ가다가 Old 영역으로 넘어감
- Old 영역 GC는 New 영역보다 오래걸림
- New 영역의 크기를 잘 조절하면 Old 영역으로 넘어가는 객체 수를 줄일 수 있음

### Full GC 실행 시간 줄이기

- Full GC는 Young GC 보다 오래걸림
- Old 영역 크기를 줄이면 OutOfMemoryError가 발생하거나, Full GC 빈도가 잦아짐
- Old 영역 크기를 늘리면 Full GC 빈도는 줄어들지만, 실행 시간이 늘어남
- Old 영역 크기를 적절히 설정할 것

## GC의 성능을 결정하는 옵션들

- 절대 안되는 것 : 누가 이렇게 설정하니까 성능이 좋으니 우리도 이렇게 적용하자
    - 서비스마다 객체 생성 수, 크기, 수명이 모두 다름
- 힙 크기 (거의 필수 지정)
    - `-Xms` : JVM 시작 시 힙 크기
    - `-Xmx` : 최대 힙 크기
- New 영역 크기
    - `-XX:NewRatio` : Old 영역과 New 영역의 비율
    - `-XX:NewSize` : New 영역 크기
    - `-XX:SurvirorRatio` : Eden 영역과 Survivor 영역의 비율
- 자주 사용 : `-Xms`, `-XmX`, `-XX:NewRatio`
- Perm 영역 크기 : OOM 이 자주 일어나고 원인이 Perm 크기 문제일 때
    - `-XX:PermSize` : Perm 영역 시작 크기
    - `-XX:MaxPermSize` : Perm 영역 최대 크기

| GC 방식                  | 옵션                                                                                                                                                                            |
|------------------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Serial GC              | `-XX:+UseSerialGC`                                                                                                                                                            |
| Parallel GC            | `-XX:+UseParallelGC`<br/>`-XX:ParallelGCThreads=<value>`                                                                                                                      |
| Parallel Compacting GC | `-XX:+UseParallelOldGC`                                                                                                                                                       |
| CMS GC                 | `-XX:+UseConcMarkSweepGC`<br/>`-XX:+UseParNewGC`<br/>`-XX:CMSParallelRemarkEnabled`<br/>`-XX:CMSInitiatingOccupancyFraction=<value>`<br/>`-XX:+UseCMSInitiatingOccupancyOnly` |
| G1                     | `-XX:+UnlockExperimentalVMOptions`<br/>`-XX:+UseG1GC`                                                                                                                         |

- Serial GC는 클라이언트 장비에 최적화되어있으므로 신경안써도 됨

## GC 튜닝의 절차

1. GC 상황 모니터링
    - GC 상황을 모니터링하며 현재 시스템의 상황을 파악
2. 모니터링 결과 분석 후 GC 튜닝 여부 결정
    - 분석 결과 GC 수행 소요시간이 0.1 ~0.3 초 정도면 튜닝 필요 없음
    - 1~3초가 넘어가면 튜닝 진행 필요
3. GC 방식, 메모리 크기 지정
    - 서버가 여러대라면, 옵션을 서로 다르게 지정해 GC 옵션에 따른 성능 비교해도 좋음
4. 결과 분석
    - 최소 24시간 이상의 데이터 수집
    - 3번을 반복하며 최적의 옵션을 찾아야 함
5. 결과가 만족스러우면 전체서버에 반영

## 1,2 단계 : GC 상황 모니터링 및 결과 분석하기

```bash
$ jstat -gcutil 21719 1s
S0     S1     E      O      P     YGC     YGCT    FGC    FGCT     GCT
48.66  0.00  48.10  49.70  77.45  3428  172.623  3  59.050  231.673
48.66  0.00  48.10  49.70  77.45  3428  172.623  3  59.050  231.673
```

1. `YGC`, `YGCT` 확인
    - `YGC` : Young GC 횟수
    - `YGCT` : Young GC 소요 시간
    - 50ms (`YGCT/ YGC`) 소요
    - Young 영역 신경 안써도 됨
2. `FGCT`, `FCG` 확인
    - `FGCT` : Full GC 소요 시간
    - `FGC` : Full GC 횟수
    - 19.68 sec (`FGCT/ FGC`) 소요

- 주의사항 : GC 시간과 횟수를 모두 볼 것
    - `-gccapacity` 옵션으로 메모리 사용량 확인
- `-verbose:gc` 옵션을 주면 GC 상황을 로그로 남김 (추천)
- HPJMeter 툴 추천

## 3-1 단계 : GC 방식 지정

- 세가지 다 지정해보는 방시기 가장 좋음
- 일반적으로 CMS GC가 Parallel GC보다 성능이 좋음
    - 그러나 Concurrent Mode Failure가 발생하면 더 느림

### Concurrent Mode Failure에 대해서 더 알아 보자

- 압축 작업 : 메모리 할당 공간 사이에 사용하지 않는 빈 공간이 없어지도록 옮김 (단편화)
- Parallel GC에서는 Full GC 마다 압축을 진행함 (시간 소요 많음)
    - Full GC 이후에는 메모리 연속할당이 가능에 더 빠름
- CMS GC는 압축작업이 없음
    - 그래서 빠름
    - 그러나 메모리 빈공간이 여기저기 있어 큰 객체가 들어갈 공간이 없을 때도 있음
    - 이럴 때 Concurrent Mode Failure가 발생함

## 3-2 단계 : 메모리 크기 (`-Xms`, `-Xmx`)

- 메모리가 크면
    - GC 발생 횟수가 감소
    - GC 시간이 증가
- 메모리가 작으면
    - GC 발생 횟수가 증가
    - GC 시간이 감소

#### 추천방안

- 500MB 로 설정 (`-Xms500m -Xmx500m` 이 아님!)
- Full GC 이후 Old 영역 메모리가 300MB 라면,
    - 300MB (기본 사용) + 500MB (Old 영역용 최소) + 200MB (여유분) = 1GB (Old 영역)

#### NewRatio : Old 영역과 New 영역의 비율

- `-XX:NewRatio=1` : Old 영역과 New 영역의 비율을 1:1로 설정
- `-XX:NewRatio=2` : Old 영역과 New 영역의 비율을 1:2로 설정
- New 영역이 작으면 Old 영역으로 넘어가는 메모리가 많아지고, Full GC 시간이 늘어나고, 잦아짐
- 추천 : 2~3

## 4단계 : GC 튜닝 결과 분석

- 우선순위
- Full GC 수행시간
- Minor GC 수행시간
- Full GC 수행 간격
- Minor GC 수행 간격
- 전체 Full GC 수행 시간
- 전체 Minor GC 수행 시간
- 전체 GC 수행 시간
- Full GC 수행 횟수
- Minor GC 수행 횟수