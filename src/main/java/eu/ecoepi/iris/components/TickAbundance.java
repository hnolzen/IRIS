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
                         int fedLarvae, int fedNymphs, int fedAdults,
                         int infectedLarvae, int infectedNymphs, int infectedAdults) {
        abundance.put(LifeCycleStage.LARVAE, larvae);
        abundance.put(LifeCycleStage.NYMPH, nymphs);
        abundance.put(LifeCycleStage.ADULT, adults);
        abundance.put(LifeCycleStage.LARVAE_INACTIVE, inactiveLarvae);
        abundance.put(LifeCycleStage.NYMPH_INACTIVE, inactiveNymphs);
        abundance.put(LifeCycleStage.ADULT_INACTIVE, inactiveAdults);
        abundance.put(LifeCycleStage.LARVAE_FED, fedLarvae);
        abundance.put(LifeCycleStage.NYMPH_FED, fedNymphs);
        abundance.put(LifeCycleStage.ADULT_FED, fedAdults);
        infected.put(LifeCycleStage.LARVAE, infectedLarvae);
        infected.put(LifeCycleStage.NYMPH, infectedNymphs);
        infected.put(LifeCycleStage.ADULT, infectedAdults);
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
        return abundance.get(LifeCycleStage.LARVAE);
    }

    public int getNymphs() {
        return abundance.get(LifeCycleStage.NYMPH);
    }

    public int getAdults() {
        return abundance.get(LifeCycleStage.ADULT);
    }

    public int getInactiveLarvae() {
        return abundance.get(LifeCycleStage.LARVAE_INACTIVE);
    }

    public int getInactiveNymphs() {
        return abundance.get(LifeCycleStage.NYMPH_INACTIVE);
    }

    public int getInactiveAdults() {
        return abundance.get(LifeCycleStage.ADULT_INACTIVE);
    }

    public int getFedLarvae() {
        return abundance.get(LifeCycleStage.LARVAE_FED);
    }

    public int getFedNymphs() {
        return abundance.get(LifeCycleStage.NYMPH_FED);
    }

    public int getFedAdults() {
        return abundance.get(LifeCycleStage.ADULT_FED);
    }

    public int getInfectedLarvae() {
        return infected.get(LifeCycleStage.LARVAE);
    }

    public int getInfectedNymphs() {
        return infected.get(LifeCycleStage.NYMPH);
    }

    public int getInfectedAdults() {
        return infected.get(LifeCycleStage.ADULT);
    }

    public void addStage(LifeCycleStage stage, int num) {
        abundance.compute(stage, (_stage, count) -> count + num);
    }

    public void addLarvae(int larvae) {
        abundance.compute(LifeCycleStage.LARVAE, (stage, count) -> count + larvae);
    }

    public void addNymphs(int nymphs) {
        abundance.compute(LifeCycleStage.NYMPH, (stage, count) -> count + nymphs);
    }

    public void addAdults(int adults) {
        abundance.compute(LifeCycleStage.ADULT, (stage, count) -> count + adults);
    }

    public void addInactiveLarvae(int larvae) {
        abundance.compute(LifeCycleStage.LARVAE_INACTIVE, (stage, count) -> count + larvae);
    }

    public void addInactiveNymphs(int nymphs) {
        abundance.compute(LifeCycleStage.NYMPH_INACTIVE, (stage, count) -> count + nymphs);
    }

    public void addInactiveAdults(int adults) {
        abundance.compute(LifeCycleStage.ADULT_INACTIVE, (stage, count) -> count + adults);
    }

    public void addFedLarvae(int larvae) {
        abundance.compute(LifeCycleStage.LARVAE_FED, (stage, count) -> count + larvae);
    }

    public void addFedNymphs(int nymphs) {
        abundance.compute(LifeCycleStage.NYMPH_FED, (stage, count) -> count + nymphs);
    }

    public void addFedAdults(int adults) {
        abundance.compute(LifeCycleStage.ADULT_FED, (stage, count) -> count + adults);
    }

    public void addInfectedLarvae(int larvae) {
        infected.compute(LifeCycleStage.LARVAE, (stage, count) -> count + larvae);
    }

    public void addInfectedNymphs(int nymphs) {
        infected.compute(LifeCycleStage.NYMPH, (stage, count) -> count + nymphs);
    }

    public void addInfectedAdults(int adults) {
        infected.compute(LifeCycleStage.ADULT, (stage, count) -> count + adults);
    }
}
