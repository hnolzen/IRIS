package eu.ecoepi.iris.systems;

import com.artemis.ComponentMapper;
import com.artemis.annotations.All;
import com.artemis.systems.IteratingSystem;
import eu.ecoepi.iris.Parameters;
import eu.ecoepi.iris.components.Habitat;
import eu.ecoepi.iris.components.TickAbundance;

@All({TickAbundance.class, Habitat.class})
public class TickLifeCycle extends IteratingSystem {

    ComponentMapper<TickAbundance> abundanceMapper;
    ComponentMapper<Habitat> habitatMapper;

    @Override
    protected void process(int entityId) {
        var abundance = abundanceMapper.get(entityId);
        var habitat = habitatMapper.get(entityId);
        var newNymphs = (int) (abundance.getLarvae() * Parameters.LARVAE_TO_NYMPHS);
        var newAdults = (int) (abundance.getNymphs() * Parameters.NYMPHS_TO_ADULTS);
        var newLarvae = (int) (abundance.getAdults() * Parameters.BIRTH_RATE);
        var deadAdults = (int) (abundance.getAdults() * Parameters.DEATH_RATE);

        abundance.addLarvae(-newNymphs + newLarvae);
        abundance.addNymphs(-newAdults + newNymphs);
        abundance.addAdults(-deadAdults + newAdults);

    }
}
