import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

// Server3 için Message sınıfı
class Server3Message {
    private String demand;
    private String response;

    public Server3Message(String demand, String response) {
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

public class Server3 {
    public static void main(String[] args) {
        int port = 5003; // Server3 için port numarası
        String host = "localhost";

        // Diğer sunucuların portları
        int server1Port = 5001;
        int server2Port = 5002;

        // Server (dinleyici) işlemi
        new Thread(() -> {
            try (ServerSocket serverSocket = new ServerSocket(port)) {
                System.out.println("Server3 dinlemeye başladı. Port: " + port);

                // Bağlantıları kabul et
                while (true) {
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("Bir bağlantı kuruldu: " + clientSocket.getRemoteSocketAddress());

                    // İstemciden gelen mesajı al
                    ObjectInputStream input = new ObjectInputStream(clientSocket.getInputStream());
                    Server3Message message = (Server3Message) input.readObject();

                    // Gelen mesajı işleyin
                    System.out.println("Gelen talep: " + message.getDemand());

                    // Mesajın yanıtını ayarlayın
                    message.setResponse("YEP");

                    // Yanıtı istemciye gönderin
                    ObjectOutputStream output = new ObjectOutputStream(clientSocket.getOutputStream());
                    output.writeObject(message);
                }
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Server3 hata oluştu: " + e.getMessage());
                e.printStackTrace();
            }
        }).start();

        // Client işlemi (Diğer sunuculara bağlanma)
        new Thread(() -> {
            try {
                // Server1'e bağlan
                Socket connection1 = new Socket(host, server1Port);
                System.out.println("Server3 -> Server1 bağlantısı kuruldu.");

                // Server2'ye bağlan
                Socket connection2 = new Socket(host, server2Port);
                System.out.println("Server3 -> Server2 bağlantısı kuruldu.");

            } catch (IOException e) {
                System.out.println("Bağlantı kurulurken hata oluştu: " + e.getMessage());
                e.printStackTrace();
            }
        }).start();
    }
}