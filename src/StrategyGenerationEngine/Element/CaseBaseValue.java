package StrategyGenerationEngine.Element;

import Model.AbstactClass.Rule.EnvironmentCondition;

import java.util.ArrayList;

public class CaseBaseValue {

    private EnvironmentCondition environmentCondition;
    private ArrayList<StrategyElement> strategyList;

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
}
