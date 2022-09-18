package subscriber;

import broker.BrokerSocket;

public class SubscriberReaderThread implements Runnable {
    private BrokerSocket transport;

    public SubscriberReaderThread(BrokerSocket transport) {
        this.transport = transport;
    }

    public void run() {
        String messageFromServer;
        while (!(messageFromServer = transport.readAsync()).equals("Disconnect"))
            System.out.println(messageFromServer);
        System.out.println("Disconnected from broker");
    }
}
