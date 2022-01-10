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
import uni.bombenstimmung.de.backend.maa.MouseActionArea;
import uni.bombenstimmung.de.backend.maa.MouseActionAreaHandler;
import uni.bombenstimmung.de.backend.maa.MouseActionAreaType;

public class LobbyButtons extends MouseActionAreaHandler{
	
	static boolean rightisPressed = false;
	static boolean leftisPressed = false;
	
	private static int yPlayer = (int)(GraphicsHandler.getHeight()*0.15) + (int)(GraphicsHandler.getHeight()*0.1) + 75;
	private static int yPlayerMap = (int)(GraphicsHandler.getHeight()*0.15) + (int)((GraphicsHandler.getHeight()*0.1)*3.5) + 75;
	
	private static int xPlayer1Left = (int)((((GraphicsHandler.getWidth()/4)/2) + ((GraphicsHandler.getWidth()/4)/2)*0*2)-100 - GraphicsHandler.getWidth()*0.04);
	private static int xPlayer1Right = (int)((((GraphicsHandler.getWidth()/4)/2) + ((GraphicsHandler.getWidth()/4)/2)*0*2)+100 + GraphicsHandler.getWidth()*0.04) - 50;
	
	private static int xPlayer2Left = (int)((((GraphicsHandler.getWidth()/4)/2) + ((GraphicsHandler.getWidth()/4)/2)*1*2)-100 - GraphicsHandler.getWidth()*0.04);
	private static int xPlayer2Right = (int)((((GraphicsHandler.getWidth()/4)/2) + ((GraphicsHandler.getWidth()/4)/2)*1*2)+100 + GraphicsHandler.getWidth()*0.04) - 50;
	
	private static int xPlayer3Left = (int)((((GraphicsHandler.getWidth()/4)/2) + ((GraphicsHandler.getWidth()/4)/2)*2*2)-100 - GraphicsHandler.getWidth()*0.04);
	private static int xPlayer3Right = (int)((((GraphicsHandler.getWidth()/4)/2) + ((GraphicsHandler.getWidth()/4)/2)*2*2)+100 + GraphicsHandler.getWidth()*0.04) - 50;
	
	private static int xPlayer4Left = (int)((((GraphicsHandler.getWidth()/4)/2) + ((GraphicsHandler.getWidth()/4)/2)*3*2)-100 - GraphicsHandler.getWidth()*0.04);
	private static int xPlayer4Right = (int)((((GraphicsHandler.getWidth()/4)/2) + ((GraphicsHandler.getWidth()/4)/2)*3*2)+100 + GraphicsHandler.getWidth()*0.04) - 50;

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
		
		//LOBBY STARTBUTTON
		startLobby = new MouseActionArea((int)(GraphicsHandler.getWidth()*0.25), GraphicsHandler.getHeight()/4 + (GraphicsHandler.getHeight()/5)*3, 100, 70,
				MouseActionAreaType.MAA_LOBBY_STARTBUTTON, "Starten", 20, Color.WHITE, Color.ORANGE) {
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
				return GraphicsHandler.getDisplayType() == DisplayType.LOBBY;
			}
		};
		
		//LOBBY EXITBUTTON
		exitLobby = new MouseActionArea((int)(GraphicsHandler.getWidth()*0.75), GraphicsHandler.getHeight()/4 + (GraphicsHandler.getHeight()/5)*3, 100, 70,
				MouseActionAreaType.MAA_LOBBY_STARTBUTTON, "Exit", 20, Color.WHITE, Color.ORANGE) {
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
			    	    LobbyCreate.client.sendMessage(LobbyCreate.client.getSession(), "512-" + LobbyCreate.client.getId());
			    	}
			    	
			    	// Setze alle Objekte = null und switche ins Menu
			    	for (int i=0; i< LobbyCreate.numberOfMaxPlayers; i++) {
				    LobbyCreate.player[i] = null;
			    	}	
			    	LobbyCreate.numberOfMaxPlayers = 0;
			    	GraphicsHandler.lobby = null;
			    	// Schliessung der Session
