package Behavior;

import SoS.MCIRSoS;
import SoS.Pair;

import static SoS.MCIRSoS.*;

public class GroundPatient extends Environment {

    private Status status;
    private int x, y;

    public Status getStatus() {
        return status;
    }
    private void setStatus(Status aStatus) {
        status = aStatus;
    }

    public GroundPatient(int y, int x) {
        setStatus(Status.Idle);
        this.y = y;
        this.x = x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void run() {
        Status aStatus = status;
        switch (aStatus) {
            case Idle:
                if (Math.random() < patientEmergingRate && curGroundPatient < maxGroundPatient) {
                    setStatus(Status.Emerge);
                    curGroundPatient++;
                    if (getStatus() == Status.Emerge) {
                        if (Math.random() < 0.5) {
                            GroundMap[y][x] = 1;
                            setStatus(Status.SeverityLevel1);

                        }
                        else {
                            GroundMap[y][x] = 2;
                            setStatus(Status.SeverityLevel2);
                        }
                    }
                }
            break;
        }
    }


    public void getTransferred() {
        if (getStatus() == Status.Rescued) {
            //System.out.println("GroundPatient Transferred");
            savedPatient++;
            //rescuedPatient--;
            setStatus(Status.Transferred);
            //System.out.println("GroundPatient Rescued");
        }
    }

    public void getRescued() {
        //System.out.println("GroundPatient Rescued");
        if (getStatus() == Status.SeverityLevel1 || getStatus() == Status.SeverityLevel2) {

            rescuedPatient++; // rescue Patient 1증가
            GQ.offer(new Pair(y, x));
            GroundMap[y][x] = 0;
            //System.out.println("GRescued");
            //System.out.println(GQ.size());
            //EnvironmentModelList.put(String.format("GroundPatient(%s,%s)",0,0),this);
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
