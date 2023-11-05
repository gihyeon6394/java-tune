package sample.four;

import org.openjdk.jmh.annotations.*;
import org.tune.four.RandomKeyUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
public class MapGet {

    int LOOP_CNT = 1000;
    Map<Integer, String> hashMap;
    Map<Integer, String> hashTable;
    Map<Integer, String> treeMap;
    Map<Integer, String> linkedHashlMap;

    int[] keys;

    @Setup(Level.Trial)
    public void setup() {
        hashMap = new HashMap<>();
        hashTable = new HashMap<>();
        treeMap = new HashMap<>();
        linkedHashlMap = new HashMap<>();

        String data = "Karina is Beautiful";

        for (int i = 0; i < LOOP_CNT; i++) {
            String dt = data + i;
            hashMap.put(i, dt);
            hashTable.put(i, dt);
            treeMap.put(i, dt);
            linkedHashlMap.put(i, dt);
        }

        keys = RandomKeyUtil.generateRandomNumberKeysSwap(LOOP_CNT);
    }

    @Benchmark
    public void getSeqHashMap() {
        for (int i = 0; i < LOOP_CNT; i++) {
            hashMap.get(i);
        }
    }

    @Benchmark
    public void getRandomHashMap() {
        for (int i = 0; i < LOOP_CNT; i++) {
            hashMap.get(keys[i]);
        }
    }

    @Benchmark
    public void getSeqHashTable() {
        for (int i = 0; i < LOOP_CNT; i++) {
            hashTable.get(i);
        }
    }

    @Benchmark
    public void getRandomHashTable() {
        for (int i = 0; i < LOOP_CNT; i++) {
            hashTable.get(keys[i]);
        }
    }

    @Benchmark
    public void getSeqTreeMap() {
        for (int i = 0; i < LOOP_CNT; i++) {
            treeMap.get(i);
        }
    }

    @Benchmark
    public void getRandomTreeMap() {
        for (int i = 0; i < LOOP_CNT; i++) {
            treeMap.get(keys[i]);
        }
    }

    @Benchmark
    public void getSeqLinkedHashMap() {
        for (int i = 0; i < LOOP_CNT; i++) {
            linkedHashlMap.get(i);
        }
    }

    @Benchmark
    public void getRandomLinkedHashMap() {
        for (int i = 0; i < LOOP_CNT; i++) {
            linkedHashlMap.get(keys[i]);
        }
    }

}
