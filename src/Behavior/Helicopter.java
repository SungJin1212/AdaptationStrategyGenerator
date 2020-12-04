package Behavior;

import SoS.MCIRSoS;
import SoS.Pair;

import static SoS.MCIRSoS.*;

public class Helicopter extends CS {
    private Status status;
    private int x;
    private int y;

    public Status getStatus() {
        return status;
    }
    private void setStatus(Status aStatus) {
        status = aStatus;
    }

    public Helicopter(double cost) {
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
                if(Math.random() < (1 - weatherCondition)) {
                    setStatus(Status.Check);
                    if(getStatus() == Status.Check) {
                        boolean isFindSeaPatient = searchSeaPatient();
                        if(isFindSeaPatient) {
                            setStatus(Status.Rescue);
                        }
                        else {
                            move();
                            setStatus(Status.Search);
                        }
                    }
                }
                break;
            case Rescue:
                rescueSeaPatient((SeaPatient) EnvironmentModelList.get(String.format("SeaPatient(%d,%d)",y,x)));
                setStatus(Status.Transfer);
                /*
                if (Math.random() < 1 - weatherCondition) { // 날씨가 좋으면 직접 transfer
                    setStatus(Status.Transfer);
                }
                else {
                    rescueSeaPatient((SeaPatient) EnvironmentModelList.get(String.format("SeaPatient(%d,%d)",y,x)));
                    setStatus(Status.Search);
                }
                */
                break;
            case Transfer:
                if (Math.random() < 1 - weatherCondition) { // 날씨가 좋으면 직접 transfer
                    if (SQ.size() >= 1) {
                        Pair p = SQ.poll();
                        transferToHospital((SeaPatient) EnvironmentModelList.get(String.format("SeaPatient(%d,%d)", p.first(), p.second())));
                        setStatus(Status.Search);
                    }
                }
                else {
                    setStatus(Status.Search);
                }
        }
        return cost;
    }

    private void transferToHospital(SeaPatient seaPatient) {
        seaPatient.getTransferred();
//        SeaMap[y][x] = 0;
//        //rescueSeaPatient((SeaPatient) EnvironmentModelList.get(String.format("SeaPatient(%d,%d)",y,x)));
//
//        savedPatient++;
    }

    private void rescueSeaPatient(SeaPatient seaPatient) {

        seaPatient.getRescued();
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

    private boolean searchSeaPatient() {
        //찾았으면 그자리로 이동
        //못찼았으면 랜덤 이동

        int[] dy = {0,1,1,1,0,-1,-1,-1};
        int[] dx = {1,1,0,-1,-1,-1,0,1};

        for (int dir = 0; dir < 8; dir++) {
            for(int s = 1; s <= Sight; s++) {
                int searchY = y + dy[dir] * s;
                int searchX = x + dx[dir] * s;

                if (searchX >= 1 && searchX <= MAPSIZE && searchY >= 1 && searchY <= MAPSIZE) {
                    if (SeaMap[searchY][searchX] == 2) {
                        x = searchX;
                        y = searchY;
                        return true;
                    }
                }
            }
        }

        for (int dir = 0; dir < 8; dir++) {
            for(int s = 1; s <= Sight; s++) {
                int searchY = y + dy[dir] * s;
                int searchX = x + dx[dir] * s;

                if (searchX >= 1 && searchX <= MAPSIZE && searchY >= 1 && searchY <= MAPSIZE) {
                    if (SeaMap[searchY][searchX] == 1) {
                        x = searchX;
                        y = searchY;
                        return true;
                    }
                }
            }
        }

        return false;
    }


    private void initPosition() {
        y = (int) ((Math.random() * ( MAPSIZE - 1 )) + 1); // y = [1, MAPSIZE]
        x = (int) ((Math.random() * ( MAPSIZE - 1 )) + 1); // x = [1, MAPSIZE]
    }

    public enum Status {
        Dispatch,
        Search,
        Check,
        Rescue,
        Transfer
    }
}
