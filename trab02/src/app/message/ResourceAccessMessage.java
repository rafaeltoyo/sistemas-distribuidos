package app.message;

import app.agent.MulticastPeer;
import org.json.JSONObject;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class ResourceAccessMessage extends Message {

    private short resource;

    public ResourceAccessMessage(MulticastPeer user, short resource) {
        super(MessageType.RESOURCE_ACCESS_REQUEST.toString(), user);
        this.resource = resource;
    }

    @Override
    public JSONObject getJSON() throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        JSONObject json = super.getJSON();
        json.put("resource", resource);
        return json;
    }
}
