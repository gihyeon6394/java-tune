package org.tune.two;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class CompareTimerJMH {

    @Benchmark
    public DummyData makeObject() {
        HashMap<String, String> map = new HashMap<>(1000000);
        ArrayList<String> list = new ArrayList<>(1000000);
        return new DummyData(map, list);
    }
}
