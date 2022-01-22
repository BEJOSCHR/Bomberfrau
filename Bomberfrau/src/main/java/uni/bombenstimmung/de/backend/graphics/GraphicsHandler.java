/*
 * GraphicsHandler
 *
 * Version 1.0
 * Author: Benni
 *
 * Verwaltet die graphischen Ver�nderungen und Wechsel zwischen den Modulen
 */
package uni.bombenstimmung.de.backend.graphics;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import uni.bombenstimmung.de.aftergame.DeadPlayerHandler;
import uni.bombenstimmung.de.backend.animation.Animation;
import uni.bombenstimmung.de.backend.animation.AnimationHandler;
import uni.bombenstimmung.de.backend.console.ConsoleHandler;
import uni.bombenstimmung.de.backend.console.MessageType;
import uni.bombenstimmung.de.backend.graphics.subhandler.KeyHandler;
import uni.bombenstimmung.de.backend.graphics.subhandler.MouseHandler;
import uni.bombenstimmung.de.backend.graphics.subhandler.WindowHandler;
import uni.bombenstimmung.de.backend.images.ImageHandler;
import uni.bombenstimmung.de.backend.sounds.SoundHandler;
import uni.bombenstimmung.de.backend.sounds.SoundType;
import uni.bombenstimmung.de.game.Game;
import uni.bombenstimmung.de.game.GameCounter;
import uni.bombenstimmung.de.game.GameData;
import uni.bombenstimmung.de.game.PlayerHandler;
import uni.bombenstimmung.de.lobby.LobbyButtons;
import uni.bombenstimmung.de.lobby.LobbyCreate;
import uni.bombenstimmung.de.lobby.LobbyPlayer;
import uni.bombenstimmung.de.main.BomberfrauMain;
import uni.bombenstimmung.de.menu.Menu;
import uni.bombenstimmung.de.menu.MenuAnimations;
import uni.bombenstimmung.de.menu.Settings;

public class GraphicsHandler {

	private static int width = 0, height = 0;
	private static Label label;
	private static JFrame frame;
	private static DisplayType displayType = DisplayType.LOADINGSCREEN;
	private static boolean shuttingDown = false;
	private static FrameDragListener frameDragListener;
	public static LobbyCreate lobby;
	
	/**
	 * Wird am anfang aufgerufen um sowohl den Frame als auch das Label zu erzeugen und zuzuordnen
	 */
	public static void initVisuals() {
		
		frame = new JFrame();
		
		frame.setLocationRelativeTo(null);
		//frame.setLocation(0, 0);
		frame.setLocation((Settings.getRes_width_max()-Settings.getRes_width())/2, (Settings.getRes_height_max()-Settings.getRes_height())/2);
		frame.setUndecorated(true);
		
		frame.setTitle("BomberFrau - "+BomberfrauMain.VERSION);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		
		frame.addKeyListener(new KeyHandler());
		frame.addMouseListener(new MouseHandler());
		frame.addMouseMotionListener(new MouseHandler());
		frame.addMouseWheelListener(new MouseHandler());
		frame.addWindowListener(new WindowHandler());
		
		try {
			frame.setIconImage(ImageIO.read(BomberfrauMain.class.getClassLoader().getResourceAsStream(ImageHandler.PATH+"Bomberman_Icon.png")));
		} catch (Exception error) {
			ConsoleHandler.print("Couldn't load window icon!", MessageType.BACKEND);
		}
		
//		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
//		frame.setSize(1920, 1080);
		frame.setSize(Settings.getRes_width(), Settings.getRes_height());
		//frame.setPreferredSize(frame.getSize());
		//frame.setMinimumSize(frame.getSize());
		//frame.setMaximumSize(frame.getSize());

		frameDragListener = new FrameDragListener(frame);
		setMoveable();
	        
		width = frame.getWidth();
		height = frame.getHeight();
		
		//TODO CALCULATE DIMENSIONS RELATIVE TO WIDTH AND HEIGHT
		GameData.FIELD_DIMENSION = (int) (height-GameData.MAP_SIDE_BORDER)/GameData.MAP_DIMENSION;
		label = new Label();
		label.requestFocus();

		frame.requestFocus();
		
		ConsoleHandler.print("Initialised Visuals!", MessageType.BACKEND);
		
	}
	
