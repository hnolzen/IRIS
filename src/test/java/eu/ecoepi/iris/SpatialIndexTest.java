package eu.ecoepi.iris;

import eu.ecoepi.iris.components.Position;
import eu.ecoepi.iris.resources.SpatialIndex;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.assertEquals;

public class SpatialIndexTest {

    @Test
    public void insertThenLookUp() {
        var index = new SpatialIndex();
        index.insert(new Position(2, 4), 6);
        var entityId = index.lookUp(new Position(2, 4));
        assertEquals(Optional.of(6), entityId);

        entityId = index.lookUp(new Position(4, 2));
        assertEquals(Optional.empty(), entityId);

    }

}
