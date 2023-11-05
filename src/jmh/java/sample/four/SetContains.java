package sample.four;

import org.openjdk.jmh.annotations.*;
import org.tune.four.RandomKeyUtil;

import java.util.Set;
import java.util.concurrent.TimeUnit;

@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
public class SetContains {

    int LOOP_CNT = 1000;
    Set<String> hashSet;
    Set<String> treeSet;
    Set<String> linkedHashSet;


    String data = "Karina is Beautiful";
    String[] keys;

    @Setup(Level.Trial)
    public void setUp() {
        hashSet = new java.util.HashSet<>();
        treeSet = new java.util.TreeSet<>();
        linkedHashSet = new java.util.LinkedHashSet<>();

        for (int i = 0; i < LOOP_CNT; i++) {
            String input = data + i;
            hashSet.add(input);
            treeSet.add(input);
            linkedHashSet.add(input);

        }

        // key를 섞음
        if (keys == null || keys.length != LOOP_CNT) {
            keys = RandomKeyUtil.generateRandomSetKeysSwap(hashSet);
        }
    }

    @Benchmark
    public void hashSet() {
        for (String key : keys) {
            hashSet.contains(key);
        }
    }

    @Benchmark
    public void treeSet() {
        for (String key : keys) {
            treeSet.contains(key);
        }
    }

    @Benchmark
    public void linkedHashSet() {
        for (String key : keys) {
            linkedHashSet.contains(key);
        }
    }


}
