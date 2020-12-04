package CleaningSoSCaseStudy.Model.SoS.Tactics;

import Model.SoS.Configuration;
import Model.SoS.Tactic;
import Model.Behavior.CS;

import static Model.SoS.CleaningSoS.maxNumOfSweepingRobotType1;
import static Model.SoS.SoS.csModelList;
import static Model.SoS.SoS.csSpecificationList;

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


