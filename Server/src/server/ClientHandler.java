package server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class ClientHandler implements Runnable {
    private Server server;
    private PrintWriter printWriterToClient;
    private Scanner scannerFromClient;

    ClientHandler(Socket socket, Server server) {
        try {
            this.server = server;
            this.printWriterToClient = new PrintWriter(socket.getOutputStream());
            this.scannerFromClient = new Scanner(socket.getInputStream());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            server.sendMessageToAllClients("New member!");

            while(true) {
                if (scannerFromClient.hasNext()) {
                    String clientMessage = scannerFromClient.nextLine();

                    System.out.println(clientMessage);
                    server.sendMessageToAllClients(clientMessage);
                }
            }
        } finally {
            this.exit();
        }
    }
    public void sendMessage(String msg) {
        try {
            printWriterToClient.println(msg);
            printWriterToClient.flush();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    public void exit() {
        server.removeClient(this);
    }
}