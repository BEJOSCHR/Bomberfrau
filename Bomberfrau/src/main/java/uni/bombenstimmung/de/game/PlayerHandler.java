/*
 * PlayerHandler
 *
 * Version 0.4.1
 * 
 * Author: Christopher
 * 
 * Datum: 18.12.2021
 *
 * Diese Klasse ist zustaendig fuer die Gesamtheit der Player im Spiel.
 * Es koennen der clientPlayer, also der steuerbare Player in der
 * laufen Programminstanz, angepasst und zusaetzliche opponentPlayers
 * hinzugefuegt werden.
 * Ausserdem werden hier die Player-Ereignisse der Tastenbetaetigungen verwaltet.
 */

package uni.bombenstimmung.de.game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collections;

import uni.bombenstimmung.de.backend.console.ConsoleHandler;
import uni.bombenstimmung.de.backend.console.MessageType;
import uni.bombenstimmung.de.backend.graphics.GraphicsHandler;
import uni.bombenstimmung.de.backend.images.ImageHandler;
import uni.bombenstimmung.de.backend.images.ImageType;
import uni.bombenstimmung.de.backend.serverconnection.host.ConnectedClient;
import uni.bombenstimmung.de.lobby.LobbyCreate;

public class PlayerHandler {
    private static Player clientPlayer;
    private static ArrayList<Player> opponentPlayers = new ArrayList<Player>();
    private static ArrayList<Player> allPlayer = new ArrayList<Player>();
    private static int opponentCount = 0;
    private static boolean playerMoving = false;
    private static boolean multiPress = false;
    private static ArrayList<Integer> inputBuffer = new ArrayList<Integer>();
    private static boolean debugKeys = true;
    
    // vorlaeufige ArrayList mit allen Playern aus der Lobby
    private static ArrayList<Player> playerFromLobby = new ArrayList<Player>();
    
    /**
     * Gibt die Gesamtzahl der am Spiel teilnehmenden Players zurueck.
     * @return Integer mit opponentPlayers + 1
     */
    public static int getPlayerAmount() {
    return opponentPlayers.size()+1;
    }
    
    public static Player getClientPlayer() {
	return clientPlayer;
    }
    
    public static ArrayList<Player> getOpponentPlayers() {
	return opponentPlayers;
    }
    
    public static int getOpponentCount() {
	return opponentCount;
    }
    
