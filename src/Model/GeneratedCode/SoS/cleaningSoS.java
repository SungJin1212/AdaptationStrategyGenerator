package Model.GeneratedCode.SoS;

import Model.AbstactClass.SoS;

public class cleaningSoS extends SoS {

    static int dust = 3;

    public cleaningSoS() {
        super();
        init();
    }

    private void init() {
        AddCSs();
        AddEnvironments();
    }

    private void AddCSs() {
        csList.put("MoppingRobot1", new MoppingRobot());
        csList.put("SweepingRobot1", new SweepingRobot());
    }

    private void AddEnvironments() {
        environmentList.put("Tile(1,1)", new Tile(1,1));
        environmentList.put("Tile(1,2)", new Tile(1,2));
        environmentList.put("Tile(2,1)", new Tile(2,1));
        environmentList.put("Tile(2,2)", new Tile(2,2));
    }






}
