package eu.ecoepi.iris.components;

import com.artemis.Component;

import java.util.Objects;

public class Habitat extends Component {

    public Habitat(Type type) {
        this.type = type;
    }

    public enum Type {

        WOOD, ECOTONE, MEADOW

    }

    Type type;

    public Type getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Habitat habitat = (Habitat) o;
        return type == habitat.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(type);
    }
}


