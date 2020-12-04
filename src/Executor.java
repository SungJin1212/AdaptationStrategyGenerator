import SoS.MCIRSoS;
import SoS.MCIRSoSConfiguration;
import SoS.MCIRSoSEnvironmentCondition;
import SoS.Strategy;
import StrategyPlanner.CasebasedReasoning.CaseBaseElement;
import StrategyPlanner.CasebasedReasoning.CaseBaseValue;
import StrategyPlanner.CasebasedReasoning.Casebase;
import StrategyPlanner.CasebasedReasoning.StrategyElement;
import StrategyPlanner.GenerationEngine;
import StrategyPlanner.Simulator;
import static SoS.MCIRSoS.*;


import java.util.ArrayList;
import java.util.Random;

public class Executor {
    private static Casebase caseBase = new Casebase();
    private static Casebase caseBaseNoSeeding = new Casebase();
    private static Casebase caseBaseWithoutRuntimeContext = new Casebase();
    private ArrayList<MCIRSoSEnvironmentCondition> expectedEnvironmentConditions;
    private ArrayList<MCIRSoSConfiguration> MCIRSoSConfigurations;
    private MCIRSoSConfiguration initialMCIRSoSConfiguration;
    private MCIRSoSEnvironmentCondition initialMCIRSoSEnvironmentCondition;
    private ArrayList<MCIRSoSEnvironmentCondition> runtimeEnvironmentalConditions;
    private GenerationEngine generationEngine;
    private static ArrayList <MCIRSoSEnvironmentCondition> runtimeEnvironments = new ArrayList<>(0);

    public Executor(GenerationEngine generationEngine, ArrayList<MCIRSoSEnvironmentCondition> expectedEnvironmentConditions, ArrayList<MCIRSoSConfiguration> MCIRSoSConfigurations,
                    MCIRSoSConfiguration initialMCIRSoSConfiguration, MCIRSoSEnvironmentCondition initialMCIRSoSEnvironmentCondition,
                    ArrayList<MCIRSoSEnvironmentCondition> runtimeEnvironmentalConditions) {
        this.generationEngine = generationEngine;
        this.expectedEnvironmentConditions = expectedEnvironmentConditions;
        this.MCIRSoSConfigurations = MCIRSoSConfigurations;
        this.initialMCIRSoSConfiguration = initialMCIRSoSConfiguration;
        this.initialMCIRSoSEnvironmentCondition = initialMCIRSoSEnvironmentCondition;
        this.runtimeEnvironmentalConditions = runtimeEnvironmentalConditions;
    }

    public void run(int timeFrame, int runtimeSimulationTime) throws CloneNotSupportedException {
        long start = System.currentTimeMillis(); //시작하는 시점 계산
        ConstructCaseBaseAtDesignTime(timeFrame);
        long end = System.currentTimeMillis();
        System.out.println( "실행 시간 : " + ( end - start)/1000.0 +"초");


        caseBase.printCaseBase();
        //caseBaseNoSeeding.printCaseBase();
        RunTimeSimulation(timeFrame, runtimeSimulationTime);
        RunTimeSimulationNoSeeding(timeFrame, runtimeSimulationTime);
        RunTimeSimulationWithoutContext(timeFrame, runtimeSimulationTime);
    }

    private void ConstructCaseBaseAtDesignTime(int timeFrame) throws CloneNotSupportedException { //set CaseBase at system design time

        for (MCIRSoSConfiguration mcirSoSConfiguration : MCIRSoSConfigurations) {
            CaseBaseElement caseBaseElement = new CaseBaseElement(mcirSoSConfiguration);
            CaseBaseElement caseBaseElementNoSeeding = new CaseBaseElement(mcirSoSConfiguration);
            CaseBaseElement caseBaseElementWithoutRuntimeContext = new CaseBaseElement(mcirSoSConfiguration);
            for (MCIRSoSEnvironmentCondition mcirSoSEnvironmentCondition : expectedEnvironmentConditions) {
                CaseBaseValue caseBaseValue;
                caseBaseValue = generationEngine.getCaseBaseValueAtDesignTime(timeFrame, mcirSoSEnvironmentCondition, (MCIRSoSConfiguration) mcirSoSConfiguration.clone());
                caseBaseValue.calAverageFitness();

                caseBaseElement.addCaseBaseValue((CaseBaseValue) caseBaseValue.clone());
                caseBaseElementNoSeeding.addCaseBaseValue((CaseBaseValue) caseBaseValue.clone());
                caseBaseElementWithoutRuntimeContext.addCaseBaseValue((CaseBaseValue) caseBaseValue.clone());
            }
            caseBase.add(caseBaseElement);
            caseBaseNoSeeding.add(caseBaseElementNoSeeding);
            caseBaseWithoutRuntimeContext.add(caseBaseElementWithoutRuntimeContext);
        }
    }

