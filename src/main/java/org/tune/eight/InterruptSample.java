package org.tune.eight;

public class InterruptSample {

    public static void main(String[] args) throws InterruptedException {

        InfinitThread infinitThread = new InfinitThread();
        infinitThread.start();
        Thread.sleep(2000); // sleep for 2 seconds
        System.out.println("isInterrupted: " + infinitThread.isInterrupted());
        infinitThread.interrupt();
        System.out.println("isInterrupted: " + infinitThread.isInterrupted());
    }
}
