package org.tune.two;

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
