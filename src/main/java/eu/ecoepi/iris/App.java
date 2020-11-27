package eu.ecoepi.iris;

import com.artemis.World;
import com.artemis.WorldConfigurationBuilder;
import eu.ecoepi.iris.components.*;
import eu.ecoepi.iris.observers.CsvTimeSeriesWriter;
import eu.ecoepi.iris.systems.Activity;
import eu.ecoepi.iris.systems.Feeding;
import eu.ecoepi.iris.systems.TickLifeCycle;
import eu.ecoepi.iris.systems.Weather;
import org.apache.commons.cli.*;
import org.apache.commons.math3.random.MersenneTwister;

/**
 * IRIS
 */
public class App {
    public static void main(String[] args) throws Exception {

        Options options = new Options();

        options.addOption(Option.builder("s")
                .hasArg()
                .longOpt("seed")
                .build());

        options.addOption(Option.builder("f")
                .hasArg()
                .longOpt("fructification")
                .build());

        options.addOption(Option.builder("w")
                .hasArg()
                .longOpt("weather").required()
                .build());

        options.addOption(Option.builder("o")
                .hasArg()
                .longOpt("output").required()
                .build());

        options.addOption(Option.builder("l")
                .hasArg()
                .longOpt("larvae")
                .build());

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        var seed = Long.parseLong(cmd.getOptionValue("s", "42"));

        var rng = new MersenneTwister(seed);

        var fructification = new Fructification(Integer.parseInt(cmd.getOptionValue("f", "4")));

        var config = new WorldConfigurationBuilder()
                .with(new TickLifeCycle())
                .with(new Feeding(rng))
                .with(new CsvTimeSeriesWriter(cmd.getOptionValue("o")))
                .with(new Weather(cmd.getOptionValue("w")))
                .with(new Activity())
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

                var initialInactiveLarvae = Integer.parseInt(cmd.getOptionValue("l", Integer.toString(Parameters.INITIAL_INACTIVE_LARVAE)));

                var abundance = new TickAbundance(
                        (int) (Parameters.INITIAL_LARVAE * fructification.getRate()),
                        Parameters.INITIAL_NYMPHS,
                        Parameters.INITIAL_ADULTS,
                        (int) (initialInactiveLarvae * fructification.getRate()),
                        Parameters.INITIAL_INACTIVE_NYMPHS,
                        Parameters.INITIAL_INACTIVE_ADULTS,
                        (int) (Parameters.INITIAL_FED_LARVAE * fructification.getRate()),
                        Parameters.INITIAL_FED_NYMPHS,
                        Parameters.INITIAL_FED_ADULTS,
                        Parameters.INITIAL_LATE_FED_LARVAE,
                        Parameters.INITIAL_LATE_FED_NYMPHS,
                        (int) (Parameters.INITIAL_INFECTED_LARVAE * fructification.getRate()),
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

            //Thread.sleep(70);

        }
    }
}
