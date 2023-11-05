package sample.four;

import org.openjdk.jmh.annotations.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
public class ListAdd {

    int LOOP_CNT = 1000;
    List<Integer> arrayList;
    List<Integer> vector;
    List<Integer> linkedList;

    @Benchmark
    public void addArrayList() {
        arrayList = new ArrayList<>();
        for (int i = 0; i < LOOP_CNT; i++) {
            arrayList.add(i);
        }
    }

    @Benchmark
    public void addVector() {
        vector = new java.util.Vector<>();
        for (int i = 0; i < LOOP_CNT; i++) {
            vector.add(i);
        }
    }

    @Benchmark
    public void addLinkedList() {
        linkedList = new java.util.LinkedList<>();
        for (int i = 0; i < LOOP_CNT; i++) {
            linkedList.add(i);
        }
    }

}
