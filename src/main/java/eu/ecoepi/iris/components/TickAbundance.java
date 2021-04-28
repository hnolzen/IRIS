package eu.ecoepi.iris.components;

import com.artemis.Component;
import eu.ecoepi.iris.LifeCycleStage;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class TickAbundance extends Component {

    final Map<LifeCycleStage, Integer> abundance = new HashMap<>();
    final Map<LifeCycleStage, Integer> infected = new HashMap<>();

    int feedingEventsLarvae = 0;
    int feedingEventsNymphs = 0;
    int feedingEventsAdults = 0;

    public TickAbundance() {
    }

    public TickAbundance(int larvae, int nymphs, int adults,
                         int inactiveLarvae, int inactiveNymphs, int inactiveAdults,
                         int engorgedLarvae, int engorgedNymphs, int engorgedAdults,
                         int lateEngorgedLarvae, int lateEngorgedNymphs,
                         int infectedLarvae, int infectedNymphs, int infectedAdults) {
        abundance.put(LifeCycleStage.LARVAE_QUESTING, larvae);
        abundance.put(LifeCycleStage.NYMPHS_QUESTING, nymphs);
        abundance.put(LifeCycleStage.ADULTS_QUESTING, adults);
        abundance.put(LifeCycleStage.LARVAE_INACTIVE, inactiveLarvae);
        abundance.put(LifeCycleStage.NYMPHS_INACTIVE, inactiveNymphs);
        abundance.put(LifeCycleStage.ADULTS_INACTIVE, inactiveAdults);
        abundance.put(LifeCycleStage.LARVAE_ENGORGED, engorgedLarvae);
        abundance.put(LifeCycleStage.NYMPHS_ENGORGED, engorgedNymphs);
        abundance.put(LifeCycleStage.ADULTS_ENGORGED, engorgedAdults);
        abundance.put(LifeCycleStage.LARVAE_LATE_ENGORGED, lateEngorgedLarvae);
        abundance.put(LifeCycleStage.NYMPHS_LATE_ENGORGED, lateEngorgedNymphs);
        infected.put(LifeCycleStage.LARVAE_QUESTING, infectedLarvae);
        infected.put(LifeCycleStage.NYMPHS_QUESTING, infectedNymphs);
        infected.put(LifeCycleStage.ADULTS_QUESTING, infectedAdults);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TickAbundance that = (TickAbundance) o;
        return Objects.equals(abundance, that.abundance) &&
                Objects.equals(infected, that.infected);
    }

    @Override
    public int hashCode() {
        return Objects.hash(abundance, infected);
    }

    public void addFeedingEventLarvae(int event){
        feedingEventsLarvae += event;
    }

    public void addFeedingEventNymphs(int event){
        feedingEventsNymphs += event;
    }

    public void addFeedingEventAdults(int event){
        feedingEventsAdults += event;
    }

    public int getFeedingEventsLarvae(){
        return feedingEventsLarvae;
    }

    public int getFeedingEventsNymphs(){
        return feedingEventsNymphs;
    }

    public int getFeedingEventsAdults(){
        return feedingEventsNymphs;
    }

    public int getStage(LifeCycleStage stage) {
        return abundance.get(stage);
    }

    public int getLarvae() {
        return abundance.get(LifeCycleStage.LARVAE_QUESTING);
    }

    public int getNymphs() {
        return abundance.get(LifeCycleStage.NYMPHS_QUESTING);
    }

    public int getAdults() {
        return abundance.get(LifeCycleStage.ADULTS_QUESTING);
    }

    public int getInactiveLarvae() {
        return abundance.get(LifeCycleStage.LARVAE_INACTIVE);
    }

    public int getInactiveNymphs() {
        return abundance.get(LifeCycleStage.NYMPHS_INACTIVE);
    }

    public int getInactiveAdults() {
        return abundance.get(LifeCycleStage.ADULTS_INACTIVE);
    }

    public int getEngorgedLarvae() {
        return abundance.get(LifeCycleStage.LARVAE_ENGORGED);
    }

    public int getEngorgedNymphs() {
        return abundance.get(LifeCycleStage.NYMPHS_ENGORGED);
    }

    public int getEngorgedAdults() {
        return abundance.get(LifeCycleStage.ADULTS_ENGORGED);
    }

    public int getLateEngorgedLarvae() {
        return abundance.get(LifeCycleStage.LARVAE_LATE_ENGORGED);
    }

    public int getLateEngorgedNymphs() {
        return abundance.get(LifeCycleStage.NYMPHS_LATE_ENGORGED);
    }

    public int getInfectedLarvae() {
        return infected.get(LifeCycleStage.LARVAE_QUESTING);
    }

    public int getInfectedNymphs() {
        return infected.get(LifeCycleStage.NYMPHS_QUESTING);
    }

    public int getInfectedAdults() {
        return infected.get(LifeCycleStage.ADULTS_QUESTING);
    }

    public void addStage(LifeCycleStage stage, int num) {
        abundance.compute(stage, (_stage, count) -> count + num);
    }

    public void addLarvae(int larvae) {
        abundance.compute(LifeCycleStage.LARVAE_QUESTING, (stage, count) -> count + larvae);
    }

    public void addNymphs(int nymphs) {
        abundance.compute(LifeCycleStage.NYMPHS_QUESTING, (stage, count) -> count + nymphs);
    }

    public void addAdults(int adults) {
        abundance.compute(LifeCycleStage.ADULTS_QUESTING, (stage, count) -> count + adults);
    }

    public void addInactiveLarvae(int larvae) {
        abundance.compute(LifeCycleStage.LARVAE_INACTIVE, (stage, count) -> count + larvae);
    }

    public void addInactiveNymphs(int nymphs) {
        abundance.compute(LifeCycleStage.NYMPHS_INACTIVE, (stage, count) -> count + nymphs);
    }

    public void addInactiveAdults(int adults) {
        abundance.compute(LifeCycleStage.ADULTS_INACTIVE, (stage, count) -> count + adults);
    }

    public void addEngorgedLarvae(int larvae) {
        abundance.compute(LifeCycleStage.LARVAE_ENGORGED, (stage, count) -> count + larvae);
    }

    public void addEngorgedNymphs(int nymphs) {
        abundance.compute(LifeCycleStage.NYMPHS_ENGORGED, (stage, count) -> count + nymphs);
    }

    public void addEngorgedAdults(int adults) {
        abundance.compute(LifeCycleStage.ADULTS_ENGORGED, (stage, count) -> count + adults);
    }

    public void addLateEngorgedLarvae(int larvae) {
        abundance.compute(LifeCycleStage.LARVAE_LATE_ENGORGED, (stage, count) -> count + larvae);
    }

    public void addLateEngorgedNymphs(int nymphs) {
        abundance.compute(LifeCycleStage.NYMPHS_LATE_ENGORGED, (stage, count) -> count + nymphs);
    }

    public void addInfectedLarvae(int larvae) {
        infected.compute(LifeCycleStage.LARVAE_QUESTING, (stage, count) -> count + larvae);
    }

    public void addInfectedNymphs(int nymphs) {
        infected.compute(LifeCycleStage.NYMPHS_QUESTING, (stage, count) -> count + nymphs);
    }

    public void addInfectedAdults(int adults) {
        infected.compute(LifeCycleStage.ADULTS_QUESTING, (stage, count) -> count + adults);
    }
}
