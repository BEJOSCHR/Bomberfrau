package uni.bombenstimmung.de.aftergame;

import java.awt.Color;

import uni.bombenstimmung.de.backend.console.ConsoleHandler;
import uni.bombenstimmung.de.backend.console.MessageType;
import uni.bombenstimmung.de.backend.graphics.DisplayType;
import uni.bombenstimmung.de.backend.graphics.GraphicsHandler;
import uni.bombenstimmung.de.backend.maa.MouseActionArea;
import uni.bombenstimmung.de.backend.maa.MouseActionAreaHandler;
import uni.bombenstimmung.de.backend.maa.MouseActionAreaType;
import uni.bombenstimmung.de.backend.serverconnection.host.ServerHandler;
import uni.bombenstimmung.de.lobby.LobbyCreate;

public class AftergameButtons extends MouseActionAreaHandler{
    
    public static void initAftergameButtons(){
	
	new MouseActionArea(GraphicsHandler.getWidth()*3/4-100, GraphicsHandler.getHeight()*6/8, 200, 100,
		MouseActionAreaType.MAA_AFTERGAME, "Exit", 20, new Color(225,0,0), new Color(0,0,255)) {
	    @Override
	    public void performAction_LEFT_RELEASE() {
		//ServerHandler.sessionClosed();
		GraphicsHandler.shutdownProgram();
	    }
	    @Override
	    public boolean isActiv() {
		return GraphicsHandler.getDisplayType() == DisplayType.AFTERGAME;
	    }
	};

	new MouseActionArea(GraphicsHandler.getWidth()*1/4-100, GraphicsHandler.getHeight()*6/8, 200, 100,
		MouseActionAreaType.MAA_AFTERGAME, "Back to Menu", 20, new Color(225,0,0), new Color(0,0,255).darker()) {
	    @Override
	    public void performAction_LEFT_RELEASE() {
		GraphicsHandler.switchToMenuFromAftergame();
	    }
	    @Override
	    public boolean isActiv() {
		return GraphicsHandler.getDisplayType() == DisplayType.AFTERGAME;
	    }
	};
	
	new MouseActionArea(GraphicsHandler.getWidth()*2/4-100, GraphicsHandler.getHeight()*6/8, 200, 100,
		MouseActionAreaType.MAA_AFTERGAME, "Back to Lobby", 20, new Color(225,0,0), new Color(0,0,255).darker()) {
	    @Override
	    public void performAction_LEFT_RELEASE() {
		GraphicsHandler.switchToLobbyFromAftergame();
	    }
	    @Override
	    public boolean isActiv() {
		return GraphicsHandler.getDisplayType() == DisplayType.AFTERGAME;
	    }
	};
    }

}
