import Model.AbstactClass.Behavior.Environment;
import Model.AbstactClass.Rule.Configuration;
import Model.AbstactClass.Rule.Strategy;
import Model.AbstactClass.Rule.Tactic;
import Model.GeneratedCode.Behavior.CleaningSoS;
import Model.GeneratedCode.Rule.EnvironmentCondition;
import Model.GeneratedCode.Rule.cleaningSoSConfiguration;
import org.moeaframework.Executor;
import org.moeaframework.core.NondominatedPopulation;
import org.moeaframework.core.Solution;
import org.moeaframework.core.variable.EncodingUtils;
import org.moeaframework.problem.AbstractProblem;

import static Model.AbstactClass.Behavior.SoS.tacticSpecificationList;

public class Test extends AbstractProblem {

    private EnvironmentCondition environmentCondition;
    private cleaningSoSConfiguration cleaningSoSConfiguration;

    public Test(EnvironmentCondition environmentCondition, cleaningSoSConfiguration cleaningSoSConfiguration) {
        super(4,3);
        this.environmentCondition = environmentCondition;
        this.cleaningSoSConfiguration = cleaningSoSConfiguration;
    }
    @Override
    public void evaluate(Solution solution) {
        int MType1 = EncodingUtils.getInt(solution.getVariable(0));
        int MType2 = EncodingUtils.getInt(solution.getVariable(1));
        int SType1 = EncodingUtils.getInt(solution.getVariable(2));
        int SType2 = EncodingUtils.getInt(solution.getVariable(3));
        double[] Goals = null;

        //EnvironmentCondition environmentCondition = new EnvironmentCondition(10);
        //cleaningSoSConfiguration configuration = new cleaningSoSConfiguration(5, 5, 5, 5);
        try {
            CleaningSoS cleaningSoS = new CleaningSoS(this.environmentCondition, this.cleaningSoSConfiguration);
            Strategy strategy = new Strategy();

            if(MType1 >= 0) {
                while(MType1-- != 0) {
                    strategy.AddTactic( (Tactic) tacticSpecificationList.get("AddMoppingRobotType1").clone());
                }
            }
            else {
                while(MType1++ != 0) {
                    strategy.AddTactic( (Tactic) tacticSpecificationList.get("RemoveMoppingRobotType1").clone());
                }
            }
            if(MType2 >= 0) {
                while(MType2-- != 0) {
                    strategy.AddTactic( (Tactic) tacticSpecificationList.get("AddMoppingRobotType2").clone());
                }
            }
            else {
                while(MType2++ != 0) {
                    strategy.AddTactic( (Tactic) tacticSpecificationList.get("RemoveMoppingRobotType2").clone());
                }
            }
            if(SType1 >= 0) {
                while(SType1-- != 0) {
                    strategy.AddTactic( (Tactic) tacticSpecificationList.get("AddSweepingRobotType1").clone());
                }
            }
            else {
                while(SType1++ != 0) {
                    strategy.AddTactic( (Tactic) tacticSpecificationList.get("RemoveSweepingRobotType1").clone());
                }
            }
            if(SType2 >= 0) {
                while(SType2-- != 0) {
                    strategy.AddTactic( (Tactic) tacticSpecificationList.get("AddSweepingRobotType2").clone());
                }
            }
            else {
                while(SType2++ != 0) {
                    strategy.AddTactic((Tactic) tacticSpecificationList.get("RemoveSweepingRobotType2").clone());
                }
            }

            Goals = cleaningSoS.getFitness(strategy);
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }

        assert Goals != null;
        solution.setObjective(0, -Goals[0]); //minimize
        solution.setObjective(1, Goals[1]); //minimize
        solution.setObjective(2, Goals[2]); //minimize

    }

    @Override
    public Solution newSolution() {

        Solution solution = new Solution(getNumberOfVariables(),
                getNumberOfObjectives());

        solution.setVariable(0, EncodingUtils.newInt(-5, 5));
        solution.setVariable(1, EncodingUtils.newInt(-5, 5));
        solution.setVariable(2, EncodingUtils.newInt(-5, 5));
        solution.setVariable(3, EncodingUtils.newInt(-5, 5));

        return solution;
    }

    public static void main(String[] args) {

        EnvironmentCondition environmentCondition = new EnvironmentCondition(10);
        cleaningSoSConfiguration cleaningSoSConfiguration = new cleaningSoSConfiguration(5,5,5,5);
        passingTest(environmentCondition,cleaningSoSConfiguration);
    }

    private static void passingTest(EnvironmentCondition environmentCondition, cleaningSoSConfiguration cleaningSoSConfiguration) {
        NondominatedPopulation result = new Executor()
                .withProblemClass(Test.class, environmentCondition, cleaningSoSConfiguration)
                .withProperty("populationSize", 40)
                .withAlgorithm("NSGA2")
                .withMaxEvaluations(30)
                .run();
        //display the results
        System.out.format("Objective1  Objective2   Objective3%n");

        for (Solution solution : result) { //all Pareto optimal solutions produced by the algorithm during the run.
            System.out.format("%.4f      %.4f      %.4f     %d      %d       %d       %d%n",
                    -solution.getObjective(0),
                    solution.getObjective(1),
                    solution.getObjective(2),

                    EncodingUtils.getInt(solution.getVariable(0)),
                    EncodingUtils.getInt(solution.getVariable(1)),
                    EncodingUtils.getInt(solution.getVariable(2)),
                    EncodingUtils.getInt(solution.getVariable(3))
            );
        }
    }

}
