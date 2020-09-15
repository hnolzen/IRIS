package eu.ecoepi.iris.components;

import com.artemis.Component;
import eu.ecoepi.iris.Parameters;

import java.util.Objects;

public class Position extends Component {
    int x;
    int y;

    public Position() {

    }

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Position moveBy(int dx, int dy) {
        var x = (this.x + dx) % Parameters.GRID_WIDTH;
        var y = (this.y + dy) % Parameters.GRID_HEIGHT;
        if (x < 0) {
            x += Parameters.GRID_WIDTH;
        }
        if (y < 0) {
            y += Parameters.GRID_HEIGHT;
        }
        return new Position(x, y);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position position = (Position) o;
        return x == position.x &&
                y == position.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
