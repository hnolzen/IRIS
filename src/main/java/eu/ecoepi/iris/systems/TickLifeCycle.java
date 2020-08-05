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

        var newLarvae = (int) (abundance.getAdults() * Parameters.BIRTH_RATE);
        var newNymphs = (int) (abundance.getLarvae() * Parameters.LARVAE_TO_NYMPHS);
        var newAdults = (int) (abundance.getNymphs() * Parameters.NYMPHS_TO_ADULTS);

        var deadAdults = (int) (abundance.getAdults() * Parameters.DEATH_RATE);

        var desiccatedLarvae = (int) (abundance.getLarvae() * Parameters.DESICCATION_RATE.get(habitat.getType()));
        var desiccatedNymphs = (int) (abundance.getNymphs() * Parameters.DESICCATION_RATE.get(habitat.getType()));
        var desiccatedAdults = (int) (abundance.getAdults() * Parameters.DESICCATION_RATE.get(habitat.getType()));

        abundance.addLarvae(-newNymphs + newLarvae - desiccatedLarvae);
        abundance.addNymphs(-newAdults + newNymphs - desiccatedNymphs);
        abundance.addAdults(-deadAdults + newAdults - desiccatedAdults);

    }
}
