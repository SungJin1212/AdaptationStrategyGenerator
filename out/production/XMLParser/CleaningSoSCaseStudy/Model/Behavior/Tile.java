package CleaningSoSCaseStudy.Model.Behavior;

import static Model.SoS.CleaningSoS.*;
import static Model.SoS.SoS.SoSGoal;


/**
 * Generated State Machine Code From: Tile.xml;
 */
public class Tile extends Environment {
  private Status status;

  private int x;

  private int y;

  private int MoppedTime;

  public Tile(int x, int y) {
    setStatus(Status.Dirty);
    this.x = x;
    this.y = y;
    this.MoppedTime = 3;
  }

  public Status getStatus() {
    return status;
  }

  private void setStatus(Status aStatus) {
    status = aStatus;
  }

  public boolean returnToDirty() {
    boolean wasEventProcessed = false;
    Status aStatus = status;
    switch(aStatus) {
      case Model.Behavior.Tile.Status.Mopped: {
        initDirty();
        setStatus(Status.Dirty);
        wasEventProcessed = true;
        break;
      }
    }
    return wasEventProcessed;
  }

  public boolean Sweep() {
    boolean wasEventProcessed = false;
    Status aStatus = status;
    switch(aStatus) {
      case Model.Behavior.Tile.Status.Dirty: {
        setStatus(Status.Sweepped);
        wasEventProcessed = true;
        break;
      }
    }
    return wasEventProcessed;
  }

  public boolean Mop() {
    boolean wasEventProcessed = false;
    Status aStatus = status;
    switch(aStatus) {
      case Model.Behavior.Tile.Status.Sweepped: {
        SoSGoal++;
        setStatus(Status.Mopped);
        wasEventProcessed = true;
        break;
      }
    }
    return wasEventProcessed;
  }

  private void initDirty() {
    for(int i=1; i<=MapSize; i++) {
      for(int j=1; j<=MapSize; j++) {
        tileMap[i][j] = INITIALDUST;
      }
    }
  }

  public void run() {
    Status aStatus = status;
    switch(aStatus) {
      case Model.Behavior.Tile.Status.Dirty: {
        double pro = Math.random();
      }
      case Model.Behavior.Tile.Status.Sweepped: {
        double pro = Math.random();
      }
      case Model.Behavior.Tile.Status.Mopped: {
        if(--MoppedTime == 0) {
          MoppedTime = 3;
          returnToDirty();
          break;
        }
      }
    }
  }

  public enum Status {
    Dirty,

    Sweepped,

    Mopped
  }
}
