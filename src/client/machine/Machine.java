package client.machine;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;


public class Machine{

    private String name;
    private int memory;
    private int availableMemory;


    public Machine(String name, int memory){
        this.name = name;
        this.memory = memory;
        this.availableMemory = memory;
    }


    public String getName(){
        return this.name;
    }


    public int getAvaiableMemory(){
        return this.availableMemory;
    }


    public void decreaseMemory(int memoryRequired) throws IllegalArgumentException{
        if (this.availableMemory < memoryRequired) throw new IllegalArgumentException();
        this.availableMemory -= memoryRequired;
    }

    
    public int hashCode(){
        return this.name.hashCode();
    }


    public boolean equals(Object obj){

        if (obj == null || this.getClass() != obj.getClass()) return false;
        
        Machine that = (Machine) obj;
        return this.name.equals(that.getName());
    }


    public String toString(){
        StringBuilder buffer = new StringBuilder();
        buffer.append("Name: ").append(this.name);
        buffer.append("\tMemory: ").append(this.memory);
        buffer.append("\tAvaiable memory: ").append(this.availableMemory);
        return buffer.toString();
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
