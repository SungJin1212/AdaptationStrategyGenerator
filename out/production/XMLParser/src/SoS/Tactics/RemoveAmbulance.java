package SoS.Tactics;

import SoS.Configuration;
import SoS.Strategy;
import SoS.Tactic;

import static SoS.MCIRSoS.*;


public class RemoveAmbulance extends Tactic implements Cloneable {

    private double remainTime;

    public RemoveAmbulance() {
        super(1, "RemoveAmbulance");
        remainTime = getLatency();
    }

    @Override
    public double run(Configuration configuration, Strategy strategy) throws CloneNotSupportedException {
        double ret = 0;

        if (isExecuted()) {
            return ret;
        }

        if (configuration.getConfigurations().get("numOfAmbulance") - 1 < configuration.getMinConfigurations().get("numOfAmbulance")) {
            setExecuted(true);
            strategy.RemoveTactic(this);
            //savedPatient = -1000;
            return 0;
        }

        if(--remainTime == 0) {
            int currentNumOfAmbulance = configuration.getConfigurations().get("numOfAmbulance");
            csModelList.remove(String.format("Ambulance_%s", currentNumOfAmbulance));
            configuration.getConfigurations().put("numOfAmbulance", configuration.getConfigurations().get("numOfAmbulance") - 1);
            setExecuted(true);
        }

        return 1;
    }
}
