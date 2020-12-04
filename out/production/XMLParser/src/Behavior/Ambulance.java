package Behavior;

import SoS.Pair;

import java.util.LinkedList;
import java.util.Queue;

import static SoS.MCIRSoS.*;

public class Ambulance extends CS {

    private int transferTime;
    private Status status;
    private Queue<Pair> P_GQ;
    private Queue<Pair> P_SQ;

    public Status getStatus() {
        return status;
    }
    private void setStatus(Status aStatus) {
        status = aStatus;
    }

    public Ambulance(double cost) {
        super(cost);
        setStatus(Status.Waiting);
        P_GQ = new LinkedList<Pair>();
        P_SQ = new LinkedList<Pair>();

    }

    @Override
    public double run() {
        Status aStatus = status;
        switch (aStatus) {
            case Waiting:
                setStatus(Status.Check);
                if (getStatus() == Status.Check) {
                    int isExistPatient = checkRescuedPatient(); //1 = Ground, 2 = Sea, 0 = X
                    if (isExistPatient == 1) {
                        transferTime = (int) roadCondition; // set TransferTime as roadCondition
                        setStatus(Status.TransferG);
                    }
                    else if(isExistPatient == 2) {
                        transferTime = (int) roadCondition; // set TransferTime as roadCondition
                        setStatus(Status.TransferS);
                    }
                    else {
                        setStatus(Status.Waiting);
                    }
                }
                break;
            case TransferS:
                if(--transferTime == 0) {
                    transferToHospitalS();
                    setStatus(Status.Waiting);
                }
                break;
            case TransferG:
                if(--transferTime == 0) {
                    transferToHospitalG();
                    setStatus(Status.Waiting);
                }
                break;
        }
        return cost;
    }

    private void transferToHospitalS() {
        //System.out.println("transferToHospitalS");
        //System.out.println(P_SQ.size());

        if (P_SQ.size() >= AmbulanceNum) {
            for(int i=0; i<AmbulanceNum;i ++) {
                Pair p = P_SQ.poll();
                transferSeaPatient((SeaPatient) EnvironmentModelList.get(String.format("SeaPatient(%d,%d)",p.first(),p.second())));
            }
        }
        /*
        while(!P_SQ.isEmpty()) {
            //System.out.println("transferToHospitalS");
            Pair p = P_SQ.poll();
            transferSeaPatient((SeaPatient) EnvironmentModelList.get(String.format("SeaPatient(%d,%d)",p.first(),p.second())));
        }
        */
    }

    private void transferToHospitalG() {
        //System.out.println("transferToHospitalG");
        //System.out.println(P_GQ.size());
        if (P_GQ.size() >= AmbulanceNum) {
            for(int i=0; i<AmbulanceNum;i ++) {
                Pair p = P_GQ.poll();
                transferGroundPatient((GroundPatient) EnvironmentModelList.get(String.format("GroundPatient(%d,%d)",p.first(),p.second())));
            }
        }
        /*
        while(!P_GQ.isEmpty()) {
            //System.out.println("transferToHospitalG");

            Pair p = P_GQ.poll();
            transferGroundPatient((GroundPatient) EnvironmentModelList.get(String.format("GroundPatient(%d,%d)",p.first(),p.second())));
        }
        */

    }

    private void transferGroundPatient(GroundPatient groundPatient) {
        groundPatient.getTransferred();
    }

    private void transferSeaPatient(SeaPatient seaPatient) {
        seaPatient.getTransferred();
    }

    /*
    private void savePatient() {
        savedPatient+=AmbulanceNum;
    }*/

    private int checkRescuedPatient() {
        if (Math.random() < 0.5 ) {
            if (GQ.size() >= AmbulanceNum) {
                for(int i=1; i<=AmbulanceNum; i++) {
                    P_GQ.offer(GQ.poll());
                }
                return 1;
            }
        }
        else {
            if (SQ.size() >= AmbulanceNum) {
                for(int i=1; i<=AmbulanceNum; i++) {
                    P_SQ.offer(SQ.poll());
                }
                return 2;
            }
        }

        return 0;
    }


    public enum Status {
        Waiting,
        Check,
        TransferS,
        TransferG
    }
}
