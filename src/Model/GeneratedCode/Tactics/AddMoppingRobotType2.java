package Model.GeneratedCode.Tactics;

import Model.AbstactClass.Behavior.CS;
import Model.AbstactClass.Rule.Configuration;
import Model.AbstactClass.Rule.Tactic;

import static Model.AbstactClass.Behavior.SoS.csModelList;
import static Model.AbstactClass.Behavior.SoS.csSpecificationList;

public class AddMoppingRobotType2 extends Tactic implements Cloneable {
    private double remainTime = getLatency();

    public AddMoppingRobotType2() {

        super(3, 3, "AddMoppingRobotType2");
    }

    @Override
    public double[] run(Configuration configuration) throws CloneNotSupportedException {
        double[] ret = new double[2];

        if(--remainTime == 0) {
            ret[0] = getCost();
            ret[1] = getLatency();

            int currentMoppingRobotType2 = configuration.getConfiguration().get("numMoppingRobotType2");
            csModelList.put(String.format("MoppingRobotType1_%s",currentMoppingRobotType2 + 1), (CS) csSpecificationList.get("MoppingRobotType2").clone());
            configuration.getConfiguration().put("numMoppingRobotType2", configuration.getConfiguration().get("numMoppingRobotType2") + 1);
            setExecuted(true);
        }
        return ret;
    }
}
