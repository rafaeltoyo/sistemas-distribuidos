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

import connection.Connection;
import message.JoinMessage;
import message.LeaveMessage;

import java.io.IOException;
import java.security.*;
import java.util.ArrayList;
import java.util.Scanner;

/*============================================================================*/

public class MulticastPeer {

    private int peerId;

    // Variáveis do Multicast
    private Connection conn;

    // Thread que recebe mensagem dos outros processos
    private MulticastRecvThread recvThread;

    // Variáveis de autenticação
    private PrivateKey privateKey;
    private PublicKey publicKey;

    private ArrayList<Peer> onlinePeerList;

    // Variáveis de input
    private Scanner scanner;

    // IP e porta do Socket Multicast
    private final static String MULTICAST_IP = "232.232.232.232";
    private final static short MULTICAST_PORT = 6789;

    // Tamanho da chave RSA em bits
    private final static int KEYSIZE_BITS = 512;

    /*------------------------------------------------------------------------*/

    public int getPeerId() {
        return peerId;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    /*------------------------------------------------------------------------*/

    public MulticastPeer() throws IOException, NoSuchAlgorithmException {
        // Cria o objeto de conexão multicast
        this.conn = new Connection(MULTICAST_IP, MULTICAST_PORT);

        // Cria a thread que recebe mensagens e as trata (não a inicia)
        this.recvThread = new MulticastRecvThread(this, conn);

        // Gera chaves RSA
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(KEYSIZE_BITS);

        KeyPair keyPair = keyGen.generateKeyPair();
        privateKey = keyPair.getPrivate();
        publicKey = keyPair.getPublic();

        // Inicializa a lista de peers online
        onlinePeerList = new ArrayList<>();

        // Cria scanner para ler input
        this.scanner = new Scanner(System.in);
    }

    /*------------------------------------------------------------------------*/

    public void run() throws IOException {
        // FIXME: Como gerar IDs únicos para cada processo?
        System.out.print("Digite um ID para o processo: ");
        peerId = scanner.nextInt();

        // Inicia a thread
        recvThread.start();

        // FIXME: Debug para mostrar a chave quando inicia o programa
        System.out.println(publicKey.toString());

        // Envia a chave pública quando entra na rede
        // Todos os processos que já estão na rede vão responder.
        // Essas mensagens serão recebidas e processadas pela thread recvThread.
        JoinMessage joinMessage = new JoinMessage(this);
        conn.send(joinMessage);

        // Loop principal: processa comandos de input
        while (true) {
            String input = scanner.nextLine();
            if (input.startsWith("!q")) {
                quit();
                break;
            }

            // TODO: Adicionar comandos para acessar os recursos
        }

        close();
    }

    /*------------------------------------------------------------------------*/

    public boolean isPeerOnline(int id) {
        for (Peer peer : onlinePeerList) {
            if (peer.peerId == id) {
                return true;
            }
        }
        return false;
    }

    /*------------------------------------------------------------------------*/

    public PublicKey getPeerPublicKey(int id) {
        for (Peer peer : onlinePeerList) {
            if (peer.peerId == id) {
                return peer.publicKey;
            }
        }
        return null;
    }

    /*------------------------------------------------------------------------*/

    public void addOnlinePeer(Peer peer) {
        onlinePeerList.add(peer);
    }

    /*------------------------------------------------------------------------*/

    public void removeOnlinePeer(int id) {
        for (Peer peer : onlinePeerList) {
            if (peer.peerId == id) {
                onlinePeerList.remove(peer);
                break;
            }
        }
    }

    /*------------------------------------------------------------------------*/

    public void close() {
        if (scanner != null) scanner.close();
        if (conn != null) conn.close();
    }

    /*------------------------------------------------------------------------*/

    private void quit() throws IOException {
        try {
            LeaveMessage leaveMessage = new LeaveMessage(this);
            conn.send(leaveMessage);
        }
        catch (GeneralSecurityException e) {
            System.err.println("Security: " + e);
        }

        recvThread.interrupt();
    }

}

/*============================================================================*/
