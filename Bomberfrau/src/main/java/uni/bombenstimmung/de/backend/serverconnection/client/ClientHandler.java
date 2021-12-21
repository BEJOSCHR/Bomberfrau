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
import java.util.Date;

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
		ConsoleHandler.print("Client connected with Server" + session.getRemoteAddress(), MessageType.BACKEND);
		try {
			session.write((String) "001-");
			Thread.sleep(1000);
			session.write((String) "002-");
			Thread.sleep(1000); 
		} catch (Exception e) {
			e.printStackTrace();
		}
		//calculatePing (session);
		/*
		while (true) {
		    try {
		    	ConsoleHandler.print("In infinite loop", MessageType.BACKEND);
		    	Date date = new Date();
		    	long time = date.getTime();
		        session.write("003-" + Long.toString(time));
		        Thread.sleep(500);
		    } catch (Exception e) {
		        e.printStackTrace();;
		    }
		} */
	}
		
	
	@Override
	public void sessionClosed(IoSession session) throws Exception {
		ConsoleHandler.print("Session of ID " + client.getId() + " closed", MessageType.BACKEND);	
	}
	
	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {
		String[] parts = message.toString().split("-");
		client.receivedMessage(Integer.parseInt(parts[0]), message.toString(), session);
		
		SocketAddress remoteAddress = session.getRemoteAddress();
		ConsoleHandler.print("Client:" + client.getId() + " Message received from Server " + remoteAddress + ": " + message.toString(), MessageType.BACKEND);
	}
	
	@Override
	public void messageSent(IoSession session, Object message) throws Exception {
		//ConsoleHandler.print("Client: Message sent");
	}
	
	
	private void calculatePing(IoSession session) {
		while (true) {
		    try {
		    	ConsoleHandler.print("In infinite loop", MessageType.BACKEND);
		    	Date date = new Date();
		    	long time = date.getTime();
		        session.write("003-" + Long.toString(time));
		        Thread.sleep(500);
		    } catch (Exception e) {
		        e.printStackTrace();;
		    }
		} 
	}
}
