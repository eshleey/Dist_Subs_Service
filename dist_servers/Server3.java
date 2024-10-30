import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server3 {
    public static void main(String[] args) {
        int port = 5003; // Server3 için port numarası
        String host = "localhost";

        // Diğer sunucuların portları
        int server1Port = 5001;
        int server2Port = 5002;

        new Thread(() -> {
            try (ServerSocket serverSocket = new ServerSocket(port)) {
                System.out.println("Server3 dinlemeye başladı. Port: " + port);

                while (true) {
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("Bir bağlantı kuruldu: " + clientSocket.getRemoteSocketAddress());
                }
            } catch (IOException e) {
                System.out.println("Server3 hata oluştu: " + e.getMessage());
                e.printStackTrace();
            }
        }).start();

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
