package buffer;
import java.io.DataOutputStream;

import carrier.Carrier;
import packets.Packet;


public class BufferToSocket implements Runnable{

    private Buffer outBuffer;
    private DataOutputStream outputStream;

    
    public BufferToSocket(Buffer outBuffer, DataOutputStream outputStream){
        this.outBuffer = outBuffer;
        this.outputStream = outputStream;
    }
    

    public void run(){

        try{

            Packet packet;
            Carrier carrier = Carrier.getInstance();

            while ((packet = outBuffer.getPacketBlock()) != null){
                carrier.sendPacket(outputStream,packet);
            }
        }

        catch (Exception e){
            e.printStackTrace();
        }
    }
}