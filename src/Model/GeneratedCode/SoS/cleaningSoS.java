package Model.GeneratedCode.SoS;

import Model.AbstactClass.SoS;
import Model.GeneratedCode.Behavior.MoppingRobot;
import Model.GeneratedCode.Behavior.SweepingRobot;
import Model.GeneratedCode.Behavior.Tile;
import Model.GeneratedCode.Rule.Configuration;

public class cleaningSoS extends SoS {

    /*Global Variables*/

    public static double successPro = 0.9;

    public static int MapSize = 100;
    public static int[][] tileMap = new int[MapSize+1][MapSize+1]; //2: Dirty, 1: Swept, 0: Mopped
    public static int totalcost = 0;
    public static int moppedTile = 0;
    public static int sweeppedTile = 0;



    public cleaningSoS(Configuration configuration) {
        super();
        init(configuration);
    }

    private void init(Configuration configuration) {
        totalcost = 0;
        moppedTile = 0;
        sweeppedTile = 0;
        for(int i=1; i<=MapSize; i++) {
            for(int j=1; j<=MapSize; j++) {
                tileMap[i][j] = 2;
            }
        }
        AddSystemSpecification();
        AddCSs(configuration);
        AddActiveEnvironments(configuration);
        AddPassiveEnvironments();
    }



    private void AddSystemSpecification() {
        csSpecificationList.put("SweepingRobotType1", new SweepingRobot(1, 10));
        csSpecificationList.put("SweepingRobotType2", new SweepingRobot(4, 40));
        csSpecificationList.put("SweepingRobotType3", new SweepingRobot(8, 80));
        csSpecificationList.put("MoppingRobotType1", new MoppingRobot(1, 10));
        csSpecificationList.put("MoppingRobotType2", new MoppingRobot(4, 40));
        csSpecificationList.put("MoppingRobotType3", new MoppingRobot(8, 80));
    }
    private void AddCSs(Configuration configuration) {
        for(int i=1; i<=configuration.getNumSweepingRobotType1(); i++) {csModelList.put(String.format("SweepingRobotType1_%s",i), new SweepingRobot(1, 10));}
        for(int i=1; i<=configuration.getNumSweepingRobotType2(); i++) {csModelList.put(String.format("SweepingRobotType2_%s",i), new SweepingRobot(4, 40));}
        for(int i=1; i<=configuration.getNumSweepingRobotType3(); i++) {csModelList.put(String.format("SweepingRobotType3_%s",i), new SweepingRobot(8, 80));}
        for(int i=1; i<=configuration.getNumMoppingRobotType1(); i++) {csModelList.put(String.format("MoppingRobotType1_%s",i), new MoppingRobot(1, 10));}
        for(int i=1; i<=configuration.getNumMoppingRobotType2(); i++) {csModelList.put(String.format("MoppingRobotType2_%s",i), new MoppingRobot(4, 40));}
        for(int i=1; i<=configuration.getNumMoppingRobotType3(); i++) {csModelList.put(String.format("MoppingRobotType3_%s",i), new MoppingRobot(8, 80));}

        System.out.println(csModelList.size());
    }

    private void AddActiveEnvironments(Configuration configuration) {
        //activeEnvironmentModelList.put("Dust", new Dust());
    }

    private void AddPassiveEnvironments() {

        for(int i=1; i<=MapSize; i++) {
            for(int j=1; j<=MapSize; j++) {
                passiveEnvironmentModelList.put(String.format("Tile(%s,%s)",i,j), new Tile(i,j));
            }
        }
    }


    public String getFitness() {

        return String.format("totalcost: %s, sweeppedTile: %s, moppedTile: %s",totalcost,sweeppedTile,moppedTile);
    }

}
