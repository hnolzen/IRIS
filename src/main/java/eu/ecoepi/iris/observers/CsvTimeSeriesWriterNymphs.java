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
    ComponentMapper<HostAbundance> abundanceMapperRodents;
    ComponentMapper<Temperature> temperatureMapper;
    ComponentMapper<Humidity> humidityMapper;

    private final PrintWriter csvWriter;

    private int nymphs;
    private int nymphsInfected;
    private int nymphsEngorged;
    private int nymphsLateEngorged;
    private int feedingEvents;

    private int rodentsSusceptible;
    private int rodentsInfected;

    private double dailyMeanTemperature;
    private double dailyMaxTemperature;
    private double dailyHumidity;

    @Wire
    TimeStep timeStep;

    public CsvTimeSeriesWriterNymphs(String path) throws IOException {
        csvWriter = new PrintWriter(path);
        csvWriter.print(
                "tick," +
                "questing_nymphs," +
                "questing_nymphs_infected," +
                "nymphs_engorged," +
                "nymphs_late_engorged," +
                "feeding_events," +
                "rodents_susceptible," +
                "rodents_infected," +
                "mean_temperature," +
                "max_temperature," +
                "humidity" +
                "\n"
        );
    }

    @Override
    protected void process(int entityId) {
        var abundance = abundanceMapper.get(entityId);
        var rodentAbundance = abundanceMapperRodents.get(entityId);
        var temperature = temperatureMapper.get(entityId);
        var humidity = humidityMapper.get(entityId);

        nymphs += abundance.getQuestingNymphs();
        nymphsInfected += abundance.getInfectedQuestingNymphs();
        nymphsEngorged += abundance.getEngorgedNymphs();
        nymphsLateEngorged += abundance.getLateEngorgedNymphs();
        feedingEvents += abundance.getFeedingEventsNymphs();

        rodentsSusceptible += rodentAbundance.getRodentsSusceptible();
        rodentsInfected += rodentAbundance.getRodentsInfected();

        dailyMeanTemperature = temperature.getMeanTemperature();
        dailyMaxTemperature = temperature.getMaxTemperature();
        dailyHumidity = humidity.getRelativeHumidity();
    }

    @Override
    protected void end() {

        csvWriter.format("%d,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f\n",
                timeStep.getCurrent(),
                (double)nymphs,
                (double)nymphsInfected,
                (double)nymphsEngorged,
                (double)nymphsLateEngorged,
                (double)feedingEvents,
                (double)rodentsSusceptible,
                (double)rodentsInfected,
                dailyMeanTemperature,
                dailyMaxTemperature,
                dailyHumidity
        );

        nymphs = 0;
        nymphsEngorged = 0;
        nymphsInfected = 0;
        nymphsLateEngorged = 0;
        feedingEvents = 0;

        rodentsSusceptible = 0;
        rodentsInfected = 0;
    }

    @Override
    protected void dispose() {
        csvWriter.flush();
    }
}
