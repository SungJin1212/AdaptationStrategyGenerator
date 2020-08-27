package Model.SoS;

public class CleaningSoSEnvironmentCondition extends EnvironmentCondition {

    public CleaningSoSEnvironmentCondition(int dustUnit) {
        super(1);

        getEnvironmentCondition().put("dustUnit", dustUnit);

    }
}
