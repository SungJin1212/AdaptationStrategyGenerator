package StrategyGenerationEngine.Casebase;

import Model.AbstactClass.Rule.Configuration;
import Model.AbstactClass.Rule.EnvironmentCondition;
import StrategyGenerationEngine.Element.CaseBaseValue;
import StrategyGenerationEngine.Element.StrategyElement;

import java.util.ArrayList;

public class CaseBase {
    private final double INF = 987654321.0;

    private ArrayList<CaseBaseElement> caseBaseElements;


    public CaseBase() {
        caseBaseElements = new ArrayList<>(0);
    }

    public void printCaseBase() {
        for(CaseBaseElement caseBaseElement : caseBaseElements) {
            ArrayList<Integer> configurationRet = new ArrayList<>(caseBaseElement.getConfiguration().getConfiguration().values());
            System.out.println(String.format("Configuration: %d %d %d %d", configurationRet.get(0), configurationRet.get(1), configurationRet.get(2), configurationRet.get(3)));
            for(CaseBaseValue caseBaseValue : caseBaseElement.getCaseBaseValues()) {
                ArrayList<Integer> environmentConditionRet = new ArrayList<>(caseBaseValue.getEnvironmentCondition().getEnvironmentCondition().values());
                System.out.print(String.format("EnvironmentCondition: %d, AverageFitness: %f", environmentConditionRet.get(0), caseBaseValue.getAverageFitness()));
                System.out.print(" Strategy List: ");
                for(StrategyElement s : caseBaseValue.getStrategyList()) {
                    System.out.print(String.format("%d %d %d %d  ",s.getStrategyValueList().get(0), s.getStrategyValueList().get(1), s.getStrategyValueList().get(2), s.getStrategyValueList().get(3)));
                }
                System.out.println();
            }
        }
    }

    public void add(CaseBaseElement caseBaseElement) {
        caseBaseElements.add(caseBaseElement);
    }

    public void Store(Configuration curConfiguration, CaseBaseValue StoredCaseBaseValue, EnvironmentCondition curEnvironmentCondition) { //현재 configuration, environment condition
        double configurationMinDist = INF;
        double environmentConditionMinDist = INF;
        int configurationIndex = 0;
        int environmentConditionIndex = 0;
        boolean isSameEnvironment = false;


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

            if(curDist == 0) {
                isSameEnvironment = true;
                environmentConditionIndex = index;
                break;
            }

            if (environmentConditionMinDist > curDist) {
                environmentConditionMinDist = curDist;
                environmentConditionIndex = index;
            }
            index++;
        }

        if (!isSameEnvironment) {
            CaseBaseValue caseBaseValue = new CaseBaseValue(curEnvironmentCondition);
            caseBaseValue.setStrategyList(StoredCaseBaseValue.getStrategyList());
            caseBaseElements.get(configurationIndex).getCaseBaseValues().add(caseBaseValue);

        }
        else {
            caseBaseElements.get(configurationIndex).getCaseBaseValues().get(environmentConditionIndex).setStrategyList(StoredCaseBaseValue.getStrategyList()); //같은게 있으면 update
            caseBaseElements.get(configurationIndex).getCaseBaseValues().get(environmentConditionIndex).setAverageFitness(StoredCaseBaseValue.getAverageFitness());

        }
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
            ret += Math.sqrt(Math.pow(arrayList1.get(i) - arrayList2.get(i),2));
        }
        return ret;
    }

}
