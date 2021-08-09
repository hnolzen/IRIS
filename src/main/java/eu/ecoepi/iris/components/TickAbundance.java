package eu.ecoepi.iris.components;

import com.artemis.Component;
import eu.ecoepi.iris.CohortStateTicks;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class TickAbundance extends Component {

    final Map<CohortStateTicks, Integer> abundance = new HashMap<>();

    int feedingEventsLarvae = 0;
    int feedingEventsNymphs = 0;
    int feedingEventsAdults = 0;

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

    public void addFeedingEventLarvae(int event) {
        feedingEventsLarvae += event;
    }

    public void addFeedingEventNymphs(int event) {
        feedingEventsNymphs += event;
    }

    public void addFeedingEventAdults(int event) {
        feedingEventsAdults += event;
    }

    public int getFeedingEventsLarvae() {
        return feedingEventsLarvae;
    }

    public int getFeedingEventsNymphs() {
        return feedingEventsNymphs;
    }

    public int getFeedingEventsAdults() {
        return feedingEventsNymphs;
    }

    public int getStage(CohortStateTicks stage) {
        return abundance.get(stage);
    }

    public int getQuestingLarvae() {
        return abundance.get(CohortStateTicks.LARVAE_QUESTING);
    }

    public int getInfectedQuestingLarvae() {
        return abundance.get(CohortStateTicks.LARVAE_QUESTING_INFECTED);
    }

    public int getQuestingNymphs() {
        return abundance.get(CohortStateTicks.NYMPHS_QUESTING);
    }

    public int getInfectedQuestingNymphs() {
        return abundance.get(CohortStateTicks.NYMPHS_QUESTING_INFECTED);
    }

    public int getQuestingAdults() {
        return abundance.get(CohortStateTicks.ADULTS_QUESTING);
    }

    public int getInactiveLarvae() {
        return abundance.get(CohortStateTicks.LARVAE_INACTIVE);
    }

    public int getInfectedInactiveLarvae() {
        return abundance.get(CohortStateTicks.LARVAE_INACTIVE_INFECTED);
    }

    public int getInactiveNymphs() {
        return abundance.get(CohortStateTicks.NYMPHS_INACTIVE);
    }

    public int getInfectedInactiveNymphs() {
        return abundance.get(CohortStateTicks.NYMPHS_INACTIVE_INFECTED);
    }

    public int getInactiveAdults() {
        return abundance.get(CohortStateTicks.ADULTS_INACTIVE);
    }

    public int getEngorgedLarvae() {
        return abundance.get(CohortStateTicks.LARVAE_ENGORGED);
    }

    public int getInfectedEngorgedLarvae() {
        return abundance.get(CohortStateTicks.LARVAE_ENGORGED_INFECTED);
    }

    public int getEngorgedNymphs() {
        return abundance.get(CohortStateTicks.NYMPHS_ENGORGED);
    }

    public int getInfectedEngorgedNymphs() {
        return abundance.get(CohortStateTicks.NYMPHS_ENGORGED_INFECTED);
    }

    public int getEngorgedAdults() {
        return abundance.get(CohortStateTicks.ADULTS_ENGORGED);
    }

    public int getLateEngorgedLarvae() {
        return abundance.get(CohortStateTicks.LARVAE_LATE_ENGORGED);
    }

    public int getInfectedLateEngorgedLarvae() {
        return abundance.get(CohortStateTicks.LARVAE_LATE_ENGORGED_INFECTED);
    }

    public int getLateEngorgedNymphs() {
        return abundance.get(CohortStateTicks.NYMPHS_LATE_ENGORGED);
    }

    public int getInfectedLateEngorgedNymphs() {
        return abundance.get(CohortStateTicks.NYMPHS_LATE_ENGORGED_INFECTED);
    }

    public void addStage(CohortStateTicks stage, int num) {
        abundance.compute(stage, (_stage, count) -> count + num);
    }

    public void addQuestingLarvae(int larvae) {
        abundance.compute(CohortStateTicks.LARVAE_QUESTING, (stage, count) -> count + larvae);
    }

    public void addInfectedQuestingLarvae(int larvae) {
        abundance.compute(CohortStateTicks.LARVAE_QUESTING_INFECTED, (stage, count) -> count + larvae);
    }

    public void addQuestingNymphs(int nymphs) {
        abundance.compute(CohortStateTicks.NYMPHS_QUESTING, (stage, count) -> count + nymphs);
    }

    public void addInfectedQuestingNymphs(int nymphs) {
        abundance.compute(CohortStateTicks.NYMPHS_QUESTING_INFECTED, (stage, count) -> count + nymphs);
    }

    public void addQuestingAdults(int adults) {
        abundance.compute(CohortStateTicks.ADULTS_QUESTING, (stage, count) -> count + adults);
    }

    public void addInactiveLarvae(int larvae) {
        abundance.compute(CohortStateTicks.LARVAE_INACTIVE, (stage, count) -> count + larvae);
    }

    public void addInfectedInactiveLarvae(int larvae) {
        abundance.compute(CohortStateTicks.LARVAE_INACTIVE_INFECTED, (stage, count) -> count + larvae);
    }

    public void addInactiveNymphs(int nymphs) {
        abundance.compute(CohortStateTicks.NYMPHS_INACTIVE, (stage, count) -> count + nymphs);
    }

    public void addInfectedInactiveNymphs(int nymphs) {
        abundance.compute(CohortStateTicks.NYMPHS_INACTIVE_INFECTED, (stage, count) -> count + nymphs);
    }

    public void addInactiveAdults(int adults) {
        abundance.compute(CohortStateTicks.ADULTS_INACTIVE, (stage, count) -> count + adults);
    }

    public void addEngorgedLarvae(int larvae) {
        abundance.compute(CohortStateTicks.LARVAE_ENGORGED, (stage, count) -> count + larvae);
    }

    public void addInfectedEngorgedLarvae(int larvae) {
        abundance.compute(CohortStateTicks.LARVAE_ENGORGED_INFECTED, (stage, count) -> count + larvae);
    }

    public void addEngorgedNymphs(int nymphs) {
        abundance.compute(CohortStateTicks.NYMPHS_ENGORGED, (stage, count) -> count + nymphs);
    }

    public void addInfectedEngorgedNymphs(int nymphs) {
        abundance.compute(CohortStateTicks.NYMPHS_ENGORGED_INFECTED, (stage, count) -> count + nymphs);
    }

    public void addEngorgedAdults(int adults) {
        abundance.compute(CohortStateTicks.ADULTS_ENGORGED, (stage, count) -> count + adults);
    }

    public void addLateEngorgedLarvae(int larvae) {
        abundance.compute(CohortStateTicks.LARVAE_LATE_ENGORGED, (stage, count) -> count + larvae);
    }

    public void addInfectedLateEngorgedLarvae(int larvae) {
        abundance.compute(CohortStateTicks.LARVAE_LATE_ENGORGED_INFECTED, (stage, count) -> count + larvae);
    }

    public void addLateEngorgedNymphs(int nymphs) {
        abundance.compute(CohortStateTicks.NYMPHS_LATE_ENGORGED, (stage, count) -> count + nymphs);
    }

    public void addInfectedLateEngorgedNymphs(int nymphs) {
        abundance.compute(CohortStateTicks.NYMPHS_LATE_ENGORGED_INFECTED, (stage, count) -> count + nymphs);
    }

}
