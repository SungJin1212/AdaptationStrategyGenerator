package SoS;

public class MCIRSoSEnvironmentCondition extends EnvironmentCondition {
    public MCIRSoSEnvironmentCondition(double weatherCondition, double roadCondition) {
        super(2);

        getEnvironmentCondition().put("weatherCondition", weatherCondition);
        getEnvironmentCondition().put("roadCondition", roadCondition);
    }
}
