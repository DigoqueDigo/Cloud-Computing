package server.containers;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import packets.Packet;


public class PacketContainer{

    private ReentrantLock lock;
    private Condition condition;
    private Map<String,List<Packet>> packetContainer;

    
    public PacketContainer(){
        this.lock = new ReentrantLock();
        this.condition = this.lock.newCondition();
        this.packetContainer = new HashMap<String,List<Packet>>();
    }


    public void addPacket(String username, Packet packet){
        
        try{
            this.lock.lock();
            this.packetContainer.putIfAbsent(username,new ArrayList<>());
            this.packetContainer.get(username).add(packet);
            this.condition.signalAll();
        }

        catch (Exception e){}

        finally {this.lock.unlock();}
    }


    public Packet getPacket(String username){

        try{

            this.lock.lock();
            Packet packet = null;
            List<Packet> packets = this.packetContainer.get(username);
            
            while (packets == null || packets.size() == 0){
                this.condition.await();
            }

            packet = packets.get(packets.size()-1);
            packets.remove(packets.size()-1);
            return packet;
        }

        catch (Exception e) {return null;}

        finally{this.lock.unlock();}
    }   
}