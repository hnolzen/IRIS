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
public class CsvTimeSeriesWriterNymphsHabitats extends IteratingSystem {

    ComponentMapper<TickAbundance> abundanceMapper;
    ComponentMapper<HostAbundance> abundanceMapperRodents;
    ComponentMapper<Habitat> habitatMapper;
    ComponentMapper<Temperature> temperatureMapper;
    ComponentMapper<Humidity> humidityMapper;

    private final PrintWriter csvWriter;

    private int nymphsAllHabitats;
    private int nymphsForest;
    private int nymphsMeadow;
    private int nymphsEcotone;

    private int nymphsInfectedAllHabitats;
    private int nymphsInfectedForest;
    private int nymphsInfectedMeadow;
    private int nymphsInfectedEcotone;

    private int rodentsSusceptibleAllHabitats;
    private int rodentsSusceptibleForest;
    private int rodentsSusceptibleMeadow;
    private int rodentsSusceptibleEcotone;

    private int rodentsInfectedAllHabitats;
    private int rodentsInfectedForest;
    private int rodentsInfectedMeadow;
    private int rodentsInfectedEcotone;

    private double dailyMeanTemperature;
    private double dailyMaxTemperature;
    private double dailyHumidity;

    @Wire
    TimeStep timeStep;

    public CsvTimeSeriesWriterNymphsHabitats(String path) throws IOException {
        csvWriter = new PrintWriter(path);
        csvWriter.print(
                "tick," +
                "questing_nymphs," +
                "questing_nymphs_infected," +
                "questing_nymphs_forest," +
                "questing_nymphs_forest_infected," +
                "questing_nymphs_meadow," +
                "questing_nymphs_meadow_infected," +
                "questing_nymphs_ecotone," +
                "questing_nymphs_ecotone_infected," +
                "rodents_susceptible_all," +
                "rodents_infected_all," +
                "rodents_susceptible_forest," +
                "rodents_forest_infected," +
                "rodents_susceptible_meadow," +
                "rodents_meadow_infected," +
                "rodents_susceptible_ecotone," +
                "rodents_ecotone_infected," +
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
        var habitat = habitatMapper.get(entityId);
        var temperature = temperatureMapper.get(entityId);
        var humidity = humidityMapper.get(entityId);

        nymphsAllHabitats += abundance.getQuestingNymphs();
        nymphsInfectedAllHabitats += abundance.getInfectedQuestingNymphs();

        rodentsSusceptibleAllHabitats += rodentAbundance.getRodents();
        rodentsInfectedAllHabitats += rodentAbundance.getInfectedRodents();

        if (habitat.getType() == Habitat.Type.WOOD) {
            nymphsForest += abundance.getQuestingNymphs();
            nymphsInfectedForest += abundance.getInfectedQuestingNymphs();

            rodentsSusceptibleForest += rodentAbundance.getRodents();
            rodentsInfectedForest += rodentAbundance.getInfectedRodents();
        }

        if (habitat.getType() == Habitat.Type.MEADOW) {
            nymphsMeadow += abundance.getQuestingNymphs();
            nymphsInfectedMeadow += abundance.getInfectedQuestingNymphs();

            rodentsSusceptibleMeadow += rodentAbundance.getRodents();
            rodentsInfectedMeadow += rodentAbundance.getInfectedRodents();
        }

        if (habitat.getType() == Habitat.Type.ECOTONE) {
            nymphsEcotone += abundance.getQuestingNymphs();
            nymphsInfectedEcotone += abundance.getInfectedQuestingNymphs();

            rodentsSusceptibleEcotone += rodentAbundance.getRodents();
            rodentsInfectedEcotone += rodentAbundance.getInfectedRodents();
        }

        dailyMeanTemperature = temperature.getMeanTemperature();
        dailyMaxTemperature = temperature.getMaxTemperature();
        dailyHumidity = humidity.getRelativeHumidity();
    }

    @Override
    protected void end() {

        csvWriter.format("%d,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f\n",
                timeStep.getCurrent(),
                (double)nymphsAllHabitats,
                (double)nymphsInfectedAllHabitats,
                (double)nymphsForest,
                (double)nymphsInfectedForest,
                (double)nymphsMeadow,
                (double)nymphsInfectedMeadow,
                (double)nymphsEcotone,
                (double)nymphsInfectedEcotone,
                (double)rodentsSusceptibleAllHabitats,
                (double)rodentsInfectedAllHabitats,
                (double)rodentsSusceptibleForest,
                (double)rodentsInfectedForest,
                (double)rodentsSusceptibleMeadow,
                (double)rodentsInfectedMeadow,
                (double)rodentsSusceptibleEcotone,
                (double)rodentsInfectedEcotone,
                dailyMeanTemperature,
                dailyMaxTemperature,
                dailyHumidity
        );

        nymphsAllHabitats = 0;
        nymphsForest = 0;
        nymphsMeadow = 0;
        nymphsEcotone = 0;

        nymphsInfectedAllHabitats = 0;
        nymphsInfectedForest = 0;
        nymphsInfectedMeadow = 0;
        nymphsInfectedEcotone = 0;

        rodentsSusceptibleAllHabitats = 0;
        rodentsSusceptibleForest = 0;
        rodentsSusceptibleMeadow = 0;
        rodentsSusceptibleEcotone = 0;

        rodentsInfectedAllHabitats = 0;
        rodentsInfectedForest = 0;
        rodentsInfectedMeadow = 0;
        rodentsInfectedEcotone = 0;
    }

    @Override
    protected void dispose() {
        csvWriter.flush();
    }
}
