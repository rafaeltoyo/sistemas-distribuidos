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

import message.Message;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.DatagramPacket;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;

/*============================================================================*/

public class MulticastRecvThread extends Thread {

	private MulticastSocket socket;

    /*------------------------------------------------------------------------*/
	
	public MulticastRecvThread(MulticastSocket ms) {
		this.socket = ms;
	}

    /*------------------------------------------------------------------------*/

	@Override
	public void run() {
		byte[] buffer = new byte[4096];
		
		try {
		    // Executa essa thread enquanto não for interrompida
			while (!Thread.currentThread().isInterrupted()) {
			    // Recebe mensagem pelo socket Multicast
				DatagramPacket messageIn = new DatagramPacket(buffer,
                        buffer.length);
				socket.receive(messageIn);

                String messageStr = new String(messageIn.getData());

				// TODO: Lançamento de uma thread para tratar a mensagem.

                BufferedReader reader = new BufferedReader(new StringReader(messageStr));

                // Lê a primeira linha (status code)
                String statusCodeLine = reader.readLine();

                if (statusCodeLine.equals("StatusCode: 100")) {
                    // Lê a segunda linha (deve ser a chave pública)
                    String keyString = reader.readLine();
                    if (!keyString.startsWith("PublicKey: ")) {
                        continue;
                    }

                    // Retira a label e gera vetor de bytes a partir do valor da chave
                    keyString = keyString.replace("PublicKey: ", "");
                    byte[] keyBytes = Message.hexStringToBytes(keyString);
                    PublicKey pk = null;

                    // Recria a chave pública a partir dos bytes
                    try {
                        pk = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(keyBytes));
                    }
                    catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
                        System.err.println("Security: " + e.getMessage());
                        continue;
                    }

                    if (pk != null) System.out.println(pk.toString());
                }

				Arrays.fill(buffer, (byte) 0);
			}
		}
		catch (SocketException e) {
			System.err.println("Socket: " + e.getMessage());
		}
		catch (IOException e) {
			System.err.println("IO: " + e.getMessage());
		}
	}
}
