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

        var nextStageLarvae = Parameters.INITIAL_NEXT_STAGE_LARVAE;
        var nextStageNymphs = Parameters.INITIAL_NEXT_STAGE_NYMPHS;
        var nextStageAdults = Parameters.INITIAL_NEXT_STAGE_ADULTS;

        var desiccatedLarvae = Parameters.INITIAL_DESICCATED_LARVAE;
        var desiccatedNymphs = Parameters.INITIAL_DESICCATED_NYMPHS;
        var desiccatedAdults = Parameters.INITIAL_DESICCATED_ADULTS;

        var frozenLarvae = Parameters.INITIAL_FROZEN_LARVAE;
        var frozenNymphs = Parameters.INITIAL_FROZEN_NYMPHS;
        var frozenAdults = Parameters.INITIAL_FROZEN_ADULTS;

        var deadAdults = Parameters.INITIAL_DEAD_ADULTS;

        if (timestep.getCurrent() >= Parameters.BEGIN_OF_DEVELOPMENT && timestep.getCurrent() <= Parameters.END_OF_DEVELOPMENT) {
            nextStageLarvae = randomness.roundRandom(abundance.getFedAdults() * Parameters.ADULTS_TO_LARVAE);
            nextStageNymphs = randomness.roundRandom(abundance.getFedLarvae() * Parameters.LARVAE_TO_NYMPHS);
            nextStageAdults = randomness.roundRandom(abundance.getFedNymphs() * Parameters.NYMPHS_TO_ADULTS);
        }

        deadAdults = randomness.roundRandom(abundance.getAdults() * Parameters.NATURAL_DEATH_RATE);

        var isDesiccated = false;
        if (humidity.getRelativeHumidity() < 70 && temperature.getMeanTemperature() > 15) { // Ostfeld and Brunner 2015
            isDesiccated = true;
        }

        if (isDesiccated) {
            desiccatedLarvae = randomness.roundRandom(abundance.getLarvae() * Parameters.DESICCATION_RATE.get(habitat.getType()));
            desiccatedNymphs = randomness.roundRandom(abundance.getNymphs() * Parameters.DESICCATION_RATE.get(habitat.getType()));
            desiccatedAdults = randomness.roundRandom(abundance.getAdults() * Parameters.DESICCATION_RATE.get(habitat.getType()));
        }

        var isFrozen = false;
        if (temperature.getMinTemperature() < -18.9 ||                                            // Gray et al. 2009
                (temperature.getMinTemperature() < -15.0 && precipitation.getSnowHeight() < 1)    // Ostfeld and Brunner 2015, Jore et al. 2014
        ) {
            isFrozen = true;
        }

        if (isFrozen) {
            frozenLarvae = randomness.roundRandom(abundance.getLarvae() * Parameters.FREEZING_RATE.get(LifeCycleStage.LARVAE));
            frozenNymphs = randomness.roundRandom(abundance.getNymphs() * Parameters.FREEZING_RATE.get(LifeCycleStage.NYMPH));
            frozenAdults = randomness.roundRandom(abundance.getAdults() * Parameters.FREEZING_RATE.get(LifeCycleStage.ADULT));
        }

        abundance.addLarvae(nextStageLarvae - desiccatedLarvae - frozenLarvae);
        abundance.addFedLarvae(-nextStageLarvae);

        abundance.addNymphs(nextStageNymphs - desiccatedNymphs - frozenNymphs);
        abundance.addFedNymphs(-nextStageNymphs);

        abundance.addAdults(-deadAdults + nextStageAdults - desiccatedAdults - frozenAdults);
        abundance.addFedAdults(-nextStageAdults);
    }
}