	/**
	 * L�sst das Fenster trotz 'Undecorated' per Maus bewegen
	 */
	private static class FrameDragListener extends MouseAdapter {

	      private final JFrame frame;
	      private Point mouseDownCompCoords = null;

	      public FrameDragListener(JFrame frame) {
	         this.frame = frame;
	      }

	      public void mouseReleased(MouseEvent e) {
	         mouseDownCompCoords = null;
	      }

	      public void mousePressed(MouseEvent e) {
	         mouseDownCompCoords = e.getPoint();
	      }

	      public void mouseDragged(MouseEvent e) {
	         Point currCoords = e.getLocationOnScreen();
	         frame.setLocation(currCoords.x - mouseDownCompCoords.x, currCoords.y - mouseDownCompCoords.y);
	      }
	}
	
	/**
	 * Deaktiviert die M�glichkeit der Fensterbewegung bei Vollbild, aktivert sie ansonsten. 
	 */
	public static void setMoveable() {
        	frame.removeMouseListener(frameDragListener);
        	frame.removeMouseMotionListener(frameDragListener);
            	if (Settings.getRes_nr() != 0) {
        	    frame.addMouseListener(frameDragListener);
        	    frame.addMouseMotionListener(frameDragListener);
        	    ConsoleHandler.print("window moveable = on", MessageType.MENU);
        	};
	}
	
	//------------------------------------------------------------------------------------------------------------------
	//SWITCH TO SECTION
	
	/**
	 * Wird von der Main am Start aufgerufen und startet das Intro
	 */
	public static void switchToIntroFromLoadingscreen() {
	    
		ConsoleHandler.print("Switched to 'INTRO' from 'START'!", MessageType.BACKEND);
		
		AnimationHandler.stopAllAnimations();
		
	    	SoundHandler.playSound2(SoundType.INTRO, false);
	    	//SoundHandler.playSound(SoundType.INTRO, false,  Menu.VolumeIntToFloat(Settings.getIni_VolMusic()));

		MenuAnimations.introTextAni();
		MenuAnimations.introAnimation();
		
		displayType = DisplayType.INTRO;
	}
	
	/**
	 * Wird nach dem Intro aufgerufen
	 */
	public static void switchToMenuFromIntro() {

	        ConsoleHandler.print("Switched to 'MENU' from 'INTRO'!", MessageType.BACKEND);
	        
		SoundHandler.stopAllSounds();
		
		new Timer().schedule(new TimerTask() {
		    @Override
		    public void run() {
			MenuAnimations.titlePulseAni(); 
		    }
		} , 200);
		
	        Settings.setCreate_selected(true);
		ConsoleHandler.print("isHost = " + Menu.getIs_host(), MessageType.BACKEND);
//	        Menu.titlePulseAni();

	        Menu.buildOptions();
		Menu.buildMenu();
		Menu.optionsComponentsActive(false);
		//Menu.menuComponentsActive(true);
		
	    	SoundHandler.playSound2(SoundType.MENU, true);
	    	//SoundHandler.playSound(SoundType.MENU, false, Menu.VolumeIntToFloat(Settings.getIni_VolMusic()));
		
		displayType = DisplayType.MENU;

	}
	
