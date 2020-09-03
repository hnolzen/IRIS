package eu.ecoepi.iris.observers;

import com.artemis.ComponentMapper;
import com.artemis.annotations.All;
import com.artemis.annotations.Wire;
import com.artemis.systems.IteratingSystem;
import eu.ecoepi.iris.components.Humidity;
import eu.ecoepi.iris.components.Temperature;
import eu.ecoepi.iris.TimeStep;
import eu.ecoepi.iris.components.Position;
import eu.ecoepi.iris.components.TickAbundance;

@All({TickAbundance.class, Position.class})
public class ConsoleTimeSeriesWriter extends IteratingSystem {

    ComponentMapper<TickAbundance> abundanceMapper;
    ComponentMapper<Position> positionMapper;
    ComponentMapper<Temperature> temperatureMapper;
    ComponentMapper<Humidity> humidityMapper;

    @Wire
    TimeStep timeStep;

    @Override
    protected void process(int entityId) {
        var abundance = abundanceMapper.get(entityId);
        var position = positionMapper.get(entityId);
        var temperature = temperatureMapper.get(entityId);
        var humidity = humidityMapper.get(entityId);
        System.out.format("Time step: %d, Position: %d %d, Temp: %f, Humidity: %f, Larvae: %d, Nymphs: %d, Adults: %d, Sum: %d\n",
                timeStep.getCurrent(),
                position.getX(), position.getY(),
                temperature.getMeanTemperature(),
                humidity.getRelativeHumidity(),
                abundance.getLarvae(), abundance.getNymphs(), abundance.getAdults(),
                abundance.getLarvae() + abundance.getNymphs() + abundance.getAdults());
    }
}
