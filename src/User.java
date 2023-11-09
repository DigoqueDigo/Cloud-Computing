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
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream())); //leitor que serve para ler o que vem do outro lado
                PrintWriter out = new PrintWriter(socket.getOutputStream()); //escritor para o servidor
                BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in)); //leitor que serve para ler do terminal

                while (true) {
                    String reader = consoleReader.readLine();
                    out.println(reader);

                    String receivedMessage = in.readLine();
                    System.out.println(receivedMessage);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}