package org.tune.two;

import java.util.ArrayList;
import java.util.HashMap;

public class CompareTimer {

    public static void main(String[] args) {
        CompareTimer compareTimer = new CompareTimer();
        for (int loop = 0; loop < 10; loop++) {
            compareTimer.checkNanoTime();
            compareTimer.checkCurrentTimeMillis();
        }
    }


    private DummyData dummyData;

    public void checkCurrentTimeMillis() {
        long start = System.currentTimeMillis();
        dummyData = timeMakeObjects();
        long end = System.currentTimeMillis();
        System.out.println("elapsed time (ms) : " + (end - start));
    }

    public void checkNanoTime() {
        long start = System.nanoTime();
        dummyData = timeMakeObjects();
        long end = System.nanoTime();
        System.out.println("elapsed time (ns) : " + (end - start));
    }

    private DummyData timeMakeObjects() {
        HashMap<String, String> map = new HashMap<>(1000000);
        ArrayList<String> list = new ArrayList<>(1000000);
        return new DummyData(map, list);
    }


}