//			    	LobbyCreate.client.getSession().closeNow();
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
				if(isHovered() || (leftisPressed == true && LobbyCreate.getHochRunterNavigation() == 0)) { // Hier kann man mit dem Key auch das Hovern imitieren
					g.drawImage(ImageHandler.getImage(ImageType.IMAGE_LOBBY_ARROW_LEFT_BIGGER).getImage(), xPlayer1Left, yPlayerMap+1, null);
				}else {
//					}else if (Lobby_Create.getHochRunterNavigation() == 0){ // So um die Pfeile nur auf Tastatur sichtbar zu machen
					g.drawImage(ImageHandler.getImage(ImageType.IMAGE_LOBBY_ARROW_LEFT).getImage(), xPlayer1Left, yPlayerMap, null);
				}
			}
		};
		
		//LOBBY RIGHT Button fuer Pfeil fuer MAP -> MAA_LOBBY_PFEILBUTTON_RIGHT
		mapright = new MouseActionArea(xPlayer1Right, yPlayerMap, 45, 44,//Diese Werte sind nicht sichtbar, aber das sind die Werte wo ich dann klicke
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
				if(isHovered() || (rightisPressed == true && LobbyCreate.getHochRunterNavigation() == 0)) { // Hier kann man mit dem Key auch das Hovern imitieren
					// Die y Koordinate -3, weil ja von oben links das Bild geprinted wird und der Button so leicht nach unten verschoben wird
					g.drawImage(ImageHandler.getImage(ImageType.IMAGE_LOBBY_ARROW_RIGHT_BIGGER).getImage(), xPlayer1Right, yPlayerMap - (int)(GraphicsHandler.getWidth()*0.0015), null);
				}else {
//				}else if (Lobby_Create.getHochRunterNavigation() == 0){ // So um die Pfeile nur auf Tastatur sichtbar zu machen
					g.drawImage(ImageHandler.getImage(ImageType.IMAGE_LOBBY_ARROW_RIGHT).getImage(), xPlayer1Right, yPlayerMap, null);
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
				if(GraphicsHandler.getDisplayType() == DisplayType.LOBBY && LobbyCreate.client.getId() == 0)
					return true;
				else
					return false;
			}
			// Um die Kaestchen wieder anzeigen zu lassen (sodass man sieht wo man klickt) einfach drawCustomParts anstatt draw Overriden
			@Override
			public void draw(Graphics g) {
				if(isHovered() || (leftisPressed == true && LobbyCreate.getHochRunterNavigation() == 1)) { // Hier kann man mit dem Key auch das Hovern imitieren
					g.drawImage(ImageHandler.getImage(ImageType.IMAGE_LOBBY_ARROW_LEFT_BIGGER).getImage(),xPlayer1Left, yPlayer, null);
				}else {
//				}else if (Lobby_Create.getHochRunterNavigation() == 1){ // So um die Pfeile nur auf Tastatur sichtbar zu machen
					// Unten andere Werte
//					g.drawImage(ImageHandler.getImage(ImageType.IMAGE_LOBBY_ARROW_LEFT).getImage(), (int)(((GraphicsHandler.getWidth()/4)/2) + ((GraphicsHandler.getWidth()/4)/2)*0*2)-200, GraphicsHandler.getHeight()/4 + (int)((GraphicsHandler.getHeight()*0.1) + GraphicsHandler.getWidth()*0.05), null);
					g.drawImage(ImageHandler.getImage(ImageType.IMAGE_LOBBY_ARROW_LEFT).getImage(), xPlayer1Left, yPlayer, null);
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
				if(isHovered() || (leftisPressed == true && LobbyCreate.getHochRunterNavigation() == 1)) { // Hier kann man mit dem Key auch das Hovern imitieren
					g.drawImage(ImageHandler.getImage(ImageType.IMAGE_LOBBY_ARROW_RIGHT_BIGGER).getImage(), xPlayer1Right, yPlayer - (int)(GraphicsHandler.getWidth()*0.0015), null);
				}else {
					g.drawImage(ImageHandler.getImage(ImageType.IMAGE_LOBBY_ARROW_RIGHT).getImage(), xPlayer1Right, yPlayer , null);
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
				if(GraphicsHandler.getDisplayType() == DisplayType.LOBBY && LobbyCreate.client.getId() == 1)
					return true;
				else
					return false;
			}
			@Override
			public void draw(Graphics g) {
				if(isHovered() || (leftisPressed == true && LobbyCreate.getHochRunterNavigation() == 1)) { // Hier kann man mit dem Key auch das Hovern imitieren
					g.drawImage(ImageHandler.getImage(ImageType.IMAGE_LOBBY_ARROW_LEFT_BIGGER).getImage(),xPlayer2Left, yPlayer, null);
				}else {
//				}else if (Lobby_Create.getHochRunterNavigation() == 1){ // So um die Pfeile nur auf Tastatur sichtbar zu machen
					// Unten andere Werte
//					g.drawImage(ImageHandler.getImage(ImageType.IMAGE_LOBBY_ARROW_LEFT).getImage(), (int)(((GraphicsHandler.getWidth()/4)/2) + ((GraphicsHandler.getWidth()/4)/2)*0*2)-200, GraphicsHandler.getHeight()/4 + (int)((GraphicsHandler.getHeight()*0.1) + GraphicsHandler.getWidth()*0.05), null);
					g.drawImage(ImageHandler.getImage(ImageType.IMAGE_LOBBY_ARROW_LEFT).getImage(), xPlayer2Left, yPlayer, null);
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
				if(GraphicsHandler.getDisplayType() == DisplayType.LOBBY && LobbyCreate.client.getId() == 1)
					return true;
				else
					return false;
			}
			@Override
			public void draw(Graphics g) {
				if(isHovered() || (leftisPressed == true && LobbyCreate.getHochRunterNavigation() == 1)) { // Hier kann man mit dem Key auch das Hovern imitieren
					g.drawImage(ImageHandler.getImage(ImageType.IMAGE_LOBBY_ARROW_RIGHT_BIGGER).getImage(), xPlayer2Right, yPlayer - (int)(GraphicsHandler.getWidth()*0.0015), null);
				}else {
					g.drawImage(ImageHandler.getImage(ImageType.IMAGE_LOBBY_ARROW_RIGHT).getImage(), xPlayer2Right, yPlayer , null);
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
				if(GraphicsHandler.getDisplayType() == DisplayType.LOBBY && LobbyCreate.client.getId() == 2)
					return true;
				else
					return false;
			}
			@Override
			public void draw(Graphics g) {
				if(isHovered() || (leftisPressed == true && LobbyCreate.getHochRunterNavigation() == 1)) { // Hier kann man mit dem Key auch das Hovern imitieren
					g.drawImage(ImageHandler.getImage(ImageType.IMAGE_LOBBY_ARROW_LEFT_BIGGER).getImage(),xPlayer3Left, yPlayer, null);
				}else {
//				}else if (Lobby_Create.getHochRunterNavigation() == 1){ // So um die Pfeile nur auf Tastatur sichtbar zu machen
					// Unten andere Werte
//					g.drawImage(ImageHandler.getImage(ImageType.IMAGE_LOBBY_ARROW_LEFT).getImage(), (int)(((GraphicsHandler.getWidth()/4)/2) + ((GraphicsHandler.getWidth()/4)/2)*0*2)-200, GraphicsHandler.getHeight()/4 + (int)((GraphicsHandler.getHeight()*0.1) + GraphicsHandler.getWidth()*0.05), null);
					g.drawImage(ImageHandler.getImage(ImageType.IMAGE_LOBBY_ARROW_LEFT).getImage(), xPlayer3Left, yPlayer, null);
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
				if(GraphicsHandler.getDisplayType() == DisplayType.LOBBY && LobbyCreate.client.getId() == 2)
					return true;
				else
					return false;
			}
			@Override
			public void draw(Graphics g) {
				if(isHovered() || (leftisPressed == true && LobbyCreate.getHochRunterNavigation() == 1)) { // Hier kann man mit dem Key auch das Hovern imitieren
					g.drawImage(ImageHandler.getImage(ImageType.IMAGE_LOBBY_ARROW_RIGHT_BIGGER).getImage(), xPlayer3Right, yPlayer - (int)(GraphicsHandler.getWidth()*0.0015), null);
				}else {
					g.drawImage(ImageHandler.getImage(ImageType.IMAGE_LOBBY_ARROW_RIGHT).getImage(), xPlayer3Right, yPlayer , null);
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
				if(isHovered() || (leftisPressed == true && LobbyCreate.getHochRunterNavigation() == 1)) { // Hier kann man mit dem Key auch das Hovern imitieren
					g.drawImage(ImageHandler.getImage(ImageType.IMAGE_LOBBY_ARROW_LEFT_BIGGER).getImage(),xPlayer4Left, yPlayer, null);
				}else {
//				}else if (Lobby_Create.getHochRunterNavigation() == 1){ // So um die Pfeile nur auf Tastatur sichtbar zu machen
					// Unten andere Werte
//					g.drawImage(ImageHandler.getImage(ImageType.IMAGE_LOBBY_ARROW_LEFT).getImage(), (int)(((GraphicsHandler.getWidth()/4)/2) + ((GraphicsHandler.getWidth()/4)/2)*0*2)-200, GraphicsHandler.getHeight()/4 + (int)((GraphicsHandler.getHeight()*0.1) + GraphicsHandler.getWidth()*0.05), null);
					g.drawImage(ImageHandler.getImage(ImageType.IMAGE_LOBBY_ARROW_LEFT).getImage(), xPlayer4Left, yPlayer, null);
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
				if(isHovered() || (leftisPressed == true && LobbyCreate.getHochRunterNavigation() == 1)) { // Hier kann man mit dem Key auch das Hovern imitieren
					g.drawImage(ImageHandler.getImage(ImageType.IMAGE_LOBBY_ARROW_RIGHT_BIGGER).getImage(), xPlayer4Right, yPlayer - (int)(GraphicsHandler.getWidth()*0.0015), null);
				}else {
					g.drawImage(ImageHandler.getImage(ImageType.IMAGE_LOBBY_ARROW_RIGHT).getImage(), xPlayer4Right, yPlayer , null);
				}
			}
		};

		///////////////////////////////// ALLE BUTTONS FUER DIE SKIN SELECTION ////////////////////////////////////////////////////////
		
		
		
		
		///////////////////////////////// ALLE BUTTONS FÜR DIE CHECKBOXEN PLAYER 2-4 ////////////////////////////////////////////////////////
		
		// Player 2
		player2check = new MouseActionArea(xPlayer2Right-100-40, yPlayerMap+15, 50, 50,
				MouseActionAreaType.MAA_LOBBY_CHECKMARK, "Ready", 20, Color.DARK_GRAY, Color.ORANGE) {
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
			public void drawCustomParts(Graphics g){
			    	if(LobbyCreate.player[1] != null) {
			    	    if(LobbyCreate.player[1].getisReady() == true) {
			    		g.drawImage(ImageHandler.getImage(ImageType.IMAGE_LOBBY_CHECKMARK).getImage(), xPlayer2Right-100-40, yPlayerMap , null);
			    	    }			    	    
			    	}
			}
		};
		// Player 3
		player3check = new MouseActionArea(xPlayer3Right-100-40, yPlayerMap+15, 50, 50,
				MouseActionAreaType.MAA_LOBBY_CHECKMARK, "Ready", 20, Color.DARK_GRAY, Color.ORANGE) {
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
			public void drawCustomParts(Graphics g){
			    if(LobbyCreate.player[2] != null) {
				if(LobbyCreate.player[2].getisReady() == true) {
				    g.drawImage(ImageHandler.getImage(ImageType.IMAGE_LOBBY_CHECKMARK).getImage(), xPlayer3Right-100-40, yPlayerMap , null); 
				}				
			    }
			}
		};
		// Player 4
		player4check = new MouseActionArea(xPlayer4Right-100-40, yPlayerMap+15, 50, 50,
				MouseActionAreaType.MAA_LOBBY_CHECKMARK, "Ready", 20, Color.DARK_GRAY, Color.ORANGE) {
			@Override
			public void performAction_LEFT_RELEASE() {
			    if (LobbyCreate.client.getId() == 3) {
				ConsoleHandler.print("CheckBox Player 4", MessageType.LOBBY);
				LobbyCreate.setisReady(3);
			    }
			}
			@Override
			public boolean isActiv() {
				if(GraphicsHandler.getDisplayType() == DisplayType.LOBBY && LobbyCreate.numberOfMaxPlayers >= 4 && LobbyCreate.player[3] != null) {
				    return true;
				}
				else {
				    return false;
				}	
			}
			@Override
			public void drawCustomParts(Graphics g){
			    if(LobbyCreate.player[2] != null) {
				if(LobbyCreate.player[3].getisReady() == true) {
				    g.drawImage(ImageHandler.getImage(ImageType.IMAGE_LOBBY_CHECKMARK).getImage(), xPlayer4Right-100-40, yPlayerMap , null);
				}	
			    }
			}
		};
		
		
		///////////////////////////////// ALLE BUTTONS FÜR DIE CHECKBOXEN PLAYER 2-4 ////////////////////////////////////////////////////////	

			
		}
	
	public static void lobbyButtonsreset() {
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
		
		initLobbyButtons();
	}

	
	/**
	 * Wird von KeyHandler aufgerufen, sodass keyIsPressed Funktionalitäten in der Lobby funktionieren.
	 */
	public static void keyIsPressed(int keyCode) {
		if (keyCode == KeyEvent.VK_RIGHT || keyCode == KeyEvent.VK_D) {
			rightisPressed = true;
		}
		else if (keyCode == KeyEvent.VK_LEFT || keyCode == KeyEvent.VK_A) {
			leftisPressed = true;
		}
		else if (keyCode == KeyEvent.VK_DOWN || keyCode == KeyEvent.VK_S) {
			LobbyCreate.setIncrementHochRunterNavigation();
		}
		else if (keyCode == KeyEvent.VK_UP || keyCode == KeyEvent.VK_W) {
			LobbyCreate.setDecrementHochRunterNavigation();
		}
	}
	
//	/**
//	 * Wird von KeyHandler aufgerufen, sodass keyIsReleased Funktionalitäten in der Lobby funktionieren.
//	 */
	// Noch in KeyHandler einstellen / auskommentieren
//	public static void keyIsReleased(int keyCode) {
//		if (keyCode == KeyEvent.VK_RIGHT || keyCode == KeyEvent.VK_D) {
//			rightisPressed = false;
//			if (Lobby_Create.getHochRunterNavigation() == 0)
//				Lobby_Create.setIncrementMap();
//			else if (Lobby_Create.getHochRunterNavigation() == 1)
//				Lobby_Create.setIncrementSkin();
//		}
//		else if (keyCode == KeyEvent.VK_LEFT || keyCode == KeyEvent.VK_A) {
//			leftisPressed = false;
//			if (Lobby_Create.getHochRunterNavigation() == 0)
//				Lobby_Create.setDecrementMap();
//			else if (Lobby_Create.getHochRunterNavigation() == 1)
//				Lobby_Create.setDecrementSkin();
//		}
//	}
	

}
