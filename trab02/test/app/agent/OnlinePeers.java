package app.agent;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OnlinePeers {

    private Map<Integer, Peer> peers = new HashMap<>();

    /*------------------------------------------------------------------------*/

    public OnlinePeers() {
        // nada
    }

    /*------------------------------------------------------------------------*/

    public void addPeer(Peer peer) {
        peers.put(peer.id, peer);
    }

    /*------------------------------------------------------------------------*/

    public Peer getPeer(int id) {
        return peers.get(id);
    }

    /*------------------------------------------------------------------------*/

    public boolean removePeer(Peer peer) {
        return peers.remove(peer.id, peer);
    }

    public Peer removePeer(int id) {
        return peers.remove(id);
    }

    /*------------------------------------------------------------------------*/

    public boolean isOnline(int id) {
        try {
            return peers.get(id) != null;
        } catch (IndexOutOfBoundsException e) {
            return false;
        }
    }

    public boolean isOnline(Peer peer) {
        return (peers.get(peer.id) == null);
    }

    /*------------------------------------------------------------------------*/

    public int countOnline() {
        return peers.size();
    }

    /*------------------------------------------------------------------------*/

    public void copyOnlineListTo(ArrayList<Peer> list) {
        list.clear();
        list.addAll(peers.values());
    }

    /*------------------------------------------------------------------------*/
}
