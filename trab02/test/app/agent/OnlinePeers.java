package app.agent;

import java.util.ArrayList;

public class OnlinePeers {

    private ArrayList<Peer> peers = new ArrayList<>();

    public OnlinePeers() {
        // nada
    }

    public void addPeer(Peer peer) {
        peers.add(peer.id, peer);
    }

    public Peer getPeer(int id) {
        return peers.get(id);
    }

    public boolean removePeer(Peer peer) { return peers.remove(peer); }

    public Peer removePeer(int id) { return peers.remove(id); }

    public boolean isOnline(int id) {
        return (peers.get(id) == null);
    }

    public boolean isOnline(Peer peer) {
        return (peers.get(peer.id) == null);
    }

    public int countOnline() {
        return peers.size();
    }

    public void copyOnlineListTo(ArrayList<Peer> list) {
        list.clear();
        list.addAll(peers);
    }
}
