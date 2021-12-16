package uni.bombenstimmung.de.lobby;

import java.util.Enumeration;

import uni.bombenstimmung.de.backend.console.ConsoleHandler;
import uni.bombenstimmung.de.backend.console.MessageType;
import uni.bombenstimmung.de.backend.graphics.GraphicsHandler;
import uni.bombenstimmung.de.backend.language.LanguageBlockType;
import uni.bombenstimmung.de.backend.language.LanguageHandler;

//Java program to find IP address of your computer
//java.net.InetAddress class provides method to get
//IP of any host name
import java.net.*;
import java.awt.Color;
import java.awt.Graphics;
import java.io.*;
//import java.util.*;
import java.net.InetAddress;

public class Lobby_Create {

	Player player[] = new Player[3];
	
	// Host wird als player 0 im Array gespeichert.
	public Lobby_Create (Player player){
		this.player[0] = player;
//		 //FindIP-Adress
//		try {
//			findIP_Adress();
//		}
//		catch(Exception e) {
//			ConsoleHandler.print("FindIP has failed", MessageType.LOBBY);
//		}
	}
	
	// Ein weiterer Player wird als nächstes im Array ausgegeben
	public void addPlayer(Player playerN) {
		int i = 0;
		while (player[i] != null) {
			i++;
		}
		player[i] = playerN;
		printPlayers();
			
	}
	
	// die toString wird von allen Playern im Array ausgegeben
	public void printPlayers() {
		int i = 0;
		while(player[i] != null) {
			ConsoleHandler.print(player[i].toString(), MessageType.LOBBY); 
			i++;
		}
	}
	

	
	// Wird von Label.java aufgerufen und malt alles was ich haben will. Daher auch static, sodass ich davon nicht extra ein Objekt erzeugen muss
	public static void drawScreen(Graphics g) {
		GraphicsHandler.drawCentralisedText(g, Color.WHITE, 50, LanguageHandler.getLLB(LanguageBlockType.LB_LOBBY_TEST).getContent(), GraphicsHandler.getWidth()/2, GraphicsHandler.getHeight()-20);
		GraphicsHandler.drawCentralisedText(g, Color.WHITE, 10, player[0].name, GraphicsHandler.getWidth()/5, GraphicsHandler.getHeight()/4);
	}
	
	
//	
//	// https://stackoverflow.com/questions/8083479/java-getting-my-ip-address
//	public void findIP_Adress() throws Exception {
//	    String ip;
//	    try {
//	        Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
//	        while (interfaces.hasMoreElements()) {
//	            NetworkInterface iface = interfaces.nextElement();
//	            // filters out 127.0.0.1 and inactive interfaces
//	            if (iface.isLoopback() || !iface.isUp())
//	                continue;
//
//	            Enumeration<InetAddress> addresses = iface.getInetAddresses();
//	            while(addresses.hasMoreElements()) {
//	                InetAddress addr = addresses.nextElement();
//	                ip = addr.getHostAddress();
//	                System.out.println(iface.getDisplayName() + " " + ip);
//	            }
//	        }
//	    } catch (SocketException e) {
//	        throw new RuntimeException(e);
//	    }
//		
//	}
	

	
	
}
