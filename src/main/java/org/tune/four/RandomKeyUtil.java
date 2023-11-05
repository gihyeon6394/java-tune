package org.tune.four;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RandomKeyUtil {

    public static String[] generateRandomSetKeysSwap(Set<String> set) {
        int size = set.size();
        String result[] = new String[size];

        Random rand = new Random();
        int maxNumb = size;

        // result[]에 set의 key값을 넣는다.
        Iterator<String> iterator = set.iterator();
        int resultPos = 0;
        while (iterator.hasNext()) {
            result[resultPos++] = iterator.next();
        }

        // result[]의 key값을 랜덤하게 섞는다.
        for (int i = 0; i < size; i++) {
            int randNumb1 = rand.nextInt(maxNumb);
            int randNumb2 = rand.nextInt(maxNumb);
            String temp = result[randNumb2];
            result[randNumb2] = result[randNumb1];
            result[randNumb1] = temp;
        }

        return result;
    }

    public static void main(String[] args) {
        Set<String> set = new java.util.HashSet<>();
        set.add("Karina is Beautiful");
        set.add("Karina is Cute");
        set.add("Karina is Pretty");
        set.add("Karina is Lovely");
        set.add("Karina is Adorable");

        String[] result = generateRandomSetKeysSwap(set);
        for (String key : result) {
            System.out.println(key);
        }

        System.out.println("====================================");

        int[] result2 = generateRandomNumberKeysSwap(10);
        for (int key : result2) {
            System.out.println(key);
        }
    }

    public static int[] generateRandomNumberKeysSwap(int loopCnt) {
        int result[] = new int[loopCnt];
        Random rand = new Random();
        int maxNumb = loopCnt;

        // result[]에 0부터 loopCnt-1까지의 숫자를 넣는다.
        Stream.iterate(0, i -> i + 1).limit(loopCnt).forEach(i -> result[i] = i);

        Stream.iterate(0, i -> i + 1).limit(loopCnt).forEach(i -> result[i] = i);

        // result -> list
        List<Integer> list = Arrays.stream(result).boxed().collect(Collectors.toList());

        // list의 숫자를 랜덤하게 섞는다.
        Collections.shuffle(list);

        // list -> result
        for (int i = 0; i < loopCnt; i++) {
            result[i] = list.get(i);
        }
        return result;
    }
}
