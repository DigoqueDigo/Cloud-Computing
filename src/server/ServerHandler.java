package server;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import carrier.Carrier;
import packets.Packet;
import server.containers.ServerContainer;
import server.workers.client.ServerClientWorkerWriter;
import server.workers.machine.ServerMachineWorkerReader;
import server.workers.machine.ServerMachineWorkerWriter;
import server.workers.client.ServerClientWorkerReader;


public class ServerHandler implements Runnable{

    private Socket socket;
    private ServerContainer serverContainer;
    private DataInputStream inputStream;
    private DataOutputStream outputStream;


    public ServerHandler(Socket socket, ServerContainer serverContainer) throws IOException{
        this.socket = socket;
        this.serverContainer = serverContainer;
        this.inputStream = new DataInputStream(socket.getInputStream());
        this.outputStream = new DataOutputStream(socket.getOutputStream());
    };


    public void run(){

        try{

            Carrier carrier = Carrier.getInstance();
            String nonce = UUID.randomUUID().toString();
            Packet packet = carrier.receivePacket(this.inputStream);
            
            System.out.println("AAA");
            System.out.println(packet);
            System.out.println("AAA");
            
            List<Thread> threads = new ArrayList<Thread>();
            
            switch (packet.getProtocol()){
                
                case CONNECT_USER:
                
                    ServerClientWorkerReader clientWorkerWriter = new ServerClientWorkerReader(nonce,serverContainer,inputStream);
                    ServerClientWorkerWriter clientWorkerReader = new ServerClientWorkerWriter(nonce,serverContainer,outputStream);

                    threads.add(new Thread(clientWorkerReader));
                    threads.add(new Thread(clientWorkerWriter));

                    break;

                case CONNECT_MACHINE:

                    ServerMachineWorkerReader machineWorkerReader = new ServerMachineWorkerReader(nonce,serverContainer,inputStream);
                    ServerMachineWorkerWriter machineWorkerWriter = new ServerMachineWorkerWriter(nonce,serverContainer,outputStream);

                    threads.add(new Thread(machineWorkerReader));
                    threads.add(new Thread(machineWorkerWriter));

                    break;

                default:
                    break;
            }

            for (Thread thread : threads) {thread.start();}
            for (Thread thread : threads) {thread.join();}

            socket.close();
            System.out.println("Client disconnected");
        }

        catch (Exception e){
            try {socket.close();}
            catch (Exception f) {}
        }
    }
}