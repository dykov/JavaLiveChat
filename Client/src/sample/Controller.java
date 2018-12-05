package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.awt.event.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Controller {

    @FXML
    TextArea textArea ;

    @FXML
    TextField textFieldLogin , textFieldMessage ;

    @FXML
    Button button ;

    private String Login = "";
    final String host = "localhost";
    final int port = 1111;
    Socket clientSocket;
    Scanner scannerFromServer;
    PrintWriter printWriterToServer;

    @FXML
    void initialize() {

        try {
            clientSocket = new Socket(host, port);
            if (clientSocket.isConnected()){
                System.out.println("connected");
            } else {
                System.out.println("not connected!!!");
            }
            scannerFromServer = new Scanner(clientSocket.getInputStream());
            printWriterToServer = new PrintWriter(clientSocket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        ClientAction() ;
    }

    public void ClientAction() {

        button.setOnAction(event -> {
            if( !textFieldLogin.getText().isEmpty() && !textFieldMessage.getText().isEmpty() ){
                Login = textFieldLogin.getText() ;
                sendMsg();
            }
        });


        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (true) {
                        if (scannerFromServer.hasNext()) {
                            String inMes = scannerFromServer.nextLine();
                            String lastMessages = textArea.getText() ;
                            textArea.clear();
                            textArea.setText( lastMessages + inMes + "\n" );
                        }
                    }
                } catch (Exception e) {
                }
            }
        }).start();
    }

    public void sendMsg() {
        String message = textFieldLogin.getText() + ": " + textFieldMessage.getText();
        printWriterToServer.println(message);
        printWriterToServer.flush();
        textFieldMessage.setText("");
    }

}
