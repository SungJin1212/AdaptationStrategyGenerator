package Model.GeneratedCode.Tactics;

import Model.AbstactClass.Behavior.CS;
import Model.AbstactClass.Rule.Configuration;
import Model.AbstactClass.Rule.Tactic;

import static Model.AbstactClass.Behavior.SoS.csModelList;
import static Model.AbstactClass.Behavior.SoS.csSpecificationList;

public class AddSweepingRobotType2 extends Tactic implements Cloneable {
    private double remainTime = getLatency();

    public AddSweepingRobotType2() {

        super(3, 3, "AddSweepingRobotType2");
    }

    @Override
    public double[] run(Configuration configuration) throws CloneNotSupportedException {
        double[] ret = new double[2];

        if(--remainTime == 0) {
            ret[0] = getCost();
            ret[1] = getLatency();
            int currentSweepingRobotType2 = configuration.getConfiguration().get("numSweepingRobotType2");
            csModelList.put(String.format("SweepingRobotType2_%s",currentSweepingRobotType2 + 1), (CS) csSpecificationList.get("SweepingRobotType2").clone());
            configuration.getConfiguration().put("numSweepingRobotType2", configuration.getConfiguration().get("numSweepingRobotType2") + 1);
            setExecuted(true);
        }
        return ret;
    }

}
