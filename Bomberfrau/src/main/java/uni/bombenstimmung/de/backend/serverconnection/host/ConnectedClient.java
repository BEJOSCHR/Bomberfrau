/*
 * ConnectedClient
 *
 * Version 1.0
 * Author: Tim
 *
 * Die Klasse erzeugt die jeweiligen Server-/Client-Objekte und verwaltet die verbundenen Clients, sowie die empfangenen Nachrichten. 
 */
package uni.bombenstimmung.de.backend.serverconnection.host;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.URL;
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

import uni.bombenstimmung.de.aftergame.DeadPlayerHandler;
import uni.bombenstimmung.de.backend.console.ConsoleHandler;
import uni.bombenstimmung.de.backend.console.MessageType;
import uni.bombenstimmung.de.backend.graphics.GraphicsHandler;
import uni.bombenstimmung.de.backend.serverconnection.ConnectionData;
import uni.bombenstimmung.de.backend.serverconnection.client.ClientHandler;
import uni.bombenstimmung.de.game.FieldContent;
import uni.bombenstimmung.de.game.Game;
import uni.bombenstimmung.de.game.Player;
import uni.bombenstimmung.de.game.PlayerHandler;
import uni.bombenstimmung.de.lobby.LobbyCreate;


public class ConnectedClient extends IoHandlerAdapter{
	
	private int id = -1; 
	private boolean host;
	private long ping;
	private IoSession conSession;
	private IoConnector connector;
	NioDatagramAcceptor acceptor;
	
	private ConcurrentHashMap<SocketAddress, Integer> connectedClients;
	private Stack<Integer> idStack;
	
