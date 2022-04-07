package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Client {
    // TODO
    private static String url = "localhost";
    private static int port = 3005;

    public static void main(String[] args) {
        try (Socket socket = new Socket(url, port)) {
            DataInputStream input = new DataInputStream(socket.getInputStream());
            DataOutputStream output = new DataOutputStream(socket.getOutputStream());

            String payload = "Client wanting to connect.";
            output.writeUTF(payload);
            output.flush();

            boolean flag = true;
            while (flag) {
                if (input.available() > 0) {
                    String msg = input.readUTF();
                    System.out.println("From server: " + msg);
                    flag = false;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
