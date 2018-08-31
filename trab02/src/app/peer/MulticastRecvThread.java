/*============================================================================*/
/* MulticastRecvThread.java                                                   */
/*                                                                            */
/* THREAD DE RECEBIMENTO PARA O SOCKET MULTICAST                              */
/*============================================================================*/
/* Autor: Victor Barpp Gomes                                                  */
/*                                                                            */
/* 2018-08-23                                                                 */
/*============================================================================*/
// Esta classe é uma thread que recebe dados do socket multicast em loop.
/*============================================================================*/

package app.peer;

import app.connection.Connection;
import app.message.JoinResponse;
import app.message.Message;
import app.resource.Resource;
import app.resource.ResourceState;

import org.json.JSONException;
import org.json.JSONObject;

import javax.crypto.Cipher;
import java.io.IOException;
import java.net.SocketException;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

/*============================================================================*/

public class MulticastRecvThread extends Thread {

    private MulticastPeer selfPeer;
    private Connection conn;

    /*------------------------------------------------------------------------*/

    public MulticastRecvThread(MulticastPeer selfPeer, Connection conn) {
        this.selfPeer = selfPeer;
        this.conn = conn;
    }

    /*------------------------------------------------------------------------*/

    @Override
    public void run() {
        // Executa essa thread enquando não for interrompida
        while (!Thread.currentThread().isInterrupted()) {
            try {
                // Recebe uma mensagem por multicast. Se der erro, interrompe a thread.
                byte[] msgBytes = conn.recv();

                // (Alternativa) Lançar uma thread aqui para tratar a mensagem recebida
                // Cria uma string a partir dos dados recebidos, e interpreta o JSON
                String msgString = new String(msgBytes);

                JSONObject jsonMsg = new JSONObject(msgString);
                String messageType = jsonMsg.getString("MessageType");

                // Switch usa equals() para strings
                switch (messageType) {
                    case "join":
                        processJoinMessage(jsonMsg);
                        break;
                    case "joinResponse":
                        processJoinResponse(jsonMsg);
                        break;
                    case "leave":
                        processLeaveMessage(jsonMsg);
                        break;
                    case "resourceAccess":
                        processResourceAccessMessage(jsonMsg);
                        break;
                    case "resourceAccessResponse":
                        processResourceAccessResponse(jsonMsg);
                        break;
                }
            }
            catch (JSONException e) {
                System.err.println("JSON: mensagem inválida recebida");
            }
            catch (SocketException e) {
                System.err.println("Socket: " + e.getMessage());
                break;
            }
            catch (IOException e) {
                System.err.println("IO: " + e.getMessage());
                break;
            }
        }
    }

    /*------------------------------------------------------------------------*/

