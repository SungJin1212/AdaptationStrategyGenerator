package SoS;

import Behavior.CS;
import Behavior.Environment;

import java.util.HashMap;
import java.util.Map;

abstract public class SoS {

    abstract void initSystemSpecification();
    abstract void initTacticSpecification();
    abstract void initCS(Configuration configuration) throws CloneNotSupportedException;
    abstract void initEnvironments();

    private Configuration configuration;
    private EnvironmentCondition environmentCondition;



    public SoS(EnvironmentCondition environmentCondition, Configuration configuration) {

        this.environmentCondition = environmentCondition;
        this.configuration = configuration;
    }

    public EnvironmentCondition getSoSEnvironmentCondition() {
        return environmentCondition;
    }

    public Configuration getSoSConfiguration() {
        return configuration;
    }


    public void setSoSConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    public void setSoSEnvironmentCondition(EnvironmentCondition environmentCondition) {
        this.environmentCondition = environmentCondition;
    }
}
