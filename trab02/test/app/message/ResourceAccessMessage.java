package app.message;

import app.agent.MulticastPeer;
import org.json.JSONObject;

public class ResourceAccessMessage extends Message {

    private long timestamp;

    /*------------------------------------------------------------------------*/

    public long getTimestamp() {
        return timestamp;
    }

    /*------------------------------------------------------------------------*/

    public ResourceAccessMessage(MulticastPeer user, short resource) {
        JSONObject jsonMsg = new JSONObject();

        this.timestamp = System.currentTimeMillis();

        jsonMsg.put("MessageType", "resourceAccess");
        jsonMsg.put("Sender", user.getId());
        jsonMsg.put("Resource", resource);
        jsonMsg.put("Timestamp", this.timestamp);

        msg = jsonMsg.toString().getBytes();
    }

}
