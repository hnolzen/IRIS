package eu.ecoepi.iris.components;

import com.artemis.Component;

import java.util.Objects;

public class Precipitation extends Component {
    float rainfall;

    public Precipitation() {

    }

    public Precipitation(float rainfall) {
        this.rainfall = rainfall;
    }

    public double getRainfall() {
        return rainfall;
    }

    public void setRainfall(float rainfall) {
        this.rainfall = rainfall;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Precipitation that = (Precipitation) o;
        return Float.compare(that.rainfall, rainfall) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(rainfall);
    }
}
