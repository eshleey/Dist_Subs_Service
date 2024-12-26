package Clients;

import java.io.IOException;

public class Client2 {
    private static final int PORT = 7002;
    private static final int ID = 15;

    public static void main(String[] args) {
        try {
            ClientHandler.connectServer(PORT);
            Thread.sleep(100);
            ClientHandler.sendRequest("SUBS", ID, ClientHandler.getOutput());
            Thread.sleep(100);
            ClientHandler.sendRequest("DEL", ID, ClientHandler.getOutput());
            ClientHandler.receiveResponse(ClientHandler.getInput());
        } catch (IOException | InterruptedException e) {
            System.err.println("Error in client: " + e.getMessage());
        } finally {
            ClientHandler.disconnectServer();
        }
    }
}