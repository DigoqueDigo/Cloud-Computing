package server.workers.client;
import java.io.DataInputStream;
import java.util.Map;
import carrier.Carrier;
import client.user.User;
import job.Consult;
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


    private void loginHandler(Packet packet){

        Packet resultPacket;
        User user = ((UserPacket) packet).getUser();

        if (this.serverContainer.getUser(user.getUsername()) == null)
            resultPacket = new UserPacket(Protocol.ERROR,"Unknown username",user);
            
        else if (!user.equals(this.serverContainer.getUser(user.getUsername())))
            resultPacket = new UserPacket(Protocol.ERROR,"Invalid password",user);

        else resultPacket = new UserPacket(Protocol.LOGIN,"Login successful",user);
        this.serverContainer.addResultPacket(this.nonce,resultPacket);
    }


    private void createAccountHandler(Packet packet){

        User user = ((UserPacket) packet).getUser();

        Packet resultPacket = (!this.serverContainer.addUser(user)) ?
                new UserPacket(Protocol.ERROR,"Username already in use",user) :
                new UserPacket(Protocol.CREATE_ACCOUNT,"Account successfully created",user);

        this.serverContainer.addResultPacket(this.nonce,resultPacket);
    }


    private void consultHandler(Packet packet){
        
        int pendingJobs = this.serverContainer.getPendingJobs();
        Map<String,Integer> systemSate = this.serverContainer.getSystemState();

        Packet resutPacket = (pendingJobs != -1 && systemSate != null) ?
            new ConsultPacket(Protocol.CONSULT,new Consult(pendingJobs,systemSate)) :
            new ConsultPacket(Protocol.ERROR);

        this.serverContainer.addResultPacket(this.nonce,resutPacket);
    }


    private void jobHandler(Packet packet){
        JobPacket jobPacket = (JobPacket) packet;
        jobPacket.setClientNonce(this.nonce);
        this.serverContainer.addJobPacket(jobPacket);
    }


    public void run(){

        try{

            Packet packet;
            Carrier carrier = Carrier.getInstance();
            System.out.println(this.serverContainer.toString());

            while ((packet = carrier.receivePacket(inputStream)) != null){

                switch (packet.getProtocol()){

                    case LOGIN:
                        loginHandler(packet);
                        break;

                    case CREATE_ACCOUNT:
                        createAccountHandler(packet);
                        break;
                        
                    case CONSULT:
                        consultHandler(packet);
                        break;

                    case JOB:
                        jobHandler(packet);
                        break;

                    default:
                        break;
                }
                
                System.out.println(this.serverContainer.toString());
            }
        }

        catch (Exception e){
            e.printStackTrace();
            this.serverContainer.addResultPacket(nonce,null);
        }
    }
}