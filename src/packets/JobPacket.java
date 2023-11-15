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
    private String optionalMessage;


    public JobPacket(Protocol protocol, Job job){
        super(protocol);
        this.job = job;
    }


    public JobPacket(Protocol protocol, Job job, String optionalMessage){
        super(protocol);
        this.job = job;
        this.optionalMessage = optionalMessage;
    }


    public byte[] serialize() throws IOException{

        byte[] data_job = this.job.serialize();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);

        dataOutputStream.writeUTF(super.getProtocol().name());
        dataOutputStream.writeInt(data_job.length);
        dataOutputStream.write(data_job);
        dataOutputStream.writeUTF(this.optionalMessage);
        dataOutputStream.flush();

        return byteArrayOutputStream.toByteArray();
    }


    private static JobPacket deserializePrivate(byte[] data) throws IOException{

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
        DataInputStream dataInputStream = new DataInputStream(byteArrayInputStream);

        Protocol protocol = Protocol.valueOf(dataInputStream.readUTF());
        byte[] data_job = new byte[dataInputStream.readInt()];
        Reader.read(dataInputStream,data_job,data_job.length);
        String optionalMessage = dataInputStream.readUTF();

        return new JobPacket(protocol,Job.deserialize(data_job),optionalMessage);
    }


    public JobPacket deserialize(byte[] data) throws IOException{
        return JobPacket.deserializePrivate(data);
    }    
}