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
	public ConnectedClient(boolean sHost, int sId) {
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
				ConsoleHandler.print("UDP Server started at "+ConnectionData.IP+":"+ConnectionData.PORT);
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
			
		//Is the new created Client not the host, a new UDP Client will be initialized
		else {
			connector = new NioDatagramConnector();
			//connector.setHandler(this);
			connector.setHandler(new ClientHandler(this));
			connector.getFilterChain().addLast("codec", new ProtocolCodecFilter(new TextLineCodecFactory(Charset.forName("UTF-8"))));
			ConnectFuture connFuture = connector.connect(new InetSocketAddress(ConnectionData.IP, ConnectionData.PORT));
			ConsoleHandler.print("UDP Client created");
			connFuture.addListener(new IoFutureListener() {
				public void operationComplete(IoFuture future) {
					ConnectFuture connFuture = (ConnectFuture) future;
					if (connFuture.isConnected()) {
						conSession = future.getSession();
						try {
							sendMessage(conSession);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					} else {
							ConsoleHandler.print("Client is not connected to the server....exiting");
						}
					}
				});
		}
	}
	
	//Sends a message to the connected Client
	private void sendMessage(IoSession session) throws InterruptedException {
		for (int i = 0; i < 0; i++) {
			String message = "Testnachricht"+i+" von ClientID: " + this.id;
			session.write(message);
			Thread.sleep(200);
		}
	}
	
	//Sends a message to all connected Clients
	public void sendMessageToAllClients(String message) {
		acceptor.broadcast(message);
	}
	
	void printMessage(Object message) {
		ConsoleHandler.print(message.toString());
	}
	
	//Add a new client with the corresponding remote address to the Hash Map
	public void addClientToList(SocketAddress remoteAddress) {
		if (!containsClient(remoteAddress)) {
			connectedClients.put(remoteAddress, id = connectedClients.size()+1);
			ConsoleHandler.print("Client added to List");
		}
	} 
		
	//Prints out the elements inside the Hash Map
	public void printConnectedClients() {
		connectedClients.forEach((k,v)-> ConsoleHandler.print(k+"="+v));
	}
	
	//Checks if the Hash Map already contains a client with the remote address
	public boolean containsClient(SocketAddress remoteAddress) {
		return connectedClients.contains(remoteAddress);
	}
	
	//Removes the client from the Hash Map
	public void removeClient(SocketAddress remoteAdress) {
		connectedClients.remove(remoteAdress);
	}
	
	public int getId() {
		return id;
	}
	
	public boolean isHost() {
		return host;
	}
}


