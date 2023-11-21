package server.schedule;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;
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

    public void activateSignalAll(){
        
        try{
            this.lock.lock();
            this.condition.signalAll();
        }

        catch (Exception e) {}
        finally {this.lock.unlock();}
    }


    public void activateAwait(){

        try{
            this.lock.lock();
            this.condition.await();
        }

        catch (Exception e) {}
        finally {this.lock.unlock();}
    }


    private void insertJobPacket(JobPacket jobPacket){

        this.jobContainer.add(jobPacket);
        int index = this.jobContainer.size() - 1;

        while (index > 0 && this.jobContainer.get(index-1).getJob().getTolerance() != 0){

            Job currentJob = this.jobContainer.get(index).getJob();
            Job previousJob = this.jobContainer.get(index-1).getJob();

            if (currentJob.getMemory() > previousJob.getMemory()) break;

            swapJobPacket(index-1,index);
            previousJob.decreaseTolerance();
            index--;
        }
    }


    public void addJobPacket(JobPacket jobPacket){

        try{
            this.lock.lock();
            this.insertJobPacket(jobPacket);
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


    public int size(){
        
        try{
            this.lock.lock();
            return this.jobContainer.size();
        }

        catch (Exception e) {return 0;}
        finally {this.lock.unlock();}
    }


    public String toString(){

        try{
            this.lock.lock();
            StringBuilder buffer = new StringBuilder();
            buffer.append("SCHEDULE");
            buffer.append(this.jobContainer.stream().map(x -> x.toString()).collect(Collectors.joining("\n","\n","\n")));
            return buffer.toString();
        }

        catch (Exception e) {return "";}
        finally {this.lock.unlock();}
    }
}