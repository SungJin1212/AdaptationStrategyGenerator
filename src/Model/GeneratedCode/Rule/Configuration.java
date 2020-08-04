package Model.GeneratedCode.Rule;

import java.util.HashMap;
import java.util.Map;

public class Configuration {
    int NumSweepingRobotType1;
    int NumSweepingRobotType2;
    int NumSweepingRobotType3;

    int NumMoppingRobotType1;
    int NumMoppingRobotType2;
    int NumMoppingRobotType3;

    public Configuration(int numSweepingRobotType1, int numSweepingRobotType2, int numSweepingRobotType3, int numMoppingRobotType1, int numMoppingRobotType2, int numMoppingRobotType3) {
        NumSweepingRobotType1 = numSweepingRobotType1;
        NumSweepingRobotType2 = numSweepingRobotType2;
        NumSweepingRobotType3 = numSweepingRobotType3;
        NumMoppingRobotType1 = numMoppingRobotType1;
        NumMoppingRobotType2 = numMoppingRobotType2;
        NumMoppingRobotType3 = numMoppingRobotType3;
    }


    public int getNumSweepingRobotType1() {
        return NumSweepingRobotType1;
    }

    public int getNumSweepingRobotType2() {
        return NumSweepingRobotType2;
    }

    public int getNumSweepingRobotType3() {
        return NumSweepingRobotType3;
    }

    public int getNumMoppingRobotType1() {
        return NumMoppingRobotType1;
    }

    public int getNumMoppingRobotType2() {
        return NumMoppingRobotType2;
    }

    public int getNumMoppingRobotType3() {
        return NumMoppingRobotType3;
    }
}
