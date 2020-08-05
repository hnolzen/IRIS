package eu.ecoepi.iris.systems;

import com.artemis.ComponentMapper;
import com.artemis.annotations.All;
import com.artemis.systems.IteratingSystem;
import eu.ecoepi.iris.Parameters;
import eu.ecoepi.iris.components.Habitat;
import eu.ecoepi.iris.components.Humidity;
import eu.ecoepi.iris.components.Temperature;
import eu.ecoepi.iris.components.TickAbundance;

@All({TickAbundance.class, Habitat.class, Temperature.class, Humidity.class})
public class TickLifeCycle extends IteratingSystem {

    ComponentMapper<TickAbundance> abundanceMapper;
    ComponentMapper<Habitat> habitatMapper;
    ComponentMapper<Temperature> temperatureMapper;
    ComponentMapper<Humidity> humidityMapper;

    @Override
    protected void process(int entityId) {
        var abundance = abundanceMapper.get(entityId);
        var habitat = habitatMapper.get(entityId);
        var temperature = temperatureMapper.get(entityId);
        var humidity = humidityMapper.get(entityId);

        var newLarvae = (int) (abundance.getAdults() * Parameters.BIRTH_RATE);
        var newNymphs = (int) (abundance.getLarvae() * Parameters.LARVAE_TO_NYMPHS);
        var newAdults = (int) (abundance.getNymphs() * Parameters.NYMPHS_TO_ADULTS);

        var deadAdults = (int) (abundance.getAdults() * Parameters.DEATH_RATE);

        var desiccatedLarvae = 0;
        var desiccatedNymphs = 0;
        var desiccatedAdults = 0;
        if (humidity.getRelativeHumidity() < 80.0) {
            desiccatedLarvae = (int) (abundance.getLarvae() * Parameters.DESICCATION_RATE.get(habitat.getType()));
            desiccatedNymphs = (int) (abundance.getNymphs() * Parameters.DESICCATION_RATE.get(habitat.getType()));
            desiccatedAdults = (int) (abundance.getAdults() * Parameters.DESICCATION_RATE.get(habitat.getType()));
        }

        abundance.addLarvae(-newNymphs + newLarvae - desiccatedLarvae);
        abundance.addNymphs(-newAdults + newNymphs - desiccatedNymphs);
        abundance.addAdults(-deadAdults + newAdults - desiccatedAdults);

    }
}
