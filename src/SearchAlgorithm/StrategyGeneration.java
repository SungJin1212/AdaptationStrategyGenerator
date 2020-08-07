package SearchAlgorithm;

import Model.AbstactClass.Behavior.SoS;
import Model.AbstactClass.Rule.Strategy;
import Model.AbstactClass.Rule.Tactic;
import java.util.Random;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StrategyGeneration {

    public Strategy RandomStrategyGeneration(int maxLength) {
        Strategy randomStrategy = new Strategy();

        int strategyLength = (int) ((Math.random() * (maxLength)) + 1);

        Map<String, Tactic> tacticList = SoS.getTacticSpecificationList();
        List<String> keys = new ArrayList<>(tacticList.keySet());
        Random r = new Random();

        for(int i=0; i<strategyLength; i++) {
            Tactic randomTactic = tacticList.get(keys.get(r.nextInt(keys.size())));
            randomStrategy.AddTactic(randomTactic);
        }

        return randomStrategy;
    }
}
