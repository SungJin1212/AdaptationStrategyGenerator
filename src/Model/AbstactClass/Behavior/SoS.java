package Model.AbstactClass.Behavior;

import Model.AbstactClass.Rule.Configuration;
import Model.AbstactClass.Rule.Strategy;
import Model.AbstactClass.Rule.Tactic;
import Model.GeneratedCode.Rule.cleaningSoSConfiguration;

import java.util.HashMap;
import java.util.Map;

abstract public class SoS {

    public static Map<String, CS> csModelList;
    public static Map<String, Environment> EnvironmentModelList;
    public static Map<String, CS> csSpecificationList;
    public static Map<String, Tactic> tacticSpecificationList;

    public static Map<String, Tactic> getTacticSpecificationList() {
        return tacticSpecificationList;
    }

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

    public double[] runStrategy(Configuration configuration, Strategy strategy) throws CloneNotSupportedException {
        Tactic curTactic = null;

        for(Tactic t : strategy.getStrategy()) {
            if(!t.isExecuted()) {
                curTactic = t;
                break;
            }
        }
        if (curTactic == null) {
            return new double[] {0.0,0.0};
        }

        return curTactic.run(configuration);
    }

    abstract public double[] getFitness(Strategy strategy) throws CloneNotSupportedException;

}
