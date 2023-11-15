package server;
import java.net.ServerSocket;
import java.net.Socket;
import server.containers.ServerContainer;


public class Server{

    public static final int ServerDefaultPort = 12345;
    public static final String ServerAddrees = "localhost";

    public static void main(String[] args){

        try{

            Socket socket;
            ServerContainer serverContainer = new ServerContainer();
            ServerSocket serverSocket = new ServerSocket(ServerDefaultPort);
            
            while ((socket = serverSocket.accept()) != null){
                new Thread(new ServerHandler(socket,serverContainer)).start();
            }

            serverSocket.close();
        }

        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}