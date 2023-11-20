package job;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.UUID;
import carrier.Reader;


public class Job{

    private int tolerance;
    private int memory;
    private byte[] data;
    private String identifier;


    public Job(int tolerance, int memory, byte[] data){
        this.memory = memory;
        this.tolerance = tolerance;
        this.data = Arrays.copyOf(data,data.length);
        this.identifier = UUID.randomUUID().toString();
    }


    public Job(byte[] data, String identifier){
        this.memory = 0;
        this.tolerance = 0;
        this.data = Arrays.copyOf(data,data.length);
        this.identifier = identifier;
    }


    public Job(int tolerance, int memory, byte[] data, String identifier){
        this.memory = memory;
        this.tolerance = tolerance;
        this.data = Arrays.copyOf(data,data.length);
        this.identifier = identifier;
    }


    public int getTolerance(){
        return this.tolerance;
    }


    public int getMemory(){
        return this.memory;
    }


    public byte[] getData(){
        return Arrays.copyOf(this.data,this.data.length);
    }


    public String getIdentifier(){
        return this.identifier;
    }


    public void setIdentifier(String identifier){
        this.identifier = identifier;
    }


    public String toString(){
        StringBuilder buffer = new StringBuilder();
        buffer.append("Tolerance: ").append(this.tolerance);
        buffer.append("\tMemory: ").append(this.memory);
        buffer.append("\tData size: ").append(this.data.length);
        buffer.append("\tID: ").append(this.identifier);
        return buffer.toString();
    }
    
    
    public byte[] serialize() throws IOException{

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);

        dataOutputStream.writeInt(this.tolerance);
        dataOutputStream.writeInt(this.memory);
        dataOutputStream.writeUTF(this.identifier);
        dataOutputStream.writeInt(this.data.length);
        dataOutputStream.write(this.data);
        dataOutputStream.flush();

        return byteArrayOutputStream.toByteArray();
    }


    public static Job deserialize(byte[] dataJob) throws IOException{

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(dataJob);
        DataInputStream dataInputStream = new DataInputStream(byteArrayInputStream);

        int tolerance = dataInputStream.readInt();
        int memory = dataInputStream.readInt();
        String identifier = dataInputStream.readUTF();
        byte[] data = new byte[dataInputStream.readInt()];
        Reader.read(dataInputStream,data,data.length);

        return new Job(tolerance,memory,data,identifier);
    }
}