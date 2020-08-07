package Model.AbstactClass.Rule;

import java.util.HashMap;
import java.util.Map;

abstract public class Configuration {
    private Map<String, Integer> configuration;
    private int numOfVariables;

    public Configuration(int numOfVariables, Map<String, Integer> configuration) {
        this.numOfVariables = numOfVariables;
        this.configuration = configuration;
    }

    public Map<String, Integer> getConfiguration() {
        return configuration;
    }

    public Configuration(int numOfVariables) {
        this.numOfVariables = numOfVariables;
        configuration = new HashMap<>(0);
    }

    public int getNumOfVariables() {
        return numOfVariables;
    }
}
