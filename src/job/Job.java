package job;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;
import carrier.Reader;


public class Job{

    private int tolerance;
    private int memory;
    private byte[] data;
    private String result;


    public Job(int tolerance, int memory, byte[] data){
        this.tolerance = tolerance;
        this.memory = memory;
        this.data = Arrays.copyOf(data,data.length);
        this.result = "";
    }


    public Job(byte[] data, String result){
        this.tolerance = 0;
        this.memory = 0;
        this.data = Arrays.copyOf(data,data.length);
        this.result = result;
    }


    public Job(int tolerance, int memory, byte[] data, String result){
        this.tolerance = tolerance;
        this.memory = memory;
        this.data = Arrays.copyOf(data,data.length);
        this.result = result;
    }


    public String toString(){
        StringBuilder buffer = new StringBuilder();
        buffer.append("Tolerance: ").append(this.tolerance);
        buffer.append("\tMemory: ").append(this.memory);
        buffer.append("\tData size: ").append(this.data.length);
        buffer.append("\tResult: ").append(this.result);
        return buffer.toString();
    }
    
    
    public byte[] serialize() throws IOException{

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);

        dataOutputStream.writeInt(this.tolerance);
        dataOutputStream.writeInt(this.memory);
        dataOutputStream.writeInt(this.data.length);
        dataOutputStream.write(this.data);
        dataOutputStream.writeUTF(this.result);
        dataOutputStream.flush();

        return byteArrayOutputStream.toByteArray();
    }


    public static Job deserialize(byte[] dataJob) throws IOException{

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(dataJob);
        DataInputStream dataInputStream = new DataInputStream(byteArrayInputStream);

        int tolerance = dataInputStream.readInt();
        int memory = dataInputStream.readInt();
        byte[] data = new byte[dataInputStream.readInt()];
        Reader.read(dataInputStream,data,data.length);
        String result = dataInputStream.readUTF();

        return new Job(tolerance,memory,data,result);
    }
}