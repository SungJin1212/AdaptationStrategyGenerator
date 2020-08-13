package Model.GeneratedCode.Rule;

import Model.AbstactClass.Rule.EnvironmentCondition;

public class CleaningSoSEnvironmentCondition extends EnvironmentCondition {

    public CleaningSoSEnvironmentCondition(int dustUnit) {
        super(1);

        getEnvironmentCondition().put("dustUnit", dustUnit);

    }
}
