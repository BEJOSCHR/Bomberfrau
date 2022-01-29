/*
 * LobbyCreate
 *
 * Version 1.0
 * Author: Josias
 *
 * Die Lobby als Host oder als client erstellt, wo alle Player, die sich in der MINA Connection befinden, zusaetzlich als lokales Abbild abgespeichert werden.
 */

package uni.bombenstimmung.de.lobby;

import java.awt.Color;
import java.awt.Graphics;

import uni.bombenstimmung.de.backend.animation.AnimationData;
import uni.bombenstimmung.de.backend.console.ConsoleHandler;
import uni.bombenstimmung.de.backend.console.MessageType;
import uni.bombenstimmung.de.backend.graphics.GraphicsHandler;
import uni.bombenstimmung.de.backend.images.ImageHandler;
import uni.bombenstimmung.de.backend.images.ImageType;
import uni.bombenstimmung.de.backend.images.LoadedImage;
import uni.bombenstimmung.de.backend.language.LanguageBlockType;
import uni.bombenstimmung.de.backend.language.LanguageHandler;
import uni.bombenstimmung.de.backend.serverconnection.host.ConnectedClient;
import uni.bombenstimmung.de.menu.Panes;
import uni.bombenstimmung.de.menu.Settings;

public class LobbyCreate {

	public static LobbyPlayer player[] = new LobbyPlayer[4];
	static LoadedImage mapSelection[] = new LoadedImage[3];
	static int zaehlerMapSelection = 0;
	static int hochRunterNavigation = 0;
	public static int numberOfMaxPlayers = 0;
	public static ConnectedClient client;

	/**
	 * Wird aufgerufen, wenn der Host die Lobby erstellt.
	 * @param player	Ein Objekt der Klasse LobbyPlayer. Wird in dem GraphicsHandler erstellt und uebergeben.
	 * @param isHost	Dieser Parameter wird nur verwendet, um 2 unterschiedliche Konstruktor zu erstellen.
	 * @param fromAfterGame Ist wichtig um eine Unterscheidung zwischen den Wechsel vom Menu in die Lobby und den
	 * 			Wechsel vom AfterGame in die Lobby zu haben. Vom Aftergame ist eine neue Connection
	 * 			nicht noetig.
	 */
	public LobbyCreate (LobbyPlayer player, boolean isHost, boolean fromAfterGame) {
	    	// Also von dem Menu aufgerufen wird
	    	if(fromAfterGame == false) {
	    	    client = new ConnectedClient(true, null);
	    	}
		// Host wird als player, der die selbe ID hat wie im ConnectedClient im Array gespeichert.
		LobbyCreate.player[client.getId()] = player;
		LobbyCreate.player[client.getId()].setId(client.getId());
		numberOfMaxPlayers++;
		initializeImages();
	}
	
	/**
	 * Wird aufgerufen, wenn ein Client dem Server beitritt. 
	 * @param player	Ein Objekt der Klasse LobbyPlayer. Wird in dem GraphicsHandler erstellt und uebergeben.
	 * @param fromAfterGame Ist wichtig um eine Unterscheidung zwischen den Wechsel vom Menu in die Lobby und den
	 * 			Wechsel vom AfterGame in die Lobby zu haben. Vom Aftergame ist eine neue Connection
	 * 			nicht noetig.
	 */
	public LobbyCreate (LobbyPlayer player, boolean fromAfterGame) {
	    // Also von dem Menu aufgerufen wird
	    if(fromAfterGame == false) {
		try {
		    client = new ConnectedClient(false, Settings.getIp());
		    Thread.sleep(300);
		} catch (Exception e) {
		    e.printStackTrace();
		}
	    }
	    
	    // Eigentlich sollte der gejointe Player nie Host sein!
	    if (client.getId() != -1) {
		client.sendMessage(client.getSession(), "501-" + client.getId() + "-" + player.getName() + "-" + player.getisHost());
		initializeImages();
	    }
	    else {
		ConsoleHandler.print("There is no reachable server, switching back to Lobby ...", MessageType.BACKEND);
		Panes.infoPane(null, LanguageHandler.getLLB(LanguageBlockType.LB_LOBBY_FULL).getContent(), "OK");
		GraphicsHandler.switchToMenuFromLobby();
		if (client.getSession() != null) {
		    client.getConnector().dispose();
		}
	    }
	}
	
	
	/**
	 * Wird aufgerufen, wenn der Host seinen Clients mitteilt, dass ein neuer Player der Lobby gejoint ist, 
	 * sodass diese auf ihrem lokalen Rechner diese darstellen koennen.
	 * Diese Methode muss natuerlich vorher auch vom Host aufgerufen werden, damit dieser auch lokal die Player speichert.
	 * @param id		Die ID von zu erstellenden Player (Muss auch der ID des Players im ConnectedClient client entsprechen)
	 * @param name		Der Name des zu erstellenden Players
	 * @param isHost	Boolean ob der zu erstellende Player Host ist (Ist auch im ConnectedClient client).
	 * @param mapNr		Die aktuelle Auswahl der Map des Players
	 * @param skinNr	Die aktuelle Auswahl des Skin des Players
	 */
	public static void addPlayer(String id, String name, String isHost, String mapNr, String skinNr) {
		// Ein weiterer Player wird als naechstes im Array erstellt
	    	LobbyCreate.player[Integer.parseInt(id)] = new LobbyPlayer(name, "");
		LobbyCreate.player[Integer.parseInt(id)].setId(Integer.parseInt(id));
		LobbyCreate.player[Integer.parseInt(id)].setisHost(Boolean.parseBoolean(isHost));
		// Skin und Map Nummern werden gesettet
		if (mapNr != "" && skinNr != "") {
		    LobbyCreate.player[Integer.parseInt(id)].setSkin(Integer.parseInt(skinNr));
		    LobbyCreate.setMap(Integer.parseInt(mapNr));
		}
		if(numberOfMaxPlayers < 4) {
		    numberOfMaxPlayers++;	
		}
	}
	
