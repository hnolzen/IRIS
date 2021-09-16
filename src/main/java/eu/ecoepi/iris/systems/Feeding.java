package eu.ecoepi.iris.systems;

import com.artemis.ComponentMapper;
import com.artemis.annotations.All;
import com.artemis.annotations.Wire;
import com.artemis.systems.IteratingSystem;
import eu.ecoepi.iris.*;
import eu.ecoepi.iris.components.HostAbundance;
import eu.ecoepi.iris.components.Position;
import eu.ecoepi.iris.components.TickAbundance;
import eu.ecoepi.iris.resources.Parameters;
import eu.ecoepi.iris.resources.Randomness;
import eu.ecoepi.iris.resources.SpatialIndex;
import eu.ecoepi.iris.resources.TimeStep;
import org.apache.commons.math3.distribution.EnumeratedDistribution;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.util.Pair;

import java.util.ArrayList;
import java.util.List;

@All({TickAbundance.class, HostAbundance.class, Position.class})
public class Feeding extends IteratingSystem {

    ComponentMapper<TickAbundance> tickAbundanceMapper;
    ComponentMapper<HostAbundance> hostAbundanceMapper;
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
        var tickAbundance = tickAbundanceMapper.get(entityId);
        var hostAbundance = hostAbundanceMapper.get(entityId);
        var position = positionMapper.get(entityId);

        var lateFeeding = timestep.getCurrent() >= Parameters.LATE_FEEDING_TIME;

        {
            var x = distribution.sample();
            var y = distribution.sample();
            var neighbourToRandom = index.lookUp(position.moveBy(x, y));
            var abundanceToRandom = tickAbundanceMapper.get(neighbourToRandom.get());
            var feedingLarvae = randomness.roundRandom(tickAbundance.getStage(CohortStateTicks.LARVAE_QUESTING) * Parameters.FEEDING_RATE.get(CohortStateTicks.LARVAE_QUESTING));
            var feedingInfectedLarvae =
                    randomness.roundRandom(tickAbundance.getStage(CohortStateTicks.LARVAE_QUESTING_INFECTED) * Parameters.FEEDING_RATE.get(CohortStateTicks.LARVAE_QUESTING_INFECTED));

            var newInfectedLarvae = randomness.roundRandom(Parameters.INFECTION_PROBABILITY * feedingLarvae * hostAbundance.getInfectedRodents());
            feedingLarvae = feedingLarvae - newInfectedLarvae;
            feedingInfectedLarvae = feedingInfectedLarvae + newInfectedLarvae;

            tickAbundance.addQuestingLarvae(-feedingLarvae);
            tickAbundance.addInfectedQuestingLarvae(-feedingInfectedLarvae);

            if (lateFeeding) {
                abundanceToRandom.addLateEngorgedLarvae(feedingLarvae);
                abundanceToRandom.addInfectedLateEngorgedLarvae(feedingInfectedLarvae);
            } else {
                abundanceToRandom.addEngorgedLarvae(feedingLarvae);
                abundanceToRandom.addInfectedEngorgedLarvae(feedingInfectedLarvae);
            }
            tickAbundance.addFeedingEventLarvae(feedingLarvae + feedingInfectedLarvae);
        }

        {
            var x = distribution.sample();
            var y = distribution.sample();
            var neighbourToRandom = index.lookUp(position.moveBy(x, y));
            var abundanceToRandom = tickAbundanceMapper.get(neighbourToRandom.get());
            var feedingNymphs = randomness.roundRandom(tickAbundance.getStage(CohortStateTicks.NYMPHS_QUESTING) * Parameters.FEEDING_RATE.get(CohortStateTicks.NYMPHS_QUESTING));
            var feedingInfectedNymphs =
                    randomness.roundRandom(tickAbundance.getStage(CohortStateTicks.NYMPHS_QUESTING_INFECTED) * Parameters.FEEDING_RATE.get(CohortStateTicks.NYMPHS_QUESTING_INFECTED));

            var newInfectedNymphs = randomness.roundRandom(Parameters.INFECTION_PROBABILITY * feedingNymphs * hostAbundance.getInfectedRodents());
            feedingNymphs = feedingNymphs - newInfectedNymphs;
            feedingInfectedNymphs = feedingInfectedNymphs + newInfectedNymphs;

            tickAbundance.addQuestingNymphs(-feedingNymphs);
            tickAbundance.addInfectedQuestingNymphs(-feedingInfectedNymphs);

            if (lateFeeding) {
                abundanceToRandom.addLateEngorgedNymphs(feedingNymphs);
                abundanceToRandom.addInfectedLateEngorgedNymphs(feedingInfectedNymphs);
            } else {
                abundanceToRandom.addEngorgedNymphs(feedingNymphs);
                abundanceToRandom.addInfectedEngorgedNymphs(feedingInfectedNymphs);
            }
            tickAbundance.addFeedingEventNymphs(feedingNymphs + feedingInfectedNymphs);

            var newInfectedRodents = randomness.roundRandom(Parameters.INFECTION_PROBABILITY * feedingInfectedNymphs * hostAbundance.getRodents());
            hostAbundance.addRodents(-newInfectedRodents);
            hostAbundance.addInfectedRodents(newInfectedRodents);
        }

        {
            var x = distribution.sample();
            var y = distribution.sample();
            var neighbourToRandom = index.lookUp(position.moveBy(x, y));
            var abundanceToRandom = tickAbundanceMapper.get(neighbourToRandom.get());
            var feedingAdults = randomness.roundRandom(tickAbundance.getStage(CohortStateTicks.ADULTS_QUESTING) * Parameters.FEEDING_RATE.get(CohortStateTicks.ADULTS_QUESTING));

            tickAbundance.addQuestingAdults(-feedingAdults);
            abundanceToRandom.addEngorgedAdults(feedingAdults);
            tickAbundance.addFeedingEventAdults(feedingAdults);
        }
    }
}
