package org.tune.one;

import javax.naming.InitialContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ServiceLocator {

    private InitialContext ic;
    private Map cache;
    private static ServiceLocator me;

    static {
        me = new ServiceLocator();
    }

    private ServiceLocator() {
        cache = Collections.synchronizedMap(new HashMap());
    }

    public InitialContext getInitialContext() {
        try {
            if (ic == null) {
                ic = new InitialContext();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ic;
    }

    public static ServiceLocator getInstance() {
        return me;
    }
}
