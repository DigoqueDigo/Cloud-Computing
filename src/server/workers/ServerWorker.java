package server.workers;
import java.net.Socket;
import server.containers.ServerContainer;


public class ServerWorker implements Runnable{

    private Socket socket;
    private ServerContainer serverContainer;
    
    public ServerWorker(Socket socket, ServerContainer serverContainer){
        this.socket = socket;
        this.serverContainer = serverContainer;
    };


    public void run(){}
}
