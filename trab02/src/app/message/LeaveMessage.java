package app.message;

import app.agent.MulticastPeer;
import org.json.JSONObject;

import javax.crypto.Cipher;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;

public class LeaveMessage extends Message {

    public LeaveMessage(MulticastPeer user) throws GeneralSecurityException {
        super(MessageType.LEAVE.toString(), user);
        setSignature("leave");
    }

}
