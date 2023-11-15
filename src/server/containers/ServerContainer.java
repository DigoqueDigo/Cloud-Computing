package server.containers;
import job.Job;
import packets.Packet;
import user.User;


public class ServerContainer{

    private UserContainer userContainer;
    private ResultContainer resultContainer;
    private JobContainer jobContainer;


    public ServerContainer(){
        this.userContainer = new UserContainer();
        this.resultContainer = new ResultContainer();
        this.jobContainer = new JobContainer();
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

    public void addJob(Job job){
        this.jobContainer.addJob(job);
    }

    public Job getJob(){
        return this.jobContainer.getJob();
    }
}