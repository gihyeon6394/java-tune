package sample.seven;

import org.openjdk.jmh.annotations.*;

import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;

@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class ReflectionTest {

    int LOOP_CNT = 100;
    String result;

    @Benchmark
    public void withEquals() {
        Object src = new BigDecimal("5");
        for (int i = 0; i < LOOP_CNT; i++) {
            result = src.getClass().getName().equals("java.math.BigDecimal") ? "true" : "false";
        }
    }

    @Benchmark
    public void withInstanceof() {
        Object src = new BigDecimal("5");
        for (int i = 0; i < LOOP_CNT; i++) {
            result = src instanceof BigDecimal ? "true" : "false";
        }

    }

}
