package client.machine;
import buffer.Buffer;
import packets.HelloPacket;
import packets.JobPacket;
import packets.MachinePacket;
import packets.Packet;
import packets.Packet.Protocol;


public class MachineClientWorker{

    Buffer inBuffer;
    Buffer outBuffer;
    Machine machine;


    public MachineClientWorker(Machine machine, Buffer inBuffer, Buffer outBuffer){
        this.machine = machine;
        this.inBuffer = inBuffer;
        this.outBuffer = outBuffer;
    }


    public void run(){

        try{

            Packet packetReceive;
            Packet packetSend = new HelloPacket(Protocol.CONNECT_MACHINE);
            this.outBuffer.addPacket(packetSend);
            
            System.out.println(packetSend);
            
            packetSend = new MachinePacket(Protocol.MACHINE_INFO,this.machine);
            outBuffer.addPacket(packetSend);

            System.out.println(packetSend);

            while ((packetReceive = inBuffer.getPacketBlock()) != null && (packetReceive.getProtocol() == Protocol.JOB)){

                MachineClientExecutor machineJobExecutor = new MachineClientExecutor(outBuffer,(JobPacket) packetReceive);
                new Thread(machineJobExecutor).start();
            }
        }

        catch (Exception e){
            this.outBuffer.addPacket(null);
        }
    }
}
