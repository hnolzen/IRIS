package eu.ecoepi.iris.systems;

import com.artemis.ComponentMapper;
import com.artemis.annotations.All;
import com.artemis.annotations.Wire;
import com.artemis.systems.IteratingSystem;
import eu.ecoepi.iris.SpatialIndex;
import eu.ecoepi.iris.components.Position;

@All(Position.class)
public class HelloWorldSystem extends IteratingSystem {

    ComponentMapper<Position> positionMapper;
    @Wire
    SpatialIndex index;

    @Override
    protected void process(int entityId) {
        var position = positionMapper.get(entityId);
        var neighbour = index.lookUp(position.moveBy(1, 1));
        System.out.format("Grid cell %d %d %s\n", position.getX(), position.getY(), neighbour);

    }
}
