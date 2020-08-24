package Model.GeneratedCode.Tactics;

import Model.GeneratedCode.Behavior.CS;
import Model.AbstactClass.Rule.Configuration;
import Model.AbstactClass.Rule.Tactic;

import static Model.GeneratedCode.Behavior.SoS.csModelList;
import static Model.GeneratedCode.Behavior.SoS.csSpecificationList;
import static Model.GeneratedCode.Behavior.CleaningSoS.maxNumOfSweepingRobotType1;

public class AddSweepingRobotType1 extends Tactic implements Cloneable {

    private double remainTime;

    public AddSweepingRobotType1() {

        super(1, 1, "AddSweepingRobotType1");
        remainTime = getLatency();
    }

    @Override
    public double[] run(Configuration configuration) throws CloneNotSupportedException {
        double[] ret = new double[2]; //cost, latency

        if(configuration.getConfigurations().get("numSweepingRobotType1") + 1 >= maxNumOfSweepingRobotType1) {
            setExecuted(true);
            ret[1] = 1; //1 tick 소요됨
            return ret;
        }

        if(--remainTime == 0) {

            ret[0] = getCost();
            ret[1] = getLatency();
            int currentSweepingRobotType1 = configuration.getConfigurations().get("numSweepingRobotType1");
            csModelList.put(String.format("SweepingRobotType1_%s",currentSweepingRobotType1 + 1), (CS) csSpecificationList.get("SweepingRobotType1").clone());
            configuration.getConfigurations().put("numSweepingRobotType1", configuration.getConfigurations().get("numSweepingRobotType1") + 1);
            setExecuted(true);

        }
        return ret;
    }

}


