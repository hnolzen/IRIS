package eu.ecoepi.iris.observers;

import com.artemis.ComponentMapper;
import com.artemis.annotations.All;
import com.artemis.annotations.Wire;
import com.artemis.systems.IteratingSystem;
import eu.ecoepi.iris.TimeStep;
import eu.ecoepi.iris.components.*;

import java.io.FileWriter;
import java.io.IOException;

@All({TickAbundance.class, Position.class})
public class CsvTimeSeriesWriter extends IteratingSystem {

    ComponentMapper<TickAbundance> abundanceMapper;
    ComponentMapper<Position> positionMapper;
    ComponentMapper<Habitat> habitatMapper;
    ComponentMapper<Temperature> temperatureMapper;
    ComponentMapper<Humidity> humidityMapper;

    private FileWriter csvWriter;

    @Wire
    TimeStep timeStep;

    public CsvTimeSeriesWriter() {
        {
            try {
                csvWriter = new FileWriter("iris_abundance.csv");
                csvWriter.write("tick,x,y,habitat," +
                        "questing_larvae,questing_nymphs,questing_adults," +
                        "inactive_larvae,inactive_nymphs,inactive_adults," +
                        "fed_larvae,fed_nymphs,fed_adults," +
                        "t_mean,t_min,t_max,humidity" +
                        "\n");
                csvWriter.flush();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    protected void process(int entityId) {
        var abundance = abundanceMapper.get(entityId);
        var position = positionMapper.get(entityId);
        var habitat = habitatMapper.get(entityId);
        var temperature = temperatureMapper.get(entityId);
        var humidity = humidityMapper.get(entityId);

        try {
            csvWriter.write(Integer.toString(timeStep.getCurrent()));
            csvWriter.write(",");
            csvWriter.write(Integer.toString(position.getX()));
            csvWriter.write(",");
            csvWriter.write(Integer.toString(position.getY()));
            csvWriter.write(",");
            csvWriter.write(String.valueOf(habitat.getType()));
            csvWriter.write(",");
            csvWriter.write(Integer.toString(abundance.getLarvae()));
            csvWriter.write(",");
            csvWriter.write(Integer.toString(abundance.getNymphs()));
            csvWriter.write(",");
            csvWriter.write(Integer.toString(abundance.getAdults()));
            csvWriter.write(",");
            csvWriter.write(Integer.toString(abundance.getInactiveLarvae()));
            csvWriter.write(",");
            csvWriter.write(Integer.toString(abundance.getInactiveNymphs()));
            csvWriter.write(",");
            csvWriter.write(Integer.toString(abundance.getInactiveAdults()));
            csvWriter.write(",");
            csvWriter.write(Integer.toString(abundance.getFedLarvae()));
            csvWriter.write(",");
            csvWriter.write(Integer.toString(abundance.getFedNymphs()));
            csvWriter.write(",");
            csvWriter.write(Integer.toString(abundance.getFedAdults()));
            csvWriter.write(",");
            csvWriter.write(Double.toString(temperature.getMeanTemperature()));
            csvWriter.write(",");
            csvWriter.write(Double.toString(temperature.getMinTemperature()));
            csvWriter.write(",");
            csvWriter.write(Double.toString(temperature.getMaxTemperature()));
            csvWriter.write(",");
            csvWriter.write(Double.toString(humidity.getRelativeHumidity()));
            csvWriter.write("\n");
            csvWriter.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
