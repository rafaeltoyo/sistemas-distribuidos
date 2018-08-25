package message;

import org.json.JSONObject;

import java.security.PublicKey;

public class JoinMessage extends Message {

    public JoinMessage(PublicKey publicKey) {
        JSONObject jsonMsg = new JSONObject();

        jsonMsg.put("StatusCode", 100);
        jsonMsg.put("PublicKey", bytesToHexString(publicKey.getEncoded()));

        msg = jsonMsg.toString().getBytes();
    }
}
