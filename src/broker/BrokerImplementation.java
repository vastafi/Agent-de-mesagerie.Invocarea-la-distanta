package broker;

import com.google.gson.Gson;
import utility.*;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class BrokerImplementation implements broker.BrokerSocket {

    public ServerSocket serverSocket;
    private Queue<Letter> letterList;
    private Queue<Receiver> receiverList;

    public BrokerImplementation() {
        letterList = new ConcurrentLinkedQueue<>();
        receiverList = new ConcurrentLinkedQueue<>();
        try {
            serverSocket = new ServerSocket(Constants.PORT);
        } catch (IOException e) {
            System.out.println("Cannot create broker socket");
            e.printStackTrace();
        }
    }

    @Override
    public String readAsync() {
        Socket connectionSocket = null;

        Consumer<Receiver> styleRec = (Receiver p) ->
                System.out.println("Name: " + p.getName() + ", Socket: " + p.getSocket());
        Consumer<Letter> printLetterConsumer = (Letter l) ->
                System.out.print("Name:" + l.getName() + ", Message text: " + l.getMessage());
        try {
            connectionSocket = serverSocket.accept();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Socket finalConnectionSocket = connectionSocket;
        ExecutorService executor = Executors.newSingleThreadExecutor();

        Callable<String> task = () -> {
            InputStream inputStream;
            StringBuilder result = new StringBuilder();

            try {
                inputStream = Objects.requireNonNull(finalConnectionSocket).getInputStream();
                BufferedReader receiveRead = new BufferedReader(new InputStreamReader(inputStream));
                String partlyTransData = receiveRead.readLine().trim();
                result.append(partlyTransData);
            } catch (IOException e) {
                e.printStackTrace();
            }

            String message = result.toString();
            String answer = "Valid";
            System.out.println("Data has been received from buffer");
            System.out.println(message);
            Payload payload = new Gson().fromJson(message, Payload.class);
            String name = payload.getTopic().get(0);
            if (payload.getType() == MessageTypes.CONNECT) {
                System.out.println("** " + name + " parsed to be connected");
                receiverList.add(new Receiver(finalConnectionSocket, name));
            } else if (payload.getType() == MessageTypes.DISCONNECT) {
                System.out.println("Parsed receiver: " + name + " to be disconnected");
                letterList.add(new Letter(name, "disconnect\n"));
            } else if (payload.getType() == MessageTypes.MESSAGE) {
                System.out.println("Parsed data");
                String msg = payload.getMessage();
                List<String> rec = payload.getTopic();
                System.out.println("Receivers : " + rec);
                for (String s : rec) letterList.add(new Letter(s, msg + "\n"));
            } else {
                answer = "Invalid";
                System.out.println("Message is not valid");
                int port = Objects.requireNonNull(finalConnectionSocket).getPort();
                for (Receiver receiver : receiverList)
                    if (receiver.getSocket().getPort() == port) {
                        letterList.add(new Letter(receiver.getName(), "null" + "\n"));
                        break;
                    }
            }
            System.out.println("Receiver List: ");
            receiverList.forEach(styleRec);
            System.out.println("Letters List");
            letterList.forEach(printLetterConsumer);
            return answer;
        };
        Future<String> future = executor.submit(task);
        String message = null;
        while (!future.isDone())
            try {
                message = future.get();
                System.out.println("Finished getting data");
            } catch (InterruptedException | ExecutionException ie) {
                ie.printStackTrace(System.err);
            }
        executor.shutdown();
        return message;
    }

    @Override
    public void writeAsync(String message) {

        BiConsumer<Receiver, Letter> receiverLetterMatch = (receiver, letter) -> {
            if (receiver.getName().equals(letter.getName())) {
                try {
                    OutputStream outputStream = receiver.getSocket().getOutputStream();
                    PrintWriter printWriter = new PrintWriter(outputStream, true);
                    printWriter.println(letter.getMessage());
                    printWriter.flush();
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                letter.setSent(true);
                System.out.println("Receiver name: " + receiver.getName());
                System.out.println("Letter name: " + letter.getName());
                System.out.println("Message: " + letter.getMessage() + " was transmitted to " + letter.getName() + " successfully...");

                if (letter.getMessage().equals("Disconnect \n")) {
                    receiver.setConnected(false);
                    try {
                        receiver.getSocket().close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        Runnable r = () -> {
            for (Letter letter : letterList) {
                for (Receiver receiver : receiverList) {
                    receiverLetterMatch.accept(receiver, letter);
                }
            }
            letterList.removeIf(Letter::isSent);
            receiverList.removeIf(Receiver::isConnected);
        };
        Thread t = new Thread(r);
        t.start();
    }
}
