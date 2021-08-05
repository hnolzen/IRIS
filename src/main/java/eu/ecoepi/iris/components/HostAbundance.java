package eu.ecoepi.iris.components;

import com.artemis.Component;
import eu.ecoepi.iris.CohortState;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class HostAbundance extends Component {

    final Map<CohortState, Integer> abundance = new HashMap<>();

    public HostAbundance() {
    }

    public HostAbundance(int rodents, int infectedRodents) {
        abundance.put(CohortState.RODENTS, rodents);
        abundance.put(CohortState.RODENTS_INFECTED, infectedRodents);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HostAbundance that = (HostAbundance) o;
        return Objects.equals(abundance, that.abundance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(abundance);
    }

    public int getRodents() {
        return abundance.get(CohortState.RODENTS);
    }

    public int getInfectedRodents() {
        return abundance.get(CohortState.RODENTS_INFECTED);
    }

    public void addRodents(int rodents) {
        abundance.compute(CohortState.RODENTS, (stage, count) -> count + rodents);
    }

    public void addInfectedRodents(int rodents) {
        abundance.compute(CohortState.RODENTS_INFECTED, (stage, count) -> count + rodents);
    }

}
