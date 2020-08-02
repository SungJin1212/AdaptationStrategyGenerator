package Simulator;

import Model.AbstactClass.SoS;

public class Simulator {

    private SoS sos;

    public Simulator(SoS sos) {
        this.sos = sos;
    }


    public void StartSimulation() {

        for(int t=0; t<=100; t++) {
            sos.run();
        }
    }
}
