package packets;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import carrier.Reader;
import user.User;


public class UserPacket extends Packet{

    private User user;


    public UserPacket(Protocol protocol, User user){
        super(protocol);
        this.user = user;
    }


    public UserPacket(Protocol protocol, String optionalMessage, User user){
        super(protocol,optionalMessage);
        this.user = user;
    }


    public User getUser(){
        return this.user;
    }


    public String toString(){
        StringBuilder buffer = new StringBuilder();
        buffer.append(super.toString());
        buffer.append("\nUser: ").append(this.user.toString());
        return buffer.toString();
    }


    public byte[] serialize() throws IOException{

        byte[] data_user = this.user.serialize();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);

        dataOutputStream.writeUTF(super.getProtocol().name());
        dataOutputStream.writeUTF(super.getOptionalMessage());
        dataOutputStream.writeInt(data_user.length);
        dataOutputStream.write(data_user);
        dataOutputStream.flush();

        return byteArrayOutputStream.toByteArray();
    }
    
    
    public static UserPacket deserialize(byte[] data) throws IOException{

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
        DataInputStream dataInputStream = new DataInputStream(byteArrayInputStream);

        Protocol protocol = Protocol.valueOf(dataInputStream.readUTF());
        String optionalMessage = dataInputStream.readUTF();
        byte[] data_user = new byte[dataInputStream.readInt()];
        Reader.read(dataInputStream,data_user,data_user.length);

        return new UserPacket(protocol,optionalMessage,User.deserialize(data_user));
    }
}