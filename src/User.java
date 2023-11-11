package src;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class User {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 9090);

            Thread tcpThread = new Thread(new TCPThread(socket));
            tcpThread.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class TCPThread implements Runnable {
        private Socket socket;
        private DataInputStream dataIn;
        private DataOutputStream dataOut;
        private Scanner scanner;
        private Lock lock = new ReentrantLock();

        public TCPThread(Socket socket) {
            this.socket = socket;
            this.scanner = new Scanner(System.in);

            try {
                dataIn = new DataInputStream(socket.getInputStream());
                dataOut = new DataOutputStream(socket.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            int login = 0;
            try {
                while (true) {
                    if (login == 0) {
                        showLoginMenu();
                        System.out.println("Opção: ");
                        String choice = scanner.next();
                        switch (choice) {
                            case "1":
                                registerUser();
                                break;
                            case "2":
                                if (loginUser()) {
                                    login = 1;
                                }
                                break;
                            case "3":
                                System.out.println("Adeus!");
                                System.exit(0);
                            default:
                                System.out.println("Opção inválida. Tente novamente.");
                        }
                    } else {
                        showMenu();
                        System.out.println("Opção: ");
                        String choice = scanner.next();
                        switch (choice) {
                            case "1":
                                executeTask();
                                break;
                            case "2":
                                checkServiceStatus();
                                break;
                            case "3":
                                loadTasksFromFile();
                                break;
                            case "4":
                                System.out.println("Adeus!");
                                System.exit(0);
                            default:
                                System.out.println("Opção inválida. Tente novamente.");
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private void showLoginMenu() {
            System.out.println("\nEscolha uma opção:");
            System.out.println("1. Registrar usuário");
            System.out.println("2. Fazer login");
            System.out.println("3. Sair");
        }

        private void showMenu() {
            System.out.println("\nBem-vindo!\nEscolha uma opção:");
            System.out.println("1. Executar tarefa");
            System.out.println("2. Consultar estado atual");
            System.out.println("3. Ler tarefas de um ficheiro");
            System.out.println("4. Sair");
        }

        private void registerUser() throws IOException {
            lock.lock();
            try {
                System.out.println("Insira o nome de usuário:");
                String username = scanner.next();
                System.out.println("Insira a senha:");
                String password = scanner.next();

                dataOut.writeUTF("REGISTER " + username + " " + password);
                dataOut.flush();

                printServerResponse();
            } finally {
                lock.unlock();
            }
        }

        private boolean loginUser() throws IOException {
            lock.lock();
            try {
                System.out.println("Insira o nome de usuário:");
                String username = scanner.next();
                System.out.println("Insira a senha:");
                String password = scanner.next();

                dataOut.writeUTF("LOGIN " + username + " " + password);
                dataOut.flush();

                return printServerResponse().equals("Login com sucesso!");
            } finally {
                lock.unlock();
            }
        }

        private void executeTask() throws IOException {
            lock.lock();
            try {
                System.out.println("Insira o código da tarefa:");
                String taskCode = scanner.next();

                dataOut.writeUTF("EXECUTE " + taskCode);
                dataOut.flush();

                printServerResponse();
                printServerResponse();
            } finally {
                lock.unlock();
            }
        }

        private void checkServiceStatus() throws IOException {
            lock.lock();
            try {
                dataOut.writeUTF("STATUS");
                dataOut.flush();

                printServerResponse();
            } finally {
                lock.unlock();
            }
        }

        private String printServerResponse() {
            try {
                String receivedMessage = dataIn.readUTF();
                System.out.println(receivedMessage);
                return receivedMessage;

            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        private void loadTasksFromFile() {
            lock.lock();
            try {
                System.out.println("Insira o nome do ficheiro a executar: ");
                scanner.nextLine();
                String filePath = "TaskLoader/" + scanner.nextLine();

                try (BufferedReader fileReader = new BufferedReader(new FileReader(new File(filePath)))) {
                    String task;
                    while ((task = fileReader.readLine()) != null) {
                        dataOut.writeUTF("EXECUTE " + task);
                        System.out.println("Tarefa enviada para execução: " + task);
                        printServerResponse();
                        printServerResponse();
                    }
                } catch (IOException e) {
                    System.out.println("Erro ao ler o arquivo de tarefas: " + e.getMessage());
                }
            } finally {
                lock.unlock();
            }
        }
    }
}
