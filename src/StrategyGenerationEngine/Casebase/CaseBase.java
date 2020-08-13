package StrategyGenerationEngine.Casebase;

import Model.AbstactClass.Rule.Configuration;
import Model.AbstactClass.Rule.EnvironmentCondition;
import Model.GeneratedCode.Rule.CleaningSoSEnvironmentCondition;
import StrategyGenerationEngine.Element.CaseBaseValue;
import StrategyGenerationEngine.Element.StrategyElement;

import java.util.ArrayList;

public class CaseBase {
    private final double INF = 987654321.0;

    private ArrayList<CaseBaseElement> caseBaseElements;

    public CaseBase() {
        caseBaseElements = new ArrayList<>(0);
    }

    public void add(CaseBaseElement caseBaseElement) {
        caseBaseElements.add(caseBaseElement);
    }


    public void Store(Configuration curConfiguration, CaseBaseValue beChangedCaseBaseValue, EnvironmentCondition curEnvironmentCondition) { //현재 configuration, environment condition
        double configurationMinDist = INF;
        double environmentConditionMinDist = INF;
        int configurationIndex = 0;
        int environmentConditionIndex = 0;


        ArrayList<CaseBaseValue> mostSimilarCaseBaseValues = null;

        ArrayList<Integer> curConfigurationValues = new ArrayList<>(curConfiguration.getConfiguration().values());
        ArrayList<Integer> curEnvironmentConditionValues = new ArrayList<>(curEnvironmentCondition.getEnvironmentCondition().values());

        int index = 0;
        for(CaseBaseElement caseBaseElement : caseBaseElements) {
            ArrayList<Integer> caseBaseElementValues = new ArrayList<>(caseBaseElement.getConfiguration().getConfiguration().values());
            double curDist = getEuclideanDist(curConfigurationValues,caseBaseElementValues);
            if (configurationMinDist > curDist) {
                configurationMinDist = curDist;
                mostSimilarCaseBaseValues = caseBaseElement.getCaseBaseValues();
                configurationIndex = index;
            }
            index++;
//            configurationMinDist = Math.min(configurationMinDist, getEuclideanDist(curConfigurationValues,caseBaseElementValues));
        }

        index = 0;
        for(CaseBaseValue caseBaseValue : mostSimilarCaseBaseValues) {
            ArrayList <Integer> environmentValues = new ArrayList<>(caseBaseValue.getEnvironmentCondition().getEnvironmentCondition().values());
            double curDist = getEuclideanDist(curEnvironmentConditionValues,environmentValues);
            if (environmentConditionMinDist > curDist) {
                environmentConditionMinDist = curDist;
                environmentConditionIndex = index;
            }
            index++;
        }
        caseBaseElements.get(configurationIndex).getCaseBaseValues().get(environmentConditionIndex).setStrategyList(beChangedCaseBaseValue.getStrategyList());
        caseBaseElements.get(configurationIndex).getCaseBaseValues().get(environmentConditionIndex).setAverageFitness(beChangedCaseBaseValue.getAverageFitness());
    }

    public ArrayList<StrategyElement> Retrieve(Configuration curConfiguration, EnvironmentCondition curEnvironmentCondition) { // retrieve most similar configuration's CaseBaseValues
        double configurationMinDist = INF;
        double environmentConditionMinDist = INF;
        ArrayList<CaseBaseValue> mostSimilarCaseBaseValues = null;
        ArrayList<StrategyElement> mostSimilarStrategyElements = null;

        ArrayList<Integer> curConfigurationValues = new ArrayList<>(curConfiguration.getConfiguration().values());
        ArrayList<Integer> curEnvironmentConditionValues = new ArrayList<>(curEnvironmentCondition.getEnvironmentCondition().values());

        for(CaseBaseElement caseBaseElement : caseBaseElements) {
            ArrayList<Integer> caseBaseElementValues = new ArrayList<>(caseBaseElement.getConfiguration().getConfiguration().values());
            double curDist = getEuclideanDist(curConfigurationValues,caseBaseElementValues);
            if (configurationMinDist > curDist) {
                configurationMinDist = curDist;
                mostSimilarCaseBaseValues = caseBaseElement.getCaseBaseValues();
            }
//            configurationMinDist = Math.min(configurationMinDist, getEuclideanDist(curConfigurationValues,caseBaseElementValues));
        }

        for(CaseBaseValue caseBaseValue : mostSimilarCaseBaseValues) {
            ArrayList <Integer> environmentValues = new ArrayList<>(caseBaseValue.getEnvironmentCondition().getEnvironmentCondition().values());
            double curDist = getEuclideanDist(curEnvironmentConditionValues,environmentValues);
            if (environmentConditionMinDist > curDist) {
                environmentConditionMinDist = curDist;
                mostSimilarStrategyElements = caseBaseValue.getStrategyList();
            }
        }
        return mostSimilarStrategyElements;
    }

    private double getEuclideanDist(ArrayList<Integer> arrayList1, ArrayList<Integer> arrayList2) {
        double ret = 0.0;

        for(int i=0; i<arrayList1.size(); i++){
            ret += Math.sqrt(Math.pow(arrayList1.indexOf(i) - arrayList2.indexOf(i),2));
        }
        return ret;
    }

}
