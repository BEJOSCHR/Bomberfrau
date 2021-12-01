/*
 * GraphicsHandler
 *
 * Version 1.0
 * Author: Benni
 *
 * Verwaltet die graphischen Veränderungen und Wechsel zwischen den Modulen
 */
package uni.bombenstimmung.de.backend.graphics;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JFrame;

import uni.bombenstimmung.de.backend.console.ConsoleHandler;
import uni.bombenstimmung.de.backend.console.MessageType;
import uni.bombenstimmung.de.main.BomberfrauMain;

public class GraphicsHandler {

	private static int width = 0, height = 0;
	private static Label label;
	private static JFrame frame;
	private static DisplayType displayType = DisplayType.LOADINGSCREEN;
	private static boolean shuttingDown = false;
	
	/**
	 * Wird am anfang aufgerufen um sowohl den Frame als auch das Label zu erzeugen und zuzuordnen
	 */
	public static void initVisuals() {
		
		frame = new JFrame();
		
		frame.setLocationRelativeTo(null);
		frame.setLocation(0, 0);
		frame.setTitle("BomberFrau - "+BomberfrauMain.VERSION);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setUndecorated(true); //TODO 
		frame.setVisible(true);
		
//		frame.addKeyListener(new KeyHandler());
//		frame.addMouseListener(new MouseHandler());
//		frame.addMouseMotionListener(new MouseHandler());
//		frame.addMouseWheelListener(new MouseHandler());
//		frame.addWindowListener(new WindowHandler());
		
//		try { //TRY TO SET ICON
//			frame.setIconImage(ImageIO.read(Bomberfrau_Main.class.getResourceAsStream("images/Icon.png")));
//		} catch (Exception error) {
//			System.out.println("The Window Icon couldn't be loaded!");
//		}
		
//		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.setSize(1920, 1080);
		frame.setPreferredSize(frame.getSize());
		frame.setMinimumSize(frame.getSize());
		frame.setMaximumSize(frame.getSize());
		
		width = frame.getWidth();
		height = frame.getHeight();
		
//		GameData.FIELD_DIMENSION = (int) ((GraphicsData.height-GameData.MAP_SIDE_BORDER*2)/GameData.MAP_DIMENSION);
//		GameData.PLAYER_DIMENSION = (int) (GameData.FIELD_DIMENSION*0.75);
//		GameData.BOMB_DIMENSION = (int) (GameData.FIELD_DIMENSION*0.65);
		
		frame.requestFocus();
		
		label = new Label();
		
		ConsoleHandler.print("Initialised Visuals!", MessageType.BACKEND);
		
	}
	
	//------------------------------------------------------------------------------------------------------------------
	//SWITCH TO SECTION
	/**
	 * Wird von der Main am Start aufgerufen und initialisiert das Menu
	 */
	public static void switchToMenuFromStart() {
		
		
		
		displayType = DisplayType.MENU;
		ConsoleHandler.print("Switched to 'MENU' from 'START'!", MessageType.BACKEND);
		
	}
	/**
	 * Wird aufgerufen wenn die Lobby verlassen wird
	 */
	public static void switchToMenuFromLobby() {
		
		
		
		displayType = DisplayType.MENU;
		ConsoleHandler.print("Switched to 'MENU' from 'LOBBY'!", MessageType.BACKEND);
		
	}
	/**
	 * Wird aufgerufen wenn wärend einem Spiel das Spiel verlassen wird bzw der Host das Spiel schließt
	 */
	public static void switchToMenuFromIngame() {
		
		
		
		displayType = DisplayType.MENU;
		ConsoleHandler.print("Switched to 'MENU' from 'INGAME'!", MessageType.BACKEND);
		
	}
	/**
	 * Wird aufgerufen wenn im Aftergame die Session verlassen wird
	 */
	public static void switchToMenuFromAftergame() {
		
		
		
		displayType = DisplayType.MENU;
		ConsoleHandler.print("Switched to 'MENU' from 'AFTERGAME'!", MessageType.BACKEND);
		
	}
	/**
	 * Wird aufgerufen wenn aus dem Menu ein Spiel erstellt wird oder einem beigetreten wird
	 */
	public static void switchToLobbyFromMenu() {
		
		
		
		displayType = DisplayType.LOBBY;
		ConsoleHandler.print("Switched to 'LOBBY' from 'MENU'!", MessageType.BACKEND);
		
	}
	/**
	 * Wird aufgerufen wenn das Spiel aus der Lobby gestartet wird
	 */
	public static void switchToIngameFromLobby() {
		
		
		
		displayType = DisplayType.INGAME;
		ConsoleHandler.print("Switched to 'INGAME' from 'LOBBY'!", MessageType.BACKEND);
		
	}
	/**
	 * Wird aufgerufen wenn ein Spiel beendet wird und man das Aftergame betreten soll
	 */
	public static void switchToAftergameFromIngame() {
		
		
		
		displayType = DisplayType.AFTERGAME;
		ConsoleHandler.print("Switched to 'AFTERGAME' from 'INGAME'!", MessageType.BACKEND);
		
	}
	//SWITCH TO SECTION
	//------------------------------------------------------------------------------------------------------------------
	
	/**
	 * Allgemeine methode um einen beliebigen text mit parametern relativ zu einem Punkt (x,y) mittig darzustellen
	 * @param g, das Graphics object
	 * @param color, die Textfarbe
	 * @param textSize, die Textgröße
	 * @param text, der eigentliche Text
	 * @param x, die X-Koordinate (Links-Rechts-Verschiebung) zu der der Text mittig dargestellt wird
	 * @param y, die Y-Koordinate (Oben-Unten-Verschiebung) zu der der Text mittig dargestellt wird
	 */
	public static void drawCentralisedText(Graphics g, Color color, int textSize, String text, int x, int y) {
		
		g.setColor(color);
		g.setFont(new Font("Arial", Font.BOLD, textSize));
		int width = g.getFontMetrics().stringWidth(text);
		int height = g.getFontMetrics().getHeight()*2/3;
		g.drawString(text, x-width/2, y+height/2);
		
	}
	
	/**
	 * Der einzige saubere Weg dieses Programm zu stoppen (Stoppt alle Timer und schließt KONTROLLIERT alle Datenzugänge bzw speichert setting etc).
	 * Wenn einmal aufgerufen werden weitere Aufrufe dieser Methode abgeblockt, so dass ein doppeltes runterfahren nicht möglich ist!
	 */
	public static void shutdownProgram() {
		
		if(shuttingDown) { return; }
		
		shuttingDown = true;
		
		ConsoleHandler.stopInputScanner();
		
		System.exit(0);
		
	}
	
	public static int getWidth() {
		return width;
	}
	public static int getHeight() {
		return height;
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
