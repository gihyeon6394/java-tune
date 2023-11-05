package sample.four;

import org.openjdk.jmh.annotations.*;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class SetIterate {

    int LOOP_CNT = 1000;
    Set<String> hashSet;
    Set<String> treeSet;
    Set<String> linkedHashSet;
    String data = "Karina is Beautiful";

    String[] keys;

    String result = null;

    @Setup(Level.Trial)
    public void setUp() {
        hashSet = new java.util.HashSet<>();
        treeSet = new java.util.TreeSet<>();
        linkedHashSet = new java.util.LinkedHashSet<>();

        for (int i = 0; i < LOOP_CNT; i++) {
            String dt = data + i;
            hashSet.add(dt);
            treeSet.add(dt);
            linkedHashSet.add(dt);
        }
    }

    @Benchmark
    public void iterateHashSet() {
        for (String key : hashSet) {
            result = key;
        }
    }

    @Benchmark
    public void iterateTreeSet() {
        for (String key : treeSet) {
            result = key;
        }
    }

    @Benchmark
    public void iterateLinkedHashSet() {
        for (String key : linkedHashSet) {
            result = key;
        }
    }

}
