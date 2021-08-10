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
        addHosts(abundance);
        removeHosts(abundance);
    }

    private void addHosts(HostAbundance abundance) {
        var newRodents = randomness.roundRandom((float) (abundance.getRodents() + abundance.getInfectedRodents()) * Parameters.BIRTH_RATE_RODENTS);
        abundance.addRodents(newRodents);
    }

    private void removeHosts(HostAbundance abundance) {
        var removeRodents = randomness.roundRandom((float) abundance.getRodents() * Parameters.MORTALITY_RATE_RODENTS);
        var removeInfectedRodents = randomness.roundRandom((float) abundance.getInfectedRodents() * Parameters.MORTALITY_RATE_INFECTED_RODENTS);
        abundance.addRodents(-removeRodents);
        abundance.addInfectedRodents(-removeInfectedRodents);
    }

}
