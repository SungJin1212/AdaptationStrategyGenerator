package Model.GeneratedCode.Tactics;

import Model.AbstactClass.Behavior.CS;
import Model.AbstactClass.Rule.Configuration;
import Model.AbstactClass.Rule.Tactic;
import Model.GeneratedCode.Behavior.MoppingRobot;

import static Model.AbstactClass.Behavior.SoS.csModelList;
import static Model.AbstactClass.Behavior.SoS.csSpecificationList;
import static Model.GeneratedCode.Behavior.CleaningSoS.totalcost;

public class AddMoppingRobotType1 extends Tactic {

    private int remainTime = getLatency();

    public AddMoppingRobotType1() {

        super(3, 3, "AddMoppingRobotType1");
    }

    @Override
    public void run(Configuration configuration) throws CloneNotSupportedException {
        if(--remainTime == 0) {
            remainTime = getLatency(); // 시간 초기화
            totalcost += getCost(); // cost update
            int currentMoppingRobotType1 = configuration.getConfiguration().get("numMoppingRobotType1");
            csModelList.put(String.format("MoppingRobotType1_%s",currentMoppingRobotType1 + 1), (CS) csSpecificationList.get("MoppingRobotType1").clone());
            configuration.getConfiguration().put("numMoppingRobotType1", configuration.getConfiguration().get("numMoppingRobotType1") + 1);
        }
    }
}
