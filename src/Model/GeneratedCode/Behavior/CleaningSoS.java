package Model.GeneratedCode.Behavior;


import Model.AbstactClass.Behavior.CS;
import Model.AbstactClass.Behavior.SoS;
import Model.GeneratedCode.Rule.EnvironmentCondition;
import Model.GeneratedCode.Rule.cleaningSoSConfiguration;
import Model.GeneratedCode.Tactics.*;

public class CleaningSoS extends SoS {


    /*Global Variables*/
    public static int INITIALDUST = 200;
    public static int MopDust = 100;


    public static int MapSize = 200;
    public static int dustunit;
    public static int[][] tileMap = new int[MapSize+1][MapSize+1];

    public static int totalcost = 0;
    public static int moppedTile = 0;
    public static int sweeppedTile = 0;
    public static int dustLevel;

    public CleaningSoS(EnvironmentCondition environmentCondition, cleaningSoSConfiguration configuration) throws CloneNotSupportedException {
        super();
        dustunit = environmentCondition.getDustUnit();
        init(configuration);
    }

    public void setRuntimeEnvironment(EnvironmentCondition environmentCondition) {
        dustunit = environmentCondition.getDustUnit();
    }

    private void init(cleaningSoSConfiguration configuration) throws CloneNotSupportedException {
        totalcost = 0;
        moppedTile = 0;
        sweeppedTile = 0;

        InitGlobalVariables();
        AddSystemSpecification();
        AddTacticSpecification();
        AddCSs(configuration);
        AddEnvironments();
    }

    private void InitGlobalVariables() {
        for(int i=1; i<=MapSize; i++) {
            for(int j=1; j<=MapSize; j++) {
                tileMap[i][j] = INITIALDUST;
            }
        }
    }

    private void AddSystemSpecification() {
        csSpecificationList.put("SweepingRobotType1", new SweepingRobot(10));
        csSpecificationList.put("SweepingRobotType2", new SweepingRobot(60));
        csSpecificationList.put("MoppingRobotType1", new MoppingRobot(10));
        csSpecificationList.put("MoppingRobotType2", new MoppingRobot(60));
        csSpecificationList.put("DustController", new DustController());
    }

    private void AddTacticSpecification() {
        tacticSpecificationList.put("AddMoppingRobotType1", new AddMoppingRobotType1());
        tacticSpecificationList.put("AddMoppingRobotType2", new AddMoppingRobotType2());
        tacticSpecificationList.put("AddSweepingRobotType1", new AddSweepingRobotType1());
        tacticSpecificationList.put("AddSweepingRobotType2", new AddSweepingRobotType2());
        tacticSpecificationList.put("RemoveMoppingRobotType1", new RemoveMoppingRobotType1());
        tacticSpecificationList.put("RemoveMoppingRobotType2", new RemoveMoppingRobotType2());
        tacticSpecificationList.put("RemoveSweepingRobotType1", new RemoveSweepingRobotType1());
        tacticSpecificationList.put("RemoveSweepingRobotType2", new RemoveSweepingRobotType2());
    }



    private void AddCSs(cleaningSoSConfiguration configuration) throws CloneNotSupportedException {

        for(int i = 1; i<= configuration.getConfiguration().get("numSweepingRobotType1"); i++) {
            csModelList.put(String.format("SweepingRobotType1_%s",i), (CS) csSpecificationList.get("SweepingRobotType1").clone());
        }
        for(int i = 1; i<= configuration.getConfiguration().get("numSweepingRobotType2"); i++) {
            csModelList.put(String.format("SweepingRobotType2_%s",i), (CS) csSpecificationList.get("SweepingRobotType2").clone());
        }
        for(int i = 1; i<= configuration.getConfiguration().get("numMoppingRobotType1"); i++) {
            csModelList.put(String.format("MoppingRobotType1_%s",i), (CS) csSpecificationList.get("MoppingRobotType1").clone());
        }
        for(int i = 1; i<= configuration.getConfiguration().get("numMoppingRobotType2"); i++) {
            csModelList.put(String.format("MoppingRobotType2_%s",i), (CS) csSpecificationList.get("MoppingRobotType2").clone());
        }

        csModelList.put("DustController", (CS) csSpecificationList.get("DustController").clone());
        //System.out.println(csModelList.size());
    }

    private void AddEnvironments() {

        for(int i=1; i<=MapSize; i++) {
            for(int j=1; j<=MapSize; j++) {
                EnvironmentModelList.put(String.format("Tile(%s,%s)",i,j), new Tile(i,j));
            }
        }
        //EnvironmentModelList.put("Dust", new Dust());
    }


    public double[] getFitness() {

        return new double[]{totalcost,moppedTile};
    }
}
