package publisher;

import broker.BrokerSocket;
import com.google.gson.Gson;
import utility.Constants;
import utility.Payload;
import utility.TransportService;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Publisher {
    public static void main(String[] args) throws IOException {
        System.out.println("Publisher");
        while (true) {
            String message;
            String receiver;

            List<String> receivers = new ArrayList<>();
            Socket socket;
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

            socket = new Socket(Constants.HOSTNAME, Constants.PORT);
            System.out.println("Input the receivers: (enter ok after typing the receivers)");
            while (!(receiver = bufferedReader.readLine()).equals("ok")) {
                receivers.add(receiver);
            }

            System.out.println("Input the message: ");
            message = bufferedReader.readLine();
            try {
                message = formJSONMessage(message, receivers);
                System.out.println("Serialized data in JSON: ");
                System.out.println(message);
                BrokerSocket readWrite = new TransportService(socket);
                readWrite.writeAsync(message);
                System.out.println("Data has been sent");
            } catch (ParserConfigurationException e) {
                System.out.println("Connection failed");
                e.printStackTrace();
            } catch (TransformerException e) {
                e.printStackTrace();
            }
                   }
            }
    static String formJSONMessage(String message, List<String> rec) throws ParserConfigurationException, TransformerException {
        Payload payload = new Payload(rec,message);
        String payloadGson = new Gson().toJson(payload);
        return payloadGson;
    }
}