	/**
	 * Wird beim Wechseln von Men� zu den Optionen aufgerufen
	 */
	public static void switchToOptionsFromMenu() {

		ConsoleHandler.print("Switched to 'OPTIONS' from 'MENU'!", MessageType.BACKEND);
		AnimationHandler.stopAllAnimations();
		Menu.menuComponentsActive(false);
		Menu.optionsComponentsActive(true);

		displayType = DisplayType.OPTIONS;
		Menu.menuComponentsActive(false);
	}
	/**
	 * Wird bei R�ckkehr von den Optionen zum Men� aufgerufen
	 */
	public static void switchToMenuFromOptions() {

		ConsoleHandler.print("Switched to 'MENU' from 'OPTIONS'!", MessageType.BACKEND);

		AnimationHandler.stopAllAnimations();
		MenuAnimations.titlePulseAni();
		//Menu.menuComponentsActive(true);
		Menu.buildMenu();
		Menu.optionsComponentsActive(false);

		displayType = DisplayType.MENU;
		
	}
	/**
	 * Wird aufgerufen wenn die Lobby verlassen wird
	 */
	public static void switchToMenuFromLobby() {

		ConsoleHandler.print("Switched to 'MENU' from 'LOBBY'!", MessageType.BACKEND);
		AnimationHandler.stopAllAnimations();
		//LobbyButtons.lobbyButtonsRemove();
		Menu.menuComponentsActive(false);

		MenuAnimations.titlePulseAni();
		Menu.buildMenu();
		//Menu.menuComponentsActive(true);

		displayType = DisplayType.MENU;
		
	}
	
	public static void switchToLobbyFromAftergame() {
		
	    AnimationHandler.stopAllAnimations();
	    
	    SoundHandler.playSound2(SoundType.MENU, true);
	    LobbyButtons.lobbyButtonsReset();
	    
	    if (DeadPlayerHandler.getClientPlayer().isHost()) {
		lobby = new LobbyCreate(new LobbyPlayer(DeadPlayerHandler.getClientPlayer().getName()), true, true);
	    }else {
		lobby = new LobbyCreate(new LobbyPlayer( DeadPlayerHandler.getClientPlayer().getName(), DeadPlayerHandler.getClientPlayer().getIp()), true);
	    }

	    displayType = DisplayType.LOBBY;
	    ConsoleHandler.print("Switched to 'LOBBY' from 'AFTERGAME'!", MessageType.BACKEND);

	}
	
	/**
	 * Wird aufgerufen wenn w�hrend einem Spiel das Spiel verlassen wird bzw der Host das Spiel schlie�t
	 */
	public static void switchToMenuFromIngame() {

		ConsoleHandler.print("Switched to 'MENU' from 'INGAME'!", MessageType.BACKEND);
		AnimationHandler.stopAllAnimations();
		SoundHandler.stopAllSounds();
		MenuAnimations.titlePulseAni();
		Menu.buildMenu();
		
	    	SoundHandler.playSound2(SoundType.MENU, true);
		displayType = DisplayType.MENU;
		
	}
	
	/**
	 * Wird aufgerufen wenn im Aftergame die Session verlassen wird
	 */
	public static void switchToMenuFromAftergame() {

		ConsoleHandler.print("Switched to 'MENU' from 'AFTERGAME'!", MessageType.BACKEND);
		AnimationHandler.stopAllAnimations();
		SoundHandler.stopAllSounds();
		MenuAnimations.titlePulseAni();
		Menu.buildMenu();
		
	    	SoundHandler.playSound2(SoundType.MENU, true);
		displayType = DisplayType.MENU;
		
	}
	
	/**
	 * Wird aufgerufen wenn aus dem Menu ein Spiel erstellt wird oder einem beigetreten wird
	 */
	public static void switchToLobbyFromMenu() {
		
	    	LobbyButtons.lobbyButtonsReset();
		Menu.menuComponentsActive(false);
		AnimationHandler.stopAllAnimations();
		
		Menu.menuComponentsActive(false);
		
		displayType = DisplayType.LOBBY;
		
		ConsoleHandler.print("Switched to 'LOBBY' from 'MENU'!", MessageType.BACKEND);

		if (Menu.getIs_host()) {
		    lobby = new LobbyCreate(new LobbyPlayer(Settings.getUser_name()), Menu.getIs_host(), false);
		}
		else {
//		    lobby = new LobbyCreate(new LobbyPlayer(Settings.getUser_name(), Settings.getIp()));
		    lobby = new LobbyCreate(new LobbyPlayer(Settings.getUser_name(), "127.0.0.1"), false);
		}
	}
	
