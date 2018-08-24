/*============================================================================*/
/* MulticastPeer.java                                                         */
/*                                                                            */
/* CLASSE QUE REPRESENTA UM PROCESSO PEER                                     */
/*============================================================================*/
/* Autor: Victor Barpp Gomes                                                  */
/*                                                                            */
/* 2018-08-23                                                                 */
/*============================================================================*/
// Esta classe implementa um processo peer. Este processo possui um socket
// multicast para se comunicar com o restante dos processos.
/*============================================================================*/

package peer;

import message.JoinMessage;

import java.net.*;
import java.security.*;
import java.util.Scanner;
import java.io.*;

/*============================================================================*/

public class MulticastPeer {

    // Variáveis do Multicast
    private InetAddress group;
    private MulticastSocket socket;
    private MulticastRecvThread recvThread;

    // Variáveis de autenticação
    private PrivateKey privateKey;
    private PublicKey publicKey;

    // Variáveis de input
    private Scanner scanner;

    // IP e porta do Socket Multicast
    private final static String MULTICAST_IP = "232.232.232.232";
	private final static short MULTICAST_PORT = 6789;

	// Tamanho da chave RSA em bits
    private final static int KEYSIZE_BITS = 512;

    /*------------------------------------------------------------------------*/

	public MulticastPeer() throws IOException, NoSuchAlgorithmException {
	    // Cria o socket Multicast
	    this.group = InetAddress.getByName(MULTICAST_IP);
	    this.socket = new MulticastSocket(MULTICAST_PORT);
	    this.socket.joinGroup(this.group);

	    // Cria a thread que recebe mensagens e as trata (não a inicia)
	    this.recvThread = new MulticastRecvThread(this.socket);

	    // Gera chaves RSA
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(KEYSIZE_BITS);

        KeyPair keyPair = keyGen.generateKeyPair();
        privateKey = keyPair.getPrivate();
        publicKey = keyPair.getPublic();

        // Cria scanner para ler input
        this.scanner = new Scanner(System.in);
    }

    /*------------------------------------------------------------------------*/

    public void run() throws IOException {
        // Inicia a thread
        recvThread.start();

        // FIXME: Debug para mostrar a chave quando inicia o programa
        System.out.println(publicKey.toString());

        // Envia a chave pública quando entra na rede
        // Todos os processos que já estão na rede vão responder.
        // Essas mensagens serão recebidas e processadas pela thread recvThread.
        JoinMessage joinMessage = new JoinMessage(publicKey);
        byte[] joinBytes = joinMessage.getBytes();
        socket.send(new DatagramPacket(
                joinBytes, joinBytes.length,
                group, MULTICAST_PORT
        ));

        // Loop principal: processa comandos de input
        // TODO: Fazer funções privadas para cada comando
        while (true) {
            String input = scanner.nextLine();
            if (input.startsWith("!q")) {
                recvThread.interrupt();
                break;
            }

            // FIXME: Remover o envio de texto puro
            byte[] msg = input.getBytes();
            DatagramPacket packet = new DatagramPacket(msg, msg.length,
                    group, MULTICAST_PORT);
            socket.send(packet);
        }

        close();
    }

    /*------------------------------------------------------------------------*/

    public void close() {
        if (scanner != null) scanner.close();
        if (socket != null) socket.close();
    }

}

/*============================================================================*/
