package SoS;

abstract public class Tactic implements Cloneable {
    private double latency;
    private String name;
    private boolean isExecuted;

    public Tactic(double latency, String name) {
        this.latency = latency;
        this.name = name;
        isExecuted = false;
    }

    public boolean isExecuted() {
        return isExecuted;
    }

    public void setExecuted(boolean executed) {
        isExecuted = executed;
    }

    protected double getLatency() {
        return latency;
    }

    public void setLatency(double latency) {
        this.latency = latency;
    }

    abstract public double run(Configuration configuration, Strategy strategy) throws CloneNotSupportedException;

    public Object clone() throws CloneNotSupportedException {
        Tactic tactic = (Tactic)super.clone();
        return tactic;
    }
}