	/**
	 * Wird aufgerufen wenn das Spiel aus der Lobby gestartet wird
	 */
	public static void switchToIngameFromLobby() {
		
		SoundHandler.reducePlayingSound(SoundType.MENU, 3, true);
		AnimationHandler.stopAllAnimations();
		SoundHandler.stopAllSounds();
		GameData.FIELD_DIMENSION = (int) (height-GameData.MAP_SIDE_BORDER)/GameData.MAP_DIMENSION;
		Game.resetIngame();
		Game.setMapNumber(LobbyCreate.getMap()+1);
	    	Game.updateMap(LobbyCreate.getMap()+1);
	    	
	    	// Add all Players into InGame ArrayList "PlayerFromLobby"
		
		for(int i=0; i < LobbyCreate.numberOfMaxPlayers; i++) {
		    if(i==0) {
			PlayerHandler.addPlayerFromLobby(LobbyCreate.player[i].getId(), LobbyCreate.player[i].getName(),  LobbyCreate.player[i].getIpAdress(),
				LobbyCreate.player[i].getisHost(), LobbyCreate.player[i].getSkin(), new Point(1,1), LobbyCreate.client);
		    }
		    if(i==1) {
			PlayerHandler.addPlayerFromLobby(LobbyCreate.player[i].getId(), LobbyCreate.player[i].getName(),  LobbyCreate.player[i].getIpAdress(),
				LobbyCreate.player[i].getisHost(), LobbyCreate.player[i].getSkin(), new Point(15,1), LobbyCreate.client);
		    }
		    if(i==2) {
			PlayerHandler.addPlayerFromLobby(LobbyCreate.player[i].getId(), LobbyCreate.player[i].getName(),  LobbyCreate.player[i].getIpAdress(),
				LobbyCreate.player[i].getisHost(), LobbyCreate.player[i].getSkin(), new Point(1,15), LobbyCreate.client);
		    }
		    if(i==3) {
			PlayerHandler.addPlayerFromLobby(LobbyCreate.player[i].getId(), LobbyCreate.player[i].getName(),  LobbyCreate.player[i].getIpAdress(),
				LobbyCreate.player[i].getisHost(), LobbyCreate.player[i].getSkin(), new Point(15,15), LobbyCreate.client);
		    }
		}
		
		// Alle Player Objekte der Lobby löschen, sodass man es resettet ist fuers Aftergame
	    	// Setze alle Objekte = null und switche ins Menu
	    	for (int i=0; i< LobbyCreate.numberOfMaxPlayers; i++) {
		    LobbyCreate.player[i] = null;
	    	}
	    	LobbyCreate.numberOfMaxPlayers = 0;
	    	
		PlayerHandler.initPlayers();
	    	PlayerHandler.addToAllPlayers(PlayerHandler.getOpponentPlayers());
	    	ConsoleHandler.print("Player Count: " + PlayerHandler.getAllPlayer().size(), MessageType.GAME);
	    	GameCounter zaehler = new GameCounter();
	    	
	    	
	    	frame.requestFocus();
	    	
		displayType = DisplayType.INGAME;
		ConsoleHandler.print("Switched to 'INGAME' from 'LOBBY'!", MessageType.BACKEND);
		new Animation(100, 4) {
		    @Override
		    public void initValues() {
			Game.setCountdown(1);
			SoundHandler.playSound2(SoundType.COUNTDOWN, false);
		    }
		    
		    @Override
		    public void changeValues() {
			Game.setCountdown(Game.getCountdown() + 1);
		    }
		    
		    @Override
		    public void finaliseValues() {
			Game.setCountdown(0);
			zaehler.startCounter();
			PlayerHandler.setMovable(true);
			switch (Game.getMapNumber()) {
			case 1:
			    SoundHandler.playSound2(SoundType.MAP1, true);
			    break;
			    
			case 2:
			    SoundHandler.playSound2(SoundType.MAP2, true);
			    break;
			    
			case 3:
			    SoundHandler.playSound2(SoundType.MAP3, true);
			    break;
			    
			default:
			    ConsoleHandler.print("No music track available for this map!", MessageType.GAME);
			}
		    }
		};
	}
	
