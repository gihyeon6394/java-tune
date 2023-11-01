# 2. 내가 만든 프로그램의 속도를 알고 싶다

- 프로그래밍 툴이란?
- System 클래스
- `System.currentTimeMillis()`와 `System.nanoTime()`

---

## 프로그래밍 툴이란?

| 구분             | 특징                                                                                                               |
|----------------|------------------------------------------------------------------------------------------------------------------|
| profiling tool | 소스 레벨 분석 <br/>application 세부 응답시간<br/>메모리 샤용량을 객체, 클래스, 소스 라인단위로 분석<br/>APM에 비해 저렴<br/>Java 기반 client 프로그램 분석 가능 |
| APM tool       | 실시간 모니터링<br/>서버 사용자, 리소스 수 모니터링<br/>가격이 profiling tool 대비 비쌈<br/>CPU 기반 가격 책정                                    |

- 응답시간 프로파일링
    - 응답시간 측정 위함
    - 클래스 내의 메서드 단위의 응답시간 측정
    - CPU 시간, 대기시간
        - 소요시간 (Clock time) = CPU 시간 + 대기시간
- 메모리 프로파일링
    - 잠깐 사용하고 GC 대상이 되는 부분
    - memory leak
    - 클래스, 메서드 단위의 메모리 사용량 분석
    -

## System 클래스

- 모든 메서드가 static

```Java
public class SystemArrayCopy {
    public static void main(String[] args) {
        String[] src = new String[]{"a", "b", "c", "d", "e"};
        String[] coppied = new String[3];
        System.arraycopy(src, 1, coppied, 0, 3);
        for (String s : coppied) {
            System.out.println(s); // b c d
        }
    }
}
```

### 속성(Properties), 환경(env)

- 속성(property) : JVM에서 지정된 값
- 환경변수(environment variable) : 서버에 지정된 값
- `static Properties getProperties()` : 자바 속성 값들 가져옴
- `static String getProperty(String key)` : 특정 속성 값 가져옴
- `static String getProperty(String key, String def)` : 특정 속성 값 가져옴, 없으면 def 반환
- `static void setProperties(Properties props)` : 자바 속성 값들 설정
- `static String setProperty(String key, String value)` : 특정 속성 값 설정

```Java
public class GetProperties {
    public static void main(String[] args) {

        System.setProperty("Aespa Leader", "Karina");
        Properties props = System.getProperties();
        Set key = props.keySet();
        Iterator it = key.iterator();
        while (it.hasNext()) {
            String k = (String) it.next();
            String v = System.getProperty(k);
            System.out.println(k + " : " + v);
        }
    }

}
```

- `static Map<String, String> getenv()` : 환경변수 가져옴
- `static String getenv(String name)` : 특정 환경변수 가져옴

```Java
public class GetEnv {
    public static void main(String[] args) {
        Map<String, String> env = System.getenv();
        for (String envName : env.keySet()) {
            System.out.println(envName + " : " + env.get(envName));
        }
    }
}
```

### 운영 코드에 절대 사용 금지 메서드

- `static void gc()` : GC 수행, Java에서 사용하는 메모리를 명시적으로 해제
- `static void exit(int status)` : 현재 수행 중인 JVM을 종료
- `static void runFinalization()` : 참조 해제 작업을 기다리는 모든 객체의 `finalize()` 메서드를 수동으로 호출해야함
    - GC가 알아서 Object 객체의 `finalize()` 메서드를 호출해줌

## `System.currentTimeMillis()`와 `System.nanoTime()`

- `static long currentTimeMillis()` : 1970년 1월 1일 0시 0분 0초부터 현재까지 경과한 시간을 밀리초 단위로 반환
- `static long nanoTime()` : 1970년 1월 1일 0시 0분 0초부터 현재까지 경과한 시간을 나노초 단위로 반환
- Java 성능은 주로 ms 단위로 측정
    - 측정 결과는 ns가 더 정확함
    - **되도록이면 `nanoTime()`을 사용하는 것이 좋음**

```Java
import java.util.ArrayList;
import java.util.HashMap;

public class CompareTimer {

    public static void main(String[] args) {
        CompareTimer compareTimer = new CompareTimer();
        for (int loop = 0; loop < 10; loop++) {
            compareTimer.checkNanoTime();
            compareTimer.checkCurrentTimeMillis();
        }
    }


    private DummyData dummyData;

    public void checkCurrentTimeMillis() {
        long start = System.currentTimeMillis();
        dummyData = timeMakeObjects();
        long end = System.currentTimeMillis();
        System.out.println("elapsed time (ms) : " + (end - start));
    }

    public void checkNanoTime() {
        long start = System.nanoTime();
        dummyData = timeMakeObjects();
        long end = System.nanoTime();
        System.out.println("elapsed time (ns) : " + (end - start));
    }

    private DummyData timeMakeObjects() {
        HashMap<String, String> map = new HashMap<>(1000000);
        ArrayList<String> list = new ArrayList<>(1000000);
        return new DummyData(map, list);
    }


    private class DummyData {
        HashMap<String, String> map;
        ArrayList<String> list;

        public DummyData(HashMap<String, String> map, ArrayList<String> list) {
            this.map = map;
            this.list = list;
        }
    }
}
```

### 메서드 성능 테스트 툴

- JMH (Java Microbenchmark Harness)
    - OpenJDK에서 만든 성능 측정용 라이브러리
- Caliper
- JUnitPerf
- JUnitBench
- ContiPerf

```Java
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class CompareTimerJMH {

    @Benchmark
    public DummyData makeObject() {
        HashMap<String, String> map = new HashMap<>(1000000);
        ArrayList<String> list = new ArrayList<>(1000000);
        return new DummyData(map, list);
    }
}

````

## 정리

- 프로파일링, APM은 적어도 하나씩 사용해야함
- JMH 사용 추천