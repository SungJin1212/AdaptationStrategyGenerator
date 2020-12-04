package Behavior;

import SoS.Pair;

import static SoS.MCIRSoS.*;

public class SeaPatient extends Environment {

    private Status status;
    private int y, x;


    public Status getStatus() {
        return status;
    }
    private void setStatus(Status aStatus) {
        status = aStatus;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public SeaPatient(int y, int x) {
        setStatus(Status.Idle);
        this.y = y;
        this.x = x;
    }

    public void run() {
        Status aStatus = status;
        switch (aStatus) {
            case Idle:
                if (Math.random() < patientEmergingRate && curSeaPatient < maxSeaPatient) {
                    setStatus(Status.Emerge);
                    curSeaPatient++;
                    if (getStatus() == Status.Emerge) {
                        //System.out.println("Status.Emerge");
                        if (Math.random() < 0.5) {
                            SeaMap[y][x] = 1;
                            setStatus(Status.SeverityLevel1);
                        }
                        else {
                            SeaMap[y][x] = 2;
                            setStatus(Status.SeverityLevel2);
                        }
                    }
                }
            break;
        }
    }

    public void getTransferred() {
        if (getStatus() == Status.Rescued) {
            //System.out.println("SeaPatient Transferred");
            savedPatient++;
            //rescuedPatient--;
            setStatus(Status.Transferred);
        }
    }

    public void getRescued() {
        if (getStatus() == SeaPatient.Status.SeverityLevel1 || getStatus() == SeaPatient.Status.SeverityLevel2) {

            //System.out.println("SeaPatient Rescued");

            rescuedPatient++; // rescue Patient 1증가
            SQ.offer(new Pair(y, x));
            //System.out.println("seaPatient Rescued");
            SeaMap[y][x] = 0;
            setStatus(Status.Rescued);
        }
    }

    public enum Status {
        Idle,
        Emerge,
        SeverityLevel1,
        SeverityLevel2,
        Rescued,
        Transferred
    }
}
