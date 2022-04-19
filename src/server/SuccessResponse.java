package server;

import org.json.simple.JSONObject;

/**
 * Wrapper type representing a success response returned by the server.
 * 
 * @author Nico Eka Dinata (770318)
 * 
 */
public class SuccessResponse extends JSONObject {
    public SuccessResponse(String payload) {
        this.put("status", "success");
        this.put("payload", payload);
    }
}
