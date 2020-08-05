package eu.ecoepi.iris.systems;

import com.artemis.ComponentMapper;
import com.artemis.annotations.All;
import com.artemis.annotations.Wire;
import com.artemis.systems.IteratingSystem;
import eu.ecoepi.iris.Parameters;
import eu.ecoepi.iris.Randomness;
import eu.ecoepi.iris.components.Humidity;
import eu.ecoepi.iris.components.Temperature;
import eu.ecoepi.iris.TimeStep;
import eu.ecoepi.iris.components.Habitat;

@All({Habitat.class})
public class Weather extends IteratingSystem {

    ComponentMapper<Habitat> habitatMapper;
    ComponentMapper<Temperature> temperatureMapper;
    ComponentMapper<Humidity> humidityMapper;

    @Wire
    TimeStep timestep;

    @Wire
    Randomness randomness;

    @Override
    protected void process(int entityId) {
        var habitat = habitatMapper.get(entityId);
        var temperature = temperatureMapper.get(entityId);
        var humidity = humidityMapper.get(entityId);

        // TODO: Insert real weather data
        if (timestep.getCurrent() <= (Parameters.TIME_STEPS / 2)) {
            temperature.setMeanTemperature(timestep.getCurrent());
        } else {
            temperature.setMeanTemperature(Parameters.TIME_STEPS - timestep.getCurrent());
        }

        if (habitat.getType() == Habitat.Type.PASTURE) {
            humidity.setRelativeHumidity(50 + (randomness.random() * 50));
        } else if (habitat.getType() == Habitat.Type.ECOTONE) {
            humidity.setRelativeHumidity(55 + (randomness.random() * 45));
        } else {
            humidity.setRelativeHumidity(60 + (randomness.random() * 40));
        }

    }
}
