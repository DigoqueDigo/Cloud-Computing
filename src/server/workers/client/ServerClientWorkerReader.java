package server.workers.client;
import java.io.DataInputStream;
import carrier.Carrier;
import client.user.User;
import packets.ConsultPacket;
import packets.JobPacket;
import packets.Packet;
import packets.UserPacket;
import packets.Packet.Protocol;
import server.containers.ServerContainer;


public class ServerClientWorkerReader implements Runnable{

    private String nonce;
    private ServerContainer serverContainer;
    private DataInputStream inputStream;
    
    
    public ServerClientWorkerReader(String nonce, ServerContainer serverContainer, DataInputStream inputStream){
        this.nonce = nonce;
        this.inputStream = inputStream;
        this.serverContainer = serverContainer;
    }


    private void welcomeHandler(Packet packet) throws Exception{

        Packet resultPacket;
        User user = ((UserPacket) packet).getUser();
        System.out.println(packet);
        
        if (packet.getProtocol() == Protocol.CREATE_ACCOUNT){

            resultPacket = (!this.serverContainer.addUser(user)) ?
                new UserPacket(Protocol.ERROR,"Username already in use",user) :
                new UserPacket(Protocol.CREATE_ACCOUNT,"Account successfully created",user);
        }

        else if (this.serverContainer.getUser(user.getUsername()) == null){
            resultPacket = new UserPacket(Protocol.ERROR,"Unknown username",user);
        }
            
        else if (!user.equals(this.serverContainer.getUser(user.getUsername()))){
            resultPacket = new UserPacket(Protocol.ERROR,"Invalid password",user);
        }

        else resultPacket = new UserPacket(Protocol.LOGIN,"Login successful",user);

        this.serverContainer.addResultPacket(this.nonce,resultPacket);
    }


    private void jobHandler(Packet packet){

        switch (packet.getProtocol()){

            case JOB:
                JobPacket jobPacket = (JobPacket) packet;
                this.serverContainer.addJob(jobPacket.getJob());
                break;
        
            default:
                this.serverContainer.addResultPacket(nonce, new ConsultPacket(Protocol.ERROR)); // por implementar
                break;
        }
    }


    public void run(){

        try{

            Packet packet;
            Carrier carrier = Carrier.getInstance();

            while ((packet = carrier.receivePacket(inputStream)) != null){

                if (packet.getProtocol() == Protocol.LOGIN || packet.getProtocol() == Protocol.CREATE_ACCOUNT){
                    welcomeHandler(packet);
                }

                else{
                    System.out.println(packet);
                    System.out.println("Por implementar");
                }
            }
        }

        catch (Exception e){
            e.printStackTrace();
            this.serverContainer.addResultPacket(nonce,null);
        }
    }
}