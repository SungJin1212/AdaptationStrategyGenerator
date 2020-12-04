package CleaningSoSCaseStudy;

import org.moeaframework.Executor;
import org.moeaframework.Instrumenter;
import org.moeaframework.algorithm.NSGAII;
import org.moeaframework.analysis.plot.Plot;
import org.moeaframework.core.*;
import org.moeaframework.core.comparator.ChainedComparator;
import org.moeaframework.core.comparator.CrowdingComparator;
import org.moeaframework.core.comparator.ParetoDominanceComparator;
import org.moeaframework.core.operator.*;
import org.moeaframework.core.operator.real.PM;
import org.moeaframework.core.variable.EncodingUtils;
import org.moeaframework.core.variable.RealVariable;
import org.moeaframework.problem.AbstractProblem;


public class Example1 {

    /**
     * Implementation of the DTLZ2 function.
     */
    public static class MyDTLZ2 extends AbstractProblem {

        /**
         * Constructs a new instance of the DTLZ2 function, defining it
         * to include 11 decision variables and 2 objectives.
         */
        public MyDTLZ2() {
            super(11, 2);
        }

        /**
         * Constructs a new solution and defines the bounds of the decision
         * variables.
         */
        @Override
        public Solution newSolution() {
            Solution solution = new Solution(getNumberOfVariables(),
                    getNumberOfObjectives());

            for (int i = 0; i < getNumberOfVariables(); i++) {
                solution.setVariable(i, new RealVariable(0.0, 1.0));
            }

            return solution;
        }

        /**
         * Extracts the decision variables from the solution, evaluates the
         * Rosenbrock function, and saves the resulting objective value back to
         * the solution.
         */
        @Override
        public void evaluate(Solution solution) {
            double[] x = EncodingUtils.getReal(solution);
            double[] f = new double[numberOfObjectives];

            int k = numberOfVariables - numberOfObjectives + 1;

            double g = 0.0;
            for (int i = numberOfVariables - k; i < numberOfVariables; i++) {
                g += Math.pow(x[i] - 0.5, 2.0);
            }

            for (int i = 0; i < numberOfObjectives; i++) {
                f[i] = 1.0 + g;

                for (int j = 0; j < numberOfObjectives - i - 1; j++) {
                    f[i] *= Math.cos(0.5 * Math.PI * x[j]);
                }

                if (i != 0) {
                    f[i] *= Math.sin(0.5 * Math.PI * x[numberOfObjectives - i - 1]);
                }
            }

            solution.setObjectives(f);
        }

    }

    public static void main(String[] args) {

        MyDTLZ2 myDTLZ2 = new MyDTLZ2();

        RandomInitialization randomInitialization = new RandomInitialization(myDTLZ2, 100); // Any remaining slots in the population will be filled with randomly-generated solutions.

        TournamentSelection selection = new TournamentSelection(2, new ChainedComparator( new ParetoDominanceComparator(), new CrowdingComparator()));

        Variation variation = new GAVariation( new OnePointCrossover(1.0), new PM(1.0/ myDTLZ2.getNumberOfVariables(), 30.0));

        Algorithm algorithm = new NSGAII(myDTLZ2, new NondominatedSortingPopulation(), null, selection, variation, randomInitialization);

        while (algorithm.getNumberOfEvaluations() < 300) {
            algorithm.step();
            NondominatedPopulation result = algorithm.getResult();
            System.out.println(result.size());
            new Plot()
                .add("NSGAII", result)
                .show();
        }
        NondominatedPopulation result = algorithm.getResult();

        //configure and run the DTLZ2 function
//        NondominatedPopulation result = new Executor()
//                .withProblemClass(MyDTLZ2.class)
//                .withAlgorithm("NSGAII")
//                .withMaxEvaluations(1000)
//                .run();

        //display the results
        System.out.println(result.size());

        System.out.format("Objective1  Objective2%n");

//        new Plot()
//                .add("NSGAII", result)
//                .show();
    }

}