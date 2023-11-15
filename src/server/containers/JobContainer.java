package server.containers;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import job.Job;


public class JobContainer{

    private ReentrantLock lock;
    private Condition condition;
    private List<Job> jobContainer;


    public JobContainer(){
        this.lock = new ReentrantLock();
        this.condition = this.lock.newCondition();
        this.jobContainer = new ArrayList<Job>();
    }


    public void addJob(Job job){

        try{
            this.lock.lock();
            this.jobContainer.add(job);
            this.condition.signalAll();
        }

        catch (Exception e) {}

        finally {this.lock.unlock();}
    }


    public Job getJob(){

        try{

            this.lock.lock();

            while (this.jobContainer.size() == 0){
                this.condition.await();
            }

            Job job = this.jobContainer.get(this.jobContainer.size()-1);
            this.jobContainer.remove(this.jobContainer.size()-1);
            return job;
        }

        catch (Exception e) {return null;}

        finally {this.lock.unlock();}
    }
}