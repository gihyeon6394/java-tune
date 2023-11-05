package sample.four;

import org.openjdk.jmh.annotations.*;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
public class ListGet {

    int LOOP_CNT = 1000;
    List<Integer> arrayList;
    List<Integer> vector;
    LinkedList<Integer> linkedList;
    int result = 0;

    @Setup
    public void setup() {
        arrayList = new java.util.ArrayList<>();
        vector = new java.util.Vector<>();
        linkedList = new java.util.LinkedList<>();
        for (int i = 0; i < LOOP_CNT; i++) {
            arrayList.add(i);
            vector.add(i);
            linkedList.add(i);
        }
    }

    @Benchmark
    public void getArrayList() {
        for (int i = 0; i < LOOP_CNT; i++) {
            result = arrayList.get(i);
        }
    }

    @Benchmark
    public void getVector() {
        for (int i = 0; i < LOOP_CNT; i++) {
            result = vector.get(i);
        }
    }

    @Benchmark
    public void getLinkedList() {
        for (int i = 0; i < LOOP_CNT; i++) {
            result = linkedList.get(i);
        }
    }

    @Benchmark
    public void peekLinkedList() {
        for (int i = 0; i < LOOP_CNT; i++) {
            result = linkedList.peek();
        }
    }

}
