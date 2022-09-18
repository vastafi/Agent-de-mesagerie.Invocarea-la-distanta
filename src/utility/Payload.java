package utility;

import java.util.List;

public class Payload {
    private List<String> topic;
    private String message;

    public Payload(List<String> topic, String message) {
        this.topic = topic;
        this.message = message;
    }

    public List<String> getTopic() {
        return topic;
    }

    public void setTopic(List<String> topic) {
        this.topic = topic;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "Payload{" +
                "topic=" + topic +
                ", message='" + message + '\'' +
                '}';
    }
}
