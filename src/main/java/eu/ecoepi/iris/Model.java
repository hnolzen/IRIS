package eu.ecoepi.iris;

import com.artemis.World;
import com.artemis.WorldConfigurationBuilder;
import eu.ecoepi.iris.components.*;
import eu.ecoepi.iris.observers.CsvTimeSeriesWriter;
import eu.ecoepi.iris.systems.Activity;
import eu.ecoepi.iris.systems.Feeding;
import eu.ecoepi.iris.systems.TickLifeCycle;
import eu.ecoepi.iris.systems.Weather;
import org.apache.commons.math3.random.MersenneTwister;

/**
 * IRIS
 */
public class Model {
    public static class Options {
        public long seed = 42;
        public String weather;
        public String output;
        public int initialLarvae = 150;
        public int initialNymphs = 150;
        public int initialAdults = 150;
        public float activationRate = 0.05f;
        public int startLarvaeQuesting = 105;
    }

    public static void run(Options options) throws Exception {
        var rng = new MersenneTwister(options.seed);

        var config = new WorldConfigurationBuilder()
                .with(new TickLifeCycle())
                .with(new Feeding(rng))
                .with(new CsvTimeSeriesWriter(options.output))
                .with(new Weather(options.weather))
                .with(new Activity(options.activationRate, options.startLarvaeQuesting))
                .build()
                .register(new SpatialIndex())
                .register(new TimeStep())
                .register(new Randomness(rng));

        var world = new World(config);

        var index = world.getRegistered(SpatialIndex.class);

        for (int x = 0; x < Parameters.GRID_WIDTH; ++x) {
            Habitat.Type habitatType;

            if (x < Parameters.GRID_WIDTH / 2) {
                if (x < Parameters.GRID_WIDTH / 6) {
                    habitatType = Habitat.Type.PASTURE;
                } else if (x < Parameters.GRID_WIDTH / 3) {
                    habitatType = Habitat.Type.ECOTONE;
                } else {
                    habitatType = Habitat.Type.WOOD;
                }

            } else {
                if (x >= Parameters.GRID_WIDTH / 6 * 5) {
                    habitatType = Habitat.Type.PASTURE;
                } else if (x >= Parameters.GRID_WIDTH / 3 * 2) {
                    habitatType = Habitat.Type.ECOTONE;
                } else {
                    habitatType = Habitat.Type.WOOD;
                }
            }

            for (int y = 0; y < Parameters.GRID_HEIGHT; ++y) {
                var entityId = world.create();
                var editor = world.edit(entityId);

                var position = new Position(x, y);
                editor.add(position);
                index.insert(position, entityId);

                var abundance = new TickAbundance(
                        Parameters.INITIAL_LARVAE,
                        Parameters.INITIAL_NYMPHS,
                        Parameters.INITIAL_ADULTS,
                        options.initialLarvae,
                        options.initialNymphs,
                        options.initialAdults,
                        Parameters.INITIAL_FED_LARVAE,
                        Parameters.INITIAL_FED_NYMPHS,
                        Parameters.INITIAL_FED_ADULTS,
                        Parameters.INITIAL_LATE_FED_LARVAE,
                        Parameters.INITIAL_LATE_FED_NYMPHS,
                        Parameters.INITIAL_INFECTED_LARVAE,
                        Parameters.INITIAL_INFECTED_NYMPHS,
                        Parameters.INITIAL_INFECTED_ADULTS);
                editor.add(abundance);

                var habitat = new Habitat(habitatType);
                editor.add(habitat);

                var temperature = new Temperature();
                editor.add(temperature);

                var humidity = new Humidity();
                editor.add(humidity);
            }
        }

        // Main loop

        for (var timeStep = world.getRegistered(TimeStep.class); timeStep.getCurrent() < Parameters.TIME_STEPS; timeStep.increment()) {
            world.process();
        }
    }
}
