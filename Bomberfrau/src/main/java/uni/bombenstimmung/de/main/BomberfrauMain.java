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

public class BomberfrauMain {
	
	public static final String AUTHOR = "Bombenstimmung - Uni Wuppertal - ©2021";
	public static final String VERSION = "Alpha - V0.0.6";
	
	/**
	 * Der Start von allem (in diesem Projekt)
	 * @param args Die Argumente die dem Start mitgegeben werden können
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
		
		//2. ...
		//TODO
		
		
		//FINISHED STARTING UP
		
	}                                                
	
}

