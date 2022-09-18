package broker;

public interface BrokerSocket {
    String readAsync();
    void writeAsync(String message);
}
