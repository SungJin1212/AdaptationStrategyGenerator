package Model.GeneratedCode.Tactics;

import Model.AbstactClass.Behavior.CS;
import Model.AbstactClass.Rule.Configuration;
import Model.AbstactClass.Rule.Tactic;

import static Model.AbstactClass.Behavior.SoS.csModelList;
import static Model.AbstactClass.Behavior.SoS.csSpecificationList;

public class AddSweepingRobotType1 extends Tactic implements Cloneable {

    private double remainTime = getLatency();

    public AddSweepingRobotType1() {

        super(3, 3, "AddSweepingRobotType1");
    }

    @Override
    public double[] run(Configuration configuration) throws CloneNotSupportedException {
        double[] ret = new double[2];

        if(--remainTime == 0) {
            ret[0] = getCost();
            ret[1] = getLatency();
            int currentSweepingRobotType1 = configuration.getConfiguration().get("numSweepingRobotType1");
            csModelList.put(String.format("SweepingRobotType1_%s",currentSweepingRobotType1 + 1), (CS) csSpecificationList.get("SweepingRobotType1").clone());
            configuration.getConfiguration().put("numSweepingRobotType1", configuration.getConfiguration().get("numSweepingRobotType1") + 1);
            setExecuted(true);

        }
        return ret;
    }

}


