package eu.ecoepi.iris.systems;

import com.artemis.ComponentMapper;
import com.artemis.annotations.All;
import com.artemis.annotations.Wire;
import com.artemis.systems.IteratingSystem;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;
import eu.ecoepi.iris.Randomness;
import eu.ecoepi.iris.components.Humidity;
import eu.ecoepi.iris.components.Temperature;
import eu.ecoepi.iris.TimeStep;
import eu.ecoepi.iris.components.Habitat;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@All({Habitat.class})
public class Weather extends IteratingSystem {

    ComponentMapper<Habitat> habitatMapper;
    ComponentMapper<Temperature> temperatureMapper;
    ComponentMapper<Humidity> humidityMapper;

    final List<Float> meanTempTimeSeries = new ArrayList<>();
    final List<Float> minTempTimeSeries = new ArrayList<>();
    final List<Float> maxTempTimeSeries = new ArrayList<>();
    final List<Float> humidityTimeSeries = new ArrayList<>();

    @Wire
    TimeStep timestep;

    @Wire
    Randomness randomness;

    public Weather() throws IOException, CsvException {
        CSVReader reader = new CSVReaderBuilder(new FileReader("weatherData.csv"))
                .withSkipLines(1)
                .build();
        String[] nextLine;

        while ((nextLine = reader.readNext()) != null) {
            meanTempTimeSeries.add(Float.parseFloat(nextLine[0]));
            minTempTimeSeries.add(Float.parseFloat(nextLine[1]));
            maxTempTimeSeries.add(Float.parseFloat(nextLine[2]));
            humidityTimeSeries.add(Float.parseFloat(nextLine[3]));
        }
    }

    @Override
    protected void process(int entityId) {
        var habitat = habitatMapper.get(entityId);
        var temperature = temperatureMapper.get(entityId);
        var humidity = humidityMapper.get(entityId);

        temperature.setMeanTemperature(meanTempTimeSeries.get(timestep.getCurrent()));
        temperature.setMinTemperature(minTempTimeSeries.get(timestep.getCurrent()));
        temperature.setMaxTemperature(maxTempTimeSeries.get(timestep.getCurrent()));
        humidity.setRelativeHumidity(humidityTimeSeries.get(timestep.getCurrent()));

    }
}
