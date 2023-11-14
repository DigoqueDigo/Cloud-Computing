package carrier;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import packets.HelloPacket;
import packets.Packet;
import packets.Packet.Protocol;


public class Carrier{

    private static Carrier carrier = null;

    private Carrier() {}

    public static Carrier getInstance(){
        if (Carrier.carrier == null) Carrier.carrier = new Carrier();
        return Carrier.carrier;
    }


    public void sendPacket(DataOutputStream dataOutputStream, Packet packet){
        
        try{
            byte[] data = packet.serialize();
            dataOutputStream.writeInt(data.length);
            dataOutputStream.write(data);
            dataOutputStream.flush();
        }

        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }


    public Packet receivePacket(DataInputStream dataInputStream) throws Exception{

        Packet packet = new HelloPacket(Protocol.USER);
        int data_size = dataInputStream.readInt();
        byte data[] = new byte[data_size];

        if (Reader.read(dataInputStream,data,data_size) != data_size){
            throw new Exception("TCP packet reading incomplete");
        }

        return packet.deserialize(data);
    }
}