package eu.ecoepi.iris.components;

import com.artemis.Component;

import java.util.Objects;

public class Sunshine extends Component {
    float sunshineHours;

    public double getSunshineHours() {
        return sunshineHours;
    }

    public void setSunshineHours(float sunshineHours) {
        this.sunshineHours = sunshineHours;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sunshine sunshine = (Sunshine) o;
        return Float.compare(sunshine.sunshineHours, sunshineHours) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(sunshineHours);
    }
}
