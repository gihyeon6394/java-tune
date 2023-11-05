# 7. 클래스 정보, 어떻게 알아낼 수 있나?

- reflection 관련 클래스들
- reflection 관련 클래스를 사용한 예
- refletcion 클래스를 잘못 사용한 사례

---

## reflection 관련 클래스들

- `reflection` 패키지 : JVM에 로딩된 클래스, 메서드 정보를 읽을 수 있는 클래스들

### Class 클래스

- 클래스에 대한 정보를 얻을 떄
- Object 클래스의 `getClass()` 메서드를 사용하면 `Class` 클래스의 인스턴스를 얻을 수 있음
- `String getName()` : 클래스 이름 리턴
- `Package getPacakge()` : 패키지 정보 리턴
- `Field[] getFields()` : public 필드 정보 리턴
- `Field getField(String name)` : 특정 이름의 public 필드 정보 리턴
- `Field[] getDeclaredFields()` : 모든 필드 정보 리턴
- `Field getDeclaredField(String name)` : 특정 이름의 필드 정보 리턴
- `Method[] getMethods()` : public 메서드 정보 리턴
    - 상속받은 메서드 포함
- `Method getMethod(String name, Class... parameterTypes)` : 특정 이름과 파라미터 타입의 public 메서드 정보 리턴
- `Method[] getDeclaredMethods()` : 모든 메서드 정보 리턴
- `Method getDeclaredMethod(String name, Class... parameterTypes)` : 특정 이름과 파라미터 타입의 메서드 정보 리턴
- `Constructor[] getConstructors()` : public 생성자 정보 리턴
- `Constructor[] getDeclaredConstructors()` : 모든 생성자 정보 리턴
- `int getModifiers()` : 클래스의 접근 제어자 리턴
- `String toString()` : 클래스 객체를 문자열로 리턴

````
// 현재 클래스 이름 알아내기
Stirng currClassName = this.getClass().getName();
````

### Method 클래스

- 메서드에 대한 정보를 얻을 때
- `Class` 클래스의 `getMethods()`, `getDeclaredMethods()` 메서드를 사용하면 `Method` 클래스의 인스턴스를 얻을 수 있음
- `Class<?> getDeclaringClass()` : 메서드가 선언된 클래스 리턴
- `Class<?> getReturnType()` : 메서드의 리턴 타입 리턴
- `Class<?>[] getParameterTypes()` : 메서드의 매개변수 타입들을 리턴
- `String getName()` : 메서드 이름 리턴
- `int getModifiers()` : 메서드의 접근 제어자 리턴
- `Class<?>[] getExceptionTypes()` : 메서드가 던지는 예외 타입들을 리턴
- `Object invoke(Object obj, Object... args)` : 메서드를 실행
    - `obj` : 메서드를 실행할 객체
    - `args` : 메서드의 매개변수들
- `String toGenericString()` : 타입 매개변수를 포함한 메서드 정보 리턴
- `String toString()` : 메서드 정보를 문자열로 리턴

### Field 클래스

- 필드에 대한 정보를 얻을 때
- `Class` 클래스의 `getFields()`, `getDeclaredFields()` 메서드를 사용하면 `Field` 클래스의 인스턴스를 얻을 수 있음
- `int getModifiers()` : 필드의 접근 제어자 리턴
- `String getName()` : 필드 이름 리턴
- `String toString()` : 필드 정보를 문자열로 리턴

## reflection 관련 클래스를 사용한 예

````java
package sample.seven;

import java.io.IOException;

public class DemoClass {

    private String privateField;
    String field;
    protected String protectedField;
    public String publicField;

    public DemoClass() {
    }

    public DemoClass(String arg) {

    }

    public void publicMethod() throws IOException, Exception {

    }

    public String publicMethod(String s, int i) {
        return "s=" + s + ", i=" + i;
    }

    protected void protectedMethod() {
    }

    private void privateMehtod() {

    }

    void method() {

    }

    public String publicRetMethod() {
        return null;
    }

    public InnerClass getInnerClass() {
        return new InnerClass();
    }

    public static class InnerClass {
    }

}

````

