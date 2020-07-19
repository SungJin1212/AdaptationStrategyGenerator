package Data;

public class Synchronization {
    String name;
    String trigger;

    public Synchronization(String name, String trigger) {
        this.name = name;
        this.trigger = trigger;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTrigger() {
        return trigger;
    }

    public void setTrigger(String trigger) {
        this.trigger = trigger;
    }
}
