package CleaningSoSCaseStudy.Model.SoS;


import Model.SoS.Configuration;

public class CleaningSoSConfiguration extends Configuration implements Cloneable {


    public CleaningSoSConfiguration(int numMoppingRobotType1, int numMoppingRobotType2, int numSweepingRobotType1, int numSweepingRobotType2) {
        super(4);

        getConfigurations().put("numMoppingRobotType1", numMoppingRobotType1);
        getConfigurations().put("numMoppingRobotType2", numMoppingRobotType2);
        getConfigurations().put("numSweepingRobotType1", numSweepingRobotType1);
        getConfigurations().put("numSweepingRobotType2", numSweepingRobotType2);

        getMaxConfigurations().put("numMoppingRobotType1", 20);
        getMaxConfigurations().put("numMoppingRobotType2", 20);
        getMaxConfigurations().put("numSweepingRobotType1", 20);
        getMaxConfigurations().put("numSweepingRobotType2", 20);

        getMinConfigurations().put("numMoppingRobotType1", 1);
        getMinConfigurations().put("numMoppingRobotType2", 1);
        getMinConfigurations().put("numSweepingRobotType1", 1);
        getMinConfigurations().put("numSweepingRobotType2", 1);
    }



}
