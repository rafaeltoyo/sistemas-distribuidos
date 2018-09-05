package app.message;

import app.agent.MulticastPeer;
import org.json.JSONObject;

public class JoinMessage extends Message {

    public JoinMessage(MulticastPeer user) {
        JSONObject jsonMsg = new JSONObject();

        jsonMsg.put("MessageType", "join");
        jsonMsg.put("Sender", user.getId());
        jsonMsg.put("PublicKey", bytesToHexString(user.getPublicKey().getEncoded()));

        msg = jsonMsg.toString().getBytes();
    }
}
