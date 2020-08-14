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
import java.util.HashMap;
import java.util.Map;

@All({Habitat.class})
public class Weather extends IteratingSystem {

    ComponentMapper<Habitat> habitatMapper;
    ComponentMapper<Temperature> temperatureMapper;
    ComponentMapper<Humidity> humidityMapper;

    final Map<Integer, Float> meanTempTimeSeries = new HashMap();
    final Map<Integer, Float> minTempTimeSeries = new HashMap();
    final Map<Integer, Float> maxTempTimeSeries = new HashMap();
    final Map<Integer, Float> humidityTimeSeries = new HashMap();

    @Wire
    TimeStep timestep;

    @Wire
    Randomness randomness;

    public Weather() throws IOException, CsvException {
        CSVReader reader = new CSVReaderBuilder(new FileReader("weatherData.csv"))
                .withSkipLines(1)
                .build();
        String[] nextLine;

        int i = 0;
        while ((nextLine = reader.readNext()) != null) {
            meanTempTimeSeries.put(i, Float.parseFloat(nextLine[0]));
            minTempTimeSeries.put(i, Float.parseFloat(nextLine[1]));
            maxTempTimeSeries.put(i, Float.parseFloat(nextLine[2]));
            humidityTimeSeries.put(i, Float.parseFloat(nextLine[3]));
            ++i;
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

//        if (habitat.getType() == Habitat.Type.PASTURE) {
//            humidity.setRelativeHumidity(humidityTimeSeries.get(timestep.getCurrent()));
//        } else if (habitat.getType() == Habitat.Type.ECOTONE) {
//            humidity.setRelativeHumidity(humidityTimeSeries.get(timestep.getCurrent()));
//        } else {
//            humidity.setRelativeHumidity(humidityTimeSeries.get(timestep.getCurrent()));
//        }

    }
}
