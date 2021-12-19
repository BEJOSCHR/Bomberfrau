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
import uni.bombenstimmung.de.lobby.OwnButtons;

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
		
		OwnButtons LobbyButtons = new OwnButtons();
		
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
