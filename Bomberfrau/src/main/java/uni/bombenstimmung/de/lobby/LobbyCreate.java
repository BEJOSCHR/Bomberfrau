/*
 * LobbyCreate
 *
 * Version 1.0
 * Author: Josias
 *
 * Die Lobby wird samt Host bzw. Playern erstellt.
 */

package uni.bombenstimmung.de.lobby;

import java.awt.Color;
import java.awt.Graphics;

import org.apache.mina.core.session.IoSession;

import uni.bombenstimmung.de.backend.console.ConsoleHandler;
import uni.bombenstimmung.de.backend.console.MessageType;
import uni.bombenstimmung.de.backend.graphics.GraphicsHandler;
import uni.bombenstimmung.de.backend.images.ImageHandler;
import uni.bombenstimmung.de.backend.images.ImageType;
import uni.bombenstimmung.de.backend.images.LoadedImage;
import uni.bombenstimmung.de.backend.serverconnection.client.ClientHandler;
import uni.bombenstimmung.de.backend.serverconnection.host.ConnectedClient;

public class LobbyCreate {

	public static LobbyPlayer player[] = new LobbyPlayer[4];
	static LoadedImage mapSelection[] = new LoadedImage[3];
	static int zaehlerMapSelection = 0;
	static int hochRunterNavigation = 0;
	public static int numberPlayer = 0;
	public static ConnectedClient client;
	
	
	/**
	 * Wird aufgerufen, wenn der Host die Lobby erstellt.
	 * @param player	Ein Objekt der Klasse LobbyPlayer. Wird in dem GraphicsHandler erstellt und uebergeben.
	 */
	// Host wird als player 0 im Array gespeichert.
	public LobbyCreate (LobbyPlayer player, boolean isHost) {
		client = new ConnectedClient(true, null);
		this.player[client.getId()] = player;
		this.player[client.getId()].setId(client.getId());
		numberPlayer++;
		initializeImages();
//		connectedClient[0] = new ConnectedClient(true, null);
	}
	
	/**
	 * Wird aufgerufen, wenn ein Player der Lobby beitritt. 
	 * @param player	Ein Objekt der Klasse LobbyPlayer. Wird in dem GraphicsHandler erstellt und uebergeben.
	 */
	public LobbyCreate (LobbyPlayer player) {
	    
	    try {
		client = new ConnectedClient(false, "127.0.0.1"); // Noch aendern, damit die IP von der Main gezogen wird: Settings.getIp() oder so
		Thread.sleep(500);
	    }
	    catch (Exception e) {
		e.printStackTrace();
	    }

		client.sendMessage(client.getSession(), "501" + "-" + "binGeJoined" + "-" + client.getId() + "-" + player.getName());
	}
	
	
	
	
	/**
	 * Wird aufgerufen, wenn der Host seinen Clients mitteilt, dass ein neuer Player der Lobby gejoint ist, sodass diese auf ihrem lokalen Rechner diese darstellen können.
	 * @param player	Ein Objekt der Klasse LobbyPlayer. Wird in dem GraphicsHandler erstellt und uebergeben.
	 */
	// Ein weiterer Player wird als nächstes im Array erstellt
	public static void addPlayer(String numberPlayers, String id, String name) {
	    	LobbyCreate.player[Integer.parseInt(numberPlayers)] = new LobbyPlayer(name, "");
		LobbyCreate.player[Integer.parseInt(numberPlayers)].setId(Integer.parseInt(id));

		if(client.isHost()) {
		    numberPlayer++;	
		    ConsoleHandler.print("Host hat das aufgerufen:" + client.getId(), MessageType.LOBBY);
		    
		}
		else {
		    numberPlayer = Integer.parseInt(numberPlayers);
		    ConsoleHandler.print("Client hat das aufgerufen" + client.getId(), MessageType.LOBBY);
		}
		initializeImages();
		ConsoleHandler.print("numberOlayer: " + numberPlayer + "Alle Player jetz: ", null);
	}

	
	/**
	 * Laed alle Images der Mapauswahl in einem Array. Die Mapauswahl wird vom Host gesteuert.
	 */
	public static void initializeImages() {
		LobbyCreate.mapSelection[0] = ImageHandler.getImage(ImageType.IMAGE_LOBBY_MAPSELECTION_PLATZHALTER_1);
		LobbyCreate.mapSelection[1] = ImageHandler.getImage(ImageType.IMAGE_LOBBY_MAPSELECTION_PLATZHALTER_2);
		LobbyCreate.mapSelection[2] = ImageHandler.getImage(ImageType.IMAGE_LOBBY_MAPSELECTION_PLATZHALTER_3);
	}
	
	/**
	 * Gibt die toString der player aus
	 */
	public static void printPlayers() {
			for(int i=0; i < numberPlayer; i++) {
				ConsoleHandler.print(player[i].toString(), MessageType.LOBBY); 
			}
		}
	
