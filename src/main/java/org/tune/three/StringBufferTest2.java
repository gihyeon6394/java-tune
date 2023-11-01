package org.tune.three;

public class StringBufferTest2 {
    public static void main(String[] args) {

        StringBuffer sb = new StringBuffer();
        sb.append("Aespa");
        sb.append(" is Best. ");

        sb.append("karina")
                .append(" is ")
                .append("the best.");

        // not good!
        sb.append("winter" + " is " + "the best.");
    }
}
