/*
 * PlayerHandler
 *
 * Version 0.4.1
 * 
 * Author: Christopher
 * 
 * Datum: 12.12.2021
 *
 * Verwaltet die erzeugten Player und deren Steuerung
 */

package uni.bombenstimmung.de.game;

import java.awt.Color;
import java.awt.Graphics;

import java.util.ArrayList;

import uni.bombenstimmung.de.backend.console.ConsoleHandler;
import uni.bombenstimmung.de.backend.console.MessageType;
import uni.bombenstimmung.de.backend.graphics.GraphicsHandler;

public class PlayerHandler {
    static Player clientPlayer = new Player("Bob");
    static ArrayList<Player> opponentPlayers = new ArrayList<Player>();
    static int opponentCount = 0;
    static boolean buttonPressed = false;
    
    public static void addClientPlayer(int id, String name, String ipAdress, boolean host, int skin) {
	clientPlayer = new Player(id, name, ipAdress, host, skin);
    }
    
    public static void addOpponentPlayer(int id, String name, String ipAdress, boolean host, int skin) {
	opponentPlayers.add(new Player(id, name, ipAdress, host, skin));
	opponentCount++;
    }
    
    public static void drawPlayers(Graphics g) {
	if (clientPlayer.getDead() == false) {
	    g.setColor(Color.red);
	    g.drawRect((int)(clientPlayer.getPosition().getX() - GraphicsHandler.getWidth()/35.6/2),
		    	(int)(clientPlayer.getPosition().getY() - GraphicsHandler.getHeight()/20),
		    	(int)(GraphicsHandler.getWidth()/35.6), (int)(GraphicsHandler.getHeight()/20));
	    g.fillRect((int)(clientPlayer.getPosition().getX() - GraphicsHandler.getWidth()/35.6/2),
		    	(int)(clientPlayer.getPosition().getY() - GraphicsHandler.getHeight()/20),
		    	(int)(GraphicsHandler.getWidth()/35.6), (int)(GraphicsHandler.getHeight()/20));
	}
    }
    
    public static void handleKeyEventPressed(int keyCode) {
	if (clientPlayer.getDead() == false && buttonPressed == false) {
	    if (keyCode == clientPlayer.getCurrentButtonConfig().getUp()) {
		ConsoleHandler.print("Client presses Button 'up'", MessageType.GAME);
		// TODO: Aktion hoch Start
		clientPlayer.actionUp();
		buttonPressed = true;
	    } else if (keyCode == clientPlayer.getCurrentButtonConfig().getDown()) {
		ConsoleHandler.print("Client presses Button 'down'", MessageType.GAME);
		// TODO: Aktion runter Start
		clientPlayer.actionDown();
		buttonPressed = true;
	    } else if (keyCode == clientPlayer.getCurrentButtonConfig().getLeft()) {
		ConsoleHandler.print("Client presses Button 'left'", MessageType.GAME);
		// TODO: Aktion links Start
		clientPlayer.actionLeft();
		buttonPressed = true;
	    } else if (keyCode == clientPlayer.getCurrentButtonConfig().getRight()) {
		ConsoleHandler.print("Client presses Button 'right'", MessageType.GAME);
		// TODO: Aktion rechts Start
		clientPlayer.actionRight();
		buttonPressed = true;
	    } else if (keyCode == clientPlayer.getCurrentButtonConfig().getSetBomb()) {
		ConsoleHandler.print("Client presses Button 'setBomb'", MessageType.GAME);
		// TODO: Aktion Bombe legen Start
		buttonPressed = true;
	    }
	}
    }
    
    public static void handleKeyEventReleased(int keyCode) {
	if (clientPlayer.getDead() == false && buttonPressed == true) {
	    if (keyCode == clientPlayer.getCurrentButtonConfig().getUp()) {
		ConsoleHandler.print("Client released Button 'up'", MessageType.GAME);
		// TODO: Aktion hoch Stop
		clientPlayer.actionStop();
		buttonPressed = false;
	    } else if (keyCode == clientPlayer.getCurrentButtonConfig().getDown()) {
		ConsoleHandler.print("Client released Button 'down'", MessageType.GAME);
		// TODO: Aktion runter Stop
		clientPlayer.actionStop();
		buttonPressed = false;
	    } else if (keyCode == clientPlayer.getCurrentButtonConfig().getLeft()) {
		ConsoleHandler.print("Client released Button 'left'", MessageType.GAME);
		// TODO: Aktion links Stop
		clientPlayer.actionStop();
		buttonPressed = false;
	    } else if (keyCode == clientPlayer.getCurrentButtonConfig().getRight()) {
		ConsoleHandler.print("Client released Button 'right'", MessageType.GAME);
		// TODO: Aktion rechts Stop
		clientPlayer.actionStop();
		buttonPressed = false;
	    } else if (keyCode == clientPlayer.getCurrentButtonConfig().getSetBomb()) {
		ConsoleHandler.print("Client released Button 'setBomb'", MessageType.GAME);
		// TODO: Aktion Bombe legen Stop
	    }
	}
    }
}
