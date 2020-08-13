package StrategyGenerationEngine;

import Model.AbstactClass.Rule.Strategy;
import Model.AbstactClass.Rule.Tactic;
import Model.GeneratedCode.Behavior.CleaningSoS;
import Model.GeneratedCode.Rule.CleaningSoSConfiguration;
import Model.GeneratedCode.Rule.CleaningSoSEnvironmentCondition;
import StrategyGenerationEngine.Element.CaseBaseValue;
import StrategyGenerationEngine.Element.StrategyElement;
import org.moeaframework.Executor;
import org.moeaframework.core.NondominatedPopulation;
import org.moeaframework.core.Solution;
import org.moeaframework.core.variable.EncodingUtils;
import org.moeaframework.problem.AbstractProblem;

import java.util.ArrayList;

import static Model.AbstactClass.Behavior.SoS.tacticSpecificationList;

public class StrategyGenerationAtDesignTime extends AbstractProblem {

    private CleaningSoSEnvironmentCondition cleaningSoSEnvironmentCondition;
    private CleaningSoSConfiguration cleaningSoSConfiguration;

    public StrategyGenerationAtDesignTime(CleaningSoSEnvironmentCondition cleaningSoSEnvironmentCondition, CleaningSoSConfiguration cleaningSoSConfiguration) {
        super(4,3);
        this.cleaningSoSEnvironmentCondition = cleaningSoSEnvironmentCondition;
        this.cleaningSoSConfiguration = cleaningSoSConfiguration;
    }
    @Override
    public void evaluate(Solution solution) {
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

            Goals = cleaningSoS.getFitness(strategy, 50);

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


        solution.setVariable(0, EncodingUtils.newInt(-5, 5));
        solution.setVariable(1, EncodingUtils.newInt(-5, 5));
        solution.setVariable(2, EncodingUtils.newInt(-5, 5));
        solution.setVariable(3, EncodingUtils.newInt(-5, 5));

        return solution;
    }


    public static CaseBaseValue getCaseBaseValueAtDesignTime(CleaningSoSEnvironmentCondition cleaningSoSEnvironmentCondition, CleaningSoSConfiguration cleaningSoSConfiguration) {

        CaseBaseValue caseBaseValue = new CaseBaseValue(cleaningSoSEnvironmentCondition);
        ArrayList<StrategyElement> strategyElementList = new ArrayList<>(0);
        
//        InjectedInitialization injectedInitialization = new InjectedInitialization(StrategyGenerationAtDesignTime.class, 50, )


        NondominatedPopulation result = new Executor()
                .withProblemClass(StrategyGenerationAtDesignTime.class, cleaningSoSEnvironmentCondition, cleaningSoSConfiguration)
                .withProperty("populationSize", 50)
                .withProperty("withReplacement", true) // use binary tournament selection
                .withAlgorithm("NSGA2")
                .withMaxEvaluations(50)
                .distributeOnAllCores() // parallelization execution.
                .run();
//        new Plot()
//                .add("NSGA2", result)
//                .show();
        //display the results
        //System.out.format("Objective1  Objective2   Objective3%n");

        for (Solution solution : result) { //all Pareto optimal solutions produced by the algorithm during the run.
            StrategyElement strategyElement = new StrategyElement();

            strategyElement.getGoalValueList().add(-solution.getObjective(0));
            strategyElement.getGoalValueList().add(solution.getObjective(1));
            strategyElement.getGoalValueList().add(solution.getObjective(2));

            strategyElement.getStrategyValueList().add(EncodingUtils.getInt(solution.getVariable(0)));
            strategyElement.getStrategyValueList().add(EncodingUtils.getInt(solution.getVariable(1)));
            strategyElement.getStrategyValueList().add(EncodingUtils.getInt(solution.getVariable(2)));
            strategyElement.getStrategyValueList().add(EncodingUtils.getInt(solution.getVariable(3)));

            strategyElementList.add(strategyElement);
        //            System.out.format("%.4f      %.4f      %.4f     %d      %d       %d       %d%n",
//                    -solution.getObjective(0),
//                    solution.getObjective(1),
//                    solution.getObjective(2),
//                    EncodingUtils.getInt(solution.getVariable(0)),
//                    EncodingUtils.getInt(solution.getVariable(1)),
//                    EncodingUtils.getInt(solution.getVariable(2)),
//                    EncodingUtils.getInt(solution.getVariable(3))
//            );
        }
        caseBaseValue.setStrategyList(strategyElementList);
        return caseBaseValue;
    }

}
