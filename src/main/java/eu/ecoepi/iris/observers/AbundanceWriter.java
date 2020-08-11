package eu.ecoepi.iris.observers;

import com.artemis.ComponentMapper;
import com.artemis.annotations.All;
import com.artemis.annotations.Wire;
import com.artemis.systems.IteratingSystem;
import eu.ecoepi.iris.Parameters;
import eu.ecoepi.iris.TimeStep;
import eu.ecoepi.iris.components.Position;
import eu.ecoepi.iris.components.TickAbundance;

import java.io.FileWriter;
import java.io.IOException;

@All({TickAbundance.class, Position.class})
public class AbundanceWriter extends IteratingSystem {

    ComponentMapper<TickAbundance> abundanceMapper;
    ComponentMapper<Position> positionMapper;

    private FileWriter csvWriter;

    @Wire
    TimeStep timeStep;

    public AbundanceWriter() {
        {
            try {
                csvWriter = new FileWriter("iris_abundance.csv");
                csvWriter.write("tick,x,y,larvae,nymphs,adults\n");
                csvWriter.flush();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    protected void process(int entityId) {
        var abundance = abundanceMapper.get(entityId);
        var position = positionMapper.get(entityId);

        try {
            csvWriter.write(Integer.toString(timeStep.getCurrent()));
            csvWriter.write(",");
            csvWriter.write(Integer.toString(position.getX()));
            csvWriter.write(",");
            csvWriter.write(Integer.toString(position.getY()));
            csvWriter.write(",");
            csvWriter.write(Integer.toString(abundance.getLarvae()));
            csvWriter.write(",");
            csvWriter.write(Integer.toString(abundance.getNymphs()));
            csvWriter.write(",");
            csvWriter.write(Integer.toString(abundance.getAdults()));
            csvWriter.write("\n");
            csvWriter.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
