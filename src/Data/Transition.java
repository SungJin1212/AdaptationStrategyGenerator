package Data;

public class Transition {
    private String from;
    private String to;
    private String guard;
    private String probability;
    private String time;
    private String action;
    private String trigger;

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public String getGuard() {
        return guard;
    }

    public String getProbability() {
        return probability;
    }

    public String getTime() {
        return time;
    }

    public String getAction() {
        return action;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public void setGuard(String guard) {
        this.guard = guard;
    }

    public void setProbability(String probability) {
        this.probability = probability;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public void setTrigger(String trigger) {
        this.trigger = trigger;
    }

    public String getTrigger() {
        return trigger;
    }

}