	private Player player;
	
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
			//hostGetPublicIP();
			acceptor = new NioDatagramAcceptor();
			getAcceptor().setHandler(new ServerHandler(this));
			getAcceptor().getFilterChain().addLast("codec", new ProtocolCodecFilter(new TextLineCodecFactory(Charset.forName("UTF-8"))));
			DefaultIoFilterChainBuilder chain = getAcceptor().getFilterChain();
			chain.addLast("logger", new LoggingFilter());
			DatagramSessionConfig dcfg = getAcceptor().getSessionConfig();
			dcfg.setReuseAddress(true);
			try {
				getAcceptor().bind(new InetSocketAddress(ConnectionData.PORT));
				ConsoleHandler.print("UDP Server started at "+hostGetPublicIP()+":"+ConnectionData.PORT, MessageType.BACKEND);
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		//Is the new created Client not the host, a new UDP Client will be initialized
		} else { 
			connector = new NioDatagramConnector();
			connector.setHandler(new ClientHandler(this));
			connector.getFilterChain().addLast("codec", new ProtocolCodecFilter(new TextLineCodecFactory(Charset.forName("UTF-8"))));
			ConnectFuture connFuture = connector.connect(new InetSocketAddress(IP, ConnectionData.PORT));
			connFuture.awaitUninterruptibly();
			connFuture.addListener(new IoFutureListener<ConnectFuture>() {
			    public void operationComplete(ConnectFuture future) {
				ConsoleHandler.print(Boolean.toString(future.isConnected()), MessageType.BACKEND);
				if (future.isConnected()) {
				    conSession = future.getSession();
				    try {
					sendMessage(conSession);
				    } catch (InterruptedException e) {
					e.printStackTrace();
				    }
				} else {
				    ConsoleHandler.print("Not connected...exiting", MessageType.BACKEND);
				}
			    }
			});
		}
	}
			/*
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
	} */
	
	/**
	 * Debug Methode, die vor finaler Version gelöscht werden soll!
	 * @param session
	 * @throws InterruptedException
	 */
	private void sendMessage(IoSession session) throws InterruptedException {
		for (int i = 0; i < 5; i++) {
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
		//202 = Setze die Position eines Players. (Client)
		//Format: "202-[ID]-[X-Cord]-[Y-Cord]-[SCREEN-HEIGHT]"
		case 202:
			String[] pMessage202 = message.split("-");
			if(id != Integer.parseInt(pMessage202[1])) {
			    if (Integer.parseInt(pMessage202[4]) == GraphicsHandler.getHeight()) {
				PlayerHandler.getAllPlayer().get(Integer.parseInt(pMessage202[1])).setDisplayCoordinates(Integer.parseInt(pMessage202[2]), Integer.parseInt(pMessage202[3]));
			    } else {
				PlayerHandler.getAllPlayer().get(Integer.parseInt(pMessage202[1])).setDisplayCoordinates(
					(int)( (  Double.parseDouble(pMessage202[2])  /  ( Double.parseDouble(pMessage202[4]) / (double)GraphicsHandler.getHeight()  )  )   + 0.5 ),
					(int)( (  Double.parseDouble(pMessage202[3])  /  ( Double.parseDouble(pMessage202[4]) / (double)GraphicsHandler.getHeight()  )  )   + 0.5 ));
			    }
			}
			break;	
		//203 = Setze die Position eines Players. (Server)
		//Format: "203-[ID]-[X-Cord]-[Y-Cord]-[SCREEN-HEIGHT]"
		case 203:
			String[] pMessage203 = message.split("-");
			if (Integer.parseInt(pMessage203[4]) == GraphicsHandler.getHeight()) {
				PlayerHandler.getAllPlayer().get(Integer.parseInt(pMessage203[1])).setDisplayCoordinates(Integer.parseInt(pMessage203[2]), Integer.parseInt(pMessage203[3]));
			} else {
			    PlayerHandler.getAllPlayer().get(Integer.parseInt(pMessage203[1])).setDisplayCoordinates(
				(int)( (  Double.parseDouble(pMessage203[2])  /  ( Double.parseDouble(pMessage203[4]) / (double)GraphicsHandler.getHeight()  )  )   + 0.5 ),
				(int)( (  Double.parseDouble(pMessage203[3])  /  ( Double.parseDouble(pMessage203[4]) / (double)GraphicsHandler.getHeight()  )  )   + 0.5 ));
			}
			this.sendMessageToAllClients("202-" + pMessage203[1] + "-" + pMessage203[2] + "-" + pMessage203[3] + "-" + pMessage203[4]);
			break;
		//204 = Bombe legen (Client)
		//Format: "204-[ID]"
		case 204:
		    String[] pMessage204 = message.split("-");
		    if (id != Integer.parseInt(pMessage204[1])) {
			PlayerHandler.getAllPlayer().get(Integer.parseInt(pMessage204[1])).actionPlantBomb();
		    }
		    break;
		//205 = Bombe legen (Server)
		//Format: "205-[ID]"
		case 205:
		    String[] pMessage205 = message.split("-");
		    if (id != Integer.parseInt(pMessage205[1])) {
			PlayerHandler.getAllPlayer().get(Integer.parseInt(pMessage205[1])).actionPlantBomb();
		    }
		    break;
		//206 = Signalisiere, dass Player tot ist. (Client)
		//Format: "206-[ID-OF-DEAD-PLAYER]"
		case 206:
		    String[] pMessage206 = message.split("-");
		    if(id != Integer.parseInt(pMessage206[1])) {
			PlayerHandler.getAllPlayer().get(Integer.parseInt(pMessage206[1])).setDead(true);
		    }
		    break;
		//207 = Signalisiere, dass Player tot ist. (Server)
		//Format: "207-[ID-OF-DEAD-PLAYER]"
		case 207:
		    String[] pMessage207 = message.split("-");
		    if (id != Integer.parseInt(pMessage207[1])) {
			PlayerHandler.getAllPlayer().get(Integer.parseInt(pMessage207[1])).setDead(true);
		    }
		    break;
		//208 = Spawnt die Items 
		//Format: "208-[X-Cord]-[Y-Cord]-[FieldContent]"
		case 208:
		    String[] pMessage208 = message.split("-");
		    int x = Integer.parseInt(pMessage208[1]);
		    int y = Integer.parseInt(pMessage208[2]);
		    FieldContent item = FieldContent.valueOf(pMessage208[3]);
		    
		    Game.changeFieldContent(item, x, y);
		    break;
		//209 = 
		//Format: 
		case 209:
		    String[] pMessage209 = message.split("-");
		    
		    break;
		//300 = Starte das Spiel
		//Format: "300"
		case 300:
			//TODO: Starte Spiel
			break;
		//400 = Erhalte die Nachricht vom Server mit der ID für den Client
		
		/////////////////////////////////////////// 500-599 Lobby Cases ////////////////////////////////////////////////////////
			

		//501 = Adde neuen Player im LobbyCreate.player[] des Hosts/Servers, dann uebergebe die Information von allen anderen Playern an den neu-gejointen,
		// sodass dieser die in seinem LobbyCreate.player[] hinzufuegen kann. (Cases 503-505)
		// Abschliessend wird die Information des neu-gejointen Player an alle gebroadcasted, sodass diese den Player auch in ihrem jeweiligen LobbyCreate.player[] adden koennen (Case 506)
		//Format: "501-[ID]-[Name]-[isHost]"
		case 501:
		    	String[] pMessage501 = message.split("-");
		    	    // Der gejointe Player muss die anderen Player Objekte auch noch erstellen. case 503-505
		    	    LobbyCreate.addPlayer(pMessage501[1], pMessage501[2], pMessage501[3], "", "");
		    	    for(int i=0; i<LobbyCreate.numberOfMaxPlayers; i++) {
		    		if (LobbyCreate.player[i] != null) {
		    		    // Cases in Abhaenigkeit der ID des zu speichernden Players werden aufgerufen
			    	    sendMessage(session, "50" + Integer.toString(3+i) + "-" +  LobbyCreate.player[i].getId() + "-" + LobbyCreate.player[i].getName() + "-" + String.valueOf(LobbyCreate.player[i].getisHost())
			    	    	+ "-" + LobbyCreate.getMap() + "-" + LobbyCreate.player[i].getSkin()); 
		    		}
		    	    }
		    	    sendMessageToAllClients("502-" + pMessage501[1] + "-" + pMessage501[2] + "-" + pMessage501[3]);
		    	    sendMessage(session, "506-" + LobbyCreate.numberOfMaxPlayers);
		    	break;
		    	
		// 502 = Aufruf an alle Clients den neu-gejointen Player zu adden
		//Format: "502-[ID]-[Name]-[isHost]"
		case 502:
//		    	ConsoleHandler.print("Der case 502 wurde aufgerufen vom Backend", MessageType.LOBBY);
		    	String[] pMessage502 = message.split("-");
			LobbyCreate.addPlayer(pMessage502[1], pMessage502[2], pMessage502[3], "", "");
		    	break;
		
		// Case 503-505 wird vom Server in 501 aufgerufen, sodass der zu joinende Client alle schon existierenen Player erstmal einfügt
		    	
		//503 = Fuer LobbyCreate.player[0]
		//Format: "503-[ID]-[Name]-[isHost]-[MapNr]-[SkinNr]" 
		case 503:
//		    	ConsoleHandler.print("Der case 503 wurde aufgerufen vom Backend", MessageType.LOBBY);
		    	String[] pMessage503 = message.split("-");
		    	LobbyCreate.addPlayer(pMessage503[1], pMessage503[2], pMessage503[3], pMessage503[4], pMessage503[5]);
		    	break;
		    	
		//504 = Fuer LobbyCreate.player[1]
		//Format: "504-[ID]-[Name]-[isHost]-[MapNr]-[SkinNr]"    	
		case 504:
//		    	ConsoleHandler.print("Der case 504 wurde aufgerufen vom Backend", MessageType.LOBBY);
		    	String[] pMessage504 = message.split("-");
		    	LobbyCreate.addPlayer(pMessage504[1], pMessage504[2], pMessage504[3], pMessage504[4], pMessage504[5]);
		    	break;
		    	
		//505 = Fuer LobbyCreate.player[2]
		//Format: "505-[ID]-[Name]-[isHost]-[MapNr]-[SkinNr]" 
		case 505:
//		    	ConsoleHandler.print("Der case 505 wurde aufgerufen vom Backend", MessageType.LOBBY);
		    	String[] pMessage505 = message.split("-");
		    	LobbyCreate.addPlayer(pMessage505[1], pMessage505[2], pMessage505[3], pMessage505[4], pMessage505[5]);
		    	break;
		    	
		//506 = Set numberPlayer mainly for newly joined Players
		//Format: "506-[numberOfMaxPlayers]"
		case 506:
			String[] pMessage506 = message.split("-");
			LobbyCreate.setNumberPlayer(Integer.parseInt(pMessage506[1]));
			ConsoleHandler.print("Case 506 wurde aufgerufen", MessageType.LOBBY);
			break;
			
		//507 = Wird von dem Host in dem Mapaenderungen aufgerufen, sodass alle Player die zaehlerSelectionMap aendern	
		//Format: "507-[MapNr]"
		case 507:
		    	String[] pMessage507 = message.split("-");
		    	LobbyCreate.setMap(Integer.parseInt(pMessage507[1]));
		    	break;
		    	
		// Wenn Clients Ihre Skin wechseln, dann setzt der Server diese bei sich und sendet an alle weiter, damit diese case 509 ausfuehren   
		//Format: "508-[ID]-[SkinNr]"
		case 508:
			String[] pMessage508 = message.split("-");
			LobbyCreate.setSkin(Integer.parseInt(pMessage508[1]), Integer.parseInt(pMessage508[2]));;
			sendMessageToAllClients("509-" + pMessage508[1] + "-" + pMessage508[2]);
		    	break;
		    	
		//509 = Befehl um Skin Auswahl eines anderen Clients zu aendern  
		//Format: "509-[ID]-[SkinNr]"
		case 509:
			String[] pMessage509 = message.split("-");
			LobbyCreate.setSkin(Integer.parseInt(pMessage509[1]), Integer.parseInt(pMessage509[2]));;
		    	break;
		    	
		//510 = Wenn Clients isReady aendern, dann sendet setzt Server diese bei sich und sendet an alle weiter, damit die case 511 ausfuehren
		//Format: "510-[ID]-[isReady]"
		case 510:
		    	String[] pMessage510 = message.split("-");
		    	LobbyCreate.player[Integer.parseInt(pMessage510[1])].setisReadyForClients(pMessage510[2]);
		    	sendMessageToAllClients("511-" + pMessage510[1] + "-" + pMessage510[2]);
		    	break;
		    	
		//511 = Befehl um IsReady Checkboxen eines Clients zu aendern
		//Format: "511-[ID]-[isReady]"
		case 511:
		    	String[] pMessage511 = message.split("-");
		    	LobbyCreate.player[Integer.parseInt(pMessage511[1])].setisReadyForClients(pMessage511[2]);
		    	break;
		    
		//512 = Wird vom Client gesendet, wenn dieser EXIT per BUTTON drueckt
		//Format: "512-[ID]"
		case 512:
		    	String[] pMessage512 = message.split("-");
		    	LobbyCreate.player[Integer.parseInt(pMessage512[1])] = null;
		    	// Befiehlt dem am verlassenden Client zu verlassen
//		    	sendMessage(session, "999-");
		    	removeClient(session);
		    	// Checken ob der verlassene Player der letzte Player war
		    	if (LobbyCreate.numberOfMaxPlayers-1 == Integer.parseInt(pMessage512[1])) {
			    LobbyCreate.numberOfMaxPlayers--;
		    	}
		    	sendMessageToAllClients("513-" + pMessage512[1]);
		    	// numberOfMaxPlayers an alle anderen Clients senden
		    	sendMessageToAllClients("506-" + LobbyCreate.numberOfMaxPlayers);
		    	break;
		    	
		//513 = Wird von allen Clients aufgerufen, sodass der geleavede Player aus dem Array geloescht wird  
		//Format: "513-[ID]"
		case 513:
		    	String[] pMessage513 = message.split("-");
		    	LobbyCreate.player[Integer.parseInt(pMessage513[1])] = null;
		    	break;
		    	
		//514 = Wird aufgerufen, sobald der Host leaved
		//Format: "514-"
		case 514:
		    	for (int i=0; i<LobbyCreate.player.length; i++) {
			    LobbyCreate.player[i] = null;
		    	}
		    	LobbyCreate.numberOfMaxPlayers = 0;
		    	GraphicsHandler.lobby = null;
		    	try {
		    	    LobbyCreate.client.getConnector().dispose();
		    	} catch(Exception e) {
		    	    e.printStackTrace();
		    	}
		    	GraphicsHandler.switchToMenuFromLobby();
		    	break;
		    	
		//515 = Wird von allen Clients aufgerufen, sobald alle in das Ingame sollen    
		//Format: "515-"
		case 515:
		    	GraphicsHandler.switchToIngameFromLobby();
		    	break;
		    	
	
		    	
			/////////////////////////////////////////// 600-699 Aftergame Cases ////////////////////////////////////////////////////////
		    	
		//601 = DeadPlayerHandler.updateDeadPlayer
		//Format: "601-[ID]-[NAME]-[deathTime]-[Score]"
		case 601: 
		    	String[] pMessage601 = message.split("-");
		    	DeadPlayerHandler.updateDeadPlayer(pMessage601[1], pMessage601[2], pMessage601[3], pMessage601[4]);
		    	break;
		    	
		case 602: 
		    
		    	break;
		    	
		case 603: 
		    
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
		getAcceptor().broadcast(message);
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
	
	public String hostGetPublicIP() {
	    try {
	    URL getIP = new URL("http://checkip.amazonaws.com/");
	    BufferedReader in = new BufferedReader(new InputStreamReader(getIP.openStream()));
	    String hostIP = in.readLine();
	    return hostIP;
	    } catch (Exception e) {
		e.printStackTrace();
	    }
	    return null;
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

	public NioDatagramAcceptor getAcceptor() {
	    return acceptor;
	}
}


