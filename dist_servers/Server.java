import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;


class Message {
    private String demand;
    private String response;

    public Message(String demand, String response) {
        this.demand = demand;
        this.response = response;
    }

    public String getDemand() {
        return demand;
    }

    public String getResponse() {
        return response;
    }

    // Setters ekleyin
    public void setDemand(String demand) {
        this.demand = demand;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}

public class Server {
    public static void main(String[] args) {
        int port = 5001; 
        String host = "localhost";

        
        int server2Port = 5002;
        int server3Port = 5003;

        
        new Thread(() -> {
            try (ServerSocket serverSocket = new ServerSocket(port)) {
                System.out.println("Server dinlemeye başladı. Port: " + port);

                // Bağlantıları kabul et
                while (true) {
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("Bir bağlantı kuruldu: " + clientSocket.getRemoteSocketAddress());

                    // İstemciden gelen mesajı al
                    ObjectInputStream input = new ObjectInputStream(clientSocket.getInputStream());
                    Message message = (Message) input.readObject();

                    
                    System.out.println("Gelen talep: " + message.getDemand());

                    
                    message.setResponse("YEP");

                    
                    ObjectOutputStream output = new ObjectOutputStream(clientSocket.getOutputStream());
                    output.writeObject(message);
                }
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Server hata oluştu: " + e.getMessage());
                e.printStackTrace();
            }
        }).start();

        // Client işlemi 
        new Thread(() -> {
            try {
                
                Socket connection1 = new Socket(host, server2Port);
                System.out.println("Server -> Server2 bağlantısı kuruldu.");

                
                Socket connection2 = new Socket(host, server3Port);
                System.out.println("Server -> Server3 bağlantısı kuruldu.");

            } catch (IOException e) {
                System.out.println("Bağlantı kurulurken hata oluştu: " + e.getMessage());
                e.printStackTrace();
            }
        }).start();
    }
}