package utility;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Payload {
    private List<String> topic;
    private MessageTypes type;
    private String message;

    public Payload(List<String> topic, MessageTypes type, String message) {
        this.topic = topic;
        this.type = type;
        this.message = message;
    }

    @Override
    public String toString() {
        return "Payload{" +
                "topic=" + topic +
                ", type='" + type + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
