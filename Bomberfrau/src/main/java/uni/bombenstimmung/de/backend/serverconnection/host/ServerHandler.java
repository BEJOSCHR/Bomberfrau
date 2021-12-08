/*
 * ServerHandler
 *
 * Version 1.0
 * Author: Benni
 *
 * Arbeitet alle Events der Server-Verbindung zu allen Clients (ConnectedClients) ab (senden, empfangen, verwalten...)
 */
package uni.bombenstimmung.de.backend.serverconnection.host;

import java.io.IOException;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.transport.socket.nio.NioDatagramAcceptor;

public class ServerHandler extends IoHandlerAdapter {


	private List<ConnectedClient> ConnectedClients = new ArrayList();
	
	private ConnectedClient server;
	
	public ServerHandler(ConnectedClient server) {
		this.server = server;
	}
	
	
	public void excepetionCaught(IoSession session, Throwable cause) throws Exception {
		cause.printStackTrace();
		session.closeNow();
	}
	
	@Override
	public void sessionCreated(IoSession session) throws Exception {
		System.out.println("Server session created");
		//ConnectedClients.add(new ConnectedClient (false));
	}
	
	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {
		//SocketAddress remoteAddress = session.getRemoteAddress();
		
		server.printMessage(message.toString()+ "\n");
		
		session.write("Answer back to Client");
	}
	
	@Override
	public void sessionClosed(IoSession session) throws Exception {
		System.out.println("Session closed .... ");
		//session.closeNow();
		//ConnectedClients.remove(ConnectedClients.size());
		
	}
}


