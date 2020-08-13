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
        ConstructCaseBaseAtDesignTime();
        RunTimeSimulation(50,1000);
    }

    private static void ConstructCaseBaseAtDesignTime() throws CloneNotSupportedException { //set CaseBase at system design time
        ArrayList<CleaningSoSEnvironmentCondition> expectedCleaningSoSEnvironmentConditions = new ArrayList<>(0);
        ArrayList<CleaningSoSConfiguration> cleaningSoSConfigurations = new ArrayList<>(0);

        expectedCleaningSoSEnvironmentConditions.add(new CleaningSoSEnvironmentCondition(10));
        expectedCleaningSoSEnvironmentConditions.add(new CleaningSoSEnvironmentCondition(50));
        expectedCleaningSoSEnvironmentConditions.add(new CleaningSoSEnvironmentCondition(100));

        cleaningSoSConfigurations.add(new CleaningSoSConfiguration(5,5,5,5));
        cleaningSoSConfigurations.add(new CleaningSoSConfiguration(15,15,15,15));

        ConstructCaseBaseAtDesignTime(expectedCleaningSoSEnvironmentConditions, cleaningSoSConfigurations);
    }

    private static void ConstructCaseBaseAtDesignTime(ArrayList<CleaningSoSEnvironmentCondition> expectedCleaningSoSEnvironmentConditions, ArrayList<CleaningSoSConfiguration> cleaningSoSConfigurations) throws CloneNotSupportedException {
        for(CleaningSoSConfiguration cleaningSoSConfiguration : cleaningSoSConfigurations) {
            CaseBaseElement caseBaseElement = new CaseBaseElement(cleaningSoSConfiguration);
            for(CleaningSoSEnvironmentCondition cleaningSoSEnvironmentCondition : expectedCleaningSoSEnvironmentConditions) {
                CaseBaseValue caseBaseValue = getCaseBaseValueAtDesignTime(cleaningSoSEnvironmentCondition, (CleaningSoSConfiguration) cleaningSoSConfiguration.clone());
                caseBaseValue.calAverageFitness();
                caseBaseElement.addCaseBaseValue(caseBaseValue);
            }
            caseBase.add(caseBaseElement);

        }
    }

    private static void RunTimeSimulation(int timeFrame, int runtimeSimulationTime) throws CloneNotSupportedException {
        CleaningSoSConfiguration initialCleaningSoSConfiguration = new CleaningSoSConfiguration(5, 5, 5, 5);
        CleaningSoSEnvironmentCondition initialCleaningSoSEnvironmentCondition = new CleaningSoSEnvironmentCondition(10);
        CleaningSoS cleaningSoS = new CleaningSoS(initialCleaningSoSEnvironmentCondition, initialCleaningSoSConfiguration);

        for (int runTime = 1; runTime <= runtimeSimulationTime; runTime += timeFrame) {
            CleaningSoSConfiguration curConfiguration = (CleaningSoSConfiguration) cleaningSoS.getSoSConfiguration();//get Current Configuration
            CleaningSoSEnvironmentCondition curEnvironmentCondition = (CleaningSoSEnvironmentCondition) cleaningSoS.getSoSEnvironmentCondition(); //get Current EnvironmentCondition
            // 현재의 configuration, environmentCondition get

            ArrayList<StrategyElement> mostSimilarStrategies = caseBase.Retrieve(curConfiguration, curEnvironmentCondition);//get most similar configuration of the current configuration

            CaseBaseValue evolvedStrategies = getCaseBaseValueAtRunTime(mostSimilarStrategies, curEnvironmentCondition, curConfiguration);
            evolvedStrategies.calAverageFitness();
            // 비슷한 strategyElements retrieve, evolve and set fitness value

            StrategyElement selectedStrategyElement = evolvedStrategies.getStrategyList().get(0); //select StrategyElement
            Strategy selectedStrategy = cleaningSoS.getStrategy(selectedStrategyElement.getStrategyValueList().get(0), selectedStrategyElement.getStrategyValueList().get(1),
                    selectedStrategyElement.getStrategyValueList().get(2), selectedStrategyElement.getStrategyValueList().get(3));
            // strategy select & execute strategy

            ExecuteStrategyAtRunTime(cleaningSoS, selectedStrategy, timeFrame);
            caseBase.Store(curConfiguration,evolvedStrategies,curEnvironmentCondition);
        }
    }
}
