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
public class Diapause extends IteratingSystem {

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

        var shareOfActivationRate = 0.0f;
                                                                // Necessary conditions for tick activity:
        if (temperature.getMaxTemperature() < 35 &&             // Gray et al. 2016, McLeod 1935
                temperature.getMaxTemperature() > 1.9 &&        // Perret et al. 2000
                temperature.getMeanTemperature() > 1.2 &&       // Perret et al. 2000, Schulz et al. 2014
                humidity.getRelativeHumidity() > 45             // Greenfield 2011
        ) {
                                                                // Optimal conditions for tick activity:
            if (temperature.getMaxTemperature() > 10.5 &&       // Perret et al. 2000
                    temperature.getMaxTemperature() < 26 &&     // Greenfield 2011, Schulz et al. 2014 (25.9)
                    temperature.getMeanTemperature() > 6 &&     // Gilbert et al. 2014
                    temperature.getMeanTemperature() < 20       // Kubiak and Dziekońska−Rynko 2006
            ) {
                shareOfActivationRate = 1.0f;
            } else {
                shareOfActivationRate = 0.05f;                  // In suboptimal conditions, only few ticks become active
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
