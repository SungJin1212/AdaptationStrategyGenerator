package ExpectedOutput.MOP;

import java.util.ArrayList;

public class Population {

    private ArrayList<cleaningSoSConfiguration> population;

    Population() {
        this.population = new ArrayList<>(0);
    }

    public void AddPopulation(cleaningSoSConfiguration configuration) {
        this.population.add(configuration);
    }
}
