package app.message;

import app.peer.MulticastPeer;
import org.json.JSONObject;

public class ResourceAccessMessage extends Message {

    public ResourceAccessMessage(MulticastPeer selfPeer, short resource) {
        JSONObject jsonMsg = new JSONObject();

        jsonMsg.put("MessageType", "resourceAccess");
        jsonMsg.put("Sender", selfPeer.getPeerId());
        jsonMsg.put("Resource", resource);
        jsonMsg.put("Timestamp", System.currentTimeMillis());

        msg = jsonMsg.toString().getBytes();
    }

}
