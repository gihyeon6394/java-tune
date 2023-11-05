# 4. 어디에 담아야하는지

- Collection 및 Map 인터페이스의 이해
- Set 클래스 중 무엇이 가장 빠를까?
- List 관련 클래스 중 무엇이 가장 빠를까?
- Map 관련 클래스 중 무엇이 가장 빠를까?
- Collection 관련 클래스의 동기화

---

## Collection 및 Map 인터페이스의 이해

- Collection : 상위 인터페이스
- Set : 중복을 허용하지 않는 집합
    - SortedSet : 오름차순 Set
- List : 중복 허용 + 순서 있는 집합
    - 인덱스 사용
    - ArrayList 를 가장 많이 사용
- Queue : FIFO
- Map : Key, Value 쌍으로 이루어진 집합
    - 중복 key 는 허용하지 않음
    - SortedMap : Key 오름차순 정렬

### Set 인터페이스

- HashSet : Hash Table에 데이터를 담음
    - 순서 없음
- TreeSet : Red-Black Tree에 데이터를 담음
    - 오름차순 정렬
    - 데이터를 담으면서 동시에 정렬 (HashSet에 비해 삽입속도 느림)
- LinkedHashSet : HashTable에 데이터를 담음
    - 저장된 순서대로 데이터 정렬

> #### Red-Black Tree
>
> - 이진 트리
> - 각 노드는 검은색 or 빨간색
> - 루트 노드는 검은색
> - 모든 리프 노드는 검은색
> - 붉은 노드의 자식은 검은 노드
> - 모든 말단 노드까지의 경로에는 동일한 수의 검은 노드가 존재


<img src="img.png"  width="60%"/>

### List 인터페이스 (배열의 확장판)

- 데이터 크기가 자동으로 조절됨
- 데이터 개수를 확실히 모를 때 유용
- Vector : 객체 생성 시 크기 지정 필요 없는 배열
- ArrayList : Vector와 동일한 기능
    - Vector와 차이점 : 동기화 처리가 되어 있지 않음
- LinkedList : ArrayList와 동일, Queue 인터페이스 구현체
    - FIFO

### Map 인터페이스

- Key, Value 쌍으로 이루어진 집합
- Hashtable : Hash Table에 데이터를 담음
    - 동기화
- HashMap : Hash Table에 데이터를 담음
    - 동기화 X
    - null 허용
- TreeMap : Red-Black Tree에 데이터를 담음
    - Key 오름차순 정렬
- LinkedHashMap : Hash Table에 데이터를 담음
    - HashMap과 거의 동일
    - 이중 연결 리스트

### Queue 인터페이스

> #### BlockingQueue
>
> - 고정된 크기의 queue
> - 공간이 부족하면 공간이 생길 때까지 대기

- FIFO
- List는 데이터가 많은 경우 처리 시간이 늘어남
    - index 0을 삭제하면 1 ~ n-1 까지의 데이터를 한 칸씩 앞으로 당겨야 함
- 구현체는 크게 두가지
    - `java.util.LinkedList`, `java.util.PriorityQueue`
    - `java.util.concurrent....`
- PriorityQueue : 삽입 순서와 상관 없이 먼저 생성된 객체가 먼저 나옴
- LinkedBlockingQueue : FIFO
    - 저장 데이터 크기를 선택적으로 지정 가능
    - BlockingQueue
- ArrayBlockingQueue : 저장된 데이터 크기가 고정
    - FIFO
    - BlockingQueue
- PriorityBlockingQueue : 저장 데이터 크기 정해져있지 않음
    - FIFO
    - BlockingQueue
- DelayQueue : 큐가 대기하는 시간을 지정해야함
- SynchronousQueue : 데이터를 저장하지 않고 전달만 함
    - `put()` 호출 시 다른 스레드는 `take()` 호출 시 까지 대기

## Set 클래스 중 무엇이 가장 빠를까?

### add

```java
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

```

````
Benchmark                     Mode  Cnt   Score    Error  Units
four.SetAdd.addHashSet        avgt   10   0.034 ±  0.001  ms/op
four.SetAdd.addLinkedHashSet  avgt   10   0.038 ±  0.001  ms/op
four.SetAdd.addTreeSet        avgt   10   0.077 ±  0.002  ms/op
````

- TreeSet이 가장 느림
    - 데이터를 저장하면서 정렬을 동시에 수행하기 때문
- `addHashSetWithInitialCapacity()` : 속도는 비스하지만 크기를 지정하는건 성능상 유리

### iterate

```java
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
```

````
Benchmark                        Mode  Cnt  Score    Error  Units
SetIterate.iterateHashSet        avgt    5  0.005 ±  0.001  ms/op
SetIterate.iterateLinkedHashSet  avgt    5  0.003 ±  0.001  ms/op
SetIterate.iterateTreeSet        avgt    5  0.006 ±  0.001  ms/op
````

- LinkedHashSet이 가장 빠름
    - 데이터를 저장한 순서대로 데이터를 반환하기 때문

