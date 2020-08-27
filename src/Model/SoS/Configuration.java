package Model.SoS;

import java.util.HashMap;
import java.util.Map;

abstract public class Configuration implements Cloneable {
    private Map<String, Integer> configurations;
    private Map<String, Integer> maxConfigurations;
    private Map<String, Integer> minConfigurations;
    private int numOfVariables;

    public Configuration(int numOfVariables) {
        this.numOfVariables = numOfVariables;
        configurations = new HashMap<>(numOfVariables);
        maxConfigurations = new HashMap<>(numOfVariables);
        minConfigurations = new HashMap<>(numOfVariables);
    }

    public Map<String, Integer> getConfigurations() {
        return configurations;
    }

    public Map<String, Integer> getMaxConfigurations() {
        return maxConfigurations;
    }

    public Map<String, Integer> getMinConfigurations() {
        return minConfigurations;
    }

    public int getNumOfVariables() {
        return numOfVariables;
    }

    public Object clone() throws CloneNotSupportedException { // deep copy even the hash map
        Configuration c = (Configuration) super.clone();
        HashMap <String, Integer> entryMap1 = new HashMap<>(0);
        HashMap <String, Integer> entryMap2 = new HashMap<>(0);
        HashMap <String, Integer> entryMap3 = new HashMap<>(0);



        for (Map.Entry<String, Integer> entry : c.configurations.entrySet()) {
            entryMap1.put(entry.getKey(), entry.getValue());
        }

        for (Map.Entry<String, Integer> entry : c.maxConfigurations.entrySet()) {
            entryMap2.put(entry.getKey(), entry.getValue());
        }

        for (Map.Entry<String, Integer> entry : c.minConfigurations.entrySet()) {
            entryMap3.put(entry.getKey(), entry.getValue());
        }
        c.configurations = entryMap1;
        c.maxConfigurations = entryMap2;
        c.minConfigurations = entryMap3;
        return c;
    }
}