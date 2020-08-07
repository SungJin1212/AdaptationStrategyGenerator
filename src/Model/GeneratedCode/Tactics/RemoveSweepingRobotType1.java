package Model.GeneratedCode.Tactics;

import Model.AbstactClass.Rule.Configuration;
import Model.AbstactClass.Rule.Tactic;

import static Model.AbstactClass.Behavior.SoS.csModelList;
import static Model.GeneratedCode.Behavior.CleaningSoS.totalcost;

public class RemoveSweepingRobotType1 extends Tactic {
    private int remainTime = getLatency();

    public RemoveSweepingRobotType1() {

        super(3, 3, "RemoveSweepingRobotType1");
    }

    @Override
    public void run(Configuration configuration) {
        if(--remainTime == 0) {
            remainTime = getLatency(); // 시간 초기화
            totalcost += getCost(); // cost update
            int currentSweepingRobotType1 = configuration.getConfiguration().get("numSweepingRobotType1");
            csModelList.remove(String.format("SweepingRobotType1_%s",currentSweepingRobotType1));
            configuration.getConfiguration().put("numSweepingRobotType1", configuration.getConfiguration().get("numSweepingRobotType1") - 1);
        }
    }
}
