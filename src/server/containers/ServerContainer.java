package server.containers;
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


    public ServerContainer(){
        this.userContainer = new UserContainer();
        this.resultContainer = new ResultContainer();
        this.machineContainer = new MachineContainer();
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

    public void addJobPacket(JobPacket jobPacket){
        this.schedule.addJobPacket(jobPacket);
    }

    public void removeJobPacket(JobPacket jobPacket){
        this.machineContainer.removeJobPacket(jobPacket);
        this.schedule.activateSignalAll();
    }

    public boolean addMachine(Machine machine){
        boolean result = this.machineContainer.addMachine(machine);
        if (result) this.schedule.activateSignalAll();
        return result;
    }
}