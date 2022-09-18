package utility;

import lombok.Getter;
import lombok.Setter;

import java.net.Socket;

@Getter
@Setter
public class Receiver {
    private Socket socket;
    private String name;
    private boolean isConnected;

    public Receiver(Socket socket, String name) {
        this.socket = socket;
        this.name = name;
    }
}
