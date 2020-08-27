package StrategyGenerationEngine;

import Model.SoS.Configuration;
import Model.SoS.Strategy;
import Model.SoS.Tactic;
import Model.SoS.SoS;
import StrategyGenerationEngine.Element.StrategyElement;
import org.moeaframework.core.NondominatedPopulation;
import org.moeaframework.core.Solution;
import org.moeaframework.core.variable.EncodingUtils;

import java.util.ArrayList;

import static Model.SoS.SoS.EnvironmentModelList;
import static Model.SoS.SoS.csModelList;

public class Simulator {

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

    public static void ExecuteStrategyAtRunTime(SoS sos, Strategy selectedStrategy, int simulationTime) throws CloneNotSupportedException {

//        for (Tactic t : selectedStrategy.getStrategy()) {
//            System.out.println(t.getClass());
//        }

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

    static double[] evaluateStrategy(SoS sos, Strategy strategy, int simulationTime) throws CloneNotSupportedException {
        SoS.SoSGoal = 0;
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
        return new double[]{SoS.SoSGoal, latency, cost};
    }
    static double evaluateStrategy(NondominatedPopulation result) {
        double ret = 0.0;
        for(Solution solution : result) {
            double SoSGoal = -solution.getObjective(0);
            double Cost = solution.getObjective(1);
            double Latency = solution.getObjective(2);

            /* Only Manual Part*/
            ret += getEachRet(SoSGoal, Cost, Latency);
        }
        return ret;
    }

    private static double getEachRet(double soSGoal, double cost, double latency) {
        return cost + latency == 0 ? soSGoal : (soSGoal / (cost + latency));
    }

    static double evaluateStrategy(ArrayList<StrategyElement> mostSimilarStrategies) {
        double ret =0.0;
        for(StrategyElement strategyElement : mostSimilarStrategies) {
            double SoSGoal = strategyElement.getGoalValueList().get(0);
            double Cost = strategyElement.getGoalValueList().get(1);
            double Latency = strategyElement.getGoalValueList().get(2);
            ret += getEachRet(SoSGoal, Cost, Latency);
        }
        return ret;
    }


    static Solution[] getInitialPopulation(ArrayList<StrategyElement> mostSimilarStrategies) {

        Solution[] solutions = new Solution[mostSimilarStrategies.size()];

        int idx = 0;

        for (StrategyElement strategyElement : mostSimilarStrategies) {
            Solution solution = new Solution(strategyElement.getStrategyValueList().size(), strategyElement.getGoalValueList().size(), strategyElement.getStrategyValueList().size());
            for(int i=0; i<solution.getNumberOfVariables(); i++) {
                solution.setVariable(i, EncodingUtils.newInt(strategyElement.getStrategyValueList().get(0), strategyElement.getStrategyValueList().get(i)));
            }
            solutions[idx] = solution;
            idx++;
        }
        return solutions;
    }

}
