package dist_servers;

import communication.*;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Queue;
import java.util.concurrent.*;

public class AdminHandler {
    private static final ConcurrentMap<Integer, SubscriberOuterClass.Subscriber> subscribers = new ConcurrentHashMap<>();
    private static final Queue<SubscriberOuterClass.Subscriber> queue = new ConcurrentLinkedQueue<>();
    private static boolean isRunning = false;
    private static final DistributedServerHandler DISTRIBUTED_SERVER_HANDLER = new DistributedServerHandler();
    private static final ProtobufHandler protobufHandler = new ProtobufHandler();

    public static void acceptAdminConnections(ServerSocket serverSocket, ExecutorService executorService, int[] ports, int clientPort, String host) {
        while (true) {
            try {
                if (serverSocket == null || serverSocket.isClosed()) {
                    System.err.println("Server socket is closed, stopping admin connection attempts.");
                    break;
                }
                Socket adminSocket = serverSocket.accept();
                System.out.println("Admin connected: " + adminSocket.getInetAddress());
                executorService.submit(() -> handleAdmin(adminSocket, executorService, ports, clientPort, host));
            } catch (SocketException e) {
                System.err.println("ServerSocket is closed, no longer accepting admins.");
                break;
            } catch (IOException e) {
                System.err.println("Error accepting admin connection: " + e.getMessage());
            }
        }
    }

    public static void handleAdmin(Socket adminSocket, ExecutorService executorService, int[] ports, int clientPort, String host) {
        try (DataInputStream input = new DataInputStream(adminSocket.getInputStream());
             DataOutputStream output = new DataOutputStream(adminSocket.getOutputStream())) {


            ConfigurationOuterClass.Configuration config = protobufHandler.receiveProtobufMessage(input, ConfigurationOuterClass.Configuration.class);
            if (config != null) {
                System.out.println("Configuration received: " + config);
                boolean start = config.getMethod() == ConfigurationOuterClass.MethodType.STRT;
                isRunning = start;

                MessageOuterClass.Message responseMessage = MessageOuterClass.Message.newBuilder()
                        .setDemand(MessageOuterClass.Demand.STRT)
                        .setResponse(start ? MessageOuterClass.Response.YEP : MessageOuterClass.Response.NOPE)
                        .build();

                protobufHandler.sendProtobufMessage(output, responseMessage);
                System.out.println("Response sent to admin: " + responseMessage.getResponse());

                if (start) {
                    for (int port : ports) {
                        if (port != clientPort) {
                            executorService.submit(() -> DISTRIBUTED_SERVER_HANDLER.connectServer(port, host));
                        }
                    }
                }
            }

            while (isRunning && !adminSocket.isClosed()) {
                try {
                    MessageOuterClass.Message request = protobufHandler.receiveProtobufMessage(input, MessageOuterClass.Message.class);
                    if (request != null && request.getDemand() == MessageOuterClass.Demand.CPCTY) {
                        CapacityOuterClass.Capacity capacityInfo = CapacityOuterClass.Capacity.newBuilder()
                                .setServer1Status(subscribers.size())
                                .setServer2Status(queue.size())
                                .setServer3Status(queue.size())
                                .setTimestamp(System.currentTimeMillis())
                                .build();
                        protobufHandler.sendProtobufMessage(output, capacityInfo);
                        System.out.println("Capacity sent to admin: " + capacityInfo);
                    }
                } catch (EOFException e) {
                    System.out.println("Admin disconnected.");
                    break;
                } catch (IOException e) {
                    System.err.println("Error handling admin request: " + e.getMessage());
                    break;
                }
            }

        } catch (IOException e) {
            System.err.println("Error handling admin connection: " + e.getMessage());
        } finally {
            DistributedServerHandler.closeSocket(adminSocket);
        }
    }
}
