package eu.ecoepi.iris;

public class Fructification {
    private int index;

    public Fructification(int index) {

        if (index < 1 || index > 4) {
            throw new RuntimeException("Fructification index out of range!");
        }
        this.index = index;
    }

    public float getRate() {
        float rate;

        if (index == 1) {
            rate = 0.25f;
        } else if (index == 2) {
            rate = 0.5f;
        } else if (index == 3) {
            rate = 0.75f;
        } else {
            rate = 1.0f;
        }

        return rate;
    }

}
