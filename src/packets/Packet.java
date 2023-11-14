package packets;
import java.io.IOException;


public abstract class Packet{

    public enum Protocol {LOGIN, CREATE_ACCOUNT, USER, MACHINE};
    private Protocol protocol;

    public Packet(Protocol protocol){
        this.protocol = protocol;
    }

    Protocol getProtocol(){
        return this.protocol;
    }

    public abstract byte[] serialize() throws IOException;

    public abstract Packet deserialize(byte[] data) throws IOException;
}