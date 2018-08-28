package app.message;

import app.peer.MulticastPeer;
import org.json.JSONObject;

public class JoinMessage extends Message {

    public JoinMessage(MulticastPeer selfPeer) {
        JSONObject jsonMsg = new JSONObject();

        jsonMsg.put("MessageType", "join");
        jsonMsg.put("Sender", selfPeer.getPeerId());
        jsonMsg.put("PublicKey", bytesToHexString(selfPeer.getPublicKey().getEncoded()));

        msg = jsonMsg.toString().getBytes();
    }
}
