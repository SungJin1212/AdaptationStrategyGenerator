package Model.GeneratedCode.Tactics;

import Model.AbstactClass.Rule.Configuration;
import Model.AbstactClass.Rule.Tactic;

import static Model.AbstactClass.Behavior.SoS.csModelList;
import static Model.GeneratedCode.Behavior.CleaningSoS.minNumOfSweepingRobotType1;
import static Model.GeneratedCode.Behavior.CleaningSoS.minNumOfSweepingRobotType2;

public class RemoveSweepingRobotType2 extends Tactic implements Cloneable {
    private double remainTime = getLatency();

    public RemoveSweepingRobotType2() {

        super(3, 3, "RemoveSweepingRobotType2");
    }

    @Override
    public double[] run(Configuration configuration) throws CloneNotSupportedException {
        double[] ret = new double[2]; //cost, latency

        if(configuration.getConfiguration().get("numSweepingRobotType2") - 1 <= minNumOfSweepingRobotType2) {
            setExecuted(true);
            ret[1] = 1; //1 tick 소요됨
            return ret;
        }

        if(--remainTime == 0) {
            ret[0] = getCost();
            ret[1] = getLatency();

            int currentSweepingRobotType2 = configuration.getConfiguration().get("numSweepingRobotType2");
            csModelList.remove(String.format("SweepingRobotType2_%s",currentSweepingRobotType2));
            configuration.getConfiguration().put("numSweepingRobotType2", configuration.getConfiguration().get("numSweepingRobotType2") - 1);
            setExecuted(true);
        }
        return ret;
    }

}
