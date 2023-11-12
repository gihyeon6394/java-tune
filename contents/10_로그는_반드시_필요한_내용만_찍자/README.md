# 10. 로그는 반드시 필요한 내용만 찍자

- System.out.println()의 문제점
- System.out.fomat() 메서드
- 로그를 더 간결하게 처리하는 방법
- 로거 사용 시의 문제점
- 로그를 깔끔하게 처리하게 도와주는 slf4j와 LogBack
- 예외처리는 이렇게

---

## System.out.println()의 문제점

- 로그를 남기는 것은 blocking
    - disk에 로그를 남기는 것은 Disk RPM에 의존
- `System.out.println()` 은 개발용
    - 운영시에는 볼수도 없어 사용하지 못함
- 운영 서버의 시스템 로그를 지우는 것만으로도 성능 향상

## System.out.fomat() 메서드

````
System.out.format("Hello %s\n", "World");
````

- `format(String format, Object... args)` : 문자열을 지정된 형식으로 프린트
- `format(Locale l, String format, Object... args)` : 문자열을 지정된 형식으로 프린트, 앞에 지역 정보 포함
    - 지역에 따라 데이터 형태 달라짐

```java
package sample.ten;

import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;

@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class StringFormat {

    private String a = "aaa", b = "bbb", c = "ccc", d = "ddd";
    private long e = 1L, f = 2L, g = 3L, h = 4L;

    private String data;

    private int LOOP_CNT = 10;

    @Benchmark
    public void timeStringAdd() {
        for (int reps = 0; reps < LOOP_CNT; reps++) {
            data = a + " " + b + " " + c + " " + d + " " + e + " " + f + " " + g + " " + h;
        }
    }

    @Benchmark
    public void timeStringFormat() {
        for (int reps = 0; reps < LOOP_CNT; reps++) {
            data = String.format("%s %s %s %s %d %d %d %d", a, b, c, d, e, f, g, h);
        }
    }

}
```

```text
Benchmark                      Mode  Cnt   Score    Error  Units
StringFormat.timeStringAdd      avgt    5  ≈ 10⁻³           ms/op
StringFormat.timeStringFormat  avgt    5   0.005 ±  0.001  ms/op
```

- `timeStringAdd()` : 문자열을 더하는 방식
- `timeStringFormat()` : `String.format()` 메서드를 사용하는 방식
- `timeStringAdd()`가 더 빠름
- `timeStringFormat()` 는 내부적으로 `Formatter` 클래스를 사용해 파싱하는 과정이 필요

## 로그를 더 간결하게 처리하는 방법

```java
public class SimpleLogger {
    public static final boolean printFlag = true;

    public static void logging(String msg) {
        // printFlag가 false면 compile 시에 아예 코드가 사라짐
        if (LogFlag.printFlag) {
            System.out.println(msg);
        }
    }
}
```

- `printFlag` 를 `false` 로 설정하면 `if` 문이 사라지기 때문에 성능 저하가 없음
- 단점 : `printFlag` 를 바꾸려면 재컴파일 필요

## 로거 사용 시의 문제점

- 로그 사용여부와 상관 없이 `SimpleLogger` 객체를 생성해야함

````
logger.info("query : " + query);
logger.info("result :" + resultMap);

// 불필요한 객체 생성 방지
if(logger.isLoggable(Level.INFO){
    logger.info("query : " + query);
    logger.info("result :" + resultMap);
}
````

- 괄호 안의 값을 문자열로 변환하는 과정이 필요

## 로그를 깔끔하게 처리하게 도와주는 slf4j와 LogBack

- slf4j (Simple Logging Facade for Java)
- 로그를 간단히 처리해주는 프레임워크

```java
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Tmp {


    final Logger logger = LoggerFactory.getLogger(Tmp.class);

    public static void main(String[] args) {
        logger.debug("Hello World");
        logger.info("Hello World {}", "with more info");
    }
}
```

- 문자열을 더할 필요 없음
- 로그를 출력하지 않을 경우 더하기 연산 발생하지 않음
- LogBack
    - 예외 스택 정보 출력 시 해당 클래스의 참조 라이브러리를 로깅

## 예외처리는 이렇게

```
try {
    // 예외가 발생할 수 있는 코드
} catch (Exception e) {
   e.printStackTrace();
}
```

- `printStackTrace()` 는 예외가 발생한 위치를 찾기 위해 사용
- 스택 정보를 출력
    - 스택 정보 : 어떤 클래스의 어떤 메서드가 어떤 메서드를 호출했는지에 대한 정보
- 운영 서버에서 가독성이 떨어지는 로깅 방법
    - 여러 스레드의 스택정보가 섞여서 출력
- 성능 부하
    - 최대 100개의 스택정보르르 출력하게 됨

```
try{
    // 예외가 발생할 수 있는 코드
} catch (Exception e) {
    StackTraceElement[] ste = e.getStackTrace();
    String className = ste[0].getClassName();
    String methodName = ste[0].getMethodName();
    int lineNumber = ste[0].getLineNumber();
    String fileName = ste[0].getFileName();
    logger.error("Exception : {} {} {} {}", className, methodName, lineNumber, fileName);
}
```

- `getStackTrace()` 를 사용해 스택정보를 가져온뒤 적절한 메시지로 가공해 출력