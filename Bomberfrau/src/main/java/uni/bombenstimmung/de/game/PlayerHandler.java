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

import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import uni.bombenstimmung.de.backend.console.ConsoleHandler;
import uni.bombenstimmung.de.backend.console.MessageType;
import uni.bombenstimmung.de.backend.images.ImageHandler;
import uni.bombenstimmung.de.backend.images.ImageType;
import uni.bombenstimmung.de.backend.serverconnection.host.ConnectedClient;
import uni.bombenstimmung.de.lobby.LobbyCreate;

public class PlayerHandler {
    private static Player clientPlayer;
    private static ArrayList<Player> opponentPlayers = new ArrayList<Player>();
    private static ArrayList<Player> allPlayer = new ArrayList<Player>();
    private static int opponentCount = 0;
    private static ArrayList<Integer> inputBuffer = new ArrayList<Integer>();
    private static boolean debugKeys = false;
    private static boolean change_ani = false;
    private static int change_int = 0;
    private static int[] remember_move = new int[4];
    private static boolean movable = false;
    // Bestimmt die Zeit zwischen den Animationen
    private static final int ANI_TIMER = 20;
    
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
	inputBuffer.clear();
    }
    
    /**
     * Draw-Methode mit allen Player-bezogenen Grafiken. Wird vom GraphicsHandler aufgerufen.
     * @param g	Graphics-Variable, auf welcher die neuen Elemente gemalt werden sollen.
     */
    public static void drawPlayers(Graphics g) {
	
	for (Player player : allPlayer) {
	    switch (player.getSkin()) {
	    case 0:
		if (player.isDead() == false) {
			if (player.getDirection() == 0) {
			    // Die letzte Animation wird sich gemerkt und beim Stehenbleiben angezeigt
			    if (remember_move[player.getId()] == 0)
				g.drawImage(ImageHandler.getImage(ImageType.IMAGE_INGAME_CHARACTER_IDLE).getImage(), (int)(player.getPosition().getX()-(GameData.FIELD_DIMENSION/2)), (int)(player.getPosition().getY()-(GameData.FIELD_DIMENSION/2)), GameData.FIELD_DIMENSION, GameData.FIELD_DIMENSION, null);
			    else if (remember_move[player.getId()] == 1)
				g.drawImage(ImageHandler.getImage(ImageType.IMAGE_INGAME_CHARACTER_WALK_NORTH_1).getImage(), (int)(player.getPosition().getX()-(GameData.FIELD_DIMENSION/2)), (int)(player.getPosition().getY()-(GameData.FIELD_DIMENSION/2)), GameData.FIELD_DIMENSION, GameData.FIELD_DIMENSION, null);
			    else if (remember_move[player.getId()] == 2)
				g.drawImage(ImageHandler.getImage(ImageType.IMAGE_INGAME_CHARACTER_WALK_1).getImage(), (int)(player.getPosition().getX()-(GameData.FIELD_DIMENSION/2)), (int)(player.getPosition().getY()-(GameData.FIELD_DIMENSION/2)), GameData.FIELD_DIMENSION, GameData.FIELD_DIMENSION, null);
			    else if (remember_move[player.getId()] == 3)
				g.drawImage(ImageHandler.getImage(ImageType.IMAGE_INGAME_CHARACTER_WALK_SIDE_LEFT_1).getImage(), (int)(player.getPosition().getX()-(GameData.FIELD_DIMENSION/2)), (int)(player.getPosition().getY()-(GameData.FIELD_DIMENSION/2)), GameData.FIELD_DIMENSION, GameData.FIELD_DIMENSION, null);
			    else if (remember_move[player.getId()] == 4)
				g.drawImage(ImageHandler.getImage(ImageType.IMAGE_INGAME_CHARACTER_WALK_SIDE_RIGHT_1).getImage(), (int)(player.getPosition().getX()-(GameData.FIELD_DIMENSION/2)), (int)(player.getPosition().getY()-(GameData.FIELD_DIMENSION/2)), GameData.FIELD_DIMENSION, GameData.FIELD_DIMENSION, null);
			}
			if (player.getDirection() == 1) {
			    remember_move[player.getId()] = 1;
			    if (change_ani)
				g.drawImage(ImageHandler.getImage(ImageType.IMAGE_INGAME_CHARACTER_WALK_NORTH_1).getImage(), (int)(player.getPosition().getX()-(GameData.FIELD_DIMENSION/2)), (int)(player.getPosition().getY()-(GameData.FIELD_DIMENSION/2)), GameData.FIELD_DIMENSION, GameData.FIELD_DIMENSION, null);
			    else
				g.drawImage(ImageHandler.getImage(ImageType.IMAGE_INGAME_CHARACTER_WALK_NORTH_IDLE).getImage(), (int)(player.getPosition().getX()-(GameData.FIELD_DIMENSION/2)), (int)(player.getPosition().getY()-(GameData.FIELD_DIMENSION/2)), GameData.FIELD_DIMENSION, GameData.FIELD_DIMENSION, null);
			    change_int++;
			    if (change_int >= ANI_TIMER) {
				change_int = 0;
				change_ani = !change_ani;
			    }
			}
			if (player.getDirection() == 2) {
			    remember_move[player.getId()] = 2;
			    if (change_ani)
				g.drawImage(ImageHandler.getImage(ImageType.IMAGE_INGAME_CHARACTER_WALK_1).getImage(), (int)(player.getPosition().getX()-(GameData.FIELD_DIMENSION/2)), (int)(player.getPosition().getY()-(GameData.FIELD_DIMENSION/2)), GameData.FIELD_DIMENSION, GameData.FIELD_DIMENSION, null);
			    else
				g.drawImage(ImageHandler.getImage(ImageType.IMAGE_INGAME_CHARACTER_WALK_2).getImage(), (int)(player.getPosition().getX()-(GameData.FIELD_DIMENSION/2)), (int)(player.getPosition().getY()-(GameData.FIELD_DIMENSION/2)), GameData.FIELD_DIMENSION, GameData.FIELD_DIMENSION, null);
			    change_int++;
			    if (change_int==ANI_TIMER) {
				change_int = 0;
				change_ani = !change_ani;
			    }
			}
			if (player.getDirection() == 3) {
			    remember_move[player.getId()] = 3;
			    if (change_ani)
				g.drawImage(ImageHandler.getImage(ImageType.IMAGE_INGAME_CHARACTER_WALK_SIDE_LEFT_1).getImage(), (int)(player.getPosition().getX()-(GameData.FIELD_DIMENSION/2)), (int)(player.getPosition().getY()-(GameData.FIELD_DIMENSION/2)), GameData.FIELD_DIMENSION, GameData.FIELD_DIMENSION, null);
			    else
				g.drawImage(ImageHandler.getImage(ImageType.IMAGE_INGAME_CHARACTER_WALK_SIDE_LEFT_IDLE).getImage(), (int)(player.getPosition().getX()-(GameData.FIELD_DIMENSION/2)), (int)(player.getPosition().getY()-(GameData.FIELD_DIMENSION/2)), GameData.FIELD_DIMENSION, GameData.FIELD_DIMENSION, null);
			    change_int++;
			    if (change_int==ANI_TIMER) {
				change_int = 0;
				change_ani = !change_ani;
			    }
			}
			if (player.getDirection() == 4) {
			    remember_move[player.getId()] = 4;
			    if (change_ani)
				g.drawImage(ImageHandler.getImage(ImageType.IMAGE_INGAME_CHARACTER_WALK_SIDE_RIGHT_1).getImage(), (int)(player.getPosition().getX()-(GameData.FIELD_DIMENSION/2)), (int)(player.getPosition().getY()-(GameData.FIELD_DIMENSION/2)), GameData.FIELD_DIMENSION, GameData.FIELD_DIMENSION, null);
			    else
				g.drawImage(ImageHandler.getImage(ImageType.IMAGE_INGAME_CHARACTER_WALK_SIDE_RIGHT_IDLE).getImage(), (int)(player.getPosition().getX()-(GameData.FIELD_DIMENSION/2)), (int)(player.getPosition().getY()-(GameData.FIELD_DIMENSION/2)), GameData.FIELD_DIMENSION, GameData.FIELD_DIMENSION, null);
			    change_int++;
			    if (change_int==ANI_TIMER) {
				change_int = 0;
				change_ani = !change_ani;
			    }
			}
		    } else {
			g.drawImage(ImageHandler.getImage(ImageType.IMAGE_INGAME_CHARACTER_DEAD).getImage(), (int)(player.getPosition().getX()-(GameData.FIELD_DIMENSION/2)), (int)(player.getPosition().getY()-(GameData.FIELD_DIMENSION/2)), GameData.FIELD_DIMENSION, GameData.FIELD_DIMENSION, null);
		    }
		break;
		
	    case 1:
		
		break;
		
	    case 2:
		
		break;
		
	    case 3:
		
		break;
		
	    default:
		ConsoleHandler.print("Wrong Skin ID!", MessageType.GAME);
	    }
	    
	}
    }
    
    /**
     * Hier werden Ereignisse ausgefuehrt die auftreten sollen, wenn eine bestimmte
     * Taste gedrueckt wird.
     * @param keyCode	Tasten-Code in Integer-Form
     */
    public static void handleKeyEventPressed(int keyCode) {
	if (clientPlayer.isDead() == false && Game.getGameOver() == false && movable == true) {
	    if (keyCode == clientPlayer.getCurrentButtonConfig().getUp() && inputBuffer.contains(keyCode) == false) {
		inputBuffer.add(keyCode);
		updateMovement();
	    } else if (keyCode == clientPlayer.getCurrentButtonConfig().getDown() && inputBuffer.contains(keyCode) == false) {
		inputBuffer.add(keyCode);
		updateMovement();
	    } else if (keyCode == clientPlayer.getCurrentButtonConfig().getLeft() && inputBuffer.contains(keyCode) == false) {
		inputBuffer.add(keyCode);
		updateMovement();
	    } else if (keyCode == clientPlayer.getCurrentButtonConfig().getRight() && inputBuffer.contains(keyCode) == false) {
		inputBuffer.add(keyCode);
		updateMovement();
	    }
	}
    }
    
    /**
     * Hier werden Ereignisse ausgefuehrt die auftreten sollen, wenn eine
     * bereits runtergedrueckte Taste losgelassen wird.
     * @param keyCode	Tasten-Code in Integer-Form
     */
    public static void handleKeyEventReleased(int keyCode) {
	if (clientPlayer.isDead() == false && Game.getGameOver() == false && movable == true) {
	    if (keyCode == clientPlayer.getCurrentButtonConfig().getUp() && inputBuffer.contains(keyCode) == true) {
		inputBuffer.remove(Integer.valueOf(keyCode));
		updateMovement();
	    } else if (keyCode == clientPlayer.getCurrentButtonConfig().getDown() && inputBuffer.contains(keyCode) == true) {
		inputBuffer.remove(Integer.valueOf(keyCode));
		updateMovement();
	    } else if (keyCode == clientPlayer.getCurrentButtonConfig().getLeft() && inputBuffer.contains(keyCode) == true) {
		inputBuffer.remove(Integer.valueOf(keyCode));
		updateMovement();
	    } else if (keyCode == clientPlayer.getCurrentButtonConfig().getRight() && inputBuffer.contains(keyCode) == true) {
		inputBuffer.remove(Integer.valueOf(keyCode));
		updateMovement();
	    }
	}
	/* 
	 * Abfrage nach 'Bombe legen' geschieht unabhaengig von der Bewegungstastenabfrage
	 * (ergo kein InputBuffer noetig).
	 * Bombe wird erst bei Loslassen der Taste gelegt.
	 */
	if (clientPlayer.isDead() == false && movable == true && Game.getGameOver() == false && keyCode == clientPlayer.getCurrentButtonConfig().getPlantBomb()) {
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
		if (clientPlayer.isDead() == false) {
		    clientPlayer.setDead(true);
		} else {
		    clientPlayer.setDead(false);
		}
	    } else if (keyCode == KeyEvent.VK_U) {
		clientPlayer.increaseMovementSpeed();
	    } else if (keyCode == KeyEvent.VK_J) {
		clientPlayer.decreaseMovementSpeed();
	    } else if (keyCode == KeyEvent.VK_M) {
		for (Player i : allPlayer) {
		    i.printPlayerInfo();
		}
		clientPlayer.printPlayerInfo();
		for (Player i : opponentPlayers) {
		    i.printPlayerInfo();
		}
	    }
	}
    }
    
    public static void updateMovement() {
	if (inputBuffer.isEmpty()) {
	    clientPlayer.actionStop();
	} else if (inputBuffer.get(0) == clientPlayer.getCurrentButtonConfig().getUp()) {
	    clientPlayer.actionUp();
	} else if (inputBuffer.get(0) == clientPlayer.getCurrentButtonConfig().getDown()) {
	    clientPlayer.actionDown();
	} else if (inputBuffer.get(0) == clientPlayer.getCurrentButtonConfig().getLeft()) {
	    clientPlayer.actionLeft();
	} else if (inputBuffer.get(0) == clientPlayer.getCurrentButtonConfig().getRight()) {
	    clientPlayer.actionRight();
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
    
    public static void resetPlayerHandler() {
	clientPlayer = null;
	opponentPlayers.clear();
	allPlayer.clear();
	opponentCount = 0;
	inputBuffer.clear();
	playerFromLobby.clear();
	for (int i = 0; i < 4; i++) {
	    remember_move[i] = 0;
	}
    }
    
    public static boolean isMovable() {
	return movable;
    }
    
    public static void setMovable(boolean m) {
	movable = m;
    }
}
