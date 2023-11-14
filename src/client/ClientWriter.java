package client;
import java.io.DataOutputStream;


public class ClientWriter implements Runnable{

    private Buffer outBuffer;
    private DataOutputStream outputStream;

    public ClientWriter(Buffer outBuffer, DataOutputStream outputStream){
        this.outBuffer = outBuffer;
        this.outputStream = outputStream;
    }
    
    public void run(){};
}
