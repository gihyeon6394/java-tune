package org.tune.seven;

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
