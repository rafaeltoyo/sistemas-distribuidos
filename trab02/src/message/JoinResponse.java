package message;

import org.json.JSONObject;

import java.security.PublicKey;

public class JoinResponse extends Message {

    public JoinResponse(PublicKey publicKey) {
        JSONObject jsonMsg = new JSONObject();

        // TODO: Adicionar ProcessID ao JSON
        jsonMsg.put("StatusCode", 101);
        jsonMsg.put("PublicKey", bytesToHexString(publicKey.getEncoded()));

        msg = jsonMsg.toString().getBytes();
    }

}
