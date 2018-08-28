package app.message;

import org.json.JSONObject;
import app.peer.MulticastPeer;

public class JoinResponse extends Message {

    public JoinResponse(MulticastPeer selfPeer, int destPeerId) {
        JSONObject jsonMsg = new JSONObject();

        // (Alternativa) Fazer a resposta por unicast.
        jsonMsg.put("MessageType", "joinResponse");
        jsonMsg.put("Sender", selfPeer.getPeerId());
        jsonMsg.put("DestinatedTo", destPeerId);
        jsonMsg.put("PublicKey", bytesToHexString(selfPeer.getPublicKey().getEncoded()));

        msg = jsonMsg.toString().getBytes();
    }

}
