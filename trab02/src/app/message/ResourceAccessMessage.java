package app.message;

import app.peer.MulticastPeer;
import org.json.JSONObject;

public class ResourceAccessMessage extends Message {

    private long timestamp;

    /*------------------------------------------------------------------------*/

    public long getTimestamp() {
        return timestamp;
    }

    /*------------------------------------------------------------------------*/

    public ResourceAccessMessage(MulticastPeer selfPeer, short resource) {
        JSONObject jsonMsg = new JSONObject();

        this.timestamp = System.currentTimeMillis();

        jsonMsg.put("MessageType", "resourceAccess");
        jsonMsg.put("Sender", selfPeer.getPeerId());
        jsonMsg.put("Resource", resource);
        jsonMsg.put("Timestamp", this.timestamp);

        msg = jsonMsg.toString().getBytes();
    }

}
