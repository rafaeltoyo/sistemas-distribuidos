package app.message;

import app.peer.MulticastPeer;
import org.json.JSONObject;

import javax.crypto.Cipher;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;

public class LeaveMessage extends Message {

    public LeaveMessage(MulticastPeer selfPeer) throws GeneralSecurityException {
        JSONObject jsonMsg = new JSONObject();

        jsonMsg.put("MessageType", "leave");
        jsonMsg.put("Sender", selfPeer.getPeerId());

        // Adiciona um campo "Auth", que contém a string "LEAVE" cifrada com a chave privada.
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, selfPeer.getPrivateKey());
        
        byte[] leaveMsg;
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update("LEAVE".getBytes());
        leaveMsg = cipher.doFinal(md.digest());

        jsonMsg.put("Auth", bytesToHexString(leaveMsg));

        msg = jsonMsg.toString().getBytes();
    }
}
