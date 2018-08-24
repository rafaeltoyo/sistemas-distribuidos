package connection;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

import message.Message;

public class Connection {
	
	private String host;
	private short port;
	private MulticastSocket socket;
	private InetAddress group;
	
	public Connection(String host, short port) throws IOException {
		this.host = host;
		this.port = port;
		this.socket = null;
		this.group = null;
		this.connect();
	}
	
	protected void connect() throws IOException {
		group = InetAddress.getByName(host);
		socket = new MulticastSocket(port);
		socket.joinGroup(group);
	}
	
	public void send(Message m) throws IOException {
		synchronized (socket) {
			socket.send(new DatagramPacket(m.getBytes(), m.getBytes().length, group, port));
		}
	}
	
	public byte[] read() throws IOException {
		byte[] buffer = new byte[4096];
		DatagramPacket messageIn = new DatagramPacket(buffer, buffer.length);
		synchronized (socket) {
			socket.receive(messageIn);
		}
		return buffer;
	}
}
