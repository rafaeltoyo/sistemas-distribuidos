/*============================================================================*/
/* Application.java                                                   */
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
import app.message.JoinMessage;
import app.message.LeaveMessage;
import app.message.Message;
import app.message.MessageType;
import app.resource.Resource;
import app.services.Commands;
import app.services.Connection;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

public class Application {

    private static Application ourInstance = new Application();

    public static Application getInstance() {
        return ourInstance;
    }

    /*------------------------------------------------------------------------*/

    // IP e porta do Socket Multicast
    private final static String MULTICAST_IP = "232.232.232.232";
    private final static short MULTICAST_PORT = 6789;

    /*------------------------------------------------------------------------*/

    // Conexão
    private Connection conn;

    // Variáveis de input
    private Scanner scanner;

    /*------------------------------------------------------------------------*/

    // Representação do usuário como um Peer
    private MulticastPeer user;

    // Representação dos demais Peers
    private OnlinePeers peers;

    // Duas belas lolis
    private Resource madoka;
    private Resource homura;

    /*------------------------------------------------------------------------*/

    private MessageDecoderThread msgThread;

    // Thread de recebimento de dados
    private MulticastRecvThread recvThread;

    /*------------------------------------------------------------------------*/

    private Application() {
    }

    public void start() throws IOException, NoSuchAlgorithmException {

        Commands.getInstance().init();

        // Cria o objeto de conexão multicast
        conn = new Connection(MULTICAST_IP, MULTICAST_PORT);

        // Cria scanner para ler input
        scanner = new Scanner(System.in);

        // Criar representação do user
        user = new MulticastPeer(0);

        // Inicializa a lista de peers online
        peers = new OnlinePeers();

        // Inicializa os recursos
        madoka = new Resource((short) 1, user, peers);
        homura = new Resource((short) 2, user, peers);

        msgThread = new MessageDecoderThread(conn, user, peers);

        // Cria a thread que recebe mensagens e as trata (não a inicia)
        recvThread = new MulticastRecvThread(conn, user, peers, data -> {
            try {
                msgThread.messages.put(data);
            } catch (InterruptedException e) {
                System.err.println("Consumer Error: " + e.getMessage());
            }
        });
    }

    public void run() throws IOException, IllegalBlockSizeException, NoSuchPaddingException, BadPaddingException, NoSuchAlgorithmException, InvalidKeyException {

        // FIXME: Como gerar IDs únicos para cada processo?
        System.out.print("Digite um ID para o processo: ");
        int peerId = scanner.nextInt();
        user.setId(peerId);

        // Inicia a thread
        msgThread.start();
        recvThread.start();

        // Envia a chave pública quando entra na rede
        // Todos os processos que já estão na rede vão responder.
        // Essas mensagens serão recebidas e processadas pela thread recvThread.
        Message joinMessage = new Message(MessageType.JOIN_REQUEST.toString(), user);
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

            LeaveMessage leaveMessage = new LeaveMessage(user);
            conn.send(leaveMessage);
        }
        catch (GeneralSecurityException e) {
            System.err.println("Security: " + e);
        }

        recvThread.interrupt();
    }

    /*------------------------------------------------------------------------*/

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
}
