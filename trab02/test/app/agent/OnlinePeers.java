/*============================================================================*/
/* OnlinePeer.java                                                            */
/*                                                                            */
/* CLASSE ONLINE PEER                                                         */
/*============================================================================*/
/* Autor: Rafael Hideo Toyomoto e Victor Barpp Gomes                          */
/*                                                                            */
/* 2018-09-04                                                                 */
/*============================================================================*/
// Esta classe representa a lista de peers online.
/*============================================================================*/

package app.agent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OnlinePeers {

    /**
     * Lista dos Peers
     */
    private Map<Integer, Peer> peers;

    public OnlinePeers() {
        peers = new HashMap<>();
    }

    /*------------------------------------------------------------------------*/

    /**
     * Adicionar um Peer a lista
     * @param peer
     */
    public void addPeer(Peer peer) {
        peers.put(peer.id, peer);
    }

    /*------------------------------------------------------------------------*/

    /**
     * Resgatar a instancia de um Peer pelo seu identificador
     * @param id
     * @return Peer
     */
    public Peer getPeer(int id) {
        return peers.get(id);
    }

    /*------------------------------------------------------------------------*/

    /**
     * Remove um Peer da lista pela sua instancia
     * @param peer
     * @return boolean
     */
    public boolean removePeer(Peer peer) {
        return peers.remove(peer.id, peer);
    }

    /**
     * Remove um Peer da lista pelo seu identificador
     * @param id
     * @return Peer
     */
    public Peer removePeer(int id) {
        return peers.remove(id);
    }

    /*------------------------------------------------------------------------*/

    /**
     * Consulta pelo identificador se um Peer esta online
     * @param id
     * @return boolean
     */
    public boolean isOnline(int id) {
        try {
            return peers.get(id) != null;
        } catch (IndexOutOfBoundsException e) {
            return false;
        }
    }

    /**
     * Consulta se um Peer esta online
     * @param peer
     * @return boolean
     */
    public boolean isOnline(Peer peer) {
        return (peers.get(peer.id) == null);
    }

    /*------------------------------------------------------------------------*/

    /**
     * Contar o numero de Peers online
     * @return int
     */
    public int countOnline() {
        return peers.size();
    }

    /*------------------------------------------------------------------------*/

    /**
     * Copiar a lista de Peers online para outra lista.
     * @param list
     */
    public void copyOnlineListTo(ArrayList<Peer> list) {
        list.clear();
        list.addAll(peers.values());
    }

    /*------------------------------------------------------------------------*/
}