### contains

```java
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
```

````
Benchmark                  Mode  Cnt   Score    Error  Units
SetContains.hashSet        avgt    5   2.089 ±  0.043  us/op
SetContains.linkedHashSet  avgt    5   2.394 ±  0.098  us/op
SetContains.treeSet        avgt    5  48.175 ± 18.158  us/op
````

- TreeSet이 가장 느림

### 정리

- TreeSet은 데이터를 저장하면서 정렬을 동시에 수행하기 때문에 느림
- 저장 순서에 따라 데이터 탐색이 필요하면 TreeSet 사용
- 아니면 HashSet, LinkedHashSet 사용 권장

## List 관련 클래스 중 무엇이 가장 빠를까?

### add

```java

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
```

````
Benchmark              Mode  Cnt   Score   Error  Units
ListAdd.addArrayList   avgt    5   3.037 ± 0.110  us/op
ListAdd.addLinkedList  avgt    5   3.399 ± 0.148  us/op
ListAdd.addVector      avgt    5  10.199 ± 0.300  us/op
````

- 어느 클래스건 큰 차이가 없음

### get

```java
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
```

````
Benchmark               Mode  Cnt    Score    Error  Units
ListGet.getArrayList    avgt    5    0.387 ±  0.005  us/op
ListGet.getVector       avgt    5    8.921 ±  0.046  us/op
ListGet.getLinkedList   avgt    5  331.890 ±  2.074  us/op
ListGet.peekLinkedList  avgt    5    0.002 ±  0.001  us/op
````

- ArrayList가 가장 빠름
- LinkedList는 peek()이 가장 빠름
- Vector의 `get()`은 `synchronized` 키워드가 붙어있어서 느림
    - thread-safe

### 삭제

```java
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

```

````
Benchmark                             Mode  Cnt  Score    Error  Units
ListRemove.removeArrayListFromFirst   avgt    5  0.060 ±  0.012  us/op
ListRemove.removeArrayListFromLast    avgt    5  0.012 ±  0.001  us/op
ListRemove.removeVectorFromFirst      avgt    5  0.075 ±  0.012  us/op
ListRemove.removeVectorFromLast       avgt    5  0.017 ±  0.001  us/op
ListRemove.removeLinkedListFromFirst  avgt    5  0.043 ±  0.002  us/op
ListRemove.removeLinkedListFromLast   avgt    5  0.040 ±  0.001  us/op
````

- 마지막에서 값을 제거하는 것이 더 빠름 (LinkedList 제외)
- ArrayList, Vector는 내부적으로 배열을 사용
    - 첫번째 값을 제거하면 1 ~ n-1 까지의 값을 한 칸씩 앞으로 당겨야 함

## Map 관련 클래스 중 무엇이 가장 빠를까?

```java
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
```

````
Benchmark                      Mode  Cnt  Score   Error  Units
MapGet.getSeqHashMap           avgt    5  3.113 ± 0.484  us/op
MapGet.getRandomHashMap        avgt    5  2.709 ± 0.311  us/op
MapGet.getSeqHashTable         avgt    5  2.976 ± 0.055  us/op
MapGet.getRandomHashTable      avgt    5  2.623 ± 0.040  us/op
MapGet.getSeqLinkedHashMap     avgt    5  3.023 ± 0.096  us/op
MapGet.getRandomLinkedHashMap  avgt    5  2.665 ± 0.337  us/op
MapGet.getSeqTreeMap           avgt    5  3.050 ± 0.424  us/op
MapGet.getRandomTreeMap        avgt    5  2.736 ± 0.239  us/op
````

- TreeMap이 가장 느림

## Collection 관련 클래스의 동기화

- 동기화 되어있는 클래스 : Vector, Hashtable
- 동기화 되어있지 않은 클래스 : 그 외 모든 컬렉션

````
// Colleciton 동기화 처리 방법
Set s = Collections.synchronizedSet(new HashSet(...));
SortedSet s = Collections.synchronizedSortedSet(new TreeSet(...));
Set s = Collections.synchronizedSortedSet(new LinkedHashSet(...));

List l = Collections.synchronizedList(new ArrayList(...)); 
List l = Collections.synchronizedList(new LinkedList(...));

Map m = Collections.synchronizedMap(new HashMap(...));
SortedMap m = Collections.synchronizedSortedMap(new TreeMap(...));
Map m = Collections.synchronizedSortedMap(new LinkedHashMap(...));


// SynchronizedMap 예제
Map<String, String> map = new HashMap<>();
map.put("karina", "is beautiful");
map.put("karina", "is cute");
map.put("karina", "is pretty");

Map syncMap = Collections.synchronizedMap(map); // return SynchronizedMap
````

## 정리

- 일반적인 웹 개발시 Collection 성능 차이를 비교하는 것은 큰 의미가 없음
- 목적에 부합하는 클래스를 선택해서 사용하면 됨
- 성능이 확실치 않으면 JMH로 성능 측정