package Model.SoS.Tactics;

import Model.SoS.Configuration;
import Model.SoS.Tactic;
import Model.Behavior.CS;

import static Model.SoS.CleaningSoS.maxNumOfMoppingRobotType2;
import static Model.SoS.SoS.csModelList;
import static Model.SoS.SoS.csSpecificationList;

public class AddMoppingRobotType2 extends Tactic implements Cloneable {
    private double remainTime;

    public AddMoppingRobotType2() {
        super(3, 2, "AddMoppingRobotType2");
        remainTime = getLatency();
    }

    @Override
    public double[] run(Configuration configuration) throws CloneNotSupportedException {
        double[] ret = new double[2]; //cost, latency

        if(configuration.getConfigurations().get("numMoppingRobotType2") + 1 >= maxNumOfMoppingRobotType2) {
            setExecuted(true);
            ret[1] = 1; //1 tick 소요됨
            return ret;
        }

        if(--remainTime == 0) {

            ret[0] = getCost();
            ret[1] = getLatency();

            int currentMoppingRobotType2 = configuration.getConfigurations().get("numMoppingRobotType2");
            csModelList.put(String.format("MoppingRobotType1_%s",currentMoppingRobotType2 + 1), (CS) csSpecificationList.get("MoppingRobotType2").clone());
            configuration.getConfigurations().put("numMoppingRobotType2", configuration.getConfigurations().get("numMoppingRobotType2") + 1);
            setExecuted(true);
        }
        return ret;
    }
}
