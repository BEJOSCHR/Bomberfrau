/*
 * MouseActionAreaHandler
 *
 * Version 1.0
 * Author: Benni
 *
 * Verwaltet alle MouseActionAreas aka Custombuttons (laden, ausgeben, ver�ndern...)
 */
package uni.bombenstimmung.de.backend.maa;

import java.util.ArrayList;
import java.util.List;

import java.awt.Color;
import java.awt.Graphics;

import uni.bombenstimmung.de.backend.console.ConsoleHandler;
import uni.bombenstimmung.de.backend.console.MessageType;
import uni.bombenstimmung.de.backend.graphics.DisplayType;
import uni.bombenstimmung.de.backend.graphics.GraphicsHandler;
import uni.bombenstimmung.de.backend.images.ImageHandler;
import uni.bombenstimmung.de.backend.images.ImageType;
import uni.bombenstimmung.de.lobby.Lobby_Create;

public class MouseActionAreaHandler {

	private static List<MouseActionArea> mouseActionAreas = new ArrayList<MouseActionArea>(); 
	
	/**
	 * Inititalisiert alle MAAs und defeniert via Overwrite die Funktionalit�t
	 */
	public static void initMAAs() {
		
		new MouseActionArea(-1, -1, GraphicsHandler.getWidth()+1, GraphicsHandler.getHeight()+1,
				MouseActionAreaType.MAA_LOADINGSCREEN_CLICKTOSTART, "", 1, Color.WHITE, Color.WHITE) {
			@Override
			public void performAction_LEFT_RELEASE() {
				GraphicsHandler.switchToMenuFromStart();
				GraphicsHandler.switchToLobbyFromMenu(); // Jos Test
			}
			@Override
			public boolean isActiv() {
				return GraphicsHandler.getDisplayType() == DisplayType.LOADINGSCREEN;
			}
		};
		
		//LOBBY STARTBUTTON
		new MouseActionArea(GraphicsHandler.getWidth()/2, GraphicsHandler.getHeight()/2, 100, 70,
				MouseActionAreaType.MAA_LOBBY_STARTBUTTON, "Starten", 20, Color.WHITE, Color.ORANGE) {
			@Override
			public void performAction_LEFT_RELEASE() {
				ConsoleHandler.print("Button was Clicked", MessageType.LOBBY);
			}
			@Override
			public boolean isActiv() {
				return GraphicsHandler.getDisplayType() == DisplayType.LOBBY;
			}
		};
		
		//LOBBY Unsichtbarer LEFT Button fuer Pfeil -> MAA_LOBBY_PFEILBUTTON_LEFT
		new MouseActionArea(GraphicsHandler.getWidth()/5, GraphicsHandler.getHeight()/2, 45, 44,
				MouseActionAreaType.MAA_LOBBY_PFEILBUTTON_LEFT, "Pfeil", 20, Color.DARK_GRAY, Color.ORANGE) { //Diese Werte sind belanglos, da ich die in draw sowieso überschreibe und nicht brauche
			@Override
			public void performAction_LEFT_RELEASE() {
				ConsoleHandler.print(" Left Pfeilbutton Button was Clicked", MessageType.LOBBY);
				Lobby_Create.setDecrementMap();
			}
			@Override
			public boolean isActiv() {
				return GraphicsHandler.getDisplayType() == DisplayType.LOBBY;
			}
			@Override
			public void draw(Graphics g) { 
				if(isHovered()) {
					g.drawImage(ImageHandler.getImage(ImageType.IMAGE_LOBBY_ARROW_LEFT_BIGGER).getImage(), GraphicsHandler.getWidth()/5, GraphicsHandler.getHeight()/2-1, null);
				}else {
					g.drawImage(ImageHandler.getImage(ImageType.IMAGE_LOBBY_ARROW_LEFT).getImage(), GraphicsHandler.getWidth()/5, GraphicsHandler.getHeight()/2, null);
				}
			}
		};
		
		//LOBBY Unsichtbarer RIGHT Button fuer Pfeil -> MAA_LOBBY_PFEILBUTTON_RIGHT
		new MouseActionArea(GraphicsHandler.getWidth()/3, GraphicsHandler.getHeight()/2, 45, 44,//Diese Werte sind nicht sichtbar, aber das sind die Werte wo ich dann klicke
				MouseActionAreaType.MAA_LOBBY_PFEILBUTTON_RIGHT, "Pfeil", 20, Color.DARK_GRAY, Color.ORANGE) {
			@Override
			public void performAction_LEFT_RELEASE() {
				ConsoleHandler.print("Right Pfeilbutton Button was Clicked", MessageType.LOBBY);
				Lobby_Create.setIncrementMap();
			}
			@Override
			public boolean isActiv() {
				return GraphicsHandler.getDisplayType() == DisplayType.LOBBY;
			}
			@Override
			public void draw(Graphics g) { 
				if(isHovered()) {
					// Die y Koordinate -3, weil ja von oben links das Bild geprinted wird und der Button so leicht nach unten verschoben wird
					g.drawImage(ImageHandler.getImage(ImageType.IMAGE_LOBBY_ARROW_RIGHT_BIGGER).getImage(), GraphicsHandler.getWidth()/3, GraphicsHandler.getHeight()/2-3, null);
				}else {
					g.drawImage(ImageHandler.getImage(ImageType.IMAGE_LOBBY_ARROW_RIGHT).getImage(), GraphicsHandler.getWidth()/3, GraphicsHandler.getHeight()/2, null);
				}
			}
		};
		
		ConsoleHandler.print("Initialised MouseActionAreas ("+mouseActionAreas.size()+")!", MessageType.BACKEND);
		
	}
	
	/**
	 * Returned eine MAA die dem angegebenene Type entspricht, falls diese gefunden wurde (Wenn richtig initialisiert wird immer eine gefunden!).
	 * Bei doppelter type zuordnung wird die zuerst gefundenen maa zur�ckgegeben.
	 * @param type - Der {@link MouseActionAreaType} nach dem gesucht werden soll
	 * @return null wenn keine maa mit dem type gefunden wurde, sonst die maa mit dem type
	 */
	public static MouseActionArea getMAA(MouseActionAreaType type) {
		for(MouseActionArea maa : getMAAs()) {
			if(maa.getType() == type) {
				return maa;
			}
		}
		return null;
	}
	
	public static List<MouseActionArea> getMAAs() {
		return mouseActionAreas;
	}
	
}
