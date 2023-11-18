package server.schedule;
import packets.JobPacket;
import server.containers.MachineContainer;


public class ScheduleWorker implements Runnable{

    private Schedule schedule;
    private MachineContainer machineContainer;


    public ScheduleWorker(Schedule schedule, MachineContainer machineContainer){
        this.schedule = schedule;
        this.machineContainer = machineContainer;
    }


    public void run(){

        try{

            JobPacket jobPacket;

            while ((jobPacket = this.schedule.getJobPacket()) != null){
                
                if (this.machineContainer.addJobPacket(jobPacket)){
                    this.schedule.removeJobPacket();
                }

                else this.schedule.activateAwait();
            }
        }

        catch (Exception e){
            e.printStackTrace();
        }
    }
}