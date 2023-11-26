package server.containers;
import java.util.Map;
import client.machine.Machine;
import client.user.User;
import packets.JobPacket;
import packets.Packet;
import server.schedule.Schedule;


public class ServerContainer{

    private Schedule schedule;
    private UserContainer userContainer;
    private ResultContainer resultContainer;
    private MachineContainer machineContainer;


    public ServerContainer(Schedule schedule, MachineContainer machineContainer){
        this.schedule = schedule;
        this.machineContainer = machineContainer;
        this.userContainer = new UserContainer();
        this.resultContainer = new ResultContainer();
    }

    public boolean addUser(User user){
        return this.userContainer.addUser(user);
    }

    public User getUser(String username){
        return this.userContainer.getUser(username);
    }

    public void addResultPacket(String nonce, Packet packet){
        this.resultContainer.addResultPacket(nonce,packet);
    }

    public Packet getResultPacket(String nonce){
        return this.resultContainer.getResultPacket(nonce);
    }

    public void removeResultEntry(String nonce){
        this.resultContainer.removeEntry(nonce);
    }

    public void addJobPacket(JobPacket jobPacket){
        this.schedule.addJobPacket(jobPacket);
    }

    public Packet getJobPacket(Machine machine){
        return this.machineContainer.getJobPacket(machine);
    }

    public void finalizeJobPacket(JobPacket jobPacket){
        this.machineContainer.finalizeJobPacket(jobPacket);
        this.schedule.activateSignalAll();
    }

    public void addMachine(Machine machine){
        this.machineContainer.addMachine(machine);
        this.schedule.activateSignalAll();
    }

    public int getMaxMemory(){
        return this.machineContainer.getMaxMemory();
    }

    public int getPendingJobs(){
        return this.schedule.size();
    }

    public Map<String,Integer> getSystemState(){
        return this.machineContainer.getSystemState();
    }
}