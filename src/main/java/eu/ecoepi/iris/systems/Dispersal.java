package eu.ecoepi.iris.systems;

import com.artemis.ComponentMapper;
import com.artemis.annotations.All;
import com.artemis.annotations.Wire;
import com.artemis.systems.IteratingSystem;
import eu.ecoepi.iris.LifeCycleStage;
import eu.ecoepi.iris.Parameters;
import eu.ecoepi.iris.Randomness;
import eu.ecoepi.iris.SpatialIndex;
import eu.ecoepi.iris.components.Position;
import eu.ecoepi.iris.components.TickAbundance;
import org.apache.commons.math3.distribution.EnumeratedDistribution;
import org.apache.commons.math3.util.Pair;

import java.util.ArrayList;
import java.util.List;

@All({TickAbundance.class, Position.class})
public class Dispersal extends IteratingSystem {

    ComponentMapper<TickAbundance> abundanceMapper;
    ComponentMapper<Position> positionMapper;

    @Wire
    SpatialIndex index;

    @Wire
    Randomness randomness;

    @Override
    protected void process(int entityId) {
        var abundance = abundanceMapper.get(entityId);
        var position = positionMapper.get(entityId);

        final List<Pair<Integer, Double>> distanceProbabilities = new ArrayList<>();

        distanceProbabilities.add(new Pair<>(1, 0.25));
        distanceProbabilities.add(new Pair<>(2, 0.25));
        distanceProbabilities.add(new Pair<>(3, 0.20));
        distanceProbabilities.add(new Pair<>(4, 0.15));
        distanceProbabilities.add(new Pair<>(5, 0.05));
        distanceProbabilities.add(new Pair<>(6, 0.04));
        distanceProbabilities.add(new Pair<>(7, 0.03));
        distanceProbabilities.add(new Pair<>(8, 0.02));
        distanceProbabilities.add(new Pair<>(9, 0.01));

        final EnumeratedDistribution<Integer> distribution = new EnumeratedDistribution<>(distanceProbabilities);

        while (true) {
            var x = distribution.sample();
            var y = distribution.sample();
            if (randomness.random() < 0.5) {
                x = -x;
            }
            if (randomness.random() < 0.5) {
                y = -y;
            }

            var neighbourToRandom = index.lookUp(position.moveBy(x, y));
            if (neighbourToRandom.isPresent()) {
                var abundanceToRandom = abundanceMapper.get(neighbourToRandom.get());
                processPair(abundance, abundanceToRandom);
                break;
            }
        }
    }

    void processPair(TickAbundance abundance, TickAbundance neighbour) {
        var movingLarvae = randomness.roundRandom(abundance.getStage(LifeCycleStage.LARVAE) * Parameters.DISPERSAL_RATE.get(LifeCycleStage.LARVAE));
        var movingNymphs = randomness.roundRandom(abundance.getStage(LifeCycleStage.NYMPH) * Parameters.DISPERSAL_RATE.get(LifeCycleStage.NYMPH));
        var movingAdults = randomness.roundRandom(abundance.getStage(LifeCycleStage.ADULT) * Parameters.DISPERSAL_RATE.get(LifeCycleStage.ADULT));

        abundance.addLarvae(-movingLarvae);
        neighbour.addFedLarvae(movingLarvae);

        abundance.addNymphs(-movingNymphs);
        neighbour.addFedNymphs(movingNymphs);

        abundance.addAdults(-movingAdults);
        neighbour.addFedAdults(movingAdults);
    }
}
