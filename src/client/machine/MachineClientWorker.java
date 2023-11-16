package client.machine;
import buffer.Buffer;
import packets.HelloPacket;
import packets.MachinePacket;
import packets.Packet;
import packets.Packet.Protocol;


public class MachineClientWorker{

    Buffer inBuffer;
    Buffer outBuffer;
    Machine machine;
    MachineClientUI machineClientUI;


    public MachineClientWorker(Machine machine, Buffer inBuffer, Buffer outBuffer){
        this.machine = machine;
        this.inBuffer = inBuffer;
        this.outBuffer = outBuffer;
        this.machineClientUI = MachineClientUI.getInstance();
    }


    public void run(){

        try{

            Packet packetReceive = null;
            Packet packetSend = new HelloPacket(Protocol.MACHINE);

            this.outBuffer.addPacket(packetSend);
            
            packetSend = new MachinePacket(Protocol.CONNECT_MACHINE,this.machine);
            packetReceive = this.inBuffer.getPacketBlock();

            if (packetReceive == null) throw new Exception();
            
            this.machineClientUI.showPacketMessage(packetReceive);

            while (packetReceive.getProtocol() != Protocol.ERROR){
                
            }
        }

        catch (Exception e){
            this.outBuffer.addPacket(null);
        }
    }
}
