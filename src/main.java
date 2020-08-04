import Model.AbstactClass.SoS;
import Model.GeneratedCode.Rule.Configuration;
import Model.GeneratedCode.Rule.EnvironmentCondition;
import Model.GeneratedCode.SoS.cleaningSoS;
import Model.RuleBase.RuleBase;
import Simulator.Simulator;

public class main {
    public static void main(String[] args) {

        RuleBase ruleBase = new RuleBase();
        RuleBaseConstructionAtDesignTime();
        RuleBaseEvolutionAtRunTime();
    }

    private static void RuleBaseConstructionAtDesignTime() {

        int populationSize = 100;
        double crossoverRate = 0.95;
        double mutationRate = 0.05;

        EnvironmentCondition environmentCondition = new EnvironmentCondition(1000,30);


//        Configuration configuration = new Configuration(getRandomInt(1,10),getRandomInt(1,10),getRandomInt(1,10),getRandomInt(1,10),getRandomInt(1,10),getRandomInt(1,10));
//        SoS cleaningSoS = new cleaningSoS(configuration);
//        Simulator simulator = new Simulator(cleaningSoS);
//
//
//        System.out.println(simulator.StartSimulation());

//        Configuration configuration = new Configuration(1,1,1,1,1,1);
//        SoS cleaningSoS = new cleaningSoS(configuration);
//        Simulator simulator = new Simulator(cleaningSoS);
//        System.out.println(simulator.StartSimulation());

        for(int i=1; i<=populationSize; i++) {
            Configuration configuration = new Configuration(getRandomInt(1,200), getRandomInt(1,200), getRandomInt(1,200), getRandomInt(1,200), getRandomInt(1,200), getRandomInt(1,200));
            SoS cleaningSoS = new cleaningSoS(configuration);
            Simulator simulator = new Simulator(cleaningSoS);
            System.out.println(simulator.StartSimulation());
        }


    }

    private static void RuleBaseEvolutionAtRunTime() {
    }

    private static int getRandomInt(int min, int max) {
        return min + (int)(Math.random()*(max-min));
    }
}
