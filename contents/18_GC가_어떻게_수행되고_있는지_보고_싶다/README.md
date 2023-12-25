# 18. GC가 어떻게 수해오디고 있는지 보고 싶다

- 자바 인스턴스 확인을 위한 jps
- GC 상황을 확인하는 jstat
- jstat 명령에서 GC 튵닝을 위해서 가장 유용한 옵션은 두 개
- 원격으로 JVM 상황을 모니터링하기 위한 jstatd
- verbosegc 옵션을 이용하여 gc 로그 남기기
- 어설프게 아는 것이 제일 무섭다

---

## 자바 인스턴스 확인을 위한 jps

- jps는 해당 머신에서 운영 중인 JVM 목록을 보여줌
- jdk/bin 디렉토리에 있음

````
jps [-q] [-mlvV] [-Joption] [<hostid>]
````

- `-q` : 클래스나 JAR 파일명, 인수 등을 생랴갛고 내용
- `-m` : main 메서드에 전달되는 인수
- `-l` : main 메서드의 전체 클래스 이름
- `-v` : JVM의 인수
- `-V` : JVM의 플래그 파일을 통해 전달된 인수
- `-Joption` : 자바 옵션을 이 옵션 뒤에 지정

````
C:\Java\jdk1.8.0_181\bin>jps
2464 Bootstrap
1024 Jps
````

## GC 상황을 확인하는 jstat

- jstat은 GC가 수행되는 정보를 확인하기 위한 명령어

````
jstat -<option> [-t] [-h<lines>] <vmid> [<interval> [<count>]]
````

- `-t` : 해당 자바 인스턴스가 생성된 시점부터의 타임 스탬프
- `-h<lines>` : 각 열의 설명을 지정된 라인 주기로 표시
- `interval` : 각 출력 사이의 간격을 지정 (단위는 ms)
- `count` : 출력 횟수를 지정

#### jstat 옵션

- `class` : 클래스 로더의 상태
- `compiler` : JIT 컴파일러의 횟수와 시간
- `gc` : GC 힙 영역에 대한 통계
- `gccapacity` : 각 영역 허용치, 연관 영역에 대한 통계
- `gccauses` : GC 요약 정보, 마지막 GC와 현재 GC에 대한 통계
- `gcnew` : 각 영역에 대한 통계
- `gcnewcapacity` : Young 영역의 통계
- `gcold` : Old, Perm 영역의 통계
- `gcoldcapacity` : Old 크기에 대한 통계
- `gcpermcapacity` : Perm 크기에 대한 통계
- `gcutil` : GC 요약 정보
- `printcompilation` : 핫 스팟 컴파일 메서드에 댛나 통계

````
jstat -gcnew -t -h10 2464 1000 20 > jstat_WAS1.log
````

- `-gcnew` : 각 영역에 대한 통계
- `-t` : 해당 자바 인스턴스가 생성된 시점부터의 타임 스탬프
- `-h10` : 10줄에 한번 씩 각 열의 설명
- `2464` : PID
- `1000` : 1초마다
- `20` : 20번 출력
- `> jstat_WAS1.log` : 로그 파일로 저장

## jstat 명령에서 GC 튵닝을 위해서 가장 유용한 옵션은 두 개

- `-gcutil`, `-gcapacity`

```bash
gimgihyeon@gimgihyeon-ui-Mac-Studio bin % jstat -gccapacity 32557
   NGCMN        NGCMX         NGC          S0C     S1C              EC         OGCMN        OGCMX         OGC           OC         MCMN       MCMX        MC       CCSMN     CCSMX     CCSC     YGC    FGC   CGC 
    174592.0    2796032.0     195072.0     44544.0     47104.0     100352.0     349696.0    5592576.0     349696.0     349696.0        0.0  1058816.0     9600.0       0.0 1048576.0    1152.0     14     0     -
    
````

- 어떤 영역의 크기를 늘리고 줄일지 판단 가능

```bash
    
gimgihyeon@gimgihyeon-ui-Mac-Studio bin % jstat -gcutil 32557    
  S0     S1     E      O      M     CCS    YGC     YGCT     FGC    FGCT     CGC    CGCT       GCT   
 58.66   0.00  51.96   3.73  95.77  91.56     30     0.536     0     0.000     -         -     0.536
````

## 원격으로 JVM 상황을 모니터링하기 위한 jstatd

- 원격 JVM 상황을 모니터링하기 위해서는 jstatd 데몬을 사용

````
jstatd [-nr] [-p port] [-n rminum] 
````

