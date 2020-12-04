package StrategyPlanner.CasebasedReasoning;

import SoS.Configuration;
import SoS.EnvironmentCondition;

import java.util.ArrayList;

import static StrategyPlanner.CasebasedReasoning.CaseBaseValue.getUtilityValue;

public class Casebase {

    private final double INF = 987654321.0;


    private ArrayList<CaseBaseElement> StrategyBase;

    public Casebase() {
        StrategyBase = new ArrayList<>(0);
    }
    public void printCaseBase() {

        double sumOfAvgFitness = 0.0;
        for (CaseBaseElement caseBaseElement : StrategyBase) {
//            sumOfAvgFitness += caseBaseElement.getAvgFitnessOfCaseBaseElement();
//            ArrayList<Integer> configurationRet = new ArrayList<>(caseBaseElement.getConfiguration().getConfigurations().values());
//            System.out.println(String.format("Configuration: %d %d %d", configurationRet.get(0), configurationRet.get(1), configurationRet.get(2)));
//            for(CaseBaseValue caseBaseValue : caseBaseElement.getCaseBaseValues()) {
//                ArrayList<Double> environmentConditionRet = new ArrayList<>(caseBaseValue.getEnvironmentCondition().getEnvironmentCondition().values());
//                System.out.print(String.format("EnvironmentCondition: %f %f, AverageFitness: %f", environmentConditionRet.get(0),environmentConditionRet.get(1), caseBaseValue.getAverageFitness()));//
//                sumOfAvgFitness += caseBaseValue.getAverageFitness();
//                System.out.print(" Tactic List: ");
//                for(StrategyElement s : caseBaseValue.getStrategyList()) {
//                    System.out.print(String.format("%d %d %d  ",s.getStrategyValueList().get(0), s.getStrategyValueList().get(1), s.getStrategyValueList().get(2)));
             }
//                System.out.println();

//        }

//       System.out.println(String.format("sumOfAverageFitness: %f", sumOfAvgFitness));
//
    }


    public void add(CaseBaseElement casebaseElement) {
        StrategyBase.add(casebaseElement);
    }

