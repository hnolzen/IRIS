package eu.ecoepi.iris.components;

import com.artemis.Component;

import java.util.Objects;

public class TickAbundance extends Component {

    int larvae;
    int nymphs;
    int adults;
    int infectedLarvae;
    int infectedNymphs;
    int infectedAdults;

    public TickAbundance() {

    }

    public TickAbundance(int larvae, int nymphs, int adults){
        this.larvae = larvae;
        this.nymphs = nymphs;
        this.adults = adults;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TickAbundance that = (TickAbundance) o;
        return larvae == that.larvae &&
                nymphs == that.nymphs &&
                adults == that.adults &&
                infectedLarvae == that.infectedLarvae &&
                infectedNymphs == that.infectedNymphs &&
                infectedAdults == that.infectedAdults;
    }

    @Override
    public int hashCode() {
        return Objects.hash(larvae, nymphs, adults, infectedLarvae, infectedNymphs, infectedAdults);
    }
}
