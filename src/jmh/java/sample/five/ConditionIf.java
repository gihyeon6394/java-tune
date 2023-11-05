package sample.five;

import org.openjdk.jmh.annotations.*;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
public class ConditionIf {

    int LOOP_CNT = 1000;

    String curr;

    // JIT compiler가 최적화를 못하게 막는다.
    private void resultProcess(String dummy) {
        curr = dummy;
    }

    @Benchmark
    public void randomOnly() {
        Random rand = new Random();
        int data = 1000 + rand.nextInt();
        for (int i = 0; i < LOOP_CNT; i++) {
            resultProcess("dummy");
        }
    }

    @Benchmark
    public void if10() {
        Random rand = new Random();
        String result = null;
        int data = 1000 + rand.nextInt();

        for (int i = 0; i < LOOP_CNT; i++) {
            if (data < 50) {
                result = "50";
            } else if (data < 100) {
                result = "100";
            } else if (data < 150) {
                result = "150";
            } else if (data < 200) {
                result = "200";
            } else if (data < 250) {
                result = "250";
            } else if (data < 300) {
                result = "300";
            } else if (data < 350) {
                result = "350";
            } else if (data < 400) {
                result = "400";
            } else if (data < 450) {
                result = "450";
            } else if (data < 500) {
                result = "500";
            } else if (data < 550) {
                result = "550";
            } else if (data < 600) {
                result = "600";
            } else if (data < 650) {
                result = "650";
            } else if (data < 700) {
                result = "700";
            } else if (data < 750) {
                result = "750";
            } else if (data < 800) {
                result = "800";
            } else if (data < 850) {
                result = "850";
            } else if (data < 900) {
                result = "900";
            } else if (data < 950) {
                result = "950";
            } else {
                result = "over";
            }
        }
        resultProcess(result);

    }

    @Benchmark
    public void if50() {
        Random rand = new Random();
        String result = null;
        int data = 1000 + rand.nextInt();

        for (int i = 0; i < LOOP_CNT; i++) {
            if (data < 50) {
                result = "50";
            } else if (data < 100) {
                result = "100";
            } else if (data < 150) {
                result = "150";
            } else if (data < 200) {
                result = "200";
            } else if (data < 250) {
                result = "250";
            } else if (data < 300) {
                result = "300";
            } else if (data < 350) {
                result = "350";
            } else if (data < 400) {
                result = "400";
            } else if (data < 450) {
                result = "450";
            } else if (data < 500) {
                result = "500";
            } else if (data < 550) {
                result = "550";
            } else if (data < 600) {
                result = "600";
            } else if (data < 650) {
                result = "650";
            } else if (data < 700) {
                result = "700";
            } else if (data < 750) {
                result = "750";
            } else if (data < 800) {
                result = "800";
            } else if (data < 850) {
                result = "850";
            } else if (data < 900) {
                result = "900";
            } else if (data < 950) {
                result = "950";
            } else if (data < 1000) {
                result = "1000";
            } else if (data < 1050) {
                result = "1050";
            } else if (data < 1100) {
                result = "1100";
            } else if (data < 1150) {
                result = "1150";
            } else if (data < 1200) {
                result = "1200";
            } else if (data < 1250) {
                result = "1250";
            } else if (data < 1300) {
                result = "1300";
            } else if (data < 1350) {
                result = "1350";
            } else if (data < 1400) {
                result = "1400";
            } else if (data < 1450) {
                result = "1450";
            } else if (data < 1500) {
                result = "1500";
            } else if (data < 1550) {
                result = "1550";
            } else if (data < 1600) {
                result = "1600";
            } else if (data < 1650) {
                result = "1650";
            } else if (data < 1700) {
                result = "1700";
            } else if (data < 1750) {
                result = "1750";
            } else if (data < 1800) {
                result = "1800";
            } else if (data < 1850) {
                result = "1850";
            } else if (data < 1900) {
                result = "1900";
            } else if (data < 1950) {
                result = "1950";
            } else if (data < 2000) {
                result = "2000";
            } else if (data < 2050) {
                result = "2050";
            } else if (data < 2100) {
                result = "2100";
            } else if (data < 2150) {
                result = "2150";
            } else if (data < 2200) {
                result = "2200";
            } else if (data < 2250) {
                result = "2250";
            } else if (data < 2300) {
                result = "2300";
            } else if (data < 2350) {
                result = "2350";
            } else if (data < 2400) {
                result = "2400";
            } else if (data < 2450) {
                result = "2450";
            } else if (data < 2500) {
                result = "2500";
            } else if (data < 2550) {
                result = "2550";
            } else if (data < 2600) {
                result = "2600";
            } else if (data < 2650) {
                result = "2650";
            } else if (data < 2700) {
                result = "2700";
            } else if (data < 2750) {
                result = "2750";
            } else if (data < 2800) {
                result = "2800";
            } else if (data < 2850) {
                result = "2850";
            } else if (data < 2900) {
                result = "2900";
            } else if (data < 2950) {
                result = "2950";
            } else if (data < 3000) {
                result = "3000";
            } else if (data < 3050) {
                result = "3050";
            } else if (data < 3100) {
                result = "3100";
            } else if (data < 3150) {
                result = "3150";
            } else if (data < 3200) {
                result = "3200";
            } else if (data < 3250) {
                result = "3250";
            } else if (data < 3300) {
                result = "3300";
            } else if (data < 3350) {
                result = "3350";
            } else if (data < 3400) {
                result = "3400";
            } else if (data < 3450) {
                result = "3450";
            } else if (data < 3500) {
                result = "3500";
            } else if (data < 3550) {
                result = "3550";
            } else if (data < 3600) {
                result = "3600";
            } else if (data < 3650) {
                result = "3650";
            } else if (data < 3700) {
                result = "3700";
            } else if (data < 3750) {
                result = "3750";
            } else if (data < 3800) {
                result = "3800";
            } else if (data < 3850) {
                result = "3850";
            } else if (data < 3900) {
                result = "3900";
            } else if (data < 3950) {
                result = "3950";
            } else if (data < 4000) {
                result = "4000";
            } else if (data < 4050) {
                result = "4050";
            } else if (data < 4100) {
                result = "4100";
            } else if (data < 4150) {
                result = "4150";
            } else if (data < 4200) {
                result = "4200";
            } else if (data < 4250) {
                result = "4250";
            } else if (data < 4300) {
                result = "4300";
            } else if (data < 4350) {
                result = "4350";
            } else if (data < 4400) {
                result = "4400";
            } else if (data < 4450) {
                result = "4450";
            } else if (data < 4500) {
                result = "4500";
            } else if (data < 4550) {
                result = "4550";
            } else if (data < 4600) {
                result = "4600";
            } else if (data < 4650) {
                result = "4650";
            } else if (data < 4700) {
                result = "4700";
            } else if (data < 4750) {
                result = "4750";
            } else if (data < 4800) {
                result = "4800";
            } else if (data < 4850) {
                result = "4850";
            } else if (data < 4900) {
                result = "4900";
            } else if (data < 4950) {
                result = "4950";
            } else {
                result = "over";
            }

        }

        resultProcess(result);

    }

}
