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

package app.peer;

import app.connection.Connection;
import app.message.JoinMessage;
import app.message.LeaveMessage;
import app.resource.Resource;

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
    
    // Duas belas lolis
    private Resource madoka;
    private Resource homura;

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
    
    public Resource getMadoka() {
    	return madoka;
    }
    
    public Resource getHomura() {
    	return homura;
    }

    public Connection getConn() {
        return conn;
    }

    public MulticastRecvThread getRecvThread() {
        return recvThread;
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
        
        // Inicializa os recursos
        madoka = new Resource(this, (short) 1);
        homura = new Resource(this, (short) 2);

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

        // Envia a chave pública quando entra na rede
        // Todos os processos que já estão na rede vão responder.
        // Essas mensagens serão recebidas e processadas pela thread recvThread.
        JoinMessage joinMessage = new JoinMessage(this);
        conn.send(joinMessage);

        // Loop principal: processa comandos de input
        while (true) {
            String input = scanner.nextLine();

            // Comandos
            // "!quit": finaliza o programa (enviando mensagem de saída à rede)
            // "!get num": tenta obter o recurso de ID num
            // "!release num": tenta obter o recurso de ID num
            if (input.startsWith("!quit")) {
                quit();
                break;
            }
            else if (input.startsWith("!get " + madoka.getResourceId())) {
                madoka.hold();
            }
            else if (input.startsWith("!get " + homura.getResourceId())) {
                homura.hold();
            }
            else if (input.startsWith("!release " + madoka.getResourceId())) {
                try {
                    madoka.release();
                }
                catch (GeneralSecurityException e) {
                    System.err.println("Security: " + e);
                }
            }
            else if (input.startsWith("!release " + homura.getResourceId())) {
                try {
                    homura.release();
                }
                catch (GeneralSecurityException e) {
                    System.err.println("Security: " + e);
                }
            }
        }

        close();
    }

    /*------------------------------------------------------------------------*/
    
    public Peer getPeerById(int id) {
    	for (Peer peer : onlinePeerList) {
            if (peer.peerId == id) {
                return peer;
            }
        }
    	return null;
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

    public int getOnlinePeerCount() {
        return onlinePeerList.size();
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

    public void copyOnlineListTo(ArrayList<Peer> list) {
        list.clear();
        list.addAll(onlinePeerList);
    }

    /*------------------------------------------------------------------------*/

    public void close() {
        if (scanner != null) scanner.close();
        if (conn != null) conn.close();
    }

    /*------------------------------------------------------------------------*/

    private void quit() throws IOException {
        try {
            // Libera todos os recursos que o processo está usando
            madoka.release();
            homura.release();

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
