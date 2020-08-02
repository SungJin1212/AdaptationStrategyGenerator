package Model.AbstactClass;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

abstract public class SoS {

    public static ArrayList<CS> csList;
    public static Map<String, Environment> environmentList;

    public SoS() {
        this.csList = new ArrayList<> (0);
        environmentList = new HashMap<>(0);
    }

    protected void addCS(CS cs) {
        csList.add(cs);
    }

    public Map<String, Environment> getEnvironmentList() {
        return environmentList;
    }

    protected void removeCS(CS cs) {
        csList.remove(cs);
    }


    public ArrayList<CS> getCSList() {
        return csList;
    }



    public void run() {
        for(CS cs : csList) {
            cs.run();
        }
    }



//    protected Environment getEnvironment(int idx) {
//    Environment E = null;
//    for(Environment environment : environmentList) {
//        if(environment.getIdx() == idx) {
//            E = environment;
//            break;
//        }
//    }
//    return E;
}
