package client;
import java.io.DataInputStream;


public class ClientReader implements Runnable{

    private Buffer inBuffer; 
    private DataInputStream inputStream;   

    public ClientReader(Buffer inBuffer, DataInputStream inputStream){
        this.inBuffer = inBuffer;
        this.inputStream = inputStream;
    }

    public void run(){}
}