package GeneratedCode;

/**
 * Generated State Machine Code;
 */
public class bulb {
  private Status status;

  private boolean Guard1;

  private boolean Guard2;

  void bulb() {
    setStatus(Status.Off);
  }

  public Status getStatus() {
    return status;
  }

  private void setStatus(Status aStatus) {
    status = aStatus;
  }

  public boolean PushButton() {
    boolean wasEventProcessed = false;
    Status aStatus = status;
    switch(aStatus) {
      case Off: {
        if(Guard1) {
          if(Math.random() < 0.8) {
            setStatus(Status.On);
            wasEventProcessed = true;
            break;
          }
        }
      }
      case On: {
        if(Guard2) {
          if(Math.random() < 0.4) {
            setStatus(Status.Off);
            wasEventProcessed = true;
            break;
          }
        }
      }
    }
    return wasEventProcessed;
  }

  public enum Status {
    Off,

    On
  }
}
