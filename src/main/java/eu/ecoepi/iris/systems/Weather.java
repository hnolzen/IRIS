package eu.ecoepi.iris.systems;

import com.artemis.ComponentMapper;
import com.artemis.annotations.All;
import com.artemis.annotations.Wire;
import com.artemis.systems.IteratingSystem;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;
import eu.ecoepi.iris.components.*;
import eu.ecoepi.iris.TimeStep;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@All({Habitat.class})
public class Weather extends IteratingSystem {

    ComponentMapper<Temperature> temperatureMapper;
    ComponentMapper<Humidity> humidityMapper;
    ComponentMapper<Precipitation> precipitationMapper;
    ComponentMapper<Sunshine> sunshineMapper;

    final List<Float> meanTempTimeSeries = new ArrayList<>();
    final List<Float> minTempTimeSeries = new ArrayList<>();
    final List<Float> maxTempTimeSeries = new ArrayList<>();
    final List<Float> humidityTimeSeries = new ArrayList<>();
    final List<Integer> precipitationTypeTimeSeries = new ArrayList<>();
    final List<Float> precipitationHeightTimeSeries = new ArrayList<>();
    final List<Float> snowHeightTimeSeries = new ArrayList<>();
    final List<Float> sunshineHoursTimeSeries = new ArrayList<>();

    @Wire
    TimeStep timestep;

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
            precipitationTypeTimeSeries.add(Integer.parseInt(nextLine[4]));
            precipitationHeightTimeSeries.add(Float.parseFloat(nextLine[5]));
            snowHeightTimeSeries.add(Float.parseFloat(nextLine[6]));
            sunshineHoursTimeSeries.add(Float.parseFloat(nextLine[7]));
        }
    }

    @Override
    protected void process(int entityId) {
        var temperature = temperatureMapper.get(entityId);
        var humidity = humidityMapper.get(entityId);
        var precipitation = precipitationMapper.get(entityId);
        var sunshine = sunshineMapper.get(entityId);

        temperature.setMeanTemperature(meanTempTimeSeries.get(timestep.getCurrent()));
        temperature.setMinTemperature(minTempTimeSeries.get(timestep.getCurrent()));
        temperature.setMaxTemperature(maxTempTimeSeries.get(timestep.getCurrent()));
        humidity.setRelativeHumidity(humidityTimeSeries.get(timestep.getCurrent()));
        precipitation.setPrecipitationType(precipitationTypeTimeSeries.get(timestep.getCurrent()));
        precipitation.setPrecipitationHeight(precipitationHeightTimeSeries.get(timestep.getCurrent()));
        precipitation.setSnowHeight(snowHeightTimeSeries.get(timestep.getCurrent()));
        sunshine.setSunshineHours(sunshineHoursTimeSeries.get(timestep.getCurrent()));

    }
}
