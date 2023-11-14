package packets;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;


public class HelloPacket extends Packet{

    public enum HelloPacketProtocol {USER, MACHINE};

    public HelloPacketProtocol protocol;

    
    public HelloPacket(HelloPacketProtocol protocol){
        this.protocol = protocol;
    }
    

    public HelloPacketProtocol getProtocol(){
        return this.protocol;
    }


    public byte[] serialize() throws IOException{

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);

        dataOutputStream.writeUTF(this.protocol.name());

        return byteArrayOutputStream.toByteArray();
    }


    public static HelloPacket deserialize(byte[] data) throws IOException{

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
        DataInputStream dataInputStream = new DataInputStream(byteArrayInputStream);

        HelloPacketProtocol protocol = HelloPacketProtocol.valueOf(dataInputStream.readUTF());

        return new HelloPacket(protocol);
    }
}