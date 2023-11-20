package packets;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import carrier.Reader;
import job.Job;


public class JobPacket extends Packet{

    private Job job;
    private String clientNonce;
    private String machineNonce;


    public JobPacket(Protocol protocol, Job job){
        super(protocol);
        this.job = job;
        this.clientNonce = "";
        this.machineNonce = "";
    }


    public JobPacket(Protocol protocol, String optionalMessage, Job job){
        super(protocol,optionalMessage);
        this.job = job;
        this.clientNonce = "";
        this.machineNonce = "";
    }


    public JobPacket(Protocol protocol, String optionalMessage, Job job, String clientNonce, String machineNonce){
        super(protocol,optionalMessage);
        this.job = job;
        this.clientNonce = clientNonce;
        this.machineNonce = machineNonce;
    }

    
    public Job getJob(){
        return this.job;
    }


    public String getClientNonce(){
        return this.clientNonce;
    }


    public String getMachineNonce(){
        return this.machineNonce;
    }


    public void setClientNonce(String clientNonce){
        this.clientNonce = clientNonce;
    }


    public void setMachineNonce(String machineNonce){
        this.machineNonce = machineNonce;
    }


    public String toString(){
        StringBuilder buffer = new StringBuilder();
        buffer.append(super.toString());
        buffer.append(("\nClientNonce: ")).append(this.clientNonce);
        buffer.append("\nMachineNonce: ").append(this.machineNonce);
        buffer.append("\nJob: ").append(this.job.toString());
        return buffer.toString();
    }


    public byte[] serialize() throws IOException{

        byte[] data_job = this.job.serialize();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);

        dataOutputStream.writeUTF(super.getProtocol().name());
        dataOutputStream.writeUTF(super.getOptionalMessage());
        dataOutputStream.writeUTF(this.clientNonce);
        dataOutputStream.writeUTF(this.machineNonce);
        dataOutputStream.writeInt(data_job.length);
        dataOutputStream.write(data_job);
        dataOutputStream.flush();

        return byteArrayOutputStream.toByteArray();
    }


    public static JobPacket deserialize(byte[] data) throws IOException{

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
        DataInputStream dataInputStream = new DataInputStream(byteArrayInputStream);

        Protocol protocol = Protocol.valueOf(dataInputStream.readUTF());
        String optionalMessage = dataInputStream.readUTF();
        String clientNonce = dataInputStream.readUTF();
        String machineNonce = dataInputStream.readUTF();
        byte[] data_job = new byte[dataInputStream.readInt()];
        Reader.read(dataInputStream,data_job,data_job.length);

        return new JobPacket(protocol,optionalMessage,Job.deserialize(data_job),clientNonce,machineNonce);
    }
}