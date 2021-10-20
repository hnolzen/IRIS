package eu.ecoepi.iris.components;

import com.artemis.Component;
import eu.ecoepi.iris.CohortStateTicks;
import eu.ecoepi.iris.resources.Randomness;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class TickAbundance extends Component {

    final Map<CohortStateTicks, Integer> abundance = new HashMap<>();
    final Map<CohortStateTicks, Integer> feeding = new HashMap<>();

    /*
    The following two variables are mainly helper variables for observation to distinguish
    between feeding events of already infected and newly infected larvae and nymphs.
    Therefore, they are not implemented as tick cohorts.
     */
    int feedingEventsNewInfectedLarvae = 0;
    int feedingEventsNewInfectedNymphs = 0;

    public TickAbundance() {
    }

    public TickAbundance(int inactiveLarvae,
                         int inactiveNymphs,
                         int inactiveAdults,
                         int infectedInactiveLarvae,
                         int infectedInactiveNymphs
                         ) {
        abundance.put(CohortStateTicks.LARVAE_INACTIVE, inactiveLarvae);
        abundance.put(CohortStateTicks.NYMPHS_INACTIVE, inactiveNymphs);
        abundance.put(CohortStateTicks.ADULTS_INACTIVE, inactiveAdults);
        abundance.put(CohortStateTicks.LARVAE_INACTIVE_INFECTED, infectedInactiveLarvae);
        abundance.put(CohortStateTicks.NYMPHS_INACTIVE_INFECTED, infectedInactiveNymphs);
        abundance.put(CohortStateTicks.LARVAE_QUESTING, 0);
        abundance.put(CohortStateTicks.NYMPHS_QUESTING, 0);
        abundance.put(CohortStateTicks.ADULTS_QUESTING, 0);
        abundance.put(CohortStateTicks.LARVAE_QUESTING_INFECTED, 0);
        abundance.put(CohortStateTicks.NYMPHS_QUESTING_INFECTED, 0);
        abundance.put(CohortStateTicks.LARVAE_ENGORGED, 0);
        abundance.put(CohortStateTicks.NYMPHS_ENGORGED, 0);
        abundance.put(CohortStateTicks.ADULTS_ENGORGED, 0);
        abundance.put(CohortStateTicks.LARVAE_ENGORGED_INFECTED, 0);
        abundance.put(CohortStateTicks.NYMPHS_ENGORGED_INFECTED, 0);
        abundance.put(CohortStateTicks.LARVAE_LATE_ENGORGED, 0);
        abundance.put(CohortStateTicks.NYMPHS_LATE_ENGORGED, 0);
        abundance.put(CohortStateTicks.LARVAE_LATE_ENGORGED_INFECTED, 0);
        abundance.put(CohortStateTicks.NYMPHS_LATE_ENGORGED_INFECTED, 0);
        feeding.put(CohortStateTicks.LARVAE_QUESTING, 0);
        feeding.put(CohortStateTicks.LARVAE_QUESTING_INFECTED, 0);
        feeding.put(CohortStateTicks.NYMPHS_QUESTING, 0);
        feeding.put(CohortStateTicks.NYMPHS_QUESTING_INFECTED, 0);
        feeding.put(CohortStateTicks.ADULTS_QUESTING, 0);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TickAbundance that = (TickAbundance) o;
        return Objects.equals(abundance, that.abundance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(abundance);
    }

    public void addFeedingEventNewInfectedLarvae(int event) {
        feedingEventsNewInfectedLarvae += event;
    }

    public void addFeedingEventNewInfectedNymphs(int event) {
        feedingEventsNewInfectedNymphs += event;
    }

    public int getFeedingEventsNewInfectedLarvae() {
        return feedingEventsNewInfectedLarvae;
    }

    public int getFeedingEventsNewInfectedNymphs() {
        return feedingEventsNewInfectedNymphs;
    }

    public void addFeedingEvents(CohortStateTicks stage, int number) {
        feeding.compute(stage, (_stage, count) -> count + number);
    }

    public int getFeedingEvents(CohortStateTicks stage) {
        return feeding.get(stage);
    }

    public int getStage(CohortStateTicks stage) {
        return abundance.get(stage);
    }

    public int removeFromStage(CohortStateTicks stage, float rate, Randomness randomness) {
        var old = abundance.get(stage);
        var removed = randomness.roundRandom(old * rate);
        abundance.put(stage, old - removed);

        return removed;
    }

    public void addToStage(CohortStateTicks stage, int number) {
        abundance.compute(stage, (_stage, count) -> count + number);
    }

}
