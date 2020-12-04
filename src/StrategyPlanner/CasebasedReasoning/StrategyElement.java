package StrategyPlanner.CasebasedReasoning;

import java.util.ArrayList;

public class StrategyElement implements Cloneable {

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

    public Object clone() throws CloneNotSupportedException {
        StrategyElement s = (StrategyElement) super.clone();

        ArrayList <Integer> arrayList1 = new ArrayList<>(0);
        ArrayList <Double> arrayList2 = new ArrayList<>(0);

        arrayList1.addAll(StrategyValueList);
        arrayList2.addAll(GoalValueList);
        s.StrategyValueList = arrayList1;
        s.GoalValueList = arrayList2;
        return s;
    }
}
