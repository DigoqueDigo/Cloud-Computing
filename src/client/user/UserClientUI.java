package client.user;
import java.io.BufferedReader;
import java.io.EOFException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import job.Job;
import packets.ConsultPacket;
import packets.HelloPacket;
import packets.JobPacket;
import packets.Packet;
import packets.UserPacket;
import packets.Packet.Protocol;


public class UserClientUI{

    private static final String RESET = "\033[0m";
    private static final String RED_BOLD = "\033[1;31m";
    private static final String YELLOW_BOLD = "\033[1;33m";
    private static final String WHITE_BOLD = "\033[1;37m";
    private static final String GREEN_BOLD = "\033[1;32m";
    private static final String PURPLE_BOLD = "\033[1;35m";

    private static UserClientUI singleton = null;
    private BufferedReader input;

    
    private UserClientUI(){
        this.input = new BufferedReader(new InputStreamReader(System.in));
    }


    public static UserClientUI getInstance(){
        if (UserClientUI.singleton == null) UserClientUI.singleton = new UserClientUI();
        return UserClientUI.singleton;
    }
    
    
    public void showInvalidFolder(){
        System.out.println(RED_BOLD + "Invalid folder" + RESET);
    }


    public void showPacketMessage(Packet packet){        
        if (packet.getProtocol() != Protocol.ERROR)
            System.out.println(GREEN_BOLD + packet.getOptionalMessage() + RESET);
        else System.out.println(RED_BOLD + packet.getOptionalMessage() + RESET);
    }


    public void checkFolders(String inForlder, String outFolder) throws FileNotFoundException{
        File in = new File(inForlder), out = new File(outFolder);
        if (!(in.exists() && in.isDirectory() && out.exists() && out.isDirectory())){
            throw new FileNotFoundException();
        }
    }


    private int checkInteger(String number) throws Exception{
        int result = Integer.valueOf(number);
        if (result < 0) throw new Exception();
        return result;
    }


    public Packet getUserPacket() throws EOFException{

        User user = null;
        Packet resultPacket = null;
        String option, username, password;

        System.out.println(WHITE_BOLD + "1 -> Login" + RESET);
        System.out.println(WHITE_BOLD + "2 -> Creat account" + RESET);
        System.out.println(WHITE_BOLD + "3 -> Exit program" + RESET);

        while (resultPacket == null || user == null){

            System.out.print(YELLOW_BOLD  + ">>> " + RESET);
            
            try{
                
                option = this.input.readLine();

                if (Integer.valueOf(option) == 1 || Integer.valueOf(option) == 2){

                    System.out.print(YELLOW_BOLD + "Enter a username: " + RESET);
                    username = this.input.readLine();
                    
                    System.out.print(YELLOW_BOLD + "Enter a password: " + RESET);
                    password = this.input.readLine();

                    user = new User(username,password);

                    resultPacket = (Integer.valueOf(option) == 1) ?
                            new UserPacket(Protocol.LOGIN,user) :
                            new UserPacket(Protocol.CREATE_ACCOUNT,user);
                }

                else if (Integer.valueOf(option) == 3) throw new EOFException(); 

                else throw new Exception();
            }

            catch (EOFException e) {throw e;}

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

                System.out.print(YELLOW_BOLD + "Enter task tolerance: " + RESET);
                tolerance = checkInteger(this.input.readLine());

                System.out.print(YELLOW_BOLD + "Enter necessary memory: " + RESET);
                memory = checkInteger(this.input.readLine());

                System.out.print(YELLOW_BOLD + "Enter task file: " + RESET);
                filename = this.input.readLine();

                File file = new File(UserClient.INPUT_FOLDER + filename);

                if (!file.exists()) throw new FileNotFoundException();

                job_data = Files.readAllBytes(file.toPath());
                
                Job job = new Job(tolerance,memory,job_data);
                resutlPacket = new JobPacket(Protocol.JOB,"Job submitted: " + job.getIdentifier(),job);
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

 
    public Packet getPacket() throws EOFException{

        int option;
        Packet resultPacket = null;

        System.out.println(WHITE_BOLD + "1 -> Check server status" + RESET);
        System.out.println(WHITE_BOLD + "2 -> Send task to server" + RESET);
        System.out.println(WHITE_BOLD + "3 -> Collect packets" + RESET);
        System.out.println(WHITE_BOLD + "4 -> Exit program" + RESET);

        while (resultPacket == null){

            System.out.print(YELLOW_BOLD + ">>> " + RESET);

            try{

                option = Integer.valueOf(this.input.readLine());

                if (option == 1) resultPacket = new ConsultPacket(Protocol.CONSULT);

                else if (option == 2) resultPacket = getJobPacket();

                else if (option == 3) resultPacket = new HelloPacket(Protocol.ERROR);

                else if (option == 4) throw new EOFException();

                else throw new Exception();
            }

            catch (EOFException e) {throw e;}

            catch (Exception e){
                System.out.println(RED_BOLD + "Invalid option");
            }
        }

        return resultPacket;
    }


    public void showPacket(Packet packet){

        switch (packet.getProtocol()) {
            
            case ERROR:
                System.out.println(RED_BOLD + packet.getOptionalMessage() + RESET);
                break;
        
            case CONSULT:
                ConsultPacket consultPacket = (ConsultPacket) packet;
                System.out.println(PURPLE_BOLD + consultPacket.getConsult().toString() + RESET);
                break;

            case JOB:
                JobPacket jobPacket = (JobPacket) packet;
                System.out.println(PURPLE_BOLD + jobPacket.getOptionalMessage() + RESET);

            default:
                break;
        }
    }
}