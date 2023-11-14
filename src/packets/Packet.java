package packets;
import java.io.IOException;


public abstract class Packet{

    public abstract byte[] serialize() throws IOException;
    
}
