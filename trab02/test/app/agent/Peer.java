package app.agent;

import java.security.PublicKey;

public class Peer {

    protected int id;

    protected PublicKey publicKey;

    public Peer(int id, PublicKey publicKey) {
        this.id = id;
        this.publicKey = publicKey;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }
}
