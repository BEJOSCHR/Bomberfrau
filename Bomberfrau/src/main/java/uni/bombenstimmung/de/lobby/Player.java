package uni.bombenstimmung.de.lobby;


import uni.bombenstimmung.de.backend.console.ConsoleHandler;
import uni.bombenstimmung.de.backend.console.MessageType;
import uni.bombenstimmung.de.backend.images.ImageHandler;
import uni.bombenstimmung.de.backend.images.ImageType;
import uni.bombenstimmung.de.backend.images.LoadedImage;

import javax.swing.*;


public class Player {
	private String ip;
	private String name;
	static int idZaehler = 0;
	private int id;
	private int skin = 0;
	private boolean isHost;
	private boolean isReady = false;
	
	public LoadedImage skinSelection[] = new LoadedImage[3];
	public int zaehlerSkinSelection = 0;

	
	// Konstruktor mit IP
	public Player(String name, String ip) {
		this.ip = ip;
		this.name = name;
		id = idZaehler;
		idZaehler ++;
		isHost = false;
		ConsoleHandler.print("Created Player. ID: " + id + ", Name: " + name, MessageType.LOBBY);
		initializeImages();
		
	}
	// Konstruktor ohne IP. Also nur den namen // Hier handelt es sich um den Host
	public Player(String name) {
		this.name = name;
		id = idZaehler;
		idZaehler ++;
		isHost = true;
		ConsoleHandler.print("Created Player. ID: " + id + ", Name: " + name, MessageType.LOBBY);
		//Lobby_Create lobby = new Lobby_Create(nameOfPlayer);
		initializeImages();
	}
	
	public void initializeImages() {
		skinSelection[0] = ImageHandler.getImage(ImageType.IMAGE_LOBBY_SKINSELECTION_PLATZHALTER_1);
		skinSelection[1] = ImageHandler.getImage(ImageType.IMAGE_LOBBY_SKINSELECTION_PLATZHALTER_2);
		skinSelection[2] = ImageHandler.getImage(ImageType.IMAGE_LOBBY_SKINSELECTION_PLATZHALTER_3);
	}

	
	
	// Skin Zaehler
	public void setIncrementSkin() {
		zaehlerSkinSelection = (zaehlerSkinSelection + 1)%3;
	}
	public void setDecrementSkin() {
		if (zaehlerSkinSelection == 0) {
			zaehlerSkinSelection = 2;
		}
		else {
			zaehlerSkinSelection = (zaehlerSkinSelection - 1)%3;	
		}
	}
	public int getSkin() {
		return zaehlerSkinSelection;
	}
    
	// To change the name in the Lobby
	public void setName(String name) {
		this.name = name;
	}
	
    public String getName() {
    	return name;
    }
    
    public int getId() {
    	return id;
    }
    public boolean getisHost() {
    	return isHost;
    }
    public void setisReady() {
    	if (isReady == false)
    		isReady = true;
    	else if (isReady == true)
    		isReady = false;
    }
    public boolean getisReady() {
    	return isReady;
    }
    
    @Override public String toString() {
    	return "Player ID: " + id + " Name: " + name + " IP-Adress: " + ip;
    }
}