	/**
	 * Wird aufgerufen wenn ein Spiel beendet wird und man das Aftergame betreten soll
	 */
	public static void switchToAftergameFromIngame() {
		
		AnimationHandler.stopAllAnimations();
		SoundHandler.stopAllSounds();
		
		//ubermittlung der daten des aktuellen Player
		DeadPlayerHandler.setClientPlayer(PlayerHandler.getClientPlayer().getId(), PlayerHandler.getClientPlayer().getName(), PlayerHandler.getClientPlayer().getIpAdress() ,PlayerHandler.getClientPlayer().getHost(), PlayerHandler.getClientPlayer().getSkin(), PlayerHandler.getClientPlayer().getConnectedClient());
		
		
//		for(int i=0; i < PlayerHandler.getPlayerAmount(); i++) {
//		    //addDeadPlayerFromIngame(int id, String name, String ipAdress, boolean host, int skin)
//		    DeadPlayerHandler.addDeadPlayerFromIngame(PlayerHandler.getAllPlayer().get(i).getId(), PlayerHandler.getAllPlayer().get(i).getName(), PlayerHandler.getAllPlayer().get(i).getIpAdress(),
//			    PlayerHandler.getAllPlayer().get(i).getHost(), PlayerHandler.getAllPlayer().get(i).getSkin());
//		}
		
		for(int i=0; i < PlayerHandler.getPlayerAmount(); i++) {
		    DeadPlayerHandler.addDeadPlayer(PlayerHandler.getAllPlayer().get(i).getId(), PlayerHandler.getAllPlayer().get(i).getName(), PlayerHandler.getAllPlayer().get(i).getDeathTime()); 
		}
		
		if (DeadPlayerHandler.getClientPlayer().isHost()) {
		    //Host uebermittelt aktuelle Punktzahl an die Clients
		    for(int i=0; i < DeadPlayerHandler.getAllDeadPlayer().size(); i++) {
			DeadPlayerHandler.getClientPlayer().getCC().sendMessageToAllClients("601-"+ i + "-" + DeadPlayerHandler.getAllDeadPlayer().get(i).getName()+ "-" + DeadPlayerHandler.getAllDeadPlayer().get(i).getDeathTime() + "-" + DeadPlayerHandler.getAllDeadPlayer().get(i).getScore());
		    }
		}
		else {
		    //Clients sollen kurz warten, bis aktuelle Punktzahl vom Host uebermittelt wurde
		    try {
			Thread.sleep(1000);
		    } catch (InterruptedException iex) {}
		}
		
		//Punktzahl und Plazierung berechnen
		DeadPlayerHandler.calculateScore();
        		
		displayType = DisplayType.AFTERGAME;
		ConsoleHandler.print("Switched to 'AFTERGAME' from 'INGAME'!", MessageType.BACKEND);
		
	}
	//SWITCH TO SECTION
	//------------------------------------------------------------------------------------------------------------------
	//
	
	/**
	 *  sch�nerer Font mit abgerundeten Zeichen
 	 */
	public static Font usedFont(int textSize) {

	    Float factor = (float)(Settings.getRes_height())/Settings.getRes_height_max();
	    Font font;
	    font = new Font("Arial", Font.BOLD, (int)(40*factor));
	    try {
        	    font = Font.createFont(Font.TRUETYPE_FONT, new File("src\\main\\resources\\fonts\\vagrounded.ttf"));
                    font = font.deriveFont(10f*factor);
	    }
            catch (Exception e) {}
            font = font.deriveFont((10f+textSize)*factor);
	    return font;
	}
	
