package app.agent;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;

public class MulticastPeer extends Peer {

    // Tamanho da chave RSA em bits
    private final static int KEYSIZE_BITS = 512;

    protected PrivateKey privateKey;

    public MulticastPeer(int id) throws NoSuchAlgorithmException {
        super(id, null);

        // Gera chaves RSA
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(KEYSIZE_BITS);
        KeyPair keyPair = keyGen.generateKeyPair();
        privateKey = keyPair.getPrivate();
        publicKey = keyPair.getPublic();
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }
}
