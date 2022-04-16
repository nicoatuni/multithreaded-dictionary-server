package server;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.net.ServerSocketFactory;

/**
 * Main server class handling incoming client connections and passing them over
 * to a thread pool to handle.
 * 
 * @author Nico Eka Dinata (770318)
 * 
 */
public class DictionaryServer {
    private static final int MAX_THREAD_POOL_SIZE = 16;

    public static void main(String[] args) {

        if (args.length < 2) {
            System.err.println("Usage: java -jar DictionaryServer.jar <port_number> <dictionary_file_path>");
            System.exit(1);
        }

        final int portNumber = Integer.parseInt(args[0]);
        final String dictionaryFilePath = args[1];

        // try to initialise the dictionary file into memory
        try {
            DictionaryHandler.initDictionaryFile(dictionaryFilePath);
        } catch (FileNotFoundException e) {
            System.err.println("ServerError: unable to find dictionary file at path '" + dictionaryFilePath + "'");
            System.exit(1);
        } catch (Exception e) {
            System.err.println(
                    "ServerError: unable to read and parse dictionary file at path '" + dictionaryFilePath + "'");
            System.exit(1);
        }

        // set up server socket
        ServerSocketFactory factory = ServerSocketFactory.getDefault();
        try (ServerSocket server = factory.createServerSocket(portNumber)) {
            System.out.println("Server: listening for connections on port " + portNumber);

            // TODO: initialise server GUI?

            // initialise thread pool to handle client requests
            ExecutorService threadPool = Executors.newFixedThreadPool(MAX_THREAD_POOL_SIZE);

            while (true) {
                try {
                    // pass off accepted client request to worker thread
                    Socket clientSocket = server.accept();
                    threadPool.execute(new RequestHandler(clientSocket));
                } catch (SocketException e) {
                    // according to the docs, this exception gets thrown when
                    // the socket (`server` in this case) has been closed
                    // reference:
                    // https://docs.oracle.com/javase/8/docs/api/java/net/ServerSocket.html#close--
                    System.out.println("Server shutdown: no longer listening for connections");
                    break;
                } catch (IOException e) {
                    System.err.println("ServerError: unable to accept incoming client connection");
                }
            }

            // server is shut down, so shut down thread pool as well
            threadPool.shutdown();
        } catch (IOException e) {
            System.err.println("ServerError: cannot listen for connections on port " + portNumber);
        }
    }
}
