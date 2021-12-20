/*
 * ServerHandler
 *
 * Version 0.1.7
 * Author: Tim
 *
 * Arbeitet alle Events der Server-Verbindung zu allen Clients (ConnectedClients).
 */
package uni.bombenstimmung.de.backend.serverconnection.host;

import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

import uni.bombenstimmung.de.backend.console.ConsoleHandler;
import uni.bombenstimmung.de.backend.console.MessageType;

public class ServerHandler extends IoHandlerAdapter {

	private ConnectedClient server;
	
	public ServerHandler(ConnectedClient server) {
		this.server = server;
	}
	
	
	public void excepetionCaught(IoSession session, Throwable cause) throws Exception {
		cause.printStackTrace();
		session.closeNow();
	}
	
	//When a session is created, the client will be added to the Hash Map of the server object
	@Override
	public void sessionCreated(IoSession session) throws Exception {
		ConsoleHandler.print("Server session created with Client " + session.getRemoteAddress(), MessageType.BACKEND);
		//server.addClientToList(session.getRemoteAddress());
		//server.printConnectedClients();
		//ConnectedClients.add(new ConnectedClient (false));
		//server.sendMessageToAllClients("Hallo");
	}
	
	//When a message from the Client is received, the Server will check it for an id and process the message as needed.
	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {
		
		String[] parts = message.toString().split("-");
		server.receivedMessage(Integer.parseInt(parts[0]), message.toString(), session);
		
		//SocketAddress remoteAddress = session.getRemoteAddress();
		//server.printMessage("Server: Message received from Client " + remoteAddress + ": " + message.toString()+ "\n");
		//server.printMessage("Server: Message received from Client " + remoteAddress + ": " + ((ConnectedClient) message).getId());
		//session.write("Answer back to Client " + remoteAddress);
		//server.sendMessageToAllClients((String) message);
	}
	
	//When a session is closed, the client will be removed from the Hash Map to remain consistency and integrity
	@Override
	public void sessionClosed(IoSession session) throws Exception {
		//ConsoleHandler.print("Client in ConnectedClients list: " + Arrays.deepToString(ConnectedClients.toArray()));
		ConsoleHandler.print("Session closed .... ", MessageType.BACKEND);
		server.removeClient(session);
	}
	
}


