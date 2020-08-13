package Model.GeneratedCode.Tactics;

import Model.AbstactClass.Rule.Configuration;
import Model.AbstactClass.Rule.Tactic;

import static Model.AbstactClass.Behavior.SoS.csModelList;
import static Model.GeneratedCode.Behavior.CleaningSoS.minNumOfMoppingRobotType2;

public class RemoveMoppingRobotType2 extends Tactic implements Cloneable {
    private double remainTime = getLatency();

    public RemoveMoppingRobotType2() {

        super(3, 3, "RemoveMoppingRobotType2");
    }

    @Override
    public double[] run(Configuration configuration) throws CloneNotSupportedException {
        double[] ret = new double[2]; //cost, latency

        if(configuration.getConfiguration().get("numMoppingRobotType2") - 1 <= minNumOfMoppingRobotType2) {
            setExecuted(true);
            ret[1] = 1; //1 tick 소요됨
            return ret;
        }

        if(--remainTime == 0) {
            ret[0] = getCost();
            ret[1] = getLatency();
            int currentMoppingRobotType2 = configuration.getConfiguration().get("numMoppingRobotType2");
            csModelList.remove(String.format("MoppingRobotType2_%s",currentMoppingRobotType2));
            configuration.getConfiguration().put("numMoppingRobotType2", configuration.getConfiguration().get("numMoppingRobotType2") - 1);
            setExecuted(true);

        }
        return ret;
    }
}
