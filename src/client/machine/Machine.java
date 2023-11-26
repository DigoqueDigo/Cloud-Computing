package client.machine;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;


public class Machine{

    private int memory;
    private int availableMemory;
    private String identifier;


    public Machine(int memory) throws Exception{
        if (memory < 0) throw new Exception();
        this.identifier = "";
        this.memory = memory;
        this.availableMemory = memory;
    }


    public Machine(String identifer, int memory){
        this.identifier = identifer;
        this.memory = memory;
        this.availableMemory = memory;
    }


    public String getIdentifier(){
        return this.identifier;
    }


    public int getMemory(){
        return this.memory;
    }


    public int getAvaiableMemory(){
        return this.availableMemory;
    }


    public void setIdentifier(String identifier){
        this.identifier = identifier;
    }

    
    public void decreaseMemory(int memory) throws IllegalArgumentException{
        if (this.availableMemory < memory) throw new IllegalArgumentException();
        this.availableMemory -= memory;
    }
    

    public void increaseMemory(int memory){
        this.availableMemory += memory;
    }
    

    public int hashCode(){
        return this.identifier.hashCode();
    }


    public boolean equals(Object obj){
        if (obj == null || this.getClass() != obj .getClass()) return false;
        Machine that = (Machine) obj;
        return this.identifier.equals(that.getIdentifier());
    }

    public String toString(){
        StringBuilder buffer = new StringBuilder();
        buffer.append("Nonce: ").append(this.identifier);
        buffer.append("\tMemory: ").append(this.memory);
        buffer.append("\tAvaiable memory: ").append(this.availableMemory);
        return buffer.toString();
    }


    public byte[] serialize() throws IOException{

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);

        dataOutputStream.writeUTF(this.identifier);
        dataOutputStream.writeInt(this.memory);
        dataOutputStream.flush();

        return byteArrayOutputStream.toByteArray();
    }


    public static Machine deserialize(byte[] data) throws IOException{

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
        DataInputStream dataInputStream = new DataInputStream(byteArrayInputStream);

        String identifier = dataInputStream.readUTF(); 
        int memory = dataInputStream.readInt();

        return new Machine(identifier,memory);
    }
}
