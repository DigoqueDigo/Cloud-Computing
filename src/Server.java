package src;

import java.beans.XMLEncoder;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(9090);
            System.out.println("Servidor ativo na porta " + 9090);

            Socket socket;
            Authentication contas = new Authentication();

            while ((socket = serverSocket.accept()) != null) {
                Thread thread = new Thread(new Threads(socket,contas));
                thread.start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class Threads implements Runnable {
        private Socket socket;
        private Authentication contas;

        public Threads(Socket socket, Authentication contas) {
            this.socket = socket;
            this.contas = contas;
        }

        @Override
        public void run() {
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream())); // leitor para comunicar com o user
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true); // escritor para comunicar com o user
                int pode_usar = 0;
                String message;

                while ((message = in.readLine()) != null) {
                    String[] words = message.split(" ");

                    if(words[0].toUpperCase().equals("REGISTER")){
                        contas.registerUser(words[1],words[2]);
                        out.println("Registado com sucesso!");
                    }

                    if(words[0].toUpperCase().equals("LOGIN")){
                        if(contas.authenticateUser(words[1],words[2])){
                            pode_usar = 1;
                            out.println("Login com sucesso!");
                        }
                    }

                    if(pode_usar == 1){
                        //...
                    }


                    //out.println(message);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
