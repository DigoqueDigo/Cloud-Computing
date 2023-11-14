package server.workers.client;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import carrier.Carrier;
import packets.UserPacket;
import packets.Packet.Protocol;
import server.containers.ServerContainer;
import user.User;


public class ServerClientWorker{

    private DataInputStream inputStream;
    private DataOutputStream outputStream;
    private ServerContainer serverContainer;
    
    
    public ServerClientWorker(ServerContainer serverContainer, DataInputStream inputStream, DataOutputStream outputStream){
        this.inputStream = inputStream;
        this.outputStream = outputStream;
        this.serverContainer = serverContainer;
    }


    public void executeLogin() throws Exception{

        Carrier carrier = Carrier.getInstance();
        UserPacket packet = (UserPacket) carrier.receivePacket(this.inputStream);
        User user = packet.getUser();

        
        switch (packet.getProtocol()){

            case CREATE_ACCOUNT:
                if (!this.serverContainer.putUser(user)){
                    packet.setProtocol(Protocol.ERROR);
                    packet.setMessage("Username already in use");
                }

            case LOGIN:
                if (!user.equals(this.serverContainer.getUser(user.getUsername()))){
                    packet.setProtocol(Protocol.ERROR);
                    packet.setMessage("Invalid password");
                }

            default:
                break;
        }

        carrier.sendPacket(outputStream,packet);
        if (packet.getProtocol() == Protocol.ERROR) executeLogin();
    }
    
    
    public void execute() throws Exception{
        System.out.println("ServerClientWorker por implementar");
    } 
}