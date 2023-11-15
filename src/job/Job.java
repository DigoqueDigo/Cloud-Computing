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
    private byte[] data;
    private String result;


    public Job(int tolerance, byte[] data){
        this.tolerance = tolerance;
        this.data = Arrays.copyOf(data,data.length);
        this.result = "";
    }


    public Job(int tolerance, byte[] data, String result){
        this.tolerance = tolerance;
        this.data = Arrays.copyOf(data,data.length);
        this.result = result;
    }
    
    
    public byte[] serialize() throws IOException{

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);

        dataOutputStream.writeInt(this.tolerance);
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
        byte[] data = new byte[dataInputStream.readInt()];
        Reader.read(dataInputStream,data,data.length);
        String result = dataInputStream.readUTF();

        return new Job(tolerance,data,result);
    }
}