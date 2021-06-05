package eu.ecoepi.iris.observers;

import com.artemis.ComponentMapper;
import com.artemis.annotations.All;
import com.artemis.annotations.Wire;
import com.artemis.systems.IteratingSystem;
import eu.ecoepi.iris.resources.TimeStep;
import eu.ecoepi.iris.components.*;

import java.io.PrintWriter;
import java.io.IOException;

@All({TickAbundance.class, Temperature.class})
public class CsvTimeSeriesWriterNymphsHabitats extends IteratingSystem {

    ComponentMapper<TickAbundance> abundanceMapper;
    ComponentMapper<Habitat> habitatMapper;
    ComponentMapper<Temperature> temperatureMapper;

    private final PrintWriter csvWriter;

    private int nymphsAllHabitats;
    private int nymphsForest;
    private int nymphsMeadow;
    private int nymphsEcotone;

    private double dailyMeanTemperature;

    @Wire
    TimeStep timeStep;

    public CsvTimeSeriesWriterNymphsHabitats(String path) throws IOException {
        csvWriter = new PrintWriter(path);
        csvWriter.print("tick,questing_nymphs_total,questing_nymphs_forest,questing_nymphs_meadow,questing_nymphs_ecotone,mean_temperature\n");
    }

    @Override
    protected void process(int entityId) {
        var abundance = abundanceMapper.get(entityId);
        var habitat = habitatMapper.get(entityId);
        var temperature = temperatureMapper.get(entityId);

        nymphsAllHabitats += abundance.getNymphs();

        if (habitat.getType() == Habitat.Type.WOOD) {
            nymphsForest += abundance.getNymphs();
        }

        if (habitat.getType() == Habitat.Type.MEADOW) {
            nymphsMeadow += abundance.getNymphs();
        }

        if (habitat.getType() == Habitat.Type.ECOTONE) {
            nymphsEcotone += abundance.getNymphs();
        }

        dailyMeanTemperature = temperature.getMeanTemperature();
    }

    @Override
    protected void end() {

        csvWriter.format("%d,%f,%f,%f,%f,%f\n",
                timeStep.getCurrent(),
                (double)nymphsAllHabitats,
                (double)nymphsForest,
                (double)nymphsMeadow,
                (double)nymphsEcotone,
                dailyMeanTemperature);

        nymphsAllHabitats = 0;
        nymphsForest = 0;
        nymphsMeadow = 0;
        nymphsEcotone = 0;
    }

    @Override
    protected void dispose() {
        csvWriter.flush();
    }
}
