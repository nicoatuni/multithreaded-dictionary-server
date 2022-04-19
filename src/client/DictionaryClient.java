package client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * @author Nico Dinata (770318)
 */
public class DictionaryClient {
    private static final String REQUEST_OP_QUERY_WORD = "query_word";
    private static final String REQUEST_OP_ADD_WORD = "add_word";
    private static final String REQUEST_OP_REMOVE_WORD = "remove_word";
    private static final String REQUEST_OP_UPDATE_WORD = "update_word";

    private static final String SERVER_RESPONSE_SUCCESS = "success";
    private static final String SERVER_RESPONSE_ERROR = "error";

    private BufferedReader input = null;
    private BufferedWriter output = null;

    public static void main(String[] args) {

        if (args.length < 2) {
            System.err.println("Usage: java -jar DictionaryClient.jar <server_hostname> <server_port>");
            System.exit(1);
        }

        final String serverHostname = args[0];
        final int serverPortNumber = Integer.parseInt(args[1]);

        Socket socket = null;
        try {
            socket = new Socket(serverHostname, serverPortNumber);
        } catch (UnknownHostException e) {
            System.err.println("ClientError: unrecogniseable server address: '" + serverHostname + "' (port "
                    + serverPortNumber
                    + "), exiting");
            System.exit(1);
        } catch (IOException e) {
            System.err.println("ClientError: unable to connect to server at address '" + serverHostname + "' on port "
                    + serverPortNumber + ", exiting");
            System.exit(1);
        }

        DictionaryClient clientInstance = new DictionaryClient();
        try {
            clientInstance.input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            clientInstance.output = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (IOException e) {
            System.err.println("ClientError: unable to read server stream, closing socket and exiting");
            if (socket != null && !socket.isClosed()) {
                try {
                    socket.close();
                } catch (IOException sioe) {
                    System.err.println("ClientError: unable to close socket, exiting");
                }
            }
            System.exit(1);
        }

        ClientGUI gui = new ClientGUI(clientInstance);
        while (gui.isRunning()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                System.err.println("ClientError: main is interrupted, exiting");
                break;
            }
        }

        try {
            System.out.println("Client shutdown: disconnecting from server");
            if (clientInstance.input != null) {
                clientInstance.input.close();
            }
            if (clientInstance.output != null) {
                clientInstance.output.close();
            }
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        } catch (IOException e) {
            System.err.println("ClientError: unable to close socket, exiting");
        }
        System.exit(0);
    }

    public String requestQueryWord(String word) {
        if (word.isBlank()) {
            return "Please enter a valid word to search for.";
        }

        JSONObject requestObj = new JSONObject();
        requestObj.put("operation", REQUEST_OP_QUERY_WORD);
        requestObj.put("payload", word);

        String serverResponse = "";
        try {
            JSONObject responseObj = writeToServer(requestObj.toJSONString());
            String status = (String) responseObj.get("status");
            if (status.equals(SERVER_RESPONSE_SUCCESS)) {
                String payload = (String) responseObj.get("payload");
                if (payload == null) {
                    serverResponse = "The word '" + word + "' does not exist in the dictionary.";
                } else {
                    serverResponse = payload;
                }
            } else {
                String errorTitle = (String) responseObj.get("error");
                String errorMsg = (String) responseObj.get("message");
                serverResponse = errorTitle + ": " + errorMsg;
            }
        } catch (IOException e) {
            serverResponse = "Unable to communicate with the server, please try again.";
        } catch (ParseException | InvalidServerResponseException e) {
            serverResponse = "Invalid server response, please try again.";
        }
        return serverResponse;
    }

    public String requestAddWord(String word, String definition) {
        if (word.isBlank() || definition.isBlank()) {
            return "Both the new word and its definition can not be blank.";
        }

        JSONObject requestObj = new JSONObject();
        requestObj.put("operation", REQUEST_OP_ADD_WORD);
        JSONObject addPayload = new JSONObject();
        addPayload.put("word", word);
        addPayload.put("definition", definition);
        requestObj.put("payload", addPayload);

        String serverResponse = "";
        try {
            JSONObject responseObj = writeToServer(requestObj.toJSONString());
            String status = (String) responseObj.get("status");
            if (status.equals(SERVER_RESPONSE_SUCCESS)) {
                serverResponse = "Success!";
            } else {
                String errorTitle = (String) responseObj.get("error");
                String errorMsg = (String) responseObj.get("message");
                serverResponse = errorTitle + ": " + errorMsg;
            }
        } catch (IOException e) {
            serverResponse = "Unable to communicate with the server, please try again";
        } catch (ParseException | InvalidServerResponseException e) {
            serverResponse = "Invalid server response, please try again";
        }
        return serverResponse;
    }

    public String requestUpdateWord(String word, String definition) {
        if (word.isBlank() || definition.isBlank()) {
            return "Both the word and its updated definition can not be blank.";
        }

        JSONObject requestObj = new JSONObject();
        requestObj.put("operation", REQUEST_OP_UPDATE_WORD);
        JSONObject addPayload = new JSONObject();
        addPayload.put("word", word);
        addPayload.put("definition", definition);
        requestObj.put("payload", addPayload);

        String serverResponse = "";
        try {
            JSONObject responseObj = writeToServer(requestObj.toJSONString());
            String status = (String) responseObj.get("status");
            if (status.equals(SERVER_RESPONSE_SUCCESS)) {
                serverResponse = "Success!";
            } else {
                String errorTitle = (String) responseObj.get("error");
                String errorMsg = (String) responseObj.get("message");
                serverResponse = errorTitle + ": " + errorMsg;
            }
        } catch (IOException e) {
            serverResponse = "Unable to communicate with the server, please try again";
        } catch (ParseException | InvalidServerResponseException e) {
            serverResponse = "Invalid server response, please try again";
        }
        return serverResponse;
    }

    public String requestRemoveWord(String word) {
        if (word.isBlank()) {
            return "Please enter a valid word to remove.";
        }

        JSONObject requestObj = new JSONObject();
        requestObj.put("operation", REQUEST_OP_REMOVE_WORD);
        requestObj.put("payload", word);

        String serverResponse = "";
        try {
            JSONObject responseObj = writeToServer(requestObj.toJSONString());
            String status = (String) responseObj.get("status");
            if (status.equals(SERVER_RESPONSE_SUCCESS)) {
                serverResponse = "Success!";
            } else {
                String errorTitle = (String) responseObj.get("error");
                String errorMsg = (String) responseObj.get("message");
                serverResponse = errorTitle + ": " + errorMsg;
            }
        } catch (IOException e) {
            serverResponse = "Unable to communicate with the server, please try again";
        } catch (ParseException | InvalidServerResponseException e) {
            serverResponse = "Invalid server response, please try again";
        }
        return serverResponse;
    }

    private JSONObject writeToServer(String requestString)
            throws IOException, ParseException, InvalidServerResponseException {
        output.write(requestString + "\n");
        output.flush();
        String serverResponse = input.readLine();

        JSONParser parser = new JSONParser();
        JSONObject responseObj = (JSONObject) parser.parse(serverResponse);
        String responseStatus = (String) responseObj.get("status");

        if (!SERVER_RESPONSE_SUCCESS.equalsIgnoreCase(responseStatus)
                && !SERVER_RESPONSE_ERROR.equalsIgnoreCase(responseStatus)) {
            throw new InvalidServerResponseException();
        }
        return responseObj;
    }
}

class InvalidServerResponseException extends Exception {
}
