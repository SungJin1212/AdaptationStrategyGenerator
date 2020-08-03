package Model.AbstactClass;

import java.util.HashMap;
import java.util.Map;

abstract public class SoS {

    public static Map<String, CS> csList;
    public static Map<String, Environment> environmentList;

    public SoS() {
        csList = new HashMap<> (0);
        environmentList = new HashMap<>(0);
    }

    public void run() {
        for(String key : csList.keySet()) {
            csList.get(key).run();
        }
    }

}
