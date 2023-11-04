package org.tune.three;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Arrays;
import java.util.Hashtable;
import java.util.SortedSet;
import java.util.concurrent.TimeUnit;

public class Tmp {
    final String aVal = "Karina is Beautiful ";
    public double nano = 1000000.0;


    @DisplayName("Stirng")
    @ParameterizedTest
    @ValueSource(ints = {1000, 10000, 50000, 100000})
    public void test_string(int innerLoop) {

        long start = System.nanoTime();


        String a = new String();
        for (int j = 0; j < innerLoop; j++) {
            a += aVal;
        }
        long end = System.nanoTime();
        double diff = end - start;
        System.out.println("diff = " + diff);
        System.out.println("diff = " + diff / 1000000);
    }


    @DisplayName("StringBuffer")
    @ParameterizedTest
    @ValueSource(ints = {1000, 10000, 50000, 100000})
    public void test_stringBuffer(int innerLoop) {

        long start = System.nanoTime();

        StringBuffer a = new StringBuffer();
        for (int j = 0; j < innerLoop; j++) {
            a.append(aVal);
        }

        long end = System.nanoTime();
        double diff = end - start;
        System.out.println("diff = " + diff);
        System.out.println("diff = " + diff / 1000000);

    }


    @DisplayName("StringBuilder")
    @ParameterizedTest
    @ValueSource(ints = {1000, 10000, 50000, 100000})
    public void test_stringBuilder(int innerLoop) {

        long start = System.nanoTime();

        StringBuilder a = new StringBuilder();
        for (int j = 0; j < innerLoop; j++) {
            a.append(aVal);
        }
        long end = System.nanoTime();
        double diff = end - start;
        System.out.println("diff = " + diff);
        System.out.println("diff = " + diff / 1000000);

    }

}
