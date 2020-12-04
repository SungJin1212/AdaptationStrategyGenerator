package StrategyPlanner;

import SoS.MCIRSoSConfiguration;
import SoS.MCIRSoSEnvironmentCondition;
import StrategyPlanner.CasebasedReasoning.CaseBaseValue;
import StrategyPlanner.CasebasedReasoning.StrategyElement;
import org.moeaframework.Executor;
import org.moeaframework.Instrumenter;
import org.moeaframework.algorithm.NSGAII;
import org.moeaframework.analysis.collector.Accumulator;
import org.moeaframework.core.*;
import org.moeaframework.core.comparator.ChainedComparator;
import org.moeaframework.core.comparator.CrowdingComparator;
import org.moeaframework.core.comparator.ParetoDominanceComparator;
import org.moeaframework.core.indicator.Hypervolume;
import org.moeaframework.core.operator.*;
import org.moeaframework.core.operator.real.UM;
import org.moeaframework.core.variable.EncodingUtils;
import org.moeaframework.core.variable.RealVariable;
import org.moeaframework.util.ReferenceSetMerger;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static StrategyPlanner.CasebasedReasoning.CaseBaseValue.getAvgFitness;
import static StrategyPlanner.CasebasedReasoning.CaseBaseValue.getUtilityValue;

public class GenerationEngine {

    private int designTimePopulationSize;
    private int designTimeGeneration;
    private int runtimePopulationSize;
    private int runTimeGeneration;
    private int seeds;
    private String searchAlgorithm;

    public GenerationEngine(int designTimePopulationSize, int designTimeGeneration, int runtimePopulationSize, int runTimeGeneration, int seeds, String searchAlgorithm) {
        this.designTimePopulationSize = designTimePopulationSize;
        this.designTimeGeneration = designTimeGeneration;
        this.runtimePopulationSize = runtimePopulationSize;
        this.runTimeGeneration = runTimeGeneration;
        this.seeds = seeds;
        this.searchAlgorithm = searchAlgorithm;
    }

    public CaseBaseValue getCaseBaseValueAtDesignTime(int simulationTime, MCIRSoSEnvironmentCondition curmcirSoSEnvironmentCondition, MCIRSoSConfiguration curmcirSoSConfiguration) throws CloneNotSupportedException {
        CaseBaseValue caseBaseValue = new CaseBaseValue(curmcirSoSEnvironmentCondition);
        ArrayList<StrategyElement> strategyElementList = new ArrayList<>(0);

        ReferenceSetMerger referenceSet = new ReferenceSetMerger();

        Instrumenter instrumenter = new Instrumenter()
                .withProblemClass(MCIRSoSProblem.class, curmcirSoSEnvironmentCondition, curmcirSoSConfiguration.clone(), simulationTime)
                .withFrequency(this.designTimePopulationSize)
 //               .attachAll();
 //               .attachHypervolumeCollector()
 //               .withReferenceSet(new File("output.txt"))
//               .attachAll();
//                .attachGenerationalDistanceCollector()
                .attachApproximationSetCollector();

        NondominatedPopulation result = new Executor()
                .withProblemClass(MCIRSoSProblem.class, curmcirSoSEnvironmentCondition, curmcirSoSConfiguration.clone(), simulationTime)
                .withAlgorithm(this.searchAlgorithm)
                .withMaxEvaluations(this.designTimeGeneration * this.designTimePopulationSize) // The total number of function evaluations exhausted by the algorithm equals the product of the population size and the number of generations.
                .withProperty("populationSize", this.designTimePopulationSize)
                .withProperty("ux.rate", 0.9)
                //.withProperty("sbx.distributionIndex", 25.0)
                //.withProperty("Two-Point", 1.0)
                .withProperty("um.rate", 0.1)
//                .withProperty("pm.rate", 1 / curmcirSoSConfiguration.getNumOfVariables())
//                .withProperty("pm.distributionIndex", 20)
//                .distributeOnAllCores()
                .withInstrumenter(instrumenter)
                .run();

        Accumulator accumulator = instrumenter.getLastAccumulator();

        for (int i=0; i<accumulator.size("NFE"); i++) {
            int nfe = (int)accumulator.get("NFE", i);
            List<Solution> solutions = (List<Solution>)accumulator.get("Approximation Set", i);

            double AvgUtility = 0.0;
            double AvgSoSGoal = 0.0;
            double AvgCost = 0.0;
            double AvgLatency = 0.0;
            for (Solution solution : solutions) {
                //int temp = EncodingUtils.getInt(solution.getVariable(0));
                double SoSGoal = -solution.getObjective(0);
                double cost = solution.getObjective(1);
                double latency = solution.getObjective(2);

                //System.out.println(String.format("SosGoal: %f Cost: %f Latency %f", SoSGoal, cost, latency));
                AvgSoSGoal += SoSGoal;
                AvgCost += cost;
                AvgLatency += latency;
                AvgUtility += getUtilityValue(SoSGoal, cost, latency);


            }
            AvgSoSGoal = AvgSoSGoal / solutions.size();
            AvgCost = AvgCost / solutions.size();
            AvgLatency = AvgLatency / solutions.size();
            AvgUtility = AvgUtility / solutions.size();


            //System.out.println(String.format("Solution size: %d", solutions.size()));

            //System.out.println(String.format("Generation %d: Average Utility: %f, AvgSoSGoal: %f, AvgCost: %f, AvgLatency: %f", i+1, AvgUtility, AvgSoSGoal, AvgCost, AvgLatency));
        }


        for (Solution solution : result) { //all Pareto optimal solutions produced by the algorithm during the run.
            if (!solution.violatesConstraints()) {
                StrategyElement strategyElement = new StrategyElement();

                strategyElement.getGoalValueList().add(-solution.getObjective(0));
                strategyElement.getGoalValueList().add(solution.getObjective(1));
                strategyElement.getGoalValueList().add(solution.getObjective(2));

                //strategyElement.getGoalValueList().add(solution.getObjective(2));

                for(int i=0; i<solution.getNumberOfVariables(); i++) {
                    strategyElement.getStrategyValueList().add(EncodingUtils.getInt(solution.getVariable(i)));
                }
                strategyElementList.add(strategyElement);
            }
        }

        caseBaseValue.setStrategyList(strategyElementList);
        return caseBaseValue;
    }

