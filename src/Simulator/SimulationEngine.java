package Simulator;

import Model.AbstactClass.Behavior.SoS;
import Model.AbstactClass.Rule.Configuration;
import Model.AbstactClass.Rule.Strategy;
import Model.AbstactClass.Rule.Tactic;

import static Model.AbstactClass.Behavior.SoS.EnvironmentModelList;
import static Model.AbstactClass.Behavior.SoS.csModelList;
import static Model.GeneratedCode.Behavior.CleaningSoS.SoSGoal;

public class SimulationEngine {

    private static void run() { //run active Environment -> cs
        for(String key : EnvironmentModelList.keySet()) {
            EnvironmentModelList.get(key).run();
        }
        for(String key : csModelList.keySet()) {
            csModelList.get(key).run();
        }
    }

    private static double[] runStrategy(Configuration configuration, Strategy strategy) throws CloneNotSupportedException {
        Tactic curTactic = null;

        for(Tactic t : strategy.getStrategy()) {
            if(!t.isExecuted()) {
                curTactic = t;
                break;
            }
        }
        if (curTactic == null) {
            return new double[] {0.0,0.0};
        }

        return curTactic.run(configuration);
    }

    public static double[] getFitness(SoS sos, Strategy strategy, int simulationTime) throws CloneNotSupportedException {
        SoSGoal = 0;
        double latency = 0.0;
        double cost = 0.0;
        double[] tempValues = new double[2];

        for(int tick = 1; tick <= simulationTime; tick++) {
            tempValues[0] = 0.0;
            tempValues[1] = 0.0;

            run();
            tempValues = runStrategy(sos.getSoSConfiguration(), strategy);

            cost += tempValues[0];
            latency += tempValues[1];
        }
        return new double[]{SoSGoal, latency, cost};
    }

    public static void ExecuteStrategyAtRunTime(SoS sos, Strategy selectedStrategy, int simulationTime) throws CloneNotSupportedException {

        double latency = 0.0;
        double cost = 0.0;
        double[] tempValues = new double[2];

        for(int tick = 1; tick <= simulationTime; tick++) {
            tempValues[0] = 0.0;
            tempValues[1] = 0.0;

            run();
            tempValues = runStrategy(sos.getSoSConfiguration(), selectedStrategy);

            cost += tempValues[0];
            latency += tempValues[1];
        }



        //strategy construct
        //execute the strategy


    }

}
