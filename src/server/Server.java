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

    public static void main(String[] args) {
        ServerSocketFactory factory = ServerSocketFactory.getDefault();
        try (ServerSocket server = factory.createServerSocket(PORT)) {
            System.out.println("Waiting for client connection.");
            while (true) {
                Socket client = server.accept();
                Thread t = new Thread(() -> serveClient(client));
                t.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // TODO: read some default words from file
    }

    private static void serveClient(Socket client) {
        // TODO: remember to close sockets + kill thread when done
        try (Socket clientSocket = client) {
            DataInputStream input = new DataInputStream(clientSocket.getInputStream());
            DataOutputStream output = new DataOutputStream(clientSocket.getOutputStream());

            while (true) {
                if (input.available() > 0) {
                    String response = RequestHandler.handleRequest(input.readUTF());
                    output.writeUTF(response);
                }
            }
        } catch (IOException e) {
            // TODO
        }
    }
}
