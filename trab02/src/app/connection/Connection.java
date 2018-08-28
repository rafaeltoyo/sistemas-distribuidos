/*============================================================================*/
/* Connection.java                                                            */
/*                                                                            */
/* CLASSE QUE IMPLEMENTA AS FUNÇÕES DE CONEXÃO, ENVIO E RECEBIMENTO POR MEIO  */
/* DE UM SOCKET MULTICAST                                                     */
/*============================================================================*/
/* Autores: Rafael Hideo Toyomoto e Victor Barpp Gomes                        */
/*                                                                            */
/* 2018-08-24                                                                 */
/*============================================================================*/
// Esta classe possui internamente um MulticastSocket. Ao ser instanciada,
// automaticamente instancia e ativa esse socket. Possui métodos para enviar
// e receber dados, e possui ReentrantLocks para evitar condições de disputa
// entre threads usando o mesmo socket.
/*============================================================================*/

package app.connection;

import app.message.Message;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.concurrent.locks.ReentrantLock;

/*============================================================================*/

/**
 * Esta classe é um "wrapper" para o socket multicast, mantendo referência
 * interna aos objetos relacionados. Possui métodos para enviar e receber dados,
 * fazendo uso de ReentrantLocks para previnir condições de disputa.
 *
 * @since 2018-08-24
 */
public class Connection {

    private String host;

    private short port;
    private MulticastSocket socket;
    private InetAddress group;

    private ReentrantLock sendLock;
    private ReentrantLock recvLock;

    /*------------------------------------------------------------------------*/

    /**
     * Construtor. Recebe um host multicast ("224.0.0.0" a "239.255.255.255")
     * e um número de porta. Invoca o método connect, que instancia o socket.
     * Cria também as ReentrantLocks para implementar controle de concorrência
     * entre threads.
     *
     * @param host IP na faixa multicast
     * @param port número de porta
     * @throws IOException se ocorrer um erro na instanciação do socket.
     */
    public Connection(String host, short port) throws IOException {
        this.host = host;
        this.port = port;
        this.socket = null;
        this.group = null;
        this.connect();
        this.sendLock = new ReentrantLock();
        this.recvLock = new ReentrantLock();
    }

    /*------------------------------------------------------------------------*/

    /**
     * Instancia o socket multicast.
     *
     * @throws IOException se o host não estiver na faixa multicast, ou se
     * ocorrer qualquer exceção de I/O durante a criação do socket.
     */
    protected void connect() throws IOException {
        // Cria o socket multicast
        group = InetAddress.getByName(host);
        socket = new MulticastSocket(port);
        socket.joinGroup(group);
    }

    /*------------------------------------------------------------------------*/

    /**
     * Envia uma mensagem pelo socket, com controle de concorrência.
     *
     * @param m mensagem a enviar
     * @throws IOException se ocorrer exceção de I/O ao enviar o datagrama
     */
    public void send(Message m) throws IOException {
        // Sincroniza o socket para evitar condições de disputa entre as threads
        sendLock.lock();
        try {
            socket.send(new DatagramPacket(m.getBytes(), m.getBytes().length, group, port));
        }
        finally {
            sendLock.unlock();
        }
    }

    /*------------------------------------------------------------------------*/

    public byte[] recv() throws IOException {
        byte[] buffer = new byte[4096];
        DatagramPacket messageIn = new DatagramPacket(buffer, buffer.length);

        recvLock.lock();
        try {
            socket.receive(messageIn);
        }
        finally {
            recvLock.unlock();
        }

        return buffer;
    }

    /*------------------------------------------------------------------------*/

    public void close() {
        if (socket != null) socket.close();
    }
}

/*============================================================================*/
