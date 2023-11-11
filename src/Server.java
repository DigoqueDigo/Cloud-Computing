package src;

import sd23.JobFunction;
import sd23.JobFunctionException;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.concurrent.LinkedBlockingQueue;

public class Server {

    private static final LinkedBlockingQueue<Task> taskQueue = new LinkedBlockingQueue<>();
    private static final String RESULT_FOLDER = "Resultados";

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(9090);
            System.out.println("Servidor ativo na porta " + 9090);

            Socket socket;
            Authentication contas = new Authentication();
            int Memory = Integer.parseInt(args[0]);

            while ((socket = serverSocket.accept()) != null) {
                Thread thread = new Thread(new TaskHandler(socket, contas, Memory));
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
                DataInputStream dataIn = new DataInputStream(socket.getInputStream());
                DataOutputStream dataOut = new DataOutputStream(socket.getOutputStream());

                while (true) {
                    String message = dataIn.readUTF();
                    String[] words = message.split(" ");

                    if (words[0].toUpperCase().equals("REGISTER")) {
                        if (contas.registerUser(words[1], words[2])) {
                            dataOut.writeUTF("Registado com sucesso!");
                        } else {
                            dataOut.writeUTF("Já existe um usuário com esse nome! Tente novamente.");
                        }
                        dataOut.flush();
                    }

                    else if (words[0].toUpperCase().equals("LOGIN")) {
                        if (contas.authenticateUser(words[1], words[2])) {
                            dataOut.writeUTF("Login com sucesso!");
                        } else {
                            dataOut.writeUTF("Credenciais erradas! Tente novamente.");
                        }
                        dataOut.flush();
                    }

                    else if (words[0].toUpperCase().equals("EXECUTE")) {
                        String taskCode = String.join(" ", Arrays.copyOfRange(words, 1, words.length));
                        int requiredMemory = taskCode.length();

                        int memoryUsed = 0;
                        for (Task task : taskQueue) {
                            memoryUsed = task.getRequiredMemory();
                        }

                        if (requiredMemory + memoryUsed <= memory) {
                            Task task = new Task(taskCode, requiredMemory);
                            taskQueue.offer(task);

                            dataOut.writeUTF("Tarefa em espera para execução.");
                        } else {
                            dataOut.writeUTF("Erro: Memória insuficiente para executar a tarefa.");
                            dataOut.writeUTF("");
                        }
                        dataOut.flush();
                    }

                    Task task = taskQueue.poll();
                    if (task != null) {
                        byte[] job = task.getTaskCode().getBytes();
                        try {
                            byte[] result = JobFunction.execute(job);

                            String resultFileName = RESULT_FOLDER + File.separator + "result_" + System.currentTimeMillis() + ".txt";

                            try (FileOutputStream fileOutputStream = new FileOutputStream(resultFileName)) {
                                fileOutputStream.write(result);
                            }

                            dataOut.writeUTF("Sucesso! Retornado " + result.length + " bytes");
                        } catch (JobFunctionException e) {
                            dataOut.writeUTF("Job failed: code=" + e.getCode() + " message=" + e.getMessage());
                        }
                        dataOut.flush();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
