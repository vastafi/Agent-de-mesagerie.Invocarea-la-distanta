package subscriber;

import broker.BrokerSocket;
import utility.Constants;
import utility.TransportService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Subscriber {
    public static void main(String[] a) throws IOException {
        Socket socket;
        String name;
        String message;
        String command;

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        socket = new Socket(Constants.HOSTNAME, Constants.PORT);

        System.out.println("Input your topic: ");
        name = bufferedReader.readLine();
        BrokerSocket brokerSocket = new TransportService(socket);
        System.out.println("Input \"connect\" command to be connected to broker");

        command = bufferedReader.readLine();
        System.out.println(command);
        while (true)
            if (command.equals("connect")) {
                message = command + " " + name + "\n";
                brokerSocket.writeAsync(message);
                System.out.println("Connected to broker");
                break;
            } else {
                System.out.println("No connection");
            }

        Runnable r = new SubscriberReaderThread(brokerSocket);
        new Thread(r).start();
        System.out.println("Input \"exit" + "\" command to be disconnected from broker");

        while (true) {
            command = bufferedReader.readLine();
            System.out.println(command);
            if (command.equals("Disconnect")) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                socket = new Socket(Constants.HOSTNAME, Constants.PORT);
                brokerSocket = new TransportService(socket);
                brokerSocket.writeAsync(command + " " + name + "\n");
                break;
            }
        }
    }
}
