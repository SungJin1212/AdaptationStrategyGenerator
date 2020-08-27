package CodeGeneration.XMLParseDataType.Behavior;

public class State {
    private String initialState;
    private String stateName;
    private String time;
    private String atomic;

    public String getAtomic() {
        return atomic;
    }

    public void setAtomic(String atomic) {
        this.atomic = atomic;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getInitialState() {
        return initialState;
    }

    public void setInitialState(String initialState) {
        this.initialState = initialState;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getStateName() {
        return stateName;
    }
}
