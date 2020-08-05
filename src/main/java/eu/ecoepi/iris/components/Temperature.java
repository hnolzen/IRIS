package eu.ecoepi.iris.components;

import com.artemis.Component;

import java.util.Objects;

/*
*
* The consideration of daily maximal and minimal Temperatures is important because extreme weather has a
* direct influence on the abundance of the tick population
*
* */
public class Temperature extends Component {
    double meanTemperature;
    double maxTemperature;
    double minTemperature;

    public Temperature() {

    }

    public Temperature(double meanTemperature, double maxTemperature, double minTemperature) {
        this.meanTemperature = meanTemperature;
        this.maxTemperature = maxTemperature;
        this.minTemperature = minTemperature;
    }

    public double getMeanTemperature() {
        return meanTemperature;
    }

    public double getMinTemperature() {
        return minTemperature;
    }

    public double getMaxTemperature() {
        return maxTemperature;
    }

    public void setMeanTemperature(double meanTemperature) {
        this.meanTemperature = meanTemperature;
    }

    public void setMaxTemperature(double maxTemperature) {
        this.maxTemperature = maxTemperature;
    }

    public void setMinTemperature(double minTemperature) {
        this.minTemperature = minTemperature;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Temperature that = (Temperature) o;
        return Double.compare(that.meanTemperature, meanTemperature) == 0 &&
                Double.compare(that.maxTemperature, maxTemperature) == 0 &&
                Double.compare(that.minTemperature, minTemperature) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(meanTemperature, maxTemperature, minTemperature);
    }
}
