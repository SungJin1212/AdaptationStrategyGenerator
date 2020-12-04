package CleaningSoSCaseStudy.Model.Behavior;

import Model.Behavior.CS;

import static Model.SoS.CleaningSoS.*;
import static Model.SoS.SoS.SoSGoal;

/**
 * Generated State Machine Code From: DustController.xml;
 */
public class DustController extends CS implements Cloneable {
  private Status status;

  public DustController() {
    setStatus(Status.UpdateDust);
  }

  public Status getStatus() {
    return status;
  }

  private void setStatus(Status aStatus) {
    status = aStatus;
  }

  public boolean dustUpdate() {
    boolean wasEventProcessed = false;
    Status aStatus = status;
    switch(aStatus) {
      case Model.Behavior.DustController.Status.UpdateDust: {
        putDust();
        setStatus(Status.UpdateDust);
        wasEventProcessed = true;
        break;
      }
    }
    return wasEventProcessed;
  }

  private void putDust() {

    for(int i=1; i<=MapSize; i++) {
      for(int j=1; j<=MapSize; j++) {
        tileMap[i][j] += dustUnit;
        if(tileMap[i][j] >= dustMax) {
          tileMap[i][j] = INITIALDUST;
          SoSGoal-= 0.5;
        }
      }
    }


  }

  public void run() {
    Status aStatus = status;
    switch(aStatus) {
      case Model.Behavior.DustController.Status.UpdateDust: {
        dustUpdate();
        break;
      }
    }
  }

  public enum Status {
    UpdateDust
  }
}
