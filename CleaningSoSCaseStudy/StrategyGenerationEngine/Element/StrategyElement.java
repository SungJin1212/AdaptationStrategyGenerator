package CleaningSoSCaseStudy.StrategyGenerationEngine.Element;

import java.util.ArrayList;

public class StrategyElement {

    private ArrayList<Integer> StrategyValueList;
    private ArrayList<Double> GoalValueList;

    public StrategyElement(ArrayList<Integer> strategyValueList, ArrayList<Double> goalValueList) {
        StrategyValueList = strategyValueList;
        GoalValueList = goalValueList;
    }

    public ArrayList<Integer> getStrategyValueList() {
        return StrategyValueList;
    }

    public ArrayList<Double> getGoalValueList() {
        return GoalValueList;
    }

    public StrategyElement() {
        StrategyValueList = new ArrayList<>(0);
        GoalValueList = new ArrayList<>(0);
    }

    public void setStrategyValueList(ArrayList<Integer> strategyValueList) {
        StrategyValueList = strategyValueList;
    }

    public void setGoalValueList(ArrayList<Double> goalValueList) {
        GoalValueList = goalValueList;
    }
}
