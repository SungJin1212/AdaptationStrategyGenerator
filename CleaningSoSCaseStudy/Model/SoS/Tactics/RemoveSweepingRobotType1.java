package CleaningSoSCaseStudy.Model.SoS.Tactics;

import Model.SoS.Configuration;
import Model.SoS.Tactic;

import static Model.SoS.CleaningSoS.minNumOfSweepingRobotType1;
import static Model.SoS.SoS.csModelList;

public class RemoveSweepingRobotType1 extends Tactic implements Cloneable {
    private double remainTime;

    public RemoveSweepingRobotType1() {

        super(1, 1, "RemoveSweepingRobotType1");
        remainTime = getLatency();
    }

    @Override
    public double[] run(Configuration configuration) throws CloneNotSupportedException {
        double[] ret = new double[2]; //cost, latency

        if(configuration.getConfigurations().get("numSweepingRobotType1") - 1 <= minNumOfSweepingRobotType1) {
            setExecuted(true);
            ret[1] = 1; //1 tick 소요됨
            return ret;
        }

        if(--remainTime == 0) {
            ret[0] = getCost();
            ret[1] = getLatency();
            int currentSweepingRobotType1 = configuration.getConfigurations().get("numSweepingRobotType1");
            csModelList.remove(String.format("SweepingRobotType1_%s",currentSweepingRobotType1)); //remove CS from the csModelList
            configuration.getConfigurations().put("numSweepingRobotType1", configuration.getConfigurations().get("numSweepingRobotType1") - 1);
            setExecuted(true);

        }
        return ret;
    }
}
