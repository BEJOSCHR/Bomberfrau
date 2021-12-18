package uni.bombenstimmung.de.lobby;

import java.util.Enumeration;

import uni.bombenstimmung.de.backend.console.ConsoleHandler;
import uni.bombenstimmung.de.backend.console.MessageType;
import uni.bombenstimmung.de.backend.graphics.GraphicsHandler;
import uni.bombenstimmung.de.backend.images.ImageHandler;
import uni.bombenstimmung.de.backend.images.ImageType;
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

	static Player player[] = new Player[4];
	static int numberPlayer = 0;
	
	// Host wird als player 0 im Array gespeichert.
	public Lobby_Create (Player player){
		Lobby_Create.player[numberPlayer] = player;
		numberPlayer++;
//		 //FindIP-Adress
//		try {
//			findIP_Adress();
//		}
//		catch(Exception e) {
//			ConsoleHandler.print("FindIP has failed", MessageType.LOBBY);
//		}
	}
	
	// Ein weiterer Player wird als nächstes im Array ausgegeben
	public void addPlayer(Player player) {
		Lobby_Create.player[numberPlayer] = player;
		
		numberPlayer++;

			
	}
	
	// die toString wird von allen Playern im Array ausgegeben!
	public static void printPlayers() {
			for(int i=0; i < numberPlayer; i++) {
				ConsoleHandler.print(player[i].toString(), MessageType.LOBBY); 
			}
		}
	

	
	// Wird von Label.java aufgerufen und malt alles was ich haben will. Daher auch static, sodass ich davon nicht extra ein Objekt erzeugen muss
	public static void drawScreen(Graphics g) {
		//Für die HintergrundFarbe
		g.setColor(Color.PINK);
		g.fillRect(0, 0, GraphicsHandler.getWidth(), GraphicsHandler.getHeight());
		
		GraphicsHandler.drawCentralisedText(g, Color.WHITE, 50, LanguageHandler.getLLB(LanguageBlockType.LB_LOBBY_TEST).getContent(), GraphicsHandler.getWidth()/2, GraphicsHandler.getHeight()-20);
		for(int i=0; i < numberPlayer; i++)
			GraphicsHandler.drawCentralisedText(g, Color.WHITE, 30, player[i].toString(), GraphicsHandler.getWidth()/5, GraphicsHandler.getHeight()/4 + 40*i);
		
		//player[0].textfield.paintComponents(g);
		
		//Wurde ersetzt in dem MouseActionAreaHandler -> Da der Button aufblinken soll
		//g.drawImage(ImageHandler.getImage(ImageType.IMAGE_LOBBY_ARROW_LEFT).getImage(), GraphicsHandler.getWidth()/3, GraphicsHandler.getHeight()/2, null);

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
