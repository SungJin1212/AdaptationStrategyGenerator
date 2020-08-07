package Model.GeneratedCode.Tactics;

import Model.AbstactClass.Rule.Configuration;
import Model.AbstactClass.Rule.Tactic;

import static Model.AbstactClass.Behavior.SoS.csModelList;
import static Model.GeneratedCode.Behavior.CleaningSoS.totalcost;

public class RemoveSweepingRobotType2 extends Tactic {
    private int remainTime = getLatency();

    public RemoveSweepingRobotType2() {

        super(3, 3, "RemoveSweepingRobotType2");
    }

    @Override
    public void run(Configuration configuration) {
        if(--remainTime == 0) {
            remainTime = getLatency(); // 시간 초기화
            totalcost += getCost(); // cost update
            int currentSweepingRobotType2 = configuration.getConfiguration().get("numSweepingRobotType2");
            csModelList.remove(String.format("SweepingRobotType2_%s",currentSweepingRobotType2));
            configuration.getConfiguration().put("numSweepingRobotType2", configuration.getConfiguration().get("numSweepingRobotType2") - 1);
        }
    }

}
