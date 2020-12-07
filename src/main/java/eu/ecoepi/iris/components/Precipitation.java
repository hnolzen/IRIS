package eu.ecoepi.iris.components;

import com.artemis.Component;

import java.util.Objects;

public class Precipitation extends Component {
    double rainfall;

    public Precipitation() {

    }

    public Precipitation(int rainfall) {
        this.rainfall = rainfall;
    }

    public double getRainfall() {
        return rainfall;
    }

    public void setRainfall(int rainfall) {
        this.rainfall = rainfall;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Precipitation that = (Precipitation) o;
        return Double.compare(that.rainfall, rainfall) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(rainfall);
    }
}
