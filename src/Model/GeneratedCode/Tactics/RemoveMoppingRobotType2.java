package Model.GeneratedCode.Tactics;

import Model.AbstactClass.Rule.Configuration;
import Model.AbstactClass.Rule.Tactic;
import Model.GeneratedCode.Behavior.CleaningSoS;

import static Model.AbstactClass.Behavior.SoS.csModelList;

public class RemoveMoppingRobotType2 extends Tactic {
    private int remainTime = getLatency();

    public RemoveMoppingRobotType2() {

        super(3, 3, "RemoveMoppingRobotType2");
    }

    @Override
    public void run(Configuration configuration) {
        if(--remainTime == 0) {
            remainTime = getLatency(); // 시간 초기화
            CleaningSoS.totalcost += getCost(); // cost update
            int currentMoppingRobotType2 = configuration.getConfiguration().get("numMoppingRobotType2");
            csModelList.remove(String.format("MoppingRobotType2_%s",currentMoppingRobotType2));
            configuration.getConfiguration().put("numMoppingRobotType2", configuration.getConfiguration().get("numMoppingRobotType2") - 1);
        }
    }
}
