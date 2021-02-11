package eu.ecoepi.iris.observers;

import com.artemis.ComponentMapper;
import com.artemis.annotations.All;
import com.artemis.annotations.Wire;
import com.artemis.systems.IteratingSystem;
import eu.ecoepi.iris.resources.TimeStep;
import eu.ecoepi.iris.components.*;

import java.io.IOException;
import java.io.PrintWriter;

@All({TickAbundance.class, Position.class})
public class CsvTimeSeriesWriter extends IteratingSystem {

    ComponentMapper<TickAbundance> abundanceMapper;
    ComponentMapper<Position> positionMapper;
    ComponentMapper<Habitat> habitatMapper;
    ComponentMapper<Temperature> temperatureMapper;
    ComponentMapper<Humidity> humidityMapper;

    private final PrintWriter csvWriter;

    @Wire
    TimeStep timeStep;

    public CsvTimeSeriesWriter(String path) throws IOException {
        csvWriter = new PrintWriter(path);
        csvWriter.print("tick,x,y,habitat," +
                "questing_larvae,questing_nymphs,questing_adults," +
                "inactive_larvae,inactive_nymphs,inactive_adults," +
                "engorged_larvae,engorged_nymphs,engorged_adults," +
                "late_engorged_larvae,late_engorged_nymphs," +
                "t_mean,t_min,t_max,humidity," +
                "feeding_events_larvae,feeding_events_nymphs,feeding_events_adults" +
                "\n");
    }

    @Override
    protected void process(int entityId) {
        var abundance = abundanceMapper.get(entityId);
        var position = positionMapper.get(entityId);
        var habitat = habitatMapper.get(entityId);
        var temperature = temperatureMapper.get(entityId);
        var humidity = humidityMapper.get(entityId);

        csvWriter.format("%d,%d,%d,%s,%d,%d,%d,%d,%d,%d,%d,%d,%d,%d,%d,%f,%f,%f,%f,%d,%d,%d\n",
            timeStep.getCurrent(),
            position.getX(),
            position.getY(),
            habitat.getType(),
            abundance.getLarvae(),
            abundance.getNymphs(),
            abundance.getAdults(),
            abundance.getInactiveLarvae(),
            abundance.getInactiveNymphs(),
            abundance.getInactiveAdults(),
            abundance.getEngorgedLarvae(),
            abundance.getEngorgedNymphs(),
            abundance.getEngorgedAdults(),
            abundance.getLateEngorgedLarvae(),
            abundance.getLateEngorgedNymphs(),
            temperature.getMeanTemperature(),
            temperature.getMinTemperature(),
            temperature.getMaxTemperature(),
            humidity.getRelativeHumidity(),
            abundance.getFeedingEventsLarvae(),
            abundance.getFeedingEventsNymphs(),
            abundance.getFeedingEventsAdults());
    }

    @Override
    protected void dispose() {
        csvWriter.flush();
    }
}
