package CleaningSoSCaseStudy.Model.SoS;

import Model.SoS.EnvironmentCondition;

public class CleaningSoSEnvironmentCondition extends EnvironmentCondition {

    public CleaningSoSEnvironmentCondition(int dustUnit) {
        super(1);

        getEnvironmentCondition().put("dustUnit", dustUnit);

    }
}
