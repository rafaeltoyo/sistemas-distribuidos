/*============================================================================*/
/* MulticastPeer.java                                                         */
/*                                                                            */
/* CLASSE MULTICAST PEER                                                      */
/*============================================================================*/
/* Autor: Rafael Hideo Toyomoto e Victor Barpp Gomes                          */
/*                                                                            */
/* 2018-09-04                                                                 */
/*============================================================================*/
// Esta classe representa o usuário como um peer.
/*============================================================================*/

package app.agent;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;

public class MulticastPeer extends Peer {

    /**
     * Tamanho da chave RSA em bits
     */
    private final static int KEYSIZE_BITS = 512;

    /**
     * Chave privada do MulticastPeer
     */
    protected PrivateKey privateKey;

    public MulticastPeer(int id) throws NoSuchAlgorithmException {
        super(id, null);

        // Gera chaves RSA
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(KEYSIZE_BITS);
        KeyPair keyPair = keyGen.generateKeyPair();

        // Salvar a chave privada gerada
        privateKey = keyPair.getPrivate();

        // Salvar a chave pública gerada
        publicKey = keyPair.getPublic();
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }
}
