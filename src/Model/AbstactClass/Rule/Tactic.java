package Model.AbstactClass.Rule;

abstract public class Tactic implements Cloneable {
    private double cost; //실행하는데 소요되는 cost
    private double latency; //실행하는데 소요되는 시간
    private String name;
    private boolean isExecuted;

    public Tactic(double cost, double latency, String name) {
        this.cost = cost;
        this.latency = latency;
        this.name = name;
        isExecuted = false;
    }

    public void setExecuted(boolean executed) {
        isExecuted = executed;
    }

    protected double getLatency() {
        return latency;
    }

    public void setLatency(int latency) {
        this.latency = latency;
    }

    abstract public double[] run(Configuration configuration) throws CloneNotSupportedException;

    protected double getCost() {
        return cost;
    }

    public boolean isExecuted() {
        return isExecuted;
    }

    public Object clone() throws CloneNotSupportedException {
        Tactic tactic = (Tactic)super.clone();
        return tactic;
    }
}