    public CaseBaseValue getCaseBaseValueAtRunTime(int simulationTime, ArrayList<StrategyElement> mostSimilarStrategies, MCIRSoSEnvironmentCondition curmcirSoSEnvironmentCondition, MCIRSoSConfiguration curmcirSoSConfiguration) {
        Simulator simulator = new Simulator();

        ArrayList<StrategyElement> evolvedStrategyElementList = new ArrayList<>(0); //deep copy
        CaseBaseValue caseBaseValue = new CaseBaseValue(curmcirSoSEnvironmentCondition);
        MCIRSoSProblem mcirSoSProblem = new MCIRSoSProblem(curmcirSoSEnvironmentCondition, curmcirSoSConfiguration, simulationTime);

        RandomInitialization randomInitialization = new RandomInitialization(mcirSoSProblem, this.runtimePopulationSize);
        InjectedInitialization injectedInitialization = new InjectedInitialization(mcirSoSProblem, this.runtimePopulationSize, getInitialPopulation(mcirSoSProblem, mostSimilarStrategies, this.seeds)); // Any remaining slots in the population will be filled with randomly-generated solutions.
        injectedInitialization.initialize();
        TournamentSelection selection = new TournamentSelection(2, new ChainedComparator( new ParetoDominanceComparator(), new CrowdingComparator()));
       //Variation variation = new GAVariation(new UniformCrossover(1.0), new PM(1.0/ mcirSoSProblem.getNumberOfVariables(), 20)); //uniform crossover
        Variation variation = new GAVariation(new UniformCrossover(0.9), new UM(0.1)); //uniform crossover
        //Variation variation = new GAVariation(new SBX(1,100), new PM(1.0/ mcirSoSProblem.getNumberOfVariables(),20)); //uniform crossover

        //Variation variation = new GAVariation( new TwoPointCrossover(1.0), new PM(1.0/ mcirSoSProblem.getNumberOfVariables(), 20)); //uniform crossover

        Algorithm algorithm = new NSGAII(mcirSoSProblem, new NondominatedSortingPopulation(), null, selection, variation, injectedInitialization);


        while (algorithm.getNumberOfEvaluations() < this.runtimePopulationSize * this.runTimeGeneration) {
            algorithm.step();
        }

        NondominatedPopulation result = algorithm.getResult();

        for (Solution solution : result) { //all Pareto optimal solutions produced by the algorithm during the run.

            if (!solution.violatesConstraints()) {
                //System.out.println(String.format("Seeding SoS Goal: %f, Cost: %f, Latency: %f",-solution.getObjective(0),solution.getObjective(1), solution.getObjective(2)));
                StrategyElement strategyElement = new StrategyElement();
                strategyElement.getGoalValueList().add(-solution.getObjective(0));
                strategyElement.getGoalValueList().add(solution.getObjective(1)); //cost
                strategyElement.getGoalValueList().add(solution.getObjective(2)); //latency


                for(int i=0; i<solution.getNumberOfVariables(); i++) {
                    strategyElement.getStrategyValueList().add(EncodingUtils.getInt(solution.getVariable(i)));
                }
                evolvedStrategyElementList.add(strategyElement);
            }
        }
        double currentMaxFitness = getAvgFitness(mostSimilarStrategies);
        double evolvedFitness = getAvgFitness(result);
        //System.out.println(String.format("getCaseBaseValueAtRunTime evolvedStrategyElementList Size %d", evolvedStrategyElementList.size()));
        caseBaseValue.setStrategyList(evolvedStrategyElementList);

//        if(currentMaxFitness > evolvedFitness) { //기존이 더 좋으면 기존 strategy 유지
//            caseBaseValue.setStrategyList(mostSimilarStrategies);
//        }
//        else {
//            caseBaseValue.setStrategyList(evolvedStrategyElementList); //evolved 된게 더 좋으면
//        }
        return caseBaseValue;
    }