    /**
     * Fuehrt den clientPlayer und eine zugegebene Player-ArrayList zusammen
     * und speichert diese in die ArrayList 'allPlayer'.
     * @param array	beliebige Player-ArrayList zum Zusammenfuehren
     */
    public static void addToAllPlayers(ArrayList<Player> array) {
	if(allPlayer.contains(clientPlayer)) {
	    allPlayer.addAll(array);
	} else {
	    int id = clientPlayer.getId();
	    allPlayer.addAll(array);
	    allPlayer.add(id, clientPlayer);
	}
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
    public static void setClientPlayer(int id, String name, String ipAdress, boolean host, int skin, Point pos, ConnectedClient cC) {
	clientPlayer = new Player(id, name, ipAdress, host, skin, pos, cC);
    }
    
    /**
     * Setzt den Player, welcher auf der eigenen Programminstanz der zu steuernde Player ist.
     * @param p	Player-Objekt, welcher der Client sein soll.
     */
    public static void setClientPlayer(Player p) {
	clientPlayer = p;
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
    public static void addOpponentPlayer(int id, String name, String ipAdress, boolean host, int skin, Point pos, ConnectedClient cC) {
	opponentPlayers.add(new Player(id, name, ipAdress, host, skin, pos, cC));
	opponentCount++;
    }
    
    /**
     * Fuegt einen Player hinzu, der aus Sicht des Benutzers sein Gegenspieler ist.
     * @param p	Player-Objekt, welcher ein Gegenspieler sein soll.
     */
    public static void addOpponentPlayer(Player p) {
	opponentPlayers.add(p);
	opponentCount++;
    }
    
    /**
     * Loescht die Gegenspieler-Liste vollstaendig.
     */
    public static void clearOpponentPlayers() {
	opponentPlayers.clear();
	opponentCount = 0;
    }
    
    /**
     * Setzt jegliche Tasteneingaben fuer die Bewegung zurueck,
     * indem der inputBuffer geleert wird und die Booleans fuer
     * playerMoving und multiPress auf false gesetzt werden.
     */
    public static void resetMovement() {
	playerMoving = false;
	multiPress = false;
	inputBuffer.clear();
    }
    
    /**
     * Draw-Methode mit allen Player-bezogenen Grafiken. Wird vom GraphicsHandler aufgerufen.
     * @param g	Graphics-Variable, auf welcher die neuen Elemente gemalt werden sollen.
     */
    public static void drawPlayers(Graphics g) {
	if (clientPlayer.getDead() == false) {
	    // Anzeigen der Hitbox
	    /*if (debugKeys) {
		g.setColor(Color.red);
		g.drawRect((int)(clientPlayer.getPosition().getX() - clientPlayer.getXHitbox()),
			(int)(clientPlayer.getPosition().getY() - clientPlayer.getYHitbox()),
			(int)(clientPlayer.getXHitbox()*2), (int)(clientPlayer.getYHitbox()*2));
		g.fillRect((int)(clientPlayer.getPosition().getX() - clientPlayer.getXHitbox()),
			(int)(clientPlayer.getPosition().getY() - clientPlayer.getYHitbox()),
			(int)(clientPlayer.getXHitbox()*2), (int)(clientPlayer.getYHitbox()*2));
	    }*/
		g.drawImage(ImageHandler.getImage(ImageType.IMAGE_INGAME_CHARACTER_IDLE).getImage(), (int)(clientPlayer.getPosition().getX()-(GameData.FIELD_DIMENSION/2)), (int)(clientPlayer.getPosition().getY()-(GameData.FIELD_DIMENSION/2)), null);
	} else {
	    g.setColor(Color.black);
	    /*g.drawRect((int)(clientPlayer.getPosition().getX() - GraphicsHandler.getWidth()/44.5/2),
		    	(int)(clientPlayer.getPosition().getY() - GraphicsHandler.getHeight()/25/2),
		    	(int)(GraphicsHandler.getWidth()/44.5), (int)(GraphicsHandler.getHeight()/25));*/
	    g.fillRect((int)(clientPlayer.getPosition().getX() - GraphicsHandler.getWidth()/44.5/2),
		    	(int)(clientPlayer.getPosition().getY() - GraphicsHandler.getHeight()/25/2),
		    	(int)(GraphicsHandler.getWidth()/44.5), (int)(GraphicsHandler.getHeight()/25));
	}
	for (Player i : opponentPlayers) {
	    if (i.getDead() == false) {
		g.drawImage(ImageHandler.getImage(ImageType.IMAGE_INGAME_CHARACTER_IDLE).getImage(), (int)(i.getPosition().getX()-(GameData.FIELD_DIMENSION/2)), (int)(i.getPosition().getY()-(GameData.FIELD_DIMENSION/2)), null);
	    } else {
		g.setColor(Color.black);
		/*g.drawRect((int)(i.getPosition().getX() - GraphicsHandler.getWidth()/44.5/2),
			    (int)(i.getPosition().getY() - GraphicsHandler.getHeight()/25/2),
			    (int)(GraphicsHandler.getWidth()/44.5), (int)(GraphicsHandler.getHeight()/25));*/
		g.fillRect((int)(i.getPosition().getX() - GraphicsHandler.getWidth()/44.5/2),
			    (int)(i.getPosition().getY() - GraphicsHandler.getHeight()/25/2),
			    (int)(GraphicsHandler.getWidth()/44.5), (int)(GraphicsHandler.getHeight()/25));
	    }
	    
	}
    }
    
    /**
     * Hier werden Ereignisse ausgefuehrt die auftreten sollen, wenn eine bestimmte
     * Taste gedrueckt wird.
     * @param keyCode	Tasten-Code in Integer-Form
     */
    public static void handleKeyEventPressed(int keyCode) {
	if (clientPlayer.getDead() == false && Game.getGameOver() == false) {
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
    
    /**
     * Hier werden Ereignisse ausgefuehrt die auftreten sollen, wenn eine
     * bereits runtergedrueckte Taste losgelassen wird.
     * @param keyCode	Tasten-Code in Integer-Form
     */
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
	if (clientPlayer.getDead() == false && keyCode == clientPlayer.getCurrentButtonConfig().getPlantBomb()) {
	    clientPlayer.actionPlantBomb();
	}
	/* Debug Tasten zum Testen von Funktionen. Koennen mit dem Boolean debugKey an-/abgeschaltet werden. */
	if (debugKeys) {
	    if (keyCode == KeyEvent.VK_O) {
		clientPlayer.increaseBombRadius();
	    } else if (keyCode == KeyEvent.VK_L) {
		clientPlayer.decreaseBombRadius();
	    } else if (keyCode == KeyEvent.VK_I) {
		clientPlayer.increaseMaxBombs();
	    } else if (keyCode == KeyEvent.VK_K) {
		clientPlayer.decreaseMaxBombs();
	    } else if (keyCode == KeyEvent.VK_P) {
		if (clientPlayer.getDead() == false) {
		    clientPlayer.setDead(true);
		} else {
		    clientPlayer.setDead(false);
		}
	    } else if (keyCode == KeyEvent.VK_U) {
		clientPlayer.increaseMovementSpeed();
	    } else if (keyCode == KeyEvent.VK_J) {
		clientPlayer.decreaseMovementSpeed();
	    }
	}
    }
    
    public static ArrayList<Player> getAllPlayer(){
	return allPlayer;
    }
    
    public static boolean getDebugKeysState() {
	return debugKeys;
    }
    
    /**
     * Fuegt Player in die Liste playerFromLobby hinzu. Diese Methode ist fuer die Lobby
     * konzipiert und dient als Ausgangspunkt der Player-Organisation fuer das Ingame.
     * @param id	ID des Players
     * @param name	Name des Player
     * @param ipAdress	IP-Adresse zugehoerig zu dem Player
     * @param host	Boolean, ob dieser Player der Host des Spiels ist
     * @param skin	Skin-ID des Players
     * @param pos	Position des Players
     */
    public static void addPlayerFromLobby(int id, String name, String ipAdress, boolean host, int skin, Point pos, ConnectedClient cC) {
	playerFromLobby.add(new Player(id, name, ipAdress, host, skin, pos, cC));
    }
    
    /**
     * Verteilt die von der Lobby uebergebenen Players in die PlayerHandler Variablen/Listen
     * clientPlayer und opponentPlayers.
     */
    public static void initPlayers() {
	// TODO: Verteilung mit IP-Adressen Vergleich anpassen, wenn ServerConnection implementiert wird.
	
	for(Player p : playerFromLobby) {
	    if(p.getId() == LobbyCreate.client.getId()) {	// TODO: wenn Lobby connectedClient implementiert hat, dann hier ID von ConnectedClient abfragen
		setClientPlayer(p);
	    } else {
		addOpponentPlayer(p);
	    }
	}
    }
}
