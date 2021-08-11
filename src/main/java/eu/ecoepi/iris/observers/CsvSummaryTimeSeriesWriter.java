package eu.ecoepi.iris.observers;

import com.artemis.ComponentMapper;
import com.artemis.annotations.All;
import com.artemis.annotations.Wire;
import com.artemis.systems.IteratingSystem;
import eu.ecoepi.iris.resources.TimeStep;
import eu.ecoepi.iris.components.*;

import java.io.PrintWriter;
import java.io.IOException;

@All(TickAbundance.class)
public class CsvSummaryTimeSeriesWriter extends IteratingSystem {

    ComponentMapper<TickAbundance> abundanceMapper;

    private final PrintWriter csvWriter;
    
    private int count;

    private int larvae;
    private int nymphs;
    private int adults;

    private int larvaeInfected;
    private int nymphsInfected;

    @Wire
    TimeStep timeStep;

    public CsvSummaryTimeSeriesWriter(String path) throws IOException {
        csvWriter = new PrintWriter(path);
        csvWriter.print("tick,questing_larvae,questing_nymphs,questing_adults,questing_larvae_infected,questing_nymphs_infected\n");
    }
    
    @Override
    protected void process(int entityId) {
        var abundance = abundanceMapper.get(entityId);
        
        count++;
        
        larvae += abundance.getQuestingLarvae();
        nymphs += abundance.getQuestingNymphs();
        adults += abundance.getQuestingAdults();

        larvaeInfected += abundance.getInfectedQuestingLarvae();
        nymphsInfected += abundance.getInfectedQuestingNymphs();
    }
    
    @Override
    protected void end() {
        csvWriter.format("%d,%f,%f,%f,%f,%f\n",
            timeStep.getCurrent(),
            (double)larvae / (double)count,
            (double)nymphs / (double)count,
            (double)adults / (double)count,
            (double)larvaeInfected / (double)count,
            (double)nymphsInfected / (double)count
        );
            
        count = 0;
        
        larvae = 0;
        nymphs = 0;
        adults = 0;

        larvaeInfected = 0;
        nymphsInfected = 0;
    }

    @Override
    protected void dispose() {
        csvWriter.flush();
    }
}
