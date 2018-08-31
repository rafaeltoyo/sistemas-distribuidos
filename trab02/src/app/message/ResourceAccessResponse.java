package app.message;

import app.peer.MulticastPeer;
import org.json.JSONObject;

import javax.crypto.Cipher;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;

public class ResourceAccessResponse extends Message {

    public ResourceAccessResponse(MulticastPeer selfPeer, int destPeerId,
                                  short resource, boolean allow) throws GeneralSecurityException {
        JSONObject jsonMsg = new JSONObject();

        jsonMsg.put("MessageType", "resourceAccessResponse");
        jsonMsg.put("Sender", selfPeer.getPeerId());
        jsonMsg.put("Destinatary", destPeerId);
        jsonMsg.put("Resource", resource);

        // Adiciona um campo "Auth", que contém a string "ALLOW" ou "DENY" cifrada com a chave privada.
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, selfPeer.getPrivateKey());
        byte[] allowOrDeny;
        MessageDigest md = MessageDigest.getInstance("MD5");
        if (allow) {
        	md.update("ALLOW".getBytes());
        }
        else {
        	md.update("DENY".getBytes());
        }
        allowOrDeny = cipher.doFinal(md.digest());

        jsonMsg.put("Auth", bytesToHexString(allowOrDeny));

        msg = jsonMsg.toString().getBytes();
    }

}
