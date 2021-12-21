package uni.bombenstimmung.de.lobby;

import java.awt.Color;
import java.awt.Graphics;

import uni.bombenstimmung.de.backend.console.ConsoleHandler;
import uni.bombenstimmung.de.backend.console.MessageType;
import uni.bombenstimmung.de.backend.graphics.GraphicsHandler;
import uni.bombenstimmung.de.backend.images.ImageHandler;
import uni.bombenstimmung.de.backend.images.ImageType;
import uni.bombenstimmung.de.backend.images.LoadedImage;
import uni.bombenstimmung.de.backend.language.LanguageBlockType;
import uni.bombenstimmung.de.backend.language.LanguageHandler;

public class Lobby_Create {

	static Player player[] = new Player[4];
	static LoadedImage mapSelection[] = new LoadedImage[3];
	static LoadedImage skinSelection[] = new LoadedImage[3];

	static int zaehlerMapSelection = 0;
	
	static int zaehlerSkinSelection = 0;
	
	static int hochRunterNavigation = 0;
	
	static int numberPlayer = 0;
	
	
	// Host wird als player 0 im Array gespeichert.
	public Lobby_Create (Player player){
		Lobby_Create.player[numberPlayer] = player;
		numberPlayer++;
		initializeImages();

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
	
	public void initializeImages() {
		Lobby_Create.mapSelection[0] = ImageHandler.getImage(ImageType.IMAGE_LOBBY_MAPSELECTION_PLATZHALTER_1);
		Lobby_Create.mapSelection[1] = ImageHandler.getImage(ImageType.IMAGE_LOBBY_MAPSELECTION_PLATZHALTER_2);
		Lobby_Create.mapSelection[2] = ImageHandler.getImage(ImageType.IMAGE_LOBBY_MAPSELECTION_PLATZHALTER_3);
		
		Lobby_Create.skinSelection[0] = ImageHandler.getImage(ImageType.IMAGE_LOBBY_SKINSELECTION_PLATZHALTER_1);
		Lobby_Create.skinSelection[1] = ImageHandler.getImage(ImageType.IMAGE_LOBBY_SKINSELECTION_PLATZHALTER_2);
		Lobby_Create.skinSelection[2] = ImageHandler.getImage(ImageType.IMAGE_LOBBY_SKINSELECTION_PLATZHALTER_3);
	}
	
	// die toString wird von allen Playern im Array ausgegeben!
	public static void printPlayers() {
			for(int i=0; i < numberPlayer; i++) {
				ConsoleHandler.print(player[i].toString(), MessageType.LOBBY); 
			}
		}
	
	// Map Zaehler
	public static void setIncrementMap() {
		zaehlerMapSelection = (zaehlerMapSelection + 1)%3;
	}
	public static void setDecrementMap() {
		if (zaehlerMapSelection == 0) {
			zaehlerMapSelection = 2;
		}
		else {
			zaehlerMapSelection = (zaehlerMapSelection - 1)%3;	
		}
	}
	public static int getMap() {
		return zaehlerMapSelection;
	}
	
	// Skin Zaehler
	public static void setIncrementSkin() {
		zaehlerSkinSelection = (zaehlerSkinSelection + 1)%3;
	}
	public static void setDecrementSkin() {
		if (zaehlerSkinSelection == 0) {
			zaehlerSkinSelection = 2;
		}
		else {
			zaehlerSkinSelection = (zaehlerSkinSelection - 1)%3;	
		}
	}
	public static int getSkin() {
		return zaehlerSkinSelection;
	}
	
	public static void setIncrementHochRunterNavigation() {
		hochRunterNavigation = (hochRunterNavigation + 1)%3;
	}
	public static void setDecrementHochRunterNavigation() {
		if (hochRunterNavigation == 0) {
			hochRunterNavigation = 2;
		}
		else {
			hochRunterNavigation = (hochRunterNavigation - 1)%3;	
		}
	}
	public static int getHochRunterNavigation() {
		return hochRunterNavigation;
	}

	
	// Wird von Label.java aufgerufen und malt alles was ich haben will. Daher auch static, sodass ich davon nicht extra ein Objekt erzeugen muss
	public static void drawScreen(Graphics g) {
		//Für die HintergrundFarbe
		g.setColor(Color.PINK);
		g.fillRect(0, 0, GraphicsHandler.getWidth(), GraphicsHandler.getHeight());
		
		GraphicsHandler.drawCentralisedText(g, Color.WHITE, 50, "LOBBY", GraphicsHandler.getWidth()/2, GraphicsHandler.getHeight()/6);
		
		GraphicsHandler.drawCentralisedText(g, Color.WHITE, 50, LanguageHandler.getLLB(LanguageBlockType.LB_LOBBY_TEST).getContent(), GraphicsHandler.getWidth()/2, GraphicsHandler.getHeight());
		
		
		
		for(int i=0; i < numberPlayer; i++) {
			if (player[i].getisHost() == true) {
				g.drawImage(ImageHandler.getImage(ImageType.IMAGE_LOBBY_CROWN).getImage(), (GraphicsHandler.getWidth()/8) + (GraphicsHandler.getWidth()/8)*(i*2), GraphicsHandler.getHeight()/4 - 50, null);
				g.drawImage(Lobby_Create.mapSelection[zaehlerMapSelection].getImage(), (GraphicsHandler.getWidth()/8) + (GraphicsHandler.getWidth()/8)*(i*2), GraphicsHandler.getHeight()/4 + GraphicsHandler.getHeight()/5, null);
			}
			GraphicsHandler.drawCentralisedText(g, Color.WHITE, 30, player[i].getName(), (GraphicsHandler.getWidth()/8) + (GraphicsHandler.getWidth()/8)*(i*2), GraphicsHandler.getHeight()/4);
			
			g.drawImage(Lobby_Create.skinSelection[zaehlerSkinSelection].getImage(), (GraphicsHandler.getWidth()/8) + (GraphicsHandler.getWidth()/8)*(i*2), GraphicsHandler.getHeight()/4 + (GraphicsHandler.getHeight()/5)*2, null);
//			GraphicsHandler.drawCentralisedText(g, Color.WHITE, 30, player[i].toString(), GraphicsHandler.getWidth()/4, GraphicsHandler.getHeight()/4 + 40*i);
		}
		//Wurde ersetzt in dem MouseActionAreaHandler -> Da der Button aufblinken soll
//		g.drawImage(Lobby_Create.image[zaehlerMapSelection].getImage(), GraphicsHandler.getWidth()/3, GraphicsHandler.getHeight()/2, null);
		
		// Einfach feste Koordinaten verwendet
//		if (LobbyMovement.getTimerZaehler() != 0) {
//			g.drawImage(ImageHandler.getImage(ImageType.IMAGE_LOBBY_ARROW_RIGHT_BIGGER).getImage(), GraphicsHandler.getWidth()/3, GraphicsHandler.getHeight()/2-3, null);
//		}
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
