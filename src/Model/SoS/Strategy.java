package Model.SoS;

import java.util.ArrayList;

public class Strategy {

    private ArrayList <Tactic> strategy; //sequence of tactics

    public Strategy() {
        strategy = new ArrayList<>(0);
    }

    public void AddTactic(Tactic tactic) {
        strategy.add(tactic);
    }
    public void RemoveTactic(Tactic tactic) {
        strategy.remove(tactic);
    }

    public ArrayList<Tactic> getStrategy() {
        return strategy;
    }

//    public double[] run(Configuration configuration, Strategy curStrategy) throws CloneNotSupportedException {
//
//        Tactic curTactic = null;
//
//        for(Tactic t : curStrategy.generateStrategy()) {
//            if (!t.isExecuted()) {
//                curTactic = t;
//                break;
//            }
//        }
//        assert curTactic != null;
//        return curTactic.run(configuration);
//    }
}
