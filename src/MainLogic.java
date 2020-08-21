import Model.AbstactClass.Rule.Strategy;
import Model.GeneratedCode.Behavior.CleaningSoS;
import Model.GeneratedCode.Rule.CleaningSoSConfiguration;
import Model.GeneratedCode.Rule.CleaningSoSEnvironmentCondition;
import StrategyGenerationEngine.Casebase.CaseBase;
import StrategyGenerationEngine.Casebase.CaseBaseElement;
import StrategyGenerationEngine.Element.CaseBaseValue;
import StrategyGenerationEngine.Element.StrategyElement;

import java.util.ArrayList;

import static Simulator.SimulationEngine.ExecuteStrategyAtRunTime;
import static StrategyGenerationEngine.GenerationEngine.getCaseBaseValueAtDesignTime;
import static StrategyGenerationEngine.GenerationEngine.getCaseBaseValueAtRunTime;


public class MainLogic implements Cloneable {

    private static CaseBase caseBase = new CaseBase();

    public static void main(String[] args) throws CloneNotSupportedException {
        ArrayList <CaseBase> results = new ArrayList<>(0);

        caseBase = new CaseBase();
        ConstructCaseBaseAtDesignTime();
        caseBase.printCaseBase();
//        RunTimeSimulationWithoutCaseBase(50, 1000);
        RunTimeSimulation(100, 3000);
        results.add(caseBase);


//        System.out.println("---------End of the RunTime---------");
//        caseBase.printCaseBase();

    }

    private static void ConstructCaseBaseAtDesignTime() throws CloneNotSupportedException { //set CaseBase at system design time
        ArrayList<CleaningSoSEnvironmentCondition> expectedCleaningSoSEnvironmentConditions = new ArrayList<>(0);
        ArrayList<CleaningSoSConfiguration> cleaningSoSConfigurations = new ArrayList<>(0);

        expectedCleaningSoSEnvironmentConditions.add(new CleaningSoSEnvironmentCondition(10));
        expectedCleaningSoSEnvironmentConditions.add(new CleaningSoSEnvironmentCondition(20));

        cleaningSoSConfigurations.add(new CleaningSoSConfiguration(5, 5, 5, 5));
        cleaningSoSConfigurations.add(new CleaningSoSConfiguration(15, 15, 15, 15));


        for(CleaningSoSConfiguration cleaningSoSConfiguration : cleaningSoSConfigurations) {
            CaseBaseElement caseBaseElement = new CaseBaseElement(cleaningSoSConfiguration);
            for(CleaningSoSEnvironmentCondition cleaningSoSEnvironmentCondition : expectedCleaningSoSEnvironmentConditions) {
                CaseBaseValue caseBaseValue;
                caseBaseValue = getCaseBaseValueAtDesignTime(cleaningSoSEnvironmentCondition, (CleaningSoSConfiguration) cleaningSoSConfiguration.clone());
                caseBaseValue.calAverageFitness();
                caseBaseElement.addCaseBaseValue(caseBaseValue);
            }
            caseBase.add(caseBaseElement);

        }
    }

