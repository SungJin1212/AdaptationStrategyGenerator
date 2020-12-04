package CleaningSoSCaseStudy.Model.SoS.Tactics;

import Model.SoS.Configuration;
import Model.SoS.Tactic;

import static Model.SoS.CleaningSoS.minNumOfMoppingRobotType2;
import static Model.SoS.SoS.csModelList;

public class RemoveMoppingRobotType2 extends Tactic implements Cloneable {
    private double remainTime;

    public RemoveMoppingRobotType2() {

        super(1, 3, "RemoveMoppingRobotType2");
        remainTime = getLatency();
    }

    @Override
    public double[] run(Configuration configuration) throws CloneNotSupportedException {
        double[] ret = new double[2]; //cost, latency

        if(configuration.getConfigurations().get("numMoppingRobotType2") - 1 <= minNumOfMoppingRobotType2) {
            setExecuted(true);
            ret[1] = 1; //1 tick 소요됨
            return ret;
        }

        if(--remainTime == 0) {
            ret[0] = getCost();
            ret[1] = getLatency();
            int currentMoppingRobotType2 = configuration.getConfigurations().get("numMoppingRobotType2");
            csModelList.remove(String.format("MoppingRobotType2_%s",currentMoppingRobotType2));
            configuration.getConfigurations().put("numMoppingRobotType2", configuration.getConfigurations().get("numMoppingRobotType2") - 1);
            setExecuted(true);

        }
        return ret;
    }
}
