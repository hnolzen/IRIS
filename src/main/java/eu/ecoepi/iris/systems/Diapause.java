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

        var newActiveLarvae = 0;
        var newActiveNymphs = 0;
        var newActiveAdults = 0;
        var newInactiveLarvae = 0;
        var newInactiveNymphs = 0;
        var newInactiveAdults = 0;

        var isActive = false;

        // Necessary conditions for tick activity:
        if (temperature.getMaxTemperature() < 35 &&     // Gray et al. 2016, McLeod 1935
            temperature.getMaxTemperature() >= 1.9 &&   // Perret et al. 2000
            temperature.getMeanTemperature() >= 1.2 &&  // Perret et al. 2000, Schulz et al. 2014
            humidity.getRelativeHumidity() > 45         // Greenfield 2011
        ) {
            // Optimal conditions for tick activity:
            if (temperature.getMaxTemperature() > 10.5 &&   // Perrett et al. 2000
                temperature.getMaxTemperature() < 26 &&     // Greenfield 2011
                temperature.getMeanTemperature() >= 5.2 &&  // Perret et al. 2000
                humidity.getRelativeHumidity() > 70         // Hauser et al. 2018
            ) {
                isActive = true;
            } else {
                // In suboptimal but possible conditions, only a few ticks become active:
                if(randomness.random() < 0.1) {
                    isActive = true;
                }
            }
        }

        if (isActive) {
            newActiveLarvae = randomness.roundRandom(abundance.getInactiveLarvae() * Parameters.ACTIVITY_RATE);
            newActiveNymphs = randomness.roundRandom(abundance.getInactiveNymphs() * Parameters.ACTIVITY_RATE);
            newActiveAdults = randomness.roundRandom(abundance.getInactiveAdults() * Parameters.ACTIVITY_RATE);
        } else {
            newInactiveLarvae = randomness.roundRandom(abundance.getLarvae() * Parameters.ACTIVITY_RATE);
            newInactiveNymphs = randomness.roundRandom(abundance.getNymphs() * Parameters.ACTIVITY_RATE);
            newInactiveAdults = randomness.roundRandom(abundance.getAdults() * Parameters.ACTIVITY_RATE);
        }

        abundance.addLarvae(newActiveLarvae - newInactiveLarvae);
        abundance.addNymphs(newActiveNymphs - newInactiveNymphs);
        abundance.addAdults(newActiveAdults - newInactiveAdults);
        abundance.addInactiveLarvae(newInactiveLarvae - newActiveLarvae);
        abundance.addInactiveNymphs(newInactiveNymphs - newActiveNymphs);
        abundance.addInactiveAdults(newInactiveAdults - newActiveAdults);
    }
}
