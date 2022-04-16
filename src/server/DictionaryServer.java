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
            System.err.println("ServerError: dictionary file cannot be found.");
            System.exit(1);
        } catch (IOException | ParseException e) {
            System.err.println("ServerError: something wrong when trying to read dictionary file.");
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
            System.err.println("ServerError: cannot listen for connections on port " + portNumber + ".");
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
                    if (inputString.equals(RequestHandler.REQUEST_EXIT)) {
                        break;
                    }

                    String response = RequestHandler.handleRequest(inputString);
                    output.writeUTF(response);
                }
            }
            clientSocket.close();
        } catch (IOException e) {
            System.err.println("ServerError: something wrong when serving client request.");
        }

        // System.out.println("Bye client!");
    }
}
