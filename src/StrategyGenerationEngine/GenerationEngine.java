package StrategyGenerationEngine;

import Model.GeneratedCode.Rule.CleaningSoSConfiguration;
import Model.GeneratedCode.Rule.CleaningSoSEnvironmentCondition;
import StrategyGenerationEngine.Element.CaseBaseValue;
import StrategyGenerationEngine.Element.StrategyElement;
import org.moeaframework.Executor;
import org.moeaframework.algorithm.NSGAII;
import org.moeaframework.core.*;
import org.moeaframework.core.comparator.ChainedComparator;
import org.moeaframework.core.comparator.CrowdingComparator;
import org.moeaframework.core.comparator.ParetoDominanceComparator;
import org.moeaframework.core.operator.GAVariation;
import org.moeaframework.core.operator.InjectedInitialization;
import org.moeaframework.core.operator.TournamentSelection;
import org.moeaframework.core.operator.real.PM;
import org.moeaframework.core.operator.real.SBX;
import org.moeaframework.core.variable.EncodingUtils;

import java.util.ArrayList;

public class GenerationEngine {

    public static CaseBaseValue getCaseBaseValueAtDesignTime(CleaningSoSEnvironmentCondition curCleaningSoSEnvironmentCondition, CleaningSoSConfiguration curCleaningSoSConfiguration) {

        CaseBaseValue caseBaseValue = new CaseBaseValue(curCleaningSoSEnvironmentCondition);
        ArrayList<StrategyElement> strategyElementList = new ArrayList<>(0);

//        InjectedInitialization injectedInitialization = new InjectedInitialization(StrategyGenerationAtDesignTime.class, 50, )

        NondominatedPopulation result = new Executor()
                .withProblemClass(CleaningSoSProblem.class, curCleaningSoSEnvironmentCondition, curCleaningSoSConfiguration)
                .withProperty("populationSize", 200)
                .withProperty("withReplacement", true) // use binary tournament selection
                .withAlgorithm("NSGA2")

                .withMaxEvaluations(2000)
//                .distributeOnAllCores() // parallelization execution.
                .run();


//        new Plot()
//                .add("NSGAII", result)
//                .show();
        //display the results
        //System.out.format("Objective1  Objective2   Objective3%n");

        for (Solution solution : result) { //all Pareto optimal solutions produced by the algorithm during the run.
            StrategyElement strategyElement = new StrategyElement();

            strategyElement.getGoalValueList().add(-solution.getObjective(0));
            strategyElement.getGoalValueList().add(solution.getObjective(1));
            strategyElement.getGoalValueList().add(solution.getObjective(2));

            strategyElement.getStrategyValueList().add(EncodingUtils.getInt(solution.getVariable(0)));
            strategyElement.getStrategyValueList().add(EncodingUtils.getInt(solution.getVariable(1)));
            strategyElement.getStrategyValueList().add(EncodingUtils.getInt(solution.getVariable(2)));
            strategyElement.getStrategyValueList().add(EncodingUtils.getInt(solution.getVariable(3)));

            strategyElementList.add(strategyElement);
        }
        caseBaseValue.setStrategyList(strategyElementList);
        return caseBaseValue;
    }

