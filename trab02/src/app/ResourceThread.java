/*============================================================================*/
/* Application.java                                                           */
/*                                                                            */
/* THREAD DE CONTROLE DE UM RECURSO                                           */
/*============================================================================*/
/* Autor: Rafael Hideo Toyomoto                                               */
/*                                                                            */
/* 2018-08-23                                                                 */
/*============================================================================*/
// Esta classe é uma thread que controla o acesso a um recurso
/*============================================================================*/

package app;

import app.agent.MulticastPeer;
import app.agent.OnlinePeers;
import app.agent.Peer;
import app.message.Message;
import app.message.MessageType;
import app.resource.Resource;
import app.resource.ResourceState;
import app.services.Commands;
import org.json.JSONObject;

import javax.crypto.Cipher;
import java.security.GeneralSecurityException;
import java.security.PublicKey;
import java.util.concurrent.SynchronousQueue;

public class ResourceThread extends Thread {

    protected SynchronousQueue<JSONObject> messages = new SynchronousQueue<>();

    private Resource resource;

    private MulticastPeer user;

    private OnlinePeers peers;

    private volatile boolean wanted;

    public ResourceThread(short id, MulticastPeer user, OnlinePeers peers) {
        this.user = user;
        this.peers = peers;
        this.wanted = false;

        // Criar o recurso
        this.resource = new Resource(id, user, peers);
    }

    @Override
    public void run() {
        // Executa essa thread enquando não for interrompida
        while (!Thread.currentThread().isInterrupted()) {

            // Solicitado o recurso pelo usuário? - Ficar Travado esperando
            while (!this.wanted) ;

            JSONObject jsonMsg = null;
            boolean waiting = true;

            // Processar pedido
            while (waiting && this.wanted) {

                // TODO: Colocar timer nesse loop - a cada X segundos checa as mensagens recebidas
                if (false) {

                    // TODO: Tratar se todos peers deram OK
                    if (false) {
                        // TODO: PEGUEI O RECURSO!
                        while(this.wanted) ;
                        // Quebra o laço de espera/uso do recurso
                        break;
                    }

                    // TODO: Tratar peers que nao responderam
                    shutdownPeers();

                    continue;
                }

                try {
                    // Espera mensagens de resposta
                    jsonMsg = messages.take();
                } catch (InterruptedException e) {
                    continue;
                }

                checkMessage(jsonMsg);
            }

            // TODO: voltar o recurso para disponivel
        }
    }

    public void hold() {
        wanted = true;
    }

    public void release() {
        wanted = false;
    }

    private boolean checkMessage(JSONObject jsonMsg) {

        // Checar se o tipo da mensagem é válido
        String messageType = jsonMsg.getString("type");
        if (!messageType.startsWith(MessageType.RESOURCE_ACCESS_RESPONSE.toString())) {
            return false;
        }

        /*====================================================================*/
        // Ler os campos da mensagem:

        //  - Sender ID
        int senderId = jsonMsg.getInt("sid");

        //  - Receiver ID
        int receiverId = jsonMsg.getInt("rid");

        //  - Resource ID
        short resourceId = (short) jsonMsg.getInt("resource");

        //  - Assinatura (Allow ou Deny)
        String authHexStr = jsonMsg.getString("auth");

        /*====================================================================*/
        // Sanity check

        // Ignora a mensagem se:
        //  - não tiver sido enviada para o próprio processo
        //  - você mesmo enviou
        if (receiverId != user.getId() || senderId == user.getId()) {
            return false;
        }

        // Checar se é um pedido do recurso dessa thread e se esse recurso está em espera
        if (resourceId != this.resource.getResourceId() || this.resource.getState() != ResourceState.WANTED) {
            return false;
        }


        // Pegar o Peer que enviou na lista de Peers Online
        Peer senderPeer = peers.getPeer(senderId);
        if (senderPeer == null) {
            return false;
        }

        /*====================================================================*/

        // Lê o campo "Auth", que deve conter a string "ALLOW" ou a string "DENY",
        // cifrada com a chave privada do peer.
        byte[] authBytes = Message.hexStringToBytes(authHexStr);

        // Decifra o campo com a chave pública do peer.
        String allowOrDeny = "";
        try {

            PublicKey peerPublicKey = peers.getPeer(senderId).getPublicKey();
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, peerPublicKey);
            allowOrDeny = new String(Message.bytesToHexString(cipher.doFinal(authBytes)));

        } catch (GeneralSecurityException e) {
            System.err.println("Security: " + e);
            return false;
        }

        // Verifica se o processo liberou o uso do recurso ou não.
        if (allowOrDeny.equals(Commands.getInstance().allowHash)) {

            // Decrementa o contador de respostas positivas faltantes
            // Marca que aquele processo respondeu à mensagem
            // Se esse incremento for o último que faltava, notifica a outra thread
            resource.receivedResponse(senderPeer, true);

        } else if (allowOrDeny.equals(Commands.getInstance().denyHash)) {

            // Mesmo que o processo esteja usando o recurso, marca que ele respondeu à mensagem.
            resource.receivedResponse(senderPeer, false);

        }
        return true;
    }

    private void shutdownPeers() {
        // TODO: tirar os peers que nao responderam da lista de onlines
    }
}
