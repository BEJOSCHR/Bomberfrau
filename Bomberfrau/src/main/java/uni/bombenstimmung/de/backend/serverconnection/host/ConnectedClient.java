/*
 * ConnectedClient
 *
 * Version 1.0
 * Author: Benni
 *
 * Representiert jeweils einen verbundenen Client im ServerHandler
 */
package uni.bombenstimmung.de.backend.serverconnection.host;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.future.IoFuture;
import org.apache.mina.core.future.IoFutureListener;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.service.IoHandler;
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
	
	public ConnectedClient(boolean sHost, int sId) {
		id = sId;
		host = sHost;
		
		//Is the new created Client the host, a new server will be initialized
		if (host == true) {
			NioDatagramAcceptor acceptor = new NioDatagramAcceptor();
			acceptor.setHandler(new ServerHandler(this));
			acceptor.getFilterChain().addLast("codec", new ProtocolCodecFilter(new TextLineCodecFactory(Charset.forName("UTF-8"))));
			//DefaultIoFilterChainBuilder chain = acceptor.getFilterChain();
			//chain.addLast("logger", new LoggingFilter());
			
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
							sendData();
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
	
	
	private void sendData() throws InterruptedException {
		for (int i = 0; i < 100; i++) {
			String message = "Testnachricht"+i+" von " + this.id;
			conSession.write(message);
			Thread.sleep(200);
		}
		//conSession.closeNow();
		//ConsoleHandler.print("Session closed from Client");
	}
	
	
	void printMessage(Object message) {
		System.out.print(message);
	}
	
	public int getId() {
		return id;
	}
	
	public boolean isHost() {
		return host;
	}
	
	
}


