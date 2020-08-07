package Model.AbstactClass.Rule;

abstract public class Tactic {
    private int cost; //실행하는데 소요되는 cost
    private int latency; //실행하는데 소요되는 시간
    private String name;

    public Tactic(int cost, int latency, String name) {
        this.cost = cost;
        this.latency = latency;
        this.name = name;
    }

    protected int getLatency() {
        return latency;
    }

    public void setLatency(int latency) {
        this.latency = latency;
    }

    abstract public void run(Configuration configuration) throws CloneNotSupportedException;

    protected int getCost() {
        return cost;
    }
}
