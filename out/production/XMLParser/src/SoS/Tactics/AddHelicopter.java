package SoS.Tactics;

import Behavior.CS;
import SoS.Configuration;
import SoS.Strategy;
import SoS.Tactic;

import static SoS.MCIRSoS.*;


public class AddHelicopter extends Tactic implements Cloneable {

    private double remainTime;

    public AddHelicopter() {
        super(1, "AddHelicopter");
        remainTime = getLatency();
    }

    @Override
    public double run(Configuration configuration, Strategy strategy) throws CloneNotSupportedException {
        double ret = 0;

        if (isExecuted()) {
            return ret;
        }

        if(configuration.getConfigurations().get("numOfHelicopter") + 1 > configuration.getMaxConfigurations().get("numOfHelicopter")) {
            setExecuted(true);
            strategy.RemoveTactic(this);
            //savedPatient = -1000;
            return 0;

        }

        if (--remainTime == 0) {

            int currentNumOfHelicopter = configuration.getConfigurations().get("numOfHelicopter");
            csModelList.put(String.format("Helicopter_%s", currentNumOfHelicopter + 1), (CS) csSpecificationList.get("Helicopter").clone());
            configuration.getConfigurations().put("numOfHelicopter", configuration.getConfigurations().get("numOfHelicopter") + 1);

            setExecuted(true);
        }

        return 1;

    }
}
