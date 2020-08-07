package Model.GeneratedCode.Tactics;

import Model.AbstactClass.Rule.Configuration;
import Model.AbstactClass.Rule.Tactic;
import Model.GeneratedCode.Behavior.CleaningSoS;

import static Model.AbstactClass.Behavior.SoS.csModelList;

public class RemoveMoppingRobotType2 extends Tactic implements Cloneable {
    private double remainTime = getLatency();

    public RemoveMoppingRobotType2() {

        super(3, 3, "RemoveMoppingRobotType2");
    }

    @Override
    public double[] run(Configuration configuration) throws CloneNotSupportedException {
        double[] ret = new double[2];

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
