package com.example.order.execution.FiXSessions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class DummyFixServer {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(9878);
       System.out.println("Dummy FIX Server listening on port 9878...");

        while (true) {
            Socket clientSocket = serverSocket.accept();
           System.out.println("Client connected.");

            new Thread(() -> handleClient(clientSocket)).start();
        }
    }

    private static void handleClient(Socket socket) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            String line;
            while ((line = in.readLine()) != null) {
               System.out.println("Received FIX: " + line);

                // Parse ClOrdID (11=)
                String clOrdID = extractTagValue(line, "11");

                // Send back a simple Execution Report FIX message:
                String execReport = String.format(
                        "35=8|39=2|150=F|11=%s|55=EUR/USD|54=1|38=100|32=100|31=1.1234|", clOrdID);

               System.out.println("Sending ExecReport: " + execReport);
                out.println(execReport);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String extractTagValue(String fixMsg, String tag) {
        for (String field : fixMsg.split("\\|")) {
            if (field.startsWith(tag + "=")) {
                return field.substring(tag.length() + 1);
            }
        }
        return null;
    }
}
