package client.user;
import java.io.EOFException;
import java.io.FileNotFoundException;
import buffer.Buffer;
import packets.HelloPacket;
import packets.Packet;
import packets.Packet.Protocol;


public class UserClientWorker{

    private Buffer inBuffer;
    private Buffer outBuffer;
    private Buffer jobBuffer;
    private UserClientUI clientUI;


    public UserClientWorker(Buffer inBuffer, Buffer outBuffer, Buffer jobBuffer){
        this.inBuffer = inBuffer;
        this.outBuffer = outBuffer;
        this.jobBuffer = jobBuffer;
        this.clientUI = UserClientUI.getInstance();
    }


    private void collectPackets(){        
        Packet packetReceive;   
        while ((packetReceive = this.inBuffer.getPacketNonBlock()) != null){   
            this.clientUI.showPacket(packetReceive);
            if (packetReceive.getProtocol() == Protocol.JOB) this.jobBuffer.addPacket(packetReceive);
        }
    }


    public void run(){

        try{
            
            Packet packetReceive = null;
            Packet packetSend = new HelloPacket(Protocol.CONNECT_USER);

            this.clientUI.checkFolders(UserClient.INPUT_FOLDER,UserClient.OUTPUT_FOLDER);
            this.outBuffer.addPacket(packetSend);
            System.out.println(packetSend);

            while (packetReceive == null || packetReceive.getProtocol() == Protocol.ERROR){

                packetSend = this.clientUI.getUserPacket();
                this.outBuffer.addPacket(packetSend);
    
                packetReceive = this.inBuffer.getPacketBlock();
                this.clientUI.showPacketMessage(packetReceive);
                System.out.println(packetReceive);
            }

            while ((packetSend = this.clientUI.getPacket()) != null){

                if (packetSend.getProtocol() == Protocol.ERROR) collectPackets();
                
                else{
                    this.outBuffer.addPacket(packetSend);
                    if (packetSend.getProtocol() == Protocol.JOB) this.clientUI.showPacket(packetSend);
                }
            }
        }

        catch (EOFException e){
            collectPackets();
            this.outBuffer.addPacket(null);
            this.jobBuffer.addPacket(null);
        }

        catch (FileNotFoundException e){
            this.clientUI.showInvalidFolder();
            this.outBuffer.addPacket(null);
            this.jobBuffer.addPacket(null);
        }
    }
}