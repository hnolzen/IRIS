package eu.ecoepi.iris.systems;

import com.artemis.ComponentMapper;
import com.artemis.annotations.All;
import com.artemis.annotations.Wire;
import com.artemis.systems.IteratingSystem;
import eu.ecoepi.iris.components.HostAbundance;
import eu.ecoepi.iris.resources.Parameters;
import eu.ecoepi.iris.resources.Randomness;

@All({HostAbundance.class})
public class HostLifeCycle extends IteratingSystem {

    ComponentMapper<HostAbundance> abundanceMapper;

    @Wire
    Randomness randomness;

    @Override
    protected void process(int entityId) {
        var abundance = abundanceMapper.get(entityId);

        var replacedInfectedRodents = randomness.roundRandom((float) abundance.getRodentsInfected() * Parameters.REPLACEMENT_RATE_RODENTS);

        abundance.addRodentsSusceptible(replacedInfectedRodents);
        abundance.addRodentsInfected(-replacedInfectedRodents);
    }
}
