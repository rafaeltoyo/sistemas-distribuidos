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

package peer;

import connection.Connection;
import message.JoinResponse;
import message.Message;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.SocketException;
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
            // Recebe uma mensagem por multicast. Se der erro, interrompe a thread.
            byte[] msgBytes;
            try {
                msgBytes = conn.recv();
            }
            catch (SocketException e) {
                System.err.println("Socket: " + e.getMessage());
                break;
            }
            catch (IOException e) {
                System.err.println("IO: " + e.getMessage());
                break;
            }

            // (Alternativa) Lançamento de uma thread para tratar a mensagem.

            // Cria uma string a partir dos dados recebidos, e interpreta o JSON
            String msgString = new String(msgBytes);
            try {
                JSONObject jsonMsg = new JSONObject(msgString);
                String messageType = jsonMsg.getString("MessageType");

                if (messageType.equals("join")) {
                    processJoinMessage(jsonMsg);
                }
                else if (messageType.equals("joinResponse")) {
                    processJoinResponse(jsonMsg);
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
        // Ignora a mensagem se tiver sido enviada pelo próprio processo
        int senderId = jsonMsg.getInt("Sender");
        if (senderId == selfPeer.getPeerId()) {
            return;
        }
        // TODO: Verificar se já há um processo online com esse ID

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
            // TODO: Adiciona o par ID + PK à lista de peers online
            System.out.println("Novo peer online: ID " + senderId);
        }

        JoinResponse joinResponse = new JoinResponse(selfPeer, senderId);
        conn.send(joinResponse);
    }

    /*------------------------------------------------------------------------*/

    private void processJoinResponse(JSONObject jsonMsg) throws JSONException {
        // Ignora a mensagem se não tiver sido enviada para o próprio processo
        int destPeerId = jsonMsg.getInt("DestinatedTo");
        if (destPeerId != selfPeer.getPeerId()) {
            return;
        }

        int senderId = jsonMsg.getInt("Sender");
        // TODO: Verificar se já há um processo online com esse ID

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
            // TODO: Adiciona o par ID + PK à lista de peers online
            System.out.println("Peer já online: ID " + senderId);
        }
    }

}
