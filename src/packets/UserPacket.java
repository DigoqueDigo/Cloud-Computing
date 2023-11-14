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
    private String message;


    public UserPacket(Protocol protocol, User user, String message){
        super(protocol);
        this.user = user;
        this.message = message;
    }


    public User getUser(){
        return this.user;
    }

    public String getMessage(){
        return this.message;
    }


    public void setMessage(String message){
        this.message = message;
    }


    public byte[] serialize() throws IOException{

        byte[] data_user = this.user.serialize();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);

        dataOutputStream.writeUTF(super.getProtocol().name());
        dataOutputStream.writeUTF(this.message);
        dataOutputStream.writeInt(data_user.length);
        dataOutputStream.write(data_user);
        dataOutputStream.flush();

        return byteArrayOutputStream.toByteArray();
    }
    
    
    private static UserPacket deserializePrivate(byte[] data) throws IOException{

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
        DataInputStream dataInputStream = new DataInputStream(byteArrayInputStream);

        Protocol protocol = Protocol.valueOf(dataInputStream.readUTF());
        String message = dataInputStream.readUTF();
        byte[] data_user = new byte[dataInputStream.readInt()];
        Reader.read(dataInputStream,data_user,data_user.length);

        return new UserPacket(protocol,User.deserialize(data_user),message);
    }


    public UserPacket deserialize(byte[] data) throws IOException{
        return UserPacket.deserializePrivate(data);
    }
}