package StrategyGenerationEngine.Element;

import Model.AbstactClass.Rule.EnvironmentCondition;

import java.util.ArrayList;

public class CaseBaseValue {

    private EnvironmentCondition environmentCondition;
    private ArrayList<StrategyElement> strategyList;
    private double averageFitness;

    public CaseBaseValue(EnvironmentCondition environmentCondition) {
        this.environmentCondition = environmentCondition;
        strategyList = new ArrayList<>(0);
    }

    public EnvironmentCondition getEnvironmentCondition() {
        return environmentCondition;
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

    public void calAverageFitness() {
        double ret = 0.0;

        for(StrategyElement strategyElement : strategyList) {
                ret += getFitness(strategyElement);
            }
        averageFitness = ret / strategyList.size();

    }
    private double getFitness(StrategyElement strategyElement) {
        double SoSGoal = strategyElement.getGoalValueList().get(0);
        double Cost = strategyElement.getGoalValueList().get(1);
        double Latency = strategyElement.getGoalValueList().get(2);
        return Cost + Latency == 0 ? SoSGoal : (SoSGoal / (Cost + Latency));
    }

    public double getAverageFitness() {
        return averageFitness;
    }
}
