/*
 * LobbyButtons
 *
 * Version 1.0
 * Author: Josias
 *
 * Vererbt MouseActionAreaHandler, wo alle Buttons der Lobby implementiert werden.
 */

package uni.bombenstimmung.de.lobby;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;

import uni.bombenstimmung.de.backend.console.ConsoleHandler;
import uni.bombenstimmung.de.backend.console.MessageType;
import uni.bombenstimmung.de.backend.graphics.DisplayType;
import uni.bombenstimmung.de.backend.graphics.GraphicsHandler;
import uni.bombenstimmung.de.backend.images.ImageHandler;
import uni.bombenstimmung.de.backend.images.ImageType;
import uni.bombenstimmung.de.backend.language.LanguageBlockType;
import uni.bombenstimmung.de.backend.language.LanguageHandler;
import uni.bombenstimmung.de.backend.maa.MouseActionArea;
import uni.bombenstimmung.de.backend.maa.MouseActionAreaHandler;
import uni.bombenstimmung.de.backend.maa.MouseActionAreaType;
import uni.bombenstimmung.de.menu.Menu;
import uni.bombenstimmung.de.menu.Settings;

public class LobbyButtons extends MouseActionAreaHandler{

    private static int yPlayer, yPlayerMap;
    private static int xPlayer1Left, xPlayer2Left, xPlayer3Left, xPlayer4Left;
    private static int xPlayer1Right, xPlayer2Right, xPlayer3Right, xPlayer4Right;

    public static MouseActionArea startLobby;
    public static MouseActionArea exitLobby;
    public static MouseActionArea mapleft;
    public static MouseActionArea mapright;
    public static MouseActionArea player1left;
    public static MouseActionArea player1right;
    public static MouseActionArea player2left;
    public static MouseActionArea player2right;
    public static MouseActionArea player3left;
    public static MouseActionArea player3right;
    public static MouseActionArea player4left;
    public static MouseActionArea player4right;
    public static MouseActionArea player2check;
    public static MouseActionArea player3check;
    public static MouseActionArea player4check;