    public CaseBaseValue getCaseBaseValueAtRunTimeNoSeeding(int simulationTime, ArrayList<StrategyElement> mostSimilarStrategies, MCIRSoSEnvironmentCondition curmcirSoSEnvironmentCondition, MCIRSoSConfiguration curmcirSoSConfiguration) {
        Simulator simulator = new Simulator();

        ArrayList<StrategyElement> evolvedStrategyElementList = new ArrayList<>(0); //deep copy
        CaseBaseValue caseBaseValue = new CaseBaseValue(curmcirSoSEnvironmentCondition);
        MCIRSoSProblem mcirSoSProblem = new MCIRSoSProblem(curmcirSoSEnvironmentCondition, curmcirSoSConfiguration, simulationTime);

        RandomInitialization randomInitialization = new RandomInitialization(mcirSoSProblem, this.runtimePopulationSize);
        //InjectedInitialization injectedInitialization = new InjectedInitialization(mcirSoSProblem, this.runtimePopulationSize, getInitialPopulation(mostSimilarStrategies)); // Any remaining slots in the population will be filled with randomly-generated solutions.
        TournamentSelection selection = new TournamentSelection(2, new ChainedComparator( new ParetoDominanceComparator(), new CrowdingComparator()));

        //Variation variation = new GAVariation(new UniformCrossover(1.0), new PM(1.0/ mcirSoSProblem.getNumberOfVariables(), 20)); //uniform crossover
        //Variation variation = new GAVariation(new UNDX(2,2), new UM(1.0/ mcirSoSProblem.getNumberOfVariables())); //uniform crossover

        Variation variation = new GAVariation(new UniformCrossover(0.9), new UM(0.1)); //uniform crossover
        //Variation variation = new GAVariation(new SBX(1,100), new PM(1.0/ mcirSoSProblem.getNumberOfVariables(),20)); //uniform crossover

        Algorithm algorithm = new NSGAII(mcirSoSProblem, new NondominatedSortingPopulation(), null, selection, variation, randomInitialization);



        if (searchAlgorithm.equals("NSGAII")) {
            algorithm = new NSGAII(mcirSoSProblem, new NondominatedSortingPopulation(), null, selection, variation, randomInitialization);
        }

        while (algorithm.getNumberOfEvaluations() < this.runtimePopulationSize * this.runTimeGeneration) {
            algorithm.step();
        }
        NondominatedPopulation result = algorithm.getResult();
        //System.out.println(String.format("Most Similar Strategy Size: %d", mostSimilarStrategies.size()));
        for (Solution solution : result) { //all Pareto optimal solutions produced by the algorithm during the run.
            if (!solution.violatesConstraints()) {
                //System.out.println(String.format("NoSeeding SoS Goal: %f, Cost: %f, Latency: %f",-solution.getObjective(0),solution.getObjective(1), solution.getObjective(2)));
                StrategyElement strategyElement = new StrategyElement();
                strategyElement.getGoalValueList().add(-solution.getObjective(0));
                strategyElement.getGoalValueList().add(solution.getObjective(1));
                strategyElement.getGoalValueList().add(solution.getObjective(2));
                for(int i=0; i<solution.getNumberOfVariables(); i++) {
                    strategyElement.getStrategyValueList().add(EncodingUtils.getInt(solution.getVariable(i)));
                }
                //System.out.println(String.format("Result: %d %d %d",EncodingUtils.getInt(solution.getVariable(0)), EncodingUtils.getInt(solution.getVariable(1)), EncodingUtils.getInt(solution.getVariable(2))));
                //System.out.println(String.format("StrategyElement: %d %d %d",strategyElement.getStrategyValueList().get(0), strategyElement.getStrategyValueList().get(1), strategyElement.getStrategyValueList().get(2)));
                evolvedStrategyElementList.add(strategyElement);
            }
        }
        double currentMaxFitness = getAvgFitness(mostSimilarStrategies);
        double evolvedFitness = getAvgFitness(result);
        caseBaseValue.setStrategyList(evolvedStrategyElementList);

//        if(currentMaxFitness > evolvedFitness) { //기존이 더 좋으면 기존 strategy 유지
//            caseBaseValue.setStrategyList(mostSimilarStrategies);
//        }
//        else {
//            caseBaseValue.setStrategyList(evolvedStrategyElementList); //evolved 된게 더 좋으면
//        }
        return caseBaseValue;
    }

