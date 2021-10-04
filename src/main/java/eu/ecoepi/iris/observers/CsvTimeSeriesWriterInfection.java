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
    private int nymphsInfectedEngorged;
    private int nymphsLateEngorged;
    private int nymphsInfectedLateEngorged;

    private int larvae;
    private int larvaeInfected;
    private int larvaeEngorged;
    private int larvaeInfectedEngorged;
    private int larvaeLateEngorged;
    private int larvaeInfectedLateEngorged;

    private int feedingEventsLarvae;
    private int feedingEventsInfectedLarvae;
    private int feedingEventsNymphs;
    private int feedingEventsInfectedNymphs;

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
                        "nymphs_engorged_infected," +
                        "nymphs_late_engorged," +
                        "nymphs_late_engorged_infected," +
                        "questing_larvae," +
                        "questing_larvae_infected," +
                        "larvae_engorged," +
                        "larvae_engorged_infected," +
                        "larvae_late_engorged," +
                        "larvae_late_engorged_infected," +
                        "feeding_events_larvae," +
                        "feeding_events_larvae_infected," +
                        "feeding_events_nymphs," +
                        "feeding_events_nymphs_infected," +
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
        nymphsInfectedEngorged += abundance.getInfectedEngorgedNymphs();
        nymphsLateEngorged += abundance.getLateEngorgedNymphs();
        nymphsInfectedLateEngorged += abundance.getInfectedLateEngorgedNymphs();

        larvae += abundance.getQuestingLarvae();
        larvaeInfected += abundance.getInfectedQuestingLarvae();
        larvaeEngorged += abundance.getEngorgedLarvae();
        larvaeInfectedEngorged += abundance.getInfectedEngorgedLarvae();
        larvaeLateEngorged += abundance.getLateEngorgedLarvae();
        larvaeInfectedLateEngorged += abundance.getInfectedLateEngorgedLarvae();

        feedingEventsLarvae += abundance.getFeedingEventsLarvae();
        feedingEventsInfectedLarvae += abundance.getFeedingEventsInfectedLarvae();
        feedingEventsNymphs += abundance.getFeedingEventsNymphs();
        feedingEventsInfectedNymphs += abundance.getFeedingEventsInfectedNymphs();

        rodentsSusceptible += rodentAbundance.getRodents();
        rodentsInfected += rodentAbundance.getInfectedRodents();

        dailyMeanTemperature = temperature.getMeanTemperature();
        dailyMaxTemperature = temperature.getMaxTemperature();
        dailyHumidity = humidity.getRelativeHumidity();
    }

    @Override
    protected void end() {

        csvWriter.format("%d,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f\n",
                timeStep.getCurrent(),
                (double)nymphs,
                (double)nymphsInfected,
                (double)nymphsEngorged,
                (double)nymphsInfectedEngorged,
                (double)nymphsLateEngorged,
                (double)nymphsInfectedLateEngorged,
                (double)larvae,
                (double)larvaeInfected,
                (double)larvaeEngorged,
                (double)larvaeInfectedEngorged,
                (double)larvaeLateEngorged,
                (double)larvaeInfectedLateEngorged,
                (double)feedingEventsLarvae,
                (double)feedingEventsInfectedLarvae,
                (double)feedingEventsNymphs,
                (double)feedingEventsInfectedNymphs,
                (double)rodentsSusceptible,
                (double)rodentsInfected,
                dailyMeanTemperature,
                dailyMaxTemperature,
                dailyHumidity
        );

        nymphs = 0;
        nymphsInfected = 0;
        nymphsEngorged = 0;
        nymphsInfectedEngorged = 0;
        nymphsLateEngorged = 0;
        nymphsInfectedLateEngorged = 0;

        larvae = 0;
        larvaeInfected = 0;
        larvaeEngorged = 0;
        larvaeInfectedEngorged = 0;
        larvaeLateEngorged = 0;
        larvaeInfectedLateEngorged = 0;

        feedingEventsLarvae = 0;
        feedingEventsInfectedLarvae = 0;
        feedingEventsNymphs = 0;
        feedingEventsInfectedNymphs = 0;

        rodentsSusceptible = 0;
        rodentsInfected = 0;
    }

    @Override
    protected void dispose() {
        csvWriter.flush();
    }
}

