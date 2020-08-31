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

        // Larvae dispersal
        var foundLarvaeDispersalNeighbour = false;
        while (!foundLarvaeDispersalNeighbour) {
            var randomXDirection = 0;
            var randX = randomness.random();
            if (randX < 0.5) {
                randomXDirection = 1;
            } else if (randX < 0.75){
                randomXDirection = 2;
            } else {
                randomXDirection = 3;
            }

            var randomYDirection = 0;
            var randY = randomness.random();
            if (randY < 0.5) {
                randomYDirection = 1;
            } else if (randY < 0.75){
                randomYDirection = 2;
            } else {
                randomYDirection = 3;
            }

            if (randomness.random() < 0.5) {
                randomXDirection = - randomXDirection;
            }

            if (randomness.random() < 0.5) {
                randomYDirection = - randomYDirection;
            }

            var neighbourToRandom = index.lookUp(position.moveBy(randomXDirection, randomYDirection));
            if (neighbourToRandom.isPresent()) {
                foundLarvaeDispersalNeighbour = true;
                var abundanceToRandom = abundanceMapper.get(neighbourToRandom.get());
                processPair(abundance, abundanceToRandom, LifeCycleStage.LARVAE);
            }
        }

        // Nymph dispersal
        var foundNymphDispersalNeighbour = false;
        while (!foundNymphDispersalNeighbour) {
            var randomXDirection = 0;
            var randX = randomness.random();
            if (randX < 0.5) {
                randomXDirection = 1;
            } else if (randX < 0.75) {
                randomXDirection = 2;
            } else if (randX < 0.90) {
                randomXDirection = 3;
            } else if (randX < 0.95) {
                randomXDirection = 4;
            } else {
                randomXDirection = 5;
            }

            var randomYDirection = 0;
            var randY = randomness.random();
            if (randY < 0.5) {
                randomYDirection = 1;
            } else if (randY < 0.75) {
                randomYDirection = 2;
            } else if (randY < 0.90) {
                randomYDirection = 3;
            } else if (randY < 0.95) {
                randomYDirection = 4;
            } else {
                randomYDirection = 5;
            }

            if (randomness.random() < 0.5) {
                randomXDirection = - randomXDirection;
            }

            if (randomness.random() < 0.5) {
                randomYDirection = - randomYDirection;
            }

            var neighbourToRandom = index.lookUp(position.moveBy(randomXDirection, randomYDirection));
            if (neighbourToRandom.isPresent()) {
                foundNymphDispersalNeighbour = true;
                var abundanceToRandom = abundanceMapper.get(neighbourToRandom.get());
                processPair(abundance, abundanceToRandom, LifeCycleStage.NYMPH);
            }
        }

        // Adult dispersal
        var foundAdultDispersalNeighbour = false;
        while (!foundAdultDispersalNeighbour) {
            var randomXDirection = 0;
            var randX = randomness.random();
            if (randX < 0.5) {
                randomXDirection = 1;
            } else if (randX < 0.75) {
                randomXDirection = 2;
            } else if (randX < 0.90) {
                randomXDirection = 3;
            } else if (randX < 0.95) {
                randomXDirection = 4;
            } else {
                randomXDirection = 5;
            }

            var randomYDirection = 0;
            var randY = randomness.random();
            if (randY < 0.5) {
                randomYDirection = 1;
            } else if (randY < 0.60) {
                randomYDirection = 2;
            } else if (randY < 0.75) {
                randomYDirection = 3;
            } else if (randY < 0.90) {
                randomYDirection = 4;
            } else {
                randomYDirection = 5;
            }

            if (randomness.random() < 0.5) {
                randomXDirection = - randomXDirection;
            }

            if (randomness.random() < 0.5) {
                randomYDirection = - randomYDirection;
            }

            var neighbourToRandom = index.lookUp(position.moveBy(randomXDirection, randomYDirection));
            if (neighbourToRandom.isPresent()) {
                foundAdultDispersalNeighbour = true;
                var abundanceToRandom = abundanceMapper.get(neighbourToRandom.get());
                processPair(abundance, abundanceToRandom, LifeCycleStage.ADULT);
            }
        }
    }

    void processPair(TickAbundance abundance, TickAbundance neighbour, LifeCycleStage stage) {
//        Parameters.DISPERSAL_RATE.forEach((stage, rate) -> {
//            var moving = (int) (abundance.getStage(stage) * rate);
//            abundance.addStage(stage, -moving);
//            neighbour.addStage(stage, moving);
//        });

        var moving = Math.round(abundance.getStage(stage) * Parameters.DISPERSAL_RATE.get(stage));

        switch (stage) {
            case LARVAE:
                abundance.addLarvae(-moving);
                neighbour.addFedLarvae(moving);
                break;
            case NYMPH:
                abundance.addNymphs(-moving);
                neighbour.addFedNymphs(moving);
                break;
            case ADULT:
                abundance.addAdults(-moving);
                neighbour.addFedAdults(moving);
                break;
        }

    }
}
