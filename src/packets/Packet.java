package packets;
import java.io.IOException;


public abstract class Packet{

    public enum Type {_HELLO_, _USER_, _JOB_, _CONSULT_}
    public enum Protocol {LOGIN, CREATE_ACCOUNT, USER, MACHINE, JOB, CONSULT, ERROR};
    
    private Protocol protocol;
    private String optionalMessage;

    
    public Packet(Protocol protocol){
        this.protocol = protocol;
        this.optionalMessage = "";
    }


    public Packet(Protocol protocol, String optionalMessage){
        this.protocol = protocol;
        this.optionalMessage = optionalMessage; 
    }


    public Protocol getProtocol(){
        return this.protocol;
    }


    public String getOptionalMessage(){
        return this.optionalMessage;
    }

    public String toString(){
        StringBuilder buffer = new StringBuilder();
        buffer.append("Protocol: ").append(this.protocol.name());
        buffer.append("\nMessage: ").append(this.optionalMessage);
        return buffer.toString();
    }

    public abstract byte[] serialize() throws IOException;
}