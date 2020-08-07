package Model.GeneratedCode.Tactics;

import Model.AbstactClass.Behavior.CS;
import Model.AbstactClass.Rule.Configuration;
import Model.AbstactClass.Rule.Tactic;
import Model.GeneratedCode.Behavior.SweepingRobot;

import static Model.AbstactClass.Behavior.SoS.csModelList;
import static Model.AbstactClass.Behavior.SoS.csSpecificationList;
import static Model.GeneratedCode.Behavior.CleaningSoS.totalcost;

public class AddSweepingRobotType1 extends Tactic {

    private int remainTime = getLatency();

    public AddSweepingRobotType1() {

        super(3, 3, "AddSweepingRobotType1");
    }

    @Override
    public void run(Configuration configuration) throws CloneNotSupportedException {
        if(--remainTime == 0) {
            remainTime = getLatency(); // 시간 초기화
            totalcost += getCost(); // cost update
            int currentSweepingRobotType1 = configuration.getConfiguration().get("numSweepingRobotType1");
            csModelList.put(String.format("SweepingRobotType1_%s",currentSweepingRobotType1 + 1), (CS) csSpecificationList.get("SweepingRobotType1").clone());
            configuration.getConfiguration().put("numSweepingRobotType1", configuration.getConfiguration().get("numSweepingRobotType1") + 1);
        }
    }

}


