package org.tune.two;

import java.util.Iterator;
import java.util.Properties;
import java.util.Set;

public class GetProperties {
    public static void main(String[] args) {

        System.setProperty("Aespa Leader", "Karina");
        Properties props = System.getProperties();
        Set key = props.keySet();
        Iterator it = key.iterator();
        while (it.hasNext()) {
            String k = (String) it.next();
            String v = System.getProperty(k);
            System.out.println(k + " : " + v);
        }
    }

}
