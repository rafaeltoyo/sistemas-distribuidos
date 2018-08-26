package message;

import org.json.JSONObject;
import peer.MulticastPeer;

public class JoinMessage extends Message {

    public JoinMessage(MulticastPeer selfPeer) {
        JSONObject jsonMsg = new JSONObject();

        jsonMsg.put("MessageType", "join");
        jsonMsg.put("Sender", selfPeer.getPeerId());
        jsonMsg.put("PublicKey", bytesToHexString(selfPeer.getPublicKey().getEncoded()));

        msg = jsonMsg.toString().getBytes();
    }
}
