/*
 * ClientHandler
 *
 * Version 1.0
 * Author: Tim
 *
 * Arbeitet alle Events der Client-Verbindung zum Server (Host) ab.
 */
package uni.bombenstimmung.de.backend.serverconnection.client;

import java.net.SocketAddress;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

import uni.bombenstimmung.de.backend.console.ConsoleHandler;
import uni.bombenstimmung.de.backend.console.MessageType;
import uni.bombenstimmung.de.backend.serverconnection.host.ConnectedClient;

public class ClientHandler extends IoHandlerAdapter implements Runnable{

	private ConnectedClient client;
	
	/**
	 * Erzeugt einen neuen ClientHandler.
	 * @param client - Beim erstellen eines neuen ConnectedClient wird dieser dem Handler ebenfalls übergeben.
	 */
	public ClientHandler(ConnectedClient client) {
		this.client = client;
	}
	
	/**
	 * Diese Methode wird immer automatisch aufgerufen, sobald eine Session mit dem Server aufgebaut wird.
	 * Hier werden die Grundlagen für das Spiel und die Verbindung gelegt. Außerdem wird auch ein Thread gestartet, über welchen der Ping berechnet wird.
	 * @param session - Session mit dem Server, wird automatisch übergeben.
	 */
	@Override
	public void sessionOpened(IoSession session) throws Exception {
		ConsoleHandler.print("Client connected with Server" + session.getRemoteAddress(), MessageType.BACKEND);
		try {
			session.write((String) "001-");					//Sende 001 zum Server um den Client der HashMap des Servers hin zu zufügen.
			Thread.sleep(1000);
			session.write((String) "002-");					//Sende 002 um die Client ID vom Server zu erhalten.
			Thread.sleep(1000);
			client.setSession(session);
			Thread pingThread = new Thread (this);
			pingThread.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
		
	/**
	 * Diese Methode wird immer automatisch beim Abbau der Session aufgerufen und es wird ebenfalls der Thread mit der Berechnung des Pings gestoppt. 
	 * @param session - Session mit dem Server, wird automatisch übergeben.
	 */
	@Override
	public void sessionClosed(IoSession session) throws Exception {
		ConsoleHandler.print("Session of ID " + client.getId() + " closed", MessageType.BACKEND);	
		Thread.currentThread().interrupt();
	}
	
	/**
	 * Diese Methode wird immer automatisch beim Erhalten einer Nachricht aufgerufen. 
	 * Hier wird die Methode receivedMessage in ConnectedClient aufgerufen, die dann die Nachricht aufteilt und weiter verarbeitet
	 * @param session - Session mit dem Server, wird automatisch übergeben.
	 * @param message - Nachricht, die der Client vom Server empfangen hat.
	 */
	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {
		String[] parts = message.toString().split("-");
		client.receivedMessage(Integer.parseInt(parts[0]), message.toString(), session);
		
		SocketAddress remoteAddress = session.getRemoteAddress();
		ConsoleHandler.print("Client:" + client.getId() + " Message received from Server " + remoteAddress + ": " + message.toString(), MessageType.BACKEND);
	}
	
	/**
	 * Diese Methode wird immer automatisch beim Senden einer Nachricht aufgerufen. 
	 */
	@Override
	public void messageSent(IoSession session, Object message) throws Exception {
	}
	
	/**
	 * Wird beim Starten des Threads aufgerufen. 
	 * Der Thread befindet sich in einer Endlosschleife, um alle 100ms den Ping zu berechnen. 
	 */
	public void run() {
		ConsoleHandler.print("Thread started...");
		while (!Thread.currentThread().isInterrupted()) {
		    try {
		        calculatePing(client.getSession());
		        Thread.sleep(100);
		    } catch (InterruptedException ex) {
		        Thread.currentThread().interrupt();
		    }
		}
	}
	
	/**
	 * Eine Nachricht an den Server mit der aktuellen Zeit des Rechner wird an den Server geschickt.
	 * @param session - Die Session, in die eine Nachricht geschrieben werden soll.
	 */
	private void calculatePing(IoSession session) {
		    	long currentTime = System.currentTimeMillis();
		        session.write("003-" + Long.toString(currentTime));
	}
}
