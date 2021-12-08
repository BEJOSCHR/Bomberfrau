/*
 * ClientHandler
 *
 * Version 1.0
 * Author: Benni
 *
 * Arbeitet alle Events der Client-Verbindung zum Server (Host) ab (senden, empfangen...)
 */
package uni.bombenstimmung.de.backend.serverconnection.client;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

import uni.bombenstimmung.de.backend.console.ConsoleHandler;
import uni.bombenstimmung.de.backend.serverconnection.host.ConnectedClient;

public class ClientHandler extends IoHandlerAdapter{

	private ConnectedClient client;
	
	public ClientHandler(ConnectedClient client) {
		this.client = client;
	}
	
	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {
		
		ConsoleHandler.print("Message received: " +message.toString());
	}
	
	@Override
	public void messageSent(IoSession session, Object message) throws Exception {
		ConsoleHandler.print("Message sent");
	}
}
