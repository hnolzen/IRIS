package eu.ecoepi.iris;

import com.artemis.World;
import com.artemis.WorldConfigurationBuilder;
import eu.ecoepi.iris.components.*;
import eu.ecoepi.iris.observers.CsvTimeSeriesWriter;
import eu.ecoepi.iris.observers.XChartTimeSeriesPlotter;
import eu.ecoepi.iris.systems.Diapause;
import eu.ecoepi.iris.systems.Dispersal;
import eu.ecoepi.iris.observers.ConsoleTimeSeriesWriter;
import eu.ecoepi.iris.systems.TickLifeCycle;
import eu.ecoepi.iris.systems.Weather;

/**
 * IRIS
 */
public class App {
    public static void main(String[] args) throws Exception {

        var config = new WorldConfigurationBuilder()
                .with(new TickLifeCycle())
                .with(new Dispersal())
                .with(new ConsoleTimeSeriesWriter())
                .with(new CsvTimeSeriesWriter())
                .with(new Weather())
                .with(new Diapause())
                .with(new XChartTimeSeriesPlotter())
                .build()
                .register(new SpatialIndex())
                .register(new TimeStep())
                .register(new Randomness());

        var world = new World(config);

        var index = world.getRegistered(SpatialIndex.class);

        for (int x = 0; x < Parameters.GRID_WIDTH; ++x) {
            Habitat.Type habitatType;

            if (x >= Parameters.BOUNDARY_PASTURE) {
                habitatType = Habitat.Type.PASTURE;
            } else if (x >= Parameters.BOUNDARY_ECOTONE) {
                habitatType = Habitat.Type.ECOTONE;
            } else {
                habitatType = Habitat.Type.WOOD;
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
                        Parameters.INITIAL_INACTIVE_LARVAE,
                        Parameters.INITIAL_INACTIVE_NYMPHS,
                        Parameters.INITIAL_INACTIVE_ADULTS,
                        Parameters.INITIAL_FED_LARVAE,
                        Parameters.INITIAL_FED_NYMPHS,
                        Parameters.INITIAL_FED_ADULTS,
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

                var sunshine = new Sunshine();
                editor.add(sunshine);
            }
        }

        // Main loop
        for (var timeStep = world.getRegistered(TimeStep.class); timeStep.getCurrent() < Parameters.TIME_STEPS; timeStep.increment()) {
            world.process();

            Thread.sleep(70);

        }
    }
}
