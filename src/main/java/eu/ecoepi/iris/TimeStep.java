package eu.ecoepi.iris;

public class TimeStep {
    private int current;

    public int getCurrent() {
        return current;
    }

    public void increment() {
        ++current;
    }

}
