# 12. DB를 사용하면서 발생 가능한 문제점들

- DB Connection과 Connection Pool, DataSource
- DB를 사용할 때 닫아야하는 것들
- JDK 7에서 등장한 AutoCloseable 인터페이스
- ResultSet.last()
- JDBC를 사용하면서 유의할 만한 몇가지 팁

---

## DB Connection과 Connection Pool, DataSource

- JDBC Interface
- `java.sql.` 인터페이스를 각 DB 벤더에 맞게 구현해야함

```java
public class Foo {
    public void connectDB() {
        try {
            // JDBC 드라이버 로딩
            Class.forName("com.jdbc.jdriver.OracleDriver");
            // DB 연결 (가장 느린 부분, DB-WAS network delay)
            Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "scott", "tiger");
            // PreparedStatement 생성
            PreparedStatement pstmt = conn.prepareStatement("select * from emp");
            // 쿼리 실행
            ResultSet rs = pstmt.executeQuery(); // cursor를 이동하며 탐색 (실제 모든 건수를 가지고 있지 않고, DB 벤더사 구현에 따라 다름)
        } catch (ClassNotFoundException e) {
            System.out.println("드라이버 로딩 실패");
            throw e;
        } catch (SQLException e) {
            System.out.println("SQL Error : " + e.getMessage());
            throw e;
        } finally {
            // 자원 반납
            try {
                rs.close();
            } catch (Exception e) {
            }
            try {
                pstmt.close();
            } catch (Exception e) {
            }
            try {
                conn.close();
            } catch (Exception e) {
            }
        }
    }
}
```

#### DB Connection Pool

- DB Connection을 미리 만들어 놓고, 필요할 때마다 가져다 쓰는 것
- WAS에서 관리
- Datasource를 사용해 JNDI로 호출

> #### DataSource? DB Connection Pool?
>
> - DataSource는 DB Connection Pool을 포함하는 객체
> - Datasource는 JDK 1.4부터 나온 표준
> - DB Connection Pool은 자바 표준 없음 (WAS 벤더사에 따라 다름)

#### Statement와 PreparedStatement

- Statement의 확장 `PreparedStatement`
- `CallableStatement` : 프로시저 호출
- PreparedStatement와 Statement의 가장 큰 차이점 : 캐시

#### 최초 사용시

1. 쿼리 문장 분석
2. compile
3. 실행

- PreparedStatement는 최초 한번만 수행후 캐싱해둠

#### 쿼리 수행 : `executeQuery()`, `executeUpdate()`, `execute()`

- `executeQuery()` : select
- `executeUpdate()` : DML, DDL
- `execute()` : 모든 쿼리
- `ResultSet` 인터페이스 : 쿼리 수행 결과
    - `next()` : 커서를 다음으로 이동하며 탐색
    - `first()`, `last()` : 커서를 처음, 마지막으로 이동

## DB를 사용할 때 닫아야하는 것들

- 먼저 얻은 것을 나중에 닫는다

### ResultSet 닫기

- `close()` : 명시적으로 닫기
    - 최대한 빨리 DB, JDBC 리소스를 해제하기 위해 사용
- GC 대상
- 관련된 Statement가 닫히면 자동으로 닫힘

### Statement 닫기

- `close()` : 명시적으로 닫기
- GC 대상

### Connection 닫기

- `close()` : 명시적으로 반납 (Connection Pool에 반납)
- GC 대상
- 에러가 발생한 경우

## JDK 7에서 등장한 AutoCloseable 인터페이스

- `close()` 메소드를 가진 인터페이스
- `try-with-resources` 구문으로 관리되는 객체를 자동으로 `close()` 해줌
- InterruptedException 비권장
- `close()`를 두번 이상 호출할 경우 눈에 보이는 부작용이 나타나도록 해야함

````
// try-catch
public String readFile(String fileName) throws IOException {
    BufferedReader br = null;
    try {
        br = new BufferedReader(new FileReader(fileName));
        return br.readLine();
    } finally {
        if (br != null) {
            br.close();
        }
    }
}

// try-with-resources
public String readFile(String fileName) throws IOException {
    try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
        return br.readLine();
    }
}
````

## ResultSet.last()

- `last()` : 결과 Cursor를 맨 끝으로 옮겨라
- 데이터 건수를 위해서라면 `count(*)`를 사용하는 것이 좋음
- `last()` 수행시간은 데이터 건수에 비례

## JDBC를 사용하면서 유의할 만한 몇가지 팁

- `setAutoCommit()`은 필요할 때만
    - 여러번의 select 쿼리 수행 시 성능 저하
- 배치성 작업에는 `executeBatch()` 사용, JDBC 호출 회수 감소
    - `addBatch()` : 배치에 쿼리를 추가
    - `executeBatch()` : 배치에 쿼리를 수행
- `setFetchSize()` :  한번에 가져오는 열의 수 설정
