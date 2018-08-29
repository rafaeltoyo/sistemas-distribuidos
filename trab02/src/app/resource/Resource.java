package app.resource;

import java.util.Comparator;
import java.util.PriorityQueue;

import app.peer.MulticastPeer;
import app.peer.Peer;

public class Resource {
	
    public ResourceState state;
    public PriorityQueue<ResourceQueueItem> requestQueue;
    
    private long timestamp = 0;
    
    private MulticastPeer selfPeer;
    
    public Resource(MulticastPeer selfPeer) {
        state = ResourceState.RELEASED;
        
        this.selfPeer = selfPeer;

        requestQueue = new PriorityQueue<>(Comparator.comparingLong(ResourceQueueItem::getTimestamp));
    }
    
    public boolean accept(Peer peer, long timestamp) {
    	if (state == ResourceState.HELD || (state == ResourceState.WANTED && this.timestamp < timestamp)) {
    		requestQueue.add(new ResourceQueueItem(peer, timestamp));
    		// TODO Enviar resposta de recurso ocupado
    		return false;
    	}
    	// TODO Enviar resposta de recurso livre
    	return true;
    }
    
    public boolean hold() {
    	if (state != ResourceState.RELEASED) {
    		return false;
    	}
    	
    	// TODO Enviar pedido
    	
    	timestamp = System.currentTimeMillis();
    	state = ResourceState.WANTED;
    	
    	// TODO Esperar respostas (N - 1)
    	
    	state = ResourceState.HELD;
    	timestamp = 0;
    	
    	return true;
    }
    
    public boolean release() {
    	if (state != ResourceState.HELD) {
    		return false;
    	}
    	
    	state = ResourceState.RELEASED;
    	
    	// TODO Enviar aviso aos peers adicionados a fila de espera
    	
    	return true;
    }
}
