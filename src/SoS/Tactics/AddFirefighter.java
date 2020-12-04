package SoS.Tactics;

import Behavior.CS;
import SoS.Configuration;
import SoS.Strategy;
import SoS.Tactic;

import static SoS.MCIRSoS.*;


public class AddFirefighter extends Tactic implements Cloneable {

    private double remainTime;

    public AddFirefighter() {
        super(1, "AddFirefighter");
        remainTime = getLatency();
    }

    @Override
    public double run(Configuration configuration, Strategy strategy) throws CloneNotSupportedException {
        double ret = 0;

        if (isExecuted()) {
            return ret;
        }

        if(configuration.getConfigurations().get("numOfFirefighter") + 1 > configuration.getMaxConfigurations().get("numOfFirefighter")) {
            setExecuted(true);
            strategy.RemoveTactic(this);
            //savedPatient = -1000;
            return 0;

        }
        if (--remainTime == 0) {
            int currentNumOfFirefighter = configuration.getConfigurations().get("numOfFirefighter");
            csModelList.put(String.format("Firefighter_%s", currentNumOfFirefighter + 1), (CS) csSpecificationList.get("Firefighter").clone());
            configuration.getConfigurations().put("numOfFirefighter", configuration.getConfigurations().get("numOfFirefighter") + 1);

            setExecuted(true);
        }

        return 1;
    }
}
