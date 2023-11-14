package user;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;


public class User{

    private String username;
    private String password;


    public User(String username, String password){
        this.username = username;
        this.password = password;
    }

    
    public String getUsername(){
        return this.username;
    }

    
    public String getPassowrd(){
        return this.password;
    }

    
    public String toString(){

        StringBuilder buffer = new StringBuilder();

        buffer.append("Username: ").append(this.username);
        buffer.append("\tPassowrd: ").append(this.password);

        return buffer.toString();
    }

    
    public boolean equals(Object obj){

        if (obj == null || this.getClass() != obj.getClass()) return false;

        User that = (User) obj;
        return this.username.equals(that.getUsername()) &&
                this.password.equals(that.getPassowrd());
    }

    
    public byte[] serialize() throws IOException{

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
        
        dataOutputStream.writeUTF(this.username);
        dataOutputStream.writeUTF(this.password);
        dataOutputStream.flush();

        return byteArrayOutputStream.toByteArray();
    }

    
    public static User deserialize(byte[] data) throws IOException{

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
        DataInputStream dataInputStream = new DataInputStream(byteArrayInputStream);
        
        String username = dataInputStream.readUTF();
        String password = dataInputStream.readUTF();

        return new User(username,password);
    }
}