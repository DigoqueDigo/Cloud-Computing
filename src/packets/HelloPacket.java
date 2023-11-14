package packets;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;


public class HelloPacket extends Packet{

    
    public HelloPacket(Protocol protocol){
        super(protocol);
    }

    
    public byte[] serialize() throws IOException{

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);

        dataOutputStream.writeUTF(super.getProtocol().name());
        dataOutputStream.flush();

        return byteArrayOutputStream.toByteArray();
    }


    private static HelloPacket deserializePrivate(byte[] data) throws IOException{

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
        DataInputStream dataInputStream = new DataInputStream(byteArrayInputStream);

        Protocol protocol = Protocol.valueOf(dataInputStream.readUTF());

        return new HelloPacket(protocol);
    }


    public HelloPacket deserialize(byte[] data) throws IOException{
        return HelloPacket.deserializePrivate(data);
    }
}