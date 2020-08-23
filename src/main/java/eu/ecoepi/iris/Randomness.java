package eu.ecoepi.iris;

import java.util.Random;

public class Randomness {

    final Random random = new Random(42);

    public double random() {
        return this.random.nextDouble();
    }

    public int randomInt(int range) {
        return this.random.nextInt(range);
    }

    public int roundRandom(float value) {
        int base = (int) value;
        float remainder = value - base;

        if (remainder == 0) {
            return base;
        }
        if (random() < remainder) {
            return base + 1;
        }
        return base;
    }
}
