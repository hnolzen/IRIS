package eu.ecoepi.iris.observers;

import com.artemis.ComponentMapper;
import com.artemis.annotations.All;
import com.artemis.annotations.Wire;
import com.artemis.systems.IteratingSystem;
import eu.ecoepi.iris.resources.TimeStep;
import eu.ecoepi.iris.components.*;

import java.io.PrintWriter;
import java.io.IOException;

@All({TickAbundance.class, Temperature.class, Humidity.class})
public class CsvTimeSeriesWriterNymphs extends IteratingSystem {

    ComponentMapper<TickAbundance> abundanceMapper;
    ComponentMapper<Temperature> temperatureMapper;
    ComponentMapper<Humidity> humidityMapper;

    private final PrintWriter csvWriter;

    private int nymphs;
    private int nymphsEngorged;
    private int nymphsLateEngorged;
    private int feedingEvents;

    private double dailyMeanTemperature;
    private double dailyMaxTemperature;
    private double dailyHumidity;

    @Wire
    TimeStep timeStep;

    public CsvTimeSeriesWriterNymphs(String path) throws IOException {
        csvWriter = new PrintWriter(path);
        csvWriter.print("tick,questing_nymphs,nymphs_engorged,nymphs_late_engorged,feeding_events,mean_temperature,max_temperature,humidity\n");
    }

    @Override
    protected void process(int entityId) {
        var abundance = abundanceMapper.get(entityId);
        var temperature = temperatureMapper.get(entityId);
        var humidity = humidityMapper.get(entityId);

        nymphs += abundance.getNymphs();
        nymphsEngorged += abundance.getEngorgedNymphs();
        nymphsLateEngorged += abundance.getLateEngorgedNymphs();
        feedingEvents += abundance.getFeedingEventsNymphs();

        dailyMeanTemperature = temperature.getMeanTemperature();
        dailyMaxTemperature = temperature.getMaxTemperature();
        dailyHumidity = humidity.getRelativeHumidity();
    }

    @Override
    protected void end() {

        csvWriter.format("%d,%f,%f,%f,%f,%f,%f,%f\n",
                timeStep.getCurrent(),
                (double)nymphs,
                (double)nymphsEngorged,
                (double)nymphsLateEngorged,
                (double)feedingEvents,
                dailyMeanTemperature,
                dailyMaxTemperature,
                dailyHumidity);

        nymphs = 0;
        nymphsEngorged = 0;
        nymphsLateEngorged = 0;
        feedingEvents = 0;
    }

    @Override
    protected void dispose() {
        csvWriter.flush();
    }
}
