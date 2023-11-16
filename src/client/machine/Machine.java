package client.machine;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;


public class Machine{

    private String name;
    private int memory;


    public Machine(String name, int memory){
        this.name = name;
        this.memory = memory;
    }


    public byte[] serialize() throws IOException{

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);

        dataOutputStream.writeUTF(this.name);
        dataOutputStream.writeInt(this.memory);
        dataOutputStream.flush();

        return byteArrayOutputStream.toByteArray();
    }


    public static Machine deserialize(byte[] data) throws IOException{

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
        DataInputStream dataInputStream = new DataInputStream(byteArrayInputStream);

        String name = dataInputStream.readUTF();
        int memory = dataInputStream.readInt();

        return new Machine(name,memory);
    }
}
