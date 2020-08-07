package Model.GeneratedCode.Tactics;

import Model.AbstactClass.Behavior.CS;
import Model.AbstactClass.Rule.Configuration;
import Model.AbstactClass.Rule.Tactic;
import Model.GeneratedCode.Behavior.MoppingRobot;
import Model.GeneratedCode.Behavior.CleaningSoS;

import static Model.AbstactClass.Behavior.SoS.csModelList;
import static Model.AbstactClass.Behavior.SoS.csSpecificationList;

public class AddMoppingRobotType2 extends Tactic {
    private int remainTime = getLatency();

    public AddMoppingRobotType2() {

        super(3, 3, "AddMoppingRobotType2");
    }

    @Override
    public void run(Configuration configuration) throws CloneNotSupportedException {
        if(--remainTime == 0) {
            remainTime = getLatency(); // 시간 초기화
            CleaningSoS.totalcost += getCost(); // cost update
            int currentMoppingRobotType2 = configuration.getConfiguration().get("numMoppingRobotType2");
            csModelList.put(String.format("MoppingRobotType1_%s",currentMoppingRobotType2 + 1), (CS) csSpecificationList.get("MoppingRobotType2").clone());
            configuration.getConfiguration().put("numMoppingRobotType2", configuration.getConfiguration().get("numMoppingRobotType2") + 1);
        }
    }
}