	/**
	 * Allgemeine Methode um einen beliebigen Text mit Parametern relativ zu einem Punkt (x,y) mittig darzustellen
	 * @param g, das Graphics object
	 * @param color, die Textfarbe
	 * @param textSize, die Textgr��e
	 * @param text, der eigentliche Text
	 * @param x, die X-Koordinate (Links-Rechts-Verschiebung) zu der der Text mittig dargestellt wird
	 * @param y, die Y-Koordinate (Oben-Unten-Verschiebung) zu der der Text mittig dargestellt wird
	 */
	public static void drawCentralisedText(Graphics g, Color color, int textSize, String text, int x, int y) {
		
		g.setColor(color);   
        	g.setFont(usedFont(textSize));
		int width = g.getFontMetrics().stringWidth(text);
		int height = g.getFontMetrics().getHeight()*2/3;
		g.drawString(text, x-width/2, y+height/2);
		
	}

	/**
	 * Wie Methode drawCentralisedText, nur linksb�ndig
	 * @param g, das Graphics object
	 * @param color, die Textfarbe
	 * @param textSize, die Textgr��e
	 * @param text, der eigentliche Text
	 * @param x, die X-Koordinate (Links-Rechts-Verschiebung) zu der der Text linksb�ndig dargestellt wird
	 * @param y, die Y-Koordinate (Oben-Unten-Verschiebung) zu der der Text mittig dargestellt wird
	 */
	public static void drawLeftText(Graphics g, Color color, int textSize, String text, int x, int y) {
		
		g.setColor(color);   
        	g.setFont(usedFont(textSize));
		int height = g.getFontMetrics().getHeight()*2/3;
		g.drawString(text, x, y+height/2);
		
	}
	
	/**
	 * Wie Methode drawCentralisedText, nur rechtsb�ndig
	 * @param g, das Graphics object
	 * @param color, die Textfarbe
	 * @param textSize, die Textgr��e
	 * @param text, der eigentliche Text
	 * @param x, die X-Koordinate (Links-Rechts-Verschiebung) zu der der Text rechtsb�ndig dargestellt wird
	 * @param y, die Y-Koordinate (Oben-Unten-Verschiebung) zu der der Text mittig dargestellt wird
	 */
	public static void drawRightText(Graphics g, Color color, int textSize, String text, int x, int y) {
		
		g.setColor(color);   
        	g.setFont(usedFont(textSize));
		int height = g.getFontMetrics().getHeight()*2/3;
		g.drawString(text, x-g.getFontMetrics().stringWidth(text), y+height/2);
	}
	
	/**
	 * Der einzige saubere Weg dieses Programm zu stoppen (Stoppt alle Timer und schlie�t KONTROLLIERT alle Datenzug�nge bzw speichert setting etc).
	 * Wenn einmal aufgerufen werden weitere Aufrufe dieser Methode abgeblockt, so dass ein doppeltes runterfahren nicht m�glich ist!
	 */
	public static void shutdownProgram() {
		
		if(shuttingDown) { return; }
		
		ConsoleHandler.print("Stopping Bomberfrau ["+BomberfrauMain.VERSION+"]", MessageType.IMPORTANT);
		
		shuttingDown = true;
		
		ConsoleHandler.stopInputScanner();
		AnimationHandler.stopTickTimer();
		
		System.exit(0);
		
	}
	
	public static int getWidth() {
		return width;
	}
	public static int getHeight() {
		return height;
	}

	public static void setWidth(int i) {
		width = i;
	}
	public static void setHeight(int i) {
		height = i;
	}
	
	public static JFrame getFrame() {
		return frame;
	}
	public static Label getLabel() {
		return label;
	}
	public static DisplayType getDisplayType() {
		return displayType;
	}
	
}
