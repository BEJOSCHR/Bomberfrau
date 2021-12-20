/*
 * ConnectedClient
 *
 * Version 0.1.7
 * Author: Tim
 *
 * Die Klasse erzeugt die jeweiligen Server-/Client-Objekte und verwaltet die verbundenen Clients. 
 */
package uni.bombenstimmung.de.backend.serverconnection.host;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
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
	private IoSession conSession;
	private IoConnector connector;
	NioDatagramAcceptor acceptor;
	
	private ConcurrentHashMap<SocketAddress, Integer> connectedClients;
	
	@SuppressWarnings("rawtypes")
	public ConnectedClient(boolean sHost, int sId, String IP) {
		id = sId;
		host = sHost;

		//Is the new created Client the host, a new server will be initialized
		if (host == true) {
			connectedClients = new ConcurrentHashMap<SocketAddress, Integer>();
			//NioDatagramAcceptor acceptor = new NioDatagramAcceptor();
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
			
		}	
		//Is the new created Client not the host, a new UDP Client will be initialized
		else {
			connector = new NioDatagramConnector();
			//connector.setHandler(this);
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
	
	//Sends a message to the connected Client
	private void sendMessage(IoSession session) throws InterruptedException {
		for (int i = 0; i < 100; i++) {
			String message = "000-Hallo" + i;
			session.write(message);
			Thread.sleep(20);
		}
	}
	
	//Handles all the received Messages from Clients and the Server.
	//This is the bridge between the server-client-backend and the game itself.
	//Here, other groups can add their functions when certain events happen.
	public void receivedMessage(int messageID, String message, IoSession session) {	
		switch(messageID) {
		//000 = Debug Message 
		//Format: "000-[Message]"
		case 000:
			String[] pMessage000 = message.split("-");
			ConsoleHandler.print("DEBUG MESSAGE: " + pMessage000[1], MessageType.BACKEND);
			//sendMessageToAllClients("1000-Sharing this to all clients");
			break;
			
		//001 = Being sent when opening a session with the server. 
		//Format: "001-[ID]"
		case 001:
			String[] pMessage001 = message.split("-");
			addClientToList(session.getRemoteAddress(), Integer.parseInt(pMessage001[1]));
			printConnectedClients();
			break;
			
		//100 = Position of one player is sent and will be broadcasted to all the other clients in the session.
		//Format: "100-[ID]-[X-Cord]-[Y-Cord]"
		case 100:
			//TODO: SetX und SetY für den Host muss noch implementiert werden.
			String[] pMessage100 = message.split("-");
			sendMessageToAllClients("202"+pMessage100[1]+pMessage100[2]+pMessage100[3]);
			break;
			
		//101 = Position of the planted bomb is sent and will be broadcasted to all the other clients in the session.
		//Format: "101-[ID-OF-BOMB-PLANTER]-[X-Cord]-[Y-Cord]"
		case 101:
			String[] pMessage101 = message.split("-");
			sendMessageToAllClients("203"+pMessage101[1]+pMessage101[2]+pMessage101[3]);
			sendMessageToAllClients(message);
			break;
			
		//END OF SERVER CASES
			
		//201 = Create a new player object
		//Format: "201-//TODO"
		case 201:
			//TODO: Neues Spielerobjekt erzeugen
			break;
			
		//202 = Position of one player is sent and will be set on the clients site. 
		//Format: "201-[ID]-[X-Cord]-[Y-Cord]"
		case 202:
			String[] pMessage202 = message.split("-");
			//TODO: Setze X und Y des Spielers
			break;
			
		//202 = Position of a bomb is sent and will be set on the clients site. 
		//Format: "202-[ID-OF-BOMB-PLANTER]-[X-Cord]-[Y-Cord]"
		case 203:
			String[] pMessage203 = message.split("-");
			//TODO: Setze X und Y der Bombe
			break;
			
		//300 = Receive the message of the host that the game is starting
		//Format: "300"
		case 300:
			//TODO: Starte Spiel
			break;
			
		//999 = Being sent when a Client disconnects/leaves from the Server. 
		//Format: "999"
		case 999:
			removeClient(session);
			break;
		
		//Default case when no message ID is being sent
		default:
			ConsoleHandler.print("Message received with no id: " + message, MessageType.BACKEND);
			break;
		}
			
	}
	
	//Sends a message to all connected Clients
	public void sendMessageToAllClients(String message) {
		ConsoleHandler.print("DEBUG: Broadcasting message = " + message);
		acceptor.broadcast(message);
	}
	
	void printMessage(Object message) {
		ConsoleHandler.print(message.toString(), MessageType.BACKEND);
	}
	
	//Add a new client with the corresponding remote address to the Hash Map
	public void addClientToList(SocketAddress remoteAddress, int id) {
		if (!containsClient(remoteAddress)) {
			connectedClients.put(remoteAddress, id);
			ConsoleHandler.print("Client added to List", MessageType.BACKEND);
		}
	} 
		
	//Prints out the elements inside the Hash Map
	public void printConnectedClients() {
		connectedClients.forEach((k,v)-> ConsoleHandler.print(k+"="+v, MessageType.BACKEND));
	}
	
	//Checks if the Hash Map already contains a client with the remote address
	public boolean containsClient(SocketAddress remoteAddress) {
		return connectedClients.contains(remoteAddress);
	}
	
	//Removes the client from the Hash Map
	public void removeClient(IoSession session) {
		connectedClients.remove(session.getRemoteAddress());
		session.closeNow();
		return;
	}
	
	//Returns the id of the player
	public int getId() {
		return id;
	}
	
	//Returns if the player is host or not
	public boolean isHost() {
		return host;
	}

	public IoConnector getConnector() {
		return connector;
	}
	
	public IoSession getSession() {
		return conSession;
	}
}


