package communication;

import com.google.protobuf.MessageLite;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class ProtobufHandler {
    public <T extends MessageLite> void sendProtobufMessage(DataOutputStream output, T message) throws IOException {
        byte[] data = message.toByteArray();
        byte[] lengthBytes = ByteBuffer.allocate(4).putInt(data.length).array();
        output.write(lengthBytes);
        output.write(data);
        output.flush();
    }

    public <T extends com.google.protobuf.MessageLite> T receiveProtobufMessage(DataInputStream input, Class<T> clazz) throws IOException {
        try {
            byte[] lengthBytes = new byte[4];
            int bytesRead = input.read(lengthBytes);
            if (bytesRead == -1) {
                return null;
            }
            if (bytesRead != 4) {
                throw new IOException("Could not read full message length.");
            }
            int length = ByteBuffer.wrap(lengthBytes).getInt();
            byte[] data = new byte[length];
            input.readFully(data);
            System.out.println(parseFrom(data, clazz));
            return parseFrom(data, clazz);
        } catch (EOFException e) {
            return null;
        }
    }

    private <T extends com.google.protobuf.MessageLite> T parseFrom(byte[] data, Class<T> clazz) throws IOException {
        try {
            java.lang.reflect.Method parseFromMethod = clazz.getMethod("parseFrom", byte[].class);
            return clazz.cast(parseFromMethod.invoke(null, (Object) data));
        } catch (Exception e) {
            throw new IOException("Error parsing protobuf message: " + e.getMessage(), e);
        }
    }
}
