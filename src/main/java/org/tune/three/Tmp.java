package org.tune.three;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

public class Tmp {

    @Test
    @DisplayName("Stirng")
    public void test_string() {
        long[] times = new long[10];
        final String aVal = "abcde";
        for (int outLoop = 0; outLoop < 10; outLoop++) {
            long start = System.nanoTime();
            String a = new String();
            for (int i = 0; i < 100000; i++) {
                a += aVal;
            }
            long end = System.nanoTime();
            times[outLoop] = end - start;
        }
        // average time
        System.out.println("av : " + Arrays.stream(times).average().getAsDouble());
    }

    @Test
    @DisplayName("StringBuffer")
    public void test_stringBuffer() throws InterruptedException {
        Thread.sleep(10000);
        long[] times = new long[10];
        final String aVal = "abcde";
        for (int outLoop = 0; outLoop < 10; outLoop++) {
            long start = System.nanoTime();

            StringBuffer a = new StringBuffer();
            for (int i = 0; i < 100000; i++) {
                a.append(aVal);
            }
            long end = System.nanoTime();
            times[outLoop] = end - start;
        }

        System.out.println("av : " + Arrays.stream(times).average().getAsDouble());

    }

    @Test
    @DisplayName("StringBuilder")
    public void test_stringBuilder() {
        long[] times = new long[10];
        final String aVal = "abcde";
        for (int outLoop = 0; outLoop < 10; outLoop++) {
            long start = System.nanoTime();

            StringBuilder a = new StringBuilder();
            for (int i = 0; i < 100000; i++) {
                a.append(aVal);
            }
            long end = System.nanoTime();
            times[outLoop] = end - start;
        }

        System.out.println("av : " + Arrays.stream(times).average().getAsDouble());

    }

    public void afterJDK5() {
        String str1 = "super";
        String str2 = "Aespa" + " is " + str1 + "Best";
    }
}
