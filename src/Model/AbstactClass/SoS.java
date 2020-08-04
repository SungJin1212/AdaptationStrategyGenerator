package Model.AbstactClass;

import java.util.HashMap;
import java.util.Map;

abstract public class SoS {

    public static Map<String, CS> csModelList;
    public static Map<String, CS> csSpecificationList;
    public static Map<String, PassiveEnvironment> passiveEnvironmentModelList;
    public static Map<String, ActiveEnvironment> activeEnvironmentModelList;

    public SoS() {
        csModelList = new HashMap<> (0);
        csSpecificationList = new HashMap<> (0);
        passiveEnvironmentModelList = new HashMap<>(0);
        activeEnvironmentModelList = new HashMap<>(0);
    }

    public void run() { //run active Environment -> cs
        for(String key : activeEnvironmentModelList.keySet()) {
            activeEnvironmentModelList.get(key).run();
        }
        for(String key : csModelList.keySet()) {
            csModelList.get(key).run();
        }
    }
    abstract public String getFitness();

}
