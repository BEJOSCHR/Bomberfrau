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

public class OwnButtons extends MouseActionAreaHandler{
	
	static boolean rightisPressed = false;
	static boolean leftisPressed = false;
	static int mapHostXLeft = GraphicsHandler.getWidth()/8 - 50;
	static int mapHostY = (GraphicsHandler.getHeight()/4 + GraphicsHandler.getHeight()/5);
	static int mapHostXRight = GraphicsHandler.getWidth()/8 + 50;
	
	
	static int xPlayer1Left = (int)((((GraphicsHandler.getWidth()/4)/2) + ((GraphicsHandler.getWidth()/4)/2)*0*2)-100 - GraphicsHandler.getWidth()*0.04);
	static int xPlayer1Right = (int)((((GraphicsHandler.getWidth()/4)/2) + ((GraphicsHandler.getWidth()/4)/2)*0*2)+100 + GraphicsHandler.getWidth()*0.04) - 50;
	static int yPlayer = GraphicsHandler.getHeight()/4 + (int)(GraphicsHandler.getHeight()*0.1) + 75;
	
	static int xPlayer2Left = (int)((((GraphicsHandler.getWidth()/4)/2) + ((GraphicsHandler.getWidth()/4)/2)*1*2)-100 - GraphicsHandler.getWidth()*0.04);
	static int xPlayer2Right = (int)((((GraphicsHandler.getWidth()/4)/2) + ((GraphicsHandler.getWidth()/4)/2)*1*2)+100 + GraphicsHandler.getWidth()*0.04) - 50;
	
	static int xPlayer3Left = (int)((((GraphicsHandler.getWidth()/4)/2) + ((GraphicsHandler.getWidth()/4)/2)*2*2)-100 - GraphicsHandler.getWidth()*0.04);
	static int xPlayer3Right = (int)((((GraphicsHandler.getWidth()/4)/2) + ((GraphicsHandler.getWidth()/4)/2)*2*2)+100 + GraphicsHandler.getWidth()*0.04) - 50;
	
	static int xPlayer4Left = (int)((((GraphicsHandler.getWidth()/4)/2) + ((GraphicsHandler.getWidth()/4)/2)*3*2)-100 - GraphicsHandler.getWidth()*0.04);
	static int xPlayer4Right = (int)((((GraphicsHandler.getWidth()/4)/2) + ((GraphicsHandler.getWidth()/4)/2)*3*2)+100 + GraphicsHandler.getWidth()*0.04) - 50;
			
