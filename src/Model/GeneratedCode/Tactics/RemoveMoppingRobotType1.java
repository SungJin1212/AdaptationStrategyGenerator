package Model.GeneratedCode.Tactics;

import Model.AbstactClass.Rule.Configuration;
import Model.AbstactClass.Rule.Tactic;
import Model.GeneratedCode.Behavior.CleaningSoS;

import static Model.AbstactClass.Behavior.SoS.csModelList;

public class RemoveMoppingRobotType1 extends Tactic {

    private int remainTime = getLatency();

    public RemoveMoppingRobotType1() {

        super(3, 3, "RemoveMoppingRobotType1");
    }

    @Override
    public void run(Configuration configuration) {
        if(--remainTime == 0) {
            remainTime = getLatency(); // 시간 초기화
            CleaningSoS.totalcost += getCost(); // cost update
            int currentMoppingRobotType1 = configuration.getConfiguration().get("numMoppingRobotType1");
            csModelList.remove(String.format("MoppingRobotType1_%s",currentMoppingRobotType1));
            configuration.getConfiguration().put("numMoppingRobotType1", configuration.getConfiguration().get("numMoppingRobotType1") - 1);
        }
    }
}
