package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

import org.json.simple.JSONObject;

/**
 * @author Nico Dinata (770318)
 */
public class DictionaryClient {
    public static void main(String[] args) {
        if (args.length < 2) {
            System.err.println("Usage: java -jar DictionaryClient.jar <server_hostname> <server_port>");
            System.exit(1);
        }

        final String serverHostname = args[0];
        final int serverPortNumber = Integer.parseInt(args[1]);

        try (Socket socket = new Socket(serverHostname, serverPortNumber);
                DataInputStream input = new DataInputStream(socket.getInputStream());
                DataOutputStream output = new DataOutputStream(socket.getOutputStream())) {

            // TODO: testing only. This allows interactivity from the client side
            // to keep sending more requests to the server from user input. Need
            // to replace this with a form suitable for the GUI.
            Scanner scanner = new Scanner(System.in);
            String inputString = null;
            // quit client side by typing in "exit"
            while (!(inputString = scanner.nextLine()).equals("exit")) {
                String[] inputs = inputString.split("-");

                if (inputs.length < 2) {
                    continue;
                }

                String op = inputs[0];
                String opWord = inputs[1];
                JSONObject requestObj = new JSONObject();

                switch (op) {
                    case "add": {
                        requestObj.put("operation", "add_word");
                        JSONObject addPayload = new JSONObject();
                        addPayload.put("word", opWord);
                        addPayload.put("definition", inputs[2]);
                        requestObj.put("payload", addPayload);
                        break;
                    }

                    case "query": {
                        requestObj.put("operation", "query_word");
                        requestObj.put("payload", opWord);
                        break;
                    }

                    case "remove": {
                        requestObj.put("operation", "remove_word");
                        requestObj.put("payload", opWord);
                        break;
                    }
                }

                output.writeUTF(requestObj.toJSONString());

                String received = input.readUTF();
                System.out.println("Server response: " + received);
            }

            output.writeUTF("exit");
            System.out.println("Client exiting, bye!");
            scanner.close();

            // boolean flag = true;
            // while (flag) {
            // if (input.available() > 0) {
            // String msg = input.readUTF();
            // System.out.println("From server: " + msg);
            // flag = false;
            // }
            // }

        } catch (IOException e) {
            // TODO: handle more exceptions, and more gracefully
            System.out.println("Client exception");
            e.printStackTrace();
        }
    }
}
