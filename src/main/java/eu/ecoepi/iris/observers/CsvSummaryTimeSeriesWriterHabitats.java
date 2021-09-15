package eu.ecoepi.iris.observers;

import com.artemis.ComponentMapper;
import com.artemis.annotations.All;
import com.artemis.annotations.Wire;
import com.artemis.systems.IteratingSystem;
import eu.ecoepi.iris.resources.TimeStep;
import eu.ecoepi.iris.components.*;

import java.io.PrintWriter;
import java.io.IOException;

@All({TickAbundance.class, Habitat.class})
public class CsvSummaryTimeSeriesWriterHabitats extends IteratingSystem {

    ComponentMapper<TickAbundance> abundanceMapper;
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
                "\n");
    }

    @Override
    protected void process(int entityId) {
        var abundance = abundanceMapper.get(entityId);
        var habitat = habitatMapper.get(entityId);

        count++;

        nymphsAllHabitats += abundance.getQuestingNymphs();
        nymphsInfectedAllHabitats += abundance.getInfectedQuestingNymphs();

        if (habitat.getType() == Habitat.Type.WOOD) {
            nymphsForest += abundance.getQuestingNymphs();
            nymphsInfectedForest += abundance.getInfectedQuestingNymphs();
        }

        if (habitat.getType() == Habitat.Type.MEADOW) {
            nymphsMeadow += abundance.getQuestingNymphs();
            nymphsInfectedMeadow += abundance.getInfectedQuestingNymphs();
        }

        if (habitat.getType() == Habitat.Type.ECOTONE) {
            nymphsEcotone += abundance.getQuestingNymphs();
            nymphsInfectedEcotone += abundance.getInfectedQuestingNymphs();
        }

    }

    @Override
    protected void end() {
        csvWriter.format("%d,%f,%f,%f,%f,%f,%f,%f,%f\n",
                timeStep.getCurrent(),
                (double)nymphsAllHabitats / (double)count,
                (double)nymphsInfectedAllHabitats / (double)count,
                (double)nymphsForest / (double)count,
                (double)nymphsInfectedForest / (double)count,
                (double)nymphsMeadow / (double)count,
                (double)nymphsInfectedMeadow / (double)count,
                (double)nymphsEcotone / (double)count,
                (double)nymphsInfectedEcotone / (double)count
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
    }

    @Override
    protected void dispose() {
        csvWriter.flush();
    }
}
