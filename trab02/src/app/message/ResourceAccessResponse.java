package app.message;

import app.agent.MulticastPeer;
import app.agent.Peer;
import org.json.JSONObject;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class ResourceAccessResponse extends Message {

    private short resource;

    public ResourceAccessResponse(MulticastPeer user, Peer receiver, short resource, boolean allow) throws GeneralSecurityException {
        super(MessageType.RESOURCE_ACCESS_RESPONSE.toString(), user);
        setReceiver(receiver);
        setSignature(allow ? "allow" : "deny");
        this.resource = resource;
    }

    @Override
    public JSONObject getJSON() throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        JSONObject json = super.getJSON();
        json.put("resource", resource);
        return json;
    }

}
