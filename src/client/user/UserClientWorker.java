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
    private UserClientUI clientUI;


    public UserClientWorker(Buffer inBuffer, Buffer outBuffer){
        this.inBuffer = inBuffer;
        this.outBuffer = outBuffer;
        this.clientUI = UserClientUI.getInstance();
    }


    public void run(){

        try{
            
            Packet packetReceive = null;
            Packet packetSend = new HelloPacket(Protocol.USER); // envia o pacote a dizer que é um client

            this.clientUI.checkFolders(UserClient.INPUT_FOLDER,UserClient.OUTPUT_FOLDER);
            this.outBuffer.addPacket(packetSend);
            System.out.println(packetSend);

            while (packetReceive == null || packetReceive.getProtocol() == Protocol.ERROR){

                packetSend = this.clientUI.getUserPacket(); // prepara o pacote de autenticação ou login
                this.outBuffer.addPacket(packetSend);
    
                packetReceive = this.inBuffer.getPacketBlock(); // a receber a resposta do servidor
                this.clientUI.showPacketMessage(packetReceive); // a mostrar a mensage enviada pelo servidor
                System.out.println(packetReceive);
            }

            while ((packetSend = this.clientUI.getPacket()) != null){
                
                this.outBuffer.addPacket(packetSend);
                System.out.println("Send Packet: " + packetSend);

                packetReceive = this.inBuffer.getPacketNonBlock();
                System.out.println("Receive Packet: " + packetReceive);
            }
        }

        catch (EOFException e){
            this.outBuffer.addPacket(null);
        }

        catch (FileNotFoundException e){
            this.clientUI.showInvalidFolder();
            this.outBuffer.addPacket(null);
        }
    }
}