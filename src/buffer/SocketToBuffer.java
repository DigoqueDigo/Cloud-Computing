package buffer;
import java.io.DataInputStream;

import carrier.Carrier;
import packets.Packet;


public class SocketToBuffer implements Runnable{

    private Buffer inBuffer; 
    private DataInputStream inputStream;   

    
    public SocketToBuffer(Buffer inBuffer, DataInputStream inputStream){
        this.inBuffer = inBuffer;
        this.inputStream = inputStream;
    }

    
    public void run(){

        try{
            
            Packet packet;
            Carrier carrier = Carrier.getInstance();

            while ((packet = carrier.receivePacket(inputStream)) != null){
                this.inBuffer.addPacket(packet);
            }
        }

        catch (Exception e){
            e.printStackTrace();
        }
    }
}