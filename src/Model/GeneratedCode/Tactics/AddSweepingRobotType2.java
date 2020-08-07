package Model.GeneratedCode.Tactics;

import Model.AbstactClass.Behavior.CS;
import Model.AbstactClass.Rule.Configuration;
import Model.AbstactClass.Rule.Tactic;

import static Model.AbstactClass.Behavior.SoS.csModelList;
import static Model.AbstactClass.Behavior.SoS.csSpecificationList;
import static Model.GeneratedCode.Behavior.CleaningSoS.totalcost;

public class AddSweepingRobotType2 extends Tactic {
    private int remainTime = getLatency();

    public AddSweepingRobotType2() {

        super(3, 3, "AddSweepingRobotType2");
    }

    @Override
    public void run(Configuration configuration) throws CloneNotSupportedException {
        if(--remainTime == 0) {
            remainTime = getLatency(); // 시간 초기화
            totalcost += getCost(); // cost update
            int currentSweepingRobotType2 = configuration.getConfiguration().get("numSweepingRobotType2");
            csModelList.put(String.format("SweepingRobotType2_%s",currentSweepingRobotType2 + 1), (CS) csSpecificationList.get("SweepingRobotType2").clone());
            configuration.getConfiguration().put("numSweepingRobotType2", configuration.getConfiguration().get("numSweepingRobotType2") + 1);
        }
    }

}
