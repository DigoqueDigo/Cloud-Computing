package server.workers.client;
import java.io.DataInputStream;
import carrier.Carrier;
import packets.ConsultPacket;
import packets.JobPacket;
import packets.Packet;
import packets.UserPacket;
import packets.Packet.Protocol;
import server.containers.ServerContainer;
import user.User;


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

        User user = ((UserPacket) packet).getUser();
        Packet resultPacket = packet;
        
        switch (packet.getProtocol()){

            case CREATE_ACCOUNT:

                if (!this.serverContainer.addUser(user)){
                    resultPacket = new UserPacket(Protocol.ERROR,"Username already in use",user);
                }

                break;

            case LOGIN:

                if (this.serverContainer.getUser(user.getUsername()) == null){
                    resultPacket = new UserPacket(Protocol.ERROR,"Unknown username",user);
                }
                
                else if (!user.equals(this.serverContainer.getUser(user.getUsername()))){
                    resultPacket = new UserPacket(Protocol.ERROR,"Invalid password",user);
                }
                
                break;

            default:
                resultPacket = new UserPacket(Protocol.ERROR,"Invalid Protocol",user);
                break;
        }

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

                else jobHandler(packet);
            }
        }

        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}