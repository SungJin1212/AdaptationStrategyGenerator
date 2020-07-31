package strc;

import java.util.ArrayList;

abstract public class SoS {

    private ArrayList<CS> CSList;

    protected SoS(ArrayList<CS> csList) {
        CSList = csList;
    }

    public void run() {

        for(CS cs : CSList) {
            cs.run();
        }
    }

}
