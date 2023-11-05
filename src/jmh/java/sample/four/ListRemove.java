package sample.four;


import org.openjdk.jmh.annotations.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.TimeUnit;

@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
public class ListRemove {

    int LOOP_CNT = 10;
    List<Integer> arrayList;
    List<Integer> vector;
    List<Integer> linkedList;

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
    public void removeArrayListFromFirst() {
        ArrayList<Integer> tmpList = new ArrayList<>(arrayList);
        for (int i = 0; i < LOOP_CNT; i++) {
            tmpList.remove(0);
        }
    }

    @Benchmark
    public void removeArrayListFromLast() {
        ArrayList<Integer> tmpList = new ArrayList<>(arrayList);
        for (int i = LOOP_CNT - 1; i >= 0; i--) {
            tmpList.remove(i);
        }
    }


    @Benchmark
    public void removeVectorFromFirst() {
        List<Integer> tmpList = new Vector<>(vector);
        for (int i = 0; i < LOOP_CNT; i++) {
            tmpList.remove(0);
        }
    }

    @Benchmark
    public void removeVectorFromLast() {
        List<Integer> tmpList = new Vector<>(vector);
        for (int i = LOOP_CNT - 1; i >= 0; i--) {
            tmpList.remove(i);
        }
    }

    @Benchmark
    public void removeLinkedListFromFirst() {
        LinkedList<Integer> tmpList = new java.util.LinkedList<>(linkedList);
        for (int i = 0; i < LOOP_CNT; i++) {
            tmpList.remove(0);
        }
    }

    @Benchmark
    public void removeLinkedListFromLast() {
        LinkedList<Integer> tmpList = new java.util.LinkedList<>(linkedList);
        for (int i = LOOP_CNT - 1; i >= 0; i--) {
            tmpList.removeLast();
        }
    }


}
