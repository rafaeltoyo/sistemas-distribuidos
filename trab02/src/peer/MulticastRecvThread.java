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
import message.Message;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.net.SocketException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

/*============================================================================*/

public class MulticastRecvThread extends Thread {

    private Connection conn;

    /*------------------------------------------------------------------------*/

    public MulticastRecvThread(Connection conn) {
        this.conn = conn;
    }

    /*------------------------------------------------------------------------*/

    @Override
    public void run() {
        try {
            // Executa essa thread enquanto não for interrompida
            while (!Thread.currentThread().isInterrupted()) {
                // Recebe mensagem pelo socket Multicast
                byte[] msgBytes = conn.recv();

                String messageStr = new String(msgBytes);

                // TODO: Lançamento de uma thread para tratar a mensagem.

                try {
                    JSONObject jsonMsg = new JSONObject(messageStr);
                    int statusCode = jsonMsg.getInt("StatusCode");

                    if (statusCode == 100) {
                        processJoinMessage(jsonMsg);
                    }
                }
                catch (JSONException e) {
                    System.err.println("JSON: mensagem inválida recebida");
                }
            }
        }
        catch (SocketException e) {
            System.err.println("Socket: " + e.getMessage());
        }
        catch (IOException e) {
            System.err.println("IO: " + e.getMessage());
        }
    }

    /*------------------------------------------------------------------------*/

    private void processJoinMessage(JSONObject jsonMsg) throws JSONException {
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

        if (pk != null) System.out.println(pk.toString());
    }

}
