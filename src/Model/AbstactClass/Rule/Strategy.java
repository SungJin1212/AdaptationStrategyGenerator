package Model.AbstactClass.Rule;

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

    public void run(Configuration configuration) throws CloneNotSupportedException {
        for(Tactic t : strategy) {
            t.run(configuration);
        }
    }
}
