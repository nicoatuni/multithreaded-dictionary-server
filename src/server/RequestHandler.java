package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * @author Nico Dinata (770318)
 */
public class RequestHandler implements Runnable {
    public static final String REQUEST_EXIT = "exit";

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
        try (DataInputStream input = new DataInputStream(clientSocket.getInputStream());
                DataOutputStream output = new DataOutputStream(clientSocket.getOutputStream())) {
            while (true) {
                if (input.available() > 0) {
                    String inputString = input.readUTF();
                    // System.out.println("Client request received: " + inputString);
                    if (inputString.equals(RequestHandler.REQUEST_EXIT)) {
                        break;
                    }

                    String response = parseRequest(inputString);
                    output.writeUTF(response);
                }
            }
            clientSocket.close();
        } catch (IOException e) {
            System.err.println("ServerError: something wrong when serving client request");
        }
    }

    private static String parseRequest(String requestString) {
        JSONParser parser = new JSONParser();

        try {
            JSONObject requestObj = (JSONObject) parser.parse(requestString);
            if (!requestObj.containsKey(REQUEST_OP_KEY) || !requestObj.containsKey(REQUEST_PAYLOAD_KEY)) {
                return new ErrorResponse("Invalid Request", "Request object is missing some fields.").toJSONString();
            }

            switch ((String) requestObj.get(REQUEST_OP_KEY)) {
                case REQUEST_OP_QUERY_WORD:
                    String queryWord = (String) requestObj.get(REQUEST_PAYLOAD_KEY);
                    String queryDefinition = DictionaryHandler.getDefinition(queryWord);
                    return new SuccessResponse(queryDefinition).toJSONString();

                case REQUEST_OP_ADD_WORD: {
                    JSONObject payload = (JSONObject) requestObj.get(REQUEST_PAYLOAD_KEY);
                    String newWord = (String) payload.get("word");
                    String newDefinition = (String) payload.get("definition");
                    DictionaryHandler.addDefinition(newWord, newDefinition);
                    return new SuccessResponse(null).toJSONString();
                }

                case REQUEST_OP_REMOVE_WORD:
                    String targetWord = (String) requestObj.get(REQUEST_PAYLOAD_KEY);
                    DictionaryHandler.removeWord(targetWord);
                    return new SuccessResponse(null).toJSONString();

                case REQUEST_OP_UPDATE_WORD:
                    JSONObject payload = (JSONObject) requestObj.get(REQUEST_PAYLOAD_KEY);
                    String updatedWord = (String) payload.get("word");
                    String updatedDefinition = (String) payload.get("definition");
                    DictionaryHandler.updateDefinition(updatedWord, updatedDefinition);
                    return new SuccessResponse(null).toJSONString();

                default:
                    return new ErrorResponse("Invalid Operation", "Requested operation is not supported by the server.")
                            .toJSONString();
            }
        } catch (Exception e) {
            return ExceptionHandler.handle(e, null).toJSONString();
        }
    }
}
