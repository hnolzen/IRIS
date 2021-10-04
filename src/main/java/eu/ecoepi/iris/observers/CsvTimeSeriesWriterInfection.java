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
public class CsvTimeSeriesWriterInfection extends IteratingSystem {

    ComponentMapper<TickAbundance> abundanceMapper;
    ComponentMapper<HostAbundance> abundanceMapperRodents;
    ComponentMapper<Temperature> temperatureMapper;
    ComponentMapper<Humidity> humidityMapper;

    private final PrintWriter csvWriter;

    private int nymphs;
    private int nymphsInfected;
    private int nymphsEngorged;
    private int nymphsLateEngorged;

    private int larvae;
    private int larvaeInfected;
    private int larvaeEngorged;
    private int larvaeLateEngorged;

    private int feedingEvents;

    private int rodentsSusceptible;
    private int rodentsInfected;

    private double dailyMeanTemperature;
    private double dailyMaxTemperature;
    private double dailyHumidity;

    @Wire
    TimeStep timeStep;

    public CsvTimeSeriesWriterInfection(String path) throws IOException {
        csvWriter = new PrintWriter(path);
        csvWriter.print(
                "tick," +
                "questing_nymphs," +
                "questing_nymphs_infected," +
                "nymphs_engorged," +
                "nymphs_late_engorged," +
                "questing_larvae," +
                "questing_larvae_infected," +
                "larvae_engorged," +
                "larvae_late_engorged," +
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

        larvae += abundance.getQuestingLarvae();
        larvaeInfected += abundance.getInfectedQuestingLarvae();
        larvaeEngorged += abundance.getEngorgedLarvae();
        larvaeLateEngorged += abundance.getLateEngorgedLarvae();

        feedingEvents += abundance.getFeedingEventsNymphs();

        rodentsSusceptible += rodentAbundance.getRodents();
        rodentsInfected += rodentAbundance.getInfectedRodents();

        dailyMeanTemperature = temperature.getMeanTemperature();
        dailyMaxTemperature = temperature.getMaxTemperature();
        dailyHumidity = humidity.getRelativeHumidity();
    }

    @Override
    protected void end() {

        csvWriter.format("%d,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f\n",
                timeStep.getCurrent(),
                (double)nymphs,
                (double)nymphsInfected,
                (double)nymphsEngorged,
                (double)nymphsLateEngorged,
                (double)larvae,
                (double)larvaeInfected,
                (double)larvaeEngorged,
                (double)larvaeLateEngorged,
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

        larvae = 0;
        larvaeEngorged = 0;
        larvaeInfected = 0;
        larvaeLateEngorged = 0;

        feedingEvents = 0;

        rodentsSusceptible = 0;
        rodentsInfected = 0;
    }

    @Override
    protected void dispose() {
        csvWriter.flush();
    }
}

