package eu.ecoepi.iris.systems;

import com.artemis.ComponentMapper;
import com.artemis.annotations.All;
import com.artemis.annotations.Wire;
import com.artemis.systems.IteratingSystem;
import eu.ecoepi.iris.*;
import eu.ecoepi.iris.components.Position;
import eu.ecoepi.iris.components.TickAbundance;
import org.apache.commons.math3.distribution.EnumeratedDistribution;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.util.Pair;

import java.util.ArrayList;
import java.util.List;

@All({TickAbundance.class, Position.class})
public class Feeding extends IteratingSystem {

    ComponentMapper<TickAbundance> abundanceMapper;
    ComponentMapper<Position> positionMapper;

    final EnumeratedDistribution<Integer> distribution;

    @Wire
    TimeStep timestep;

    @Wire
    SpatialIndex index;

    @Wire
    Randomness randomness;

    public Feeding(RandomGenerator rng) {
        final List<Pair<Integer, Double>> distanceProbabilities = new ArrayList<>();

        for (int i = 0, n = Parameters.DISTANCE_PROB.length; i < n; ++i) {
            distanceProbabilities.add(new Pair<>(i + 1, Parameters.DISTANCE_PROB[i]));
        }

        for (int i = 0, n = distanceProbabilities.size(); i < n; ++i) {
            var distance = distanceProbabilities.get(i);
            distanceProbabilities.add(new Pair<>(-distance.getFirst(), distance.getSecond()));
        }

        distribution = new EnumeratedDistribution<>(rng, distanceProbabilities);
    }

    @Override
    protected void process(int entityId) {
        var abundance = abundanceMapper.get(entityId);
        var position = positionMapper.get(entityId);

        var lateFeeding = timestep.getCurrent() >= Parameters.LATE_FEEDING_TIME;

        {
            var x = distribution.sample();
            var y = distribution.sample();
            var neighbourToRandom = index.lookUp(position.moveBy(x, y));
            var abundanceToRandom = abundanceMapper.get(neighbourToRandom.get());
            var feedingLarvae = randomness.roundRandom(abundance.getStage(LifeCycleStage.LARVAE) * Parameters.FEEDING_RATE.get(LifeCycleStage.LARVAE));
            abundance.addLarvae(-feedingLarvae);
            if (lateFeeding) {
                abundanceToRandom.addLateEngorgedLarvae(feedingLarvae);
            } else {
                abundanceToRandom.addEngorgedLarvae(feedingLarvae);
            }
            abundance.addFeedingEventLarvae(feedingLarvae);
        }

        {
            var x = distribution.sample();
            var y = distribution.sample();
            var neighbourToRandom = index.lookUp(position.moveBy(x, y));
            var abundanceToRandom = abundanceMapper.get(neighbourToRandom.get());
            var feedingNymphs = randomness.roundRandom(abundance.getStage(LifeCycleStage.NYMPH) * Parameters.FEEDING_RATE.get(LifeCycleStage.NYMPH));
            abundance.addNymphs(-feedingNymphs);
            if (lateFeeding) {
                abundanceToRandom.addLateEngorgedNymphs(feedingNymphs);
            } else {
                abundanceToRandom.addEngorgedNymphs(feedingNymphs);
            }
            abundance.addFeedingEventNymphs(feedingNymphs);
        }

        {
            var x = distribution.sample();
            var y = distribution.sample();
            var neighbourToRandom = index.lookUp(position.moveBy(x, y));
            var abundanceToRandom = abundanceMapper.get(neighbourToRandom.get());
            var feedingAdults = randomness.roundRandom(abundance.getStage(LifeCycleStage.ADULT) * Parameters.FEEDING_RATE.get(LifeCycleStage.ADULT));
            abundance.addAdults(-feedingAdults);
            abundanceToRandom.addEngorgedAdults(feedingAdults);
            abundance.addFeedingEventAdults(feedingAdults);
        }
    }
}
