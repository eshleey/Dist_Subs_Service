import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) {
        int port = 5001; // Server1 için port numarası
        String host = "localhost";

        // Diğer sunucuların portları
        int server2Port = 5002;
        int server3Port = 5003;

        // Server (dinleyici) işlemi
        new Thread(() -> {
            try (ServerSocket serverSocket = new ServerSocket(port)) {
                System.out.println("Server1 dinlemeye başladı. Port: " + port);
                
                // Bağlantıları kabul et
                while (true) {
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("Bir bağlantı kuruldu: " + clientSocket.getRemoteSocketAddress());
                }
            } catch (IOException e) {
                System.out.println("Server1 hata oluştu: " + e.getMessage());
                e.printStackTrace();
            }
        }).start();

        // Client işlemi (Diğer sunuculara bağlanma)
        new Thread(() -> {
            try {
                // Server2'ye bağlan
                Socket connection1 = new Socket(host, server2Port);
                System.out.println("Server1 -> Server2 bağlantısı kuruldu.");

                // Server3'e bağlan
                Socket connection2 = new Socket(host, server3Port);
                System.out.println("Server1 -> Server3 bağlantısı kuruldu.");

            } catch (IOException e) {
                System.out.println("Bağlantı kurulurken hata oluştu: " + e.getMessage());
                e.printStackTrace();
            }
        }).start();
    }
}
