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

}
