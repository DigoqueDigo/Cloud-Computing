package packets;
import java.io.IOException;


public abstract class Packet{

    public enum Protocol {LOGIN, CREATE_ACCOUNT, USER, MACHINE, JOB, CONSULT, ERROR};
    private Protocol protocol;

    public Packet(Protocol protocol){
        this.protocol = protocol;
    }

    public Protocol getProtocol(){
        return this.protocol;
    }

    public void setProtocol(Protocol protocol){
        this.protocol = protocol; 
    }

    public abstract byte[] serialize() throws IOException;

    public abstract Packet deserialize(byte[] data) throws IOException;
}