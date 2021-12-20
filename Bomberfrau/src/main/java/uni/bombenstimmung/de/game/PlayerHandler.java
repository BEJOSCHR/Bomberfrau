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
import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;

import uni.bombenstimmung.de.backend.console.ConsoleHandler;
import uni.bombenstimmung.de.backend.console.MessageType;
import uni.bombenstimmung.de.backend.graphics.GraphicsHandler;

public class PlayerHandler {
    private static Player clientPlayer = new Player(0, "Bob", "localhost", true, 0,
	    					    new Point((int)((double)GraphicsHandler.getWidth()/2.13),
	    					    (int)((double)GraphicsHandler.getHeight()/2.7)));
    private static ArrayList<Player> opponentPlayers = new ArrayList<Player>();
    private static int opponentCount = 0;
    private static boolean playerMoving = false;
    private static boolean multiPress = false;
    private static ArrayList<Integer> inputBuffer = new ArrayList<Integer>();
    
    public static int getOpponentCount() {
	return opponentCount;
    }
    
    /**
     * Setzt den Player, welcher auf der eigenen Programminstanz der zu steuernde Player ist.
     * @param id	ID des Players
     * @param name	Name des Player
     * @param ipAdress	IP-Adresse zugehoerig zu dem Player
     * @param host	Boolean, ob dieser Player der Host des Spiels ist
     * @param skin	Skin-ID des Players
     * @param pos	Position des Players
     */
    public static void setClientPlayer(int id, String name, String ipAdress, boolean host, int skin, Point pos) {
	clientPlayer = new Player(id, name, ipAdress, host, skin, pos);
    }
    
    /**
     * Fuegt einen Player hinzu, der aus Sicht des Benutzers sein Gegenspieler ist.
     * @param id	ID des Players
     * @param name	Name des Player
     * @param ipAdress	IP-Adresse zugehoerig zu dem Player
     * @param host	Boolean, ob dieser Player der Host des Spiels ist
     * @param skin	Skin-ID des Players
     * @param pos	Position des Players
     */
    public static void addOpponentPlayer(int id, String name, String ipAdress, boolean host, int skin, Point pos) {
	opponentPlayers.add(new Player(id, name, ipAdress, host, skin, pos));
	opponentCount++;
    }
    
    /**
     * Loescht die Gegenspieler-Liste vollstaendig.
     */
    public static void clearOpponentPlayers() {
	opponentPlayers.clear();
	opponentCount = 0;
    }
    
    /* Draw-Methode mit allen Player-bezogenen Grafiken. Wird vom GraphicsHandler aufgerufen. */
    public static void drawPlayers(Graphics g) {
	if (clientPlayer.getDead() == false) {
	    g.setColor(Color.red);
	    g.drawRect((int)(clientPlayer.getPosition().getX() - GraphicsHandler.getWidth()/44.5/2),
		    	(int)(clientPlayer.getPosition().getY() - GraphicsHandler.getHeight()/25/2),
		    	(int)(GraphicsHandler.getWidth()/44.5), (int)(GraphicsHandler.getHeight()/25));
	    g.fillRect((int)(clientPlayer.getPosition().getX() - GraphicsHandler.getWidth()/44.5/2),
		    	(int)(clientPlayer.getPosition().getY() - GraphicsHandler.getHeight()/25/2),
		    	(int)(GraphicsHandler.getWidth()/44.5), (int)(GraphicsHandler.getHeight()/25));
	}
    }
    
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
		}
	    } else {
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
		}
	    }
	}
    }
    
    @SuppressWarnings("removal")	/* Um Warnung von 'new Integer(keyCode)' auszublenden. */
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
		}
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
		}
	    }
	}
	/* 
	 * Abfrage nach 'Bombe legen' geschieht unabhaengig von der Bewegungstastenabfrage
	 * (ergo kein InputBuffer noetig).
	 * Bombe wird erst bei Loslassen der Taste gelegt.
	 */
	if (clientPlayer.getDead() == false && keyCode == clientPlayer.getCurrentButtonConfig().getSetBomb()) {
	    ConsoleHandler.print("Client placed bomb.", MessageType.GAME);
	    clientPlayer.actionSetBomb();
	}
    }
}
