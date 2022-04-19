package server;

import org.json.simple.JSONObject;

/**
 * Wrapper type representing an error response returned by the server.
 * 
 * @author Nico Eka Dinata (770318)
 * 
 */
public class ErrorResponse extends JSONObject {
    public ErrorResponse(String errorTitle, String errorMessage) {
        this.put("status", "error");
        this.put("error", errorTitle);
        this.put("message", errorMessage);
    }
}
