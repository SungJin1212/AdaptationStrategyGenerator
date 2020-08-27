package Model.Behavior;

import static Model.SoS.CleaningSoS.*;
import static Model.SoS.SoS.EnvironmentModelList;

/**
 * Generated State Machine Code From: MoppingRobot.xml;
 */
public class MoppingRobot extends CS implements Cloneable {
  private int x;

  private int y;

  private Status status;

  private boolean canMopping;
  private boolean isMopFinish;


  private int performance;

  private int MoppingTime;

  public MoppingRobot(int performance) {
    setStatus(Status.Dispatch);
    this.performance = performance;
    this.MoppingTime = 2;
  }

  public Status getStatus() {
    return status;
  }

  private void setStatus(Status aStatus) {
    status = aStatus;
  }

  public boolean tryMopping() {
    boolean wasEventProcessed = false;
    Status aStatus = status;
    switch(aStatus) {
      case Checking: {
        setStatus(Status.Mopping);
        wasEventProcessed = true;
        break;
      }
    }
    return wasEventProcessed;
  }

  public boolean initPositionandCheck() {
    boolean wasEventProcessed = false;
    Status aStatus = status;
    switch(aStatus) {
      case Dispatch: {
        initPosition();
        checkCanMopping();
        setStatus(Status.Checking);
        wasEventProcessed = true;
        break;
      }
    }
    return wasEventProcessed;
  }

  public boolean doMove() {
    boolean wasEventProcessed = false;
    Status aStatus = status;
    switch(aStatus) {
      case Checking: {
        move();
        checkCanMopping();
        setStatus(Status.Checking);
        wasEventProcessed = true;
        break;
      }
    }
    return wasEventProcessed;
  }

  public boolean MoppingNow() {
    boolean wasEventProcessed = false;
    Status aStatus = status;
    switch(aStatus) {
      case Mopping: {
        mopping();
        setStatus(Status.Mopping);
        wasEventProcessed = true;
        break;
      }
    }
    return wasEventProcessed;
  }

  public boolean Mop(Tile T) {


    boolean wasEventProcessed = false;
    Status aStatus = status;
    switch(aStatus) {
      case Mopping: {
        T.Mop();
        checkCanMopping();
        setStatus(Status.Checking);
        wasEventProcessed = true;
        break;
      }
    }
    return wasEventProcessed;
  }

  private void initPosition() {
    x = (int) ((Math.random() * ( MapSize - 1 )) + 1);
    y = (int) ((Math.random() * ( MapSize - 1 )) + 1);
  }

  private void move() {
    int[] dx = {1,1,0,-1,-1,-1,0,1};
    int[] dy = {0,1,1,1,0,-1,-1,-1};

    for(int dir = 0; dir < 8; dir++) {
      int searchX = x + dx[dir];
      int searchY = y + dy[dir];
      if (searchX >=1 && searchX <= MapSize && searchY >=1 && searchY <= MapSize) {
        if (tileMap[searchY][searchX] < MopDust) {
          x = searchX;
          y = searchY;
          return;
        }
      }
    }

    int dir = (int)(Math.random() * 8);
    x = (x + dx[dir]);
    y = (y + dy[dir]);

    if (x > MapSize) x -= MapSize;
    else if (x == 0) x = 1;
    if (y > MapSize) y -= MapSize;
    else if (y == 0) y = 1;
  }

  private void checkCanMopping() {
    if(tileMap[y][x] <= MopDust) {
      canMopping = true;
    }
    else {
      canMopping = false;
    }
    isMopFinish = false;
  }

  private void mopping() {
    if (tileMap[x][y] - performance >= 0) {
      tileMap[x][y] -= performance;
    }
    else {
      tileMap[x][y] = 0;
    }

    if (tileMap[x][y] == 0) {
      isMopFinish = true;
    }
  }

  public void run() {
    Status aStatus = status;
    switch(aStatus) {
      case Dispatch: {
        initPositionandCheck();
        break;
      }
      case Mopping: {
        if(--MoppingTime == 0) {
          MoppingTime = 2;
          if (isMopFinish == false) {
            MoppingNow();
          }
          if (isMopFinish == true) {
            Mop((Tile)EnvironmentModelList.get(String.format("Tile(%d,%d)",x,y)));
            break;
          }
        }
      }
      case Checking: {
        if (canMopping == true) {
          tryMopping();
          break;
        }
        if (canMopping == false) {
          doMove();
          break;
        }
      }
    }
  }

  public enum Status {
    Dispatch,

    Mopping,

    Checking
  }
}
