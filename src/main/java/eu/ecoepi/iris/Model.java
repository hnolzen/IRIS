package eu.ecoepi.iris;

import com.artemis.World;
import com.artemis.WorldConfigurationBuilder;
import eu.ecoepi.iris.components.*;
import eu.ecoepi.iris.observers.CsvSummaryTimeSeriesWriterHabitats;
import eu.ecoepi.iris.observers.CsvTimeSeriesWriter;
import eu.ecoepi.iris.observers.CsvSummaryTimeSeriesWriter;
import eu.ecoepi.iris.resources.Parameters;
import eu.ecoepi.iris.resources.Randomness;
import eu.ecoepi.iris.resources.SpatialIndex;
import eu.ecoepi.iris.resources.TimeStep;
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
        public int initialInactiveLarvae = 150;
        public int initialInactiveNymphs = 150;
        public int initialInactiveAdults = 150;
        public float activationRate = 0.02f;
        public boolean summary = false;
        public boolean summaryHabitats = false;
        public boolean withPrecipitation = false;
    }

    public static void run(Options options) throws Exception {
        var rng = new MersenneTwister(options.seed);

        var config = new WorldConfigurationBuilder()
                .with(new Weather(options.weather, options.withPrecipitation))
                .with(new Activity(options.activationRate))
                .with(new Feeding(rng))
                .with(new TickLifeCycle())
                .with(options.summary ? new CsvSummaryTimeSeriesWriter(options.output) : new CsvTimeSeriesWriter(options.output))
                .with(options.summaryHabitats ? new CsvSummaryTimeSeriesWriterHabitats(options.output) : new CsvTimeSeriesWriter(options.output))
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
                    habitatType = Habitat.Type.MEADOW;
                } else if (x < Parameters.GRID_WIDTH / 3) {
                    habitatType = Habitat.Type.ECOTONE;
                } else {
                    habitatType = Habitat.Type.WOOD;
                }

            } else {
                if (x >= Parameters.GRID_WIDTH / 6 * 5) {
                    habitatType = Habitat.Type.MEADOW;
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
                        options.initialInactiveLarvae,
                        options.initialInactiveNymphs,
                        options.initialInactiveAdults,
                        Parameters.INITIAL_ENGORGED_LARVAE,
                        Parameters.INITIAL_ENGORGED_NYMPHS,
                        Parameters.INITIAL_ENGORGED_ADULTS,
                        Parameters.INITIAL_LATE_ENGORGED_LARVAE,
                        Parameters.INITIAL_LATE_ENGORGED_NYMPHS,
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

                var precipitation = new Precipitation();
                editor.add(precipitation);

            }
        }

        // Main loop

        for (var timeStep = world.getRegistered(TimeStep.class); timeStep.getCurrent() < Parameters.TIME_STEPS; timeStep.increment()) {
            world.process();
        }
        
        world.dispose();
    }
}
