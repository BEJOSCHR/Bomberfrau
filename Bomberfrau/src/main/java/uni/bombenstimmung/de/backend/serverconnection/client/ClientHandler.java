/*
 * ClientHandler
 *
 * Version 1.0
 * Author: Benni
 *
 * Arbeitet alle Events der Client-Verbindung zum Server (Host) ab (senden, empfangen...)
 */
package uni.bombenstimmung.de.backend.serverconnection.client;

import java.net.SocketAddress;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

import uni.bombenstimmung.de.backend.console.ConsoleHandler;
import uni.bombenstimmung.de.backend.console.MessageType;
import uni.bombenstimmung.de.backend.serverconnection.host.ConnectedClient;

public class ClientHandler extends IoHandlerAdapter{

	private ConnectedClient client;
	
	public ClientHandler(ConnectedClient client) {
		this.client = client;
	}
	
	
	@Override
	public void sessionOpened(IoSession session) throws Exception {
		ConsoleHandler.print("Client with ID " + Integer.toString(client.getId()) + " connected with Server" + session.getRemoteAddress());
		//session.write("List"+"-"+client.getId()+"-"+client.isHost());
		//IoBuffer buffer = IoBuffer.allocate(20);
		session.write(client.getId());
	}
	
	
	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {
		SocketAddress remoteAddress = session.getRemoteAddress();
		ConsoleHandler.print("Client: Message received from Server " + remoteAddress + ": " + message.toString());
	}
	
	@Override
	public void messageSent(IoSession session, Object message) throws Exception {
		//ConsoleHandler.print("Client: Message sent");
	}
}
