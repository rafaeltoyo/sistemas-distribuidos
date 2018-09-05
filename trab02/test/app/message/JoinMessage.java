package app.message;

import app.agent.MulticastPeer;

public class JoinMessage extends Message {

    public JoinMessage(MulticastPeer user) {
        super(MessageType.JOIN_REQUEST.toString(), user);
    }

}
