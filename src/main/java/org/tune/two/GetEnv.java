package org.tune.two;

import java.util.Map;

public class GetEnv {
    public static void main(String[] args) {
        Map<String, String> env = System.getenv();
        for (String envName : env.keySet()) {
            System.out.println(envName + " : " + env.get(envName));
        }
    }
}
