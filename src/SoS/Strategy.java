package SoS;

import java.util.ArrayList;

public class Strategy implements Cloneable {
    private ArrayList<Tactic> strategy; //sequence of tactics

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



    public Object clone() throws CloneNotSupportedException {
        Strategy s =  (Strategy) super.clone();

        ArrayList <Tactic> arrayList1 = new ArrayList<>(0);

        for (Tactic t : strategy) {
            arrayList1.add((Tactic)t.clone());
        }
        s.strategy = arrayList1;
        return s;

    }

}
