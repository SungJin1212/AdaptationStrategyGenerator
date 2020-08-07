package ExpectedOutput.MOP;

public class algorithm {
    private int numofVariables;
    private int numofGoals;
    private int numofConstranit;

    public algorithm(int numofVariables, int numofGoals, int numofConstranit) {
        this.numofVariables = numofVariables;
        this.numofGoals = numofGoals;
        this.numofConstranit = numofConstranit;
    }

    public int getNumofVariables() {
        return numofVariables;
    }

    public int getNumofGoals() {
        return numofGoals;
    }

    public int getNumofConstranit() {
        return numofConstranit;
    }

    public algorithm(int numofVariables, int numofGoals) {
        this.numofVariables = numofVariables;
        this.numofGoals = numofGoals;
    }
}
