/*
 * ConnectedClient
 *
 * Version 1.0
 * Author: Tim
 *
 * Die Klasse erzeugt die jeweiligen Server-/Client-Objekte und verwaltet die verbundenen Clients, sowie die empfangenen Nachrichten. 
 */
package uni.bombenstimmung.de.backend.serverconnection.host;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.charset.Charset;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.future.IoFuture;
import org.apache.mina.core.future.IoFutureListener;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.DatagramSessionConfig;
import org.apache.mina.transport.socket.nio.NioDatagramAcceptor;
import org.apache.mina.transport.socket.nio.NioDatagramConnector;

import uni.bombenstimmung.de.backend.console.ConsoleHandler;
import uni.bombenstimmung.de.backend.console.MessageType;
import uni.bombenstimmung.de.backend.serverconnection.ConnectionData;
import uni.bombenstimmung.de.backend.serverconnection.client.ClientHandler;

public class ConnectedClient extends IoHandlerAdapter{
	
	private int id; 
	private boolean host;
	private long ping;
	private IoSession conSession;
	private IoConnector connector;
	NioDatagramAcceptor acceptor;
	
	private ConcurrentHashMap<SocketAddress, Integer> connectedClients;
	
	/**
	 * Erzeugt einen neuen ConnectedClient. 
	 * Ist dieser ein Host, dann wird ein neuer UDP-Server erstellt.
	 * Ist dieser ein Client, dann wird ein neuer UDP-Client erstellt.
	 * @param sHost - Boolean ob der ConnectedClient ein Host ist, oder nicht
	 * @param IP - IP-Addresse zu dem der Client sich verbinden soll
	 */
	public ConnectedClient(boolean sHost, String IP) {
		host = sHost;
		//Falls der Client ein Host ist, dann wird ein neuer UDP Server initialisiert. 
		if (host == true) {
			id = 0;
			connectedClients = new ConcurrentHashMap<SocketAddress, Integer>();
			acceptor = new NioDatagramAcceptor();
			acceptor.setHandler(new ServerHandler(this));
			acceptor.getFilterChain().addLast("codec", new ProtocolCodecFilter(new TextLineCodecFactory(Charset.forName("UTF-8"))));
			DefaultIoFilterChainBuilder chain = acceptor.getFilterChain();
			chain.addLast("logger", new LoggingFilter());
			DatagramSessionConfig dcfg = acceptor.getSessionConfig();
			dcfg.setReuseAddress(true);
			try {
				acceptor.bind(new InetSocketAddress(ConnectionData.PORT));
				ConsoleHandler.print("UDP Server started at "+ConnectionData.IP+":"+ConnectionData.PORT, MessageType.BACKEND);
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		//Falls der Client kein Host ist, dann wird ein neuer UDP Client initialisiert
		} else { 
			connector = new NioDatagramConnector();
			getConnector().setHandler(new ClientHandler(this));
			getConnector().getFilterChain().addLast("codec", new ProtocolCodecFilter(new TextLineCodecFactory(Charset.forName("UTF-8"))));
			ConnectFuture connFuture = getConnector().connect(new InetSocketAddress(IP, ConnectionData.PORT));
			connFuture.addListener(new IoFutureListener() {
				public void operationComplete(IoFuture future) {
					ConnectFuture connFuture = (ConnectFuture) future;
					if (connFuture.isConnected()) {
						conSession = future.getSession();
						try {
							sendMessage(conSession);
							ConsoleHandler.print("UDP Client successfully created and connected to the server", MessageType.BACKEND);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					} else {
							ConsoleHandler.print("Client is not connected to the server....exiting", MessageType.BACKEND);
						}
					}
				});
		}
	}
	
	/**
	 * Debug Methode, die vor finaler Version gelöscht werden soll!
	 * @param session
	 * @throws InterruptedException
	 */
	private void sendMessage(IoSession session) throws InterruptedException {
		for (int i = 0; i < 0; i++) {
			String message = "000-Hallo" + i;
			session.write(message);
			Thread.sleep(20);
		}
	}
	
	/**
	 * Verarbeitet alle erhaltenen Nachrichten vom Server oder Client.
	 * Das ist das Verbindungsstück zwischen dem Server-Client-Backend und dem Spiel selber. 
	 * Hier müssen Methoden der Pakete game, lobby, menu und aftergame benutzt werden.
	 * @param messageID - ID der Nachricht
	 * @param message - Nachricht selbst
	 * @param session - Sessioninformation der gesendeten Nachricht
	 */
	public void receivedMessage(int messageID, String message, IoSession session) {	
		switch(messageID) {
		//000 = Debug Message 
		//Format: "000-[Message]"
		case 000:
			String[] pMessage000 = message.split("-");
			ConsoleHandler.print("DEBUG MESSAGE: " + pMessage000[1], MessageType.BACKEND);
			break;
		//001 = Wird gesendet, wenn eine neue Session mit einem Client erstellt wird. 
		//Format: "001-"
		case 001:
			addClientToList(session.getRemoteAddress());
			printConnectedClients();
			break;
		//002 = Wird vom Client gesendet, um eine ID zu erhalten.
		case 002:
			if (containsClientKey(session.getRemoteAddress())) {
				for (SocketAddress i : connectedClients.keySet()) {
					if (i == session.getRemoteAddress()) {
						session.write("400-"+ Integer.toString(connectedClients.get(i)));
					}
					else {
						ConsoleHandler.print("Error", MessageType.BACKEND);
					}
				}
			} else {
				ConsoleHandler.print("Client with remote address: " + session.getRemoteAddress() + " is not in HashMap", MessageType.BACKEND);
			}
			break;
		//003 = Ping Anfrage an den Server.
		case 003:
			String[] pMessage003 = message.split("-");
	    	session.write("903-" + pMessage003[1]);
	    	break;
		//100 = Position eines Spieler wird gesendet und an alle anderen Spieler gebroadcastet. 
		//Format: "100-[ID]-[X-Cord]-[Y-Cord]"
		case 100:
			//TODO: SetX und SetY für den Host muss noch implementiert werden.
			String[] pMessage100 = message.split("-");
			sendMessageToAllClients("202-"+pMessage100[1]+"-"+pMessage100[2]+"-"+pMessage100[3]);
			break;	
		//101 = Position einer gesetzen Bombe wird gesendet und an alle anderen Spieler gebroadcastet. 
		//Format: "101-[ID-OF-BOMB-PLANTER]-[X-Cord]-[Y-Cord]"
		case 101:
			String[] pMessage101 = message.split("-");
			sendMessageToAllClients("203-"+pMessage101[1]+"-"+pMessage101[2]+"-"+pMessage101[3]);
			sendMessageToAllClients(message);
			break;	
		//ENDE DER SERVER-FAELLE
			
		//201 = Erstelle ein neues Spieler-Objekt
		//Format: "201-//TODO"
		case 201:
			//TODO: Neues Spielerobjekt erzeugen
			break;	
		//202 = Setze die Position eines Spieler. 
		//Format: "201-[ID]-[X-Cord]-[Y-Cord]"
		case 202:
			String[] pMessage202 = message.split("-");
			//TODO: Setze X und Y des Spielers
			break;	
		//203 = Setze die Position einer Bombe. 
		//Format: "203-[ID-OF-BOMB-PLANTER]-[X-Cord]-[Y-Cord]"
		case 203:
			String[] pMessage203 = message.split("-");
			//TODO: Setze X und Y der Bombe
			break;
		//300 = Starte das Spiel
		//Format: "300"
		case 300:
			//TODO: Starte Spiel
			break;
		//400 = Erhalte die Nachricht vom Server mit der ID für den Client
		case 400:
			String[] pMessage400 = message.split("-");
			int clientID  = Integer.parseInt(pMessage400[1]);
			this.id = clientID;
			ConsoleHandler.print("ID = " + this.id, MessageType.BACKEND);
			break;
			//ClientID can be used now be used with .getId
		//903 = Berechne den Ping und gebe diesen aus.
		case 903:
			String[] pMessage903 = message.split("-");
	    	long currentTime = System.currentTimeMillis();
	    	long startTime = Long.parseLong(pMessage903[1]);
	    	ping = currentTime-startTime;
	    	//ConsoleHandler.print("Ping: " + ping, MessageType.BACKEND);
	    	break;
		//999 = Wird gesendet, wenn ein Client das Spiel verlässt. 
		//Format: "999"
		case 999:
			removeClient(session);
			break;
		//Default-Case
		default:
			ConsoleHandler.print("Message received with no id: " + message, MessageType.BACKEND);
			break;
		}
			
	}
	
	/**
	 * Broadcast Methode, die eine Nachricht an alle mit dem Server verbundene Client schickt.
	 * @param message - Nachricht, die gesendet werden soll
	 */
	public void sendMessageToAllClients(String message) {
		ConsoleHandler.print("DEBUG: Broadcasting message = " + message);
		acceptor.broadcast(message);
	}
	
	void printMessage(Object message) {
		ConsoleHandler.print(message.toString(), MessageType.BACKEND);
	}
	
	/**
	 * Fügt einen Client der HashMap hinzu
	 * @param remoteAddress - Remote Addresse des Clients
	 */
	public void addClientToList(SocketAddress remoteAddress) {
		if (!containsClient(remoteAddress)) {
			int cCsize = connectedClients.size();
			connectedClients.put(remoteAddress, cCsize+1);
			ConsoleHandler.print("Client added to List", MessageType.BACKEND);
		}
	} 
		
	/**
	 * Ausgabe aller Verbundenen Clients.
	 */
	public void printConnectedClients() {
		connectedClients.forEach((k,v)-> ConsoleHandler.print(k+"="+v, MessageType.BACKEND));
	}
	
	/**
	 * Überprüft, ob ein Client bereits in der HashMap ist.
	 * @param remoteAddress - Remote Addresse des Clients
	 * @return - Gibt true oder false zurück
	 */
	public boolean containsClient(SocketAddress remoteAddress) {
		return connectedClients.contains(remoteAddress);
	}
	
	/**
	 * Überprüft, ob eine Remote Addresse bereits in der HashMap existiert.
	 * @param remoteAddress
	 * @return - Gibt true oder false zurück
	 */
	public boolean containsClientKey(SocketAddress remoteAddress) {
		return connectedClients.containsKey(remoteAddress);
	}
	
	/**
	 * Entfernt einen Client aus der HashMap.
	 * @param session - Session des Clients
	 */
	public void removeClient(IoSession session) {
		connectedClients.remove(session.getRemoteAddress());
		session.closeNow();
		return;
	}
	
	/**
	 * Gibt ID des Clients zurück
	 * @return - ID
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * Gibt zurück, ob der Client Host ist.
	 * @return - host
	 */
	public boolean isHost() {
		return host;
	}

	/**
	 * Gibt den IoConnector zurück.
	 * @return connector
	 */
	public IoConnector getConnector() {
		return connector;
	}
	
	/**
	 * Gibt die IoSession zurück.
	 * @return conSession
	 */
	public IoSession getSession() {
		return conSession;
	}
	
	/**
	 * Setzt die session eines Clients.
	 * @param session - session des Clients
	 */
	public void setSession(IoSession session) {
		conSession = session;
	}

	/**
	 * Gibt den aktuellen Ping in ms zurück.
	 */
	public long getPing() {
		return ping;
	}
}


