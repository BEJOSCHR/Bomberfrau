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

import uni.bombenstimmung.de.backend.images.ImageHandler;
import uni.bombenstimmung.de.backend.images.ImageType;
import uni.bombenstimmung.de.backend.images.LoadedImage;

public class LobbyPlayer {
    private String ip;
    private String name;
    private int id;
    private boolean isHost;
    private boolean isReady = false;
    public LoadedImage skinSelection[] = new LoadedImage[4];
    private int zaehlerSkinSelection = 0;

    /**
     * Konstruktor, wo die IP Adresse auch uebergeben wird. Wird vom zu erstellenden
     * HOST aufgerufen.
     * 
     * @param name String, den Namen des Players wird gespeichert.
     */
    public LobbyPlayer(String name) {
	this.name = name;
	isHost = true;
	initializeImages();
    }

    /**
     * Konstruktor, wo die IP Adresse auch uebergeben wird. Wird von zu erstellenden
     * PLAYERN aufgerufen.
     * 
     * @param name String, den Namen des Players wird gespeichert.
     * @param ip   ip-Adresse, wird gespeichert.
     */
    public LobbyPlayer(String name, String ip) {
	this.name = name;
	this.ip = ip;
	isHost = false;
	initializeImages();

    }

    /**
     * Laed alle Images der Skinauswahl in einem Array. Die Skinauswahl wird von dem
     * Player gesteuert.
     */
    public void initializeImages() {
	skinSelection[0] = ImageHandler.getImage(ImageType.IMAGE_LOBBY_SKIN_0);
	skinSelection[1] = ImageHandler.getImage(ImageType.IMAGE_LOBBY_SKIN_1);
	skinSelection[2] = ImageHandler.getImage(ImageType.IMAGE_LOBBY_SKIN_2);
	skinSelection[3] = ImageHandler.getImage(ImageType.IMAGE_LOBBY_SKIN_3);
    }

    public int getSkin() {
	return zaehlerSkinSelection;
    }

    public void setSkin(int skin) {
	zaehlerSkinSelection = skin;
    }

    public String getName() {
	return name;
    }

    public int getId() {
	return id;
    }

    public void setId(int id) {
	this.id = id;
    }

    public String getIpAdress() {
	return ip;
    }

    public boolean getisHost() {
	return isHost;
    }

    public void setisHost(boolean x) {
	isHost = x;
    }

    /**
     * Veraendert den Status, ob der Player ready ist, sodass der Host das Spiel
     * starten kann. Wird von der Checkbox aufgerufen, wo alle Player (ausser dem
     * Host) den Button klicken koennen.
     */
    public void changeisReady() {
	if (isReady == false) {
	    isReady = true;
	}

	else if (isReady == true) {
	    isReady = false;
	}
    }

    public void setisReadyForClients(String ready) {
	isReady = Boolean.parseBoolean(ready);
    }

    /**
     * Gibt zurueck, ob der Player ready ist.
     */
    public boolean getisReady() {
	return isReady;
    }

}