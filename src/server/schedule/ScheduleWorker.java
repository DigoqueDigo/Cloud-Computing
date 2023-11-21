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

            System.out.println(this.schedule);

            while ((jobPacket = this.schedule.getJobPacket()) != null){

                System.out.println(this.schedule);
                
                if (this.machineContainer.addJobPacket(jobPacket)){
                    this.schedule.removeJobPacket();
                    System.out.println(this.schedule);
                }

                else this.schedule.activateAwait();
            }
        }

        catch (Exception e){
            e.printStackTrace();
        }
    }
}