	/**
	 * Laed alle Images der Mapauswahl in einem Array. Die Mapauswahl wird vom Host gesteuert.
	 */
	public static void initializeImages() {
		LobbyCreate.mapSelection[0] = ImageHandler.getImage(ImageType.IMAGE_LOBBY_MAP_1);
		LobbyCreate.mapSelection[1] = ImageHandler.getImage(ImageType.IMAGE_LOBBY_MAP_2);
		LobbyCreate.mapSelection[2] = ImageHandler.getImage(ImageType.IMAGE_LOBBY_MAP_3);
	}
	
	
	/**
	 * Wenn die naechste Map ausgewaehlt wird, dann wird diese Methode (vom Host) aufgerufen.
	 */
	public static void setIncrementMap() {
		zaehlerMapSelection = (zaehlerMapSelection + 1)%3;
		// Sendet zaehlerMapSelection via Case 507 zu den Clients
		client.sendMessageToAllClients("507-" + zaehlerMapSelection);
	}
	
	
	/**
	 * Wenn die vorherige Map ausgewaehlt wird, dann wird diese Methode aufgerufen.
	 */
	public static void setDecrementMap() {
		zaehlerMapSelection = (zaehlerMapSelection +2)%3;
		// Sendet zaehlerMapSelection via Case 507 zu den Clients
		client.sendMessageToAllClients("507-" + zaehlerMapSelection);
	}
	
	/**
	 * Wird aufgerufen, wenn ein Player (Abbildung des Hosts) erstellt wird und die schon ausgewaehlte Map noch eingestellt werden muss.
	 * @param mapNr		Die Nummer der schon ausgewaehlten Map
	 */
	public static void setMap(int mapNr) {
	    zaehlerMapSelection = mapNr;
	}
	
	/**
	 * Gibt die Mapnummer zurueck
	 * @return Integer zaehlerMapSelection
	 */
	public static int getMap() {
		return zaehlerMapSelection;
	}
	
	/**
	 * Wird aufgerufen, wenn ein Player (Abbildung des Hosts) erstellt wird und die schon ausgewaehlte Map noch eingestellt werden muss.
	 * @param mapNr		Die Nummer der schon ausgewaehlten Map
	 */
	public static void setTimer(int timerNr) {
	    LobbyButtons.setTimer(timerNr);
	}
	
	
	/**
	 * Wenn die naechste Skin ausgewaehlt wird, dann wird diese Methode aufgerufen.
	 * @param player	Integer, welcher Player im Array gemeint ist.
	 */
	public static void setIncrementSkin(int player) {
		LobbyCreate.player[player].setSkin((LobbyCreate.player[player].getSkin() + 1) %4);
		if (client.isHost()) {
		    client.sendMessageToAllClients("509-" + client.getId() + "-" + LobbyCreate.player[player].getSkin());
		}
		else {
		    client.sendMessage(client.getSession(), "508-" + client.getId() + "-" + LobbyCreate.player[player].getSkin());
		}
	}
	
	
	/**
	 * Wenn die vorherige Skin ausgewaehlt wird, dann wird diese Methode aufgerufen.
	 * @param player	Integer, welcher Player im Array gemeint ist.
	 */
	public static void setDecrementSkin(int player) {
	    	LobbyCreate.player[player].setSkin((LobbyCreate.player[player].getSkin() + 3) %4);
		if (client.isHost()) {
		    client.sendMessageToAllClients("509-" + client.getId() + "-" + LobbyCreate.player[player].getSkin());
		}
		else {
		    client.sendMessage(client.getSession(), "508-" + client.getId() + "-" + LobbyCreate.player[player].getSkin());
		}
	}
	
	/**
	* Wird aufgerufen, wenn ein Player erstellt wird und der schon ausgewaehlte Skin noch eingestellt werden muss.
	* @param player		Integer - Die Nummer des Players im Array LobbyCreate.player[]
	* @param skin		Integer - Die Nummer der schon ausgewaehlten Skin
	*/
	public static void setSkin(int player, int skinNr) {
	    LobbyCreate.player[player].setSkin(skinNr);
	}
	
