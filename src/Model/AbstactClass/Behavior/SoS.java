package Model.AbstactClass.Behavior;

import Model.AbstactClass.Rule.Tactic;

import java.util.HashMap;
import java.util.Map;

abstract public class SoS {

    public static Map<String, CS> csModelList;
    public static Map<String, Environment> EnvironmentModelList;
    public static Map<String, CS> csSpecificationList;
    public static Map<String, Tactic> tacticSpecificationList;



    public SoS() {
        csModelList = new HashMap<> (0);
        EnvironmentModelList = new HashMap<>(0);
        csSpecificationList = new HashMap<>(0);
        tacticSpecificationList = new HashMap<>(0);
    }


    public void run() { //run active Environment -> cs
        for(String key : EnvironmentModelList.keySet()) {
            EnvironmentModelList.get(key).run();
        }
        for(String key : csModelList.keySet()) {
            csModelList.get(key).run();
        }
    }
    abstract public double[] getFitness();

}
