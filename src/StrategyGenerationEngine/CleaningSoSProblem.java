package StrategyGenerationEngine;

import Model.AbstactClass.Rule.Strategy;
import Model.GeneratedCode.Behavior.CleaningSoS;
import Model.GeneratedCode.Rule.CleaningSoSConfiguration;
import Model.GeneratedCode.Rule.CleaningSoSEnvironmentCondition;
import org.moeaframework.core.Solution;
import org.moeaframework.core.variable.EncodingUtils;
import org.moeaframework.problem.AbstractProblem;

import static StrategyGenerationEngine.Simulator.evaluateStrategy;


public class CleaningSoSProblem extends AbstractProblem {

    private CleaningSoSEnvironmentCondition cleaningSoSEnvironmentCondition;
    private CleaningSoSConfiguration cleaningSoSConfiguration;
    private int simulationTime;


    public CleaningSoSProblem(CleaningSoSEnvironmentCondition cleaningSoSEnvironmentCondition, CleaningSoSConfiguration cleaningSoSConfiguration, int simulationTime) {
        super(cleaningSoSConfiguration.getNumOfVariables(),3, cleaningSoSConfiguration.getNumOfVariables());
        this.cleaningSoSEnvironmentCondition = cleaningSoSEnvironmentCondition;
        this.cleaningSoSConfiguration = cleaningSoSConfiguration;
        this.simulationTime = simulationTime;
        //System.out.println(cleaningSoSConfiguration.getNumOfVariables());

    }

