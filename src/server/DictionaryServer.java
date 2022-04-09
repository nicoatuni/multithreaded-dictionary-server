package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import javax.net.ServerSocketFactory;

import org.json.simple.parser.ParseException;

public class DictionaryServer {
    public static void main(String[] args) {
        if (args.length < 2) {
            System.err.println("Usage: java -jar Server.jar <port_number> <dictionary_file>");
            System.exit(1);
        }

        final int portNumber = Integer.parseInt(args[0]);
        final String dictionaryFilePath = args[1];

        try {
            DictionaryHandler.initDictionaryFile(dictionaryFilePath);
        } catch (FileNotFoundException e) {
            // TODO: improve these exception messages
            System.err.println("Error: dictionary file cannot be found.");
            System.exit(1);
        } catch (IOException | ParseException e) {
            System.err.println("Error: something wrong when trying to read dictionary file.");
            System.exit(1);
        }

        ServerSocketFactory factory = ServerSocketFactory.getDefault();
        try (ServerSocket server = factory.createServerSocket(portNumber)) {
            // System.out.println("Waiting for client connection.");

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

        // System.out.println("Exiting main, bye!");
    }

    private static void serveClient(Socket clientSocket) {
        // System.out.println("Hi client, let me help you.");
        try (DataInputStream input = new DataInputStream(clientSocket.getInputStream());
                DataOutputStream output = new DataOutputStream(clientSocket.getOutputStream())) {
            while (true) {
                if (input.available() > 0) {
                    String inputString = input.readUTF();
                    // System.out.println("Client request received: " + inputString);
                    // TODO: improve handling this special exit request
                    if (inputString.equals("exit")) {
                        break;
                    }

                    String response = RequestHandler.handleRequest(inputString);
                    output.writeUTF(response);
                }
            }
            clientSocket.close();
        } catch (IOException e) {
            // TODO: maybe log this into a file instead
            System.out.println("IOException");
            e.printStackTrace();
        }

        // System.out.println("Bye client!");
    }
}
