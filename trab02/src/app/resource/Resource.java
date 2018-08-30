package app.resource;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.*;

import app.message.ResourceAccessMessage;
import app.message.ResourceAccessResponse;
import app.peer.MulticastPeer;
import app.peer.Peer;

public class Resource {
	
    public ResourceState state;
    public PriorityQueue<ResourceQueueItem> requestQueue;
    
    private long timestamp = 0;
    
    private MulticastPeer selfPeer;
    private short resourceId;

    private ArrayList<Peer> peersWhoResponded;
    private ArrayList<Peer> peersWhoRespondedPositively;
    private int waitNumResponses;
    private int waitNumPositiveResponses;

    private static final long timeoutMs = 2000; // 1 segundo

    /*------------------------------------------------------------------------*/

    public short getResourceId() {
        return resourceId;
    }

    /*------------------------------------------------------------------------*/
    
    public Resource(MulticastPeer selfPeer, short resourceId) {
        this.state = ResourceState.RELEASED;
        
        this.selfPeer = selfPeer;
        this.resourceId = resourceId;

        this.requestQueue = new PriorityQueue<>(Comparator.comparingLong(ResourceQueueItem::getTimestamp));

        this.peersWhoResponded = new ArrayList<>();
        this.peersWhoRespondedPositively = new ArrayList<>();
        this.waitNumResponses = 0;
        this.waitNumPositiveResponses = 0;
    }

    /*------------------------------------------------------------------------*/
    
    public void accept(int peerId, long timestamp) throws IOException, GeneralSecurityException {
        Peer p = selfPeer.getPeerById(peerId);
        if (p == null) {
            return;
        }

        // Verifica se o recurso está ocupado, ou se este processo tem prioridade de tempo em relação ao outro
        boolean allow = true;
    	if (state == ResourceState.HELD || (state == ResourceState.WANTED && this.timestamp < timestamp)) {
            allow = false;
    		requestQueue.add(new ResourceQueueItem(p, timestamp));
    	}

    	// Envia ALLOW ou DENY de acordo com a flag allow.
        // Se o recurso estiver ocupado, ou se este processo pediu o recurso antes do outro, envia DENY
        // Senão, envia allow.
        ResourceAccessResponse response = new ResourceAccessResponse(selfPeer, peerId, resourceId, allow);
        selfPeer.getConn().send(response);
    }

    /*------------------------------------------------------------------------*/
    
    public boolean hold() throws IOException {
    	if (state != ResourceState.RELEASED) {
    		return false;
    	}
    	
    	// Envia pedido do recurso à rede
        ResourceAccessMessage request = new ResourceAccessMessage(selfPeer, resourceId);
    	timestamp = request.getTimestamp();
    	state = ResourceState.WANTED;
        selfPeer.getConn().send(request);

        peersWhoResponded.clear();
        peersWhoRespondedPositively.clear();
        waitNumResponses = selfPeer.getOnlinePeerCount();
        waitNumPositiveResponses = waitNumResponses;
    	
    	// TODO Esperar respostas (N - 1)
        // Como implementar o timeout?

        // Primeira layer de espera: receber todas as mensagens positivas ou timeout
        //while (waitNumPositiveResponses > 0 || !timeout) {
        //    Thread.onSpinWait();
        //}
        synchronized (selfPeer.getRecvThread()) {
            try {
                selfPeer.getRecvThread().wait(timeoutMs);
            } catch (InterruptedException e) {
                System.err.println("Thread: " + e);
                return false;
            }
        }

        // Se alguém não respondeu, retira da rede
        if (waitNumResponses != 0) {
            // TODO: Retirar da rede
        }

        // Segunda layer de espera: receber todas as mensagens positivas
        //while (waitNumPositiveResponses > 0) {
        //    Thread.onSpinWait();
        //}
        if (waitNumPositiveResponses != 0) {
            synchronized (selfPeer.getRecvThread()) {
                try {
                    selfPeer.getRecvThread().wait();
                } catch (InterruptedException e) {
                    System.err.println("Thread: " + e);
                    return false;
                }
            }
        }

        // FIXME DEBUG
        System.out.println("Recebi todos os ALLOW, vou acessar o recurso " + resourceId);
    	
    	state = ResourceState.HELD;
    	timestamp = 0;
    	
    	return true;
    }

    /*------------------------------------------------------------------------*/
    
    public boolean release() throws IOException, GeneralSecurityException {
    	if (state != ResourceState.HELD) {
    		return false;
    	}
    	
    	state = ResourceState.RELEASED;

        // FIXME DEBUG
        System.out.println("Estou largando o recurso " + resourceId);
    	
    	// TODO Enviar aviso aos peers adicionados a fila de espera
        for (ResourceQueueItem p = requestQueue.poll(); p != null; p = requestQueue.poll()) {
            ResourceAccessResponse response = new ResourceAccessResponse(selfPeer, p.getPeer().getPeerId(), resourceId, true);
            selfPeer.getConn().send(response);
        }
    	
    	return true;
    }

    /*------------------------------------------------------------------------*/

    public void receivedResponse(Peer p, boolean allow) {
        if (!peersWhoResponded.contains(p)) {
            // Peer ainda não tinha respondido
            peersWhoResponded.add(p);
            waitNumResponses--;
            if (allow) {
                peersWhoRespondedPositively.add(p);
                waitNumPositiveResponses--;
            }
        }
        else {
            // Peer já respondeu uma vez
            if (allow) {
                peersWhoRespondedPositively.add(p);
                waitNumPositiveResponses--;
            }
        }
        // FIXME DEBUG
        System.out.println("Received response: total left = " + waitNumResponses + "; positive left = " + waitNumPositiveResponses);
        if (waitNumPositiveResponses == 0) {
            synchronized (selfPeer.getRecvThread()) {
                selfPeer.getRecvThread().notify();
            }
        }
    }
}