    private static void RunTimeSimulationWithoutCaseBase(int timeFrame, int runtimeSimulationTime) throws CloneNotSupportedException {
        int environmentSlice = 10;
        int maxEnvironment = 10;

        CleaningSoSConfiguration initialCleaningSoSConfiguration = new CleaningSoSConfiguration(10, 10, 10, 10);
        CleaningSoSEnvironmentCondition initialCleaningSoSEnvironmentCondition = new CleaningSoSEnvironmentCondition(10);
        CleaningSoS cleaningSoS = new CleaningSoS(initialCleaningSoSEnvironmentCondition, initialCleaningSoSConfiguration);

        for (int runTime = 50; runTime <= runtimeSimulationTime; runTime += timeFrame) {
            cleaningSoS.setRuntimeEnvironment(new CleaningSoSEnvironmentCondition((int) ((Math.random() * ( maxEnvironment - 1 )) + 1) * environmentSlice));

            CleaningSoSConfiguration curConfiguration = (CleaningSoSConfiguration) cleaningSoS.getSoSConfiguration();//get Current Configuration

            CleaningSoSEnvironmentCondition curEnvironmentCondition = (CleaningSoSEnvironmentCondition) cleaningSoS.getSoSEnvironmentCondition(); //get Current EnvironmentCondition
            // 현재의 configuration, environmentCondition get
            ArrayList<StrategyElement> mostSimilarStrategies = new ArrayList<>(0);
            //ArrayList<StrategyElement> mostSimilarStrategies = caseBase.Retrieve(curConfiguration, curEnvironmentCondition);//get most similar configuration of the current configuration

            CaseBaseValue evolvedStrategies = getCaseBaseValueAtRunTime(mostSimilarStrategies, curEnvironmentCondition, curConfiguration);
            evolvedStrategies.calAverageFitness();
            // 비슷한 strategyElements retrieve, evolve and set fitness value

            StrategyElement selectedStrategyElement = evolvedStrategies.getStrategyList().get(evolvedStrategies.getStrategyList().size() - 1); //select StrategyElement
            Strategy selectedStrategy = cleaningSoS.getStrategy(selectedStrategyElement.getStrategyValueList().get(0), selectedStrategyElement.getStrategyValueList().get(1),
                    selectedStrategyElement.getStrategyValueList().get(2), selectedStrategyElement.getStrategyValueList().get(3));
            // strategy select & execute strategy

            ExecuteStrategyAtRunTime(cleaningSoS, selectedStrategy, timeFrame);
            caseBase.Store(curConfiguration, evolvedStrategies, curEnvironmentCondition);

            System.out.println(String.format("RunTime : %d", runTime));
            caseBase.printCaseBase();
        }
    }
    private static void RunTimeSimulation(int timeFrame, int runtimeSimulationTime) throws CloneNotSupportedException {

        int environmentSlice = 10;
        int maxEnvironment = 5;

        CleaningSoSConfiguration initialCleaningSoSConfiguration = new CleaningSoSConfiguration(10,10,10,10);
        CleaningSoSEnvironmentCondition initialCleaningSoSEnvironmentCondition = new CleaningSoSEnvironmentCondition(10);
        CleaningSoS cleaningSoS = new CleaningSoS(initialCleaningSoSEnvironmentCondition, initialCleaningSoSConfiguration);

        for (int runTime = 100; runTime <= runtimeSimulationTime; runTime += timeFrame) {

            cleaningSoS.setRuntimeEnvironment(new CleaningSoSEnvironmentCondition((int) ((Math.random() * ( maxEnvironment - 1 )) + 1) * environmentSlice));

            // Get Current configuration, environmentCondition
            CleaningSoSConfiguration curConfiguration = (CleaningSoSConfiguration) cleaningSoS.getSoSConfiguration(); //get Current Configuration
            CleaningSoSEnvironmentCondition curEnvironmentCondition = (CleaningSoSEnvironmentCondition) cleaningSoS.getSoSEnvironmentCondition(); //get Current EnvironmentCondition

            ArrayList<StrategyElement> mostSimilarStrategies = caseBase.Retrieve(curConfiguration, curEnvironmentCondition);//get most similar configuration of the current configuration

            // Retrieve strategyElements retrieve, evolve and set fitness value
            CaseBaseValue evolvedStrategies = getCaseBaseValueAtRunTime(mostSimilarStrategies, curEnvironmentCondition, curConfiguration);
            evolvedStrategies.calAverageFitness();

            caseBase.Store(curConfiguration, evolvedStrategies, curEnvironmentCondition);
            // strategy select & execute strategy
            System.out.println(evolvedStrategies.getStrategyList().size());
            StrategyElement selectedStrategyElement = evolvedStrategies.getBestStrategy(); //select StrategyElement

//            System.out.println(String.format("%d %d %d %d", selectedStrategyElement.getStrategyValueList().get(0), selectedStrategyElement.getStrategyValueList().get(1),
//                    selectedStrategyElement.getStrategyValueList().get(2), selectedStrategyElement.getStrategyValueList().get(3)));

            Strategy selectedStrategy = cleaningSoS.getStrategy(selectedStrategyElement.getStrategyValueList().get(0), selectedStrategyElement.getStrategyValueList().get(1),
                    selectedStrategyElement.getStrategyValueList().get(2), selectedStrategyElement.getStrategyValueList().get(3));
            ExecuteStrategyAtRunTime(cleaningSoS, selectedStrategy, timeFrame);

            System.out.println(String.format("RunTime : %d", runTime));
            caseBase.printCaseBase();

//            ArrayList<Integer> configurationRet = new ArrayList<>(curConfiguration.getConfiguration().values());
//            System.out.println(String.format("Current selected strategy: %d %d %d %d", selectedStrategyElement.getStrategyValueList().get(0), selectedStrategyElement.getStrategyValueList().get(1),
//                    selectedStrategyElement.getStrategyValueList().get(2), selectedStrategyElement.getStrategyValueList().get(3)));
//            System.out.println(String.format("Current Configuration: %d %d %d %d",configurationRet.get(0), configurationRet.get(1),configurationRet.get(2), configurationRet.get(3)));
        }
    }
}
