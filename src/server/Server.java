package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import javax.net.ServerSocketFactory;

public class Server {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Usage: java -jar Server.jar <port_number>");
            System.exit(1);
        }

        final int portNumber = Integer.parseInt(args[0]);

        ServerSocketFactory factory = ServerSocketFactory.getDefault();
        try (ServerSocket server = factory.createServerSocket(portNumber)) {
            System.out.println("Waiting for client connection.");
            while (true) {
                Socket client = server.accept();
                Thread t = new Thread(() -> serveClient(client));
                t.start();
            }
        } catch (IOException e) {
            // TODO: maybe log this into a file instead
            System.out.println("IOException: cannot listen for connections on port " + portNumber);
            System.out.println(e.getMessage());
        }
    }

    private static void serveClient(Socket clientSocket) {
        try (DataInputStream input = new DataInputStream(clientSocket.getInputStream());
                DataOutputStream output = new DataOutputStream(clientSocket.getOutputStream())) {
            while (true) {
                if (input.available() > 0) {
                    // TODO: commit changes to in-memory dictionary to disk at some point?
                    String response = RequestHandler.handleRequest(input.readUTF());
                    output.writeUTF(response);
                    output.flush();
                }
            }

            // TODO: close the socket?
        } catch (IOException e) {
            // TODO: maybe log this into a file instead
            System.out.println("IOException");
            e.printStackTrace();
        }
    }
}
