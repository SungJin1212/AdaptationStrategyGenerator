package CleaningSoSCaseStudy.StrategyGenerationEngine;

import Model.SoS.CleaningSoSConfiguration;
import Model.SoS.CleaningSoSEnvironmentCondition;
import StrategyGenerationEngine.CleaningSoSProblem;
import StrategyGenerationEngine.Element.CaseBaseValue;
import StrategyGenerationEngine.Element.StrategyElement;
import StrategyGenerationEngine.Simulator;
import org.moeaframework.Executor;
import org.moeaframework.algorithm.NSGAII;
import org.moeaframework.core.*;
import org.moeaframework.core.comparator.ChainedComparator;
import org.moeaframework.core.comparator.CrowdingComparator;
import org.moeaframework.core.comparator.ParetoDominanceComparator;
import org.moeaframework.core.operator.*;
import org.moeaframework.core.operator.real.PM;
import org.moeaframework.core.variable.EncodingUtils;

import java.util.ArrayList;

import static StrategyGenerationEngine.Simulator.evaluateStrategy;
import static StrategyGenerationEngine.Simulator.getInitialPopulation;


public class GenerationEngine {

    private int designTimePopulationSize;
    private int designTimeGeneration;
    private int runtimePopulationSize;
    private int runTimeGeneration;
    private String searchAlgorithm;

    public GenerationEngine(int designTimePopulationSize, int designTimeGeneration, int runtimePopulationSize, int runTimeGeneration, String searchAlgorithm) {
        this.designTimePopulationSize = designTimePopulationSize;
        this.designTimeGeneration = designTimeGeneration;
        this.runtimePopulationSize = runtimePopulationSize;
        this.runTimeGeneration = runTimeGeneration;
        this.searchAlgorithm = searchAlgorithm;
    }

    public CaseBaseValue getCaseBaseValueAtDesignTime(int simulationTime, CleaningSoSEnvironmentCondition curCleaningSoSEnvironmentCondition, CleaningSoSConfiguration curCleaningSoSConfiguration) {

        CaseBaseValue caseBaseValue = new CaseBaseValue(curCleaningSoSEnvironmentCondition);
        ArrayList<StrategyElement> strategyElementList = new ArrayList<>(0);

//        InjectedInitialization injectedInitialization = new InjectedInitialization(StrategyGenerationAtDesignTime.class, 50, )

        NondominatedPopulation result = new Executor()
                .withProblemClass(CleaningSoSProblem.class, curCleaningSoSEnvironmentCondition, curCleaningSoSConfiguration, simulationTime)
                .withAlgorithm(this.searchAlgorithm)
                .withMaxEvaluations(this.designTimeGeneration * this.designTimePopulationSize) // The total number of function evaluations exhausted by the algorithm equals the product of the population size and the number of generations.
                .withProperty("populationSize", this.designTimePopulationSize)
                //.withProperty("withReplacement", true) // use binary tournament selection (default value)
                .withProperty("Two-Point", 1.0)

//                .distributeOnAllCores() // parallelization execution.
                .run();



//        new Plot()
//                .add("NSGAII", result)
//                .show();
        //display the results
        //System.out.format("Objective1  Objective2   Objective3%n");



        for (Solution solution : result) { //all Pareto optimal solutions produced by the algorithm during the run.
            if (!solution.violatesConstraints()) {
                StrategyElement strategyElement = new StrategyElement();

                strategyElement.getGoalValueList().add(-solution.getObjective(0));
                strategyElement.getGoalValueList().add(solution.getObjective(1));
                strategyElement.getGoalValueList().add(solution.getObjective(2));

                for(int i=0; i<solution.getNumberOfVariables(); i++) {
                    strategyElement.getStrategyValueList().add(EncodingUtils.getInt(solution.getVariable(i)));
                }
                strategyElementList.add(strategyElement);
            }
        }
        caseBaseValue.setStrategyList(strategyElementList);
        return caseBaseValue;
    }

    public CaseBaseValue getCaseBaseValueAtRunTime(int simulationTime, ArrayList<StrategyElement> mostSimilarStrategies, CleaningSoSEnvironmentCondition curCleaningSoSEnvironmentCondition, CleaningSoSConfiguration curCleaningSoSConfiguration) {

        ArrayList<StrategyElement> evolvedStrategyElementList = new ArrayList<>(0); //deep copy

        CaseBaseValue caseBaseValue = new CaseBaseValue(curCleaningSoSEnvironmentCondition);
        CleaningSoSProblem cleaningSoSProblem = new CleaningSoSProblem(curCleaningSoSEnvironmentCondition, curCleaningSoSConfiguration, simulationTime);

        //RandomInitialization randomInitialization = new RandomInitialization(cleaningSoSProblem, 100);

        InjectedInitialization injectedInitialization = new InjectedInitialization(cleaningSoSProblem, this.runtimePopulationSize, getInitialPopulation(mostSimilarStrategies)); // Any remaining slots in the population will be filled with randomly-generated solutions.

        TournamentSelection selection = new TournamentSelection(2, new ChainedComparator( new ParetoDominanceComparator(), new CrowdingComparator()));

        //Variation variation = new GAVariation( new OnePointCrossover(1.0), new UM(1.0/ cleaningSoSProblem.getNumberOfVariables())); //uniform crossover
        Variation variation = new GAVariation( new TwoPointCrossover(1.0), new PM(1.0/ cleaningSoSProblem.getNumberOfVariables(), 20)); //uniform crossover

        //Variation variation = new GAVariation( new TwoPointCrossover(1.0), new UM(1.0/ cleaningSoSProblem.getNumberOfVariables())); //uniform crossover

        Algorithm algorithm = null;

        if (searchAlgorithm.equals("NSGAII")) {
            algorithm = new NSGAII(cleaningSoSProblem, new NondominatedSortingPopulation(), null, selection, variation, injectedInitialization);
        }

        //terminationCount 연속으로 안좋아지면 stop.
//        int terminationCount = 5;
//        int cnt = 0;
//        double currentMaxFitness = evaluateStrategy(mostSimilarStrategies);
//        double tempFitness;
//
//        while(cnt != terminationCount) {
//            algorithm.step();
//            NondominatedPopulation result = algorithm.getResult();
//            tempFitness = evaluateStrategy(result);
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

        while (algorithm.getNumberOfEvaluations() < this.runtimePopulationSize * this.runTimeGeneration) {
            algorithm.step();
        }

        NondominatedPopulation result = algorithm.getResult();

        for (Solution solution : result) { //all Pareto optimal solutions produced by the algorithm during the run.

            if (!solution.violatesConstraints()) {
                StrategyElement strategyElement = new StrategyElement();
                strategyElement.getGoalValueList().add(-solution.getObjective(0));
                strategyElement.getGoalValueList().add(solution.getObjective(1));
                strategyElement.getGoalValueList().add(solution.getObjective(2));

                for(int i=0; i<solution.getNumberOfVariables(); i++) {
                    strategyElement.getStrategyValueList().add(EncodingUtils.getInt(solution.getVariable(i)));
                }
                evolvedStrategyElementList.add(strategyElement);
            }
        }
//        caseBaseValue.setStrategyList(evolvedStrategyElementList);

        double currentMaxFitness = Simulator.evaluateStrategy(mostSimilarStrategies);
        double evolvedFitness = Simulator.evaluateStrategy(result);

        if(currentMaxFitness > evolvedFitness) { //기존이 더 좋으면 기존 strategy 유지
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


}
