package client;

import java.net.Socket;

import server.Server;

public class Main{

    public static void main(String[] args){

        try{

            Socket socket = new Socket(Server.ServerAddrees,Server.ServerDefaultPort);
            Client client = new Client(socket);

            client.execute();
        }

        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
    
}
