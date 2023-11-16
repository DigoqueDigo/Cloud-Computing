package buffer;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import packets.Packet;


public class Buffer{

    private ReentrantLock lock;
    private Condition condition;
    private List<Packet> buffer;


    public Buffer(){
        this.lock = new ReentrantLock();
        this.condition = this.lock.newCondition();
        this.buffer = new ArrayList<Packet>();
    }


    public void addPacket(Packet packet){

        try{
            this.lock.lock();
            this.buffer.add(packet);
            this.condition.signalAll();
        }

        catch (Exception e) {}

        finally {this.lock.unlock();}
    }


    public Packet getPacketBlock(){

        try{

            this.lock.lock();

            while (this.buffer.size() == 0){
                this.condition.await();
            }

            Packet packet = this.buffer.get(this.buffer.size()-1);
            this.buffer.remove(this.buffer.size()-1);
            return packet;
        }

        catch (Exception e) {return null;}

        finally {this.lock.unlock();}
    }


    public Packet getPacketNonBlock(){

        try{

            Packet packet = null;
            this.lock.lock();

            if (this.buffer.size() != 0){
                packet = this.buffer.get(this.buffer.size()-1);
                this.buffer.remove(this.buffer.size()-1);
            }

            return packet;
        }

        catch (Exception e) {return null;}

        finally {this.lock.unlock();}
    }
}