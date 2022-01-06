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
import java.util.Stack;
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
import uni.bombenstimmung.de.lobby.LobbyCreate;

public class ConnectedClient extends IoHandlerAdapter{
	
	private int id; 
	private boolean host;
	private long ping;
	private IoSession conSession;
	private IoConnector connector;
	NioDatagramAcceptor acceptor;
	
	private ConcurrentHashMap<SocketAddress, Integer> connectedClients;
	private Stack<Integer> idStack;
	
	/**
	 * Erzeugt einen neuen ConnectedClient. 
	 * Ist dieser ein Host, dann wird ein neuer UDP-Server erstellt.
	 * Ist dieser ein Client, dann wird ein neuer UDP-Client erstellt.
	 * @param sHost - Boolean ob der ConnectedClient ein Host ist, oder nicht
	 * @param IP - IP-Addresse zu dem der Client sich verbinden soll
	 */
	@SuppressWarnings("rawtypes")
	public ConnectedClient(boolean sHost, String IP) {
		host = sHost;
		//Is the new created Client the host, a new server will be initialized
		if (host == true) {
			id = 0;
			connectedClients = new ConcurrentHashMap<SocketAddress, Integer>();
			idStack = new Stack<Integer>();
			addIdsToStack();

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
		//Is the new created Client not the host, a new UDP Client will be initialized
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
		    	addClientToList(session);
		    	break;
		//002 = Wird vom Client gesendet, um eine ID zu erhalten.
		case 002:
			if (containsClientKey(session.getRemoteAddress())) {
				for (SocketAddress i : connectedClients.keySet()) {
					if (i == session.getRemoteAddress()) {
						sendMessage(session, "900-"+ Integer.toString(connectedClients.get(i)));
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
			sendMessage(session, "903-" + pMessage003[1]);
			break;
		//007 = Wird gesendet, wenn ein Client das Spiel verlässt. 
		case 007:
		    	removeClient(session);
		    	break;
		//010 = Wird nie aufgerufen. Muss an den Server gesendet werden, wenn es zu viele Spieler gibt. 
		case 010:
		    	session.closeNow();
		    	break;
		//100 = Position eines Spieler wird gesendet und an alle anderen Spieler gebroadcastet. 
		//Format: "100-[ID]-[X-Cord]-[Y-Cord]"
		case 100:
			//TODO: SetX und SetY für den Host muss noch implementiert werden.
			String[] pMessage100 = message.split("-");
			//sendMessageToAllClients("202-"+pMessage100[1]+"-"+pMessage100[2]+"-"+pMessage100[3]);
			break;	
		//101 = Position einer gesetzen Bombe wird gesendet und an alle anderen Spieler gebroadcastet. 
		//Format: "101-[ID-OF-BOMB-PLANTER]-[X-Cord]-[Y-Cord]"
		case 101:
			String[] pMessage101 = message.split("-");
			//sendMessageToAllClients("203-"+pMessage101[1]+"-"+pMessage101[2]+"-"+pMessage101[3]);
			//sendMessageToAllClients(message);
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
			@SuppressWarnings("unused") String[] pMessage202 = message.split("-");
			//TODO: Setze X und Y des Spielers
			break;	
		//203 = Setze die Position einer Bombe. 
		//Format: "203-[ID-OF-BOMB-PLANTER]-[X-Cord]-[Y-Cord]"
		case 203:
			@SuppressWarnings("unused") String[] pMessage203 = message.split("-");
			//TODO: Setze X und Y der Bombe
			break;
		//300 = Starte das Spiel
		//Format: "300"
		case 300:
			//TODO: Starte Spiel
			break;
		//400 = Erhalte die Nachricht vom Server mit der ID für den Client
		
			
		// LOBBY TEST - Server Antwort
		case 500:
		    	String[] pMessage500 = message.split("-");
		    	if (pMessage500[0] == "3") {
			    if (pMessage500[1] == "red") {
				//Setze Farbe red
				sendMessageToAllClients("457-" +pMessage500[1] + "-" + pMessage500[0]);
			    }  
		    	}
		    	break;
		    	
		case 5001:
		    	String [] pMessage457 = message.split("-");
		    	// set Farbe von PlayerID pMessage457[2]
		    	break;
		// LOBBY TEST - 
		
		// LOBBY Join Nachrichten von Clients an Server und anschließend Aufruf von Sever an alle Clients um neuen Client zu adden case 502
		case 501:
		    	String[] pMessage501 = message.split("-");
		    	if(pMessage501[1].equals("binGeJoined")) {
		    	    // Der gejointe Player muss die anderen Player Objekte auch noch erstellen. case 503-505

		    	    LobbyCreate.addPlayer(Integer.toString(LobbyCreate.numberPlayer), pMessage501[2], pMessage501[3], pMessage501[4]);
		    	    for(int i=0; i<LobbyCreate.numberPlayer; i++) {
		    		ConsoleHandler.print("Case 50" + Integer.toString(3+i),MessageType.LOBBY);
		    	    	sendMessage(session, "50" + Integer.toString(3+i) + "-" + i + "-" +  LobbyCreate.player[i].getId() + "-" + LobbyCreate.player[i].getName() + "-" + String.valueOf(LobbyCreate.player[i].getisHost()));
		    	    }

		    	    sendMessageToAllClients("502-" + LobbyCreate.numberPlayer + "-" + pMessage501[2] + "-" + pMessage501[3] + "-" + pMessage501[4]);
		    	    sendMessage(session, "506-" + LobbyCreate.numberPlayer);
		    	}
		    	break;
		// Aufruf an alle Clients einen neuen Client zu adden
		case 502:
		    	ConsoleHandler.print("Der case 502 wurde aufgerufen vom Backend", MessageType.LOBBY);
		    	String[] pMessage502 = message.split("-");
//		    	if(this.id != Integer.parseInt(pMessage502[2])) {
			LobbyCreate.addPlayer(pMessage502[1], pMessage502[2], pMessage502[3], pMessage502[4]);

		    	break;
		
		// Case 503-505 wird vom Server in 501 aufgerufen, sodass der zu joinende Client alle schon existierenen Player erstmal einfügt
		case 503:
		    	ConsoleHandler.print("Der case 503 wurde aufgerufen vom Backend", MessageType.LOBBY);
		    	String[] pMessage503 = message.split("-");
		    	LobbyCreate.addPlayer(pMessage503[1], pMessage503[2], pMessage503[3], pMessage503[4]);
		    	break;
		    	
		case 504:
		    	ConsoleHandler.print("Der case 504 wurde aufgerufen vom Backend", MessageType.LOBBY);
		    	String[] pMessage504 = message.split("-");
		    	LobbyCreate.addPlayer(pMessage504[1], pMessage504[2], pMessage504[3], pMessage504[4]);
		    	break;
		    	
		case 505:
		    	ConsoleHandler.print("Der case 505 wurde aufgerufen vom Backend", MessageType.LOBBY);
		    	String[] pMessage505 = message.split("-");
		    	LobbyCreate.addPlayer(pMessage505[1], pMessage505[2], pMessage505[3], pMessage505[4]);
		    	break;
		// Set numberPlayer for newly joined Players
		case 506:
			String[] pMessage506 = message.split("-");
			LobbyCreate.setNumberPlayer(Integer.parseInt(pMessage506[1]));
			break;
			
		// Wird von dem Host in dem Mapaenderungen aufgerufen, sodass alle Player die zaehlerSelectionMap aendern	
		case 507:
		    	String[] pMessage507 = message.split("-");
		    	LobbyCreate.setMap(Integer.parseInt(pMessage507[1]));
		    	break;
		// Wenn Clients Ihre Skin wechseln, dann sendet Server an alle weiter, damit die case 509 ausfuehren    	
		case 508:
			String[] pMessage508 = message.split("-");
			LobbyCreate.setSkin(Integer.parseInt(pMessage508[1]), Integer.parseInt(pMessage508[2]));;
			sendMessageToAllClients("509-" + pMessage508[1] + "-" + pMessage508[2]);
		    	break;
		// Befehl um Skin Auswahl eines anderen Clients zu aendern    	
		case 509:
			String[] pMessage509 = message.split("-");
			LobbyCreate.setSkin(Integer.parseInt(pMessage509[1]), Integer.parseInt(pMessage509[2]));;
		    	break;
		    	
		case 900:
			String[] pMessage900 = message.split("-");
			int clientID  = Integer.parseInt(pMessage900[1]);
			this.id = clientID;
			ConsoleHandler.print("Client: Set ID = " + this.id, MessageType.BACKEND);
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
		//999 = Wird gesendet, wenn der Server die Verbindung beenden will.
		//Format: "999"
		case 999:
			session.closeNow();
			break;
		//Default-Case
		default:
			ConsoleHandler.print("Message received with no id: " + message, MessageType.BACKEND);
			break;
		}	
	}
	
	/**
	 * Broadcast Methode, die eine Nachricht an alle mit dem Server verbundene Client schickt.
	 * Kann nur vom Server benutzt werden!
	 * @param message - Nachricht, die gesendet werden soll
	 */
	public void sendMessageToAllClients(String message) {
		ConsoleHandler.print("DEBUG: Broadcasting message = " + message);
		acceptor.broadcast(message);
	}
	
	/**
	 * Sendet eine Nachricht vom Client an den Server
	 * @param session - Session zwischen Client und Server. Beim Client diese mit getSession holen, beim Server immer direkt "session" benutzen.
	 * @param message - Nachricht, die gesendet werden soll
	 */
	public void sendMessage(IoSession session, String message) {
	    session.write(message);
	}
	
	/**
	 * Fügt alle verfügbaren IDs dem Stack hinzu
	 */
	public void addIdsToStack () {
	    if (idStack != null) {
		idStack.push(3);
		idStack.push(2);
		idStack.push(1);
	    } else {
		ConsoleHandler.print("Error, Stack is not initialized", MessageType.BACKEND );
	    }
	}
	
	
	/**
	 * Fügt einen Client der HashMap hinzu
	 * @param remoteAddress - Remote Addresse des Clients
	 */
	public void addClientToList(IoSession session) {
	    SocketAddress remoteAddress = session.getRemoteAddress();
	    if (!containsClient(remoteAddress)) {
		if (!idStack.empty()) {
		    int id = idStack.pop();
			connectedClients.put(remoteAddress, id);
			ConsoleHandler.print("Client added to List", MessageType.BACKEND);
			printConnectedClients();
		    } else {
			ConsoleHandler.print("Stack is empty, too many players. Closing session with newly connected Client.", MessageType.BACKEND);
			sendMessage(session, "999-");
		    }
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
	    	int id = connectedClients.get(session.getRemoteAddress());
	    	idStack.push(id);
		connectedClients.remove(session.getRemoteAddress());
		session.closeNow();
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
	 * Gibt den aktuellen Ping des Clients zum Server zurück.
	 * @return ping
	 */
	public long getPing() {
	    return ping;
	}
}


