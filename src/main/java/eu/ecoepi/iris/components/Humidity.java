package eu.ecoepi.iris.components;

import com.artemis.Component;

import java.util.Objects;

public class Humidity extends Component {
    double relativeHumidity;

    public double getRelativeHumidity() {
        return relativeHumidity;
    }

    public void setRelativeHumidity(double relativeHumidity) {
        this.relativeHumidity = relativeHumidity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Humidity humidity = (Humidity) o;
        return Double.compare(humidity.relativeHumidity, relativeHumidity) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(relativeHumidity);
    }
}
