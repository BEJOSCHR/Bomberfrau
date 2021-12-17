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
    static int buttonPressedBuffer = 0;
    static boolean secondPress = false;
    static int secondPressBuffer = 0;
    
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
    
    // TODO: Bombe legen neu machen
    
    public static void handleKeyEventPressed(int keyCode) {
	if (clientPlayer.getDead() == false) {
	    if (buttonPressed == false) {
		if (keyCode == clientPlayer.getCurrentButtonConfig().getUp()) {
		    ConsoleHandler.print("Client presses Button 'up'", MessageType.GAME);
		    clientPlayer.actionUp();
		    buttonPressedBuffer = keyCode;
		    secondPressBuffer = keyCode;
		    buttonPressed = true;
		} else if (keyCode == clientPlayer.getCurrentButtonConfig().getDown()) {
		    ConsoleHandler.print("Client presses Button 'down'", MessageType.GAME);
		    clientPlayer.actionDown();
		    buttonPressedBuffer = keyCode;
		    secondPressBuffer = keyCode;
		    buttonPressed = true;
		} else if (keyCode == clientPlayer.getCurrentButtonConfig().getLeft()) {
		    ConsoleHandler.print("Client presses Button 'left'", MessageType.GAME);
		    clientPlayer.actionLeft();
		    buttonPressedBuffer = keyCode;
		    secondPressBuffer = keyCode;
		    buttonPressed = true;
		} else if (keyCode == clientPlayer.getCurrentButtonConfig().getRight()) {
		    ConsoleHandler.print("Client presses Button 'right'", MessageType.GAME);
		    clientPlayer.actionRight();
		    buttonPressedBuffer = keyCode;
		    secondPressBuffer = keyCode;
		    buttonPressed = true;
		} /*else if (keyCode == clientPlayer.getCurrentButtonConfig().getSetBomb()) {
		    ConsoleHandler.print("Client presses Button 'setBomb'", MessageType.GAME);
		    buttonPressedBuffer = keyCode;
		    secondPressBuffer = keyCode;
		    buttonPressed = true;
		}*/
	    } else if (buttonPressed == true && secondPress == false) {
		if (keyCode == clientPlayer.getCurrentButtonConfig().getUp() && keyCode != secondPressBuffer) {
		    ConsoleHandler.print("Buffer 'up'", MessageType.GAME);
		    secondPressBuffer = keyCode;
		    secondPress = true;
		} else if (keyCode == clientPlayer.getCurrentButtonConfig().getDown() && keyCode != secondPressBuffer) {
		    ConsoleHandler.print("Buffer 'down'", MessageType.GAME);
		    secondPressBuffer = keyCode;
		    secondPress = true;
		} else if (keyCode == clientPlayer.getCurrentButtonConfig().getLeft() && keyCode != secondPressBuffer) {
		    ConsoleHandler.print("Buffer 'left'", MessageType.GAME);
		    secondPressBuffer = keyCode;
		    secondPress = true;
		} else if (keyCode == clientPlayer.getCurrentButtonConfig().getRight() && keyCode != secondPressBuffer) {
		    ConsoleHandler.print("Buffer 'right'", MessageType.GAME);
		    secondPressBuffer = keyCode;
		    secondPress = true;
		}/* else if (keyCode == clientPlayer.getCurrentButtonConfig().getSetBomb() && keyCode != secondPressBuffer) {
		    ConsoleHandler.print("Buffer 'setBomb'", MessageType.GAME);
		    secondPressBuffer = keyCode;
		    secondPress = true;
		}*/
	    }
	    
	}
    }
    
    public static void handleKeyEventReleased(int keyCode) {
	if (clientPlayer.getDead() == false && buttonPressed == true) {
	    if (secondPress == true) {
		if (keyCode == clientPlayer.getCurrentButtonConfig().getUp()) {
		    if (keyCode == secondPressBuffer) {
			ConsoleHandler.print("Unbuffer 'up'", MessageType.GAME);
			secondPressBuffer = buttonPressedBuffer;
		    } else {
			if (secondPressBuffer == clientPlayer.getCurrentButtonConfig().getDown()) {
			    clientPlayer.actionDown();
			} else if (secondPressBuffer == clientPlayer.getCurrentButtonConfig().getLeft()) {
			    clientPlayer.actionLeft();
			} else if (secondPressBuffer == clientPlayer.getCurrentButtonConfig().getRight()) {
			    clientPlayer.actionRight();
			}
			buttonPressedBuffer = secondPressBuffer;
		    }
		    secondPress = false;
		} else if (keyCode == clientPlayer.getCurrentButtonConfig().getDown()) {
		    if (keyCode == secondPressBuffer) {
			ConsoleHandler.print("Unbuffer 'down'", MessageType.GAME);
			secondPressBuffer = buttonPressedBuffer;
		    } else {
			if (secondPressBuffer == clientPlayer.getCurrentButtonConfig().getUp()) {
			    clientPlayer.actionUp();
			} else if (secondPressBuffer == clientPlayer.getCurrentButtonConfig().getLeft()) {
			    clientPlayer.actionLeft();
			} else if (secondPressBuffer == clientPlayer.getCurrentButtonConfig().getRight()) {
			    clientPlayer.actionRight();
			}
			buttonPressedBuffer = secondPressBuffer;
		    }
		    secondPress = false;
		} else if (keyCode == clientPlayer.getCurrentButtonConfig().getLeft()) {
		    if (keyCode == secondPressBuffer) {
			ConsoleHandler.print("Unbuffer 'left'", MessageType.GAME);
			secondPressBuffer = buttonPressedBuffer;
		    } else {
			if (secondPressBuffer == clientPlayer.getCurrentButtonConfig().getUp()) {
			    clientPlayer.actionUp();
			} else if (secondPressBuffer == clientPlayer.getCurrentButtonConfig().getDown()) {
			    clientPlayer.actionDown();
			} else if (secondPressBuffer == clientPlayer.getCurrentButtonConfig().getRight()) {
			    clientPlayer.actionRight();
			}
			buttonPressedBuffer = secondPressBuffer;
		    }
		    secondPress = false;
		} else if (keyCode == clientPlayer.getCurrentButtonConfig().getRight()) {
		    if (keyCode == secondPressBuffer) {
			ConsoleHandler.print("Unbuffer 'right'", MessageType.GAME);
			secondPressBuffer = buttonPressedBuffer;
		    } else {
			if (secondPressBuffer == clientPlayer.getCurrentButtonConfig().getUp()) {
			    clientPlayer.actionUp();
			} else if (secondPressBuffer == clientPlayer.getCurrentButtonConfig().getDown()) {
			    clientPlayer.actionDown();
			} else if (secondPressBuffer == clientPlayer.getCurrentButtonConfig().getLeft()) {
			    clientPlayer.actionLeft();
			}
			buttonPressedBuffer = secondPressBuffer;
		    }
		    secondPress = false;
		} /*else if (keyCode == clientPlayer.getCurrentButtonConfig().getSetBomb()) {
		    
		}*/
	    } else {
		if (keyCode == clientPlayer.getCurrentButtonConfig().getUp()) {
		    ConsoleHandler.print("Client released Button 'up'", MessageType.GAME);
		    clientPlayer.actionStop();
		    buttonPressedBuffer = 0;
		    buttonPressed = false;
		} else if (keyCode == clientPlayer.getCurrentButtonConfig().getDown()) {
		    ConsoleHandler.print("Client released Button 'down'", MessageType.GAME);
		    clientPlayer.actionStop();
		    buttonPressedBuffer = 0;
		    buttonPressed = false;
		} else if (keyCode == clientPlayer.getCurrentButtonConfig().getLeft()) {
		    ConsoleHandler.print("Client released Button 'left'", MessageType.GAME);
		    clientPlayer.actionStop();
		    buttonPressedBuffer = 0;
		    buttonPressed = false;
		} else if (keyCode == clientPlayer.getCurrentButtonConfig().getRight()) {
		    ConsoleHandler.print("Client released Button 'right'", MessageType.GAME);
		    clientPlayer.actionStop();
		    buttonPressedBuffer = 0;
		    buttonPressed = false;
		} /*else if (keyCode == clientPlayer.getCurrentButtonConfig().getSetBomb()) {
		    ConsoleHandler.print("Client released Button 'setBomb'", MessageType.GAME);
		}*/
	    }
	}
    }
}
