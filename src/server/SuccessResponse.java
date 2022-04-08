package server;

import org.json.simple.JSONObject;

/**
 * @author Nico Dinata (770318)
 */
public class SuccessResponse extends JSONObject {
    public SuccessResponse(String payload) {
        this.put("status", "success");
        this.put("payload", payload);
    }
}
