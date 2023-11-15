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

            Buffer inBuffer = new Buffer();
            Buffer outBuffer = new Buffer();

            Socket socket = new Socket(Server.ServerAddrees,Server.ServerDefaultPort);
            DataInputStream inputStream = new DataInputStream(socket.getInputStream());
            DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
          
            ClientWorker clientWorker = new ClientWorker(inBuffer,outBuffer);
            Thread clientReader = new Thread(new ClientReader(inBuffer,inputStream));
            Thread clientWriter = new Thread(new ClientWriter(outBuffer,outputStream));

            clientReader.start();
            clientWriter.start();
            clientWorker.run();

            socket.close();

            clientReader.join();
            clientWriter.join();
            System.out.println("Bye");
        }

        catch (Exception e) {}
    }   
}