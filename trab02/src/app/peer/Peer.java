package app.peer;

import java.security.PublicKey;

public class Peer {
    protected int peerId;
    protected PublicKey publicKey;

    /*------------------------------------------------------------------------*/

    public int getPeerId() {
        return peerId;
    }

    /*------------------------------------------------------------------------*/

    public Peer(int peerId, PublicKey publicKey) {
        this.peerId = peerId;
        this.publicKey = publicKey;
    }
}
