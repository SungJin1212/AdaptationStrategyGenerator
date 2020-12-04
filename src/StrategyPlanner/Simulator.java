package StrategyPlanner;


import SoS.*;

import static SoS.MCIRSoS.*;
import static StrategyPlanner.CasebasedReasoning.CaseBaseValue.getUtilityValue;


public class Simulator {

    static double saveLimit = 0.7;
    static int INF = 987654321;

    private double run() { //1tick
        double cost = 0;
        //System.out.println(String.format("Before Rescued patient: %f saved patient: %f   %d %d",rescuedPatient,savedPatient,curGroundPatient, curSeaPatient));
        for (String key : EnvironmentModelList.keySet()) {
            EnvironmentModelList.get(key).run();
        }
        //System.out.println(csModelList.size());
        for (String key : csModelList.keySet()) {
            cost += csModelList.get(key).run();
        }
        //System.out.println(SQ.size());
        //System.out.println(String.format("After Rescued patient: %f saved patient: %f   %d %d",rescuedPatient,savedPatient,curGroundPatient,curSeaPatient));
        //System.out.println(cost);
        return cost;  //1 tick operation cost
    }

    private double runStrategy(Configuration configuration, Strategy strategy) throws CloneNotSupportedException { // Output: latency
        Tactic curTactic = null;

        for(Tactic t : strategy.getStrategy()) {


            if(!t.isExecuted()) { // false이면
                curTactic = t;
                break;
            }
        }
        if (curTactic == null) {
            return 0;
        }

        return curTactic.run(configuration,strategy);
    }
    //return utility value
    public double ExecuteStrategyAtRunTime(MCIRSoS mcirSoS, Strategy selectedStrategy, int simulationTime) throws CloneNotSupportedException { // Execute selected strategy
        double cost = 0.0;
        double latency = 0.0;
        //savedPatient = 0;
        //System.out.println(String.format("savedPatient: %f cost: %f curGround: %d curSea: %d rescuedPatient: %f totalRescuedPatient: %f WeatherCondition: %f RoadCondition: %f", savedPatient, cost, curGroundPatient, curSeaPatient, rescuedPatient, totalRescuedPatient, weatherCondition, roadCondition));
        for (int tick = 1; tick <= simulationTime; tick++) {
            cost += run();
            latency += runStrategy(mcirSoS.getSoSConfiguration(), selectedStrategy);
            totalRescuedPatient += rescuedPatient;
            if(savedPatient == curGroundPatient + curSeaPatient) { //만약 다 구했으면 break
                //System.out.println("All Saved");
                break;
            }
        }

        if (savedPatient <= saveLimit * (curGroundPatient + curSeaPatient)) {
            //System.out.println("Can not save");
            savedPatient = 0;
            cost = INF;
            latency = INF;
        }

        //System.out.println(String.format("savedPatient: %f cost: %f curGround: %d curSea: %d rescuedPatient: %f totalRescuedPatient: %f",savedPatient, cost, curGroundPatient, curSeaPatient, rescuedPatient, totalRescuedPatient));
        return getUtilityValue(savedPatient, cost, latency);
    }
    public double[] evaluateStrategy(MCIRSoS mcirSoS, Strategy strategy, int simulationTime, int simulationReps) throws CloneNotSupportedException {
        savedPatient = 0;
        double latency = 0;
        double costWithStrategy = 0;
        double costWithoutStrategy = 0;
        double totalSavedPatientWithStrategy = 0.0;
        double totalSavedPatientWithoutStrategy = 0.0;
        double diffPatientNum;
        double diffCost;


        MCIRSoSEnvironmentCondition mcirSoSEnvironmentCondition = (MCIRSoSEnvironmentCondition)mcirSoS.getSoSEnvironmentCondition();
        MCIRSoSConfiguration mcirSoSconfiguration = (MCIRSoSConfiguration)mcirSoS.getSoSConfiguration().clone();

        /*
        mcirSoS = new MCIRSoS(mcirSoSEnvironmentCondition, (MCIRSoSConfiguration)mcirSoSconfiguration.clone());
        savedPatient = 0;
        for (int tick = 1; tick <= simulationTime; tick++) { //60
            costWithoutStrategy = costWithoutStrategy + run();
            if(savedPatient == curGroundPatient + curSeaPatient) { //만약 다 구했으면 break
                break;
            }
        }
        totalSavedPatientWithoutStrategy += savedPatient;


        */
        savedPatient = 0;

        for(int i = 1; i <=simulationReps; i++) { //30
            Strategy cloneStrategy = (Strategy) strategy.clone();
            mcirSoS = new MCIRSoS(mcirSoSEnvironmentCondition, (MCIRSoSConfiguration)mcirSoSconfiguration.clone());

            //System.out.println(String.format("savedPatient: %f cost: %f curGround: %d curSea: %d rescuedPatient: %f totalRescuedPatient: %f WeatherCondition: %f RoadCondition: %f", savedPatient, cost, curGroundPatient, curSeaPatient, rescuedPatient, totalRescuedPatient, weatherCondition, roadCondition));
            savedPatient = 0;
            for (int tick = 1; tick <= simulationTime; tick++) { //60
                costWithStrategy = costWithStrategy + run();
                latency = latency + runStrategy(mcirSoS.getSoSConfiguration(), cloneStrategy);

                if(savedPatient == curGroundPatient + curSeaPatient) { //만약 다 구했으면 break
                    //System.out.println("All Saved");
                    break;
                }

            }
            if (savedPatient <= saveLimit * (curGroundPatient + curSeaPatient)) {// 70 % 이상 못구했으면.. 이 Tactic은 쓸모 X
                //System.out.println("Can not save");
                savedPatient = 0;
                costWithStrategy = INF;
                latency = INF;
            }

            totalSavedPatientWithStrategy += savedPatient;
        }
        //System.out.println(String.format("Latency %f", latency));
        //System.out.println(totalSavedPatientWithStrategy);
        totalSavedPatientWithStrategy = totalSavedPatientWithStrategy / simulationReps;
        costWithStrategy = costWithStrategy / simulationReps;
        latency = latency / simulationReps;

        diffPatientNum = totalSavedPatientWithStrategy - totalSavedPatientWithoutStrategy; //maximize
        diffCost = costWithStrategy - costWithoutStrategy; //minimize

        //System.out.println(String.format("SavedPatient: %f, Cost: %f, Latency: %f", savedPatient,cost,latency));
        //System.out.println(String.format("Latency %f", latency));
       //System.out.println(latency);
        return new double[]{totalSavedPatientWithStrategy, costWithStrategy, latency};
        //return new double[]{getUtilityValue(savedPatient,cost), latency, cost};
    }
}
