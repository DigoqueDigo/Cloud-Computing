package packets;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.UUID;
import carrier.Reader;
import job.Job;


public class JobPacket extends Packet{

    private Job job;
    private String identifier;


    public JobPacket(Protocol protocol, Job job){
        super(protocol);
        this.job = job;
        this.identifier = UUID.randomUUID().toString();
    }


    public JobPacket(Protocol protocol, String optionalMessage, Job job, String identifier){
        super(protocol,optionalMessage);
        this.job = job;
        this.identifier = identifier;
    }


    public String getIdentifier(){
        return this.identifier;
    }


    public Job getJob(){
        return this.job;
    }


    public int hashCode(){
        return this.identifier.hashCode();
    }

    
    public boolean equals(Object obj){

        if (obj == null || this.getClass() != obj.getClass()) return false;
        
        JobPacket that = (JobPacket) obj;
        return this.identifier.equals(that.getIdentifier());
    }


    public String toString(){
        StringBuilder buffer = new StringBuilder();
        buffer.append(super.toString());
        buffer.append(("\nIdentifier: ")).append(this.identifier);
        buffer.append("\nJob: ").append(this.job.toString());
        return buffer.toString();
    }


    public byte[] serialize() throws IOException{

        byte[] data_job = this.job.serialize();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);

        dataOutputStream.writeUTF(super.getProtocol().name());
        dataOutputStream.writeUTF(super.getOptionalMessage());
        dataOutputStream.writeUTF(this.identifier);
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
        String identifier = dataInputStream.readUTF();
        byte[] data_job = new byte[dataInputStream.readInt()];
        Reader.read(dataInputStream,data_job,data_job.length);

        return new JobPacket(protocol,optionalMessage,Job.deserialize(data_job),identifier);
    }
}