package Simulator;

import Model.AbstactClass.SoS;

public class Simulator {

    private SoS sos;

    public Simulator(SoS sos) {
        this.sos = sos;
    }


    public String StartSimulation() {

        for(int t=1; t<=50; t++) {
            sos.run();
        }

        return sos.getFitness();
    }
}