    private void RunTimeSimulation(int timeFrame, int runtimeSimulationTime) throws CloneNotSupportedException {
        double realTimeUtilityWithSeed = 0.0;
        double realTimeUtilityWithScratch = 0.0;
        double realTimeUtilityWithNoContext = 0.0;
        int simulationReps = 30;

        Simulator simulator = new Simulator();
        System.out.println("----------------------RunTime---------------------");
        MCIRSoS runningSoS = new MCIRSoS(initialMCIRSoSEnvironmentCondition, (MCIRSoSConfiguration) initialMCIRSoSConfiguration.clone());
        System.out.println(String.format("Start Configuration: %d %d %d",initialMCIRSoSConfiguration.getConfigurations().get("numOfFirefighter"),
                initialMCIRSoSConfiguration.getConfigurations().get("numOfHelicopter"),initialMCIRSoSConfiguration.getConfigurations().get("numOfAmbulance")));
        //caseBase.printCaseBase();
        ArrayList<StrategyElement> previousSolutions = caseBase.Retrieve(initialMCIRSoSConfiguration, initialMCIRSoSEnvironmentCondition);
        System.out.println("----------------------Seeding---------------------");
        double realTimeUtility = 0.0;
        for (int runTime = timeFrame; runTime <= runtimeSimulationTime; runTime += timeFrame) {

            runningSoS.setRuntimeEnvironment(runtimeEnvironmentalConditions.get(new Random().nextInt(runtimeEnvironmentalConditions.size()))); //랜덤하게 줄때


            //runningSoS.setRuntimeEnvironment(runtimeEnvironmentalConditions.get(runTime/timeFrame - 1)); //정해진 시나리오


            runtimeEnvironments.add((MCIRSoSEnvironmentCondition) runningSoS.getSoSEnvironmentCondition());
            /*Sense*/
            MCIRSoSConfiguration curConfiguration = (MCIRSoSConfiguration) runningSoS.getSoSConfiguration();
            MCIRSoSEnvironmentCondition curEnvironmentCondition = (MCIRSoSEnvironmentCondition) runningSoS.getSoSEnvironmentCondition();
            //System.out.println(String.format("Before %d", curSeaPatient));
            //System.out.println(String.format("After %d", curSeaPatient));

            ArrayList<StrategyElement> mostSimilarStrategies = caseBase.Retrieve(curConfiguration, curEnvironmentCondition);

            CaseBaseValue evolvedStrategiesWithSeed = generationEngine.getCaseBaseValueAtRunTime(timeFrame, mostSimilarStrategies, curEnvironmentCondition, curConfiguration);
            CaseBaseValue evolvedStrategiesWithScratch = generationEngine.getCaseBaseValueAtRunTimeNoSeeding(timeFrame, mostSimilarStrategies, curEnvironmentCondition, curConfiguration);
            CaseBaseValue evolvedStrategiesWithNoContext = generationEngine.getCaseBaseValueAtRunTime(timeFrame, previousSolutions, curEnvironmentCondition, curConfiguration);

            previousSolutions = evolvedStrategiesWithSeed.getStrategyList();
            double[] ObjectivesSeed = evolvedStrategiesWithSeed.getObjectiveValuesAvg();
            double[] ObjectivesScratch = evolvedStrategiesWithScratch.getObjectiveValuesAvg();
            double[] ObjectivesNoContext = evolvedStrategiesWithNoContext.getObjectiveValuesAvg();

            evolvedStrategiesWithSeed.calAverageFitness();
            evolvedStrategiesWithScratch.calAverageFitness();
            evolvedStrategiesWithNoContext.calAverageFitness();

            caseBase.Store(curConfiguration, mostSimilarStrategies, evolvedStrategiesWithSeed, curEnvironmentCondition);
            //evolvedStrategies.addStrategy(mostSimilarStrategies);
            StrategyElement selectedStrategyElementWithSeed = evolvedStrategiesWithSeed.getBestStrategy();
            StrategyElement selectedStrategyElementWithScratch = evolvedStrategiesWithScratch.getBestStrategy();
            StrategyElement selectedStrategyElementWithNoContext = evolvedStrategiesWithNoContext.getBestStrategy();

            realTimeUtilityWithSeed = 0.0;
            realTimeUtilityWithScratch = 0.0;
            realTimeUtilityWithNoContext = 0.0;



//            for (int s = 1; s <=simulationReps; s++) {
//                if (selectedStrategyElementWithSeed.getStrategyValueList().size() != 0) {
//                    MCIRSoS mcirSoSWithSeed = new MCIRSoS(curEnvironmentCondition, (MCIRSoSConfiguration) curConfiguration.clone());
//                    Strategy selectedStrategyWithSeeding = mcirSoSWithSeed.getStrategy(selectedStrategyElementWithSeed.getStrategyValueList().get(0), selectedStrategyElementWithSeed.getStrategyValueList().get(1),
//                            selectedStrategyElementWithSeed.getStrategyValueList().get(2));
//                    //System.out.println(String.format("Current %d", curSeaPatient));
//                    realTimeUtilityWithSeed += simulator.ExecuteStrategyAtRunTime(mcirSoSWithSeed, (Strategy) selectedStrategyWithSeeding.clone(), timeFrame); // --> Execute
//                }
//                if (selectedStrategyElementWithScratch.getStrategyValueList().size() != 0) {
//                    MCIRSoS mcirSoSWithScratch = new MCIRSoS(curEnvironmentCondition, (MCIRSoSConfiguration) curConfiguration.clone());
//
//                    Strategy selectedStrategyWithScratch = mcirSoSWithScratch.getStrategy(selectedStrategyElementWithScratch.getStrategyValueList().get(0), selectedStrategyElementWithScratch.getStrategyValueList().get(1),
//                            selectedStrategyElementWithScratch.getStrategyValueList().get(2));
//                    //System.out.println(String.format("Current %d", curSeaPatient));
//                    realTimeUtilityWithScratch += simulator.ExecuteStrategyAtRunTime(mcirSoSWithScratch, (Strategy) selectedStrategyWithScratch.clone(), timeFrame); // --> Execute
//                }
//
//                if (selectedStrategyElementWithNoContext.getStrategyValueList().size() != 0) {
//                    MCIRSoS mcirSoSWithNoContext = new MCIRSoS(curEnvironmentCondition, (MCIRSoSConfiguration) curConfiguration.clone());
//
//                    Strategy selectedStrategyWithNoContext = mcirSoSWithNoContext.getStrategy(selectedStrategyElementWithNoContext.getStrategyValueList().get(0), selectedStrategyElementWithNoContext.getStrategyValueList().get(1),
//                            selectedStrategyElementWithNoContext.getStrategyValueList().get(2));
//                    //System.out.println(String.format("Current %d", curSeaPatient));
//                    realTimeUtilityWithNoContext += simulator.ExecuteStrategyAtRunTime(mcirSoSWithNoContext, (Strategy) selectedStrategyWithNoContext.clone(), timeFrame); // --> Execute
//                }
//            }
//            realTimeUtilityWithSeed /= simulationReps;
//            realTimeUtilityWithScratch /= simulationReps;
//            realTimeUtilityWithNoContext /= simulationReps;

            Strategy selectedStrategyRunningSoS = runningSoS.getStrategy(selectedStrategyElementWithSeed.getStrategyValueList().get(0), selectedStrategyElementWithSeed.getStrategyValueList().get(1),
                    selectedStrategyElementWithSeed.getStrategyValueList().get(2));
            realTimeUtility += simulator.ExecuteStrategyAtRunTime(runningSoS, (Strategy) selectedStrategyRunningSoS.clone(), timeFrame);



            System.out.println(String.format("RunTime : %d", runTime));
            caseBase.printCaseBase();
            ArrayList<Integer> configurationRet = new ArrayList<>(0);
            configurationRet.add(runningSoS.getSoSConfiguration().getConfigurations().get("numOfFirefighter"));
            configurationRet.add(runningSoS.getSoSConfiguration().getConfigurations().get("numOfHelicopter"));
            configurationRet.add(runningSoS.getSoSConfiguration().getConfigurations().get("numOfAmbulance"));

            ArrayList<Double> environmentRet = new ArrayList<>(0);
            environmentRet.add(curEnvironmentCondition.getEnvironmentCondition().get("weatherCondition"));
            environmentRet.add(curEnvironmentCondition.getEnvironmentCondition().get("roadCondition"));


            /*3가지 planner의 pareto front의 평균 값 비교*/
            System.out.println(String.format("Quality of adaptation strategy Seed: %f", evolvedStrategiesWithSeed.getAverageFitness()));
            System.out.println(String.format("Quality of adaptation strategy Scratch: %f", evolvedStrategiesWithScratch.getAverageFitness()));
            System.out.println(String.format("Quality of adaptation strategy NoContext: %f", evolvedStrategiesWithNoContext.getAverageFitness()));

            System.out.println(String.format("Current selected strategy: %d %d %d", selectedStrategyElementWithSeed.getStrategyValueList().get(0), selectedStrategyElementWithSeed.getStrategyValueList().get(1),
                    selectedStrategyElementWithSeed.getStrategyValueList().get(2)));
            System.out.println(String.format("Current Utility: %f", realTimeUtility));

            System.out.println(String.format("Current Objectives WithSeed: %f %f %f", ObjectivesSeed[0], ObjectivesSeed[1], ObjectivesSeed[2]));
            System.out.println(String.format("Current Objectives Scratch: %f %f %f", ObjectivesScratch[0], ObjectivesScratch[1], ObjectivesScratch[2]));
            System.out.println(String.format("Current Objectives NoContext: %f %f %f", ObjectivesNoContext[0], ObjectivesNoContext[1], ObjectivesNoContext[2]));

//            System.out.println(String.format("Current Utility WithSeed: %f", realTimeUtilityWithSeed));
//            System.out.println(String.format("Current Utility WithScratch: %f", realTimeUtilityWithScratch));
//            System.out.println(String.format("Current Utility WithNoContext: %f", realTimeUtilityWithNoContext));

            System.out.println(String.format("Current Configuration: %d %d %d",configurationRet.get(0), configurationRet.get(1),configurationRet.get(2)));
            System.out.println(String.format("Current Environment: %f %f", environmentRet.get(0), environmentRet.get(1)));
        }
    }

