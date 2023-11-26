package client.machine;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import buffer.Buffer;
import buffer.BufferToSocket;
import buffer.SocketToBuffer;
import server.Server;


public class MachineClient{

    public static void main(String[] args){

        try{

            Buffer inBuffer = new Buffer();
            Buffer outBuffer = new Buffer();
            Machine machine = new Machine(Integer.valueOf(args[0]));

            Socket socket = new Socket(Server.ServerAddrees,Server.ServerDefaultPort);
            DataInputStream inputStream = new DataInputStream(socket.getInputStream());
            DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());

            MachineClientWorker machineClientWorker = new MachineClientWorker(machine,inBuffer,outBuffer);
            Thread machineReader = new Thread(new SocketToBuffer(inBuffer,inputStream));
            Thread machineWriter = new Thread(new BufferToSocket(outBuffer,outputStream));

            machineReader.start();
            machineWriter.start();
            machineClientWorker.run();

            socket.close();

            machineReader.join();
            machineWriter.join();
        }

        catch (Exception e) {}
    }
}