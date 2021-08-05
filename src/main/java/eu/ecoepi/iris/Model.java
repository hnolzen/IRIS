package eu.ecoepi.iris;

import com.artemis.BaseSystem;
import com.artemis.World;
import com.artemis.WorldConfigurationBuilder;
import eu.ecoepi.iris.components.*;
import eu.ecoepi.iris.observers.*;
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
        public int initialRodents = 5;
        public int initialInfectedRodents = 5;
        public float activationRate = 0.02f;
        public String outputMode = "csv_timeseries_summary";
    }

    public static void run(Options options) throws Exception {
        var rng = new MersenneTwister(options.seed);

        BaseSystem outputWriter = switch(options.outputMode){
            case "csv_timeseries" ->
                new CsvTimeSeriesWriter(options.output);

            case "csv_timeseries_summary" ->
                new CsvSummaryTimeSeriesWriter(options.output);

            case "csv_timeseries_summary_habitats" ->
                    new CsvSummaryTimeSeriesWriterHabitats(options.output);

            case "csv_timeseries_nymphs" ->
                    new CsvTimeSeriesWriterNymphs(options.output);

            case "csv_timeseries_nymphs_habitats" ->
                    new CsvTimeSeriesWriterNymphsHabitats(options.output);

            default -> throw new IllegalStateException("Unexpected value: " + options.outputMode +
                    ". Possible values are: \n" +
                    "1) 'csv_timeseries' \n" +
                    "2) 'csv_timeseries_summary' \n" +
                    "3) 'csv_timeseries_summary_habitats' \n" +
                    "4) 'csv_timeseries_nymphs' \n" +
                    "5) 'csv_timeseries_nymphs_habitats' \n");
        };

        var config = new WorldConfigurationBuilder()
                .with(new Weather(options.weather))
                .with(new Activity(options.activationRate))
                .with(new Feeding(rng))
                .with(new TickLifeCycle())
                .with(outputWriter)
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
                        Parameters.INITIAL_INFECTED_INACTIVE_LARVAE,
                        Parameters.INITIAL_INFECTED_INACTIVE_NYMPHS,
                        Parameters.INITIAL_INFECTED_ENGORGED_LARVAE,
                        Parameters.INITIAL_INFECTED_ENGORGED_NYMPHS,
                        Parameters.INITIAL_INFECTED_LATE_ENGORGED_LARVAE,
                        Parameters.INITIAL_INFECTED_LATE_ENGORGED_NYMPHS
                );
                editor.add(abundance);

                var hostAbundance = new HostAbundance(
                        options.initialRodents,
                        options.initialInfectedRodents
                );
                editor.add(hostAbundance);

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
        
        world.dispose();
    }
}
