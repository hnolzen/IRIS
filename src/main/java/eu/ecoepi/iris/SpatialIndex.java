package eu.ecoepi.iris;

import eu.ecoepi.iris.components.Position;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class SpatialIndex {

    final Map<Position, Integer> map = new HashMap<>();

    public void insert(Position position, int entityId) {
        map.put(position, entityId);
    }

    public Optional<Integer> lookUp(Position position) {
        return Optional.ofNullable(map.get(position));
    }
}
