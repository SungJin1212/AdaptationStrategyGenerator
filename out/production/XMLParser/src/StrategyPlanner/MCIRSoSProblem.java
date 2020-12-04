package StrategyPlanner;

import SoS.MCIRSoS;
import SoS.MCIRSoSConfiguration;
import SoS.MCIRSoSEnvironmentCondition;
import SoS.Strategy;
import org.moeaframework.analysis.sensitivity.Negater;
import org.moeaframework.core.Solution;
import org.moeaframework.core.variable.EncodingUtils;
import org.moeaframework.problem.AbstractProblem;

public class MCIRSoSProblem extends AbstractProblem {

    private MCIRSoSEnvironmentCondition mcirSoSEnvironmentCondition;
    private MCIRSoSConfiguration mcirSoSConfiguration;
    private int simulationTime;

    private int maxNumOfFirefighter;
    private int minNumOfFirefighter;
    private int maxNumOfHelicopter;
    private int minNumOfHelicopter;
    private int maxNumOfAmbulance;
    private int minNumOfAmbulance;

    public MCIRSoSProblem(MCIRSoSEnvironmentCondition mcirSoSEnvironmentCondition, MCIRSoSConfiguration mcirSoSConfiguration, int simulationTime) {
        super(mcirSoSConfiguration.getNumOfVariables(), 3, mcirSoSConfiguration.getNumOfVariables());
        this.mcirSoSEnvironmentCondition = mcirSoSEnvironmentCondition;
        this.mcirSoSConfiguration = mcirSoSConfiguration;
        this.simulationTime = simulationTime;

         maxNumOfFirefighter = mcirSoSConfiguration.getMaxConfigurations().get("numOfFirefighter");
         minNumOfFirefighter = mcirSoSConfiguration.getMinConfigurations().get("numOfFirefighter");

         maxNumOfHelicopter = mcirSoSConfiguration.getMaxConfigurations().get("numOfHelicopter");
         minNumOfHelicopter = mcirSoSConfiguration.getMinConfigurations().get("numOfHelicopter");

         maxNumOfAmbulance = mcirSoSConfiguration.getMaxConfigurations().get("numOfAmbulance");
         minNumOfAmbulance = mcirSoSConfiguration.getMinConfigurations().get("numOfAmbulance");
    }

    @Override
    public void evaluate(Solution solution) {

        Simulator simulator = new Simulator();
        int diffFirefighter = EncodingUtils.getInt(solution.getVariable(0));
        int diffHelicopter = EncodingUtils.getInt(solution.getVariable(1));
        int diffAmbulance = EncodingUtils.getInt(solution.getVariable(2));
        int curNumOfFirefighter = mcirSoSConfiguration.getConfigurations().get("numOfFirefighter");
        int curNumOfHelicopter = mcirSoSConfiguration.getConfigurations().get("numOfHelicopter");
        int curNumOfAmbulance = mcirSoSConfiguration.getConfigurations().get("numOfAmbulance");

        double[] Goals = null;

        try {
            MCIRSoS mcirSoS = new MCIRSoS(this.mcirSoSEnvironmentCondition, (MCIRSoSConfiguration) this.mcirSoSConfiguration.clone());
            //System.out.println(mcirSoS.curSeaPatient);



            if (curNumOfFirefighter + diffFirefighter > maxNumOfFirefighter) {
                diffFirefighter = maxNumOfFirefighter;
            }
            else if (curNumOfFirefighter + diffFirefighter < minNumOfFirefighter) {
                diffFirefighter = minNumOfFirefighter;
            }
            if (curNumOfHelicopter + diffHelicopter > maxNumOfHelicopter) {
                diffHelicopter = maxNumOfHelicopter;
            }
            else if (curNumOfHelicopter + diffHelicopter < minNumOfHelicopter) {
                diffHelicopter = minNumOfHelicopter;
            }
            if (curNumOfAmbulance + diffAmbulance > maxNumOfAmbulance) {
                diffAmbulance = maxNumOfAmbulance;
            }
            else if (curNumOfAmbulance + diffAmbulance < minNumOfAmbulance) {
                diffAmbulance = minNumOfAmbulance;
            }

            Strategy strategy = mcirSoS.getStrategy(diffFirefighter, diffHelicopter, diffAmbulance);
            //System.out.println(String.format("DiffFirefighter: %d diffHelicopter: %d diffAmbulance: %d",diffFirefighter, diffHelicopter, diffAmbulance));




            Goals = simulator.evaluateStrategy(mcirSoS,strategy,this.simulationTime,1);
            //System.out.println(Goals[0]);
            //TODO: call Simulator and get Utility Value and Latency
        }
        catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }

//        int c0 = mcirSoSConfiguration.getConfigurations().get("numOfFirefighter") + diffFirefighter;
//        int c1 = mcirSoSConfiguration.getConfigurations().get("numOfHelicopter") + diffHelicopter;
//        int c2 = mcirSoSConfiguration.getConfigurations().get("numOfAmbulance") + diffAmbulance;

        assert Goals != null;

        Negater negater = new Negater();
        //Goals[0] = -Goals[0];
        solution.setObjective(0, -Goals[0]); // Maximize;
        solution.setObjective(1, Goals[1]); // Minimize
        solution.setObjective(2, Goals[2]); // Minimize
        int c0 = curNumOfFirefighter + diffFirefighter;
        int c1 = curNumOfHelicopter + diffHelicopter;
        int c2 = curNumOfAmbulance + diffAmbulance;
        solution.setConstraint(0, c0 >= minNumOfFirefighter && c0 <= maxNumOfFirefighter ? 0 : c0);
        solution.setConstraint(1, c1 >= minNumOfHelicopter && c1 <= maxNumOfHelicopter ? 0 : c1);
        solution.setConstraint(2, c2 >= minNumOfAmbulance && c2 <= maxNumOfAmbulance ? 0 : c2);
//        solution.setConstraint(0, c0 >= minNumOfFirefighter && c0 <= maxNumOfFirefighter ? 0.0 : c0);
//        solution.setConstraint(1, c1 >= minNumOfHelicopter && c1 <= maxNumOfHelicopter ? 0.0 : c1);
//        solution.setConstraint(2, c2 >= minNumOfAmbulance && c2 <= maxNumOfAmbulance ? 0.0 : c2);


    }

    @Override
    public Solution newSolution() {

        Solution solution = new Solution(getNumberOfVariables(), getNumberOfObjectives(), getNumberOfConstraints());

        solution.setVariable(0, EncodingUtils.newInt(-50, 50));
        solution.setVariable(1, EncodingUtils.newInt(-50, 50));
        solution.setVariable(2, EncodingUtils.newInt(-50, 50));


        return solution;
    }
}
