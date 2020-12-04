import SoS.MCIRSoS;
import SoS.MCIRSoSConfiguration;
import SoS.MCIRSoSEnvironmentCondition;
import StrategyPlanner.GenerationEngine;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws CloneNotSupportedException {
        ArrayList<MCIRSoSEnvironmentCondition> expectedMCIRSoSEnvironmentConditions = new ArrayList<>(0);
        expectedMCIRSoSEnvironmentConditions.add(new MCIRSoSEnvironmentCondition(0.2,2));
        expectedMCIRSoSEnvironmentConditions.add(new MCIRSoSEnvironmentCondition(0.3,3));
        expectedMCIRSoSEnvironmentConditions.add(new MCIRSoSEnvironmentCondition(0.4,4));


        ArrayList<MCIRSoSConfiguration> MCIRSoSConfigurations = new ArrayList<>(0);
        int FFinterval = 50; //Firefighter interval
        int HCinterval = 50; //Helicopter interval
        int AMinterval = 50; //Ambulance interval

        int maxFF = 100, maxHC = 100, maxAM = 100;

//        int DiscretizedInterval = 5;
//        for(int i=1; i<=DiscretizedInterval; i++) {
//            MCIRSoSConfigurations.add(new MCIRSoSConfiguration(i * maxFF/DiscretizedInterval,
//                    i * maxHC/DiscretizedInterval, i * maxAM/DiscretizedInterval));
//        }

//

        for (int i=FFinterval; i<=maxFF; i+=FFinterval) {
            for(int j=HCinterval; j<=maxHC; j+=HCinterval) {
                for(int k=AMinterval; k<=maxAM; k+=AMinterval) {
                    MCIRSoSConfigurations.add(new MCIRSoSConfiguration(i,j,k));
                }
            }
        }

//        MCIRSoSConfigurations.add(new MCIRSoSConfiguration(10,10,10));
//        MCIRSoSConfigurations.add(new MCIRSoSConfiguration(20,20,20));

        //MCIRSoSConfiguration initialMCIRSoSConfiguration = new MCIRSoSConfiguration(7,7,7);
        // Start Configuration and Environment Condition
        MCIRSoSConfiguration initialMCIRSoSConfiguration = new MCIRSoSConfiguration(3,3,3);
        MCIRSoSEnvironmentCondition initialMCIRSoSEnvironment = new MCIRSoSEnvironmentCondition(0.2,2);

        ArrayList<MCIRSoSEnvironmentCondition> runtimeEnvironmentConditions = new ArrayList<>(0);

        for (int i=1; i<=7; i++)
        {
            for(int j=1; j<=7; j++) {
                runtimeEnvironmentConditions.add(new MCIRSoSEnvironmentCondition(i * 0.1, j));
            }
        }

        //Scenario1: 도로 상황이 계속 안좋아 지는 상황
        //runtimeEnvironmentConditions.add(new MCIRSoSEnvironmentCondition(0.2, 4));
        //runtimeEnvironmentConditions.add(new MCIRSoSEnvironmentCondition(0.2, 6));
        //runtimeEnvironmentConditions.add(new MCIRSoSEnvironmentCondition(0.2, 8));




        //MCIRSoSProblem 에 Simulation Reps 수정
        GenerationEngine generationEngine = new GenerationEngine(30, 10, 30, 3, 15, "NSGAII");

        Executor executor = new Executor(generationEngine, expectedMCIRSoSEnvironmentConditions, MCIRSoSConfigurations, initialMCIRSoSConfiguration, initialMCIRSoSEnvironment, runtimeEnvironmentConditions);
        executor.run(200,4000);
    }
}
