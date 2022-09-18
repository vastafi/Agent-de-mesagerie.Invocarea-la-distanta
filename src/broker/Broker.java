package broker;

public class Broker {
    public static void main(String[] args) {
        System.out.println("Brocker");

        BrokerSocket broker = new BrokerImplementation();
        String msg;
        while (true) {
            while (!(msg = broker.readAsync()).isEmpty())
                if (msg.equals("Invalid")) {
                    System.out.println("Invalid message");
                } else {
                    System.out.println("Valid message");
                    broker.writeAsync(msg);
                }
        }
    }
}