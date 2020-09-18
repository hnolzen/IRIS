package eu.ecoepi.iris.systems;

import com.artemis.ComponentMapper;
import com.artemis.annotations.All;
import com.artemis.annotations.Wire;
import com.artemis.systems.IteratingSystem;
import eu.ecoepi.iris.Parameters;
import eu.ecoepi.iris.Randomness;
import eu.ecoepi.iris.components.Humidity;
import eu.ecoepi.iris.components.Temperature;
import eu.ecoepi.iris.components.TickAbundance;

@All({TickAbundance.class, Temperature.class, Humidity.class})
public class Activity extends IteratingSystem {

    ComponentMapper<TickAbundance> abundanceMapper;
    ComponentMapper<Temperature> temperatureMapper;
    ComponentMapper<Humidity> humidityMapper;

    @Wire
    Randomness randomness;

    @Override
    protected void process(int entityId) {
        var abundance = abundanceMapper.get(entityId);
        var temperature = temperatureMapper.get(entityId);
        var humidity = humidityMapper.get(entityId);

        var newActiveLarvae = Parameters.INITIAL_NEW_ACTIVE_LARVAE;
        var newActiveNymphs = Parameters.INITIAL_NEW_ACTIVE_NYMPHS;
        var newActiveAdults = Parameters.INITIAL_NEW_ACTIVE_ADULTS;
        var newInactiveLarvae = Parameters.INITIAL_NEW_INACTIVE_LARVAE;
        var newInactiveNymphs = Parameters.INITIAL_NEW_INACTIVE_NYMPHS;
        var newInactiveAdults = Parameters.INITIAL_NEW_INACTIVE_ADULTS;

        var shareOfActivationRate = Parameters.INITIAL_SHARE_OF_ACTIVATION_RATE;

        if (temperature.getMaxTemperature() < Parameters.ACTIVATION_THRESHOLD_NECESSARY_MAXIMAL_MAX_TEMP &&
                temperature.getMaxTemperature() > Parameters.ACTIVATION_THRESHOLD_NECESSARY_MINIMAL_MAX_TEMP &&
                temperature.getMeanTemperature() > Parameters.ACTIVATION_THRESHOLD_NECESSARY_MINIMAL_MEAN_TEMP &&
                humidity.getRelativeHumidity() > Parameters.ACTIVATION_THRESHOLD_NECESSARY_MINIMAL_HUMIDITY
        ) {

            if (temperature.getMaxTemperature() > Parameters.ACTIVATION_THRESHOLD_OPTIMAL_MAXIMAL_MAX_TEMP &&
                    temperature.getMaxTemperature() < Parameters.ACTIVATION_THRESHOLD_OPTIMAL_MINIMAL_MAX_TEMP &&
                    temperature.getMeanTemperature() > Parameters.ACTIVATION_THRESHOLD_OPTIMAL_MINIMAL_MEAN_TEMP &&
                    temperature.getMeanTemperature() < Parameters.ACTIVATION_THRESHOLD_OPTIMAL_MAXIMAL_MEAN_TEMP
            ) {
                shareOfActivationRate = Parameters.OPTIMAL_SHARE_OF_ACTIVATION_RATE;
            } else {
                shareOfActivationRate = Parameters.SUBOPTIMAL_SHARE_OF_ACTIVATION_RATE;
            }
        }

        newActiveLarvae = randomness.roundRandom(abundance.getInactiveLarvae() * Parameters.ACTIVATION_RATE * shareOfActivationRate);
        newActiveNymphs = randomness.roundRandom(abundance.getInactiveNymphs() * Parameters.ACTIVATION_RATE * shareOfActivationRate);
        newActiveAdults = randomness.roundRandom(abundance.getInactiveAdults() * Parameters.ACTIVATION_RATE * shareOfActivationRate);
        newInactiveLarvae = randomness.roundRandom(abundance.getLarvae() * Parameters.ACTIVATION_RATE * (1 - shareOfActivationRate));
        newInactiveNymphs = randomness.roundRandom(abundance.getNymphs() * Parameters.ACTIVATION_RATE * (1 - shareOfActivationRate));
        newInactiveAdults = randomness.roundRandom(abundance.getAdults() * Parameters.ACTIVATION_RATE * (1 - shareOfActivationRate));

        abundance.addLarvae(newActiveLarvae - newInactiveLarvae);
        abundance.addNymphs(newActiveNymphs - newInactiveNymphs);
        abundance.addAdults(newActiveAdults - newInactiveAdults);
        abundance.addInactiveLarvae(newInactiveLarvae - newActiveLarvae);
        abundance.addInactiveNymphs(newInactiveNymphs - newActiveNymphs);
        abundance.addInactiveAdults(newInactiveAdults - newActiveAdults);
    }
}
