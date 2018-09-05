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

package app;

import app.agent.MulticastPeer;
import app.agent.OnlinePeers;
import app.agent.Peer;
import app.services.Commands;
import app.services.Connection;
import app.message.JoinResponse;
import app.message.Message;
import app.resource.Resource;
import app.resource.ResourceState;

import org.json.JSONException;
import org.json.JSONObject;

import javax.crypto.Cipher;
import java.io.IOException;
import java.io.Serializable;
import java.net.SocketException;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.function.Consumer;

/*============================================================================*/

public class MulticastRecvThread extends Thread {

    private Connection conn;
    private MulticastPeer user;
    private OnlinePeers peers;
    private Consumer<JSONObject> onReceiveCallback;

    /*------------------------------------------------------------------------*/

    public MulticastRecvThread(Connection conn, MulticastPeer user, OnlinePeers peers, Consumer<JSONObject> onReceiveCallback) {
        this.conn = conn;
        this.user = user;
        this.peers = peers;
        this.onReceiveCallback = onReceiveCallback;
    }

    /*------------------------------------------------------------------------*/

    @Override
    public void run() {

        // Executa essa thread enquando não for interrompida
        while (!Thread.currentThread().isInterrupted()) {

            // Recebe uma mensagem por multicast. Se der erro, interrompe a thread.
            byte[] msgBytes = new byte[0];

            try {
                msgBytes = conn.recv();
            } catch (IOException e) {
                System.err.println("IO: " + e.getMessage());
                break;
            }

            // (Alternativa) Lançar uma thread aqui para tratar a mensagem recebida
            // Cria uma string a partir dos dados recebidos, e interpreta o JSON
            String msgString = new String(msgBytes);

            JSONObject jsonMsg = new JSONObject(msgString);

            onReceiveCallback.accept(jsonMsg);

        }
    }
}
