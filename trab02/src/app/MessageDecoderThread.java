package app;

import app.agent.MulticastPeer;
import app.agent.OnlinePeers;
import app.agent.Peer;
import app.message.JoinResponse;
import app.message.Message;
import app.message.MessageType;
import app.resource.Resource;
import app.resource.ResourceState;
import app.services.Commands;
import app.services.Connection;
import org.json.JSONException;
import org.json.JSONObject;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.net.SocketException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.concurrent.SynchronousQueue;

public class MessageDecoderThread extends Thread {

    protected SynchronousQueue<JSONObject> messages = new SynchronousQueue<>();

    private Connection conn;
    private MulticastPeer user;
    private OnlinePeers peers;

    /*------------------------------------------------------------------------*/

    public MessageDecoderThread(Connection conn, MulticastPeer user, OnlinePeers peers) {
        this.conn = conn;
        this.user = user;
        this.peers = peers;
    }

    @Override
    public void run() {

        // Executa essa thread enquando não for interrompida
        while (!Thread.currentThread().isInterrupted()) {
            JSONObject jsonMsg = null;
            try {
                jsonMsg = messages.take();
            } catch (InterruptedException e) {
                continue;
            } catch (Exception e) {
            	continue;
            }

            // Pegar o tipo da mensagem
            String messageType = jsonMsg.getString("type");

            // Destina a tratativa adequada para cada um dos tipos de mensagem
            try {
                if (messageType.startsWith(MessageType.JOIN_REQUEST.toString())) {
                    processJoinMessage(jsonMsg);
                }
                else if (messageType.startsWith(MessageType.JOIN_RESPONSE.toString())) {
                    processJoinResponse(jsonMsg);
                }
                else if (messageType.startsWith(MessageType.LEAVE.toString())) {
                    processLeaveMessage(jsonMsg);
                }
                else if (messageType.startsWith(MessageType.RESOURCE_ACCESS_REQUEST.toString())) {
                    processResourceAccessMessage(jsonMsg);
                }
                else if (messageType.startsWith(MessageType.RESOURCE_ACCESS_RESPONSE.toString())) {
                    processResourceAccessResponse(jsonMsg);
                }
            } catch (GeneralSecurityException e) {
                System.err.println("Security: " + e);
            } catch (JSONException e) {
                System.err.println("JSON: mensagem inválida recebida");
            } catch (SocketException e) {
                System.err.println("Socket: " + e.getMessage());
                break;
            } catch (IOException e) {
                System.err.println("IO: " + e.getMessage());
                break;
            }
        }

    }

    /*------------------------------------------------------------------------*/

