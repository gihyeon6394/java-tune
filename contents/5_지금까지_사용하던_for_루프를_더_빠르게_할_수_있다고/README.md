# 5. 지금까지 사용하던 for 루프를 더 빠르게 할 수 있다고?

- 조건문에서의 속도는?
- 반복 구문에서의 속도는?
- 반복 구문에서의 필요 없는 반복

---

## 조건문에서의 속도는?

- `if-else if-else`
- `switch`

```java

package sample.five;

import org.openjdk.jmh.annotations.*;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
public class ConditionIf {

    int LOOP_CNT = 1000;

    String curr;

    private void resultProcess(String dummy) {
        curr = dummy;
    }

    @Benchmark
    public void randomOnly() {
        Random rand = new Random();
        int data = 1000 + rand.nextInt();
        for (int i = 0; i < LOOP_CNT; i++) {
            resultProcess("dummy");
        }
    }

    @Benchmark
    public void if10() {
        Random rand = new Random();
        String result = null;
        int data = 1000 + rand.nextInt();

        for (int i = 0; i < LOOP_CNT; i++) {
            if (data < 50) {
                result = "50";
            } else if (data < 100) {
                result = "100";
            } else if (data < 150) {
                result = "150";
            } else if (data < 200) {
                result = "200";
            } else if (data < 250) {
                result = "250";
            } else if (data < 300) {
                result = "300";
            } else if (data < 350) {
                result = "350";
            } else if (data < 400) {
                result = "400";
            } else if (data < 450) {
                result = "450";
            } else if (data < 500) {
                result = "500";
            } else if (data < 550) {
                result = "550";
            } else if (data < 600) {
                result = "600";
            } else if (data < 650) {
                result = "650";
            } else if (data < 700) {
                result = "700";
            } else if (data < 750) {
                result = "750";
            } else if (data < 800) {
                result = "800";
            } else if (data < 850) {
                result = "850";
            } else if (data < 900) {
                result = "900";
            } else if (data < 950) {
                result = "950";
            } else {
                result = "over";
            }
        }
        resultProcess(result);

    }

    @Benchmark
    public void if50() {
        Random rand = new Random();
        String result = null;
        int data = 1000 + rand.nextInt();

        for (int i = 0; i < LOOP_CNT; i++) {
            if (data < 50) {
                result = "50";
            } else if (data < 100) {
                result = "100";
            } else if (data < 150) {
                result = "150";
            } else if (data < 200) {
                result = "200";
            } else if (data < 250) {
                result = "250";
            } else if (data < 300) {
                result = "300";
            } else if (data < 350) {
                result = "350";
            } else if (data < 400) {
                result = "400";
            } else if (data < 450) {
                result = "450";
            } else if (data < 500) {
                result = "500";
            } else if (data < 550) {
                result = "550";
            } else if (data < 600) {
                result = "600";
            } else if (data < 650) {
                result = "650";
            } else if (data < 700) {
                result = "700";
            } else if (data < 750) {
                result = "750";
            } else if (data < 800) {
                result = "800";
            } else if (data < 850) {
                result = "850";
            } else if (data < 900) {
                result = "900";
            } else if (data < 950) {
                result = "950";
            } else if (data < 1000) {
                result = "1000";
            } else if (data < 1050) {
                result = "1050";
            } else if (data < 1100) {
                result = "1100";
            } else if (data < 1150) {
                result = "1150";
            } else if (data < 1200) {
                result = "1200";
            } else if (data < 1250) {
                result = "1250";
            } else if (data < 1300) {
                result = "1300";
            } else if (data < 1350) {
                result = "1350";
            } else if (data < 1400) {
                result = "1400";
            } else if (data < 1450) {
                result = "1450";
            } else if (data < 1500) {
                result = "1500";
            } else if (data < 1550) {
                result = "1550";
            } else if (data < 1600) {
                result = "1600";
            } else if (data < 1650) {
                result = "1650";
            } else if (data < 1700) {
                result = "1700";
            } else if (data < 1750) {
                result = "1750";
            } else if (data < 1800) {
                result = "1800";
            } else if (data < 1850) {
                result = "1850";
            } else if (data < 1900) {
                result = "1900";
            } else if (data < 1950) {
                result = "1950";
            } else if (data < 2000) {
                result = "2000";
            } else if (data < 2050) {
                result = "2050";
            } else if (data < 2100) {
                result = "2100";
            } else if (data < 2150) {
                result = "2150";
            } else if (data < 2200) {
                result = "2200";
            } else if (data < 2250) {
                result = "2250";
            } else if (data < 2300) {
                result = "2300";
            } else if (data < 2350) {
                result = "2350";
            } else if (data < 2400) {
                result = "2400";
            } else if (data < 2450) {
                result = "2450";
            } else if (data < 2500) {
                result = "2500";
            } else if (data < 2550) {
                result = "2550";
            } else if (data < 2600) {
                result = "2600";
            } else if (data < 2650) {
                result = "2650";
            } else if (data < 2700) {
                result = "2700";
            } else if (data < 2750) {
                result = "2750";
            } else if (data < 2800) {
                result = "2800";
            } else if (data < 2850) {
                result = "2850";
            } else if (data < 2900) {
                result = "2900";
            } else if (data < 2950) {
                result = "2950";
            } else if (data < 3000) {
                result = "3000";
            } else if (data < 3050) {
                result = "3050";
            } else if (data < 3100) {
                result = "3100";
            } else if (data < 3150) {
                result = "3150";
            } else if (data < 3200) {
                result = "3200";
            } else if (data < 3250) {
                result = "3250";
            } else if (data < 3300) {
                result = "3300";
            } else if (data < 3350) {
                result = "3350";
            } else if (data < 3400) {
                result = "3400";
            } else if (data < 3450) {
                result = "3450";
            } else if (data < 3500) {
                result = "3500";
            } else if (data < 3550) {
                result = "3550";
            } else if (data < 3600) {
                result = "3600";
            } else if (data < 3650) {
                result = "3650";
            } else if (data < 3700) {
                result = "3700";
            } else if (data < 3750) {
                result = "3750";
            } else if (data < 3800) {
                result = "3800";
            } else if (data < 3850) {
                result = "3850";
            } else if (data < 3900) {
                result = "3900";
            } else if (data < 3950) {
                result = "3950";
            } else if (data < 4000) {
                result = "4000";
            } else if (data < 4050) {
                result = "4050";
            } else if (data < 4100) {
                result = "4100";
            } else if (data < 4150) {
                result = "4150";
            } else if (data < 4200) {
                result = "4200";
            } else if (data < 4250) {
                result = "4250";
            } else if (data < 4300) {
                result = "4300";
            } else if (data < 4350) {
                result = "4350";
            } else if (data < 4400) {
                result = "4400";
            } else if (data < 4450) {
                result = "4450";
            } else if (data < 4500) {
                result = "4500";
            } else if (data < 4550) {
                result = "4550";
            } else if (data < 4600) {
                result = "4600";
            } else if (data < 4650) {
                result = "4650";
            } else if (data < 4700) {
                result = "4700";
            } else if (data < 4750) {
                result = "4750";
            } else if (data < 4800) {
                result = "4800";
            } else if (data < 4850) {
                result = "4850";
            } else if (data < 4900) {
                result = "4900";
            } else if (data < 4950) {
                result = "4950";
            } else {
                result = "over";
            }

        }

        resultProcess(result);

    }

}

```

