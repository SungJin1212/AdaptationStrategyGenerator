package Model.GeneratedCode.Rule;

import Model.AbstactClass.Rule.Configuration;

public class cleaningSoSConfiguration extends Configuration {


    public cleaningSoSConfiguration(int numSweepingRobotType1, int numSweepingRobotType2, int numMoppingRobotType1, int numMoppingRobotType2) {
        super(6);

        getConfiguration().put("numSweepingRobotType1", numSweepingRobotType1);
        getConfiguration().put("numSweepingRobotType2", numSweepingRobotType2);
        getConfiguration().put("numMoppingRobotType1", numMoppingRobotType1);
        getConfiguration().put("numMoppingRobotType2", numMoppingRobotType2);

    }
}