    private void RunTimeSimulationNoSeeding(int timeFrame, int runtimeSimulationTime) throws CloneNotSupportedException {
        double realTimeUtility = 0.0;
        Simulator simulator = new Simulator();
        System.out.println("----------------------RunTime---------------------");
        MCIRSoS mcirSoS = new MCIRSoS(initialMCIRSoSEnvironmentCondition, (MCIRSoSConfiguration) initialMCIRSoSConfiguration.clone());
        System.out.println(String.format("Start Configuration: %d %d %d",initialMCIRSoSConfiguration.getConfigurations().get("numOfFirefighter"),
                initialMCIRSoSConfiguration.getConfigurations().get("numOfHelicopter"),initialMCIRSoSConfiguration.getConfigurations().get("numOfAmbulance")));
        //caseBaseNoSeeding.printCaseBase();
        System.out.println("----------------------NoSeeding---------------------");
        int index = 0;

        for (int runTime = timeFrame; runTime <= runtimeSimulationTime; runTime += timeFrame) {


            mcirSoS.setRuntimeEnvironment(runtimeEnvironments.get(index++));

            MCIRSoSConfiguration curConfiguration = (MCIRSoSConfiguration) mcirSoS.getSoSConfiguration();
            MCIRSoSEnvironmentCondition curEnvironmentCondition = (MCIRSoSEnvironmentCondition) mcirSoS.getSoSEnvironmentCondition();

            ArrayList<StrategyElement> mostSimilarStrategies = caseBaseNoSeeding.Retrieve(curConfiguration, curEnvironmentCondition);

            CaseBaseValue evolvedStrategiesNoSeed = generationEngine.getCaseBaseValueAtRunTimeNoSeeding(timeFrame, mostSimilarStrategies, curEnvironmentCondition, curConfiguration);
            evolvedStrategiesNoSeed.calAverageFitness();

            caseBaseNoSeeding.Store(curConfiguration, mostSimilarStrategies, evolvedStrategiesNoSeed, curEnvironmentCondition);

            StrategyElement selectedStrategyElement = evolvedStrategiesNoSeed.getBestStrategy(); //select StrategyElement
            mcirSoS = new MCIRSoS(curEnvironmentCondition, curConfiguration);
            if (selectedStrategyElement.getStrategyValueList().size() != 0) {
                Strategy selectedStrategy = mcirSoS.getStrategy(selectedStrategyElement.getStrategyValueList().get(0), selectedStrategyElement.getStrategyValueList().get(1),
                        selectedStrategyElement.getStrategyValueList().get(2));

                realTimeUtility += simulator.ExecuteStrategyAtRunTime(mcirSoS, selectedStrategy, timeFrame); // --> Execute
            }


            System.out.println(String.format("RunTime : %d", runTime));
            caseBaseNoSeeding.printCaseBase();



            ArrayList<Integer> configurationRet = new ArrayList<>(0);
            configurationRet.add(mcirSoS.getSoSConfiguration().getConfigurations().get("numOfFirefighter"));
            configurationRet.add(mcirSoS.getSoSConfiguration().getConfigurations().get("numOfHelicopter"));
            configurationRet.add(mcirSoS.getSoSConfiguration().getConfigurations().get("numOfAmbulance"));

            ArrayList<Double> environmentRet = new ArrayList<>(0);
            environmentRet.add(curEnvironmentCondition.getEnvironmentCondition().get("weatherCondition"));
            environmentRet.add(curEnvironmentCondition.getEnvironmentCondition().get("roadCondition"));


            System.out.println(String.format("Quality of adaptation strategy Seed: %f", evolvedStrategiesNoSeed.getAverageFitness()));

            System.out.println(String.format("Current selected strategy: %d %d %d", selectedStrategyElement.getStrategyValueList().get(0), selectedStrategyElement.getStrategyValueList().get(1),
                   selectedStrategyElement.getStrategyValueList().get(2)));
            System.out.println(String.format("Current Utility: %f", realTimeUtility));
            System.out.println(String.format("Current Configuration: %d %d %d", configurationRet.get(0), configurationRet.get(1), configurationRet.get(2)));
            System.out.println(String.format("Current Environment: %f %f", environmentRet.get(0), environmentRet.get(1)));


        }
    }

