package eu.ecoepi.iris.observers;

import com.artemis.ComponentMapper;
import com.artemis.annotations.All;
import com.artemis.annotations.Wire;
import com.artemis.systems.IteratingSystem;
import eu.ecoepi.iris.CohortStateTicks;
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

    private int nymphsQuesting;
    private int nymphsInfectedQuesting;
    private int nymphsInfectedInactive;
    private int nymphsEngorged;
    private int nymphsInfectedEngorged;
    private int nymphsLateEngorged;
    private int nymphsInfectedLateEngorged;

    private int larvaeQuesting;
    private int larvaeInfectedQuesting;
    private int larvaeInfectedInactive;
    private int larvaeEngorged;
    private int larvaeInfectedEngorged;
    private int larvaeLateEngorged;
    private int larvaeInfectedLateEngorged;

    private int feedingEventsLarvae;
    private int feedingEventsInfectedLarvae;
    private int feedingEventsNymphs;
    private int feedingEventsInfectedNymphs;
    private int totalFeedingEventsInfected;

    private int feedingEventsNewInfectedLarvae;
    private int feedingEventsNewInfectedNymphs;

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
                "nymphs_questing," +
                "nymphs_questing_inf," +
                "nymphs_inactive_inf," +
                "nymphs_engorged," +
                "nymphs_engorged_inf," +
                "nymphs_late_engorged," +
                "nymphs_late_engorged_inf," +
                "larvae_questing," +
                "larvae_questing_inf," +
                "larvae_inactive_inf," +
                "larvae_engorged," +
                "larvae_engorged_inf," +
                "larvae_late_engorged," +
                "larvae_late_engorged_inf," +
                "larvae_feeding_events," +
                "larvae_feeding_events_inf," +
                "nymphs_feeding_events," +
                "nymphs_feeding_events_inf," +
                "total_feeding_events_inf," +
                "larvae_new_feeding_events_inf," +
                "nymphs_new_feeding_events_inf," +
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

        nymphsQuesting += abundance.getStage(CohortStateTicks.NYMPHS_QUESTING);
        nymphsInfectedQuesting += abundance.getStage(CohortStateTicks.NYMPHS_QUESTING_INFECTED);
        nymphsInfectedInactive += abundance.getStage(CohortStateTicks.NYMPHS_INACTIVE_INFECTED);
        nymphsEngorged += abundance.getStage(CohortStateTicks.NYMPHS_ENGORGED);
        nymphsInfectedEngorged += abundance.getStage(CohortStateTicks.NYMPHS_ENGORGED_INFECTED);
        nymphsLateEngorged += abundance.getStage(CohortStateTicks.NYMPHS_LATE_ENGORGED);
        nymphsInfectedLateEngorged += abundance.getStage(CohortStateTicks.NYMPHS_LATE_ENGORGED_INFECTED);

        larvaeQuesting += abundance.getStage(CohortStateTicks.LARVAE_QUESTING);
        larvaeInfectedQuesting += abundance.getStage(CohortStateTicks.NYMPHS_QUESTING_INFECTED);
        larvaeInfectedInactive += abundance.getStage(CohortStateTicks.LARVAE_INACTIVE_INFECTED);
        larvaeEngorged += abundance.getStage(CohortStateTicks.LARVAE_ENGORGED);
        larvaeInfectedEngorged += abundance.getStage(CohortStateTicks.LARVAE_ENGORGED_INFECTED);
        larvaeLateEngorged += abundance.getStage(CohortStateTicks.LARVAE_LATE_ENGORGED);
        larvaeInfectedLateEngorged += abundance.getStage(CohortStateTicks.LARVAE_LATE_ENGORGED_INFECTED);

        feedingEventsLarvae += abundance.getFeedingEvents(CohortStateTicks.LARVAE_QUESTING);
        feedingEventsInfectedLarvae += abundance.getFeedingEvents(CohortStateTicks.LARVAE_QUESTING_INFECTED);
        feedingEventsNymphs += abundance.getFeedingEvents(CohortStateTicks.NYMPHS_QUESTING);
        feedingEventsInfectedNymphs += abundance.getFeedingEvents(CohortStateTicks.NYMPHS_QUESTING_INFECTED);
        totalFeedingEventsInfected = feedingEventsInfectedLarvae + feedingEventsInfectedNymphs;

        feedingEventsNewInfectedLarvae += abundance.getFeedingEventsNewInfectedLarvae();
        feedingEventsNewInfectedNymphs += abundance.getFeedingEventsNewInfectedNymphs();

        rodentsSusceptible += rodentAbundance.getRodentsSusceptible();
        rodentsInfected += rodentAbundance.getRodentsInfected();

        dailyMeanTemperature = temperature.getMeanTemperature();
        dailyMaxTemperature = temperature.getMaxTemperature();
        dailyHumidity = humidity.getRelativeHumidity();
    }

    @Override
    protected void end() {

        csvWriter.format("%d,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f\n",
                timeStep.getCurrent(),
                (double) nymphsQuesting,
                (double) nymphsInfectedQuesting,
                (double) nymphsInfectedInactive,
                (double) nymphsEngorged,
                (double) nymphsInfectedEngorged,
                (double) nymphsLateEngorged,
                (double) nymphsInfectedLateEngorged,
                (double) larvaeQuesting,
                (double) larvaeInfectedQuesting,
                (double) larvaeInfectedInactive,
                (double) larvaeEngorged,
                (double) larvaeInfectedEngorged,
                (double) larvaeLateEngorged,
                (double) larvaeInfectedLateEngorged,
                (double) feedingEventsLarvae,
                (double) feedingEventsInfectedLarvae,
                (double) feedingEventsNymphs,
                (double) feedingEventsInfectedNymphs,
                (double) totalFeedingEventsInfected,
                (double) feedingEventsNewInfectedLarvae,
                (double) feedingEventsNewInfectedNymphs,
                (double) rodentsSusceptible,
                (double) rodentsInfected,
                dailyMeanTemperature,
                dailyMaxTemperature,
                dailyHumidity
        );

        nymphsQuesting = 0;
        nymphsInfectedQuesting = 0;
        nymphsInfectedInactive = 0;
        nymphsEngorged = 0;
        nymphsInfectedEngorged = 0;
        nymphsLateEngorged = 0;
        nymphsInfectedLateEngorged = 0;

        larvaeQuesting = 0;
        larvaeInfectedQuesting = 0;
        larvaeInfectedInactive = 0;
        larvaeEngorged = 0;
        larvaeInfectedEngorged = 0;
        larvaeLateEngorged = 0;
        larvaeInfectedLateEngorged = 0;

        feedingEventsLarvae = 0;
        feedingEventsInfectedLarvae = 0;
        feedingEventsNymphs = 0;
        feedingEventsInfectedNymphs = 0;
        totalFeedingEventsInfected = 0;

        feedingEventsNewInfectedLarvae = 0;
        feedingEventsNewInfectedNymphs = 0;

        rodentsSusceptible = 0;
        rodentsInfected = 0;
    }

    @Override
    protected void dispose() {
        csvWriter.flush();
    }
}

