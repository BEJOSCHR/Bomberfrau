package uni.bombenstimmung.de.lobby;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

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
	
	public OwnButtons(){
		
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
				if(isHovered() || leftisPressed == true) {
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
				if(isHovered() || rightisPressed == true) {
					// Die y Koordinate -3, weil ja von oben links das Bild geprinted wird und der Button so leicht nach unten verschoben wird
					g.drawImage(ImageHandler.getImage(ImageType.IMAGE_LOBBY_ARROW_RIGHT_BIGGER).getImage(), GraphicsHandler.getWidth()/3, GraphicsHandler.getHeight()/2-3, null);
				}else {
					g.drawImage(ImageHandler.getImage(ImageType.IMAGE_LOBBY_ARROW_RIGHT).getImage(), GraphicsHandler.getWidth()/3, GraphicsHandler.getHeight()/2, null);
				}
			}
		};
	}

	public static void keyIsPressed(int keyCode) {
		if (keyCode == KeyEvent.VK_RIGHT) {
			rightisPressed = true;
			Lobby_Create.setIncrementMap();
		}
		else if (keyCode == KeyEvent.VK_LEFT) {
			leftisPressed = true;
			Lobby_Create.setDecrementMap();
		}
	}
	
	public static void keyIsReleased(int keyCode) {
		if (keyCode == KeyEvent.VK_RIGHT) {
			rightisPressed = false;
		}
		else if (keyCode == KeyEvent.VK_LEFT) {
			leftisPressed = false;
		}
	}
	

}
