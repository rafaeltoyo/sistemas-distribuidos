package app.message;

import app.agent.MulticastPeer;
import org.json.JSONObject;

import javax.crypto.Cipher;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;

public class LeaveMessage extends Message {

    public LeaveMessage(MulticastPeer user) throws GeneralSecurityException {
        JSONObject jsonMsg = new JSONObject();

        jsonMsg.put("MessageType", "leave");
        jsonMsg.put("Sender", user.getId());

        // Adiciona um campo "Auth", que contém a string "LEAVE" cifrada com a chave privada.
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, user.getPrivateKey());
        
        byte[] leaveMsg;
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update("LEAVE".getBytes());
        leaveMsg = cipher.doFinal(md.digest());

        jsonMsg.put("Auth", bytesToHexString(leaveMsg));

        msg = jsonMsg.toString().getBytes();
    }
}