    public static CaseBaseValue getCaseBaseValueAtRunTime(ArrayList<StrategyElement> mostSimilarStrategies, CleaningSoSEnvironmentCondition curCleaningSoSEnvironmentCondition, CleaningSoSConfiguration curCleaningSoSConfiguration) {

        ArrayList<StrategyElement> evolvedStrategyElementList = new ArrayList<>(0); //deep copy

        CaseBaseValue caseBaseValue = new CaseBaseValue(curCleaningSoSEnvironmentCondition);
        CleaningSoSProblem cleaningSoSProblem = new CleaningSoSProblem(curCleaningSoSEnvironmentCondition, curCleaningSoSConfiguration);

        InjectedInitialization injectedInitialization = new InjectedInitialization(cleaningSoSProblem, 100, getInitialPopulation(mostSimilarStrategies));

        TournamentSelection selection = new TournamentSelection(2, new ChainedComparator( new ParetoDominanceComparator(), new CrowdingComparator()));

        Variation variation = new GAVariation( new SBX(1.0, 25.0), new PM(1.0/ cleaningSoSProblem.getNumberOfVariables(), 30.0));

        Algorithm algorithm = new NSGAII( cleaningSoSProblem, new NondominatedSortingPopulation(), null, selection, variation, injectedInitialization);

        //terminationCount 연속으로 안좋아지면 stop.
//        int terminationCount = 5;
//        int cnt = 0;
//        double currentMaxFitness = getFitness(mostSimilarStrategies);
//        double tempFitness;
//
//        while(cnt != terminationCount) {
//            algorithm.step();
//            NondominatedPopulation result = algorithm.getResult();
//            tempFitness = getFitness(result);
//
//            if (tempFitness > currentMaxFitness) {
//                evolvedStrategyElementList.clear();
//                currentMaxFitness = tempFitness;
//                setStrategyElementList(evolvedStrategyElementList, result);
//                cnt = 0;
//            }
//            else {
//                cnt++;
//            }
//
//        }
        //System.out.println(algorithm.getNumberOfEvaluations());

        while (algorithm.getNumberOfEvaluations() < 1000) {
            algorithm.step();
        }

        NondominatedPopulation result = algorithm.getResult();

        for (Solution solution : result) { //all Pareto optimal solutions produced by the algorithm during the run.
            StrategyElement strategyElement = new StrategyElement();

            strategyElement.getGoalValueList().add(-solution.getObjective(0));
            strategyElement.getGoalValueList().add(solution.getObjective(1));
            strategyElement.getGoalValueList().add(solution.getObjective(2));

            strategyElement.getStrategyValueList().add(EncodingUtils.getInt(solution.getVariable(0)));
            strategyElement.getStrategyValueList().add(EncodingUtils.getInt(solution.getVariable(1)));
            strategyElement.getStrategyValueList().add(EncodingUtils.getInt(solution.getVariable(2)));
            strategyElement.getStrategyValueList().add(EncodingUtils.getInt(solution.getVariable(3)));

            evolvedStrategyElementList.add(strategyElement);
        }
//        caseBaseValue.setStrategyList(evolvedStrategyElementList);

        double currentMaxFitness = getFitness(mostSimilarStrategies);
        double evolvedFitness = getFitness(result);

        if(currentMaxFitness > evolvedFitness) { //기존이 더 좋으면...
            caseBaseValue.setStrategyList(mostSimilarStrategies);
        }
        else {
            caseBaseValue.setStrategyList(evolvedStrategyElementList); //evolved 된게 더 좋으면
        }
        return caseBaseValue;
    }

    private static void setStrategyElementList(ArrayList<StrategyElement> strategyElementList, NondominatedPopulation result) {
        for (Solution solution : result) {
            StrategyElement strategyElement = new StrategyElement();
            strategyElement.getGoalValueList().add(-solution.getObjective(0));
            strategyElement.getGoalValueList().add(solution.getObjective(1));
            strategyElement.getGoalValueList().add(solution.getObjective(2));

            strategyElement.getStrategyValueList().add(EncodingUtils.getInt(solution.getVariable(0)));
            strategyElement.getStrategyValueList().add(EncodingUtils.getInt(solution.getVariable(1)));
            strategyElement.getStrategyValueList().add(EncodingUtils.getInt(solution.getVariable(2)));
            strategyElement.getStrategyValueList().add(EncodingUtils.getInt(solution.getVariable(3)));
            strategyElementList.add(strategyElement);
        }
    }

    private static double getFitness(NondominatedPopulation result) {
        double ret = 0.0;
        for(Solution solution : result) {
            double SoSGoal = -solution.getObjective(0);
            double Cost = solution.getObjective(1);
            double Latency = solution.getObjective(2);
            ret += Cost + Latency == 0 ? SoSGoal : (SoSGoal / (Cost + Latency));
        }
        return ret;
    }

    private static double getFitness(ArrayList<StrategyElement> mostSimilarStrategies) {
        double ret =0.0;
        for(StrategyElement strategyElement : mostSimilarStrategies) {
            double SoSGoal = strategyElement.getGoalValueList().get(0);
            double Cost = strategyElement.getGoalValueList().get(1);
            double Latency = strategyElement.getGoalValueList().get(2);
            ret += Cost + Latency == 0 ? SoSGoal : (SoSGoal / (Cost + Latency));
        }
        return ret;
    }


    private static Solution[] getInitialPopulation(ArrayList<StrategyElement> mostSimilarStrategies) {

        Solution[] solutions = new Solution[mostSimilarStrategies.size()];

        int idx = 0;

        for (StrategyElement strategyElement : mostSimilarStrategies) {
            Solution solution = new Solution(4, 3);
            solution.setVariable(0, EncodingUtils.newInt(strategyElement.getStrategyValueList().get(0), strategyElement.getStrategyValueList().get(0)));
            solution.setVariable(1, EncodingUtils.newInt(strategyElement.getStrategyValueList().get(1), strategyElement.getStrategyValueList().get(1)));
            solution.setVariable(2, EncodingUtils.newInt(strategyElement.getStrategyValueList().get(2), strategyElement.getStrategyValueList().get(2)));
            solution.setVariable(3, EncodingUtils.newInt(strategyElement.getStrategyValueList().get(3), strategyElement.getStrategyValueList().get(3)));
//            System.out.println(String.format("%d %d %d %d", strategyElement.getStrategyValueList().get(0), strategyElement.getStrategyValueList().get(1), strategyElement.getStrategyValueList().get(2), strategyElement.getStrategyValueList().get(3)));
            solutions[idx] = solution;
            idx++;
        }

        return solutions;


    }
}
