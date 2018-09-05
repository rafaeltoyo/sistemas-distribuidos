package app.message;

import app.agent.MulticastPeer;
import org.json.JSONObject;

public class JoinResponse extends Message {

    public JoinResponse(MulticastPeer user, int destPeerId) {
        JSONObject jsonMsg = new JSONObject();

        // (Alternativa) Fazer a resposta por unicast.
        jsonMsg.put("MessageType", "joinResponse");
        jsonMsg.put("Sender", user.getId());
        jsonMsg.put("Destinatary", destPeerId);
        jsonMsg.put("PublicKey", bytesToHexString(user.getPublicKey().getEncoded()));

        msg = jsonMsg.toString().getBytes();
    }

}