    @Override
    public void evaluate(Solution solution) {
        int maxRobot = 20;
        int minRobot = 1;
        int MType1 = EncodingUtils.getInt(solution.getVariable(0));
        int MType2 = EncodingUtils.getInt(solution.getVariable(1));
        int SType1 = EncodingUtils.getInt(solution.getVariable(2));
        int SType2 = EncodingUtils.getInt(solution.getVariable(3));
        double[] Goals = null;



        //CleaningSoSEnvironmentCondition cleaningSoSEnvironmentCondition = new CleaningSoSEnvironmentCondition(10);
        //CleaningSoSConfiguration configuration = new CleaningSoSConfiguration(5, 5, 5, 5);
        /*making strategy*/
        try {
            CleaningSoS cleaningSoS = new CleaningSoS(this.cleaningSoSEnvironmentCondition, (CleaningSoSConfiguration) this.cleaningSoSConfiguration.clone()); // parameter 넘길때 deep copy 필요.
//            int curMoppingRobotType1 = this.cleaningSoSConfiguration.getConfigurations().get("numMoppingRobotType1");
//            int curMoppingRobotType2 = this.cleaningSoSConfiguration.getConfigurations().get("numMoppingRobotType2");
//            int curSweepingRobotType1 = this.cleaningSoSConfiguration.getConfigurations().get("numSweepingRobotType1");
//            int curSweepingRobotType2 = this.cleaningSoSConfiguration.getConfigurations().get("numSweepingRobotType2");
//            if (curMoppingRobotType1 + MType1 > maxRobot) {
//                MType1 = maxRobot;
//            }
//            else if (curMoppingRobotType1 + MType1 < minRobot) {
//                MType1 = minRobot;
//            }
//            if (curMoppingRobotType2 + MType2 > maxRobot) {
//                MType2 = maxRobot;
//            }
//            else if (curMoppingRobotType2 + MType2 < minRobot) {
//                MType2 = minRobot;
//            }
//            if (curSweepingRobotType1 + SType1 > maxRobot) {
//                SType1 = maxRobot;
//            }
//            else if (curSweepingRobotType1 + SType1 < minRobot) {
//                SType1 = minRobot;
//            }
//            if (curSweepingRobotType2 + SType2 > maxRobot) {
//                SType2 = maxRobot;
//            }
//            else if (curSweepingRobotType2 + SType2 < minRobot) {
//                SType2 = minRobot;
//            }



            Strategy strategy = cleaningSoS.generateStrategy(MType1, MType2, SType1, SType2);
            Goals = Simulator.evaluateStrategy(cleaningSoS, strategy, this.simulationTime);

        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }

        assert Goals != null;

        int c1 = this.cleaningSoSConfiguration.getConfigurations().get("numMoppingRobotType1") + MType1;
        int c2 = this.cleaningSoSConfiguration.getConfigurations().get("numMoppingRobotType2") + MType2;
        int c3 = this.cleaningSoSConfiguration.getConfigurations().get("numSweepingRobotType1") + SType1;
        int c4 = this.cleaningSoSConfiguration.getConfigurations().get("numSweepingRobotType2") + SType2;

//        System.out.println(String.format("%d %d %d %d", cleaningSoSConfiguration.getConfigurations().get("numMoppingRobotType1"),
//                cleaningSoSConfiguration.getConfigurations().get("numMoppingRobotType2"),
//                cleaningSoSConfiguration.getConfigurations().get("numSweepingRobotType1"),
//                cleaningSoSConfiguration.getConfigurations().get("numSweepingRobotType2")));

        //System.out.println(String.format("%d %d %d %d", c1, c2, c3, c4));



        int maxNumMoppingRobotType1 = this.cleaningSoSConfiguration.getMaxConfigurations().get("numMoppingRobotType1");
        int maxNumMoppingRobotType2 = this.cleaningSoSConfiguration.getMaxConfigurations().get("numMoppingRobotType2");
        int maxNumSweepingRobotType1 = this.cleaningSoSConfiguration.getMaxConfigurations().get("numSweepingRobotType1");
        int maxNumSweepingRobotType2 = this.cleaningSoSConfiguration.getMaxConfigurations().get("numSweepingRobotType2");

        int minNumMoppingRobotType1 = this.cleaningSoSConfiguration.getMinConfigurations().get("numMoppingRobotType1");
        int minNumMoppingRobotType2 = this.cleaningSoSConfiguration.getMinConfigurations().get("numMoppingRobotType2");
        int minNumSweepingRobotType1 = this.cleaningSoSConfiguration.getMinConfigurations().get("numSweepingRobotType1");
        int minNumSweepingRobotType2 = this.cleaningSoSConfiguration.getMinConfigurations().get("numSweepingRobotType2");



        solution.setConstraint(0, (c1 >= minNumMoppingRobotType1 && c1 <= maxNumMoppingRobotType1) ? 0.0 : c1);
        solution.setConstraint(1, (c2 >= minNumMoppingRobotType2 && c2 <= maxNumMoppingRobotType2) ? 0.0 : c2);
        solution.setConstraint(2, (c3 >= minNumSweepingRobotType1 && c3 <= maxNumSweepingRobotType1) ? 0.0 : c3);
        solution.setConstraint(3, (c4 >= minNumSweepingRobotType2 && c4 <= maxNumSweepingRobotType2) ? 0.0 : c4);

        solution.setObjective(0, -Goals[0]); //maximize goal
        solution.setObjective(1, Goals[1]); //minimize goal
        solution.setObjective(2, Goals[2]); //minimize goal



//        solution.setConstraint(0, (c1 >= minNumMoppingRobotType1 && c1 <= maxNumMoppingRobotType1) ? 0.0 : c1);
//        solution.setConstraint(1, (c2 >= minNumMoppingRobotType2 && c2 <= maxNumMoppingRobotType2) ? 0.0 : c2);
//        solution.setConstraint(2, (c3 >= minNumSweepingRobotType1 && c3 <= maxNumSweepingRobotType1) ? 0.0 : c3);
//        solution.setConstraint(3, (c4 >= minNumSweepingRobotType2 && c4 <= maxNumSweepingRobotType2) ? 0.0 : c4);


    }



    @Override
    public Solution newSolution() {
        Solution solution = new Solution(getNumberOfVariables(), getNumberOfObjectives(), getNumberOfConstraints());

        solution.setVariable(0, EncodingUtils.newInt(-10, 10));
        solution.setVariable(1, EncodingUtils.newInt(-10, 10));
        solution.setVariable(2, EncodingUtils.newInt(-10, 10));
        solution.setVariable(3, EncodingUtils.newInt(-10, 10));

        return solution;
    }

}
