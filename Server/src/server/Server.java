package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
    int port = 1111;
    private ArrayList<ClientHandler> clients = new ArrayList<>();

    public Server() {

        Socket clientSocket = null;
        ServerSocket serverSocket = null;

        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Waiting for a client...");
            while (true) {

                // на этом моменте цикл замирает, пока не подключится новый клиент
                clientSocket = serverSocket.accept();
                System.out.println("New client)))");

                ClientHandler client = new ClientHandler(clientSocket, this);

                clients.add(client);
                new Thread(client).start();
            }
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
        finally {
            try {
                clientSocket.close();
                System.out.println("Server is stopped :-(");
                serverSocket.close();
            }
            catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void sendMessageToAllClients(String msg) {
        for (ClientHandler ch : clients) {
            ch.sendMessage(msg);
        }

    }

    public void removeClient(ClientHandler client) {
        clients.remove(client);
    }

}