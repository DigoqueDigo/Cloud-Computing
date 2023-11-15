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
    private String optionalMessage;


    public ConsultPacket(){
        super(Protocol.CONSULT);
        this.consult = new Consult();
        this.optionalMessage = "";
    }


    public ConsultPacket(Consult consult){
        super(Protocol.CONSULT);
        this.consult = consult;
        this.optionalMessage = "";
    }


    public ConsultPacket(Consult consult, String optinalMessage){
        super(Protocol.CONSULT);
        this.consult = consult;
        this.optionalMessage = optinalMessage;
    }

    
    public byte[] serialize() throws IOException{

        byte[] consult_data = this.consult.serialize();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);

        dataOutputStream.writeInt(consult_data.length);
        dataOutputStream.write(consult_data);
        dataOutputStream.writeUTF(this.optionalMessage);
        dataOutputStream.flush();

        return byteArrayOutputStream.toByteArray();
    }


    private static ConsultPacket deserializePrivate(byte[] dataConsult) throws IOException{

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(dataConsult);
        DataInputStream dataInputStream = new DataInputStream(byteArrayInputStream);

        byte[] data = new byte[dataInputStream.readInt()];
        Reader.read(dataInputStream,data,data.length);
        String optionalMessage = dataInputStream.readUTF();

        return new ConsultPacket(Consult.deserialize(data),optionalMessage);
    }


    public ConsultPacket deserialize(byte[] data) throws IOException{
        return ConsultPacket.deserializePrivate(data);
    }
}