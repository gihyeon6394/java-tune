package sample.ten;

import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;

@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class StringFormat {

    private String a = "aaa", b = "bbb", c = "ccc", d = "ddd";
    private long e = 1L, f = 2L, g = 3L, h = 4L;

    private String data;

    private int LOOP_CNT = 10;

    @Benchmark
    public void timeStringAdd() {
        for (int reps = 0; reps < LOOP_CNT; reps++) {
            data = a + " " + b + " " + c + " " + d + " " + e + " " + f + " " + g + " " + h;
        }
    }

    @Benchmark
    public void timeStringFormat() {
        for (int reps = 0; reps < LOOP_CNT; reps++) {
            data = String.format("%s %s %s %s %d %d %d %d", a, b, c, d, e, f, g, h);
        }
    }

}
