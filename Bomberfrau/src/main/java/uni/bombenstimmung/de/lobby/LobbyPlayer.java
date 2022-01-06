/*
 * Player
 *
 * Version 1.0
 * Author: Josias
 *
 * Diese Klasse enthaelt Funktionen, die fuer die Auswahl der Skins jedes Players (Host ist auch ein Player) benoetigt werden,
 * sowie getter- und settermethoden fuer benoetigte Informationen.
 */
package uni.bombenstimmung.de.lobby;


import uni.bombenstimmung.de.backend.console.ConsoleHandler;
import uni.bombenstimmung.de.backend.console.MessageType;
import uni.bombenstimmung.de.backend.images.ImageHandler;
import uni.bombenstimmung.de.backend.images.ImageType;
import uni.bombenstimmung.de.backend.images.LoadedImage;
import uni.bombenstimmung.de.backend.serverconnection.host.ConnectedClient;


public class LobbyPlayer {
	private String ip;
	private String name;
	private int id;
	private boolean isHost;
	private boolean isReady = false;
	public LoadedImage skinSelection[] = new LoadedImage[3];
	private int zaehlerSkinSelection = 0;
	private ConnectedClient connectedClient;

	
	/**
	 * Konstruktor, wo die IP Adresse auch uebergeben wird. Wird vom zu erstellenden HOST aufgerufen.
	 */
	public LobbyPlayer(String name) {
		this.name = name;

		isHost = true;
		initializeImages();
	}
	
	/**
	 * Konstruktor, wo die IP Adresse auch uebergeben wird. Wird von zu erstellenden PLAYERN aufgerufen.
	 */
	public LobbyPlayer(String name, String ip) {
		this.name = name;
		this.ip = ip;
		isHost = false;
		ConsoleHandler.print("Created Player. ID: " + id + ", Name: " + name, MessageType.LOBBY);
		initializeImages();
		
	}
	
	/**
	 * Läd alle Images der Skinauswahl in einem Array. Die Skinauswahl wird von dem Player gesteuert.
	 */
	public void initializeImages() {
		skinSelection[0] = ImageHandler.getImage(ImageType.IMAGE_LOBBY_SKINSELECTION_PLATZHALTER_1);
		skinSelection[1] = ImageHandler.getImage(ImageType.IMAGE_LOBBY_SKINSELECTION_PLATZHALTER_2);
		skinSelection[2] = ImageHandler.getImage(ImageType.IMAGE_LOBBY_SKINSELECTION_PLATZHALTER_3);
	}
	
//	public LoadedImage getskinSelection() {
//	    
//	}
	
	
	/**
	 * Wenn die naechste Skin ausgewaehlt wird, dann wird diese Methode aufgerufen.
	 */
	public void setIncrementSkin() {
		zaehlerSkinSelection = (zaehlerSkinSelection + 1)%3;
	}
	/**
	 * Wenn die vorherige Skin ausgewaehlt wird, dann wird diese Methode aufgerufen.
	 */
	public void setDecrementSkin() {
		if (zaehlerSkinSelection == 0) {
			zaehlerSkinSelection = 2;
		}
		else {
			zaehlerSkinSelection = (zaehlerSkinSelection - 1)%3;	
		}
	}
	/**
	 * Gibt die Skinnummer zurueck.
	 */
	public int getSkin() {
		return zaehlerSkinSelection;
	}
	/**
	 * Setzt den Namen
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * Gibt den Namen zurueck.
	 */
    public String getName() {
    	return name;
    }
	/**
	 * Gibt die Player Id zurueck.
	 */
    public int getId() {
    	return id;
    }
    public void setId(int id) {
	this.id = id;
    }
    
    public String getIpAdress() {
    	return ip;
    }
	/**
	 * Gibt zurueck ob der Player der Host ist.
	 */
    public boolean getisHost() {
    	return isHost;
    }
    
    public void setisHost(boolean x) {
	isHost = x;
    }
	/**
	 * Veraendert den Status, ob der Player ready ist, sodass der Host das Spiel starten kann.
	 * Wird von der Checkbox aufgerufen, wo alle Player (ausser dem Host) den Button klicken koennen.
	 */
    public void setisReady() {
    	if (isReady == false) {
    	    isReady = true;
    	    connectedClient.sendMessage(connectedClient.getSession(), "500-ready");
    	}

    	else if (isReady == true)
    		isReady = false;
    }
	/**
	 * Gibt zurueck, ob der Player ready ist.
	 */
    public boolean getisReady() {
    	return isReady;
    }
	/**
	 * Die toString, wo alle wichtigsten Informationen des Players in einem String zurueck gibt.
	 */
    @Override public String toString() {
    	return "Player ID: " + id + " Name: " + name + " IP-Adress: " + ip;
    }
}