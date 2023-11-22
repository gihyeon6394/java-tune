# 11. JSP와 서블릿, Spring에서 발생할 수 있는 여러 문제점

- JSP와 Servlet의 기본적인 동작 원리는 꼭 알아야 한다
- 적절한 include 사용하기
- 자바 빈즈, 잘 쓰면 약 못쓰면 독
- 태그 라이브러리도 잘 써야 한다
- 스프링 프레임워크 간단 정리
- 스프링 프레임워크를 사용하면서 발생할 수 있는 문제점들

---

## JSP와 서블릿, Spring에서 발생할 수 있는 여러 문제점

### JSP life cycle

1. JSP, URL 호출
2. 페이지 번역
3. JSP 페이지 compile
4. class load
5. instance 생성
6. jspInit() 호출
7. _jspService() 호출
8. jspDestroy() 호출

- 이미 Compile되어있으면 2~4번 과정 생략

### Servlet life cycle

1. Servlet 생성
    - Servlet 객체가 자동으로 생성되고 초기화 되거나,
    - 사용자가 해당 Servlet을 처음 호출했을 때 생성되고 초기화 됨
2. Service Requests : Servlet 사용 가능 상태로 대기
3. destory : Servlet이 더 이상 필요 없을 때
4. unload : Servlet 객체가 메모리에서 제거됨

- Servlet은 Singleton으로 관리됨

## 적절한 include 사용하기

- include : 하나의 jsp에 다른 jsp를 혼합하는 방법
- include directive : `<%@ include file="include.jsp" %>`
- include action : `<jsp:include page="include.jsp" />`
    - 더 느림 (약 30배)

## 자바 빈즈, 잘 쓰면 약 못쓰면 독

- Transfer Object를 사용해 Java Beans 종류를 줄이자

## 태그 라이브러리도 잘 써야 한다

- JSP에서 공통으로 쓰는 코드를 클래스로 만들어 태그로 사용하는 방법
- 태그 라이브러리에서 처리량이 많으면 성능 저하가 발생할 수 있음

```xml

<webapp>
    <taglib>
        <taglib-uri>http://www.example.com/tags</taglib-uri>
        <taglib-location>/WEB-INF/tags/mytags.tld</taglib-location>
    </taglib>
</webapp>
``` 

```xml
<?xml version="1.0" encoding="UTF-8"?>
<taglib xmlns="http://java.sun.com/JSP/Page"
        xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
        xsi:schemaLocation="http://java.sun.com/xml/ns/j2ee/web-jsptaglibrary_2_0.xsd"
        version="2.0">
    <tlib-version>1.0</tlib-version>
    <short-name>mytags</short-name>
    <uri>http://www.example.com/tags</uri>
    <tag>
        <name>hello</name>
        <tag-class>com.example.tags.HelloTag</tag-class>
        <bodycontent>empty</bodycontent>
    </tag>
</taglib>
```

```jsp
<%@ taglib prefix="my" uri="http://www.example.com/tags" %>

<my:hello>
    <jsp:attribute name="name">World</jsp:attribute>
    <%=bodycontent%>
</my:hello>

```

## 스프링 프레임워크 간단 정리

- 스프링 프레임워크는 DI와 AOP를 지원하는 프레임워크
- 복잡한 application도 POJO로 개발 가능
    - JSP, Servlet은 POJO 아님
- Servlet을 개발하려면 반드시 HttpServlet을 확장해야함
- Spring을 사용하면 확장하지 않아도 웹 요청 처리 가능

### 스프링의 핵심 기술

- Dependency Injection
- Aspect Oriented Programming
- Portable Service Abstraction

#### Dependency Injection (DI)

```java
public class A {
    private B b = new B();
}
```

- A가 B에 의존하고 있음
- B를 변경하기 힘듬

```java
public class A {
    private B b;

    public A(B b) {
        this.b = b;
    }
}
```

- A가 B에 의존하고 있음
- Spring이 B에 대한 의존성을 주입함 (XML, @ 등으로)
    - 생성자, setter, field 주입 등

#### Aspect Oriented Programming (AOP)

- 공통적으로 사용하는 기능을 분리하여 관리하는 것 (트랜잭션, 로깅, 보완 체크코드 등)
- Java의 AOP framework : AspectJ
- Spring은 AspectJ를 지원, 연동함

#### Portable Service Abstraction (PSA)

- Spring은 다양한 기술을 추상화하여 제공
- 라이브러리 A -> B 로 전환하는 경우 용이함
- 제대로 개발되었다면 PSA를 활용해 라이브러리르 간단히 전환 가능 (e.g. JPA -> MyBatis)

## 스프링 프레임워크를 사용하면서 발생할 수 있는 문제점들

- proxy e.g. `@Transactional`
- 개발자가 직접 작성한 AOP 코드는 반드시 부하 테스트 필요

```java
public class SampleController {
    @RequestMapping("/sample/{id}")
    public String sample(@PathVariable("id") String id) {
        return "redirect:/sample/" + id; // cache
        // return new RedirectView("/sample/" + id); // no cache
    }
}
```

- 동일한 뷰 객체를 캐싱해두면 성능 향상에 도움이 됨 (`InternalResourceViewResolver`)