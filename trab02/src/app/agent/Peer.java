/*============================================================================*/
/* Peer.java                                                                  */
/*                                                                            */
/* CLASSE PEER                                                                */
/*============================================================================*/
/* Autor: Rafael Hideo Toyomoto e Victor Barpp Gomes                          */
/*                                                                            */
/* 2018-09-04                                                                 */
/*============================================================================*/
// Esta classe representa um peer.
/*============================================================================*/

package app.agent;

import java.security.PublicKey;

public class Peer {

    /**
     * Identificador do peer
     */
    protected int id;

    /**
     * Chave pública do Peer
     */
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
