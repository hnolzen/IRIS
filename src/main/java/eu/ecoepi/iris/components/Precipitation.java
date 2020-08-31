package eu.ecoepi.iris.components;

import com.artemis.Component;

import java.util.Objects;

/*
 *
 * Daily height of precipitation in mm
 *
 * Type of precipitation:
 *      0: no precipitation
 *      1: rain (in historic data before 1979)
 *      4: unknown precipitation
 *      6: rain
 *      7: snow
 *      8: rain and snow
 *      9: missing or not identifiable
 *
 * Snow Height in cm
 *
 * */
public class Precipitation extends Component {
    int precipitationType;
    double precipitationHeight;
    double snowHeight;

    public Precipitation() {

    }

    public Precipitation(int precipitationType, double precipitationHeight, double snowHeight) {
        this.precipitationHeight = precipitationType;
        this.precipitationHeight = precipitationHeight;
        this.snowHeight = snowHeight;
    }

    public double getPrecipitationType() {
        return precipitationType;
    }

    public double getPrecipitationHeight() {
        return precipitationHeight;
    }

    public void setPrecipitationType(int precipitationType) {
        this.precipitationType = precipitationType;
    }

    public void setPrecipitationHeight(double precipitationHeight) { this.precipitationHeight = precipitationHeight; }

    public double getSnowHeight() {
        return snowHeight;
    }

    public void setSnowHeight(double snowHeight) { this.snowHeight = snowHeight; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Precipitation that = (Precipitation) o;
        return precipitationType == that.precipitationType &&
                Double.compare(that.precipitationHeight, precipitationHeight) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(precipitationType, precipitationHeight);
    }
}
