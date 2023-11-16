package client.user;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import buffer.Buffer;
import buffer.SocketToBuffer;
import buffer.BufferToSocket;
import server.Server;


public class UserClient{

    public static String INPUT_FOLDER;
    public static String OUTPUT_FOLDER;

    public static void main(String[] args){

        try{

            UserClient.INPUT_FOLDER = args[0];
            UserClient.OUTPUT_FOLDER = args[1];

            Buffer inBuffer = new Buffer();
            Buffer outBuffer = new Buffer();

            Socket socket = new Socket(Server.ServerAddrees,Server.ServerDefaultPort);
            DataInputStream inputStream = new DataInputStream(socket.getInputStream());
            DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
          
            UserClientWorker clientWorker = new UserClientWorker(inBuffer,outBuffer);
            Thread clientReader = new Thread(new SocketToBuffer(inBuffer,inputStream));
            Thread clientWriter = new Thread(new BufferToSocket(outBuffer,outputStream));

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