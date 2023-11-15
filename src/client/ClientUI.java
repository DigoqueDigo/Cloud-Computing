package client;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import job.Job;
import packets.ConsultPacket;
import packets.JobPacket;
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
        Packet resultPacket = null;
        String option, username, password;

        System.out.println("1 -> Creat account");
        System.out.println("2 -> Login\n");

        while (resultPacket == null || user == null){

            System.out.println(YELLOW_BOLD  + ">>> " + RESET);
            
            try{
                
                option = this.input.readLine();

                if (Integer.valueOf(option) == 1 || Integer.valueOf(option) == 2){

                    System.out.println(YELLOW_BOLD + "Enter a username: " + RESET);
                    username = this.input.readLine();
                    
                    System.out.println(YELLOW_BOLD + "Enter a password: " + RESET);
                    password = this.input.readLine();

                    user = new User(username,password);

                    resultPacket = (Integer.valueOf(option) == 1) ?
                            new UserPacket(Protocol.CREATE_ACCOUNT,user) :
                            new UserPacket(Protocol.LOGIN,user);
                }

                else throw new Exception();
            }

            catch (Exception e){
                System.out.println(RED_BOLD + "Invalid option" + RESET);
            }
        }

        return resultPacket;
    }


    private Packet getJobPacket(){

        String filename;
        byte[] job_data;
        int tolerance, memory;
        Packet resutlPacket = null;

        while (resutlPacket == null){

            try{

                System.out.println("Enter task tolerance" + YELLOW_BOLD + " >>> " + RESET);
                tolerance = Integer.valueOf(this.input.readLine());

                System.out.println("Enter necessary memory" + YELLOW_BOLD + " >>> " + RESET);
                memory = Integer.valueOf(this.input.readLine());

                System.out.println("Enter task file" + YELLOW_BOLD + " >>> " + RESET);
                filename = this.input.readLine();

                File file = new File(Client.INPUT_FOLDER + filename);

                if (!file.exists()) throw new FileNotFoundException();

                job_data = Files.readAllBytes(file.toPath());
                
                Job job = new Job(tolerance,memory,job_data);
                resutlPacket = new JobPacket(Protocol.JOB,job);
            }

            catch (FileNotFoundException e){
                System.out.println(RED_BOLD + "Invalid file" + RESET);
            }

            catch (Exception e){
                System.out.println(RED_BOLD + "Invalid entry" + RESET);
            }
        }

        return resutlPacket;
    }

 
    public Packet getPacket(){

        String option;
        Packet resultPacket = null;

        System.out.println("1 -> Check server status");
        System.out.println("2 -> Send task to server");

        while (resultPacket == null){

            System.out.println(YELLOW_BOLD + ">>> " + RESET);

            try{

                option = this.input.readLine();

                if (Integer.valueOf(option) == 1) resultPacket = new ConsultPacket(Protocol.CONSULT);

                else if (Integer.valueOf(option) == 2) resultPacket = getJobPacket();

                else throw new Exception();
            }

            catch (Exception e){
                System.out.println(RED_BOLD + "Invalid option");
            }
        }

        return resultPacket;
    }
}