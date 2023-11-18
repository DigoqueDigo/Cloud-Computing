package server;
import java.net.ServerSocket;
import java.net.Socket;
import server.containers.MachineContainer;
import server.containers.ServerContainer;
import server.schedule.Schedule;
import server.schedule.ScheduleWorker;


public class Server{

    public static final int ServerDefaultPort = 12345;
    public static final String ServerAddrees = "localhost";

    public static void main(String[] args){

        try{
            
            Socket socket;
            ServerSocket serverSocket = new ServerSocket(ServerDefaultPort);

            Schedule schedule = new Schedule();
            MachineContainer machineContainer = new MachineContainer();
            ServerContainer serverContainer = new ServerContainer(schedule,machineContainer);
            ScheduleWorker scheduleWorker = new ScheduleWorker(schedule,machineContainer);
            
            new Thread(scheduleWorker).start();

            while ((socket = serverSocket.accept()) != null){
                new Thread(new ServerHandler(socket,serverContainer)).start();
            }

            serverSocket.close();
        }

        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}