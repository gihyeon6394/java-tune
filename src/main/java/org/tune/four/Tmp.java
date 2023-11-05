package org.tune.four;

import java.util.*;

public class Tmp {

    public void makeMap() {

        Map<String, String> map = new HashMap<>();
        map.put("karina", "is beautiful");
        map.put("karina", "is cute");
        map.put("karina", "is pretty");

        Map syncMap = Collections.synchronizedMap(map);
    }
}