    private void processJoinMessage(JSONObject jsonMsg) throws JSONException, IOException {
        int senderId = jsonMsg.getInt("Sender");

        // Ignora a mensagem se tiver sido enviada pelo próprio processo
        if (senderId == selfPeer.getPeerId()) {
            return;
        }

        // Ignora a mensagem se já houver um peer online com aquele ID
        if (selfPeer.isPeerOnline(senderId)) {
            return;
        }

        String keyString = jsonMsg.getString("PublicKey");
        byte[] keyBytes = Message.hexStringToBytes(keyString);
        PublicKey pk = null;

        // Recria a chave pública a partir dos bytes
        try {
            pk = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(keyBytes));
        }
        catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            System.err.println("Security: " + e.getMessage());
        }

        if (pk != null) {
            // Adiciona o par ID + PK à lista de peers online
            selfPeer.addOnlinePeer(new Peer(senderId, pk));

            // FIXME: Debug
            System.out.println("Novo peer online: ID " + senderId);
        }

        JoinResponse joinResponse = new JoinResponse(selfPeer, senderId);
        conn.send(joinResponse);
    }

    /*------------------------------------------------------------------------*/

    private void processJoinResponse(JSONObject jsonMsg) throws JSONException {
        int destPeerId = jsonMsg.getInt("Destinatary");

        // Ignora a mensagem se não tiver sido enviada para o próprio processo
        if (destPeerId != selfPeer.getPeerId()) {
            return;
        }

        int senderId = jsonMsg.getInt("Sender");

        // Ignora a mensagem se esse ID já estiver na lista de peers online
        if (selfPeer.isPeerOnline(senderId)) {
            return;
        }

        String keyString = jsonMsg.getString("PublicKey");
        byte[] keyBytes = Message.hexStringToBytes(keyString);
        PublicKey pk = null;

        // Recria a chave pública a partir dos bytes
        try {
            pk = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(keyBytes));
        }
        catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            System.err.println("Security: " + e.getMessage());
        }

        if (pk != null) {
            // Adiciona o par ID + PK à lista de peers online
            selfPeer.addOnlinePeer(new Peer(senderId, pk));

            // FIXME: Debug
            System.out.println("Peer já online: ID " + senderId);
        }
    }

    /*------------------------------------------------------------------------*/

    private void processLeaveMessage(JSONObject jsonMsg) throws JSONException {
        int senderId = jsonMsg.getInt("Sender");

        // Ignora a mensagem se esse ID não estiver na lista de peers online
        if (!selfPeer.isPeerOnline(senderId)) {
            return;
        }

        // Lê o campo "Auth", que deve conter a string "LEAVE" cifrada com a chave privada do peer.
        String authHexStr = jsonMsg.getString("Auth");
        byte[] authBytes = Message.hexStringToBytes(authHexStr);

        // Decifra o campo com a chave pública do peer.
        String leaveMsg = "";
        try {
            PublicKey peerPublicKey = selfPeer.getPeerPublicKey(senderId);
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, peerPublicKey);
            leaveMsg = new String(cipher.doFinal(authBytes));
        }
        catch (GeneralSecurityException e) {
            System.err.println("Security: " + e);
        }

        if (leaveMsg.equals("LEAVE")) {
            selfPeer.removeOnlinePeer(senderId);

            // FIXME: Debug
            System.out.println("Peer saiu: ID " + senderId);
        }
    }

    /*------------------------------------------------------------------------*/

    private void processResourceAccessMessage(JSONObject jsonMsg) throws JSONException, IOException {
        int senderId = jsonMsg.getInt("Sender");

        // Ignora a mensagem se tiver sido enviada pelo próprio processo
        if (senderId == selfPeer.getPeerId()) {
            return;
        }

        // Lê qual recurso o processo remetente quer, e qual seu timestamp
        short resourceId = (short) jsonMsg.getInt("Resource");
        long timestamp = jsonMsg.getBigInteger("Timestamp").longValue();

        // Se o estado do recurso para este processo for HELD,
        // ou se o estado for WANTED e seu próprio timestamp for menor que o timestamp do remetente:
        //     Coloca o pedido na fila, e responde com uma mensagem negativa.
        // Caso contrário:
        //     Responde com uma mensagem positiva.
        Resource resource = null;
        
        if (resourceId == 1) {
        	resource = selfPeer.getMadoka();
        } else if (resourceId == 2) {
        	resource = selfPeer.getHomura();
        }
        
        if (resource != null) {
            try {
                resource.accept(senderId, timestamp);
            }
            catch (GeneralSecurityException e) {
                System.err.println("Security: " + e);
            }
        }
    }

    /*------------------------------------------------------------------------*/

    private void processResourceAccessResponse(JSONObject jsonMsg) throws JSONException {
        // Ignora a mensagem se não tiver sido enviada para o próprio processo
        int destPeerId = jsonMsg.getInt("Destinatary");
        if (destPeerId != selfPeer.getPeerId()) {
            return;
        }

        // Lê qual processo respondeu
        int senderId = jsonMsg.getInt("Sender");
        short resourceId = (short) jsonMsg.getInt("Resource");

        Resource resource = null;
        if (resourceId == 1) {
            resource = selfPeer.getMadoka();
        }
        else if (resourceId == 2) {
            resource = selfPeer.getHomura();
        }
        else {
            return;
        }

        Peer senderPeer = selfPeer.getPeerById(senderId);
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
        String authHexStr = jsonMsg.getString("Auth");
        byte[] authBytes = Message.hexStringToBytes(authHexStr);

        // Decifra o campo com a chave pública do peer.
        String allowOrDeny = "";
        try {
            PublicKey peerPublicKey = selfPeer.getPeerPublicKey(senderId);
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, peerPublicKey);
            allowOrDeny = new String(cipher.doFinal(authBytes));
        }
        catch (GeneralSecurityException e) {
            System.err.println("Security: " + e);
        }

        // Verifica se o processo liberou o uso do recurso ou não.
        if (allowOrDeny.equals("ALLOW")) {
            // Decrementa o contador de respostas positivas faltantes
            // Marca que aquele processo respondeu à mensagem
            // Se esse incremento for o último que faltava, notifica a outra thread
            resource.receivedResponse(senderPeer, true);
        }
        else if (allowOrDeny.equals("DENY")) {
            // Mesmo que o processo esteja usando o recurso, marca que ele respondeu à mensagem.
            resource.receivedResponse(senderPeer, false);
        }
    }

}
