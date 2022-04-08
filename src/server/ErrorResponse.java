package server;

import org.json.simple.JSONObject;

/**
 * @author Nico Dinata (770318)
 */
public class ErrorResponse extends JSONObject {
    public ErrorResponse(String errorTitle, String errorMessage) {
        this.put("status", "error");
        this.put("error", errorTitle);
        this.put("message", errorMessage);
    }
}
