package job;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;


public class Consult{

    public int pendingTasks;
    public Map<String,Integer> systemState;


    public Consult(){
        this.pendingTasks = 0;
        this.systemState = new HashMap<String,Integer>();
    }


    public Consult(int pendingTasks){
        this.pendingTasks = pendingTasks;
        this.systemState = new HashMap<String,Integer>();
    }


    public Consult(int pendingTasks, Map<String,Integer> systemState){
        this.pendingTasks = pendingTasks;
        this.systemState = systemState;
    }


    public String toString(){

        StringBuilder buffer = new StringBuilder();

        buffer.append("Pending Tasks: ").append(this.pendingTasks);

        if (this.systemState.size() > 0){
            buffer.append(this.systemState
                .entrySet()
                .stream()
                .map(x -> "Machine: " + x.getKey().toString() + "\tAvailable memory: " + x.getValue().toString())
                .collect(Collectors.joining("\n","\n","")));
        }

        else buffer.append("\nNo machines in the system");
        return buffer.toString();
    }


    public byte[] serialize() throws IOException{

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);

        dataOutputStream.writeInt(this.pendingTasks);
        dataOutputStream.writeInt(this.systemState.size());

        for (Map.Entry<String,Integer> entry : this.systemState.entrySet()){
            dataOutputStream.writeUTF(entry.getKey());
            dataOutputStream.writeInt(entry.getValue());
        }

        dataOutputStream.flush();
        return byteArrayOutputStream.toByteArray();
    }


    public static Consult deserialize(byte[] dataConsult) throws IOException{

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(dataConsult);
        DataInputStream dataInputStream = new DataInputStream(byteArrayInputStream);

        int pendingTasks = dataInputStream.readInt();
        int size = dataInputStream.readInt();
        Map<String,Integer> systemState = new HashMap<String,Integer>();

        for (int p = 0; p < size; p++){
            String key = dataInputStream.readUTF();
            int value = dataInputStream.readInt();
            systemState.put(key,value);
        }

        return new Consult(pendingTasks,systemState);
    }
}