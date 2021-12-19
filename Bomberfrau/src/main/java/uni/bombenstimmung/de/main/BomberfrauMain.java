/*
 * BomberfrauMain
 *
 * Version 1.0
 * Author: Benni
 *
 * Der Beginn des Programmes
 */

package uni.bombenstimmung.de.main;

import java.util.Random;

import uni.bombenstimmung.de.backend.animation.Animation;
import uni.bombenstimmung.de.backend.animation.AnimationData;
import uni.bombenstimmung.de.backend.animation.AnimationHandler;
import uni.bombenstimmung.de.backend.console.ConsoleHandler;
import uni.bombenstimmung.de.backend.console.MessageType;
import uni.bombenstimmung.de.backend.graphics.GraphicsHandler;
import uni.bombenstimmung.de.backend.images.ImageHandler;
import uni.bombenstimmung.de.backend.language.LanguageHandler;
import uni.bombenstimmung.de.backend.maa.MouseActionAreaHandler;
import uni.bombenstimmung.de.backend.sounds.SoundHandler;
import uni.bombenstimmung.de.menu.Settings;

public class BomberfrauMain {
	
	public static final String AUTHOR = "Bombenstimmung - Uni Wuppertal - C2021";
	public static final String VERSION = "Alpha - V0.0.8";
	
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
		
		String ini = "save.ini";
		Settings.initIni(ini);
		Settings.iniValuesToTerminal(ini);
		
		//1. BACKEND
		LanguageHandler.initLLBs();
		GraphicsHandler.initVisuals();
		MouseActionAreaHandler.initMAAs();
		ImageHandler.initImages();
		SoundHandler.initSounds();
		ConsoleHandler.startInputScanner();
		AnimationHandler.startTickTimer();
		
		//LOADINGSCREEN ANIMATIONS
		new Animation(60, -1) {
			@Override
			public void initValues() {
				AnimationData.loadingScreen_textSizeModifier = 0;
			}
			@Override
			public void changeValues() {
				if(getSteps()%2 == 0) {
					AnimationData.loadingScreen_textSizeModifier = 1;
				}else {
					AnimationData.loadingScreen_textSizeModifier = 0;
				}
			}
			@Override
			public void finaliseValues() {
				AnimationData.loadingScreen_textSizeModifier = 0;
			}
		};
		Random r = new Random();
		new Animation(10, -1) {
			@Override
			public void initValues() {
				AnimationData.loadingScreen_posXModifier = 0;
				AnimationData.loadingScreen_posYModifier = 0;
			}
			@Override
			public void changeValues() {
				if(getSteps()%2 == 0) {
					int stepSize = 1;
					AnimationData.loadingScreen_posXModifier += r.nextInt(stepSize*2)-stepSize;
					AnimationData.loadingScreen_posYModifier += r.nextInt(stepSize*2)-stepSize;
					int limit = 10;
					if(AnimationData.loadingScreen_posXModifier < -limit) { AnimationData.loadingScreen_posXModifier = -limit; }
					else if(AnimationData.loadingScreen_posXModifier > limit) { AnimationData.loadingScreen_posXModifier = limit; }
					if(AnimationData.loadingScreen_posYModifier < -limit) { AnimationData.loadingScreen_posYModifier = -limit; }
					else if(AnimationData.loadingScreen_posYModifier > limit) { AnimationData.loadingScreen_posYModifier = limit; }
				}else {
					AnimationData.loadingScreen_posXModifier = 0;
					AnimationData.loadingScreen_posYModifier = 0;
				}
			}
			@Override
			public void finaliseValues() {
				AnimationData.loadingScreen_posXModifier = 0;
				AnimationData.loadingScreen_posYModifier = 0;
			}
		};
		
//		ConnectedClient host = new ConnectedClient(true, 0);
//		System.out.println("Client: " + host.getId()+" Host: " +host.isHost());
//		ConnectedClient client = new ConnectedClient(false, 1);
//		System.out.println("Client: " + client.getId()+" Host: " +client.isHost());
//		ConnectedClient client2 = new ConnectedClient(false, 2);
//		System.out.println("Client: " + client2.getId()+" Host: " +client2.isHost());
		//2. ...
		//TODO
		
		
		//FINISHED STARTING UP
		
	}                                                
	
}

