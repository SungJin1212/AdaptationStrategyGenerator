package StrategyPlanner.CasebasedReasoning;

import SoS.EnvironmentCondition;
import org.moeaframework.core.NondominatedPopulation;
import org.moeaframework.core.Solution;

import java.util.ArrayList;

import static SoS.MCIRSoS.maxGroundPatient;
import static SoS.MCIRSoS.maxSeaPatient;

public class CaseBaseValue implements Cloneable {

    private EnvironmentCondition environmentCondition;
    private ArrayList<StrategyElement> strategyList;
    private double averageFitness;

    public CaseBaseValue(EnvironmentCondition environmentCondition) {
        this.environmentCondition = environmentCondition;
        strategyList = new ArrayList<>(0);
    }

    public CaseBaseValue(ArrayList<StrategyElement> strategyList) {
        this.strategyList = strategyList;
    }

    public EnvironmentCondition getEnvironmentCondition() {
        return environmentCondition;
    }

    public void setEnvironmentCondition(EnvironmentCondition environmentCondition) {
        this.environmentCondition = environmentCondition;
    }

    public ArrayList<StrategyElement> getStrategyList() {
        return strategyList;
    }

    public void setStrategyList(ArrayList<StrategyElement> strategyList) {
        this.strategyList = strategyList;
    }

    public void setAverageFitness(double averageFitness) {
        this.averageFitness = averageFitness;
    }

    public void addStrategy(ArrayList<StrategyElement> strategyElements) {
        strategyList.addAll(strategyElements);
    }

    public StrategyElement getBestStrategy() {
        StrategyElement bestStrategy = new StrategyElement();
        double maxFitness = -987654321;

        for (StrategyElement strategyElement : strategyList) {
            double curFitness = getStrategyFitnessValue(strategyElement);

            if (maxFitness < curFitness) {
                maxFitness = curFitness;
                bestStrategy.setGoalValueList(strategyElement.getGoalValueList());
                bestStrategy.setStrategyValueList(strategyElement.getStrategyValueList());
            }
        }
        return bestStrategy;
    }

    public double[] getObjectiveValuesAvg() {
        double[] ret = new double[3];

        double sumSoSGoalAchievement = 0.0;
        double cost = 0.0;
        double latency = 0.0;
        for(StrategyElement strategyElement : strategyList) {
            sumSoSGoalAchievement += strategyElement.getGoalValueList().get(0);
            cost += strategyElement.getGoalValueList().get(1);
            latency += strategyElement.getGoalValueList().get(2);
        }
        ret[0] = sumSoSGoalAchievement / strategyList.size();
        ret[1] = cost / strategyList.size();
        ret[2] = latency / strategyList.size();

        return ret;
    }

    public void calAverageFitness() {
        double ret = 0.0;
        for (StrategyElement strategyElement : strategyList) {
            ret += getStrategyFitnessValue(strategyElement);
        }
        //System.out.println(strategyList.size());
        ret = ret / strategyList.size();
        averageFitness = ret;
    }

    public double getAverageFitness() {
        double ret = 0.0;

        for (StrategyElement strategyElement : strategyList) {
            ret += getStrategyFitnessValue(strategyElement);
        }

        return ret / strategyList.size();
    }

    private double getStrategyFitnessValue(StrategyElement strategyElement) {
        double UtilityValue = strategyElement.getGoalValueList().get(0);
        double cost = strategyElement.getGoalValueList().get(1);
        double latency = strategyElement.getGoalValueList().get(2);

        return getUtilityValue(UtilityValue, cost, latency);
    }

    public static double getAvgFitness(ArrayList<StrategyElement> mostSimilarStrategies) {
        double ret = 0.0;

        for (StrategyElement strategyElement : mostSimilarStrategies) {
            double utilityValue = strategyElement.getGoalValueList().get(0);
            double cost = strategyElement.getGoalValueList().get(1);
            double latency = strategyElement.getGoalValueList().get(2);
            ret += getUtilityValue(utilityValue,cost, latency);
        }
        return ret / mostSimilarStrategies.size();
    }

    public static double getAvgFitness(NondominatedPopulation result) {
        double ret = 0.0;

        for (Solution solution : result) {
            double SoSGoalAchievement = -solution.getObjective(0);
            double cost = solution.getObjective(1);
            double latency = solution.getObjective(2);
            //System.out.println(SoSGoalAchievement);
            ret += getUtilityValue(SoSGoalAchievement,cost, latency);
        }

        return ret / result.size();
    }



    public static double getUtilityValue(double diffSavedPatient, double diffOperationCost, double tacticLatency) {
        // diffSavedPatient 의 최대화, diffOperationCost 의 최소화, tacticLatency 의 최소화
        //return 10 * savedPatient - operationCost;
        //return savedPatient;
        //System.out.println(operationCost);
        double tick = 200;
        double OneTickMaxCost = 0.1 * tick + 0.2 * tick + 0.1 * tick;
        double maxCost = tick * OneTickMaxCost;
        double w1 = 1.0 / 3, w2 = 1.0 / 3, w3 = 1.0 / 3; // weight 일단 다 같게.
        //double w1 = 0.5, w2 = 0.5, w3 = 0.0; // weight 일단 다 같게.

        double SoSGoalAchievement = 100 * diffSavedPatient / (maxSeaPatient + maxGroundPatient);
        double N_operationCost =  100 - 100 * (diffOperationCost / maxCost); //y축 값 cost를 적게쓸수록 높음
        double N_tacticLatency = 100 - 100 * (tacticLatency / tick); //y축 값

        //System.out.println(String.format("SoSGoalAchievement: %f, N_operationCost: %f, N_tacticLatency: %f", SoSGoalAchievement, N_operationCost, N_tacticLatency));
        //return SoSGoalAchievement / Math.sqrt(operationCost);
        //System.out.println(operationCost);
        //return SoSGoalAchievement / operationCost;
        //System.out.println(String.format("savedPatient: %f, SoSGoalAchievement: %f, Operationcost: %f TacticLatency: %f",savedPatient,SoSGoalAchievement,operationCost, tacticLatency));
        //return SoSGoalAchievement;
        //return SoSGoalAchievement/N_operationCost;
        //System.out.println(savedPatient);
        //System.out.println(diffOperationCost);
        //return diffSavedPatient;
        //return tacticLatency;
        //return w1 * SoSGoalAchievement + w2 * N_operationCost;
        //return w1 * SoSGoalAchievement + w2 * N_operationCost;
        return w1 * SoSGoalAchievement + w2 * N_operationCost + w3 * N_tacticLatency;
        //return Math.pow(SoSGoalAchievement,2) / Math.sqrt(operationCost);
        //return operationCost == 0 ? savedPatient / (maxSeaPatient + maxGroundPatient) : savedPatient / (maxSeaPatient + maxGroundPatient) * 10000 / operationCost;

        //return operationCost == 0 ? savedPatient * 100 : savedPatient * 100 / operationCost;

    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
