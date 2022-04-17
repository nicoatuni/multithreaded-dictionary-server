package server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * This class parses and responds to an incoming client's requests by consulting
 * the dictionary handler for supported operations requested by the client. It
 * assumes that incoming requests are in the form of JSON objects, and that they
 * contain fields to identify the operation the client wants to carry out and
 * its payload.
 * 
 * @author Nico Eka Dinata (770318)
 * 
 */
public class RequestHandler implements Runnable {

    // the keys of the dictionary operations supported by this server
    private static final String REQUEST_OP_KEY = "operation";
    private static final String REQUEST_PAYLOAD_KEY = "payload";
    private static final String REQUEST_OP_QUERY_WORD = "query_word";
    private static final String REQUEST_OP_ADD_WORD = "add_word";
    private static final String REQUEST_OP_REMOVE_WORD = "remove_word";
    private static final String REQUEST_OP_UPDATE_WORD = "update_word";

    private Socket clientSocket;

    public RequestHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        BufferedReader input = null;
        BufferedWriter output = null;

        // try to set up client socket's I/O streams
        try {
            input = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream()));
            output = new BufferedWriter(new OutputStreamWriter(this.clientSocket.getOutputStream()));
        } catch (IOException e) {
            System.err.println("RequestHandlerError: unable to read client stream, closing socket and exiting");
            if (!this.clientSocket.isClosed()) {
                try {
                    this.clientSocket.close();
                } catch (IOException sioe) {
                    System.err.println("RequestHandlerError: unable to close client socket, exiting");
                }
            }
            return;
        }

        // System.out.println("Hello client!");

        // read and parse client requests as long as there are any
        String inputString;
        try {
            while ((inputString = input.readLine()) != null) {
                try {
                    String response = this.parseRequest(inputString);
                    output.write(response + "\n");
                    output.flush();
                } catch (IOException e) {
                    System.err.println("RequestHandlerError: unable to write response to client");
                }
            }
        } catch (IOException e) {
            System.err.println("RequestHandlerError: error when reading from client input");
        }

        // client is no longer interested: let's close and exit
        try {
            // System.out.println("Bye client!");
            this.clientSocket.close();
        } catch (IOException e) {
            System.err.println("RequestHandlerError: unable to close client socket, exiting");
        }
    }

    /**
     * Parses the incoming client's request string and handles it if it contains
     * a valid dictionary operation. Returns a success response object if this
     * operation is successful. Otherwise, returns an error response object to
     * be sent to the client.
     */
    private String parseRequest(String requestString) {
        JSONParser parser = new JSONParser();

        try {
            // try to parse incoming request string as JSON
            JSONObject requestObj = null;
            try {
                requestObj = (JSONObject) parser.parse(requestString);
            } catch (ParseException e) {
                return new ErrorResponse("Invalid Request", "Request is not valid JSON.").toJSONString();
            }

            // check if request JSON has required fields
            if (!requestObj.containsKey(REQUEST_OP_KEY) || !requestObj.containsKey(REQUEST_PAYLOAD_KEY)) {
                return new ErrorResponse("Invalid Request", "Request object is missing some fields.").toJSONString();
            }

            switch ((String) requestObj.get(REQUEST_OP_KEY)) {

                // handle query for a word's definition
                case REQUEST_OP_QUERY_WORD: {
                    String queryWord = (String) requestObj.get(REQUEST_PAYLOAD_KEY);
                    String queryDefinition = DictionaryHandler.getDefinition(queryWord);
                    return new SuccessResponse(queryDefinition).toJSONString();
                }

                // handle adding a new word definition
                case REQUEST_OP_ADD_WORD: {
                    JSONObject payload = (JSONObject) requestObj.get(REQUEST_PAYLOAD_KEY);
                    String newWord = (String) payload.get("word");
                    String newDefinition = (String) payload.get("definition");
                    DictionaryHandler.addDefinition(newWord, newDefinition);
                    return new SuccessResponse(null).toJSONString();
                }

                // handle removing a word
                case REQUEST_OP_REMOVE_WORD: {
                    String targetWord = (String) requestObj.get(REQUEST_PAYLOAD_KEY);
                    DictionaryHandler.removeWord(targetWord);
                    return new SuccessResponse(null).toJSONString();
                }

                // handle updating a word's definition
                case REQUEST_OP_UPDATE_WORD: {
                    JSONObject payload = (JSONObject) requestObj.get(REQUEST_PAYLOAD_KEY);
                    String updatedWord = (String) payload.get("word");
                    String updatedDefinition = (String) payload.get("definition");
                    DictionaryHandler.updateDefinition(updatedWord, updatedDefinition);
                    return new SuccessResponse(null).toJSONString();
                }

                default:
                    return new ErrorResponse("Invalid Operation", "Requested operation is not supported by the server.")
                            .toJSONString();
            }
        } catch (Exception e) {
            return ExceptionHandler.handle(e, null).toJSONString();
        }
    }
}
