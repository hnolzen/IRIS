package eu.ecoepi.iris;

import com.artemis.World;
import com.artemis.WorldConfigurationBuilder;
import eu.ecoepi.iris.components.Habitat;
import eu.ecoepi.iris.components.Position;
import eu.ecoepi.iris.components.TickAbundance;
import eu.ecoepi.iris.systems.Dispersal;
import eu.ecoepi.iris.systems.PrintAbundance;
import eu.ecoepi.iris.systems.TickLifeCycle;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {

        var config = new WorldConfigurationBuilder()
                .with(new TickLifeCycle())
                .with(new Dispersal())
                .with(new PrintAbundance())
                .build()
                .register(new SpatialIndex())
                .register(new TimeStep());

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

                var abundance = new TickAbundance(100, 100, 100);
                editor.add(abundance);

                var habitat = new Habitat(habitatType);
                editor.add(habitat);

            }
        }

        for (var timeStep = world.getRegistered(TimeStep.class); timeStep.getCurrent() < Parameters.TIME_STEPS; timeStep.increment()) {
            world.process();
        }
    }
}
