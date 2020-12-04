package SoS;

import Behavior.*;
import SoS.Tactics.*;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;


public class MCIRSoS extends SoS {

    public static double patientEmergingRate = 1.0;
    public static int MAPSIZE = 50;
    public static int[][] GroundMap = new int[MAPSIZE + 1][MAPSIZE + 1];
    public static int[][] SeaMap = new int[MAPSIZE + 1][MAPSIZE + 1];
    public static double totalRescuedPatient = 0;
    //public static double emergedPatient = 0;
    public static int Sight = 7;
    public static int maxSeaPatient = 1000;
    public static int maxGroundPatient = 1000;
    public static int AmbulanceNum = 4;
    public static int AmbulancePosY = 0;
    public static int AmbulancePosX = 0;

    public static int curSeaPatient;
    public static int curGroundPatient;
    public static double rescuedPatient;
    public static double savedPatient;
    public static Queue<Pair> GQ = new LinkedList<Pair>();
    public static Queue<Pair> SQ = new LinkedList<Pair>();

    public static Map<String, CS> csModelList = new HashMap<>(0);
    public static Map<String, Environment> EnvironmentModelList = new HashMap<>(0);
    public static Map<String, CS> csSpecificationList = new HashMap<>(0);
    public static Map<String, Tactic> tacticSpecificationList = new HashMap<>(0);
    /*Environment Vars*/
    public static double weatherCondition; // Influence to sight of Firefighter and Helicopter (1,2,3)
    public static double roadCondition; // Influence to time to transfer (1,2,3,4)


    public MCIRSoS(MCIRSoSEnvironmentCondition mcirSoSEnvironmentCondition, MCIRSoSConfiguration mcirSoSConfiguration) throws CloneNotSupportedException {
        super(mcirSoSEnvironmentCondition,mcirSoSConfiguration);
        weatherCondition = mcirSoSEnvironmentCondition.getEnvironmentCondition().get("weatherCondition");
        roadCondition = mcirSoSEnvironmentCondition.getEnvironmentCondition().get("roadCondition");
        //System.out.println(String.format("Before %f", savedPatient));
        initStaticVars();
        //System.out.println(String.format("After %f", savedPatient));

        initSystemSpecification();
        initEnvironments();
        initTacticSpecification();
        initCS(mcirSoSConfiguration);

        //System.out.println(csModelList.size());
    }

    private void initStaticVars() {
        //System.out.println(String.format("Before %d", curSeaPatient));
        curSeaPatient = 0;
        //System.out.println(String.format("After %d", curSeaPatient));
        curGroundPatient = 0;
        rescuedPatient = 0;
        savedPatient = 0;
        for(int i = 1; i<=MAPSIZE; i++) {
            for(int j=1; j<=MAPSIZE; j++) {
                GroundMap[i][j] = 0;
                SeaMap[i][j] = 0;
            }
        }
        csModelList.clear();
        EnvironmentModelList.clear();
        csSpecificationList.clear();
        tacticSpecificationList.clear();
        GQ.clear();
        SQ.clear();


    }

    public void setRuntimeEnvironment(MCIRSoSEnvironmentCondition mcirSoSEnvironmentCondition) {
        weatherCondition = mcirSoSEnvironmentCondition.getEnvironmentCondition().get("weatherCondition");
        roadCondition = mcirSoSEnvironmentCondition.getEnvironmentCondition().get("roadCondition");

        setSoSEnvironmentCondition(mcirSoSEnvironmentCondition);

//        dustUnit = cleaningSoSEnvironmentCondition.getDustUnit();
    }

    @Override
    void initSystemSpecification() {
        csSpecificationList.put("Firefighter", new Firefighter(0.1));
        csSpecificationList.put("Helicopter", new Helicopter(0.1));
        csSpecificationList.put("Ambulance", new Ambulance(0.1));
    }
    @Override
    void initEnvironments() {
        for(int i = 1; i<=MAPSIZE; i++) {
            for(int j=1; j<=MAPSIZE; j++) {
                EnvironmentModelList.put(String.format("GroundPatient(%d,%d)",i,j), new GroundPatient(i,j));
            }
        }
        for(int i = 1; i<=MAPSIZE; i++) {
            for(int j=1; j<=MAPSIZE; j++) {
                EnvironmentModelList.put(String.format("SeaPatient(%d,%d)",i,j), new SeaPatient(i,j));
            }
        }
    }

