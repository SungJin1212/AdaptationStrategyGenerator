package Behavior;

import SoS.MCIRSoS;

import static SoS.MCIRSoS.*;

public class Firefighter extends CS {

    private Status status;
    private int x;
    private int y;

    public Status getStatus() {
        return status;
    }
    private void setStatus(Status aStatus) {
        status = aStatus;
    }

    public Firefighter(double cost) {
        super(cost);
        setStatus(Status.Dispatch);
    }

    @Override
    public double run() {
        Status aStatus = status;
        switch (aStatus) {
            case Dispatch:
                initPosition();
                setStatus(Status.Search);
                break;
            case Search:
                if (Math.random() < (1 - weatherCondition)) {
                    setStatus(Status.Check);
                    if (getStatus() == Status.Check) {
                        boolean isFindGroundPatient = searchGroundPatient();
                        if (isFindGroundPatient) {
                            setStatus(Status.Rescue);
                        } else {
                            move();
                            setStatus(Status.Search);
                        }
                    }
                }
                break;
            case Rescue:
                //rescueBuildingPatient((SeaPatient) EnvironmentModelList.get(String.format("SeaPatient(%d,%d)",y,x)));
                //System.out.println("Rescue");
                rescueGroundPatient((GroundPatient) EnvironmentModelList.get(String.format("GroundPatient(%d,%d)",y,x)));
                setStatus(Status.Search);
                break;
        }
        return cost;
    }

    private void rescueGroundPatient(GroundPatient groundPatient) {
        groundPatient.getRescued();
    }

    private boolean searchGroundPatient() {
        //찾았으면 그자리로 이동
        //못찼았으면 랜덤 이동

        int[] dy = {0,1,1,1,0,-1,-1,-1};
        int[] dx = {1,1,0,-1,-1,-1,0,1};

        for (int dir = 0; dir < 8; dir++) {
            for(int s = 1; s <= Sight; s++) { //sight
                int searchY = y + dy[dir] * s;
                int searchX = x + dx[dir] * s;


                if (searchX >= 1 && searchX <= MAPSIZE && searchY >= 1 && searchY <= MAPSIZE) {
                    if (GroundMap[searchY][searchX] == 2) {
                        x = searchX;
                        y = searchY;
                        return true;
                    }
                }
            }
        }
        for (int dir = 0; dir < 8; dir++) {
            for  (int s = 1; s <= Sight; s++) {
                int searchY = y + dy[dir] * s;
                int searchX = x + dx[dir] * s;

                if (searchX >= 1 && searchX <= MAPSIZE && searchY >= 1 && searchY <= MAPSIZE) {
                    if (GroundMap[searchY][searchX] == 1) {
                        x = searchX;
                        y = searchY;
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private void move() {
        int[] dy = {0,1,1,1,0,-1,-1,-1};
        int[] dx = {1,1,0,-1,-1,-1,0,1};

        int randomDir = (int)(Math.random() * 8);
        x = x + dx[randomDir];
        y = y + dy[randomDir];

        if (x > MAPSIZE) x = MAPSIZE;
        else if (x == 0) x = 1;
        if (y > MAPSIZE) y = MAPSIZE;
        else if (y == 0) y = 1;
    }

    private void initPosition() {
        x = (int) ((Math.random() * ( MAPSIZE - 1 )) + 1); // x = [1, MAPSIZE]
        y = (int) ((Math.random() * ( MAPSIZE - 1 )) + 1); // y = [1, MAPSIZE]
    }

    public enum Status {
        Dispatch,
        Check,
        Search,
        Rescue,
    }
}
