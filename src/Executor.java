import Model.SoS.Strategy;
import Model.SoS.CleaningSoS;
import Model.SoS.CleaningSoSConfiguration;
import Model.SoS.CleaningSoSEnvironmentCondition;
import StrategyGenerationEngine.Casebase.CaseBase;
import StrategyGenerationEngine.Casebase.CaseBaseElement;
import StrategyGenerationEngine.Element.CaseBaseValue;
import StrategyGenerationEngine.Element.StrategyElement;
import StrategyGenerationEngine.GenerationEngine;

import java.util.ArrayList;
import java.util.Random;

import static StrategyGenerationEngine.Simulator.ExecuteStrategyAtRunTime;

public class Executor {

    private static CaseBase caseBase = new CaseBase();
    private ArrayList <CleaningSoSEnvironmentCondition> expectedCleaningSoSEnvironmentConditions;
    private ArrayList<CleaningSoSConfiguration> cleaningSoSConfigurations;
    private CleaningSoSConfiguration initialCleaningSoSConfiguration;
    private CleaningSoSEnvironmentCondition initialCleaningSoSEnvironmentCondition;
    private ArrayList<CleaningSoSEnvironmentCondition> runtimeEnvironmentalConditions;
    private GenerationEngine generationEngine;

    public Executor(GenerationEngine generationEngine, ArrayList<CleaningSoSEnvironmentCondition> expectedCleaningSoSEnvironmentConditions, ArrayList<CleaningSoSConfiguration> cleaningSoSConfigurations, CleaningSoSConfiguration initialCleaningSoSConfiguration, CleaningSoSEnvironmentCondition initialCleaningSoSEnvironmentCondition, ArrayList<CleaningSoSEnvironmentCondition> runtimeEnvironmentalConditions) {
        this.generationEngine = generationEngine;
        this.expectedCleaningSoSEnvironmentConditions = expectedCleaningSoSEnvironmentConditions;
        this.cleaningSoSConfigurations = cleaningSoSConfigurations;
        this.initialCleaningSoSConfiguration = initialCleaningSoSConfiguration;
        this.initialCleaningSoSEnvironmentCondition = initialCleaningSoSEnvironmentCondition;
        this.runtimeEnvironmentalConditions = runtimeEnvironmentalConditions;

    }

    public void run(int timeFrame, int runtimeSimulationTime) throws CloneNotSupportedException {
        ConstructCaseBaseAtDesignTime(timeFrame);
        caseBase.printCaseBase();
        RunTimeSimulation(timeFrame, runtimeSimulationTime);
    }

    private void ConstructCaseBaseAtDesignTime(int timeFrame) throws CloneNotSupportedException { //set CaseBase at system design time

        for(CleaningSoSConfiguration cleaningSoSConfiguration : cleaningSoSConfigurations) {
            CaseBaseElement caseBaseElement = new CaseBaseElement(cleaningSoSConfiguration);
            for(CleaningSoSEnvironmentCondition cleaningSoSEnvironmentCondition : expectedCleaningSoSEnvironmentConditions) {
                CaseBaseValue caseBaseValue;
                caseBaseValue = generationEngine.getCaseBaseValueAtDesignTime(timeFrame, cleaningSoSEnvironmentCondition, (CleaningSoSConfiguration) cleaningSoSConfiguration.clone());
                caseBaseValue.calAverageFitness();
                caseBaseElement.addCaseBaseValue(caseBaseValue);
            }
            caseBase.add(caseBaseElement);
        }
    }

    private void RunTimeSimulation(int timeFrame, int runtimeSimulationTime) throws CloneNotSupportedException {


        CleaningSoS cleaningSoS = new CleaningSoS(initialCleaningSoSEnvironmentCondition, initialCleaningSoSConfiguration);

        for (int runTime = timeFrame; runTime <= runtimeSimulationTime; runTime += timeFrame) {

            cleaningSoS.setRuntimeEnvironment(runtimeEnvironmentalConditions.get(new Random().nextInt(runtimeEnvironmentalConditions.size())));
            // Get Current configuration, environmentCondition
            CleaningSoSConfiguration curConfiguration = (CleaningSoSConfiguration) cleaningSoS.getSoSConfiguration(); //get Current Configuration
            CleaningSoSEnvironmentCondition curEnvironmentCondition = (CleaningSoSEnvironmentCondition) cleaningSoS.getSoSEnvironmentCondition(); //get Current EnvironmentCondition

            ArrayList<StrategyElement> mostSimilarStrategies = caseBase.Retrieve(curConfiguration, curEnvironmentCondition);//get most similar configuration of the current configuration

            // Retrieve strategyElements retrieve, evolve and set fitness value
            CaseBaseValue evolvedStrategies = generationEngine.getCaseBaseValueAtRunTime(timeFrame, mostSimilarStrategies, curEnvironmentCondition, curConfiguration);
            evolvedStrategies.calAverageFitness();

            caseBase.Store(curConfiguration, evolvedStrategies, curEnvironmentCondition);
            // strategy select & execute strategy
            //System.out.println(evolvedStrategies.getStrategyList().size());
            StrategyElement selectedStrategyElement = evolvedStrategies.getBestStrategy(); //select StrategyElement

            System.out.println(String.format("%d %d %d %d", selectedStrategyElement.getStrategyValueList().get(0), selectedStrategyElement.getStrategyValueList().get(1),
                    selectedStrategyElement.getStrategyValueList().get(2), selectedStrategyElement.getStrategyValueList().get(3)));

            Strategy selectedStrategy = cleaningSoS.generateStrategy(selectedStrategyElement.getStrategyValueList().get(0), selectedStrategyElement.getStrategyValueList().get(1),
                    selectedStrategyElement.getStrategyValueList().get(2), selectedStrategyElement.getStrategyValueList().get(3));
            ExecuteStrategyAtRunTime(cleaningSoS, selectedStrategy, timeFrame);

            System.out.println(String.format("RunTime : %d", runTime));
            caseBase.printCaseBase();

//            System.out.println(String.format("Current numMoppingRobotType1: %d",curConfiguration.getConfigurations().get("numMoppingRobotType1")));
//            System.out.println(String.format("Current numMoppingRobotType2: %d",curConfiguration.getConfigurations().get("numMoppingRobotType2")));
//            System.out.println(String.format("Current numSweepingRobotType1: %d",curConfiguration.getConfigurations().get("numSweepingRobotType1")));
//            System.out.println(String.format("Current numSweepingRobotType2: %d",curConfiguration.getConfigurations().get("numSweepingRobotType2")));

//
            ArrayList<Integer> configurationRet = new ArrayList<>(curConfiguration.getConfigurations().values());
////            System.out.println(String.format("Current selected strategy: %d %d %d %d", selectedStrategyElement.getStrategyValueList().get(0), selectedStrategyElement.getStrategyValueList().get(1),
////                    selectedStrategyElement.getStrategyValueList().get(2), selectedStrategyElement.getStrategyValueList().get(3)));
            System.out.println(String.format("Current Configuration: %d %d %d %d",configurationRet.get(0), configurationRet.get(1),configurationRet.get(2), configurationRet.get(3)));
        }
    }

}
