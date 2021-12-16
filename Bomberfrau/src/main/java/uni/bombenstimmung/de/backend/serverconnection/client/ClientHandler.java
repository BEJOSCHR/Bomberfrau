/*
 * ClientHandler
 *
 * Version 0.1.7
 * Author: Tim
 *
 * Arbeitet alle Events der Client-Verbindung zum Server (Host) ab.
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
		ConsoleHandler.print("Client with ID " + Integer.toString(client.getId()) + " connected with Server" + session.getRemoteAddress(), MessageType.BACKEND);
		session.write((String) "001-"+client.getId());
	}
	
	@Override
	public void sessionClosed(IoSession session) throws Exception {
		ConsoleHandler.print("Session of ID " + client.getId() + " closed", MessageType.BACKEND);	
		}
	
	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {
		SocketAddress remoteAddress = session.getRemoteAddress();
		ConsoleHandler.print("Client:" + client.getId() + " Message received from Server " + remoteAddress + ": " + message.toString(), MessageType.BACKEND);
	}
	
	@Override
	public void messageSent(IoSession session, Object message) throws Exception {
		//ConsoleHandler.print("Client: Message sent");
	}
}
