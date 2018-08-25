package connection;

import message.Message;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.concurrent.locks.ReentrantLock;

public class Connection {
	
	private String host;
	private short port;
	private MulticastSocket socket;
	private InetAddress group;

	private ReentrantLock sendLock;
    private ReentrantLock recvLock;
	
	public Connection(String host, short port) throws IOException {
		this.host = host;
		this.port = port;
		this.socket = null;
		this.group = null;
		this.connect();
		this.sendLock = new ReentrantLock();
		this.recvLock = new ReentrantLock();
	}
	
	protected void connect() throws IOException {
	    // Cria o socket multicast
		group = InetAddress.getByName(host);
		socket = new MulticastSocket(port);
		socket.joinGroup(group);
	}
	
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

	public void close() {
        if (socket != null) socket.close();
    }
}
