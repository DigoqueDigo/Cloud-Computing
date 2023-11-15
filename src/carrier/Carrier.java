package carrier;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import packets.ConsultPacket;
import packets.HelloPacket;
import packets.JobPacket;
import packets.Packet;
import packets.UserPacket;
import packets.Packet.Type;


public class Carrier{

    private static Carrier carrier = null;

    private Carrier() {}


    public static Carrier getInstance(){
        if (Carrier.carrier == null) Carrier.carrier = new Carrier();
        return Carrier.carrier;
    }


    public void sendPacket(DataOutputStream dataOutputStream, Packet packet) throws IOException{

        if (packet instanceof HelloPacket) dataOutputStream.writeUTF(Type._HELLO_.name());
        else if (packet instanceof UserPacket) dataOutputStream.writeUTF(Type._USER_.name());
        else if (packet instanceof ConsultPacket) dataOutputStream.writeUTF(Type._CONSULT_.name());
        else dataOutputStream.writeUTF(Type._JOB_.name());

        byte[] data = packet.serialize();
        dataOutputStream.writeInt(data.length);
        dataOutputStream.write(data);
        dataOutputStream.flush();
    }


    public Packet receivePacket(DataInputStream dataInputStream) throws IOException{

        Packet resultPacket;
        Type type = Type.valueOf(dataInputStream.readUTF());
        int data_size = dataInputStream.readInt();
        byte data[] = new byte[data_size];

        if (Reader.read(dataInputStream,data,data_size) != data_size){
            throw new IOException("TCP packet reading incomplete");
        }

        switch (type){

            case _HELLO_:
                resultPacket = HelloPacket.deserialize(data);
                break;

            case _USER_:
                resultPacket = UserPacket.deserialize(data);
                break;

            case _CONSULT_:
                resultPacket = ConsultPacket.deserialize(data);
                break;

            default:
                resultPacket = JobPacket.deserialize(data);
                break;
        }

        return resultPacket;
    }
}