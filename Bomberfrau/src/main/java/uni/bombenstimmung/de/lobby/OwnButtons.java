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
	static int skinHostY = GraphicsHandler.getHeight()/4 + (GraphicsHandler.getHeight()/5)*2;
			
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
		
		//LOBBY Unsichtbarer LEFT Button fuer Pfeil fuer MAP -> MAA_LOBBY_PFEILBUTTON_LEFT
		new MouseActionArea(mapHostXLeft, mapHostY, 45, 44,
				MouseActionAreaType.MAA_LOBBY_PFEILBUTTON_LEFT, "Pfeil", 20, Color.DARK_GRAY, Color.ORANGE) { //Diese Werte sind belanglos, da ich die in draw sowieso überschreibe und nicht brauche
			@Override
			public void performAction_LEFT_RELEASE() {
				ConsoleHandler.print(" Left Pfeilbutton MAP Button was Clicked", MessageType.LOBBY);
				Lobby_Create.setDecrementMap();
			}
			@Override
			public boolean isActiv() {
				return GraphicsHandler.getDisplayType() == DisplayType.LOBBY;
			}
			@Override
			public void draw(Graphics g) { 
				if(isHovered() || (leftisPressed == true && Lobby_Create.getHochRunterNavigation() == 0)) { // Hier kann man mit dem Key auch das Hovern imitieren
					g.drawImage(ImageHandler.getImage(ImageType.IMAGE_LOBBY_ARROW_LEFT_BIGGER).getImage(), mapHostXLeft, mapHostY+1, null);
				}else {
//					}else if (Lobby_Create.getHochRunterNavigation() == 0){ // So um die Pfeile nur auf Tastatur sichtbar zu machen
					g.drawImage(ImageHandler.getImage(ImageType.IMAGE_LOBBY_ARROW_LEFT).getImage(), (GraphicsHandler.getWidth()/8) - 35, GraphicsHandler.getHeight()/4 + GraphicsHandler.getHeight()/5, null);
				}
			}
		};
		
		//LOBBY Unsichtbarer RIGHT Button fuer Pfeil fuer MAP -> MAA_LOBBY_PFEILBUTTON_RIGHT
		new MouseActionArea(mapHostXRight, mapHostY, 45, 44,//Diese Werte sind nicht sichtbar, aber das sind die Werte wo ich dann klicke
				MouseActionAreaType.MAA_LOBBY_PFEILBUTTON_RIGHT, "Pfeil", 20, Color.DARK_GRAY, Color.ORANGE) {
			@Override
			public void performAction_LEFT_RELEASE() {
				ConsoleHandler.print("Right Pfeilbutton MAP Button was Clicked", MessageType.LOBBY);
				Lobby_Create.setIncrementMap();
			}
			@Override
			public boolean isActiv() {
				return GraphicsHandler.getDisplayType() == DisplayType.LOBBY;
			}
			@Override
			public void draw(Graphics g) { 
				if(isHovered() || (rightisPressed == true && Lobby_Create.getHochRunterNavigation() == 0)) { // Hier kann man mit dem Key auch das Hovern imitieren
					// Die y Koordinate -3, weil ja von oben links das Bild geprinted wird und der Button so leicht nach unten verschoben wird
					g.drawImage(ImageHandler.getImage(ImageType.IMAGE_LOBBY_ARROW_RIGHT_BIGGER).getImage(), mapHostXRight, mapHostY-3, null);
				}else {
//				}else if (Lobby_Create.getHochRunterNavigation() == 0){ // So um die Pfeile nur auf Tastatur sichtbar zu machen
					g.drawImage(ImageHandler.getImage(ImageType.IMAGE_LOBBY_ARROW_RIGHT).getImage(), mapHostXRight, mapHostY, null);
				}
			}
		};
		
		//LOBBY Unsichtbarer LEFT Button fuer Pfeil fuer SKIN -> MAA_LOBBY_PFEILBUTTON_LEFT
		new MouseActionArea(GraphicsHandler.getWidth()/5, skinHostY, 45, 44,
				MouseActionAreaType.MAA_LOBBY_PFEILBUTTON_LEFT, "Pfeil", 20, Color.DARK_GRAY, Color.ORANGE) { //Diese Werte sind belanglos, da ich die in draw sowieso überschreibe und nicht brauche
			@Override
			public void performAction_LEFT_RELEASE() {
				ConsoleHandler.print(" Left Pfeilbutton SKIN Button was Clicked", MessageType.LOBBY);
				Lobby_Create.setDecrementSkin();
			}
			@Override
			public boolean isActiv() {
				return GraphicsHandler.getDisplayType() == DisplayType.LOBBY;
			}
			@Override
			public void draw(Graphics g) { 
				if(isHovered() || (leftisPressed == true && Lobby_Create.getHochRunterNavigation() == 1)) { // Hier kann man mit dem Key auch das Hovern imitieren
					g.drawImage(ImageHandler.getImage(ImageType.IMAGE_LOBBY_ARROW_LEFT_BIGGER).getImage(), GraphicsHandler.getWidth()/5, skinHostY-1, null);
				}else {
//				}else if (Lobby_Create.getHochRunterNavigation() == 1){ // So um die Pfeile nur auf Tastatur sichtbar zu machen
					g.drawImage(ImageHandler.getImage(ImageType.IMAGE_LOBBY_ARROW_LEFT).getImage(), GraphicsHandler.getWidth()/5, skinHostY, null);
				}
			}
		};
		
		//LOBBY Unsichtbarer RIGHT Button fuer Pfeil fuer SKIN -> MAA_LOBBY_PFEILBUTTON_RIGHT
		new MouseActionArea(GraphicsHandler.getWidth()/3, skinHostY, 45, 44,//Diese Werte sind nicht sichtbar, aber das sind die Werte wo ich dann klicke
				MouseActionAreaType.MAA_LOBBY_PFEILBUTTON_RIGHT, "Pfeil", 20, Color.DARK_GRAY, Color.ORANGE) {
			@Override
			public void performAction_LEFT_RELEASE() {
				ConsoleHandler.print("Right Pfeilbutton SKIN Button was Clicked", MessageType.LOBBY);
				Lobby_Create.setIncrementSkin();
			}
			@Override
			public boolean isActiv() {
				return GraphicsHandler.getDisplayType() == DisplayType.LOBBY;
			}
			@Override
			public void draw(Graphics g) { 
				if(isHovered() || (rightisPressed == true && Lobby_Create.getHochRunterNavigation() == 1)) { // Hier kann man mit dem Key auch das Hovern imitieren
					// Die y Koordinate -3, weil ja von oben links das Bild geprinted wird und der Button so leicht nach unten verschoben wird
					g.drawImage(ImageHandler.getImage(ImageType.IMAGE_LOBBY_ARROW_RIGHT_BIGGER).getImage(), GraphicsHandler.getWidth()/3, skinHostY-3, null);
				}else {
//				}else if (Lobby_Create.getHochRunterNavigation() == 1){
					g.drawImage(ImageHandler.getImage(ImageType.IMAGE_LOBBY_ARROW_RIGHT).getImage(), GraphicsHandler.getWidth()/3, skinHostY, null);
				}
			}
		};
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
	public static void keyIsReleased(int keyCode) {
		if (keyCode == KeyEvent.VK_RIGHT || keyCode == KeyEvent.VK_D) {
			rightisPressed = false;
			if (Lobby_Create.getHochRunterNavigation() == 0)
				Lobby_Create.setIncrementMap();
			else if (Lobby_Create.getHochRunterNavigation() == 1)
				Lobby_Create.setIncrementSkin();
		}
		else if (keyCode == KeyEvent.VK_LEFT || keyCode == KeyEvent.VK_A) {
			leftisPressed = false;
			if (Lobby_Create.getHochRunterNavigation() == 0)
				Lobby_Create.setDecrementMap();
			else if (Lobby_Create.getHochRunterNavigation() == 1)
				Lobby_Create.setDecrementSkin();
		}
	}
	

}
