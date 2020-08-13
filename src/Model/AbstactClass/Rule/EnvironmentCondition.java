package Model.AbstactClass.Rule;

import java.util.HashMap;
import java.util.Map;

public class EnvironmentCondition {

    private Map<String, Integer> environmentCondition;
    private int numOfVariables;

    public EnvironmentCondition(int numOfVariables) {
        this.numOfVariables = numOfVariables;
        environmentCondition = new HashMap<>(numOfVariables);
    }

    public Map<String, Integer> getEnvironmentCondition() {return environmentCondition;}
}
