package eu.ecoepi.iris;

import org.apache.commons.math3.random.RandomGenerator;

public class Randomness {

    final RandomGenerator rng;

    public Randomness(RandomGenerator rng) {
        this.rng = rng;
    }

    public double random() {
        return this.rng.nextDouble();
    }

    /*
    This is necessary to compensate for the finite support of the model:
    We only have a finite number of ticks but rates only make sense for
    an infinite number of ticks. This method might round in the "wrong" direction.
    This allows processes that statistically affect less than a single tick to still happen.
     */
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