```java
package sample.seven;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class DemoTest {

    public static void main(String[] args) {
        DemoClass dc = new DemoClass(); // reflection 대상 클래스

        DemoTest dt = new DemoTest();
        dt.getClassInfos(dc);
    }

    private void getClassInfos(Object clazz) {
        Class demoClass = clazz.getClass(); // Class 타입의 인스턴스를 얻는다.
        getClassInfo(demoClass);
        getFieldInfo(demoClass);
        getMethodInfo(demoClass);
    }


    private void getClassInfo(Class demoClass) {
        String className = demoClass.getName();
        System.out.println("className = " + className);
        String classCanonicalName = demoClass.getCanonicalName();
        System.out.println("classCanonicalName = " + classCanonicalName);

        String classSimpleName = demoClass.getSimpleName();
        System.out.println("classSimpleName = " + classSimpleName);

        String packageName = demoClass.getPackage().getName();
        System.out.println("packageName = " + packageName);

        String toString = demoClass.toString();
        System.out.println("toString = " + toString);

    }

    private void getFieldInfo(Class demoClass) {
        System.out.println("====================================");
        Field[] field1 = demoClass.getDeclaredFields();
        Field[] field2 = demoClass.getFields();
        System.out.format("field1.length = %d, field2.length = %d\n", field1.length, field2.length);

        for (Field field : field1) {
            String fieldName = field.getName();
            int modifiers = field.getModifiers();
            String modifierStr = Modifier.toString(modifiers);
            String type = field.getType().getSimpleName();
            System.out.format("fieldName = %s, modifierStr = %s, type = %s\n", fieldName, modifierStr, type);

        }

    }

    private void getMethodInfo(Class demoClass) {
        System.out.println("====================================");
        Method[] method1 = demoClass.getDeclaredMethods();
        Method[] method2 = demoClass.getMethods();
        System.out.format("method1.length = %d, method2.length = %d\n", method1.length, method2.length);

        for (Method met1 : method1) {
            String methodName = met1.getName(); // 메소드 이름
            int modifiers = met1.getModifiers(); // 메소드의 접근 제어자
            String modifierStr = Modifier.toString(modifiers); // 접근 제어자를 문자열로 변환
            String returnType = met1.getReturnType().getSimpleName(); // 메소드의 리턴 타입
            Class params[] = met1.getParameterTypes(); // 메소드의 파라미터 타입
            StringBuilder paramStr = new StringBuilder();
            int paramLeng = params.length;

            if (paramLeng != 0) {
                paramStr.append(params[0].getSimpleName())
                        .append(" arg");

                for (int i = 1; i < paramLeng; i++) {
                    paramStr.append(", ")
                            .append(params[i].getName())
                            .append(" arg")
                            .append(i);
                }
            }

            // 메소드의 예외 타입
            Class exceptions[] = met1.getExceptionTypes();
            StringBuilder exceptionStr = new StringBuilder();
            int exceptionLeng = exceptions.length;

            if (exceptionLeng != 0) {
                exceptionStr.append(" throws ")
                        .append(exceptions[0].getSimpleName());

                for (int i = 1; i < exceptionLeng; i++) {
                    exceptionStr.append(", ")
                            .append(exceptions[i].getSimpleName());
                }
            }

            System.out.format("methodName = %s, modifierStr = %s, returnType = %s, paramStr = %s, exceptionStr = %s\n",
                    methodName, modifierStr, returnType, paramStr.toString(), exceptionStr.toString());
        }

    }


}

````

## reflection 클래스를 잘못 사용한 사례

### `.getClass().getName()` vs `instanceof`

```java
package sample.seven;

import org.openjdk.jmh.annotations.*;

import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;

@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class ReflectionTest {

    int LOOP_CNT = 100;
    String result;

    @Benchmark
    public void withEquals() {
        Object src = new BigDecimal("5");
        for (int i = 0; i < LOOP_CNT; i++) {
            result = src.getClass().getName().equals("java.math.BigDecimal") ? "true" : "false";
        }
    }

    @Benchmark
    public void withInstanceof() {
        Object src = new BigDecimal("5");
        for (int i = 0; i < LOOP_CNT; i++) {
            result = src instanceof BigDecimal ? "true" : "false";
        }

    }

}
```



