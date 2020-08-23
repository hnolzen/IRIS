package eu.ecoepi.iris.systems;

import com.artemis.ComponentMapper;
import com.artemis.annotations.All;
import com.artemis.systems.IteratingSystem;
import eu.ecoepi.iris.LifeCycleStage;
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

        // Birth processes
        var newLarvae = 0;
        var newNymphs = 0;
        var newAdults = 0;
        //if (temperature.getMeanTemperature() > 5 && temperature.getMeanTemperature() < 19) {
        //    newLarvae = (int) (abundance.getAdults() * Parameters.BIRTH_RATE_GOOD.get(habitat.getType()));
        //} else {
        //    newLarvae = (int) (abundance.getAdults() * Parameters.BIRTH_RATE_POOR.get(habitat.getType()));
        //}

        //var newNymphs = (int) (abundance.getLarvae() * Parameters.LARVAE_TO_NYMPHS);
        //var newAdults = (int) (abundance.getNymphs() * Parameters.NYMPHS_TO_ADULTS);

        // Death processes

        var deadAdults = (int) (abundance.getAdults() * Parameters.NATURAL_DEATH_RATE);

        var desiccatedLarvae = 0;
        var desiccatedNymphs = 0;
        var desiccatedAdults = 0;
        if (temperature.getMaxTemperature() > 28) {
            desiccatedLarvae = (int) (abundance.getLarvae() * Parameters.DESICCATION_RATE.get(habitat.getType()));
            desiccatedNymphs = (int) (abundance.getNymphs() * Parameters.DESICCATION_RATE.get(habitat.getType()));
            desiccatedAdults = (int) (abundance.getAdults() * Parameters.DESICCATION_RATE.get(habitat.getType()));
        }

        var frozenLarvae = 0;
        var frozenNymphs = 0;
        var frozenAdults = 0;
        if (temperature.getMinTemperature() < -10.0) {
            frozenLarvae = (int) (abundance.getLarvae() * Parameters.FREEZING_RATE.get(LifeCycleStage.LARVAE));
            frozenNymphs = (int) (abundance.getNymphs() * Parameters.FREEZING_RATE.get(LifeCycleStage.NYMPH));
            frozenAdults = (int) (abundance.getAdults() * Parameters.FREEZING_RATE.get(LifeCycleStage.ADULT));
        }

        abundance.addLarvae(-newNymphs + newLarvae - desiccatedLarvae - frozenLarvae);
        abundance.addNymphs(-newAdults + newNymphs - desiccatedNymphs - frozenNymphs);
        abundance.addAdults(-deadAdults + newAdults - desiccatedAdults - frozenAdults);

    }
}
