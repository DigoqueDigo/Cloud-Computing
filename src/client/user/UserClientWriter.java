package client.user;
import java.io.FileOutputStream;
import buffer.Buffer;
import packets.JobPacket;
import packets.Packet;
import packets.Packet.Protocol;


public class UserClientWriter implements Runnable{

    private Buffer inBuffer;

    
    public UserClientWriter(Buffer inBuffer){
        this.inBuffer = inBuffer;
    }

    
    public void run(){

        Packet packet;
        JobPacket jobPacket;

        while ((packet = inBuffer.getPacketBlock()) != null && packet.getProtocol() == Protocol.JOB){

            jobPacket = (JobPacket) packet;

            try{
                FileOutputStream outputStream = new FileOutputStream(UserClient.OUTPUT_FOLDER + jobPacket.getJob().getIdentifier());
                outputStream.write(jobPacket.getJob().getData());
                outputStream.close();
            }

            catch (Exception e){
                System.out.println("Can not save job: " + jobPacket.getJob().getIdentifier());
            }
        }
    }
}