    public void Store(Configuration curConfiguration, ArrayList<StrategyElement> mostSimilarStrategies, CaseBaseValue StoredCaseBaseValue, EnvironmentCondition curEnvironmentCondition) {
        //System.out.println(String.format("Store mostSimilarStrategies :%d", mostSimilarStrategies.size()));

        double configurationMinDist = INF;
        double environmentConditionMinDist = INF;
        int configurationIndex = 0;
        int environmentConditionIndex = 0;
        boolean isSameEnvironment = false;

        ArrayList<CaseBaseValue> mostSimilarCaseBaseValues = null;
        ArrayList<Integer> curConfigurationValues = new ArrayList<>(0);

        curConfigurationValues.add(curConfiguration.getConfigurations().get("numOfFirefighter"));
        curConfigurationValues.add(curConfiguration.getConfigurations().get("numOfHelicopter"));
        curConfigurationValues.add(curConfiguration.getConfigurations().get("numOfAmbulance"));


        ArrayList<Double> curEnvironmentConditionValues = new ArrayList<>(0);

        curEnvironmentConditionValues.add(curEnvironmentCondition.getEnvironmentCondition().get("weatherCondition"));
        curEnvironmentConditionValues.add(curEnvironmentCondition.getEnvironmentCondition().get("roadCondition"));

        int index = 0;

        for(CaseBaseElement casebaseElement : StrategyBase) {
            ArrayList<Integer> caseBaseElementValues = new ArrayList<>(0);
            caseBaseElementValues.add(casebaseElement.getConfiguration().getConfigurations().get("numOfFirefighter"));
            caseBaseElementValues.add(casebaseElement.getConfiguration().getConfigurations().get("numOfHelicopter"));
            caseBaseElementValues.add(casebaseElement.getConfiguration().getConfigurations().get("numOfAmbulance"));
            double curDist = getEuclideanDistII(curConfigurationValues,caseBaseElementValues);
            if (configurationMinDist > curDist) {
                configurationMinDist = curDist;
                mostSimilarCaseBaseValues = casebaseElement.getCaseBaseValues();
                configurationIndex = index;
            }
            index++;
//            configurationMinDist = Math.min(configurationMinDist, getEuclideanDist(curConfigurationValues,caseBaseElementValues));
        }

        index = 0;
        for(CaseBaseValue caseBaseValue : mostSimilarCaseBaseValues) {
            ArrayList <Double> environmentValues = new ArrayList<>(0);
            environmentValues.add(caseBaseValue.getEnvironmentCondition().getEnvironmentCondition().get("weatherCondition"));
            environmentValues.add(caseBaseValue.getEnvironmentCondition().getEnvironmentCondition().get("roadCondition"));

            double curDist = getEuclideanDistDD(curEnvironmentConditionValues,environmentValues);

            if(curDist == 0) { // 같은게 있으면
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
        if (!isSameEnvironment) { //다른 환경 변수이면 만들어서 넣어줌
            CaseBaseValue caseBaseValue = new CaseBaseValue(curEnvironmentCondition);
            caseBaseValue.setStrategyList(StoredCaseBaseValue.getStrategyList());
            StrategyBase.get(configurationIndex).getCaseBaseValues().add(caseBaseValue);
            //System.out.println(String.format("1Store caseBase StrategySize :%d", StoredCaseBaseValue.getStrategyList().size()));
        }
        else { // 같은게 있으면
            if (StoredCaseBaseValue.getAverageFitness() > getAverageFitness(mostSimilarStrategies)) { //이미 겪은 경우, evolved 된게 더 좋으면 evolved 된걸로 체인지
                StrategyBase.get(configurationIndex).getCaseBaseValues().get(environmentConditionIndex).setStrategyList(StoredCaseBaseValue.getStrategyList());
                StrategyBase.get(configurationIndex).getCaseBaseValues().get(environmentConditionIndex).setAverageFitness(StoredCaseBaseValue.getAverageFitness());
                //System.out.println(String.format("2Store caseBase StrategySize :%d", StoredCaseBaseValue.getStrategyList().size()));

            }
        }
    }

    public double getAverageFitness(ArrayList<StrategyElement> strategyElements) {
        double ret = 0.0;

        for (StrategyElement strategyElement : strategyElements) {
            ret += getStrategyFitnessValue(strategyElement);
        }

        return ret / strategyElements.size();
    }

    public double getStrategyFitnessValue(StrategyElement strategyElement) {
        double UtilityValue = strategyElement.getGoalValueList().get(0);
        double cost = strategyElement.getGoalValueList().get(1);
        double latency = strategyElement.getGoalValueList().get(2);

        return getUtilityValue(UtilityValue,cost, latency);
    }

    public ArrayList<StrategyElement> Retrieve(Configuration curConfiguration, EnvironmentCondition curEnvironmentCondition) { //get StrategyElement List
        double configurationMinDist = INF;
        double environmentConditionMinDist = INF;
        ArrayList<CaseBaseValue> mostSimilarCaseBaseValues = null;
        ArrayList<StrategyElement> mostSimilarStrategyElements = null;

        ArrayList<Integer> curConfigurationValues = new ArrayList<>(0);

        curConfigurationValues.add(curConfiguration.getConfigurations().get("numOfFirefighter"));
        curConfigurationValues.add(curConfiguration.getConfigurations().get("numOfHelicopter"));
        curConfigurationValues.add(curConfiguration.getConfigurations().get("numOfAmbulance"));


        ArrayList<Double> curEnvironmentConditionValues = new ArrayList<>(0);

        curEnvironmentConditionValues.add(curEnvironmentCondition.getEnvironmentCondition().get("weatherCondition"));
        curEnvironmentConditionValues.add(curEnvironmentCondition.getEnvironmentCondition().get("roadCondition"));

        for(CaseBaseElement casebaseElement : StrategyBase) {
            ArrayList<Integer> caseBaseElementValues = new ArrayList<>(0);
            caseBaseElementValues.add(casebaseElement.getConfiguration().getConfigurations().get("numOfFirefighter"));
            caseBaseElementValues.add(casebaseElement.getConfiguration().getConfigurations().get("numOfHelicopter"));
            caseBaseElementValues.add(casebaseElement.getConfiguration().getConfigurations().get("numOfAmbulance"));

            double configurationDist = getEuclideanDistII(curConfigurationValues,caseBaseElementValues);
            if (configurationMinDist > configurationDist) {
                configurationMinDist = configurationDist;
                mostSimilarCaseBaseValues = casebaseElement.getCaseBaseValues();
            }
        }

        for (CaseBaseValue caseBaseValue : mostSimilarCaseBaseValues) {
            ArrayList <Double> environmentValues = new ArrayList<>(0);
            environmentValues.add(caseBaseValue.getEnvironmentCondition().getEnvironmentCondition().get("weatherCondition"));
            environmentValues.add(caseBaseValue.getEnvironmentCondition().getEnvironmentCondition().get("roadCondition"));

            double environmentConditionDist = getEuclideanDistDD(curEnvironmentConditionValues, environmentValues);
            if (environmentConditionMinDist > environmentConditionDist) {
                environmentConditionMinDist = environmentConditionDist;
                mostSimilarStrategyElements = caseBaseValue.getStrategyList();

                //System.out.println(String.format("caseBase StrategySize :%d", caseBaseValue.getStrategyList().size()));
            }
        }
        //System.out.println(String.format("mostSimilarStrategyElements: %d",mostSimilarStrategyElements.size()));
        return mostSimilarStrategyElements;
    }


    private double getEuclideanDistII(ArrayList<Integer> arrayList1, ArrayList<Integer> arrayList2) {
        double ret = 0.0;

        for(int i=0; i<arrayList1.size(); i++){
            ret += Math.pow(arrayList1.get(i) - arrayList2.get(i),2);
        }
        return ret;
    }

    private double getEuclideanDistID(ArrayList<Integer> arrayList1, ArrayList<Double> arrayList2) {
        double ret = 0.0;

        for(int i=0; i<arrayList1.size(); i++){
            ret += (Math.pow(arrayList1.get(i) - arrayList2.get(i),2));
        }
        return ret;
    }

    private double getEuclideanDistDD(ArrayList<Double> arrayList1, ArrayList<Double> arrayList2) {
        double ret = 0.0;

        ret += Math.pow(10 * (arrayList1.get(0) - arrayList2.get(0)),2); // weatherCondition
        ret += Math.pow(arrayList1.get(1) - arrayList2.get(1),2); // roadCondition
        /*
        for(int i=0; i<arrayList1.size(); i++){
            ret += (Math.pow(arrayList1.get(i) - arrayList2.get(i),2));
        }
        */
        return ret;
    }
}
