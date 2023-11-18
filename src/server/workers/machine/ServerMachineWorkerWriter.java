package server.workers.machine;
import java.io.DataOutputStream;

import carrier.Carrier;
import client.machine.Machine;
import packets.Packet;
import server.containers.ServerContainer;


public class ServerMachineWorkerWriter implements Runnable{

    private String nonce;
    private DataOutputStream outputStream;
    private ServerContainer serverContainer;
    
    
    public ServerMachineWorkerWriter(String nonce, ServerContainer serverContainer, DataOutputStream outputStream){
        this.nonce = nonce;
        this.outputStream = outputStream;
        this.serverContainer = serverContainer;
    }
    
    
    public void run(){

        try{

            Packet packet;
            Machine machine = new Machine(0);
            Carrier carrier = Carrier.getInstance();

            machine.setIdentifier(this.nonce);

            while ((packet = this.serverContainer.getJobPacket(machine)) != null){

                carrier.sendPacket(outputStream,packet);
            }
        }

        catch (Exception e){}
    }
}