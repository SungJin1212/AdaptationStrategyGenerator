package Model.SoS.Tactics;

import Model.SoS.Configuration;
import Model.SoS.Tactic;

import static Model.SoS.CleaningSoS.minNumOfSweepingRobotType2;
import static Model.SoS.SoS.csModelList;

public class RemoveSweepingRobotType2 extends Tactic implements Cloneable {
    private double remainTime;

    public RemoveSweepingRobotType2() {

        super(1, 1, "RemoveSweepingRobotType2");
        remainTime = getLatency();
    }

    @Override
    public double[] run(Configuration configuration) throws CloneNotSupportedException {
        double[] ret = new double[2]; //cost, latency

        if(configuration.getConfigurations().get("numSweepingRobotType2") - 1 <= minNumOfSweepingRobotType2) {
            setExecuted(true);
            ret[1] = 1; //1 tick 소요됨
            return ret;
        }

        if(--remainTime == 0) {
            ret[0] = getCost();
            ret[1] = getLatency();

            int currentSweepingRobotType2 = configuration.getConfigurations().get("numSweepingRobotType2");
            csModelList.remove(String.format("SweepingRobotType2_%s",currentSweepingRobotType2));
            configuration.getConfigurations().put("numSweepingRobotType2", configuration.getConfigurations().get("numSweepingRobotType2") - 1);
            setExecuted(true);
        }
        return ret;
    }

}
