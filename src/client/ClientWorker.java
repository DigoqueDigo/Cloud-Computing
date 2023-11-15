package client;

public class ClientWorker{

    private Buffer inBuffer;
    private Buffer outBuffer;
    private ClientUI clientUI;


    public ClientWorker(Buffer inBuffer, Buffer outBuffer){
        this.inBuffer = inBuffer;
        this.outBuffer = outBuffer;
    }   
}