````
Benchmark               Mode  Cnt  Score   Error  Units
ConditionIf.if10        avgt    5  0.694 ± 2.131  us/op
ConditionIf.if50        avgt    5  5.250 ± 1.128  us/op
ConditionIf.randomOnly  avgt    5  2.850 ± 0.103  us/op
````

- `resultProcess()` : JIT compiler가 최적화하지 못하게 막는 것
    - `result` 사용처가 없으면 값을 할당해도 JIT compiler가 무시해버림

### switch의 가독성이 더 좋음

````
public int getMonthNubmer(String monthStr){
    int month = -1;
    
    switch(monthStr){
        case "JAN" : month = 1; break;
        case "FEB" : month = 2; break;
        case "MAR" : month = 3; break;
        case "APR" : month = 4; break;
        case "MAY" : month = 5; break;
        case "JUN" : month = 6; break;
        case "JUL" : month = 7; break;
        case "AUG" : month = 8; break;
        case "SEP" : month = 9; break;
        case "OCT" : month = 10; break;
        case "NOV" : month = 11; break;
        case "DEC" : month = 12; break;
    }
    
    return month;
}
````

- `switch`는 `if-else if-else`보다 가독성이 좋다.
- 내부적으로 int를 리턴하는 Object 클래스의 `hashCode()`를 호출한다.
    - **hashcode를 정렬해두고 비교함**

## 반복 구문에서의 속도는?

- `for`
- `do-while`
- `while`
    - 무한 루프에 빠질 수 있음을 주의

```java
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

```

````  
Benchmark                       Mode  Cnt   Score    Error  Units
ForLoop.timeForEachLoop         avgt    5  52.734 ± 14.162  us/op
ForLoop.traditionalLoop         avgt    5  43.881 ±  0.863  us/op
ForLoop.traditionalSizeForLoop  avgt    5  43.746 ±  0.402  us/op
````

- `traditionalSizeForLoop()` : `list.size()`를 매번 호출
    - 근데 성능은 또 비슷하고... TODO
    - `list.size()`는 `ArrayList`에서는 `O(1)`이기 때문에 성능 차이가 없다?
- for-each는 모든 원소를 순차적으로 탐색해야할 떄 유용

## 반복 구문에서의 필요 없는 반복

````
public void sample(DataVO data, String key){
  TreeSet treeset2 = (TreeSet) data.get(key);
  if(treeSet2 != null){
      for(int i = 0; i < treeSet2.size(); i++){
          DataVO2 data2 = (DataVO2)treeset2..toArray()[i];
      }
  }
}

// 향상
public void sample(DataVO data, String key){
  TreeSet treeset2 = (TreeSet) data.get(key);
  if(treeSet2 != null){
      DataVO2 data2 = (DataVO2)treeset2..toArray()[i];
      int size = treeSet2.size();
      for(int i = 0; i < size; i++){
        DataVO2 data2 = data2[i];
      }
  }
}



````

