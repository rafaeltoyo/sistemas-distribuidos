package app.message;

import app.agent.MulticastPeer;
import app.agent.Peer;

public class JoinResponse extends Message {

    public JoinResponse(MulticastPeer user, Peer receiver) {
        super(MessageType.JOIN_RESPONSE.toString(), user);
        setReceiver(receiver);
        setSignature("allow");
    }

}
