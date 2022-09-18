package broker;

import javax.sound.midi.Receiver;
import java.net.ServerSocket;
import java.util.List;

public class BrokerImplementation {
    public ServerSocket serverSocket;
    private List<Receiver> receiverList;
}
