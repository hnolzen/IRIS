package eu.ecoepi.iris.systems;

import com.artemis.ComponentMapper;
import com.artemis.annotations.All;
import com.artemis.annotations.Wire;
import com.artemis.systems.IteratingSystem;
import eu.ecoepi.iris.Parameters;
import eu.ecoepi.iris.SpatialIndex;
import eu.ecoepi.iris.components.Position;
import eu.ecoepi.iris.components.TickAbundance;

@All({TickAbundance.class, Position.class})
public class Dispersal extends IteratingSystem {

    ComponentMapper<TickAbundance> abundanceMapper;
    ComponentMapper<Position> positionMapper;

    @Wire
    SpatialIndex index;

    @Override
    protected void process(int entityId) {
        var abundance = abundanceMapper.get(entityId);
        var position = positionMapper.get(entityId);
        var neighbourToNorth = index.lookUp(position.moveBy(0, 1));
        if (neighbourToNorth.isPresent()) {
            var abundanceToNorth = abundanceMapper.get(neighbourToNorth.get());
            processPair(abundance, abundanceToNorth);
        }

        var neighbourToSouth = index.lookUp(position.moveBy(0, -1));
        if (neighbourToSouth.isPresent()) {
            var abundanceToSouth = abundanceMapper.get(neighbourToSouth.get());
            processPair(abundance, abundanceToSouth);
        }

        // TODO: Ost und West

    }

    void processPair(TickAbundance abundance, TickAbundance neighbour) {
        var movingLarvae = (int) (abundance.getLarvae() * Parameters.LARVA_DISPERSAL_RATE);
        abundance.addLarvae(-movingLarvae);
        neighbour.addLarvae(movingLarvae);

        // TODO: Nymphs, Adults, Habitatabhängigkeit, etc.

    }
}