    private void RunTimeSimulationWithoutContext(int timeFrame, int runtimeSimulationTime) throws CloneNotSupportedException {
        double realTimeUtility = 0.0;

        Simulator simulator = new Simulator();
        System.out.println("----------------RunTimeWithoutContext---------------");
        MCIRSoS mcirSoS = new MCIRSoS(initialMCIRSoSEnvironmentCondition, (MCIRSoSConfiguration) initialMCIRSoSConfiguration.clone());
        System.out.println(String.format("Start Configuration: %d %d %d",initialMCIRSoSConfiguration.getConfigurations().get("numOfFirefighter"),
                initialMCIRSoSConfiguration.getConfigurations().get("numOfHelicopter"),initialMCIRSoSConfiguration.getConfigurations().get("numOfAmbulance")));
        //caseBaseWithoutRuntimeContext.printCaseBase();
        System.out.println("-----------------SeedingWithoutContext--------");

        MCIRSoSConfiguration curConfiguration = (MCIRSoSConfiguration) mcirSoS.getSoSConfiguration();
        MCIRSoSEnvironmentCondition curEnvironmentCondition = (MCIRSoSEnvironmentCondition) mcirSoS.getSoSEnvironmentCondition();

        ArrayList<StrategyElement> mostSimilarStrategies = caseBaseWithoutRuntimeContext.Retrieve(curConfiguration, curEnvironmentCondition);
        int index = 0;


        for (int runTime = timeFrame; runTime <= runtimeSimulationTime; runTime += timeFrame) {

            mcirSoS.setRuntimeEnvironment(runtimeEnvironments.get(index++));

            curConfiguration = (MCIRSoSConfiguration) mcirSoS.getSoSConfiguration();
            curEnvironmentCondition = (MCIRSoSEnvironmentCondition) mcirSoS.getSoSEnvironmentCondition();

//            mcirSoS = new MCIRSoS(curEnvironmentCondition, (MCIRSoSConfiguration) curConfiguration.clone());

            CaseBaseValue evolvedStrategiesNoContext = generationEngine.getCaseBaseValueAtRunTime(timeFrame, mostSimilarStrategies, curEnvironmentCondition, curConfiguration);
            evolvedStrategiesNoContext.calAverageFitness();
            caseBaseWithoutRuntimeContext.Store(curConfiguration, mostSimilarStrategies, evolvedStrategiesNoContext, curEnvironmentCondition);

            mostSimilarStrategies = evolvedStrategiesNoContext.getStrategyList();

            StrategyElement selectedStrategyElement = evolvedStrategiesNoContext.getBestStrategy(); //select StrategyElement
            mcirSoS = new MCIRSoS(curEnvironmentCondition, curConfiguration);
            if (selectedStrategyElement.getStrategyValueList().size() != 0) {
                Strategy selectedStrategy = mcirSoS.getStrategy(selectedStrategyElement.getStrategyValueList().get(0), selectedStrategyElement.getStrategyValueList().get(1),
                        selectedStrategyElement.getStrategyValueList().get(2));

                realTimeUtility += simulator.ExecuteStrategyAtRunTime(mcirSoS, selectedStrategy, timeFrame); // --> Execute
            }


            System.out.println(String.format("RunTime : %d", runTime));
            caseBaseWithoutRuntimeContext.printCaseBase();
            ArrayList<Integer> configurationRet = new ArrayList<>(0);
            configurationRet.add(mcirSoS.getSoSConfiguration().getConfigurations().get("numOfFirefighter"));
            configurationRet.add(mcirSoS.getSoSConfiguration().getConfigurations().get("numOfHelicopter"));
            configurationRet.add(mcirSoS.getSoSConfiguration().getConfigurations().get("numOfAmbulance"));

            ArrayList<Double> environmentRet = new ArrayList<>(0);
            environmentRet.add(curEnvironmentCondition.getEnvironmentCondition().get("weatherCondition"));
            environmentRet.add(curEnvironmentCondition.getEnvironmentCondition().get("roadCondition"));

            System.out.println(String.format("Quality of adaptation strategy NoContext: %f", evolvedStrategiesNoContext.getAverageFitness()));


            System.out.println(String.format("Current selected strategy: %d %d %d", selectedStrategyElement.getStrategyValueList().get(0), selectedStrategyElement.getStrategyValueList().get(1),
                    selectedStrategyElement.getStrategyValueList().get(2)));
            System.out.println(String.format("Current Utility: %f", realTimeUtility));
            System.out.println(String.format("Current Configuration: %d %d %d",configurationRet.get(0), configurationRet.get(1),configurationRet.get(2)));
            //System.out.println(csModelList.size());
            System.out.println(String.format("Current Environment: %f %f", environmentRet.get(0), environmentRet.get(1)));
        }
    }
}
