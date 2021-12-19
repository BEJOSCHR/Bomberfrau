/*
 * PlayerHandler
 *
 * Version 0.4.1
 * 
 * Author: Christopher
 * 
 * Datum: 18.12.2021
 *
 * Verwaltet die erzeugten Player und deren Steuerung
 */

package uni.bombenstimmung.de.game;

import java.awt.Color;
import java.awt.Graphics;

import java.util.ArrayList;
import java.util.Collections;

import uni.bombenstimmung.de.backend.console.ConsoleHandler;
import uni.bombenstimmung.de.backend.console.MessageType;
import uni.bombenstimmung.de.backend.graphics.GraphicsHandler;

public class PlayerHandler {
    static Player clientPlayer = new Player("Bob");
    static ArrayList<Player> opponentPlayers = new ArrayList<Player>();
    static int opponentCount = 0;
    static boolean playerMoving = false;
    static boolean multiPress = false;
    static ArrayList<Integer> inputBuffer = new ArrayList<Integer>();
    
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
	    if (playerMoving == false) {
		if (keyCode == clientPlayer.getCurrentButtonConfig().getUp()) {
		    ConsoleHandler.print("Client presses Button 'up'", MessageType.GAME);
		    clientPlayer.actionUp();
		    inputBuffer.add(keyCode);
		    playerMoving = true;
		} else if (keyCode == clientPlayer.getCurrentButtonConfig().getDown()) {
		    ConsoleHandler.print("Client presses Button 'down'", MessageType.GAME);
		    clientPlayer.actionDown();
		    inputBuffer.add(keyCode);
		    playerMoving = true;
		} else if (keyCode == clientPlayer.getCurrentButtonConfig().getLeft()) {
		    ConsoleHandler.print("Client presses Button 'left'", MessageType.GAME);
		    clientPlayer.actionLeft();
		    inputBuffer.add(keyCode);
		    playerMoving = true;
		} else if (keyCode == clientPlayer.getCurrentButtonConfig().getRight()) {
		    ConsoleHandler.print("Client presses Button 'right'", MessageType.GAME);
		    clientPlayer.actionRight();
		    inputBuffer.add(keyCode);
		    playerMoving = true;
		} /*else if (keyCode == clientPlayer.getCurrentButtonConfig().getSetBomb()) {
		    ConsoleHandler.print("Client presses Button 'setBomb'", MessageType.GAME);
		    playerMoving = true;
		}*/
	    } else if (playerMoving == true/* && multiPress == false*/) {
		if (keyCode == clientPlayer.getCurrentButtonConfig().getUp() && inputBuffer.contains(keyCode) == false) {
		    ConsoleHandler.print("Buffer 'up'", MessageType.GAME);
		    inputBuffer.add(keyCode);
		    multiPress = true;
		} else if (keyCode == clientPlayer.getCurrentButtonConfig().getDown() && inputBuffer.contains(keyCode) == false) {
		    ConsoleHandler.print("Buffer 'down'", MessageType.GAME);
		    inputBuffer.add(keyCode);
		    multiPress = true;
		} else if (keyCode == clientPlayer.getCurrentButtonConfig().getLeft() && inputBuffer.contains(keyCode) == false) {
		    ConsoleHandler.print("Buffer 'left'", MessageType.GAME);
		    inputBuffer.add(keyCode);
		    multiPress = true;
		} else if (keyCode == clientPlayer.getCurrentButtonConfig().getRight() && inputBuffer.contains(keyCode) == false) {
		    ConsoleHandler.print("Buffer 'right'", MessageType.GAME);
		    inputBuffer.add(keyCode);
		    multiPress = true;
		}/* else if (keyCode == clientPlayer.getCurrentButtonConfig().getSetBomb() && keyCode != multiPressBuffer) {
		    ConsoleHandler.print("Buffer 'setBomb'", MessageType.GAME);
		    multiPress = true;
		}*/
	    }
	}
    }
    
    @SuppressWarnings("removal")
    public static void handleKeyEventReleased(int keyCode) {
	if (clientPlayer.getDead() == false && playerMoving == true) {
	    if (multiPress == true) {
		if (keyCode == clientPlayer.getCurrentButtonConfig().getUp()) {
		    if (keyCode != inputBuffer.get(0)) {
			ConsoleHandler.print("Unbuffer 'up'", MessageType.GAME);
			inputBuffer.remove(new Integer(keyCode));
		    } else {
			ConsoleHandler.print("Client released Button 'up'", MessageType.GAME);
			if (inputBuffer.get(inputBuffer.size() - 1) == clientPlayer.getCurrentButtonConfig().getDown()) {
			    clientPlayer.actionDown();
			} else if (inputBuffer.get(inputBuffer.size() - 1) == clientPlayer.getCurrentButtonConfig().getLeft()) {
			    clientPlayer.actionLeft();
			} else if (inputBuffer.get(inputBuffer.size() - 1) == clientPlayer.getCurrentButtonConfig().getRight()) {
			    clientPlayer.actionRight();
			}
			inputBuffer.remove(0);
			Collections.swap(inputBuffer, 0, inputBuffer.size() - 1);
		    }
		    if (inputBuffer.size() == 1) {
			multiPress = false;
		    }
		} else if (keyCode == clientPlayer.getCurrentButtonConfig().getDown()) {
		    if (keyCode != inputBuffer.get(0)) {
			ConsoleHandler.print("Unbuffer 'down'", MessageType.GAME);
			inputBuffer.remove(new Integer(keyCode));
		    } else {
			ConsoleHandler.print("Client released Button 'down'", MessageType.GAME);
			if (inputBuffer.get(inputBuffer.size() - 1) == clientPlayer.getCurrentButtonConfig().getUp()) {
			    clientPlayer.actionUp();
			} else if (inputBuffer.get(inputBuffer.size() - 1) == clientPlayer.getCurrentButtonConfig().getLeft()) {
			    clientPlayer.actionLeft();
			} else if (inputBuffer.get(inputBuffer.size() - 1) == clientPlayer.getCurrentButtonConfig().getRight()) {
			    clientPlayer.actionRight();
			}
			inputBuffer.remove(0);
			Collections.swap(inputBuffer, 0, inputBuffer.size() - 1);
		    }
		    if (inputBuffer.size() == 1) {
			multiPress = false;
		    }
		} else if (keyCode == clientPlayer.getCurrentButtonConfig().getLeft()) {
		    if (keyCode != inputBuffer.get(0)) {
			ConsoleHandler.print("Unbuffer 'left'", MessageType.GAME);
			inputBuffer.remove(new Integer(keyCode));
		    } else {
			ConsoleHandler.print("Client released Button 'left'", MessageType.GAME);
			if (inputBuffer.get(inputBuffer.size() - 1) == clientPlayer.getCurrentButtonConfig().getUp()) {
			    clientPlayer.actionUp();
			} else if (inputBuffer.get(inputBuffer.size() - 1) == clientPlayer.getCurrentButtonConfig().getDown()) {
			    clientPlayer.actionDown();
			} else if (inputBuffer.get(inputBuffer.size() - 1) == clientPlayer.getCurrentButtonConfig().getRight()) {
			    clientPlayer.actionRight();
			}
			inputBuffer.remove(0);
			Collections.swap(inputBuffer, 0, inputBuffer.size() - 1);
		    }
		    if (inputBuffer.size() == 1) {
			multiPress = false;
		    }
		} else if (keyCode == clientPlayer.getCurrentButtonConfig().getRight()) {
		    if (keyCode != inputBuffer.get(0)) {
			ConsoleHandler.print("Unbuffer 'right'", MessageType.GAME);
			inputBuffer.remove(new Integer(keyCode));
		    } else {
			ConsoleHandler.print("Client released Button 'right'", MessageType.GAME);
			if (inputBuffer.get(inputBuffer.size() - 1) == clientPlayer.getCurrentButtonConfig().getUp()) {
			    clientPlayer.actionUp();
			} else if (inputBuffer.get(inputBuffer.size() - 1) == clientPlayer.getCurrentButtonConfig().getDown()) {
			    clientPlayer.actionDown();
			} else if (inputBuffer.get(inputBuffer.size() - 1) == clientPlayer.getCurrentButtonConfig().getLeft()) {
			    clientPlayer.actionLeft();
			}
			inputBuffer.remove(0);
			Collections.swap(inputBuffer, 0, inputBuffer.size() - 1);
		    }
		    if (inputBuffer.size() == 1) {
			multiPress = false;
		    }
		} /*else if (keyCode == clientPlayer.getCurrentButtonConfig().getSetBomb()) {
		    
		}*/
	    } else {
		if (keyCode == clientPlayer.getCurrentButtonConfig().getUp()) {
		    ConsoleHandler.print("Client released Button 'up'", MessageType.GAME);
		    clientPlayer.actionStop();
		    inputBuffer.clear();
		    playerMoving = false;
		} else if (keyCode == clientPlayer.getCurrentButtonConfig().getDown()) {
		    ConsoleHandler.print("Client released Button 'down'", MessageType.GAME);
		    clientPlayer.actionStop();
		    inputBuffer.clear();
		    playerMoving = false;
		} else if (keyCode == clientPlayer.getCurrentButtonConfig().getLeft()) {
		    ConsoleHandler.print("Client released Button 'left'", MessageType.GAME);
		    clientPlayer.actionStop();
		    inputBuffer.clear();
		    playerMoving = false;
		} else if (keyCode == clientPlayer.getCurrentButtonConfig().getRight()) {
		    ConsoleHandler.print("Client released Button 'right'", MessageType.GAME);
		    clientPlayer.actionStop();
		    inputBuffer.clear();
		    playerMoving = false;
		} /*else if (keyCode == clientPlayer.getCurrentButtonConfig().getSetBomb()) {
		    ConsoleHandler.print("Client released Button 'setBomb'", MessageType.GAME);
		}*/
	    }
	}
    }
}
