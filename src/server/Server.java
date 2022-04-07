package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import javax.net.ServerSocketFactory;

public class Server {
    // TODO: read this from user input when starting
    private static int PORT = 3005;

    private static int counter = 0;

    public static void main(String[] args) {
        ServerSocketFactory factory = ServerSocketFactory.getDefault();
        try (ServerSocket server = factory.createServerSocket(PORT)) {
            System.out.println("Waiting for client connection.");
            while (true) {
                Socket client = server.accept();
                counter++;
                System.out.println("Client " + counter + " connecting!");
                Thread t = new Thread(() -> serverClient(client));
                t.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // TODO: read some default words from file
    }

    private static void serverClient(Socket client) {
        try (Socket clientSocket = client) {
            DataInputStream input = new DataInputStream(clientSocket.getInputStream());
            DataOutputStream output = new DataOutputStream(clientSocket.getOutputStream());
            System.out.println("Client: " + input.readUTF());
            output.writeUTF("Server: hi client " + counter);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
