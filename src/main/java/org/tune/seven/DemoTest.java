package org.tune.seven;

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
