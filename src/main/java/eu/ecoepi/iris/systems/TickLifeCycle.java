package eu.ecoepi.iris.systems;

import com.artemis.ComponentMapper;
import com.artemis.annotations.All;
import com.artemis.annotations.Wire;
import com.artemis.systems.IteratingSystem;
import eu.ecoepi.iris.LifeCycleStage;
import eu.ecoepi.iris.Parameters;
import eu.ecoepi.iris.Randomness;
import eu.ecoepi.iris.TimeStep;
import eu.ecoepi.iris.components.*;

@All({TickAbundance.class, Habitat.class, Temperature.class, Humidity.class, Precipitation.class})
public class TickLifeCycle extends IteratingSystem {

    ComponentMapper<TickAbundance> abundanceMapper;
    ComponentMapper<Habitat> habitatMapper;
    ComponentMapper<Temperature> temperatureMapper;
    ComponentMapper<Humidity> humidityMapper;
    ComponentMapper<Precipitation> precipitationMapper;

    @Wire
    TimeStep timestep;

    @Wire
    Randomness randomness;

    @Override
    protected void process(int entityId) {
        var abundance = abundanceMapper.get(entityId);
        var habitat = habitatMapper.get(entityId);
        var temperature = temperatureMapper.get(entityId);
        var humidity = humidityMapper.get(entityId);
        var precipitation = precipitationMapper.get(entityId);

        // Birth processes
        var newLarvae = 0;
        var newNymphs = 0;
        var newAdults = 0;

        // Development to the next life cycle stage only in Summer (insert reference here):
        if (timestep.getCurrent() > 195 && timestep.getCurrent() < 255) {
            newLarvae = randomness.roundRandom(abundance.getFedAdults() * Parameters.ADULTS_TO_LARVAE);
            newNymphs = randomness.roundRandom(abundance.getFedLarvae() * Parameters.LARVAE_TO_NYMPHS);
            newAdults = randomness.roundRandom(abundance.getFedNymphs() * Parameters.NYMPHS_TO_ADULTS);
        }

        // Natural death of adults
        var deadAdults = randomness.roundRandom(abundance.getAdults() * Parameters.NATURAL_DEATH_RATE);

        // Death by desiccation:
        var desiccatedLarvae = 0;
        var desiccatedNymphs = 0;
        var desiccatedAdults = 0;

        var isDesiccated = false;

        if (humidity.getRelativeHumidity() < 70 && temperature.getMeanTemperature() > 15) { // Ostfeld and Brunner 2015
            isDesiccated = true;
        }

        if (isDesiccated) {
            desiccatedLarvae = randomness.roundRandom(abundance.getLarvae() * Parameters.DESICCATION_RATE.get(habitat.getType()));
            desiccatedNymphs = randomness.roundRandom(abundance.getNymphs() * Parameters.DESICCATION_RATE.get(habitat.getType()));
            desiccatedAdults = randomness.roundRandom(abundance.getAdults() * Parameters.DESICCATION_RATE.get(habitat.getType()));
        }

        // Death by freezing:
        var frozenLarvae = 0;
        var frozenNymphs = 0;
        var frozenAdults = 0;

        var isFrozen = false;

        if (temperature.getMinTemperature() < -18.9 ||                                       // Gray et al. 2009
           (temperature.getMinTemperature() < -15.0 && precipitation.getSnowHeight() < 1)    // Ostfeld and Brunner 2015, Jore et al. 2014
        ) {
            isFrozen = true;
        }

        if (isFrozen) {
            frozenLarvae = randomness.roundRandom(abundance.getLarvae() * Parameters.FREEZING_RATE.get(LifeCycleStage.LARVAE));
            frozenNymphs = randomness.roundRandom(abundance.getNymphs() * Parameters.FREEZING_RATE.get(LifeCycleStage.NYMPH));
            frozenAdults = randomness.roundRandom(abundance.getAdults() * Parameters.FREEZING_RATE.get(LifeCycleStage.ADULT));
        }

        abundance.addLarvae(newLarvae - desiccatedLarvae - frozenLarvae);
        abundance.addFedLarvae(-newLarvae);

        abundance.addNymphs(newNymphs - desiccatedNymphs - frozenNymphs);
        abundance.addFedNymphs(-newNymphs);

        abundance.addAdults(-deadAdults + newAdults - desiccatedAdults - frozenAdults);
        abundance.addFedAdults(-newAdults);
    }
}
