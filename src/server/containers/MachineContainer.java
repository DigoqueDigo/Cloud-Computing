package server.containers;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;
import client.machine.Machine;
import packets.JobPacket;
import packets.Packet;


public class MachineContainer{

    private ReentrantLock lock;
    private Condition condition;
    private Map<Machine,List<JobPacket>> machineContainer;


    public MachineContainer(){
        this.lock = new ReentrantLock();
        this.condition = this.lock.newCondition();
        this.machineContainer = new HashMap<Machine,List<JobPacket>>();
    }


    private Machine getAvaiableMachine(int minMemory){
        
        Comparator<Machine> comparator = (x,y) -> 
            Integer.compare(x.getAvaiableMemory(),y.getAvaiableMemory());

        return this.machineContainer.entrySet()
                .stream()
                .map(x -> x.getKey())
                .filter(x -> x.getAvaiableMemory() >= minMemory)
                .sorted(comparator)
                .findFirst()
                .orElse(null);
    }


    public void addMachine(Machine machine){

        try{
            this.lock.lock();
            this.machineContainer.putIfAbsent(machine,new ArrayList<JobPacket>());
        }

        catch (Exception e) {}
        finally {this.lock.unlock();}
    }


    public boolean addJobPacket(JobPacket jobPacket){

        try{

            this.lock.lock();
            Machine machine = this.getAvaiableMachine(jobPacket.getJob().getMemory());

            if (machine != null){
                
                jobPacket.setMachineNonce(machine.getIdentifier());
                machine.decreaseMemory(jobPacket.getJob().getMemory());
                
                this.machineContainer.get(machine).add(jobPacket);
                this.condition.signalAll();
            }
            
            return machine != null;
        }

        catch (Exception e) {return false;}
        finally {this.lock.unlock();}
    }


    public Packet getJobPacket(Machine machine){

        try{
            
            this.lock.lock();
            List<JobPacket> list_packets = this.machineContainer.get(machine); 

            while (list_packets == null || list_packets.size() == 0){
                this.condition.await();
                list_packets = this.machineContainer.get(machine);
            }

            Packet packet = list_packets.get(0);
            list_packets.remove(0);

            return packet;
        }

        catch (Exception e) {return null;}
        finally {this.lock.unlock();}
    }


    public void finalizeJobPacket(JobPacket jobPacket){

        try{

            this.lock.lock();
            this.machineContainer
                .keySet()
                .stream()
                .filter(x -> x.getIdentifier().equals(jobPacket.getMachineNonce()))
                .forEach(x -> x.increaseMemory(jobPacket.getJob().getMemory()));
        }

        catch (Exception e) {}
        finally {this.lock.unlock();}
    }


    public int getPendingJobs(){
        
        try{
            this.lock.lock();
            return this.machineContainer.values().stream().mapToInt(x -> x.size()).sum();
        }

        catch (Exception e) {return -1;}
        finally {this.lock.unlock();}
    }


    public Map<String,Integer> getSystemState(){

        try{
            this.lock.lock();
            return this.machineContainer.keySet().stream().collect(Collectors.toMap(
                x -> x.getIdentifier(),
                x -> x.getAvaiableMemory()));
        }

        catch (Exception e) {return null;}
        finally {this.lock.unlock();}
    }


    public int getMaxMemory(){

        try{
            this.lock.lock();
            return this.machineContainer.keySet().stream().mapToInt(x -> x.getMemory()).max().orElse(-1);
        }

        catch (Exception e) {return -1;}
        finally {this.lock.unlock();}
    }


    public String toString(){
        
        try{
            this.lock.lock();
            return this.machineContainer.entrySet().stream().map(x -> x.toString()).collect(Collectors.joining("\n"));
        }

        catch (Exception e) {return "";}
        finally {this.lock.unlock();}
    }
}