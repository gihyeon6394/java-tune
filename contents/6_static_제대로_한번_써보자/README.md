# 6. static 제대로 한번 써보자

- static의 특징
- static 잘 활용하기
- static 잘못 쓰면 이렇게 된다
- static과 메모리 릭

---

## static의 특징

````
public class VariableTypes{
    int instance Variable;
    static int classVariable;
    void method(){
        int localVariable;
    }
}
````

- `classVariable` : 클래스 변수
    - 객체의 변수가 아님
    - `VariableTypes` 인스턴스와 상관 없이, `classVariable`은 하나만 존재
- 하나의 JVM, WAS 인스턴스에서는 같은 주소에 존재
- GC 대상 아님

```java
public class StaticBasicSample {

    public static int instaticInt = 0;

    public static void main(String[] args) {
        StaticBasicSample sample1 = new StaticBasicSample();
        sample1.instaticInt++;
        StaticBasicSample sample2 = new StaticBasicSample();
        sample2.instaticInt++;
        System.out.println(sample1.instaticInt);  // 2
        System.out.println(sample2.instaticInt);  // 2
    }
}

```

### static 초기화 블록

- 클래스 변수를 초기화하는 방법

```java

public class StaticBlockSample {

    static String staticVal;

    static {
        staticVal = "Karina is the best";
        staticVal = StaticBlockSample.staticVal + " in the world";
    }

    public static void main(String[] args) {
        System.out.println(StaticBlockSample.staticVal); // I love Karina 
    }

    static {
        staticVal = "I love Karina";
    }
}

```

- 클래스 로딩시 static 블록을 순차적으로 실행

## static 잘 활용하기

### 자주 사용하고 절대 변하지 않는 변수는 final static으로 선언하자

- 1 byte 이상의 객체가 GC 대상에서 제외됨
- 간단한 쿼리 문자열, JNDI 이름, 코드성 데이터등

````
try{
    Template template = Velocity.getTemplate("template.vm");
    ....
}

// static으로 성능 개선

static Template template
static {
    try {
        template = Velocity.getTemplate("template.vm");
        ...
    }   
}

````

### 설정 파일 정보도 static 으로 관리하자

- 설정 파일 정보는 자주 사용되고, 변경될 일이 거의 없음
- 클래스 객체 생성마다 설정 파일 로딩할 필요 없음
- static으로 데이터를 읽어서 관리

### 코드성 데이터는 DB에서 한 번만 읽자

````java
// code를 DB에서 읽어서 관리하는 클래스
public class CodeManager {

    private HashMap<String, String> codeMap;
    private static CodeDAO cDAO;
    private static CodeManager cm;

    static {
        cDAO = new CodeDAO();
        cm = new CodeManager();
        if (!cm.getCodes()) {
            // 에러 처리
        }
    }

    // 객체 생성 방지
    private CodeManager() {
    }

    // 싱글톤 패턴
    public static CodeManager getInstance() {
        return cm;
    }

    public boolean getCodes() {
        try {
            codeMap = cDAO.getCodes();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean updateCodes() {
        return cm.getCodes();
    }

    public String getCodeVal(String code) {
        return codeMap.get(code);
    }
}
````

- `CodeManager` 클래스는 `CodeDAO` 클래스를 이용해서 DB에서 코드 정보를 읽어서 관리
- `CodeManager` 클래스는 싱글톤 패턴으로 구현
- `updateCodes()` 메서드를 이용해서 코드 정보를 갱신할 수 있음
- 여러 was 인스턴스 환경에서는 `updateCodes()` 메서드를 이용해서 코드 정보를 갱신하는 번거로움
    - memcached, Ehcache 등을 이용해서 해결

## static 잘못 쓰면 이렇게 된다

````java

import java.io.FileReader;
import java.util.HashMap;

public class BadQueryManager {

    private static String queryURL = null; // static으로 선언

    public BadQueryManager(String badUrl) {
        queryURL = badUrl;
    }

    // queryURL의 내용을 읽어서 HashMap에 저장, idSql을 이용해서 SQL문을 읽어옴
    public static String getSql(String idSql) {
        try {
            HashMap<String, String> docs = new FileReader().read(queryURL);
            return docs.get(idSql);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
````

- `queryURL` 변수는 static으로 선언 (JVM에 하나만 존재)
- 요청 thread에 따라 `queryURL` 변수의 값이 변경될 수 있음

## static과 메모리 릭

- static은 GC 대상이 아님
- static 객체 용량이 많아짐 -> `outOfMemoryError` 발생 -> JVM 재시작 필요
- Memory Leak : 더이상 사용가능한 메모리가 없어져가는 현상
- HeapDump : 메모리 덤프 파일
    - JDK/bin 의 jmap.exe를 이용해서 HeapDump 생성