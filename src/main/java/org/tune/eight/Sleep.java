package org.tune.eight;

public class Sleep extends Thread {

    @Override
    public void run() {
        try {
            Thread.sleep(10000); // 10 seconds 대기
        } catch (InterruptedException e) {
            System.out.println("interrupted"); // interrupted : 다른 스레드가 현재 스레드를 중단시킴
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Sleep s = new Sleep();
        s.start(); // 스레드 시작

        int cnt = 0;

        while (cnt < 5) {
            try {
                s.join(1000); // 1 second 대기하면서 thread가 죽기를 기다림
                cnt++;
                System.out.format("%d seconds waited\n", cnt);

                // 스레드가 살아있으면 중단
                if (s.isAlive()) {
                    s.interrupt(); // 스레드 중단
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
