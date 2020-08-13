import Model.GeneratedCode.Behavior.CleaningSoS;
import Model.GeneratedCode.Rule.CleaningSoSConfiguration;
import Model.GeneratedCode.Rule.CleaningSoSEnvironmentCondition;
import StrategyGenerationEngine.Casebase.CaseBase;
import StrategyGenerationEngine.Casebase.CaseBaseElement;
import StrategyGenerationEngine.Element.CaseBaseValue;
import StrategyGenerationEngine.Element.StrategyElement;

import java.util.ArrayList;

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

        cleaningSoSConfigurations.add(new CleaningSoSConfiguration(1,1,1,1));
        cleaningSoSConfigurations.add(new CleaningSoSConfiguration(5,5,5,5));

        ConstructCaseBaseAtDesignTime(expectedCleaningSoSEnvironmentConditions, cleaningSoSConfigurations);
    }

    private static void ConstructCaseBaseAtDesignTime(ArrayList<CleaningSoSEnvironmentCondition> expectedCleaningSoSEnvironmentConditions, ArrayList<CleaningSoSConfiguration> cleaningSoSConfigurations) throws CloneNotSupportedException {
        for(CleaningSoSConfiguration cleaningSoSConfiguration : cleaningSoSConfigurations) {
            CaseBaseElement caseBaseElement = new CaseBaseElement(cleaningSoSConfiguration);
            for(CleaningSoSEnvironmentCondition cleaningSoSEnvironmentCondition : expectedCleaningSoSEnvironmentConditions) {
                CaseBaseValue caseBaseValue = getCaseBaseValueAtDesignTime(cleaningSoSEnvironmentCondition, (CleaningSoSConfiguration) cleaningSoSConfiguration.clone());
                caseBaseElement.addCaseBaseValue(caseBaseValue);
            }
            caseBase.add(caseBaseElement);
        }
    }

    private static void RunTimeSimulation(int adaptationTriggerTime, int runtimeSimulationTime) throws CloneNotSupportedException {
        CleaningSoSConfiguration initialCleaningSoSConfiguration = new CleaningSoSConfiguration(5, 5, 5, 5);
        CleaningSoSEnvironmentCondition initialCleaningSoSEnvironmentCondition = new CleaningSoSEnvironmentCondition(10);
        //evolve the pareto fronts
        //execute the strategy

        CleaningSoS cleaningSoS = new CleaningSoS(initialCleaningSoSEnvironmentCondition, initialCleaningSoSConfiguration);
        for (int tick = 1; tick <= runtimeSimulationTime; tick++) {
            if (runtimeSimulationTime % adaptationTriggerTime == 0) { // 매 adaptationTime 마다
                CleaningSoSConfiguration curConfiguration = (CleaningSoSConfiguration) cleaningSoS.getSoSConfiguration();//get Current Configuration
                CleaningSoSEnvironmentCondition curEnvironmentCondition = (CleaningSoSEnvironmentCondition) cleaningSoS.getSoSEnvironmentCondition(); //get Current EnvironmentCondition
                ArrayList<StrategyElement> mostSimilarStrategies = caseBase.Retrieve(curConfiguration, curEnvironmentCondition);//get most similar configuration of the current configuration
                CaseBaseValue evolvedStrategies = getCaseBaseValueAtRunTime(mostSimilarStrategies, curEnvironmentCondition, curConfiguration);
            }
        }
    }
}
