package Model.GeneratedCode.Tactics;

import Model.AbstactClass.Rule.Configuration;
import Model.AbstactClass.Rule.Tactic;

import static Model.AbstactClass.Behavior.SoS.csModelList;

public class RemoveMoppingRobotType1 extends Tactic implements Cloneable {

    private double remainTime = getLatency();

    public RemoveMoppingRobotType1() {

        super(3, 3, "RemoveMoppingRobotType1");
    }

    @Override
    public double[] run(Configuration configuration) throws CloneNotSupportedException {
        double[] ret = new double[2];

        if(--remainTime == 0) {
            ret[0] = getCost();
            ret[1] = getLatency();

            int currentMoppingRobotType1 = configuration.getConfiguration().get("numMoppingRobotType1");
            csModelList.remove(String.format("MoppingRobotType1_%s",currentMoppingRobotType1));
            configuration.getConfiguration().put("numMoppingRobotType1", configuration.getConfiguration().get("numMoppingRobotType1") - 1);
            setExecuted(true);

        }
        return ret;
    }
}
