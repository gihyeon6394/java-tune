package sample.five;

import org.junit.jupiter.api.Test;
import org.openjdk.jmh.annotations.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
public class ForLoop {

    int LOOP_CNT = 100000; // 십만
    List<Integer> list;

    int curr;

    public void resultProcess(int result) {
        curr = result;
    }

    @Setup
    public void setUp() {
        list = Stream.iterate(0, i -> i + 1)
                .limit(LOOP_CNT) // 십만
                .collect(Collectors.toList());
    }


    @Benchmark
    public void traditionalLoop() {
        int listSize = list.size();
        for (int i = 0; i < listSize; i++) {
            resultProcess(list.get(i));
        }
    }

    @Benchmark
    public void traditionalSizeForLoop() {
        for (int i = 0; i < list.size(); i++) {
            resultProcess(list.get(i));
        }
    }

    @Benchmark
    public void timeForEachLoop() {
        for (Integer integer : list) {
            resultProcess(integer);
        }
    }
}
