package eu.ecoepi.iris.systems;

import com.artemis.ComponentMapper;
import com.artemis.annotations.All;
import com.artemis.systems.IteratingSystem;
import eu.ecoepi.iris.components.Hello;

@All(Hello.class)
public class HelloWorldSystem extends IteratingSystem {

    ComponentMapper<Hello> helloMapper;

    @Override
    protected void process(int entityId) {
        var message = helloMapper.get(entityId).getMessage();
        System.out.println(message);
    }
}