    private ArrayList<Solution> getInitialPopulation(MCIRSoSProblem mcirSoSProblem, ArrayList<StrategyElement> mostSimilarStrategies, int seeds) {

        /*
        for(StrategyElement strategyElement : mostSimilarStrategies) {
            System.out.println(String.format("MostSimilarStrategy: %d %d %d", strategyElement.getStrategyValueList().get(0), strategyElement.getStrategyValueList().get(1), strategyElement.getStrategyValueList().get(2)));
        }*/

        ArrayList <Solution> solutions = new ArrayList<>(0);

        //Solution[] solutions;

        /*
        if (mostSimilarStrategies.size() < seeds) {
            solutions = new Solution[mostSimilarStrategies.size()];

        }
        else {
            solutions = new Solution[seeds];
        }*/

        /*
        for(int i = 0; i < this.populationSize; ++i) {
            Solution solution = this.problem.newSolution();

            for(int j = 0; j < solution.getNumberOfVariables(); ++j) {
                solution.getVariable(j).randomize();
            }

            initialPopulation[i] = solution;
        }
         */

        int idx = 0;
        for (StrategyElement strategyElement : mostSimilarStrategies) {
            Solution solution = mcirSoSProblem.newSolution();

            for(int i=0; i <solution.getNumberOfVariables(); i++) {
                RealVariable realVariable = new RealVariable(strategyElement.getStrategyValueList().get(i), -50, 50);
                //realVariable.randomize();
                solution.setVariable(i, realVariable);
            }

            //System.out.println(String.format("Solution: %d %d %d",  EncodingUtils.getInt(solution.getVariable(0)), EncodingUtils.getInt(solution.getVariable(1)),EncodingUtils.getInt(solution.getVariable(2))));
            solutions.add(solution.deepCopy());
            //solutions[idx] = solution.deepCopy();
            idx++;
            if (idx >= seeds) {
                break;
            }
        }
        System.out.println(String.format("Most similar Strategy Size: %d, Seed size: %d", mostSimilarStrategies.size(), solutions.size()));
        /*
        int idx = 0;
        for (StrategyElement strategyElement : mostSimilarStrategies) {
            Solution solution = new Solution(strategyElement.getStrategyValueList().size(), strategyElement.getGoalValueList().size(), strategyElement.getStrategyValueList().size());
            for(int i=0; i <solution.getNumberOfVariables(); i++) {
                System.out.println(strategyElement.getStrategyValueList().get(i));
                //solution.setVariable(i, EncodingUtils.newInt(strategyElement.getStrategyValueList().get(i), strategyElement.getStrategyValueList().get(i)));
                System.out.println(solution.getVariable(0));

                RealVariable realVariable = new RealVariable(strategyElement.getStrategyValueList().get(i),strategyElement.getStrategyValueList().get(i));
                realVariable.randomize();
                solution.setVariable(i, realVariable);
            }

            //System.out.println(String.format("Solution: %d %d %d",  EncodingUtils.getInt(solution.getVariable(0)), EncodingUtils.getInt(solution.getVariable(1)),EncodingUtils.getInt(solution.getVariable(2))));
            solutions.add(solution.deepCopy());
            //solutions[idx] = solution.deepCopy();
            idx++;
            if (idx >= seeds) {
                break;
            }
        }
        */

        /*
        for (int i=0; i<mostSimilarStrategies.size(); i++) {
            System.out.println(String.format("Solutions: %d %d %d",  EncodingUtils.getInt(solutions[i].getVariable(0)), EncodingUtils.getInt(solutions[i].getVariable(1)),EncodingUtils.getInt(solutions[i].getVariable(2))));
        }
        */

        return solutions;
    }
}
