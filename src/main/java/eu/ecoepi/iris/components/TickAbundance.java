package eu.ecoepi.iris.components;

import com.artemis.Component;
import eu.ecoepi.iris.LifeCycleStage;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class TickAbundance extends Component {

    final Map<LifeCycleStage, Integer> abundance = new HashMap<>();
    final Map<LifeCycleStage, Integer> infectedAbundance = new HashMap<>();

    public TickAbundance() {

    }

    public TickAbundance(int larvae, int nymphs, int adults, int infectedLarvae, int infectedNymphs, int infectedAdults) {
        abundance.put(LifeCycleStage.LARVAE, larvae);
        abundance.put(LifeCycleStage.NYMPH, nymphs);
        abundance.put(LifeCycleStage.ADULT, adults);
        infectedAbundance.put(LifeCycleStage.LARVAE, infectedLarvae);
        infectedAbundance.put(LifeCycleStage.NYMPH, infectedNymphs);
        infectedAbundance.put(LifeCycleStage.ADULT, infectedAdults);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TickAbundance that = (TickAbundance) o;
        return Objects.equals(abundance, that.abundance) &&
                Objects.equals(infectedAbundance, that.infectedAbundance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(abundance, infectedAbundance);
    }

    public int getStage(LifeCycleStage stage){
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

    public int getInfectedLarvae() {
        return infectedAbundance.get(LifeCycleStage.LARVAE);
    }

    public int getInfectedNymphs() {
        return infectedAbundance.get(LifeCycleStage.NYMPH);
    }

    public int getInfectedAdults() {
        return infectedAbundance.get(LifeCycleStage.ADULT);
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

    public void addInfectedLarvae(int larvae) {
        infectedAbundance.compute(LifeCycleStage.LARVAE, (stage, count) -> count + larvae);
    }

    public void addInfectedNymphs(int nymphs) {
        infectedAbundance.compute(LifeCycleStage.NYMPH, (stage, count) -> count + nymphs);
    }

    public void addInfectedAdults(int adults) {
        infectedAbundance.compute(LifeCycleStage.ADULT, (stage, count) -> count + adults);
    }
}
