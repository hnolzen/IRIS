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

    @Wire
    TimeStep timeStep;

    public CsvSummaryTimeSeriesWriter(String path) throws IOException {
        csvWriter = new PrintWriter(path);
        csvWriter.print("tick,questing_larvae,questing_nymphs,questing_adults\n");
    }
    
    @Override
    protected void process(int entityId) {
        var abundance = abundanceMapper.get(entityId);
        
        count++;
        
        larvae += abundance.getLarvae();
        nymphs += abundance.getNymphs();
        adults += abundance.getAdults();
    }
    
    @Override
    protected void end() {
        csvWriter.format("%d,%f,%f,%f\n",
            timeStep.getCurrent(),
            (double)larvae / (double)count,
            (double)nymphs / (double)count,
            (double)adults / (double)count);
            
        count = 0;
        
        larvae = 0;
        nymphs = 0;
        adults = 0;
    }

    @Override
    protected void dispose() {
        csvWriter.flush();
    }
}
