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
import uni.bombenstimmung.de.backend.serverconnection.host.ServerHandler;
import uni.bombenstimmung.de.lobby.LobbyCreate;

public class AftergameButtons extends MouseActionAreaHandler{
    
    public static void initAftergameButtons(){
	
	new MouseActionArea(GraphicsHandler.getWidth()*3/4-100, GraphicsHandler.getHeight()*6/8, 200, 100,
		MouseActionAreaType.MAA_AFTERGAME, LanguageHandler.getLLB(LanguageBlockType.LB_AFTERGAME_BT3).getContent(), 20, new Color(225,0,0), new Color(0,0,255)) {
	    @Override
	    public void performAction_LEFT_RELEASE() {
//	    	if (DeadPlayerHandler.getClientPlayer().isHost()) {
//	    	    DeadPlayerHandler.getClientPlayer().getCC().sendMessageToAllClients("602-");
//	    	}
//	    	DeadPlayerHandler.getClientPlayer().getCC().
//		GraphicsHandler.shutdownProgram();
	    	
	    	if (DeadPlayerHandler.getClientPlayer().isHost()) {
	    	    DeadPlayerHandler.getClientPlayer().getCC().sendMessageToAllClients("602-");
	    	    GraphicsHandler.shutdownProgram();
	    	}
	    	else {
	    	    LobbyCreate.client.sendMessage(LobbyCreate.client.getSession(), "512-" + LobbyCreate.client.getId());
	    	    DeadPlayerHandler.getClientPlayer().getCC().getConnector().dispose();
	    	    GraphicsHandler.shutdownProgram();
	    	}
	    }
	    @Override
	    public boolean isActiv() {
		return GraphicsHandler.getDisplayType() == DisplayType.AFTERGAME;
	    }
	};

	new MouseActionArea(GraphicsHandler.getWidth()*1/4-100, GraphicsHandler.getHeight()*6/8, 200, 100,
		MouseActionAreaType.MAA_AFTERGAME, LanguageHandler.getLLB(LanguageBlockType.LB_AFTERGAME_BT1).getContent(), 20, new Color(225,0,0), new Color(0,0,255).darker()) {
	    @Override
	    public void performAction_LEFT_RELEASE() {
	    	if (LobbyCreate.client.isHost()) {
	    	    // Wenn der Host nicht alleine in der Lobby ist
	    	    if (LobbyCreate.numberOfMaxPlayers > 1) {
	    		LobbyCreate.client.sendMessageToAllClients("514-");
	    		LobbyCreate.client.getAcceptor().dispose();
	    		ConsoleHandler.print("Server disposed " + LobbyCreate.client.getAcceptor().isDisposed() + " ... ", MessageType.BACKEND);
	    	    }
	    	}
	    	else {
	    	// Wenn der Client nicht alleine in der Lobby ist
	    	if (LobbyCreate.numberOfMaxPlayers > 1) {
	    	    LobbyCreate.client.sendMessage(LobbyCreate.client.getSession(), "512-" + LobbyCreate.client.getId());
	    	    LobbyCreate.client.getConnector().dispose();
	    	}
	    	}
	    	
	    	// Setze alle Objekte = null und switche ins Menu
	    	for (int i=0; i< LobbyCreate.numberOfMaxPlayers; i++) {
		    LobbyCreate.player[i] = null;
	    	}	
	    	LobbyCreate.numberOfMaxPlayers = 0;
	    	GraphicsHandler.lobby = null;
	    	// Schliessung der Session
//	    	LobbyCreate.client.getSession().closeNow();
		GraphicsHandler.switchToMenuFromAftergame();
	    }
	    @Override
	    public boolean isActiv() {
		return GraphicsHandler.getDisplayType() == DisplayType.AFTERGAME;
	    }
	};
	
	new MouseActionArea(GraphicsHandler.getWidth()*2/4-100, GraphicsHandler.getHeight()*6/8, 200, 100,
		MouseActionAreaType.MAA_AFTERGAME, LanguageHandler.getLLB(LanguageBlockType.LB_AFTERGAME_BT2).getContent(), 20, new Color(225,0,0), new Color(0,0,255).darker()) {
	    @Override
	    public void performAction_LEFT_RELEASE() {
		if (DeadPlayerHandler.getClientPlayer().isHost()) {
		    ConsoleHandler.print("Button Back to Lobby", MessageType.AFTERGAME);
		    GraphicsHandler.switchToLobbyFromAftergame();
		    LobbyCreate.client.sendMessageToAllClients("600-");

		}

	    }
	    @Override
	    public boolean isActiv() {
		return GraphicsHandler.getDisplayType() == DisplayType.AFTERGAME;
	    }
	};
    }

}