	public static void initOwnButtons(){
		
		//LOBBY STARTBUTTON
		new MouseActionArea(GraphicsHandler.getWidth()/2, GraphicsHandler.getHeight()/4 + (GraphicsHandler.getHeight()/5)*3, 100, 70,
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
		
		
		
//		//LOBBY Unsichtbarer LEFT Button fuer Pfeil fuer MAP -> MAA_LOBBY_PFEILBUTTON_LEFT
//		new MouseActionArea(mapHostXLeft, mapHostY, 45, 44,
//				MouseActionAreaType.MAA_LOBBY_PFEILBUTTON_LEFT, "Pfeil", 20, Color.DARK_GRAY, Color.ORANGE) { //Diese Werte sind belanglos, da ich die in draw sowieso überschreibe und nicht brauche
//			@Override
//			public void performAction_LEFT_RELEASE() {
//				ConsoleHandler.print(" Left Pfeilbutton MAP Button was Clicked", MessageType.LOBBY);
//				Lobby_Create.setDecrementMap();
//			}
//			@Override
//			public boolean isActiv() {
//				return GraphicsHandler.getDisplayType() == DisplayType.LOBBY;
//			}
//			@Override
//			public void draw(Graphics g) { 
//				if(isHovered() || (leftisPressed == true && Lobby_Create.getHochRunterNavigation() == 0)) { // Hier kann man mit dem Key auch das Hovern imitieren
//					g.drawImage(ImageHandler.getImage(ImageType.IMAGE_LOBBY_ARROW_LEFT_BIGGER).getImage(), mapHostXLeft, mapHostY+1, null);
//				}else {
////					}else if (Lobby_Create.getHochRunterNavigation() == 0){ // So um die Pfeile nur auf Tastatur sichtbar zu machen
//					g.drawImage(ImageHandler.getImage(ImageType.IMAGE_LOBBY_ARROW_LEFT).getImage(), (GraphicsHandler.getWidth()/8) - 35, GraphicsHandler.getHeight()/4 + GraphicsHandler.getHeight()/5, null);
//				}
//			}
//		};
//		
//		//LOBBY Unsichtbarer RIGHT Button fuer Pfeil fuer MAP -> MAA_LOBBY_PFEILBUTTON_RIGHT
//		new MouseActionArea(Lobby_Create.getXValueForDraw(Lobby_Create.getHost())+100, GraphicsHandler.getHeight()/4 + (GraphicsHandler.getHeight()/5)*2, 45, 44,//Diese Werte sind nicht sichtbar, aber das sind die Werte wo ich dann klicke
//				MouseActionAreaType.MAA_LOBBY_PFEILBUTTON_RIGHT, "Pfeil", 20, Color.DARK_GRAY, Color.ORANGE) {
//			@Override
//			public void performAction_LEFT_RELEASE() {
//				ConsoleHandler.print("Right Pfeilbutton MAP Button was Clicked", MessageType.LOBBY);
//				Lobby_Create.setIncrementMap();
//			}
//			@Override
//			public boolean isActiv() {
//				return GraphicsHandler.getDisplayType() == DisplayType.LOBBY;
//			}
//			@Override
//			public void draw(Graphics g) { 
//				if(isHovered() || (rightisPressed == true && Lobby_Create.getHochRunterNavigation() == 0)) { // Hier kann man mit dem Key auch das Hovern imitieren
//					// Die y Koordinate -3, weil ja von oben links das Bild geprinted wird und der Button so leicht nach unten verschoben wird
//					g.drawImage(ImageHandler.getImage(ImageType.IMAGE_LOBBY_ARROW_RIGHT_BIGGER).getImage(), Lobby_Create.getXValueForDraw(Lobby_Create.getHost())+100, GraphicsHandler.getHeight()/4 + (GraphicsHandler.getHeight()/5)*2 +3, null);
//				}else {
////				}else if (Lobby_Create.getHochRunterNavigation() == 0){ // So um die Pfeile nur auf Tastatur sichtbar zu machen
//					g.drawImage(ImageHandler.getImage(ImageType.IMAGE_LOBBY_ARROW_RIGHT).getImage(), Lobby_Create.getXValueForDraw(Lobby_Create.getHost())+100, GraphicsHandler.getHeight()/4 + (GraphicsHandler.getHeight()/5)*2, null);
////					g.drawImage(ImageHandler.getImage(ImageType.IMAGE_LOBBY_ARROW_RIGHT).getImage(), Lobby_Create.getXValueForDraw(Lobby_Create.getHost())+130, GraphicsHandler.getHeight()/4 + (GraphicsHandler.getHeight()/5)*2, null);
//				}
//			}
//		};
		
	
		// PLAYER 1  LEFT
		new MouseActionArea(xPlayer1Left, yPlayer, 45, 44,
				MouseActionAreaType.MAA_LOBBY_PFEILBUTTON_LEFT, "Pfeil", 20, Color.DARK_GRAY, Color.ORANGE) {
			@Override
			public void performAction_LEFT_RELEASE() {
				ConsoleHandler.print("Left Player 1", MessageType.LOBBY);
				Lobby_Create.player[0].setDecrementSkin();
			}
			@Override
			public boolean isActiv() {
				if(GraphicsHandler.getDisplayType() == DisplayType.LOBBY && Lobby_Create.numberPlayer != 0)
					return true;
				else
					return false;
			}
			// Um die Kaestchen wieder anzeigen zu lassen (sodass man sieht wo man klickt) einfach drawCustomParts anstatt draw Overriden
			@Override
			public void draw(Graphics g) {
				if(isHovered() || (leftisPressed == true && Lobby_Create.getHochRunterNavigation() == 1)) { // Hier kann man mit dem Key auch das Hovern imitieren
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
		new MouseActionArea(xPlayer1Right, yPlayer, 45, 44,
				MouseActionAreaType.MAA_LOBBY_PFEILBUTTON_RIGHT, "Pfeil", 20, Color.DARK_GRAY, Color.ORANGE) {
			@Override
			public void performAction_LEFT_RELEASE() {
				ConsoleHandler.print("Right Player 1", MessageType.LOBBY);
				Lobby_Create.player[0].setIncrementSkin();
			}
			@Override
			public boolean isActiv() {
				if(GraphicsHandler.getDisplayType() == DisplayType.LOBBY && Lobby_Create.numberPlayer != 0)
					return true;
				else
					return false;
			}
			@Override
			public void draw(Graphics g) {
				if(isHovered() || (leftisPressed == true && Lobby_Create.getHochRunterNavigation() == 1)) { // Hier kann man mit dem Key auch das Hovern imitieren
					g.drawImage(ImageHandler.getImage(ImageType.IMAGE_LOBBY_ARROW_RIGHT_BIGGER).getImage(), xPlayer1Right, yPlayer - (int)(GraphicsHandler.getWidth()*0.0015), null);
				}else {
					g.drawImage(ImageHandler.getImage(ImageType.IMAGE_LOBBY_ARROW_RIGHT).getImage(), xPlayer1Right, yPlayer , null);
				}
			}
		};
		
		
		
		// PLAYER 2  LEFT
		new MouseActionArea(xPlayer2Left, yPlayer, 45, 44,
				MouseActionAreaType.MAA_LOBBY_PFEILBUTTON_LEFT, "Pfeil", 20, Color.DARK_GRAY, Color.ORANGE) {
			@Override
			public void performAction_LEFT_RELEASE() {
				ConsoleHandler.print("Left Player 2", MessageType.LOBBY);
				Lobby_Create.player[1].setDecrementSkin();
			}
			@Override
			public boolean isActiv() {
				if(GraphicsHandler.getDisplayType() == DisplayType.LOBBY && Lobby_Create.numberPlayer >= 2)
					return true;
				else
					return false;
			}
			@Override
			public void draw(Graphics g) {
				if(isHovered() || (leftisPressed == true && Lobby_Create.getHochRunterNavigation() == 1)) { // Hier kann man mit dem Key auch das Hovern imitieren
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
		new MouseActionArea(xPlayer2Right, yPlayer, 45, 44,
				MouseActionAreaType.MAA_LOBBY_PFEILBUTTON_RIGHT, "Pfeil", 20, Color.DARK_GRAY, Color.ORANGE) {
			@Override
			public void performAction_LEFT_RELEASE() {
				ConsoleHandler.print("Right Player 2", MessageType.LOBBY);
				Lobby_Create.player[1].setIncrementSkin();
			}
			@Override
			public boolean isActiv() {
				if(GraphicsHandler.getDisplayType() == DisplayType.LOBBY && Lobby_Create.numberPlayer >= 2)
					return true;
				else
					return false;
			}
			@Override
			public void draw(Graphics g) {
				if(isHovered() || (leftisPressed == true && Lobby_Create.getHochRunterNavigation() == 1)) { // Hier kann man mit dem Key auch das Hovern imitieren
					g.drawImage(ImageHandler.getImage(ImageType.IMAGE_LOBBY_ARROW_RIGHT_BIGGER).getImage(), xPlayer2Right, yPlayer - (int)(GraphicsHandler.getWidth()*0.0015), null);
				}else {
					g.drawImage(ImageHandler.getImage(ImageType.IMAGE_LOBBY_ARROW_RIGHT).getImage(), xPlayer2Right, yPlayer , null);
				}
			}
		};
		
		
		
		// PLAYER 3  LEFT
		new MouseActionArea(xPlayer3Left, yPlayer, 45, 44,
				MouseActionAreaType.MAA_LOBBY_PFEILBUTTON_LEFT, "Pfeil", 20, Color.DARK_GRAY, Color.ORANGE) {
			@Override
			public void performAction_LEFT_RELEASE() {
				ConsoleHandler.print("Left Player 3", MessageType.LOBBY);
				Lobby_Create.player[2].setDecrementSkin();
			}
			@Override
			public boolean isActiv() {
				if(GraphicsHandler.getDisplayType() == DisplayType.LOBBY && Lobby_Create.numberPlayer >= 3)
					return true;
				else
					return false;
			}
			@Override
			public void draw(Graphics g) {
				if(isHovered() || (leftisPressed == true && Lobby_Create.getHochRunterNavigation() == 1)) { // Hier kann man mit dem Key auch das Hovern imitieren
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
		new MouseActionArea(xPlayer3Right, yPlayer, 45, 44,
				MouseActionAreaType.MAA_LOBBY_PFEILBUTTON_RIGHT, "Pfeil", 20, Color.DARK_GRAY, Color.ORANGE) {
			@Override
			public void performAction_LEFT_RELEASE() {
				ConsoleHandler.print("Right Player 3", MessageType.LOBBY);
				Lobby_Create.player[2].setIncrementSkin();
			}
			@Override
			public boolean isActiv() {
				if(GraphicsHandler.getDisplayType() == DisplayType.LOBBY && Lobby_Create.numberPlayer >= 3)
					return true;
				else
					return false;
			}
			@Override
			public void draw(Graphics g) {
				if(isHovered() || (leftisPressed == true && Lobby_Create.getHochRunterNavigation() == 1)) { // Hier kann man mit dem Key auch das Hovern imitieren
					g.drawImage(ImageHandler.getImage(ImageType.IMAGE_LOBBY_ARROW_RIGHT_BIGGER).getImage(), xPlayer3Right, yPlayer - (int)(GraphicsHandler.getWidth()*0.0015), null);
				}else {
					g.drawImage(ImageHandler.getImage(ImageType.IMAGE_LOBBY_ARROW_RIGHT).getImage(), xPlayer3Right, yPlayer , null);
				}
			}
		};
		
		
		
		
		// PLAYER 4  LEFT
		new MouseActionArea(xPlayer4Left, yPlayer, 45, 44,
				MouseActionAreaType.MAA_LOBBY_PFEILBUTTON_LEFT, "Pfeil", 20, Color.DARK_GRAY, Color.ORANGE) {
			@Override
			public void performAction_LEFT_RELEASE() {
				ConsoleHandler.print("Left Player 4", MessageType.LOBBY);
				Lobby_Create.player[3].setDecrementSkin();
			}
			@Override
			public boolean isActiv() {
				if(GraphicsHandler.getDisplayType() == DisplayType.LOBBY && Lobby_Create.numberPlayer >= 4)
					return true;
				else
					return false;
			}
			@Override
			public void draw(Graphics g) {
				if(isHovered() || (leftisPressed == true && Lobby_Create.getHochRunterNavigation() == 1)) { // Hier kann man mit dem Key auch das Hovern imitieren
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
		new MouseActionArea(xPlayer4Right, yPlayer, 45, 44,
				MouseActionAreaType.MAA_LOBBY_PFEILBUTTON_RIGHT, "Pfeil", 20, Color.DARK_GRAY, Color.ORANGE) {
			@Override
			public void performAction_LEFT_RELEASE() {
				ConsoleHandler.print("Right Player 4", MessageType.LOBBY);
				Lobby_Create.player[3].setIncrementSkin();
			}
			@Override
			public boolean isActiv() {
				if(GraphicsHandler.getDisplayType() == DisplayType.LOBBY && Lobby_Create.numberPlayer >= 4)
					return true;
				else
					return false;
			}
			@Override
			public void draw(Graphics g) {
				if(isHovered() || (leftisPressed == true && Lobby_Create.getHochRunterNavigation() == 1)) { // Hier kann man mit dem Key auch das Hovern imitieren
					g.drawImage(ImageHandler.getImage(ImageType.IMAGE_LOBBY_ARROW_RIGHT_BIGGER).getImage(), xPlayer4Right, yPlayer - (int)(GraphicsHandler.getWidth()*0.0015), null);
				}else {
					g.drawImage(ImageHandler.getImage(ImageType.IMAGE_LOBBY_ARROW_RIGHT).getImage(), xPlayer4Right, yPlayer , null);
				}
			}
		};


		
		
		
			// Player 1
		
//			//LOBBY Unsichtbarer LEFT Button fuer Pfeil fuer SKIN -> MAA_LOBBY_PFEILBUTTON_LEFT
//			new MouseActionArea(Lobby_Create.getXValueForDraw(0)-200, GraphicsHandler.getHeight()/4 + (int)(GraphicsHandler.getHeight()*0.1) + 75, 45, 44,
//					MouseActionAreaType.MAA_LOBBY_PFEILBUTTON_LEFT, "Pfeil", 20, Color.DARK_GRAY, Color.ORANGE) { //Diese Werte sind belanglos, da ich die in draw sowieso überschreibe und nicht brauche
//				@Override
//				public void performAction_LEFT_RELEASE() {
//					ConsoleHandler.print(" Left Pfeilbutton SKIN Button was Clicked", MessageType.LOBBY);
//					Lobby_Create.setDecrementSkin();
//				}
//				@Override
//				public boolean isActiv() {
//					return GraphicsHandler.getDisplayType() == DisplayType.LOBBY;
//				}
//				@Override
//				public void draw(Graphics g) { 
//					if(isHovered() || (leftisPressed == true && Lobby_Create.getHochRunterNavigation() == 1)) { // Hier kann man mit dem Key auch das Hovern imitieren
//						g.drawImage(ImageHandler.getImage(ImageType.IMAGE_LOBBY_ARROW_LEFT_BIGGER).getImage(), Lobby_Create.getXValueForDraw(0)-200, GraphicsHandler.getHeight()/4 + (int)(GraphicsHandler.getHeight()*0.1)-1 +72, null);
//					}else {
////					}else if (Lobby_Create.getHochRunterNavigation() == 1){ // So um die Pfeile nur auf Tastatur sichtbar zu machen
//						g.drawImage(ImageHandler.getImage(ImageType.IMAGE_LOBBY_ARROW_LEFT).getImage(), Lobby_Create.getXValueForDraw(0)-200, GraphicsHandler.getHeight()/4 + (int)(GraphicsHandler.getHeight()*0.1) + 75, null);
//					}
//				}
//			};
			
			//LOBBY Unsichtbarer RIGHT Button fuer Pfeil fuer SKIN -> MAA_LOBBY_PFEILBUTTON_RIGHT
//			new MouseActionArea(Lobby_Create.getXValueForDraw(0), GraphicsHandler.getHeight()/4 + (int)(GraphicsHandler.getHeight()*0.1), 45, 44,//Diese Werte sind nicht sichtbar, aber das sind die Werte wo ich dann klicke
//					MouseActionAreaType.MAA_LOBBY_PFEILBUTTON_RIGHT, "Pfeil", 20, Color.DARK_GRAY, Color.ORANGE) {
//				@Override
//				public void performAction_LEFT_RELEASE() {
//					ConsoleHandler.print("Right Pfeilbutton SKIN Button was Clicked", MessageType.LOBBY);
//					Lobby_Create.setIncrementSkin();
//				}
//				@Override
//				public boolean isActiv() {
//					return GraphicsHandler.getDisplayType() == DisplayType.LOBBY;
//				}
//				@Override
//				public void draw(Graphics g) { 

//						g.drawImage(ImageHandler.getImage(ImageType.IMAGE_LOBBY_ARROW_RIGHT_BIGGER).getImage(), Lobby_Create.getXValueForDraw(0), GraphicsHandler.getHeight()/4 + (int)(GraphicsHandler.getHeight()*0.1)-3, null);
//					}else {
////					}else if (Lobby_Create.getHochRunterNavigation() == 1){
//						g.drawImage(ImageHandler.getImage(ImageType.IMAGE_LOBBY_ARROW_RIGHT).getImage(), Lobby_Create.getXValueForDraw(0), GraphicsHandler.getHeight()/4 + (int)(GraphicsHandler.getHeight()*0.1), null);
//					}
//				}
//			};
			
		}



	public static void keyIsPressed(int keyCode) {
		if (keyCode == KeyEvent.VK_RIGHT || keyCode == KeyEvent.VK_D) {
			rightisPressed = true;
		}
		else if (keyCode == KeyEvent.VK_LEFT || keyCode == KeyEvent.VK_A) {
			leftisPressed = true;
		}
		else if (keyCode == KeyEvent.VK_DOWN || keyCode == KeyEvent.VK_S) {
			Lobby_Create.setIncrementHochRunterNavigation();
		}
		else if (keyCode == KeyEvent.VK_UP || keyCode == KeyEvent.VK_W) {
			Lobby_Create.setDecrementHochRunterNavigation();
		}
	}
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
