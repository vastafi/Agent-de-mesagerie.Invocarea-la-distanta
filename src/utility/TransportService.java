package utility;

import broker.BrokerSocket;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.*;

public class TransportService implements BrokerSocket {
    Socket transport;

    public TransportService(Socket transport) {
        this.transport = transport;
    }

    @Override
    public String readAsync() {

        ExecutorService executor = Executors.newSingleThreadExecutor();

        Callable<String> task = () -> {
            InputStream inputStream;
            String partlyTransData;
            StringBuilder result = new StringBuilder();
            try {
                inputStream = transport.getInputStream();
                BufferedReader receiveRead = new BufferedReader(new InputStreamReader(inputStream));

                if (!(partlyTransData = receiveRead.readLine()).isEmpty())
                    result.append(partlyTransData.trim());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result.toString();
        };

        Future<String> future = executor.submit(task);
        String message = "";

        while (!future.isDone())
            try {
                message =
                        future.get();
            } catch (InterruptedException | ExecutionException ie) {
                ie.printStackTrace(System.err);
            }
        executor.shutdown();
        return message;
    }

    @Override
    public void writeAsync(final String message) {
        Thread thread = new Thread(() -> {
            OutputStream outputStream;
            try {
                outputStream = transport.getOutputStream();
                PrintWriter printWriter = new PrintWriter(outputStream, true);
                printWriter.println(message);
                printWriter.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        thread.start();
    }
}
