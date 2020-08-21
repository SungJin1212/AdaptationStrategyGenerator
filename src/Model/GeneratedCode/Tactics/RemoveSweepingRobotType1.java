package Model.GeneratedCode.Tactics;

import Model.AbstactClass.Rule.Configuration;
import Model.AbstactClass.Rule.Tactic;

import static Model.AbstactClass.Behavior.SoS.csModelList;
import static Model.GeneratedCode.Behavior.CleaningSoS.*;

public class RemoveSweepingRobotType1 extends Tactic implements Cloneable {
    private double remainTime = getLatency();

    public RemoveSweepingRobotType1() {

        super(1, 1, "RemoveSweepingRobotType1");
    }

    @Override
    public double[] run(Configuration configuration) throws CloneNotSupportedException {
        double[] ret = new double[2]; //cost, latency

        if(configuration.getConfiguration().get("numSweepingRobotType1") - 1 <= minNumOfSweepingRobotType1) {
            setExecuted(true);
            ret[1] = 1; //1 tick 소요됨
            return ret;
        }

        if(--remainTime == 0) {
            ret[0] = getCost();
            ret[1] = getLatency();
            int currentSweepingRobotType1 = configuration.getConfiguration().get("numSweepingRobotType1");
            csModelList.remove(String.format("SweepingRobotType1_%s",currentSweepingRobotType1));
            configuration.getConfiguration().put("numSweepingRobotType1", configuration.getConfiguration().get("numSweepingRobotType1") - 1);
            setExecuted(true);

        }
        return ret;
    }
}
