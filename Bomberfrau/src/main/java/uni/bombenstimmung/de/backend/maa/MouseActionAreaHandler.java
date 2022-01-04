/*
 * MouseActionAreaHandler
 *
 * Version 1.0
 * Author: Benni
 *
 * Verwaltet alle MouseActionAreas aka Custombuttons (laden, ausgeben, verï¿½ndern...)
 */
package uni.bombenstimmung.de.backend.maa;

import java.util.ArrayList;
import java.util.List;

import uni.bombenstimmung.de.backend.console.ConsoleHandler;
import uni.bombenstimmung.de.backend.console.MessageType;
import uni.bombenstimmung.de.lobby.LobbyButtons;
import uni.bombenstimmung.de.menu.Menu;

public class MouseActionAreaHandler {
	
	private static List<MouseActionArea> mouseActionAreas = new ArrayList<MouseActionArea>(); 
	
	/**
	 * Inititalisiert alle MAAs und defeniert via Overwrite die Funktionalitï¿½t
	 */
	public static void initMAAs() {
		
	    	Menu.initMaaIntro();
	    	Menu.initMaaMainmenu();
	    	Menu.initMaaOptions();

		LobbyButtons.initLobbyButtons();
		
		ConsoleHandler.print("Initialised MouseActionAreas ("+mouseActionAreas.size()+")!", MessageType.BACKEND);
		
	}
	
	
	/**
	 * Returned eine MAA die dem angegebenene Type entspricht, falls diese gefunden wurde (Wenn richtig initialisiert wird immer eine gefunden!).
	 * Bei doppelter type zuordnung wird die zuerst gefundenen maa zurückgegeben.
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