- `-nr` : RMI registry가 존재하지 않을 경우 새로운 RMI registry를 jstatd 프로세스 내에서 시작하지 않을 것을 정의
- `-p port` : RMI 레지스트리 식별 포트
- `-n rminum` : RMI 객체 이름, (defualt : JsatRemoteHost)

#### lib/security/java.policy 파일 수정

````
grant codebase "file:${java.home}/../lib/tools.jar" {
    permission java.security.AllPermission;
};
````

#### jstatd 실행

````
jstatd -J-Djava.security.policy=java.policy -p 2020
````

- `-p 2020` : 2020 포트로 실행

#### jps로 확인

````
C:\Java\jdk1.8.0_181\bin>jps kghpc:2020
2464 Bootstrap
3500 Jps
C:\Java\jdk1.8.0_181\bin>jstat -gcutil 2464 kghpc:2020 1000
  S0     S1     E      O      M     CCS    YGC     YGCT     FGC    FGCT     CGC    CGCT       GCT
 58.66   0.00  51.96   3.73  95.77  91.56     30     0.536     0     0.000     -         -     0.536
````

## verbosegc 옵션을 이용하여 gc 로그 남기기

- GC 로그를 남기기 위해서는 java 수행 시 `-verbosegc` 옵션을 사용

```bash
gimgihyeon@gimgihyeon-ui-Mac-Studio eighteen % java -verbosegc GCMaker.java
[0.004s][info][gc] Using G1
[0.242s][info][gc] GC(0) Pause Young (Normal) (G1 Evacuation Pause) 35M->18M(520M) 2.310ms
.
[1.275s][info][gc] GC(1) Pause Young (Normal) (G1 Evacuation Pause) 54M->54M(520M) 14.916ms
[1.293s][info][gc] GC(2) Pause Young (Normal) (G1 Evacuation Pause) 74M->75M(520M) 7.892ms
.
[2.323s][info][gc] GC(3) Pause Young (Normal) (G1 Evacuation Pause) 103M->105M(520M) 11.852ms
.
[3.363s][info][gc] GC(4) Pause Young (Normal) (G1 Evacuation Pause) 145M->147M(520M) 15.567ms
.
[4.411s][info][gc] GC(5) Pause Young (Normal) (G1 Evacuation Pause) 195M->198M(1040M) 22.921ms
.
.
[6.479s][info][gc] GC(6) Pause Young (Normal) (G1 Evacuation Pause) 258M->263M(1040M) 25.831ms
.
[7.539s][info][gc] GC(7) Pause Young (Normal) (G1 Evacuation Pause) 331M->335M(1040M) 27.299ms
.
.
.
[10.634s][info][gc] GC(8) Pause Young (Normal) (G1 Evacuation Pause) 431M->437M(1040M) 36.539ms
.
.
.
[13.723s][info][gc] GC(9) Pause Young (Normal) (G1 Evacuation Pause) 549M->555M(1040M) 34.661ms
.
[14.762s][info][gc] GC(10) Pause Young (Concurrent Start) (G1 Humongous Allocation) 587M->591M(1040M) 26.492ms
[14.762s][info][gc] GC(11) Concurrent Mark Cycle
[14.781s][info][gc] GC(11) Pause Remark 631M->107M(376M) 0.981ms
[14.802s][info][gc] GC(11) Pause Cleanup 107M->107M(376M) 0.058ms
[14.802s][info][gc] GC(11) Concurrent Mark Cycle 40.713ms
.
.
.
[17.873s][info][gc] GC(12) Pause Young (Normal) (G1 Preventive Collection) 219M->207M(376M) 35.324ms
.
[18.891s][info][gc] GC(13) Pause Young (Concurrent Start) (G1 Humongous Allocation) 213M->224M(376M) 11.510ms

````

### PrintGCTimeStamps 옵션

- `-XX:+PrintGCTimeStamps` : GC가 발생한 시각을 출력

### PrintHeapAtGC 옵션

- `-XX:+PrintHeapAtGC` : GC가 발생할 때 힙 영역의 상태를 출력

### PrintGCDetails

- `-XX:+PrintGCDetails` : GC가 발생할 때 자세한 정보를 출력

## 어설프게 아는 것이 제일 무섭다

- 메모리 릭이 발생하는 Java application은 1프로도 안됨
- jstat과 verbose 로그로 추적해볼 것
- Full GC가 일어난 뒤 Old 영역 메모리 사용량이 80% 이상이면 메모리 릭이 발생했을 가능성이 높음