    private void processJoinMessage(JSONObject jsonMsg) throws IOException, IllegalBlockSizeException, BadPaddingException, NoSuchPaddingException, InvalidKeyException {
        // Pegar o ID do peer que enviou essa mensagem
        int senderId = jsonMsg.getInt("sid");

        // Ignorar a mensagem se tiver sido enviada pelo próprio usuário
        if (senderId == user.getId()) {
            return;
        }

        // Ignora a mensagem se já houver um peer online com aquele ID
        if (peers.isOnline(senderId)) {
            return;
        }

        String publicKey = jsonMsg.getString("publickey");
        byte[] keyBytes = Message.hexStringToBytes(publicKey);

        // Recria a chave pública a partir dos bytes
        try {
            PublicKey pk = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(keyBytes));
            Peer sender = new Peer(senderId, pk);

            peers.addPeer(sender);

            // FIXME: Debug
            System.out.println("Novo peer online: ID " + senderId);

            JoinResponse joinResponse = new JoinResponse(user, sender);
            conn.send(joinResponse);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            System.err.println("Security: " + e.getMessage());
        }
    }

    private void processJoinResponse(JSONObject jsonMsg) {
        int destPeerId = jsonMsg.getInt("rid");

        // Ignora a mensagem se não tiver sido enviada para o próprio processo
        if (destPeerId != user.getId()) {
            return;
        }

        int senderId = jsonMsg.getInt("sid");

        // Ignora a mensagem se esse ID já estiver na lista de peers online
        if (peers.isOnline(senderId)) {
            return;
        }

        String keyString = jsonMsg.getString("publickey");
        byte[] keyBytes = Message.hexStringToBytes(keyString);
        PublicKey pk = null;

        // Recria a chave pública a partir dos bytes
        try {
            pk = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(keyBytes));
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            System.err.println("Security: " + e.getMessage());
        }

        if (pk != null) {
            // Adiciona o par ID + PK à lista de peers online
            peers.addPeer(new Peer(senderId, pk));

            // FIXME: Debug
            System.out.println("Peer já online: ID " + senderId);
        }
    }

    private void processLeaveMessage(JSONObject jsonMsg) throws GeneralSecurityException {
        int senderId = jsonMsg.getInt("sid");

        // Ignora a mensagem se esse ID não estiver na lista de peers online
        if (!peers.isOnline(senderId)) {
            return;
        }

        // Lê o campo "Auth", que deve conter a string "LEAVE" cifrada com a chave privada do peer.
        String authHexStr = jsonMsg.getString("auth");
        byte[] authBytes = Message.hexStringToBytes(authHexStr);

        // Decifra o campo com a chave pública do peer.
        String leaveMsg = "";

        PublicKey peerPublicKey = peers.getPeer(senderId).getPublicKey();
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, peerPublicKey);
        leaveMsg = new String(Message.bytesToHexString(cipher.doFinal(authBytes)));

        if (leaveMsg.equals(Commands.getInstance().leaveHash)) {
            peers.removePeer(senderId);

            // FIXME: Debug
            System.out.println("Peer saiu: ID " + senderId);
        }
    }

    private void processResourceAccessMessage(JSONObject jsonMsg) throws IOException, GeneralSecurityException {
        int senderId = jsonMsg.getInt("sid");

        // Ignora a mensagem se tiver sido enviada pelo próprio processo
        if (senderId == user.getId()) {
            return;
        }

        // Lê qual recurso o processo remetente quer, e qual seu timestamp
        short resourceId = (short) jsonMsg.getInt("resource");
        long timestamp = jsonMsg.getBigInteger("timestamp").longValue();

        // Se o estado do recurso para este processo for HELD,
        // ou se o estado for WANTED e seu próprio timestamp for menor que o timestamp do remetente:
        //     Coloca o pedido na fila, e responde com uma mensagem negativa.
        // Caso contrário:
        //     Responde com uma mensagem positiva.
        Resource resource = null;

        if (resourceId == 1) {
            resource = Application.getInstance().getMadoka();
        } else if (resourceId == 2) {
            resource = Application.getInstance().getHomura();
        }

        if (resource != null) {
            resource.accept(senderId, timestamp);
        }
    }

    private void processResourceAccessResponse(JSONObject jsonMsg) {
        // Ignora a mensagem se não tiver sido enviada para o próprio processo
        int destPeerId = jsonMsg.getInt("rid");
        if (destPeerId != user.getId()) {
            return;
        }

        // Lê qual processo respondeu
        int senderId = jsonMsg.getInt("sid");
        short resourceId = (short) jsonMsg.getInt("resource");

        Resource resource = null;
        if (resourceId == 1) {
            resource = Application.getInstance().getMadoka();
        } else if (resourceId == 2) {
            resource = Application.getInstance().getHomura();
        } else {
            return;
        }

        Peer senderPeer = peers.getPeer(senderId);
        if (senderPeer == null) {
            return;
        }

        // Sanity check
        // Verifica se este processo está realmente no estado WANTED para o recurso.
        if (resource.getState() != ResourceState.WANTED) {
            return;
        }

        // Lê o campo "Auth", que deve conter a string "ALLOW" ou a string "DENY",
        // cifrada com a chave privada do peer.
        String authHexStr = jsonMsg.getString("auth");
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
    }

    /*------------------------------------------------------------------------*/
}
