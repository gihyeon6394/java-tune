package org.tune.eighteen;

import java.util.ArrayList;
import java.util.List;

public class GCMaker {

    public static void main(String[] args) throws InterruptedException {
        GCMaker maker = new GCMaker();
        for (int loop = 0; loop < 120; loop++) {
            maker.makeObject();
            Thread.sleep(1000); // 1초 대기
            System.out.println(".");
        }
    }

    public void makeObject() {
        Integer[] intArr = new Integer[1024000];
        List<Integer> list = new ArrayList<>(1024000);
        for (int loop = 0; loop < 1024000; loop++) {
            intArr[loop] = loop;
            list.add(loop);
        }
    }
}
