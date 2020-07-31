package eu.ecoepi.iris.systems;

import com.artemis.ComponentMapper;
import com.artemis.annotations.All;
import com.artemis.annotations.Wire;
import com.artemis.systems.IteratingSystem;
import eu.ecoepi.iris.TimeStep;
import eu.ecoepi.iris.components.Position;
import eu.ecoepi.iris.components.TickAbundance;

@All({TickAbundance.class, Position.class})
public class PrintAbundance extends IteratingSystem {

    ComponentMapper<TickAbundance> abundanceMapper;
    ComponentMapper<Position> positionMapper;
    @Wire
    TimeStep timeStep;

    @Override
    protected void process(int entityId) {
        var abundance = abundanceMapper.get(entityId);
        var position = positionMapper.get(entityId);
        System.out.format("Time step: %d, Position: %d %d, Larvae: %d, Nymphs: %d, Adults: %d, Sum: %d\n",
                timeStep.getCurrent(),
                position.getX(), position.getY(),
                abundance.getLarvae(), abundance.getNymphs(), abundance.getAdults(),
                abundance.getLarvae() + abundance.getNymphs() + abundance.getAdults());
    }
}
