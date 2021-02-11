package eu.ecoepi.iris.resources;

public class TimeStep {
    private int current;

    public int getCurrent() {
        return current;
    }

    public void increment() {
        ++current;
    }

}
