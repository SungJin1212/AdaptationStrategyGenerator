package SoS;

public class MCIRSoSConfiguration extends Configuration implements Cloneable {
    public MCIRSoSConfiguration(int numOfFirefighter, int numOfHelicopter, int numOfAmbulance) {
        super(3);

        getConfigurations().put("numOfFirefighter", numOfFirefighter);
        getConfigurations().put("numOfHelicopter", numOfHelicopter);
        getConfigurations().put("numOfAmbulance", numOfAmbulance);

        getMaxConfigurations().put("numOfFirefighter", 100);
        getMaxConfigurations().put("numOfHelicopter", 100);
        getMaxConfigurations().put("numOfAmbulance", 100);

        getMinConfigurations().put("numOfFirefighter", 10);
        getMinConfigurations().put("numOfHelicopter", 10);
        getMinConfigurations().put("numOfAmbulance", 10);
    }
}
