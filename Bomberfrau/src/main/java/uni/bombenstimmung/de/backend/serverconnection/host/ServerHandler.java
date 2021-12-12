/*
 * ServerHandler
 *
 * Version 1.0
 * Author: Benni
 *
 * Arbeitet alle Events der Server-Verbindung zu allen Clients (ConnectedClients) ab (senden, empfangen, verwalten...)
 */
package uni.bombenstimmung.de.backend.serverconnection.host;

import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

import uni.bombenstimmung.de.backend.console.ConsoleHandler;

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
		ConsoleHandler.print("Server session created with Client " + session.getRemoteAddress());
		server.addClientToList(session.getRemoteAddress());
		server.printConnectedClients();
		//ConnectedClients.add(new ConnectedClient (false));
	}
	
	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {
		
		SocketAddress remoteAddress = session.getRemoteAddress();
		//ConsoleHandler.print(Integer.toString(client.getId()));
		
		server.printMessage("Server: Message received from Client " + remoteAddress + ": " + message.toString()+ "\n");
		//server.printMessage("Server: Message received from Client " + remoteAddress + ": " + ((ConnectedClient) message).getId());
		session.write("Answer back to Client " + remoteAddress); 
	}
	
	//When a session is closed, the client will be removed from the Hash Map to remain consistency and integrity
	@Override
	public void sessionClosed(IoSession session) throws Exception {
		//ConsoleHandler.print("Client in ConnectedClients list: " + Arrays.deepToString(ConnectedClients.toArray()));
		System.out.println("Session closed .... ");
		server.removeClient(session.getRemoteAddress());
		server.printConnectedClients();
		//session.closeNow();
		//ConnectedClients.remove(ConnectedClients.size());
		
	}
	
}


