package Model.GeneratedCode.Tactics;

import Model.AbstactClass.Rule.Configuration;
import Model.AbstactClass.Rule.Tactic;

import static Model.GeneratedCode.Behavior.SoS.csModelList;
import static Model.GeneratedCode.Behavior.CleaningSoS.minNumOfMoppingRobotType1;

public class RemoveMoppingRobotType1 extends Tactic implements Cloneable {

    private double remainTime;

    public RemoveMoppingRobotType1() {

        super(1, 1, "RemoveMoppingRobotType1");
        remainTime = getLatency();
    }

    @Override
    public double[] run(Configuration configuration) throws CloneNotSupportedException {
        double[] ret = new double[2]; //cost, latency

        if(configuration.getConfigurations().get("numMoppingRobotType1") - 1 <= minNumOfMoppingRobotType1) {
            setExecuted(true);
            ret[1] = 1; //1 tick 소요됨
            return ret;
        }


        if(--remainTime == 0) {
            ret[0] = getCost();
            ret[1] = getLatency();

            int currentMoppingRobotType1 = configuration.getConfigurations().get("numMoppingRobotType1");
            csModelList.remove(String.format("MoppingRobotType1_%s",currentMoppingRobotType1));
            configuration.getConfigurations().put("numMoppingRobotType1", configuration.getConfigurations().get("numMoppingRobotType1") - 1);
            setExecuted(true);

        }
        return ret;
    }
}
