package packets;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import carrier.Reader;
import job.Consult;


public class ConsultPacket extends Packet{

    private Consult consult;


    public ConsultPacket(Protocol protocol){
        super(protocol);
        this.consult = new Consult();
    }


    public ConsultPacket(Protocol protocol, Consult consult){
        super(protocol);
        this.consult = consult;
    }


    public ConsultPacket(Protocol protocol, String optinalMessage, Consult consult){
        super(protocol,optinalMessage);
        this.consult = consult;
    }


    public String toString(){
        StringBuilder buffer = new StringBuilder();
        buffer.append(super.toString());
        buffer.append("\nConsult: ").append(this.consult.toString());
        return buffer.toString();
    }

    
    public byte[] serialize() throws IOException{

        byte[] consult_data = this.consult.serialize();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);

        dataOutputStream.writeUTF(super.getProtocol().name());
        dataOutputStream.writeUTF(super.getOptionalMessage());
        dataOutputStream.writeInt(consult_data.length);
        dataOutputStream.write(consult_data);
        dataOutputStream.flush();

        return byteArrayOutputStream.toByteArray();
    }


    public static ConsultPacket deserialize(byte[] dataConsult) throws IOException{

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(dataConsult);
        DataInputStream dataInputStream = new DataInputStream(byteArrayInputStream);

        Protocol protocol = Protocol.valueOf(dataInputStream.readUTF());
        String optionalMessage = dataInputStream.readUTF();
        byte[] data = new byte[dataInputStream.readInt()];
        Reader.read(dataInputStream,data,data.length);

        return new ConsultPacket(protocol,optionalMessage,Consult.deserialize(data));
    }
}