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

            INPUT_FOLDER = args[0];
            OUTPUT_FOLDER = args[1];

            Buffer inBuffer = new Buffer();
            Buffer outBuffer = new Buffer();
            Buffer jobBuffer = new Buffer();

            Socket socket = new Socket(Server.ServerAddrees,Server.ServerDefaultPort);
            DataInputStream inputStream = new DataInputStream(socket.getInputStream());
            DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
          
            UserClientWorker clientWorker = new UserClientWorker(inBuffer,outBuffer,jobBuffer);
            
            Thread clientWorkerWriter = new Thread(new UserClientWriter(jobBuffer));
            Thread clientReader = new Thread(new SocketToBuffer(inBuffer,inputStream));
            Thread clientWriter = new Thread(new BufferToSocket(outBuffer,outputStream));

            clientWorkerWriter.start();
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