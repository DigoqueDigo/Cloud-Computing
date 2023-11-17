package server.schedule;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import job.Job;
import packets.JobPacket;


public class Schedule{

    private ReentrantLock lock;
    private Condition condition;
    private List<JobPacket> jobContainer;


    public Schedule(){
        this.lock = new ReentrantLock();
        this.condition = this.lock.newCondition();
        this.jobContainer = new ArrayList<JobPacket>();
    }


    private void swapJobPacket(int positionA, int positionB){
        JobPacket jobPacket = this.jobContainer.get(positionA);
        this.jobContainer.set(positionA,this.jobContainer.get(positionB));
        this.jobContainer.set(positionB,jobPacket);
    }


    private void putJob(JobPacket jobPacket){

        this.jobContainer.add(jobPacket);
        int index = this.jobContainer.size() - 1;

        while (index > 0 && this.jobContainer.get(index-1).getJob().getTolerance() != 0){

            Job currentJob = this.jobContainer.get(index).getJob();
            Job previousJob = this.jobContainer.get(index-1).getJob();

            if (currentJob.getMemory() > previousJob.getMemory()) break;

            swapJobPacket(index-1,index);
            index--;
        }
    }


    public void addJobPacket(JobPacket jobPacket){

        try{
            this.lock.lock();
            this.putJob(jobPacket);
            this.condition.signalAll();
        }

        catch (Exception e) {}

        finally {this.lock.unlock();}
    }


    public JobPacket getJobPacket(){

        try{

            this.lock.lock();
            
            while (this.jobContainer.size() == 0){
                this.condition.await();
            }

            return this.jobContainer.get(0);
        }

        catch (Exception e) {return null;}

        finally {this.lock.unlock();}
    }


    public void removeJobPacket(){

        try{
            this.lock.lock();
            this.jobContainer.remove(0);
        }

        catch (Exception e) {}

        finally {this.lock.unlock();}
    }


    public void activateSignalAll(){
        this.condition.signalAll();
    }
}