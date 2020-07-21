package eu.ecoepi.iris.components;

import com.artemis.Component;

public class Hello extends Component {
    String message;

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }
}
