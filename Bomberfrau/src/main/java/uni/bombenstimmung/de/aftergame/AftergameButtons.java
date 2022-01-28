/*
 * AfretgameButtos
 *
 * Version 1.0
 * 
 * Author: Alexej
 * 
 *
 * konfiguration der Buttons im Aftergame
 */

package uni.bombenstimmung.de.aftergame;

import java.awt.Color;

import uni.bombenstimmung.de.backend.console.ConsoleHandler;
import uni.bombenstimmung.de.backend.console.MessageType;
import uni.bombenstimmung.de.backend.graphics.DisplayType;
import uni.bombenstimmung.de.backend.graphics.GraphicsHandler;
import uni.bombenstimmung.de.backend.language.LanguageBlockType;
import uni.bombenstimmung.de.backend.language.LanguageHandler;
import uni.bombenstimmung.de.backend.maa.MouseActionArea;
import uni.bombenstimmung.de.backend.maa.MouseActionAreaHandler;
import uni.bombenstimmung.de.backend.maa.MouseActionAreaType;
import uni.bombenstimmung.de.lobby.LobbyCreate;
import uni.bombenstimmung.de.menu.Menu;
import uni.bombenstimmung.de.menu.Settings;

public class AftergameButtons extends MouseActionAreaHandler{
    
    public static MouseActionArea mma_aftergame_1;
    public static MouseActionArea mma_aftergame_2;
    public static MouseActionArea mma_aftergame_3;
    
    
    public static void aftergameButtonsReset() {
	ConsoleHandler.print("reseting Aftergame MAAs", MessageType.AFTERGAME);
	mma_aftergame_1.remove();
	mma_aftergame_2.remove();
	mma_aftergame_3.remove();
	Menu.sleep(50);
	initAftergameButtons();
    }
    
    public static void initAftergameButtons(){
	
	//Back to Menu Button
	mma_aftergame_1 = new MouseActionArea(GraphicsHandler.getWidth()*1/4-100, GraphicsHandler.getHeight()*6/8, Settings.scaleValue(300), Settings.scaleValue(100),
		MouseActionAreaType.MAA_AFTERGAME, LanguageHandler.getLLB(LanguageBlockType.LB_AFTERGAME_BT1).getContent(), 20, new Color(225,0,0), new Color(0,0,255).darker()) {
	    @Override
	    public void performAction_LEFT_RELEASE() {
		ConsoleHandler.print("Back to Menu Button", MessageType.AFTERGAME);
	    	if (LobbyCreate.client.isHost()) {
	    	    // Wenn der Host nicht alleine in der Session ist
	    	    if (LobbyCreate.numberOfMaxPlayers > 1) {
	    		LobbyCreate.client.sendMessageToAllClients("514-");
	    		LobbyCreate.client.getAcceptor().dispose();
	    		ConsoleHandler.print("Server disposed " + LobbyCreate.client.getAcceptor().isDisposed() + " ... ", MessageType.BACKEND);
	    	    }
	    	    else {
			LobbyCreate.client.getAcceptor().dispose();
	    	    }
	    	}
	    	else {
	    	    // Wenn der Client nicht alleine in der Session ist
        	    if (LobbyCreate.numberOfMaxPlayers > 1) {
        		LobbyCreate.client.sendMessage(LobbyCreate.client.getSession(), "512-" + LobbyCreate.client.getId());
        	    	LobbyCreate.client.getConnector().dispose();
        	    }
        	    else {
			LobbyCreate.client.getConnector().dispose();
        	    }
	    	}
	    	
	    	// Setze alle Objekte = null und switche ins Menu
	    	for (int i=0; i< LobbyCreate.numberOfMaxPlayers; i++) {
		    LobbyCreate.player[i] = null;
	    	}	
	    	LobbyCreate.numberOfMaxPlayers = 0;
	    	GraphicsHandler.lobby = null;
		GraphicsHandler.switchToMenuFromAftergame();
	    }
	    @Override
	    public boolean isActiv() {
		//Button soll nur im Aftergame angezeigt werden
		return GraphicsHandler.getDisplayType() == DisplayType.AFTERGAME;
	    }
	};
	
	//Back to Lobby Button
	mma_aftergame_2 = new MouseActionArea(GraphicsHandler.getWidth()*2/4-100, GraphicsHandler.getHeight()*6/8, Settings.scaleValue(300), Settings.scaleValue(100),
		MouseActionAreaType.MAA_AFTERGAME, LanguageHandler.getLLB(LanguageBlockType.LB_AFTERGAME_BT2).getContent(), 20, new Color(225,0,0), new Color(0,0,255).darker()) {
	    @Override
	    public void performAction_LEFT_RELEASE() {
		ConsoleHandler.print("Back to Lobby Button", MessageType.AFTERGAME);
		
		//Nur der Host darf eine neue Runde starten. Clients wechseln in die Lobby
		if (DeadPlayerHandler.getClientPlayer().isHost()) {
		    GraphicsHandler.switchToLobbyFromAftergame();
		    DeadPlayerHandler.getClientPlayer().getCC().sendMessageToAllClients("600-");
		}

	    }
	    @Override
	    public boolean isActiv() {
		//Button soll nur im Aftergame angezeigt werden
		return GraphicsHandler.getDisplayType() == DisplayType.AFTERGAME;
	    }
	};
	
	//EXIT Button
	mma_aftergame_3 = new MouseActionArea(GraphicsHandler.getWidth()*3/4-100, GraphicsHandler.getHeight()*6/8, Settings.scaleValue(300), Settings.scaleValue(100),
		MouseActionAreaType.MAA_AFTERGAME, LanguageHandler.getLLB(LanguageBlockType.LB_AFTERGAME_BT3).getContent(), 20, new Color(225,0,0), new Color(0,0,255)) {
	    @Override
	    public void performAction_LEFT_RELEASE() {
		ConsoleHandler.print("EXIT Button", MessageType.AFTERGAME);
		
		//Wenn Host EXIT druekt, sollen alle anderen Spieler ins Menu wechseln und das Programm vom Host wird beendet.
	    	if (DeadPlayerHandler.getClientPlayer().isHost()) {
	    	    DeadPlayerHandler.getClientPlayer().getCC().sendMessageToAllClients("602-");
	    	    DeadPlayerHandler.getClientPlayer().getCC().getAcceptor().dispose();
	    	    GraphicsHandler.shutdownProgram();
	    	}
	    	//Wenn ein Client EXIT druekt, soll der Host über das Verlassen informiert weden und das Programm wird beendet.
	    	else {
	    	    LobbyCreate.client.sendMessage(LobbyCreate.client.getSession(), "512-" + LobbyCreate.client.getId());
	    	    DeadPlayerHandler.getClientPlayer().getCC().getConnector().dispose();
	    	    GraphicsHandler.shutdownProgram();
	    	}
	    }
	    @Override
	    public boolean isActiv() {
		//Button soll nur im Aftergame angezeigt werden
		return GraphicsHandler.getDisplayType() == DisplayType.AFTERGAME;
	    }
	};
    }

}
