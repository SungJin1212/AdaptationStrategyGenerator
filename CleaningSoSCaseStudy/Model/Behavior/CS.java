package CleaningSoSCaseStudy.Model.Behavior;

abstract public class CS implements Cloneable {

    public abstract void run();

    public Object clone() throws CloneNotSupportedException {
        CS cs = (CS)super.clone();
        return cs;
    }
}


