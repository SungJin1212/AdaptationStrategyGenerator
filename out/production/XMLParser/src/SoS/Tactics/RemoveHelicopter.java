package SoS.Tactics;

import SoS.Configuration;
import SoS.Strategy;
import SoS.Tactic;

import static SoS.MCIRSoS.*;


public class RemoveHelicopter extends Tactic implements Cloneable {
    private double remainTime;


    public RemoveHelicopter() {
        super(1, "RemoveHelicopter");
        remainTime = getLatency();
    }

    @Override
    public double run(Configuration configuration, Strategy strategy) throws CloneNotSupportedException {
        double ret = 0;

        if (isExecuted()) {
            return ret;
        }

        if (configuration.getConfigurations().get("numOfHelicopter") - 1 < configuration.getMinConfigurations().get("numOfHelicopter")) {
            setExecuted(true);
            strategy.RemoveTactic(this);
            //savedPatient = -1000;
            return 0;

        }

        if(--remainTime == 0) {
            int currentNumOfHelicopter = configuration.getConfigurations().get("numOfHelicopter");
            csModelList.remove(String.format("Helicopter_%s", currentNumOfHelicopter));
            configuration.getConfigurations().put("numOfHelicopter", configuration.getConfigurations().get("numOfHelicopter") - 1);

            setExecuted(true);
        }

        return 1;
    }
}
