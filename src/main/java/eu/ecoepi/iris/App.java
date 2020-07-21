package eu.ecoepi.iris;

import com.artemis.World;
import com.artemis.WorldConfigurationBuilder;
import eu.ecoepi.iris.components.Position;
import eu.ecoepi.iris.systems.HelloWorldSystem;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {

        var config = new WorldConfigurationBuilder().with(new HelloWorldSystem()).build().register(new SpatialIndex());
        var world = new World(config);

        var index = world.getRegistered(SpatialIndex.class);

        for (int x = 0; x < Parameters.GRID_WIDTH; ++x) {
            for (int y = 0; y < Parameters.GRID_HEIGHT; ++y) {
                var entityId = world.create();
                var editor = world.edit(entityId);
                var position = new Position(x, y);
                editor.add(position);
                index.insert(position, entityId);
            }
        }

        world.process();

    }

}
