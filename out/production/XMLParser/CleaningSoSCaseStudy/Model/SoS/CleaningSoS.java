package CleaningSoSCaseStudy.Model.SoS;


import Model.Behavior.*;
import Model.SoS.Configuration;
import Model.SoS.SoS;
import Model.SoS.Strategy;
import Model.SoS.Tactic;
import Model.SoS.Tactics.*;


public class CleaningSoS extends SoS {

    /*Global Variables*/
    public static int INITIALDUST = 200;
    public static int MopDust = 100;
    public static int dustMax = 400;
    public static int MapSize = 15;
    public static int dustUnit;
    public static int[][] tileMap = new int[MapSize+1][MapSize+1];


    /*Constraint*/
    public static int maxNumOfSweepingRobotType1 = 20;
    public static int minNumOfSweepingRobotType1 = 1;
    public static int maxNumOfSweepingRobotType2 = 20;
    public static int minNumOfSweepingRobotType2 = 1;

    public static int maxNumOfMoppingRobotType1 = 20;
    public static int minNumOfMoppingRobotType1 = 1;
    public static int maxNumOfMoppingRobotType2 = 20;
    public static int minNumOfMoppingRobotType2 = 1;


    public CleaningSoS(CleaningSoSEnvironmentCondition cleaningSoSEnvironmentCondition, CleaningSoSConfiguration cleaningSoSConfiguration) throws CloneNotSupportedException {
        super(cleaningSoSEnvironmentCondition, cleaningSoSConfiguration);
        dustUnit = cleaningSoSEnvironmentCondition.getEnvironmentCondition().get("dustUnit");
        init(cleaningSoSConfiguration);
    }

    public void setRuntimeEnvironment(CleaningSoSEnvironmentCondition cleaningSoSEnvironmentCondition) {
        dustUnit = cleaningSoSEnvironmentCondition.getEnvironmentCondition().get("dustUnit");
        setSoSEnvironmentCondition(cleaningSoSEnvironmentCondition);

//        dustUnit = cleaningSoSEnvironmentCondition.getDustUnit();
    }

    private void init(CleaningSoSConfiguration configuration) throws CloneNotSupportedException {

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
    @Override
    void AddSystemSpecification() {

        csSpecificationList.put("SweepingRobotType1", new SweepingRobot(50));
        csSpecificationList.put("SweepingRobotType2", new SweepingRobot(90));
        csSpecificationList.put("MoppingRobotType1", new MoppingRobot(50));
        csSpecificationList.put("MoppingRobotType2", new MoppingRobot(90));
        csSpecificationList.put("DustController", new DustController());
    }
    @Override
    void AddTacticSpecification() {
        tacticSpecificationList.put("AddMoppingRobotType1", new AddMoppingRobotType1());
        tacticSpecificationList.put("AddMoppingRobotType2", new AddMoppingRobotType2());
        tacticSpecificationList.put("AddSweepingRobotType1", new AddSweepingRobotType1());
        tacticSpecificationList.put("AddSweepingRobotType2", new AddSweepingRobotType2());
        tacticSpecificationList.put("RemoveMoppingRobotType1", new RemoveMoppingRobotType1());
        tacticSpecificationList.put("RemoveMoppingRobotType2", new RemoveMoppingRobotType2());
        tacticSpecificationList.put("RemoveSweepingRobotType1", new RemoveSweepingRobotType1());
        tacticSpecificationList.put("RemoveSweepingRobotType2", new RemoveSweepingRobotType2());
    }
    @Override
    void AddCSs(Configuration configuration) throws CloneNotSupportedException {

        for(int i = 1; i<= configuration.getConfigurations().get("numSweepingRobotType1"); i++) {
            csModelList.put(String.format("SweepingRobotType1_%s",i), (CS) csSpecificationList.get("SweepingRobotType1").clone());
        }
        for(int i = 1; i<= configuration.getConfigurations().get("numSweepingRobotType2"); i++) {
            csModelList.put(String.format("SweepingRobotType2_%s",i), (CS) csSpecificationList.get("SweepingRobotType2").clone());
        }
        for(int i = 1; i<= configuration.getConfigurations().get("numMoppingRobotType1"); i++) {
            csModelList.put(String.format("MoppingRobotType1_%s",i), (CS) csSpecificationList.get("MoppingRobotType1").clone());
        }
        for(int i = 1; i<= configuration.getConfigurations().get("numMoppingRobotType2"); i++) {
            csModelList.put(String.format("MoppingRobotType2_%s",i), (CS) csSpecificationList.get("MoppingRobotType2").clone());
        }

        csModelList.put("DustController", (CS) csSpecificationList.get("DustController").clone());
        //System.out.println(csModelList.size());
    }
    @Override
    void AddEnvironments() {
        for(int i=1; i<=MapSize; i++) {
            for(int j=1; j<=MapSize; j++) {
                EnvironmentModelList.put(String.format("Tile(%s,%s)",i,j), new Tile(i,j));
            }
        }
    }

    public Strategy generateStrategy(int MType1, int MType2, int SType1, int SType2) throws CloneNotSupportedException {
        Strategy strategy = new Strategy();

        if(MType1 >= 0) {
            while(MType1-- != 0) {
                strategy.AddTactic( (Tactic) tacticSpecificationList.get("AddMoppingRobotType1").clone());
            }
        }
        else {
            while(MType1++ != 0) {
                strategy.AddTactic( (Tactic) tacticSpecificationList.get("RemoveMoppingRobotType1").clone());
            }
        }
        if(MType2 >= 0) {
            while(MType2-- != 0) {
                strategy.AddTactic( (Tactic) tacticSpecificationList.get("AddMoppingRobotType2").clone());
            }
        }
        else {
            while(MType2++ != 0) {
                strategy.AddTactic( (Tactic) tacticSpecificationList.get("RemoveMoppingRobotType2").clone());
            }
        }
        if(SType1 >= 0) {
            while(SType1-- != 0) {
                strategy.AddTactic( (Tactic) tacticSpecificationList.get("AddSweepingRobotType1").clone());
            }
        }
        else {
            while(SType1++ != 0) {
                strategy.AddTactic( (Tactic) tacticSpecificationList.get("RemoveSweepingRobotType1").clone());
            }
        }
        if(SType2 >= 0) {
            while(SType2-- != 0) {
                strategy.AddTactic( (Tactic) tacticSpecificationList.get("AddSweepingRobotType2").clone());
            }
        }
        else {
            while(SType2++ != 0) {
                strategy.AddTactic((Tactic) tacticSpecificationList.get("RemoveSweepingRobotType2").clone());
            }
        }
        return strategy;
    }

}
