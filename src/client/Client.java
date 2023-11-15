package client;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import server.Server;


public class Client{

    public static String INPUT_FOLDER;
    public static String OUTPUT_FOLDER;

    public static void main(String[] args){

        try{

            Client.INPUT_FOLDER = args[0];
            Client.OUTPUT_FOLDER = args[1];

            Socket socket = new Socket(Server.ServerAddrees,Server.ServerDefaultPort);
            DataInputStream inputStream = new DataInputStream(socket.getInputStream());
            DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());

            Thread clientReader = new Thread(new ClientReader(new Buffer(),inputStream));
            Thread clientWriter = new Thread(new ClientWriter(new Buffer(),outputStream));

            clientReader.start();
            clientWriter.start();

            socket.close();

            clientReader.join();
            clientWriter.join();
        }

        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }   
}