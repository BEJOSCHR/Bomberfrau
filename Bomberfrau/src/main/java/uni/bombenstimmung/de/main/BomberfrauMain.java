/*
 * BomberfrauMain
 *
 * Version 1.0
 * Author: Benni
 *
 * Der Beginn des Programmes
 */

package uni.bombenstimmung.de.main;

import uni.bombenstimmung.de.backend.console.ConsoleHandler;
import uni.bombenstimmung.de.backend.console.MessageType;
import uni.bombenstimmung.de.backend.graphics.GraphicsHandler;
import uni.bombenstimmung.de.backend.images.ImageHandler;
import uni.bombenstimmung.de.backend.maa.MouseActionAreaHandler;
import uni.bombenstimmung.de.backend.serverconnection.host.ConnectedClient;

public class BomberfrauMain {
	
	public static final String AUTHOR = "Bombenstimmung - Uni Wuppertal - �2021";
	public static final String VERSION = "Alpha - V0.0.6";
	
	/**
	 * Der Start von allem (in diesem Projekt)
	 * @param args Die Argumente die dem Start mitgegeben werden k�nnen
	 */
	public static void main(String[] args) {
		
		ConsoleHandler.print("Starting Bomberfrau ["+VERSION+"]", MessageType.IMPORTANT);
		
		/*STARTUP
		1. BACKEND STUFF
		2. ...
		*/
		
		//1. BACKEND
		GraphicsHandler.initVisuals();
		MouseActionAreaHandler.initMAAs();
		ImageHandler.initImages();
		ConsoleHandler.startInputScanner();
		
		ConnectedClient host = new ConnectedClient(true, 0);
		System.out.println("Client: " + host.getId()+" Host: " +host.isHost());
		ConnectedClient client = new ConnectedClient(false, 1);
		System.out.println("Client: " + client.getId()+" Host: " +client.isHost());
		ConnectedClient client2 = new ConnectedClient(false, 2);
		System.out.println("Client: " + client2.getId()+" Host: " +client2.isHost());
		//2. ...
		//TODO
		
		
		//FINISHED STARTING UP
		
	}                                                
	
}

