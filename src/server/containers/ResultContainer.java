package server.containers;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import packets.Packet;


public class ResultContainer{

    private ReentrantLock lock;
    private Condition condition;
    private Map<String,List<Packet>> resultContainer;

    
    public ResultContainer(){
        this.lock = new ReentrantLock();
        this.condition = this.lock.newCondition();
        this.resultContainer = new HashMap<String,List<Packet>>();
    }


    public void addResultPacket(String nonce, Packet packet){
        
        try{
            this.lock.lock();
            this.resultContainer.putIfAbsent(nonce,new ArrayList<>());
            this.resultContainer.get(nonce).add(packet);
            this.condition.signalAll();
        }

        catch (Exception e){}

        finally {this.lock.unlock();}
    }


    public Packet getResultPacket(String nonce){

        try{

            this.lock.lock();
            Packet packet = null;
            List<Packet> packets = this.resultContainer.get(nonce);
            
            while (packets == null || packets.size() == 0){
                this.condition.await();
                packets = this.resultContainer.get(nonce);
            }

            packet = packets.get(packets.size()-1);
            packets.remove(packets.size()-1);
            return packet;
        }

        catch (Exception e) {return null;}

        finally{this.lock.unlock();}
    }


    public void removeEntry(String nonce){

        try{
            this.lock.lock();
            this.resultContainer.remove(nonce);
        }

        catch (Exception e) {}
        finally {this.lock.unlock();}
    }
}