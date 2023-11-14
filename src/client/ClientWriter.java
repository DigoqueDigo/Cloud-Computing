package client;
import java.io.DataOutputStream;
import carrier.Carrier;
import packets.Packet;


public class ClientWriter implements Runnable{

    private Buffer outBuffer;
    private DataOutputStream outputStream;

    
    public ClientWriter(Buffer outBuffer, DataOutputStream outputStream){
        this.outBuffer = outBuffer;
        this.outputStream = outputStream;
    }
    

    public void run(){

        Packet packet;
        Carrier carrier = Carrier.getInstance();

        while ((packet = outBuffer.getPacket()) != null){
            carrier.sendPacket(outputStream,packet);
        }
    }
}