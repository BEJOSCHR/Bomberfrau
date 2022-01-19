/*
 * ServerHandler
 *
 * Version 1.0
 * Author: Tim
 *
 * Arbeitet alle Events der Server-Verbindung zu allen Clients (ConnectedClients) ab.
 */
package uni.bombenstimmung.de.backend.serverconnection.host;

import java.net.SocketAddress;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

import uni.bombenstimmung.de.backend.console.ConsoleHandler;
import uni.bombenstimmung.de.backend.console.MessageType;

public class ServerHandler extends IoHandlerAdapter {

	private ConnectedClient server;
	
	/**
	 * Erzeugt einen neuen ServerHandler.
	 * @param server - Beim erstellen eines neuen ConnectedClient wird dieser dem Handler ebenfalls übergeben.
	 */
	public ServerHandler(ConnectedClient server) {
		this.server = server;
	}
	
	/**
	 * Bei einem Fehler des Server mit einem Client wird die Verbindung getrennt.
	 * @param session
	 * @param cause
	 * @throws Exception
	 */
	public void excepetionCaught(IoSession session, Throwable cause) throws Exception {
		cause.printStackTrace();
		session.closeNow();
	}
	
	/**
	 * Diese Methode wird immer automatisch aufgerufen, sobald eine Session mit dem Client aufgebaut wird.
	 */
	@Override
	public void sessionCreated(IoSession session) throws Exception {
		ConsoleHandler.print("Server: session created with Client " + session.getRemoteAddress(), MessageType.BACKEND);
		if (server.isIdStackEmpty() == true) {
		    Thread.sleep(300);
		    session.write("514-");
		    ConsoleHandler.print("Server is full, disconnecting ...", MessageType.BACKEND);
		}
	}
	
	/**
	 * Diese Methode wird immer automatisch beim Abbau der Session aufgerufen. Daraufhin wird der Client aus der HashMap entfernt. 
	 * @param session - Session mit dem Server, wird automatisch übergeben.
	 */
	@Override
	public void sessionClosed(IoSession session) throws Exception {
		ConsoleHandler.print("Session closed .... ", MessageType.BACKEND);
		if (server.containsClient(session.getRemoteAddress())) {
		    server.removeClient(session);
		}
	}
	
	/**
	 * Diese Methode wird immer automatisch beim Erhalten einer Nachricht aufgerufen. 
	 * Hier wird die Methode receivedMessage in ConnectedClient aufgerufen, die dann die Nachricht aufteilt und weiter verarbeitet
	 * @param session - Session mit dem Client, wird automatisch übergeben.
	 * @param message - Nachricht, die der Server vom Client empfangen hat.
	 */
	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {
		String[] parts = message.toString().split("-");
		if (Integer.parseInt(parts[0]) != 003) {
		    SocketAddress remoteAddress = session.getRemoteAddress();
		    ConsoleHandler.print("Server: Message received from Client " + remoteAddress + ": " + message.toString(), MessageType.BACKEND);
		}
		else if (Integer.parseInt(parts[0]) != 003) {
		    
		}
		server.receivedMessage(Integer.parseInt(parts[0]), message.toString(), session);
	}
}


