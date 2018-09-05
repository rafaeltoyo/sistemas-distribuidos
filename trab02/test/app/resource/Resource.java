package app.resource;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

import app.Application;
import app.agent.MulticastPeer;
import app.agent.OnlinePeers;
import app.agent.Peer;
import app.message.ResourceAccessMessage;
import app.message.ResourceAccessResponse;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class Resource {
	
    public ResourceState state;
    public PriorityQueue<ResourceQueueItem> requestQueue;
    
    private long timestamp = 0;
    
    private short resourceId;
    private MulticastPeer user;
    private OnlinePeers peers;

    private ArrayList<Peer> peersWhoResponded;
    private ArrayList<Peer> peersWhoRespondedPositively;
    private ArrayList<Peer> peersWhoAreYetToRespond;
    private int waitNumResponses;
    private int waitNumPositiveResponses;

    private static final long timeoutMs = 2000; // 1 segundo

    /*------------------------------------------------------------------------*/

    public short getResourceId() {
        return resourceId;
    }

    public ResourceState getState() {
        return state;
    }

    /*------------------------------------------------------------------------*/
    
    public Resource(short resourceId, MulticastPeer user, OnlinePeers peers) {
        this.state = ResourceState.RELEASED;
        this.resourceId = resourceId;

        this.user = user;
        this.peers = peers;

        this.requestQueue = new PriorityQueue<>(Comparator.comparingLong(ResourceQueueItem::getTimestamp));

        this.peersWhoResponded = new ArrayList<>();
        this.peersWhoRespondedPositively = new ArrayList<>();
        this.peersWhoAreYetToRespond = new ArrayList<>();
        this.waitNumResponses = 0;
        this.waitNumPositiveResponses = 0;
    }

    /*------------------------------------------------------------------------*/
    
    public void accept(int peerId, long timestamp) throws IOException, GeneralSecurityException {
        Peer p = peers.getPeer(peerId);
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
        ResourceAccessResponse response = new ResourceAccessResponse(user, p, resourceId, allow);

        Application.getInstance().getConn().send(response);
    }

    /*------------------------------------------------------------------------*/
    
    public boolean hold() throws IOException, IllegalBlockSizeException, NoSuchPaddingException, BadPaddingException, NoSuchAlgorithmException, InvalidKeyException {
    	if (state != ResourceState.RELEASED) {
    		return false;
    	}

    	// Alteração 30/08 20:13 - Passei o envio da mensagem para depois da inicialização das variáveis
        waitNumResponses = peers.countOnline();
        waitNumPositiveResponses = waitNumResponses;

        peersWhoResponded.clear();
        peersWhoRespondedPositively.clear();

        peers.copyOnlineListTo(peersWhoAreYetToRespond);

        // FIXME: DEBUG
        System.out.println("Resource: Iniciando requisição do recurso " + resourceId);
        System.out.println("Resource: Faltam " + waitNumPositiveResponses + " respostas");
    	
    	// Envia pedido do recurso à rede
        ResourceAccessMessage request = new ResourceAccessMessage(user, resourceId);
    	timestamp = request.getTimestamp();
    	state = ResourceState.WANTED;
        Application.getInstance().getConn().send(request);

        // --- Primeira layer de espera (com timeout) ---
        // Espera as respostas (que são processadas pela função receivedResponse)
        // Bloqueia a thread principal por timeoutMs milissegundos, ou até receber todas as respostas
        // O número de respostas esperado (N) está contido na variável waitNumResponses
        synchronized (Application.getInstance().getRecvThread()) {
            try {
                Application.getInstance().getRecvThread().wait(timeoutMs);
            } catch (InterruptedException e) {
                System.err.println("Thread: " + e);
                return false;
            }
        }

        // Se alguém não respondeu, retira da lista de peers online
        if (waitNumResponses != 0) {
            for (Peer p : peersWhoAreYetToRespond) {
                peers.removePeer(p.getId());
                waitNumResponses--;
                waitNumPositiveResponses--;

                // FIXME: DEBUG
                System.out.println("Resource: Peer " + p.getId() + " não respondeu, removido da lista de peers online");
            }
        }

        // --- Segunda layer de espera ---
        // Apenas espera aqui caso algum peer tenha dado DENY no acesso ao recurso
        // Bloqueia a thread até que todos os outros peers permitam o acesso
        // *** É possível colocar um timeout aqui também, e usar um retorno booleano para
        // *** simbolizar o acesso ou não ao recurso
        if (waitNumPositiveResponses != 0) {
            synchronized (Application.getInstance().getRecvThread()) {
                try {
                    Application.getInstance().getRecvThread().wait();
                } catch (InterruptedException e) {
                    System.err.println("Thread: " + e);
                    return false;
                }
            }
        }

        // Caso utilizemos timeout na segunda layer de espera, vamos retornar falso aqui se algum processo morreu
        /*
        if (waitNumPositiveResponses != 0) return false;
        */

        // FIXME: DEBUG
        System.out.println("Resource: acesso liberado ao recurso " + resourceId);
    	
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

        // FIXME: DEBUG
        System.out.println("Estou largando o recurso " + resourceId);
    	
    	// Envia aviso aos peers adicionados a fila de espera
        for (ResourceQueueItem p = requestQueue.poll(); p != null; p = requestQueue.poll()) {
            ResourceAccessResponse response = new ResourceAccessResponse(user, p.getPeer(), resourceId, true);
            Application.getInstance().getConn().send(response);
        }
    	
    	return true;
    }

    /*------------------------------------------------------------------------*/

    public void receivedResponse(Peer p, boolean allow) {
        if (!peersWhoResponded.contains(p)) {
            // Peer ainda não tinha respondido
            peersWhoResponded.add(p);
            peersWhoAreYetToRespond.remove(p);
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
        // FIXME: DEBUG
        System.out.println("Resource: Peer " + p.getId() + ((allow)? " ALLOW" : " DENY") +". total left = " + waitNumResponses + "; positive left = " + waitNumPositiveResponses);

        if (waitNumPositiveResponses == 0) {
            synchronized (Application.getInstance().getRecvThread()) {
                Application.getInstance().getRecvThread().notify();
            }
        }
    }
}
