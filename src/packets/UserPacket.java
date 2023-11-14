package packets;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import carrier.Reader;
import user.User;


public class UserPacket extends Packet{

    public enum UserPacketProtocol {LOGIN, CREATE_ACCOUNT};
    
    private UserPacketProtocol protocol;
    private User user;


    public UserPacket(UserPacketProtocol protocol, User user){
        this.protocol = protocol;
        this.user = user;
    }


    public UserPacketProtocol getProtocol(){
        return this.protocol;
    }


    public byte[] serialize() throws IOException{

        byte[] data_user = this.user.serialize();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);

        dataOutputStream.writeUTF(this.protocol.name());
        dataOutputStream.writeInt(data_user.length);
        dataOutputStream.write(data_user);

        return byteArrayOutputStream.toByteArray();
    }
    
    
    public UserPacket deserialize(byte[] data) throws IOException{

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
        DataInputStream dataInputStream = new DataInputStream(byteArrayInputStream);

        UserPacketProtocol protocol = UserPacketProtocol.valueOf(dataInputStream.readUTF());
        byte[] data_user = new byte[dataInputStream.readInt()];
        Reader.read(dataInputStream,data_user,data_user.length);

        return new UserPacket(protocol,User.deserialize(data_user));
    }
}