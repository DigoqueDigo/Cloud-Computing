package client.machine;
import buffer.Buffer;
import job.Job;
import packets.JobPacket;
import packets.Packet.Protocol;
import sd23.JobFunction;
import sd23.JobFunctionException;


public class MachineClientExecutor implements Runnable{

    private Buffer outBuffer;
    private JobPacket jobPacket;

    
    public MachineClientExecutor(Buffer outBuffer, JobPacket jobPacket){
        this.outBuffer = outBuffer;
        this.jobPacket = jobPacket;
    }

    
    public void run(){

        String message;
        JobPacket result;
        Job job = jobPacket.getJob();

        try{
            byte[] output = JobFunction.execute(jobPacket.getJob().getData());
            message = "Job successful: " + jobPacket.getJob().getIdentifier();
            message += " (returned " + output.length + " bytes)";
            job = new Job(job.getTolerance(),job.getMemory(),output,job.getIdentifier());
            result = new JobPacket(Protocol.JOB,message,job);
        }

        catch (JobFunctionException e){ 
            message = "Job failed: " + jobPacket.getJob().getIdentifier(); 
            message += " (code = " + e.getCode() + " message = " + e.getMessage() + ")";
            job = new Job(job.getTolerance(),job.getMemory(),new byte[0],job.getIdentifier());
            result = new JobPacket(Protocol.ERROR,message,job);
        }

        result.setClientNonce(jobPacket.getClientNonce());
        result.setMachineNonce(jobPacket.getMachineNonce());
        this.outBuffer.addPacket(result);
    }
}