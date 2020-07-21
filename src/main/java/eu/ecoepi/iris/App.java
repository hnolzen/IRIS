package eu.ecoepi.iris;

import com.artemis.World;
import com.artemis.WorldConfigurationBuilder;
import eu.ecoepi.iris.components.Hello;
import eu.ecoepi.iris.systems.HelloWorldSystem;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {
        var config = new WorldConfigurationBuilder().with(new HelloWorldSystem()).build();
        var world = new World(config);

        var entityId = world.create();
        var hello = world.edit(entityId).create(Hello.class);
        hello.setMessage("Hello ECS World!");

        world.process();

    }

}