    /**
     * Inititalisiert alle MAAs der Lobby und definiert via Overwrite restliche Funktionalitäten
     */
    public static void initLobbyButtons(){

	yPlayer = (int)(GraphicsHandler.getHeight()*0.28) + Settings.scaleValue(100);
	yPlayerMap = (int)(GraphicsHandler.getHeight()*0.55) + Settings.scaleValue(100);

	xPlayer1Left = GraphicsHandler.getWidth()/8   - Settings.scaleValue(205);
	xPlayer2Left = GraphicsHandler.getWidth()/8*3 - Settings.scaleValue(205);
	xPlayer3Left = GraphicsHandler.getWidth()/8*5 - Settings.scaleValue(205);
	xPlayer4Left = GraphicsHandler.getWidth()/8*7 - Settings.scaleValue(205);

	xPlayer1Right = GraphicsHandler.getWidth()/8   + Settings.scaleValue(150);
	xPlayer2Right = GraphicsHandler.getWidth()/8*3 + Settings.scaleValue(150);
	xPlayer3Right = GraphicsHandler.getWidth()/8*5 + Settings.scaleValue(150);
	xPlayer4Right = GraphicsHandler.getWidth()/8*7 + Settings.scaleValue(150);

	ConsoleHandler.print("starting: initLobbyButtons", MessageType.LOBBY);

	//LOBBY STARTBUTTON
	startLobby = new MouseActionArea((int)(GraphicsHandler.getWidth()*0.35 - Settings.scaleValue(100)), GraphicsHandler.getHeight()/4 + (GraphicsHandler.getHeight()/5)*3, Settings.scaleValue(200), Settings.scaleValue(100),
		MouseActionAreaType.MAA_LOBBY_STARTBUTTON, "START", Settings.scaleValue(40), Color.WHITE, Color.GREEN) {
	    @Override
	    public void performAction_LEFT_RELEASE() {
		// Nur der Host kann den Startbutton klicken
		if (LobbyCreate.client.isHost()) {
		    ConsoleHandler.print("Button was Clicked", MessageType.LOBBY);
		    int counterForReady = 1;
		    // Hier wird gecheckt, ob alle Player die Checkbox aktiviert haben
		    for(int i = 1; i < LobbyCreate.numberOfMaxPlayers ; i++) {
			if(LobbyCreate.player[i].getisReady() == true) {
			    counterForReady++;		    
			}
		    }
		    if (counterForReady == LobbyCreate.numberOfMaxPlayers) {
			ConsoleHandler.print("Ja, alle Player sind ready", MessageType.LOBBY);
			LobbyCreate.client.sendMessageToAllClients("515-");
			GraphicsHandler.switchToIngameFromLobby();
		    }
		}
	    }
	    @Override
	    public boolean isActiv() {
		if(GraphicsHandler.getDisplayType() == DisplayType.LOBBY) {
		    return true;  
		}
		else {
		    return false;   
		}
	    }
	    @Override
	    public void draw(Graphics g) {
		if(isHovered() && LobbyCreate.client.getId() == 0) {
		    g.setColor(Color.GREEN);
		    g.drawRect((int)(GraphicsHandler.getWidth()*0.35 - Settings.scaleValue(100)), GraphicsHandler.getHeight()/4 + (GraphicsHandler.getHeight()/5)*3, Settings.scaleValue(200), Settings.scaleValue(100));
		    GraphicsHandler.drawCentralisedText(g, Color.GREEN, Settings.scaleValue(40), "START", (int)(GraphicsHandler.getWidth()*0.35 - Settings.scaleValue(100))+Settings.scaleValue(200)/2, 
			    GraphicsHandler.getHeight()/4 + (GraphicsHandler.getHeight()/5)*3+Settings.scaleValue(100)/2); 	
		}
		else {
		    g.setColor(Color.WHITE);
		    g.drawRect((int)(GraphicsHandler.getWidth()*0.35 - Settings.scaleValue(100)), GraphicsHandler.getHeight()/4 + (GraphicsHandler.getHeight()/5)*3, Settings.scaleValue(200), Settings.scaleValue(100));
		    GraphicsHandler.drawCentralisedText(g, Color.WHITE, Settings.scaleValue(40), "START", (int)(GraphicsHandler.getWidth()*0.35 - Settings.scaleValue(100))+Settings.scaleValue(200)/2, 
			    GraphicsHandler.getHeight()/4 + (GraphicsHandler.getHeight()/5)*3+Settings.scaleValue(100)/2); 
		}
	    }
	};

	//LOBBY EXITBUTTON
	exitLobby = new MouseActionArea((int)(GraphicsHandler.getWidth()*0.65 - Settings.scaleValue(100)), GraphicsHandler.getHeight()/4 + (GraphicsHandler.getHeight()/5)*3, Settings.scaleValue(200), Settings.scaleValue(100),
		MouseActionAreaType.MAA_LOBBY_STARTBUTTON, LanguageHandler.getLLB(LanguageBlockType.LB_OPTIONS_BTN).getContent(), Settings.scaleValue(40), Color.WHITE, Color.RED) {
	    @Override
	    public void performAction_LEFT_RELEASE() {

		if (LobbyCreate.client.isHost()) {
		    // Wenn der Host nicht alleine in der Lobby ist
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
		GraphicsHandler.switchToMenuFromLobby();
	    }
	    @Override
	    public boolean isActiv() {
		return GraphicsHandler.getDisplayType() == DisplayType.LOBBY;
	    }
	};

	//LOBBY LEFT Button fuer Pfeil fuer MAP -> MAA_LOBBY_PFEILBUTTON_LEFT
	mapleft = new MouseActionArea(xPlayer1Left, yPlayerMap, 45, 44,
		MouseActionAreaType.MAA_LOBBY_PFEILBUTTON_LEFT, "Pfeil", 20, Color.DARK_GRAY, Color.ORANGE) { //Diese Werte sind belanglos, da ich die in draw sowieso überschreibe und nicht brauche
	    @Override
	    public void performAction_LEFT_RELEASE() {
		ConsoleHandler.print(" Left Pfeilbutton MAP Button was Clicked", MessageType.LOBBY);
		LobbyCreate.setDecrementMap();
	    }
	    @Override
	    public boolean isActiv() {
		if(GraphicsHandler.getDisplayType() == DisplayType.LOBBY && LobbyCreate.client.isHost()) {
		    return true; 
		}
		else {
		    return false;				    
		}

	    }
	    @Override
	    public void draw(Graphics g) { 
		if(isHovered()) {
		    g.drawImage(ImageHandler.getImage(ImageType.IMAGE_LOBBY_ARROW_LEFT).getImage(), xPlayer1Left - Settings.scaleValue(5), yPlayerMap - (int)(GraphicsHandler.getHeight()*0.003), Settings.scaleValue(70), Settings.scaleValue(70), null);
		}else {
		    g.drawImage(ImageHandler.getImage(ImageType.IMAGE_LOBBY_ARROW_LEFT).getImage(), xPlayer1Left, yPlayerMap, Settings.scaleValue(60), Settings.scaleValue(60), null);
		}
	    }
	};

	//LOBBY RIGHT Button fuer Pfeil fuer MAP -> MAA_LOBBY_PFEILBUTTON_RIGHT
	mapright = new MouseActionArea(GraphicsHandler.getWidth()/8 + Settings.scaleValue(150), yPlayerMap, 45, 44,//Diese Werte sind nicht sichtbar, aber das sind die Werte wo ich dann klicke
		MouseActionAreaType.MAA_LOBBY_PFEILBUTTON_RIGHT, "Pfeil", 20, Color.DARK_GRAY, Color.ORANGE) {
	    @Override
	    public void performAction_LEFT_RELEASE() {
		ConsoleHandler.print("Right Pfeilbutton MAP Button was Clicked", MessageType.LOBBY);
		LobbyCreate.setIncrementMap();
	    }
	    @Override
	    public boolean isActiv() {
		if(GraphicsHandler.getDisplayType() == DisplayType.LOBBY && LobbyCreate.client.isHost()) {
		    return true;	    
		}

		else {
		    return false;				    
		}
	    }
	    @Override
	    public void draw(Graphics g) { 
		if(isHovered()) {
		    // Die y Koordinate -3, weil ja von oben links das Bild geprinted wird und der Button so leicht nach unten verschoben wird
		    g.drawImage(ImageHandler.getImage(ImageType.IMAGE_LOBBY_ARROW_RIGHT).getImage(), xPlayer1Right - Settings.scaleValue(5), yPlayerMap - (int)(GraphicsHandler.getHeight()*0.003), Settings.scaleValue(70), Settings.scaleValue(70), null);
		}else {
		    g.drawImage(ImageHandler.getImage(ImageType.IMAGE_LOBBY_ARROW_RIGHT).getImage(), xPlayer1Right, yPlayerMap, Settings.scaleValue(60), Settings.scaleValue(60), null);
		}
	    }
	};

	///////////////////////////////// ALLE BUTTONS FÜR DIE SKIN SELECTION ////////////////////////////////////////////////////////

	// PLAYER 1  LEFT
	player1left = new MouseActionArea(xPlayer1Left, yPlayer, 45, 44,
		MouseActionAreaType.MAA_LOBBY_PFEILBUTTON_LEFT, "Pfeil", 20, Color.DARK_GRAY, Color.ORANGE) {
	    @Override
	    public void performAction_LEFT_RELEASE() {
		ConsoleHandler.print("Left Player 1", MessageType.LOBBY);
		LobbyCreate.setDecrementSkin(0);
	    }
	    @Override
	    public boolean isActiv() {
		if(GraphicsHandler.getDisplayType() == DisplayType.LOBBY && LobbyCreate.client.getId() == 0) {
		    return true; 
		}
		else {
		    return false;    
		}
	    }
	    @Override
	    public void draw(Graphics g) {
		if(isHovered()) {
		    g.drawImage(ImageHandler.getImage(ImageType.IMAGE_LOBBY_ARROW_LEFT).getImage(), xPlayer1Left - Settings.scaleValue(5), yPlayer, Settings.scaleValue(70), Settings.scaleValue(70), null);
		}else {
		    g.drawImage(ImageHandler.getImage(ImageType.IMAGE_LOBBY_ARROW_LEFT).getImage(), xPlayer1Left, yPlayer, Settings.scaleValue(60), Settings.scaleValue(60), null);
		}
	    }
	};

	// PLAYER 1  RIGHT
	player1right = new MouseActionArea(xPlayer1Right, yPlayer, 45, 44,
		MouseActionAreaType.MAA_LOBBY_PFEILBUTTON_RIGHT, "Pfeil", 20, Color.DARK_GRAY, Color.ORANGE) {
	    @Override
	    public void performAction_LEFT_RELEASE() {
		ConsoleHandler.print("Right Player 1", MessageType.LOBBY);
		LobbyCreate.setIncrementSkin(0);
	    }
	    @Override
	    public boolean isActiv() {
		if(GraphicsHandler.getDisplayType() == DisplayType.LOBBY && LobbyCreate.client.getId() == 0) {
		    return true;    
		}
		else {
		    return false;				    
		}
	    }
	    @Override
	    public void draw(Graphics g) {
		if(isHovered()) {
		    g.drawImage(ImageHandler.getImage(ImageType.IMAGE_LOBBY_ARROW_RIGHT).getImage(), xPlayer1Right, yPlayer - (int)(GraphicsHandler.getWidth()*0.0015), Settings.scaleValue(70), Settings.scaleValue(70), null);
		}else {
		    g.drawImage(ImageHandler.getImage(ImageType.IMAGE_LOBBY_ARROW_RIGHT).getImage(), xPlayer1Right, yPlayer, Settings.scaleValue(60), Settings.scaleValue(60), null);
		}
	    }
	};

	// PLAYER 2  LEFT
	player2left = new MouseActionArea(xPlayer2Left, yPlayer, 45, 44,
		MouseActionAreaType.MAA_LOBBY_PFEILBUTTON_LEFT, "Pfeil", 20, Color.DARK_GRAY, Color.ORANGE) {
	    @Override
	    public void performAction_LEFT_RELEASE() {
		ConsoleHandler.print("Left Player 2", MessageType.LOBBY);
		LobbyCreate.setDecrementSkin(1);
	    }
	    @Override
	    public boolean isActiv() {
		if(GraphicsHandler.getDisplayType() == DisplayType.LOBBY && LobbyCreate.client.getId() == 1) {
		    return true;  
		}
		else{
		    return false;
		}	
	    }
	    @Override
	    public void draw(Graphics g) {
		if(isHovered()) {
		    g.drawImage(ImageHandler.getImage(ImageType.IMAGE_LOBBY_ARROW_LEFT).getImage(),xPlayer2Left - Settings.scaleValue(5), yPlayer, Settings.scaleValue(70), Settings.scaleValue(70), null);
		}else {
		    g.drawImage(ImageHandler.getImage(ImageType.IMAGE_LOBBY_ARROW_LEFT).getImage(), xPlayer2Left, yPlayer, Settings.scaleValue(60), Settings.scaleValue(60), null);
		}
	    }
	};

	// PLAYER 2  RIGHT
	player2right = new MouseActionArea(xPlayer2Right, yPlayer, 45, 44,
		MouseActionAreaType.MAA_LOBBY_PFEILBUTTON_RIGHT, "Pfeil", 20, Color.DARK_GRAY, Color.ORANGE) {
	    @Override
	    public void performAction_LEFT_RELEASE() {
		ConsoleHandler.print("Right Player 2", MessageType.LOBBY);
		LobbyCreate.setIncrementSkin(1);
	    }
	    @Override
	    public boolean isActiv() {
		if(GraphicsHandler.getDisplayType() == DisplayType.LOBBY && LobbyCreate.client.getId() == 1) {
		    return true;  
		}
		else {
		    return false; 
		}
	    }
	    @Override
	    public void draw(Graphics g) {
		if(isHovered()) {
		    g.drawImage(ImageHandler.getImage(ImageType.IMAGE_LOBBY_ARROW_RIGHT).getImage(), xPlayer2Right, yPlayer - (int)(GraphicsHandler.getWidth()*0.0015), Settings.scaleValue(70), Settings.scaleValue(70), null);
		}else {
		    g.drawImage(ImageHandler.getImage(ImageType.IMAGE_LOBBY_ARROW_RIGHT).getImage(), xPlayer2Right, yPlayer, Settings.scaleValue(60), Settings.scaleValue(60) , null);
		}
	    }
	};

	// PLAYER 3  LEFT
	player3left = new MouseActionArea(xPlayer3Left, yPlayer, 45, 44,
		MouseActionAreaType.MAA_LOBBY_PFEILBUTTON_LEFT, "Pfeil", 20, Color.DARK_GRAY, Color.ORANGE) {
	    @Override
	    public void performAction_LEFT_RELEASE() {
		ConsoleHandler.print("Left Player 3", MessageType.LOBBY);
		LobbyCreate.setDecrementSkin(2);
	    }
	    @Override
	    public boolean isActiv() {
		if(GraphicsHandler.getDisplayType() == DisplayType.LOBBY && LobbyCreate.client.getId() == 2) {
		    return true;    
		}
		else {
		    return false; 
		}

	    }
	    @Override
	    public void draw(Graphics g) {
		if(isHovered()) {
		    g.drawImage(ImageHandler.getImage(ImageType.IMAGE_LOBBY_ARROW_LEFT).getImage(),xPlayer3Left - Settings.scaleValue(5), yPlayer, Settings.scaleValue(70), Settings.scaleValue(70), null);
		}else {
		    g.drawImage(ImageHandler.getImage(ImageType.IMAGE_LOBBY_ARROW_LEFT).getImage(), xPlayer3Left, yPlayer, Settings.scaleValue(60), Settings.scaleValue(60), null);
		}
	    }
	};

	// PLAYER 3  RIGHT
	player3right = new MouseActionArea(xPlayer3Right, yPlayer, 45, 44,
		MouseActionAreaType.MAA_LOBBY_PFEILBUTTON_RIGHT, "Pfeil", 20, Color.DARK_GRAY, Color.ORANGE) {
	    @Override
	    public void performAction_LEFT_RELEASE() {
		ConsoleHandler.print("Right Player 3", MessageType.LOBBY);
		LobbyCreate.setIncrementSkin(2);
	    }
	    @Override
	    public boolean isActiv() {
		if(GraphicsHandler.getDisplayType() == DisplayType.LOBBY && LobbyCreate.client.getId() == 2) {
		    return true; 
		}
		else {
		    return false;
		} 
	    }
	    @Override
	    public void draw(Graphics g) {
		if(isHovered()) {
		    g.drawImage(ImageHandler.getImage(ImageType.IMAGE_LOBBY_ARROW_RIGHT).getImage(), xPlayer3Right, yPlayer - (int)(GraphicsHandler.getWidth()*0.0015), Settings.scaleValue(70), Settings.scaleValue(70), null);
		}else {
		    g.drawImage(ImageHandler.getImage(ImageType.IMAGE_LOBBY_ARROW_RIGHT).getImage(), xPlayer3Right, yPlayer, Settings.scaleValue(60), Settings.scaleValue(60) , null);
		}
	    }
	};

	// PLAYER 4  LEFT
	player4left = new MouseActionArea(xPlayer4Left, yPlayer, 45, 44,
		MouseActionAreaType.MAA_LOBBY_PFEILBUTTON_LEFT, "Pfeil", 20, Color.DARK_GRAY, Color.ORANGE) {
	    @Override
	    public void performAction_LEFT_RELEASE() {
		ConsoleHandler.print("Left Player 4", MessageType.LOBBY);
		LobbyCreate.setDecrementSkin(3);
	    }
	    @Override
	    public boolean isActiv() {
		if(GraphicsHandler.getDisplayType() == DisplayType.LOBBY && LobbyCreate.client.getId() == 3)
		    return true;
		else
		    return false;
	    }
	    @Override
	    public void draw(Graphics g) {
		if(isHovered()) {
		    g.drawImage(ImageHandler.getImage(ImageType.IMAGE_LOBBY_ARROW_LEFT).getImage(),xPlayer4Left - Settings.scaleValue(5), yPlayer, Settings.scaleValue(70), Settings.scaleValue(70), null);
		}else {
		    g.drawImage(ImageHandler.getImage(ImageType.IMAGE_LOBBY_ARROW_LEFT).getImage(), xPlayer4Left, yPlayer, Settings.scaleValue(60), Settings.scaleValue(60), null);
		}
	    }
	};

	// PLAYER 4  RIGHT
	player4right = new MouseActionArea(xPlayer4Right, yPlayer, 45, 44,
		MouseActionAreaType.MAA_LOBBY_PFEILBUTTON_RIGHT, "Pfeil", 20, Color.DARK_GRAY, Color.ORANGE) {
	    @Override
	    public void performAction_LEFT_RELEASE() {
		ConsoleHandler.print("Right Player 4", MessageType.LOBBY);
		LobbyCreate.setIncrementSkin(3);
	    }
	    @Override
	    public boolean isActiv() {
		if(GraphicsHandler.getDisplayType() == DisplayType.LOBBY && LobbyCreate.client.getId() == 3)
		    return true;
		else
		    return false;
	    }
	    @Override
	    public void draw(Graphics g) {
		if(isHovered()) {
		    g.drawImage(ImageHandler.getImage(ImageType.IMAGE_LOBBY_ARROW_RIGHT).getImage(), xPlayer4Right, yPlayer - (int)(GraphicsHandler.getWidth()*0.0015), Settings.scaleValue(70), Settings.scaleValue(70), null);
		}else {
		    g.drawImage(ImageHandler.getImage(ImageType.IMAGE_LOBBY_ARROW_RIGHT).getImage(), xPlayer4Right, yPlayer, Settings.scaleValue(60), Settings.scaleValue(60), null);
		}
	    }
	};

	///////////////////////////////// ALLE BUTTONS FUER DIE SKIN SELECTION ////////////////////////////////////////////////////////




	///////////////////////////////// ALLE BUTTONS FÜR DIE CHECKBOXEN PLAYER 2-4 ////////////////////////////////////////////////////////

	// Player 2
	player2check = new MouseActionArea(xPlayer2Left+Settings.scaleValue(140), yPlayerMap-Settings.scaleValue(50), Settings.scaleValue(130), Settings.scaleValue(80),
		MouseActionAreaType.MAA_LOBBY_CHECKMARK, LanguageHandler.getLLB(LanguageBlockType.LB_LOBBY_READY).getContent(), Settings.scaleValue(30), Color.DARK_GRAY, Color.GREEN) {
	    @Override
	    public void performAction_LEFT_RELEASE() {
		if (LobbyCreate.client.getId() == 1) {
		    ConsoleHandler.print("CheckBox Player 2", MessageType.LOBBY);
		    LobbyCreate.setisReady(1);
		}
	    }
	    @Override
	    public boolean isActiv() {
		if(GraphicsHandler.getDisplayType() == DisplayType.LOBBY && LobbyCreate.numberOfMaxPlayers >= 2 && LobbyCreate.player[1] != null) {
		    return true;    
		}
		else {
		    return false;  
		}
	    }
	    @Override
	    public void draw(Graphics g){
		if(LobbyCreate.player[1] != null) {
		    if(LobbyCreate.client.getId() == 1){
			if(isHovered()) {
			    g.setColor(Color.GREEN);
			}
			else {
			    g.setColor(Color.DARK_GRAY);
			}
			g.drawRect(xPlayer2Left+Settings.scaleValue(158), yPlayerMap-Settings.scaleValue(50), Settings.scaleValue(80), Settings.scaleValue(80));
		    }

		    if(LobbyCreate.player[1].getisReady() == true) {
			g.drawImage(ImageHandler.getImage(ImageType.IMAGE_LOBBY_CHECKMARK).getImage(), xPlayer2Left+Settings.scaleValue(168), yPlayerMap-Settings.scaleValue(46), Settings.scaleValue(70), Settings.scaleValue(70), null);
		    }
		    else {
			g.drawImage(ImageHandler.getImage(ImageType.IMAGE_LOBBY_CROSS).getImage(), xPlayer2Left+Settings.scaleValue(167), yPlayerMap-Settings.scaleValue(46), Settings.scaleValue(70), Settings.scaleValue(70), null);
		    }
		}
	    }
	};

	// Player 3
	player3check = new MouseActionArea(xPlayer3Left+Settings.scaleValue(140), yPlayerMap-Settings.scaleValue(50), Settings.scaleValue(130), Settings.scaleValue(80),
		MouseActionAreaType.MAA_LOBBY_CHECKMARK, LanguageHandler.getLLB(LanguageBlockType.LB_LOBBY_READY).getContent(), Settings.scaleValue(30), Color.DARK_GRAY, Color.GREEN) {
	    @Override
	    public void performAction_LEFT_RELEASE() {
		if (LobbyCreate.client.getId() == 2) {
		    ConsoleHandler.print("CheckBox Player 3", MessageType.LOBBY);
		    LobbyCreate.setisReady(2);
		}
	    }
	    @Override
	    public boolean isActiv() {
		if(GraphicsHandler.getDisplayType() == DisplayType.LOBBY && LobbyCreate.numberOfMaxPlayers >= 3 && LobbyCreate.player[2] != null) {
		    return true;    
		}
		else {
		    return false;  
		}
	    }
	    @Override
	    public void draw(Graphics g){
		if(LobbyCreate.player[2] != null) {
		    if(LobbyCreate.client.getId() == 2){
			if(isHovered()) {
			    g.setColor(Color.GREEN);
			}
			else {
			    g.setColor(Color.DARK_GRAY);
			}
			g.drawRect(xPlayer3Left+Settings.scaleValue(158), yPlayerMap-Settings.scaleValue(50), Settings.scaleValue(80), Settings.scaleValue(80));
		    }

		    if(LobbyCreate.player[2].getisReady() == true) {
			g.drawImage(ImageHandler.getImage(ImageType.IMAGE_LOBBY_CHECKMARK).getImage(), xPlayer3Left+Settings.scaleValue(168), yPlayerMap-Settings.scaleValue(46), Settings.scaleValue(70), Settings.scaleValue(70), null); 
		    }
		    else {
			g.drawImage(ImageHandler.getImage(ImageType.IMAGE_LOBBY_CROSS).getImage(), xPlayer3Left+Settings.scaleValue(167), yPlayerMap-Settings.scaleValue(46), Settings.scaleValue(70), Settings.scaleValue(70), null); 
		    }
		}				
	    }
	};

	// Player 4
	player4check = new MouseActionArea(xPlayer4Left+Settings.scaleValue(140), yPlayerMap-Settings.scaleValue(50), Settings.scaleValue(130), Settings.scaleValue(80),
		MouseActionAreaType.MAA_LOBBY_CHECKMARK, LanguageHandler.getLLB(LanguageBlockType.LB_LOBBY_READY).getContent(), Settings.scaleValue(30), Color.DARK_GRAY, Color.GREEN) {
	    @Override
	    public void performAction_LEFT_RELEASE() {
		if (LobbyCreate.client.getId() == 3) {
		    ConsoleHandler.print("CheckBox Player 4", MessageType.LOBBY);
		    LobbyCreate.setisReady(3);
		}
	    }
	    @Override
	    public boolean isActiv() {
		if(GraphicsHandler.getDisplayType() == DisplayType.LOBBY && LobbyCreate.numberOfMaxPlayers == 4 && LobbyCreate.player[3] != null) {
		    return true;
		}
		else {
		    return false;
		}	
	    }
	    @Override
	    public void draw(Graphics g){
		if(LobbyCreate.player[3] != null) {
		    if(LobbyCreate.client.getId() == 3){
			if(isHovered()) {
			    g.setColor(Color.GREEN);
			}
			else {
			    g.setColor(Color.DARK_GRAY);
			}
			g.drawRect(xPlayer4Left+Settings.scaleValue(158), yPlayerMap-Settings.scaleValue(50), Settings.scaleValue(80), Settings.scaleValue(80));
		    }

		    if(LobbyCreate.player[3].getisReady() == true) {
			g.drawImage(ImageHandler.getImage(ImageType.IMAGE_LOBBY_CHECKMARK).getImage(),
				xPlayer4Left+Settings.scaleValue(168), yPlayerMap-Settings.scaleValue(46), Settings.scaleValue(70), Settings.scaleValue(70), null);
		    }
		    else {
			g.drawImage(ImageHandler.getImage(ImageType.IMAGE_LOBBY_CROSS).getImage(), xPlayer4Left+Settings.scaleValue(167), yPlayerMap-Settings.scaleValue(46), Settings.scaleValue(70), Settings.scaleValue(70), null);
		    }
		}	
	    }
	};


	///////////////////////////////// ALLE BUTTONS FÜR DIE CHECKBOXEN PLAYER 2-4 ////////////////////////////////////////////////////////	
    }

    public static void lobbyButtonsReset() {
	ConsoleHandler.print("reseting Lobbymenu MAAs", MessageType.LOBBY);
	startLobby.remove();
	exitLobby.remove();
	mapleft.remove();
	mapright.remove();
	player1left.remove();
	player1right.remove();
	player2left.remove();
	player2right.remove();
	player3left.remove();
	player3right.remove();
	player4left.remove();
	player4right.remove();
	player2check.remove();
	player3check.remove();
	player4check.remove();
	Menu.sleep(50);
	initLobbyButtons();
    }
}
