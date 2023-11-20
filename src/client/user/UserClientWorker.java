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
    private Buffer joBuffer;
    private UserClientUI clientUI;


    public UserClientWorker(Buffer inBuffer, Buffer outBuffer, Buffer jobBuffer){
        this.inBuffer = inBuffer;
        this.outBuffer = outBuffer;
        this.joBuffer = jobBuffer;
        this.clientUI = UserClientUI.getInstance();
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

                if (packetSend.getProtocol() == Protocol.ERROR){
                    
                    while ((packetReceive = this.inBuffer.getPacketNonBlock()) != null){   
                        this.clientUI.showPacket(packetReceive);
                        if (packetReceive.getProtocol() == Protocol.JOB) this.joBuffer.addPacket(packetReceive);
                    }
                }
                
                else{
                    this.outBuffer.addPacket(packetSend);
                    if (packetSend.getProtocol() == Protocol.JOB) this.clientUI.showPacket(packetSend);
                }
            }
        }

        catch (EOFException e){
            this.outBuffer.addPacket(null);
            this.joBuffer.addPacket(null);
        }

        catch (FileNotFoundException e){
            this.clientUI.showInvalidFolder();
            this.outBuffer.addPacket(null);
            this.joBuffer.addPacket(null);
        }
    }
}