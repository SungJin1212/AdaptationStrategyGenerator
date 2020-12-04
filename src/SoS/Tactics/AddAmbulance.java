package SoS.Tactics;

import Behavior.CS;
import SoS.Configuration;
import SoS.Strategy;
import SoS.Tactic;

import static SoS.MCIRSoS.*;


public class AddAmbulance extends Tactic implements Cloneable {
    private double remainTime;

    public AddAmbulance() {
        super(1, "AddAmbulance");
        remainTime = getLatency();
    }

    @Override
    public double run(Configuration configuration, Strategy strategy) throws CloneNotSupportedException {
        double ret = 0;

        if (isExecuted()) {
            return ret;
        }

        if(configuration.getConfigurations().get("numOfAmbulance") + 1 > configuration.getMaxConfigurations().get("numOfAmbulance")) {
            setExecuted(true);
            strategy.RemoveTactic(this);
            //savedPatient = -1000;
            return 0;

        }

        if (--remainTime == 0) {
            int currentNumOfAmbulance = configuration.getConfigurations().get("numOfAmbulance");
            csModelList.put(String.format("Ambulance_%s", currentNumOfAmbulance + 1), (CS) csSpecificationList.get("Ambulance").clone());
            configuration.getConfigurations().put("numOfAmbulance", configuration.getConfigurations().get("numOfAmbulance") + 1);

            setExecuted(true);
        }

        return 1;
    }
}
