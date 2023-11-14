package server.workers.machine;
import java.io.DataInputStream;
import java.io.DataOutputStream;

import server.containers.ServerContainer;


public class ServerMachineWorker{

    private DataInputStream inputStream;
    private DataOutputStream outputStream;
    private ServerContainer serverContainer;
    
    
    public ServerMachineWorker(ServerContainer serverContainer, DataInputStream inputStream, DataOutputStream outputStream){
        this.inputStream = inputStream;
        this.outputStream = outputStream;
        this.serverContainer = serverContainer;
    }
    
    
    public void execute(){
        System.out.println("ServerMachineWorker por implementar");
    } 
}