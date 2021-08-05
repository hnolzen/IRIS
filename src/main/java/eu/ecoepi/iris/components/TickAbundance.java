package eu.ecoepi.iris.components;

import com.artemis.Component;
import eu.ecoepi.iris.CohortState;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class TickAbundance extends Component {

    final Map<CohortState, Integer> abundance = new HashMap<>();

    int feedingEventsLarvae = 0;
    int feedingEventsNymphs = 0;
    int feedingEventsAdults = 0;

    public TickAbundance() {
    }

    public TickAbundance(int larvae, int nymphs, int adults,
                         int inactiveLarvae, int inactiveNymphs, int inactiveAdults,
                         int engorgedLarvae, int engorgedNymphs, int engorgedAdults,
                         int lateEngorgedLarvae, int lateEngorgedNymphs,
                         int infectedLarvae, int infectedNymphs,
                         int infectedInactiveLarvae, int infectedInactiveNymphs,
                         int infectedEngorgedLarvae, int infectedEngorgedNymphs,
                         int infectedLateEngorgedLarvae, int infectedLateEngorgedNymphs) {
        abundance.put(CohortState.LARVAE_QUESTING, larvae);
        abundance.put(CohortState.NYMPHS_QUESTING, nymphs);
        abundance.put(CohortState.ADULTS_QUESTING, adults);
        abundance.put(CohortState.LARVAE_INACTIVE, inactiveLarvae);
        abundance.put(CohortState.NYMPHS_INACTIVE, inactiveNymphs);
        abundance.put(CohortState.ADULTS_INACTIVE, inactiveAdults);
        abundance.put(CohortState.LARVAE_ENGORGED, engorgedLarvae);
        abundance.put(CohortState.NYMPHS_ENGORGED, engorgedNymphs);
        abundance.put(CohortState.ADULTS_ENGORGED, engorgedAdults);
        abundance.put(CohortState.LARVAE_LATE_ENGORGED, lateEngorgedLarvae);
        abundance.put(CohortState.NYMPHS_LATE_ENGORGED, lateEngorgedNymphs);
        abundance.put(CohortState.LARVAE_QUESTING_INFECTED, infectedLarvae);
        abundance.put(CohortState.NYMPHS_QUESTING_INFECTED, infectedNymphs);
        abundance.put(CohortState.LARVAE_INACTIVE_INFECTED, infectedInactiveLarvae);
        abundance.put(CohortState.NYMPHS_INACTIVE_INFECTED, infectedInactiveNymphs);
        abundance.put(CohortState.LARVAE_ENGORGED_INFECTED, infectedEngorgedLarvae);
        abundance.put(CohortState.NYMPHS_ENGORGED_INFECTED, infectedEngorgedNymphs);
        abundance.put(CohortState.LARVAE_LATE_ENGORGED_INFECTED, infectedLateEngorgedLarvae);
        abundance.put(CohortState.NYMPHS_LATE_ENGORGED_INFECTED, infectedLateEngorgedNymphs);
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

    public int getStage(CohortState stage) {
        return abundance.get(stage);
    }

    public int getLarvae() {
        return abundance.get(CohortState.LARVAE_QUESTING);
    }

    public int getInfectedLarvae() {
        return abundance.get(CohortState.LARVAE_QUESTING_INFECTED);
    }

    public int getNymphs() {
        return abundance.get(CohortState.NYMPHS_QUESTING);
    }

    public int getInfectedNymphs() {
        return abundance.get(CohortState.NYMPHS_QUESTING_INFECTED);
    }

    public int getAdults() {
        return abundance.get(CohortState.ADULTS_QUESTING);
    }

    public int getInactiveLarvae() {
        return abundance.get(CohortState.LARVAE_INACTIVE);
    }

    public int getInfectedInactiveLarvae() {
        return abundance.get(CohortState.LARVAE_INACTIVE_INFECTED);
    }

    public int getInactiveNymphs() {
        return abundance.get(CohortState.NYMPHS_INACTIVE);
    }

    public int getInfectedInactiveNymphs() {
        return abundance.get(CohortState.NYMPHS_INACTIVE_INFECTED);
    }

    public int getInactiveAdults() {
        return abundance.get(CohortState.ADULTS_INACTIVE);
    }

    public int getEngorgedLarvae() {
        return abundance.get(CohortState.LARVAE_ENGORGED);
    }

    public int getInfectedEngorgedLarvae() {
        return abundance.get(CohortState.LARVAE_ENGORGED_INFECTED);
    }

    public int getEngorgedNymphs() {
        return abundance.get(CohortState.NYMPHS_ENGORGED);
    }

    public int getInfectedEngorgedNymphs() {
        return abundance.get(CohortState.NYMPHS_ENGORGED_INFECTED);
    }

    public int getEngorgedAdults() {
        return abundance.get(CohortState.ADULTS_ENGORGED);
    }

    public int getLateEngorgedLarvae() {
        return abundance.get(CohortState.LARVAE_LATE_ENGORGED);
    }

    public int getInfectedLateEngorgedLarvae() {
        return abundance.get(CohortState.LARVAE_LATE_ENGORGED_INFECTED);
    }

    public int getLateEngorgedNymphs() {
        return abundance.get(CohortState.NYMPHS_LATE_ENGORGED);
    }

    public int getInfectedLateEngorgedNymphs() {
        return abundance.get(CohortState.NYMPHS_LATE_ENGORGED_INFECTED);
    }

    public void addStage(CohortState stage, int num) {
        abundance.compute(stage, (_stage, count) -> count + num);
    }

    public void addLarvae(int larvae) {
        abundance.compute(CohortState.LARVAE_QUESTING, (stage, count) -> count + larvae);
    }

    public void addInfectedLarvae(int larvae) {
        abundance.compute(CohortState.LARVAE_QUESTING_INFECTED, (stage, count) -> count + larvae);
    }

    public void addNymphs(int nymphs) {
        abundance.compute(CohortState.NYMPHS_QUESTING, (stage, count) -> count + nymphs);
    }

    public void addInfectedNymphs(int nymphs) {
        abundance.compute(CohortState.NYMPHS_QUESTING_INFECTED, (stage, count) -> count + nymphs);
    }

    public void addAdults(int adults) {
        abundance.compute(CohortState.ADULTS_QUESTING, (stage, count) -> count + adults);
    }

    public void addInactiveLarvae(int larvae) {
        abundance.compute(CohortState.LARVAE_INACTIVE, (stage, count) -> count + larvae);
    }

    public void addInfectedInactiveLarvae(int larvae) {
        abundance.compute(CohortState.LARVAE_INACTIVE_INFECTED, (stage, count) -> count + larvae);
    }

    public void addInactiveNymphs(int nymphs) {
        abundance.compute(CohortState.NYMPHS_INACTIVE, (stage, count) -> count + nymphs);
    }

    public void addInfectedInactiveNymphs(int nymphs) {
        abundance.compute(CohortState.NYMPHS_INACTIVE_INFECTED, (stage, count) -> count + nymphs);
    }

    public void addInactiveAdults(int adults) {
        abundance.compute(CohortState.ADULTS_INACTIVE, (stage, count) -> count + adults);
    }

    public void addEngorgedLarvae(int larvae) {
        abundance.compute(CohortState.LARVAE_ENGORGED, (stage, count) -> count + larvae);
    }

    public void addInfectedEngorgedLarvae(int larvae) {
        abundance.compute(CohortState.LARVAE_ENGORGED_INFECTED, (stage, count) -> count + larvae);
    }

    public void addEngorgedNymphs(int nymphs) {
        abundance.compute(CohortState.NYMPHS_ENGORGED, (stage, count) -> count + nymphs);
    }

    public void addInfectedEngorgedNymphs(int nymphs) {
        abundance.compute(CohortState.NYMPHS_ENGORGED_INFECTED, (stage, count) -> count + nymphs);
    }

    public void addEngorgedAdults(int adults) {
        abundance.compute(CohortState.ADULTS_ENGORGED, (stage, count) -> count + adults);
    }

    public void addLateEngorgedLarvae(int larvae) {
        abundance.compute(CohortState.LARVAE_LATE_ENGORGED, (stage, count) -> count + larvae);
    }

    public void addInfectedLateEngorgedLarvae(int larvae) {
        abundance.compute(CohortState.LARVAE_LATE_ENGORGED_INFECTED, (stage, count) -> count + larvae);
    }

    public void addLateEngorgedNymphs(int nymphs) {
        abundance.compute(CohortState.NYMPHS_LATE_ENGORGED, (stage, count) -> count + nymphs);
    }

    public void addInfectedLateEngorgedNymphs(int nymphs) {
        abundance.compute(CohortState.NYMPHS_LATE_ENGORGED_INFECTED, (stage, count) -> count + nymphs);
    }

}
