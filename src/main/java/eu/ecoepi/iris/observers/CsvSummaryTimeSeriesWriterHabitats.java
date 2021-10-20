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

@All({TickAbundance.class, Habitat.class})
public class CsvSummaryTimeSeriesWriterHabitats extends IteratingSystem {

    ComponentMapper<TickAbundance> abundanceMapper;
    ComponentMapper<HostAbundance> abundanceMapperRodents;
    ComponentMapper<Habitat> habitatMapper;

    private final PrintWriter csvWriter;

    private int count;

    private int nymphsAllHabitats;
    private int nymphsInfectedAllHabitats;
    private int nymphsForest;
    private int nymphsInfectedForest;
    private int nymphsMeadow;
    private int nymphsInfectedMeadow;
    private int nymphsEcotone;
    private int nymphsInfectedEcotone;

    private int rodentsSusceptibleAllHabitats;
    private int rodentsSusceptibleForest;
    private int rodentsSusceptibleMeadow;
    private int rodentsSusceptibleEcotone;

    private int rodentsInfectedAllHabitats;
    private int rodentsInfectedForest;
    private int rodentsInfectedMeadow;
    private int rodentsInfectedEcotone;

    @Wire
    TimeStep timeStep;

    public CsvSummaryTimeSeriesWriterHabitats(String path) throws IOException {
        csvWriter = new PrintWriter(path);
        csvWriter.print("tick," +
                "questing_nymphs_total," +
                "questing_nymphs_total_infected," +
                "questing_nymphs_forest," +
                "questing_nymphs_forest_infected," +
                "questing_nymphs_meadow," +
                "questing_nymphs_meadow_infected," +
                "questing_nymphs_ecotone" +
                "questing_nymphs_ecotone_infected" +
                "rodents_susceptible_all," +
                "rodents_infected_all," +
                "rodents_susceptible_forest," +
                "rodents_forest_infected," +
                "rodents_susceptible_meadow," +
                "rodents_meadow_infected," +
                "rodents_susceptible_ecotone," +
                "rodents_ecotone_infected," +
                "\n");
    }

    @Override
    protected void process(int entityId) {
        var abundance = abundanceMapper.get(entityId);
        var rodentAbundance = abundanceMapperRodents.get(entityId);
        var habitat = habitatMapper.get(entityId);

        count++;

        nymphsAllHabitats += abundance.getStage(CohortStateTicks.NYMPHS_QUESTING);
        nymphsInfectedAllHabitats += abundance.getStage(CohortStateTicks.NYMPHS_QUESTING_INFECTED);

        rodentsSusceptibleAllHabitats += rodentAbundance.getRodentsSusceptible();
        rodentsInfectedAllHabitats += rodentAbundance.getRodentsInfected();

        if (habitat.getType() == Habitat.Type.WOOD) {
            nymphsForest += abundance.getStage(CohortStateTicks.NYMPHS_QUESTING);
            nymphsInfectedForest += abundance.getStage(CohortStateTicks.NYMPHS_QUESTING_INFECTED);

            rodentsSusceptibleForest += rodentAbundance.getRodentsSusceptible();
            rodentsInfectedForest += rodentAbundance.getRodentsInfected();
        }

        if (habitat.getType() == Habitat.Type.MEADOW) {
            nymphsMeadow += abundance.getStage(CohortStateTicks.NYMPHS_QUESTING);
            nymphsInfectedMeadow += abundance.getStage(CohortStateTicks.NYMPHS_QUESTING_INFECTED);

            rodentsSusceptibleMeadow += rodentAbundance.getRodentsSusceptible();
            rodentsInfectedMeadow += rodentAbundance.getRodentsInfected();
        }

        if (habitat.getType() == Habitat.Type.ECOTONE) {
            nymphsEcotone += abundance.getStage(CohortStateTicks.NYMPHS_QUESTING);
            nymphsInfectedEcotone += abundance.getStage(CohortStateTicks.NYMPHS_QUESTING_INFECTED);

            rodentsSusceptibleEcotone += rodentAbundance.getRodentsSusceptible();
            rodentsInfectedEcotone += rodentAbundance.getRodentsInfected();
        }

    }

    @Override
    protected void end() {
        csvWriter.format("%d,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f\n",
                timeStep.getCurrent(),
                (double)nymphsAllHabitats / (double)count,
                (double)nymphsInfectedAllHabitats / (double)count,
                (double)nymphsForest / (double)count,
                (double)nymphsInfectedForest / (double)count,
                (double)nymphsMeadow / (double)count,
                (double)nymphsInfectedMeadow / (double)count,
                (double)nymphsEcotone / (double)count,
                (double)nymphsInfectedEcotone / (double)count,
                (double)rodentsSusceptibleAllHabitats,
                (double)rodentsInfectedAllHabitats,
                (double)rodentsSusceptibleForest,
                (double)rodentsInfectedForest,
                (double)rodentsSusceptibleMeadow,
                (double)rodentsInfectedMeadow,
                (double)rodentsSusceptibleEcotone,
                (double)rodentsInfectedEcotone
        );

        count = 0;

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
