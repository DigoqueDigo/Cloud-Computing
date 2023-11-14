package server.containers;
import packets.Packet;
import user.User;


public class ServerContainer{

    private UserContainer userContainer;
    private PacketContainer packetContainer;

    public ServerContainer(){
        this.userContainer = new UserContainer();
        this.packetContainer = new PacketContainer();
    }

    public boolean addUser(User user){
        return this.userContainer.addUser(user);
    }

    public User getUser(String username){
        return this.userContainer.getUser(username);
    }

    public void addPacket(String username, Packet packet){
        this.packetContainer.addPacket(username,packet);
    }

    public Packet getPacket(String username){
        return this.packetContainer.getPacket(username);
    }
}