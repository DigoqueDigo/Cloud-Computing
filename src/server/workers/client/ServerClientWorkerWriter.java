package server.workers.client;
import java.io.DataOutputStream;
import carrier.Carrier;
import packets.Packet;
import server.containers.ServerContainer;


public class ServerClientWorkerWriter implements Runnable{

    private String nonce;
    private ServerContainer serverContainer;
    private DataOutputStream outputStream;


    public ServerClientWorkerWriter(String nonce, ServerContainer serverContainer, DataOutputStream outputStream){
        this.nonce = nonce;
        this.serverContainer = serverContainer;
        this.outputStream = outputStream;
    }


    public void run(){

        Packet packet;
        Carrier carrier = Carrier.getInstance();
        
        while ((packet = serverContainer.getPacket(this.nonce)) != null){
            carrier.sendPacket(outputStream,packet);
        }
    }
}