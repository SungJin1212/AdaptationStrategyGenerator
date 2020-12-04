package Behavior;

import SoS.SoS;

abstract public class CS implements Cloneable {

    protected double cost;
    protected SoS sos;

    public CS(double cost) {
        this.cost = cost;
    }

    public abstract double run();


    public Object clone() throws CloneNotSupportedException {
        CS cs = (CS)super.clone();
        return cs;
    }
}
