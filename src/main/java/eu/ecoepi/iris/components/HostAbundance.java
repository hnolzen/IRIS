package eu.ecoepi.iris.components;

import com.artemis.Component;
import java.util.Objects;

public class HostAbundance extends Component {

    int rodentsSusceptible;
    int rodentsInfected;

    public HostAbundance() {
    }

    public HostAbundance(int rodentsSusceptible, int rodentsInfected) {
        this.rodentsSusceptible = rodentsSusceptible;
        this.rodentsInfected = rodentsInfected;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HostAbundance that = (HostAbundance) o;
        return rodentsSusceptible == that.rodentsSusceptible && rodentsInfected == that.rodentsInfected;
    }

    @Override
    public int hashCode() {
        return Objects.hash(rodentsSusceptible, rodentsInfected);
    }

    public int getRodentsSusceptible() {
        return rodentsSusceptible;
    }

    public int getRodentsInfected() {
        return rodentsInfected;
    }

    public void addRodentsSusceptible(int number) {
        rodentsSusceptible += number;
    }

    public void addRodentsInfected(int number) {
        rodentsInfected += number;
    }

}