    @Override
    void initTacticSpecification() {
        tacticSpecificationList.put("AddFirefighter", new AddFirefighter());
        tacticSpecificationList.put("AddHelicopter", new AddHelicopter());
        tacticSpecificationList.put("AddAmbulance", new AddAmbulance());
        tacticSpecificationList.put("RemoveFirefighter", new RemoveFirefighter());
        tacticSpecificationList.put("RemoveHelicopter", new RemoveHelicopter());
        tacticSpecificationList.put("RemoveAmbulance", new RemoveAmbulance());
    }


    @Override
    void initCS(Configuration configuration) throws CloneNotSupportedException {
        for (int i=1; i<= configuration.getConfigurations().get("numOfFirefighter"); i++) {
            csModelList.put(String.format("Firefighter_%s",i), (CS) csSpecificationList.get("Firefighter").clone());
        }
        for (int i=1; i<= configuration.getConfigurations().get("numOfHelicopter"); i++) {
            csModelList.put(String.format("Helicopter_%s",i), (CS) csSpecificationList.get("Helicopter").clone());
        }
        for (int i=1; i<= configuration.getConfigurations().get("numOfAmbulance"); i++) {
            csModelList.put(String.format("Ambulance_%s",i), (CS) csSpecificationList.get("Ambulance").clone());
        }
    }

    public Strategy getStrategy(int numOfDiffFirefighter, int numDiffOfHelicopter, int numOfDiffAmbulance) throws CloneNotSupportedException {
        Strategy strategy = new Strategy();

        int i = numOfDiffFirefighter;
        int j = numDiffOfHelicopter;
        int k = numOfDiffAmbulance;

        while ((Math.abs(i)+Math.abs(j)+Math.abs(k) != 0)) {
            if (i > 0) {
                strategy.AddTactic((Tactic) tacticSpecificationList.get("AddFirefighter").clone());
                i--;
            } else if (i < 0) {
                strategy.AddTactic((Tactic) tacticSpecificationList.get("RemoveFirefighter").clone());
                i++;
            }

            if (j > 0) {
                strategy.AddTactic((Tactic) tacticSpecificationList.get("AddHelicopter").clone());
                j--;

            } else if (j < 0) {
                strategy.AddTactic((Tactic) tacticSpecificationList.get("RemoveHelicopter").clone());
                j++;
            }
            if (k > 0) {
                strategy.AddTactic( (Tactic) tacticSpecificationList.get("AddAmbulance").clone());
                k--;
            }
            else if (k < 0) {
                strategy.AddTactic((Tactic) tacticSpecificationList.get("RemoveAmbulance").clone());
                k++;
            }
        }

        /*
        if (numOfDiffFirefighter >= 0) {
            while(numOfDiffFirefighter-- != 0) {
                strategy.AddTactic( (Tactic) tacticSpecificationList.get("AddFirefighter").clone());
            }
        }
        else {
            while(numOfDiffFirefighter++ != 0) {
                strategy.AddTactic((Tactic) tacticSpecificationList.get("RemoveFirefighter").clone());
            }
        }

        if (numDiffOfHelicopter >= 0) {
            while(numDiffOfHelicopter-- != 0) {
                strategy.AddTactic( (Tactic) tacticSpecificationList.get("AddHelicopter").clone());
            }
        }
        else {
            while(numDiffOfHelicopter++ != 0) {
                strategy.AddTactic((Tactic) tacticSpecificationList.get("RemoveHelicopter").clone());
            }
        }

        if (numOfDiffAmbulance >= 0) {
            while(numOfDiffAmbulance-- != 0) {
                strategy.AddTactic( (Tactic) tacticSpecificationList.get("AddAmbulance").clone());
            }
        }
        else {
            while(numOfDiffAmbulance++ != 0) {
                strategy.AddTactic((Tactic) tacticSpecificationList.get("RemoveAmbulance").clone());
            }
        }
        */
        return strategy;
    }
}
