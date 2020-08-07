package ExpectedOutput.MOP.temp.Simulator;


import Model.AbstactClass.Behavior.SoS;

public class Simulator {

    private SoS sos;

    public Simulator(SoS sos) {
        this.sos = sos;
    }


    public double[] StartSimulation() {

        for(int t=1; t<=50; t++) {
            sos.run();
        }

        return sos.getFitness();
    }
}
