package app.resource;

import app.peer.Peer;

public class ResourceQueueItem {
    private Peer peer;
    private long timestamp;

    public Peer getPeer() {
        return peer;
    }

    public long getTimestamp() {
        return timestamp;
    }

    /*------------------------------------------------------------------------*/

    public ResourceQueueItem(Peer peer, long timestamp) {
        this.peer = peer;
        this.timestamp = timestamp;
    }
}
