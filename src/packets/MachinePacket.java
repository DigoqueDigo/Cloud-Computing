package packets;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import carrier.Reader;
import client.machine.Machine;


public class MachinePacket extends Packet{

    private Machine machine;


    public MachinePacket(Protocol protocol, Machine machine){
        super(protocol);
        this.machine = machine;
    }


    public MachinePacket(Protocol protocol, String optionalMessage, Machine machine){
        super(protocol,optionalMessage);
        this.machine = machine;
    }


    public Machine getMachine(){
        return this.machine;
    }


    public byte[] serialize() throws IOException{

        byte[] data_machine = this.machine.serialize();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);

        dataOutputStream.writeUTF(super.getProtocol().name());
        dataOutputStream.writeUTF(super.getOptionalMessage());
        dataOutputStream.writeInt(data_machine.length);
        dataOutputStream.write(data_machine);
        dataOutputStream.flush();

        return byteArrayOutputStream.toByteArray();
    }


    public static MachinePacket deserialize(byte[] data) throws IOException{

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
        DataInputStream dataInputStream = new DataInputStream(byteArrayInputStream);

        Protocol protocol = Protocol.valueOf(dataInputStream.readUTF());
        String optionalMessage = dataInputStream.readUTF();
        byte[] data_machine = new byte[dataInputStream.readInt()];
        Reader.read(dataInputStream,data_machine,data_machine.length);

        return new MachinePacket(protocol,optionalMessage,Machine.deserialize(data_machine));
    }
}