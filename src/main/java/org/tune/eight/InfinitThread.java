package org.tune.eight;

public class InfinitThread extends Thread {

    int val = Integer.MIN_VALUE;

    public void run() {
        while (true) {
            val++;

            if (val == Integer.MAX_VALUE) {
                val = Integer.MIN_VALUE;
                System.out.println("MAX_VALUE reached!");
            }

            try {
                Thread.sleep(0, 1); // 1 nanosecond sleep
            } catch (InterruptedException e) {
                break;
            }
        }
    }
}
