package SoS.Tactics;

import SoS.Configuration;
import SoS.Strategy;
import SoS.Tactic;

import static SoS.MCIRSoS.*;


public class RemoveFirefighter extends Tactic implements Cloneable {

    private double remainTime;

    public RemoveFirefighter() {
        super(1, "RemoveFirefighter");
        remainTime = getLatency();
    }

    @Override
    public double run(Configuration configuration, Strategy strategy) throws CloneNotSupportedException {
        double ret = 0;

        if (isExecuted()) {
            return ret;
        }

        if (configuration.getConfigurations().get("numOfFirefighter") - 1 < configuration.getMinConfigurations().get("numOfFirefighter")) {
            setExecuted(true);
            strategy.RemoveTactic(this);
            //savedPatient = -1000;
            return 0;

        }

        if(--remainTime == 0) {
            int currentNumOfFirefighter = configuration.getConfigurations().get("numOfFirefighter");
            csModelList.remove(String.format("Firefighter_%s", currentNumOfFirefighter));
            configuration.getConfigurations().put("numOfFirefighter", configuration.getConfigurations().get("numOfFirefighter") - 1);

            setExecuted(true);
        }

        return 1;
    }
}
