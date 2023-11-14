package server.workers;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import carrier.Carrier;
import packets.Packet;
import server.containers.ServerContainer;
import server.workers.client.ServerClientWorker;


public class ServerHandler implements Runnable{

    private Socket socket;
    private ServerContainer serverContainer;
    private DataInputStream inputstream;
    private DataOutputStream outputStream;
    
    public ServerHandler(Socket socket, ServerContainer serverContainer) throws IOException{
        this.socket = socket;
        this.serverContainer = serverContainer;
        this.inputstream = new DataInputStream(socket.getInputStream());
        this.outputStream = new DataOutputStream(socket.getOutputStream());
    };


    public void run(){

        try{

            Carrier carrier = Carrier.getInstance();
            Packet packet = carrier.receivePacket(this.inputstream);

            switch (packet.getProtocol()){

                case USER:
                    ServerClientWorker serverClientWorker = new ServerClientWorker(serverContainer,inputstream,outputStream);
                    serverClientWorker.executeLogin();
                    serverClientWorker.execute();
                    break;

                case MACHINE:

                    break;

                default:
                    break;
            }

            socket.close();
        }

        catch (Exception e){

            try {socket.close();}
            catch (Exception f) {}

            System.out.println(e.getMessage());
        }
    }
}