	/**
	 * Veraendert den Status, ob der Player ready ist, sodass der Host das Spiel starten kann.
	 * Wird von der Checkbox in LobbyButtons aufgerufen, wo alle Player (ausser dem Host) den Button klicken koennen.
	 * @param player	Integer - Die Nummer des Players im Array LobbyCreate.player[]
	 */
	public static void setisReady(int player) {
	    LobbyCreate.player[player].changeisReady();
	    client.sendMessage(client.getSession(), "510-" + player + "-" + LobbyCreate.player[player].getisReady());
	}
	
	
	
	/**
	 * Ist fuer die Navigation mit Pfeiltasten gedacht, dass jede Auswahl (Skin, Map, Start, Lobby verlassen)
	 * mit den Hoch-Runter Pfeiltasten gesteuert werden kann.
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
	 *  numberOfMaxPlayers wird neu gesettet, was fuer neu erstellte clients hilfreich ist.
	 */
	public static void setNumberPlayer(int numberPlayers) {
	    // Wird von case506 aufgerufen, wo die numberPlayer uebernommen wird.
	    numberOfMaxPlayers = numberPlayers;
	}

	/**
	 * Wird von Label.java aufgerufen und drawed alles ausser die Buttons.
	 */
	public static void drawScreen(Graphics g) {
		//Fuer die HintergrundFarbe
		g.setColor(Color.PINK);
		g.fillRect(0, 0, GraphicsHandler.getWidth(), GraphicsHandler.getHeight());
		GraphicsHandler.drawCentralisedText(g, Color.WHITE, Settings.scaleValue(100), "LOBBY", GraphicsHandler.getWidth()/2, (int)(GraphicsHandler.getHeight()*0.05));
		
		if (client.isHost()) {
		    GraphicsHandler.drawLeftText(g, Color.WHITE, Settings.scaleValue(36), LanguageHandler.getLLB(LanguageBlockType.LB_LOBBY_IP).getContent() + client.hostGetPublicIP(),
			    (int)(GraphicsHandler.getWidth()/8 - (Settings.scaleValue(250)/2)), (int)(GraphicsHandler.getHeight()*0.05));
		}
		
		for(int i=0; i < numberOfMaxPlayers; i++) {
		    if (player[i] != null) {
			// Malt die fuer den Host zustaendigen Sachen. 
			if (player[i].getisHost() == true) {
				g.drawImage(ImageHandler.getImage(ImageType.IMAGE_LOBBY_CROWN).getImage(), GraphicsHandler.getWidth()/8*(2*i+1) - Settings.scaleValue(25), 
					(int)(GraphicsHandler.getHeight()*0.13), Settings.scaleValue(50), Settings.scaleValue(50), null);
				g.drawImage(LobbyCreate.mapSelection[zaehlerMapSelection].getImage(), GraphicsHandler.getWidth()/8*(2*i+1)- Settings.scaleValue(125), 
					(int)(GraphicsHandler.getHeight()*0.5), Settings.scaleValue(250), Settings.scaleValue(250), null);
				String show = "" + LobbyButtons.getTimer();
				if (show.equals("0")) show = LanguageHandler.getLLB(LanguageBlockType.LB_LOBBY_ENDLESS_TIME).getContent();
				GraphicsHandler.drawCentralisedText(g, Color.WHITE, Settings.scaleValue(96-4*show.length()), show, GraphicsHandler.getWidth()/8*(2*i+1), 
				    (int)(GraphicsHandler.getHeight()*0.808));
			}
			// Loest den viel zu grossen Namen, bei einem Namen unter 3 Zeichen
			if(player[i].getName().length() < 3) {
			    GraphicsHandler.drawCentralisedText(g, Color.WHITE, Settings.scaleValue(100), player[i].getName(), GraphicsHandler.getWidth()/8*(2*i+1), (int)(GraphicsHandler.getHeight()*0.2));    
			} else {
			    GraphicsHandler.drawCentralisedText(g, Color.WHITE, Settings.scaleValue(300/player[i].getName().length()), player[i].getName(), 
				    GraphicsHandler.getWidth()/8*(2*i+1), (int)(GraphicsHandler.getHeight()*0.2));    
			}

			try {

			g.drawImage(LobbyPlayer.skinSelection[player[i].getSkin()][AnimationData.lobby_walk].getImage(), GraphicsHandler.getWidth()/8*(2*i+1) - Settings.scaleValue(125), (int)(GraphicsHandler.getHeight()*0.28),
				Settings.scaleValue(250), Settings.scaleValue(250), null);
			
			if (player[i].getisHost() == false) {
			    GraphicsHandler.drawCentralisedText(g, Color.WHITE, Settings.scaleValue(50), LanguageHandler.getLLB(LanguageBlockType.LB_LOBBY_READY).getContent(), 
				    GraphicsHandler.getWidth()/8*(2*i+1), (int)(GraphicsHandler.getHeight()*0.65));  
			}
			} catch(Exception e) {
			    e.printStackTrace();
			}
		}
		}
	}
}
