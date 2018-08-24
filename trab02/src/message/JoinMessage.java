package message;

import java.security.PublicKey;

public class JoinMessage extends Message {

    public JoinMessage(PublicKey publicKey) {
        String enterMsg = "StatusCode: 100\n" +
                "PublicKey: " + bytesToHexString(publicKey.getEncoded()) + "\n";
        msg = enterMsg.getBytes();
    }
}
