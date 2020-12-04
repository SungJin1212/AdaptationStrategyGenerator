package CleaningSoSCaseStudy.Model.Behavior;

import Model.Behavior.CS;
import Model.Behavior.Tile;

import static Model.SoS.CleaningSoS.*;
import static Model.SoS.SoS.EnvironmentModelList;

/**
 * Generated State Machine Code From: SweepingRobot.xml;
 */
public class SweepingRobot extends CS implements Cloneable {
  private int x;

  private int y;

  private Status status;
  private boolean canSweeping;
  private boolean isSweepFinish;



  private int performance;

  private int SweepingTime;

  public SweepingRobot(int performance) {
    setStatus(Status.Dispatch);
    this.performance = performance;
    this.SweepingTime = 2;
  }

  public Status getStatus() {
    return status;
  }

  private void setStatus(Status aStatus) {
    status = aStatus;
  }

  public boolean trySweeping() {
    boolean wasEventProcessed = false;
    Status aStatus = status;
    switch(aStatus) {
      case Model.Behavior.SweepingRobot.Status.Checking: {
        setStatus(Status.Sweeping);
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
      case Model.Behavior.SweepingRobot.Status.Dispatch: {
        initPosition();
        checkCanSweeping();
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
      case Model.Behavior.SweepingRobot.Status.Checking: {
        move();
        checkCanSweeping();
        setStatus(Status.Checking);
        wasEventProcessed = true;
        break;
      }
    }
    return wasEventProcessed;
  }

  public boolean Sweep(Tile T) {
    boolean wasEventProcessed = false;
    Status aStatus = status;
    switch(aStatus) {
      case Model.Behavior.SweepingRobot.Status.Sweeping: {
        T.Sweep();
        sweeping();
        checkCanSweeping();
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

  public boolean SweepingNow() {
    boolean wasEventProcessed = false;
    Status aStatus = status;
    switch(aStatus) {
      case Model.Behavior.SweepingRobot.Status.Sweeping: {
        sweeping();
        setStatus(Status.Sweeping);
        wasEventProcessed = true;
        break;
      }
    }
    return wasEventProcessed;
  }

  private void move() {
    int[] dx = {1,1,0,-1,-1,-1,0,1};
    int[] dy = {0,1,1,1,0,-1,-1,-1};

    for(int dir = 0; dir < 8; dir++) {
      int searchX = x + dx[dir];
      int searchY = y + dy[dir];
      if (searchX >=1 && searchX <= MapSize && searchY >=1 && searchY <= MapSize) {
        if (tileMap[searchY][searchX] >= MopDust) {
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
  private void checkCanSweeping() {
    if(tileMap[y][x] >= 100) {
      canSweeping = true;
    }
    else {
      canSweeping = false;
    }
  }

  private void sweeping() {
    if (tileMap[x][y] - performance >= MopDust) {
      tileMap[x][y] -= performance;
    }
    else {
      tileMap[x][y] = MopDust;
    }

    if (tileMap[x][y] == MopDust) {
      isSweepFinish = true;
    }
  }

  public void run() {
    Status aStatus = status;
    switch(aStatus) {
      case Model.Behavior.SweepingRobot.Status.Dispatch: {
        initPositionandCheck();
        break;
      }
      case Model.Behavior.SweepingRobot.Status.Sweeping: {
        if(--SweepingTime == 0) {
          SweepingTime = 2;

          if (isSweepFinish == false) {
            SweepingNow();
          }
          if (isSweepFinish == true) {
            Sweep((Tile)EnvironmentModelList.get(String.format("Tile(%d,%d)",x,y)));
            break;
          }

          break;
        }
      }
      case Model.Behavior.SweepingRobot.Status.Checking: {
        if (canSweeping == true) {
          trySweeping();
          break;
        }
        if (canSweeping == false) {
          doMove();
          break;
        }
      }
    }
  }

  public enum Status {
    Dispatch,

    Sweeping,

    Checking
  }
}
