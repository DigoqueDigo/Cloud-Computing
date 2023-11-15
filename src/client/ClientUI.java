package client;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import job.Job;
import packets.Packet;
import packets.UserPacket;
import packets.Packet.Protocol;
import user.User;


public class ClientUI{

    private static final String RESET = "\033[0m";
    private static final String RED_BOLD = "\033[1;31m";
    private static final String YELLOW_BOLD = "\033[1;33m";

    private static ClientUI singleton = null;
    private BufferedReader input;

    
    private ClientUI(){
        this.input = new BufferedReader(new InputStreamReader(System.in));
    }


    public static ClientUI getInstance(){
        if (ClientUI.singleton == null) ClientUI.singleton = new ClientUI();
        return ClientUI.singleton;
    }


    public Packet getUserPacket(){

        User user = null;
        Packet packet = null;
        String option, username, password;

        System.out.println("1 -> Creat account");
        System.out.println("2 -> Login\n");

        while (packet == null || user == null){

            System.out.println(YELLOW_BOLD  + ">>> " + RESET);
            
            try{
                
                option = this.input.readLine();

                if (Integer.valueOf(option) == 1 || Integer.valueOf(option) == 2){

                    System.out.println(YELLOW_BOLD + "Enter a username: " + RESET);
                    username = this.input.readLine();
                    
                    System.out.println(YELLOW_BOLD + "Enter a password: " + RESET);
                    password = this.input.readLine();

                    user = new User(username,password);

                    packet = (Integer.valueOf(option) == 1) ?
                            new UserPacket(Protocol.CREATE_ACCOUNT,user) :
                            new UserPacket(Protocol.LOGIN,user);
                }

                else throw new Exception();
            }

            catch (Exception e){
                System.out.println(RED_BOLD + "Invalid option" + RESET);
            }
        }

        return packet;
    }

 
    public Packet getJobPacket(){

        Job job = null;
        Packet packet = null;
        String option, file;
        int tolerance, memory;

        System.out.println("1 -> Check server status");
        System.out.println("2 -> Send task to server");

        while (packet == null || job == null){

            System.out.println(YELLOW_BOLD + ">>> " + RESET);

            try{

                option = this.input.readLine();

                if (Integer.valueOf(option) == 1){

                }

                else if (Integer.valueOf(option) == 1){

                }

                else throw new Exception();
            }

            catch (Exception e){
                System.out.println(RED_BOLD + "Invalid option");
            }
        }

        return packet;
    }
}