package SoS;

import java.util.HashMap;
import java.util.Map;

public class EnvironmentCondition implements Cloneable {

    private Map<String, Double> environmentCondition;
    private int numOfVariables;

    public EnvironmentCondition(int numOfVariables) {
        this.numOfVariables = numOfVariables;
        environmentCondition = new HashMap<>(numOfVariables);
    }

    public Map<String, Double> getEnvironmentCondition() {return environmentCondition;}

    public Object clone() throws CloneNotSupportedException {
        EnvironmentCondition e = (EnvironmentCondition) super.clone();

        HashMap <String, Double> entryMap1 = new HashMap<>(0);

        for (Map.Entry<String, Double> entry : e.environmentCondition.entrySet()) {
            entryMap1.put(entry.getKey(), entry.getValue());
        }
        e.environmentCondition = entryMap1;
        return e;
    }
}
