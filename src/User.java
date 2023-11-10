package src;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class User {
    public static void main(String[] args) {
        try {

            Socket socket = new Socket("localhost", 9090);

            Thread tcpThread = new Thread(new TCPThread(socket));
            tcpThread.start();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    static class TCPThread implements Runnable {
        private Socket socket;

        public TCPThread(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));

                Thread serverReader = new Thread(() -> {
                    try {
                        String receivedMessage;
                        while ((receivedMessage = in.readLine()) != null) {
                            System.out.println(receivedMessage);
                            if (receivedMessage.isEmpty()) {
                                break;
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                serverReader.start();

                String userInput;
                while ((userInput = consoleReader.readLine()) != null) {
                    out.println(userInput);
                }

                serverReader.join();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }



}