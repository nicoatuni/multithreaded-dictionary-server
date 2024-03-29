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
 * to worker threads in a pool to process.
 * 
 * @author Nico Eka Dinata (770318)
 * 
 */
public class DictionaryServer {
    // private static final int MAX_THREAD_POOL_SIZE = 16;

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
            System.err.println(
                    "ServerError: unable to find dictionary file at path '" + dictionaryFilePath + "', exiting");
            System.exit(1);
        } catch (Exception e) {
            System.err.println(
                    "ServerError: unable to read and parse dictionary file at path '" + dictionaryFilePath
                            + "', exiting");
            System.exit(1);
        }

        // initialise the GUI and wait until we are allowed to start the server
        ServerGUI gui = new ServerGUI();
        while (gui.isRunning() && !gui.shouldServerStart()) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                System.err.println("ServerError: main is interrupted, exiting");
                System.exit(1);
            }
        }

        // if GUI is no longer running, we shut down the server
        if (!gui.isRunning()) {
            System.out.println("Server shutdown: exiting");
            System.exit(0);
        }

        // set up server socket
        ServerSocketFactory factory = ServerSocketFactory.getDefault();
        try (ServerSocket server = factory.createServerSocket(portNumber)) {
            System.out.println("Server: listening for connections on port " + portNumber);

            // initialise thread pool using a size value set on the GUI
            int threadPoolSize = gui.useThreadCount(server);
            ExecutorService threadPool = Executors.newFixedThreadPool(threadPoolSize);

            while (gui.isRunning()) {
                try {
                    // pass off accepted client request to worker thread
                    Socket clientSocket = server.accept();
                    threadPool.execute(new RequestHandler(clientSocket));
                } catch (SocketException e) {
                    // according to the docs, this exception gets thrown when
                    // the socket (`server` in this case) has been closed
                    // reference:
                    // https://docs.oracle.com/javase/8/docs/api/java/net/ServerSocket.html#close--
                    break;
                } catch (IOException e) {
                    System.err.println("ServerError: unable to accept incoming client connection");
                }
            }

            // shut down server socket and thread pool
            System.out.println("Server shutdown: no longer listening for connections, exiting");
            if (!server.isClosed()) {
                try {
                    server.close();
                } catch (IOException e) {
                    System.err.println("ServerError: unable to close socket, exiting");
                }
            }
            threadPool.shutdown();
            System.exit(0);
        } catch (IOException e) {
            System.err.println("ServerError: unable to listen for connections on port " + portNumber + ", exiting");
            System.exit(1);
        }
    }
}
