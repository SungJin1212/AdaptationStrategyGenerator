package Model.AbstactClass.Behavior;

import Model.AbstactClass.Rule.Configuration;
import Model.AbstactClass.Rule.EnvironmentCondition;
import Model.AbstactClass.Rule.Tactic;

import java.util.HashMap;
import java.util.Map;

abstract public class SoS {

    public static Map<String, CS> csModelList;
    public static Map<String, Environment> EnvironmentModelList;
    public static Map<String, CS> csSpecificationList;
    public static Map<String, Tactic> tacticSpecificationList;
    private Configuration configuration;
    private EnvironmentCondition environmentCondition;

    public EnvironmentCondition getSoSEnvironmentCondition() {
        return environmentCondition;
    }

    public Configuration getSoSConfiguration() {
        return configuration;
    }

    public static Map<String, Tactic> getTacticSpecificationList() {
        return tacticSpecificationList;
    }

    public SoS() {
        csModelList = new HashMap<> (0);
        EnvironmentModelList = new HashMap<>(0);
        csSpecificationList = new HashMap<>(0);
        tacticSpecificationList = new HashMap<>(0);
    }

    public SoS(EnvironmentCondition environmentCondition, Configuration configuration) {
        csModelList = new HashMap<> (0);
        EnvironmentModelList = new HashMap<>(0);
        csSpecificationList = new HashMap<>(0);
        tacticSpecificationList = new HashMap<>(0);
        this.environmentCondition = environmentCondition;
        this.configuration = configuration;
    }

    public void setSoSConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    public void setSoSEnvironmentCondition(EnvironmentCondition environmentCondition) {
        this.environmentCondition = environmentCondition;
    }


}
