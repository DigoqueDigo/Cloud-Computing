package src;

import sd23.JobFunction;
import sd23.JobFunctionException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;

public class Server {

    private static final LinkedBlockingQueue<Task> taskQueue = new LinkedBlockingQueue<>();

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(9090);
            System.out.println("Servidor ativo na porta " + 9090);

            Socket socket;
            Authentication contas = new Authentication();
            int Memory = Integer.parseInt(args[0]);

            while ((socket = serverSocket.accept()) != null) {
                Thread thread = new Thread(new TaskHandler(socket,contas,Memory));
                thread.start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class TaskHandler implements Runnable {
        private Socket socket;
        private Authentication contas;
        private final int memory;

        public TaskHandler(Socket socket, Authentication contas, int maxMemory) {
            this.socket = socket;
            this.contas = contas;
            this.memory = maxMemory;

        }

        @Override
        public void run() {
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

                while (true) {
                    String message = in.readLine();
                    String[] words = message.split(" ");

                    if (words[0].toUpperCase().equals("REGISTER")) {
                        if(contas.registerUser(words[1], words[2])){
                            out.println("Registado com sucesso!");
                        }
                        else{
                            out.println("Já existe um usuário com esse nome! Tente novamente.");
                        }
                        out.flush();
                    }

                    else if (words[0].toUpperCase().equals("LOGIN")) {
                        if (contas.authenticateUser(words[1], words[2])) {
                            out.println("Login com sucesso!");
                            out.flush();
                        }
                        else{
                            out.println("Credenciais erradas! Tente novamente.");
                        }
                    }

                    else if (words[0].toUpperCase().equals("EXECUTE")) {
                        String taskCode = words[1];
                        int requiredMemory = taskCode.length();

                        if (requiredMemory <= memory) {
                            Task task = new Task(taskCode, requiredMemory);
                            taskQueue.offer(task);

                            out.println("Tarefa em espera para execução.");
                            out.flush();
                        } else {
                            out.println("Erro: Memória insuficiente para executar a tarefa.");
                            out.flush();
                          }

                    }

                    Task task = taskQueue.poll();
                    if (task != null) {
                        byte[] job = task.getTaskCode().getBytes();
                        try {
                            byte[] result = JobFunction.execute(job);
                            out.println("Sucesso! Retornado "+result.length+" bytes");
                            out.flush();
                        } catch (JobFunctionException e) {
                            out.println("Job failed: code="+e.getCode()+" message="+e.getMessage());
                            out.flush();
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
