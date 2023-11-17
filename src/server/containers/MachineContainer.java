package server.containers;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import client.machine.Machine;
import packets.JobPacket;


public class MachineContainer{

    private ReentrantLock lock;
    private Condition condition;
    private Map<Machine,List<JobPacket>> waitContainer;
    private Map<Machine,List<JobPacket>> inExcecutionContainer;


    public MachineContainer(){
        this.lock = new ReentrantLock();
        this.condition = this.lock.newCondition();
        this.waitContainer = new HashMap<Machine,List<JobPacket>>();
        this.inExcecutionContainer = new HashMap<Machine,List<JobPacket>>();
    }


    private Machine getAvaiableMachine(int minMemory){
        
        Comparator<Machine> comparator = (x,y) -> 
            Integer.compare(x.getAvaiableMemory(),y.getAvaiableMemory());

        return this.waitContainer.entrySet()
                .stream()
                .map(x -> x.getKey())
                .filter(x -> x.getAvaiableMemory() >= minMemory)
                .sorted(comparator)
                .findFirst()
                .orElse(null);
    }


    public boolean addMachine(Machine machine){

        try{
            this.lock.lock();
            boolean result = this.waitContainer.containsKey(machine);
            
            if (!result){
                this.waitContainer.put(machine,new ArrayList<JobPacket>());
                this.inExcecutionContainer.put(machine,new ArrayList<JobPacket>());
            }

            return !result;
        }

        catch (Exception e) {return false;}

        finally {this.lock.unlock();}
    }


    public boolean addJobPacket(JobPacket jobPacket){

        try{
            
            this.lock.lock();
            Machine machine = this.getAvaiableMachine(jobPacket.getJob().getMemory());

            if (machine != null){
                this.waitContainer.get(machine).add(jobPacket);
                machine.decreaseMemory(jobPacket.getJob().getMemory());
                this.condition.signalAll();
            }

            return machine != null;
        }

        catch (Exception e) {return false;}

        finally {this.lock.unlock();}
    }


    public JobPacket getJobPacket(Machine machine){

        try{
            
            this.lock.lock();
            
            List<JobPacket> jobPackets = this.waitContainer.get(machine); 
            JobPacket jobPacket = jobPackets.get(jobPackets.size()-1);

            while (jobPacket != null){
                this.condition.await();
                jobPackets = this.waitContainer.get(machine);
                jobPacket = jobPackets.get(jobPackets.size()-1);
            }

            this.inExcecutionContainer.get(machine).add(jobPacket);
            this.waitContainer.get(machine).remove(jobPackets.size()-1);

            return jobPacket;
        }

        catch (Exception e) {return null;}

        finally {this.lock.unlock();}
    }


    public void removeJobPacket(JobPacket jobPacket){

        try{

            this.lock.lock();
            
            Machine machine = this.inExcecutionContainer.entrySet()
                .stream()
                .filter(x -> x.getValue().contains(jobPacket))
                .map(x -> x.getKey())
                .findFirst()
                .orElse(null);

            if (machine != null) machine.decreaseMemory(jobPacket.getJob().getMemory());
        }

        catch (Exception e) {}

        finally {this.lock.unlock();}
    }
}