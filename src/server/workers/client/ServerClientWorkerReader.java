package server.workers.client;
import java.io.DataInputStream;
import carrier.Carrier;
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


    private void executeLogin(UserPacket packet) throws Exception{

        User user = packet.getUser();
        
        switch (packet.getProtocol()){

            case CREATE_ACCOUNT:
                
                if (!this.serverContainer.addUser(user)){
                    packet.setProtocol(Protocol.ERROR);
                    packet.setMessage("Username already in use");
                }
                
                break;

            case LOGIN:
                
                if (!user.equals(this.serverContainer.getUser(user.getUsername()))){
                    packet.setProtocol(Protocol.ERROR);
                    packet.setMessage("Invalid password");
                }
                
                break;

            default:
                break;
        }

        this.serverContainer.addPacket(nonce,packet);
    }


    public void run(){

        try{

            Carrier carrier = Carrier.getInstance();
            UserPacket packet = (UserPacket) carrier.receivePacket(this.inputStream);

            switch (packet.getProtocol()){
                
                case LOGIN:
                case CREATE_ACCOUNT:
                    executeLogin((UserPacket)packet);
                    break;
            
                default:
                    break;
            }
        }

        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}