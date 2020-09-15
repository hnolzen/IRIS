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

    final EnumeratedDistribution<Integer> distribution;

    @Wire
    SpatialIndex index;

    @Wire
    Randomness randomness;

    public Dispersal() {
        final List<Pair<Integer, Double>> distanceProbabilities = new ArrayList<>();

        for (int i = 0, n = Parameters.DISTANCE_PROB.length; i < n; ++i) {
            distanceProbabilities.add(new Pair<>(i + 1, Parameters.DISTANCE_PROB[i]));
        }

        for (int i = 0, n = distanceProbabilities.size(); i < n; ++i) {
            var distance = distanceProbabilities.get(i);
            distanceProbabilities.add(new Pair<>(-distance.getFirst(), distance.getSecond()));
        }

        distribution = new EnumeratedDistribution<>(distanceProbabilities);
    }

    @Override
    protected void process(int entityId) {
        var abundance = abundanceMapper.get(entityId);
        var position = positionMapper.get(entityId);

        {
            var x = distribution.sample();
            var y = distribution.sample();
            var neighbourToRandom = index.lookUp(position.moveBy(x, y));
            var abundanceToRandom = abundanceMapper.get(neighbourToRandom.get());
            var movingLarvae = randomness.roundRandom(abundance.getStage(LifeCycleStage.LARVAE) * Parameters.DISPERSAL_RATE.get(LifeCycleStage.LARVAE));
            abundance.addLarvae(-movingLarvae);
            abundanceToRandom.addFedLarvae(movingLarvae);
        }

        {
            var x = distribution.sample();
            var y = distribution.sample();
            var neighbourToRandom = index.lookUp(position.moveBy(x, y));
            var abundanceToRandom = abundanceMapper.get(neighbourToRandom.get());
            var movingNymphs = randomness.roundRandom(abundance.getStage(LifeCycleStage.NYMPH) * Parameters.DISPERSAL_RATE.get(LifeCycleStage.NYMPH));
            abundance.addNymphs(-movingNymphs);
            abundanceToRandom.addFedNymphs(movingNymphs);
        }

        {
            var x = distribution.sample();
            var y = distribution.sample();
            var neighbourToRandom = index.lookUp(position.moveBy(x, y));
            var abundanceToRandom = abundanceMapper.get(neighbourToRandom.get());
            var movingAdults = randomness.roundRandom(abundance.getStage(LifeCycleStage.ADULT) * Parameters.DISPERSAL_RATE.get(LifeCycleStage.ADULT));
            abundance.addAdults(-movingAdults);
            abundanceToRandom.addFedAdults(movingAdults);
        }
    }
}
