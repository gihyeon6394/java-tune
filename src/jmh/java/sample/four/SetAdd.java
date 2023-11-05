package sample.four;

import org.openjdk.jmh.annotations.*;

import java.util.Set;
import java.util.concurrent.TimeUnit;

@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class SetAdd {

    int LOOP_CNT = 1000;
    Set<String> set;
    String data = "Karina is Beautiful";

    @Benchmark
    public void addHashSet() {
        set = new java.util.HashSet<>();
        for (int i = 0; i < LOOP_CNT; i++) {
            set.add(data + i);
        }
    }
    @Benchmark
    public void addHashSetWithInitialCapacity() {
        set = new java.util.HashSet<>(LOOP_CNT);
        for (int i = 0; i < LOOP_CNT; i++) {
            set.add(data + i);
        }
    }

    @Benchmark
    public void addTreeSet() {
        set = new java.util.TreeSet<>();
        for (int i = 0; i < LOOP_CNT; i++) {
            set.add(data + i);
        }
    }

    @Benchmark
    public void addLinkedHashSet() {
        set = new java.util.LinkedHashSet<>();
        for (int i = 0; i < LOOP_CNT; i++) {
            set.add(data + i);
        }
    }

}
