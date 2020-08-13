package Model.AbstactClass.Rule;

import java.util.HashMap;
import java.util.Map;

abstract public class Configuration implements Cloneable {
    private Map<String, Integer> configuration;
    private int numOfVariables;

    public Configuration(int numOfVariables, Map<String, Integer> configuration) {
        this.numOfVariables = numOfVariables;
        this.configuration = configuration;
    }

    public Configuration(int numOfVariables) {
        this.numOfVariables = numOfVariables;
        configuration = new HashMap<>(numOfVariables);
    }

    public Map<String, Integer> getConfiguration() {
        return configuration;
    }

    public int getNumOfVariables() {
        return numOfVariables;
    }

    public Object clone() throws CloneNotSupportedException { // deep copy even the hash map
        Configuration c = (Configuration) super.clone();
        HashMap <String, Integer> entryMap = new HashMap<>(0);

        for (Map.Entry<String, Integer> entry : c.configuration.entrySet()) {

            entryMap.put(entry.getKey(), entry.getValue());
        }
        c.configuration = entryMap;
        return c;
    }
}