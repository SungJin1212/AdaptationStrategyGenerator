package ExpectedOutput.MOP;

import Model.AbstactClass.SoS;
import Model.GeneratedCode.Behavior.CleaningSoS;
import Simulator.Simulator;
import org.moeaframework.Executor;
import org.moeaframework.core.NondominatedPopulation;
import org.moeaframework.core.Solution;
import org.moeaframework.core.variable.EncodingUtils;
import org.moeaframework.problem.AbstractProblem;

public class cleaningSoSProblem extends AbstractProblem {

    public cleaningSoSProblem() {
        super(6, 2);
    }

    @Override
    public void evaluate(Solution solution) {
        int NumOfSweepingRobotType1 = EncodingUtils.getInt(solution.getVariable(0));
        int NumOfSweepingRobotType2 = EncodingUtils.getInt(solution.getVariable(1));
        int NumOfSweepingRobotType3 = EncodingUtils.getInt(solution.getVariable(2));

        int NumOfMoppingRobotType1 = EncodingUtils.getInt(solution.getVariable(3));
        int NumOfMoppingRobotType2 = EncodingUtils.getInt(solution.getVariable(4));
        int NumOfMoppingRobotType3 = EncodingUtils.getInt(solution.getVariable(5));

        cleaningSoSConfiguration configuration = new cleaningSoSConfiguration(NumOfSweepingRobotType1,NumOfSweepingRobotType2,NumOfSweepingRobotType3,NumOfMoppingRobotType1,NumOfMoppingRobotType2,NumOfMoppingRobotType3);
        SoS cleaningSoS = new CleaningSoS(configuration);
        Simulator simulator = new Simulator(cleaningSoS);



        double[] Goals = simulator.StartSimulation(); //cost, moppedTile

        solution.setObjective(0, Goals[0]); //minimize
        solution.setObjective(1, -Goals[1]); //maximize
    }

    @Override
    public Solution newSolution() {
        Solution solution = new Solution(getNumberOfVariables(),getNumberOfObjectives());

        solution.setVariable(0, EncodingUtils.newInt(0, 10));
        solution.setVariable(1, EncodingUtils.newInt(0, 10));
        solution.setVariable(2, EncodingUtils.newInt(0, 10));
        solution.setVariable(3, EncodingUtils.newInt(0, 10));
        solution.setVariable(4, EncodingUtils.newInt(0, 10));
        solution.setVariable(5, EncodingUtils.newInt(0, 10));




        return solution;
    }

    public static void main(String[] args) {
        NondominatedPopulation result = new Executor()
                .withProblemClass(cleaningSoSProblem.class)
                .withProperty("populationSize", 50)
                .withAlgorithm("NSGA2")
                .withMaxEvaluations(10000)
                .run();
        //display the results
        System.out.format("Objective1  Objective2%n");

        for (Solution solution : result) { //all Pareto optimal solutions produced by the algorithm during the run.
            System.out.format("%.4f      %.4f      %d      %d       %d       %d      %d      %d%n",
                    solution.getObjective(0),
                    -solution.getObjective(1),
                    EncodingUtils.getInt(solution.getVariable(0)),
                    EncodingUtils.getInt(solution.getVariable(1)),
                    EncodingUtils.getInt(solution.getVariable(2)),
                    EncodingUtils.getInt(solution.getVariable(3)),
                    EncodingUtils.getInt(solution.getVariable(4)),
                    EncodingUtils.getInt(solution.getVariable(5))
                    );
        }
    }
}
