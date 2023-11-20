package client.machine;
import buffer.Buffer;
import job.Job;
import packets.HelloPacket;
import packets.JobPacket;
import packets.MachinePacket;
import packets.Packet;
import packets.Packet.Protocol;
import sd23.JobFunction;
import sd23.JobFunctionException;


public class MachineClientWorker{

    Buffer inBuffer;
    Buffer outBuffer;
    Machine machine;


    public MachineClientWorker(Machine machine, Buffer inBuffer, Buffer outBuffer){
        this.machine = machine;
        this.inBuffer = inBuffer;
        this.outBuffer = outBuffer;
    }


    public JobPacket executeJob(JobPacket jobPacket){

        Job job;
        String message;
        JobPacket result;

        try{
            byte[] output = JobFunction.execute(jobPacket.getJob().getData());
            message = "Success, returned " + output.length + " bytes";
            job = new Job(output,message); 
        }

        catch (JobFunctionException e){   
            message = "Job failed: code = " + e.getCode() + " message = " + e.getMessage();
            job = new Job(new byte[0],message);
        }

        result = new JobPacket(Protocol.JOB_COMPLETED,job);
        result.setClientNonce(jobPacket.getClientNonce());
        result.setMachineNonce(jobPacket.getMachineNonce());
        
        return result;
    }


    public void run(){

        try{

            Packet packetReceive;
            Packet packetSend = new HelloPacket(Protocol.CONNECT_MACHINE);
            this.outBuffer.addPacket(packetSend);
            
            System.out.println(packetSend);
            
            packetSend = new MachinePacket(Protocol.MACHINE_INFO,this.machine);
            outBuffer.addPacket(packetSend);

            System.out.println(packetSend);

            while ((packetReceive = inBuffer.getPacketBlock()) != null && (packetReceive.getProtocol() == Protocol.JOB)){

                packetSend = executeJob((JobPacket) packetReceive);
                outBuffer.addPacket(packetSend);
            }
        }

        catch (Exception e){
            this.outBuffer.addPacket(null);
        }
    }
}