	/**
	 * Wenn die naechste Map ausgewaehlt wird, dann wird diese Methode aufgerufen.
	 */
	// Map Zaehler
	public static void setIncrementMap() {
		zaehlerMapSelection = (zaehlerMapSelection + 1)%3;
	}
	
	/**
	 * Wenn die vorherige Map ausgewaehlt wird, dann wird diese Methode aufgerufen.
	 */
	public static void setDecrementMap() {
		if (zaehlerMapSelection == 0) {
			zaehlerMapSelection = 2;
		}
		else {
			zaehlerMapSelection = (zaehlerMapSelection - 1)%3;	
		}
	}
	/**
	 * Gibt die Mapnummer zurück
	 * @return Integer zaehlerMapSelection
	 */
	public static int getMap() {
		return zaehlerMapSelection;
	}
	
	/**
	 * Ist fuer die Navigation mit Pfeiltasten gedacht, dass jede Auswahl (Skin, Map, Start, Lobby verlassen) mit den Hoch-Runter Pfeiltasten gesteuert werden kann.
	 * Diese Methode wird aufgerufen, wenn die naechste Auswahl (unten) mit den Pfeiltasten ausgewaehlt wird.
	 */
	public static void setIncrementHochRunterNavigation() {
		hochRunterNavigation = (hochRunterNavigation + 1)%3;
	}
	/**
	 * Diese Methode wird aufgerufen, wenn die vorherige Auswahl (oben) mit den Pfeiltasten ausgewaehlt wird.
	 */
	public static void setDecrementHochRunterNavigation() {
		if (hochRunterNavigation == 0) {
			hochRunterNavigation = 2;
		}
		else {
			hochRunterNavigation = (hochRunterNavigation - 1)%3;	
		}
	}
	
	/**
	 * Gibt die Auswahlnummer der Pfeiltasten zurück
	 */
	public static int getHochRunterNavigation() {
		return hochRunterNavigation;
	}
	
	/**
	 * Gibt die mittige X Koordinate in Abhaengigkeit der Player wieder.
	 * Fuer den Parameter int i wird die Anzahl der Player benötigt
	 */
	public static int getXValueForDraw(int i) {
		if (numberPlayer != 0)
			// Dieser Wert, damit der Screen sich variabel für die Anzahl der Player verändert. Die Buttons sind dann aber halt blöd
//			return (int)(((GraphicsHandler.getWidth()/numberPlayer)/2) + ((GraphicsHandler.getWidth()/numberPlayer)/2)*i*2);
			return (int)(((GraphicsHandler.getWidth()/4)/2) + ((GraphicsHandler.getWidth()/4)/2)*i*2);
		else
			return 1;
	}
	/**
	 * Gibt die Nummer des Hosts im Array wieder. (Wird meistens 0 sein)
	 */
	public static int getHost() {
		int host = 0;
		for(int i=0; i < numberPlayer; i++) {
			if (player[i].getisHost() == true) {
				host = i;
			}
		}
		return host;
	}
	public static void setNumberPlayer(int numberPlayers) {
	    numberPlayer = numberPlayers;
	}

	/**
	 * Wird von Label.java aufgerufen und drawed alles ausser die Buttons.
	 */
	public static void drawScreen(Graphics g) {
		//Fuer die HintergrundFarbe
		g.setColor(Color.PINK);
		g.fillRect(0, 0, GraphicsHandler.getWidth(), GraphicsHandler.getHeight());
		
		GraphicsHandler.drawCentralisedText(g, Color.WHITE, 50, "LOBBY", GraphicsHandler.getWidth()/2, (int)(GraphicsHandler.getHeight()*0.05));
//		try {
//		Thread.sleep(3000);
//		}catch(Exception e) {
//		    e.printStackTrace();
//		}
		
		for(int i=0; i < numberPlayer; i++) {
			if (player[i].getisHost() == true) {
				g.drawImage(ImageHandler.getImage(ImageType.IMAGE_LOBBY_CROWN).getImage(), (int)(getXValueForDraw(i)-12.5), (int)(GraphicsHandler.getHeight()*0.15)- 50, null);
				g.drawImage(LobbyCreate.mapSelection[zaehlerMapSelection].getImage(), getXValueForDraw(i)-100, (int)(GraphicsHandler.getHeight()*0.15) + (int)((GraphicsHandler.getHeight()*0.1)*3.5), null);
			}
			
			GraphicsHandler.drawCentralisedText(g, Color.WHITE, 30, player[i].getName(), getXValueForDraw(i), (int)(GraphicsHandler.getHeight()*0.15)+35);

			g.drawImage(player[i].skinSelection[player[i].getSkin()].getImage(), getXValueForDraw(i)-100, (int)(GraphicsHandler.getHeight()*0.15) + (int)(GraphicsHandler.getHeight()*0.1), null);
			
//			GraphicsHandler.drawCentralisedText(g, Color.WHITE, 30, player[i].toString(), GraphicsHandler.getWidth()/4, GraphicsHandler.getHeight()/4 + 40*i);
		}
		
	}

	
	
}
