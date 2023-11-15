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
import server.workers.machine.ServerMachineWorker;
import server.workers.client.ServerClientWorkerReader;


public class ServerHandler implements Runnable{

    private Socket socket;
    private ServerContainer serverContainer;
    private DataInputStream inputstream;
    private DataOutputStream outputStream;


    public ServerHandler(Socket socket, ServerContainer serverContainer) throws IOException{
        this.socket = socket;
        this.serverContainer = serverContainer;
        this.inputstream = new DataInputStream(socket.getInputStream());
        this.outputStream = new DataOutputStream(socket.getOutputStream());
    };


    public void run(){

        try{

            Carrier carrier = Carrier.getInstance();
            String nonce = UUID.randomUUID().toString();
            Packet packet = carrier.receivePacket(this.inputstream);
            
            System.out.println(packet);
            
            List<Thread> threads = new ArrayList<Thread>();

            switch (packet.getProtocol()){

                case USER:
                    
                    ServerClientWorkerReader workerWriter = new ServerClientWorkerReader(nonce,serverContainer,inputstream);
                    ServerClientWorkerWriter workerReader = new ServerClientWorkerWriter(nonce,serverContainer,outputStream);

                    threads.add(new Thread(workerReader));
                    threads.add(new Thread(workerWriter));

                    break;

                case MACHINE:
                    ServerMachineWorker serverMachineWorker = new ServerMachineWorker(serverContainer,inputstream,outputStream);
                    serverMachineWorker.execute();
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