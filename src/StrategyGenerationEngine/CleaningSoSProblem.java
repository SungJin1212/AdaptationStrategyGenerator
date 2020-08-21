package StrategyGenerationEngine;

import Model.AbstactClass.Rule.Strategy;
import Model.GeneratedCode.Behavior.CleaningSoS;
import Model.GeneratedCode.Rule.CleaningSoSConfiguration;
import Model.GeneratedCode.Rule.CleaningSoSEnvironmentCondition;
import org.moeaframework.core.Solution;
import org.moeaframework.core.variable.EncodingUtils;
import org.moeaframework.problem.AbstractProblem;

import static Simulator.SimulationEngine.getFitness;

public class CleaningSoSProblem extends AbstractProblem {

    private CleaningSoSEnvironmentCondition cleaningSoSEnvironmentCondition;
    private CleaningSoSConfiguration cleaningSoSConfiguration;


    public CleaningSoSProblem(CleaningSoSEnvironmentCondition cleaningSoSEnvironmentCondition, CleaningSoSConfiguration cleaningSoSConfiguration) {
        super(4,3);
        this.cleaningSoSEnvironmentCondition = cleaningSoSEnvironmentCondition;
        this.cleaningSoSConfiguration = cleaningSoSConfiguration;
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
            int curMoppingRobotType1 = this.cleaningSoSConfiguration.getConfiguration().get("numMoppingRobotType1");
            int curMoppingRobotType2 = this.cleaningSoSConfiguration.getConfiguration().get("numMoppingRobotType2");
            int curSweepingRobotType1 = this.cleaningSoSConfiguration.getConfiguration().get("numSweepingRobotType1");
            int curSweepingRobotType2 = this.cleaningSoSConfiguration.getConfiguration().get("numSweepingRobotType2");

            if (curMoppingRobotType1 + MType1 > maxRobot) {
                MType1 = maxRobot;
            }
            else if (curMoppingRobotType1 + MType1 < minRobot) {
                MType1 = minRobot;
            }
            if (curMoppingRobotType2 + MType2 > maxRobot) {
                MType2 = maxRobot;
            }
            else if (curMoppingRobotType2 + MType2 < minRobot) {
                MType2 = minRobot;
            }
            if (curSweepingRobotType1 + SType1 > maxRobot) {
                SType1 = maxRobot;
            }
            else if (curSweepingRobotType1 + SType1 < minRobot) {
                SType1 = minRobot;
            }
            if (curSweepingRobotType2 + SType2 > maxRobot) {
                SType2 = maxRobot;
            }
            else if (curSweepingRobotType2 + SType2 < minRobot) {
                SType2 = minRobot;
            }

            Strategy strategy = cleaningSoS.getStrategy(MType1, MType2, SType1, SType2);
            Goals = getFitness(cleaningSoS, strategy, 100);
            //Goals = cleaningSoS.getFitness(strategy, 50);

        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }

        assert Goals != null;
        solution.setObjective(0, -Goals[0]); //maximize goal
        solution.setObjective(1, Goals[1]); //minimize goal
        solution.setObjective(2, Goals[2]); //minimize goal
    }



    @Override
    public Solution newSolution() {
        Solution solution = new Solution(getNumberOfVariables(),
                getNumberOfObjectives());
        solution.setVariable(0, EncodingUtils.newInt(-10, 10));
        solution.setVariable(1, EncodingUtils.newInt(-10, 10));
        solution.setVariable(2, EncodingUtils.newInt(-10, 10));
        solution.setVariable(3, EncodingUtils.newInt(-10, 10));
        return solution;
    }
}
