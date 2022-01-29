/*
 * Game
 *
 * Version 1.1
 * Author: Dennis und Mustafa
 *
 * Malt und verwaltet die Map
 */

package uni.bombenstimmung.de.game;

import java.awt.Graphics;
import java.util.ArrayList;
import java.awt.Color;

import uni.bombenstimmung.de.backend.animation.Animation;
import uni.bombenstimmung.de.backend.animation.AnimationData;
import uni.bombenstimmung.de.backend.console.*;
import uni.bombenstimmung.de.backend.graphics.GraphicsHandler;
import uni.bombenstimmung.de.backend.images.ImageHandler;
import uni.bombenstimmung.de.backend.images.ImageType;
import uni.bombenstimmung.de.backend.sounds.SoundHandler;
import uni.bombenstimmung.de.backend.sounds.SoundType;
import uni.bombenstimmung.de.lobby.LobbyCreate;
import uni.bombenstimmung.de.lobby.LobbyPlayer;
import uni.bombenstimmung.de.backend.language.LanguageBlockType;
import uni.bombenstimmung.de.backend.language.LanguageHandler;
import uni.bombenstimmung.de.menu.MenuAnimations;
import uni.bombenstimmung.de.menu.Settings;

public class Game {

    private static Field map[][] = new Field[GameData.MAP_DIMENSION][GameData.MAP_DIMENSION];
    private static int mapNumber = 1;
    private static ArrayList<Bomb> placedBombs = new ArrayList<Bomb>();
    private static boolean gameOver = false;
    private static int countdown = 0;
    private static int lastPlayer = -1;
//    private static int livingPlayers;

    /**
     * Fuellt das Map Array mit leeren Feldern X baut auf von West nach Ost Y baut
     * auf von Nord nach Sued
     */
    public static void fillMap() {
	for (int x = 0; x < GameData.MAP_DIMENSION; x++) {
	    for (int y = 0; y < GameData.MAP_DIMENSION; y++) {
		map[x][y] = new Field(x, y, FieldContent.EMPTY);
	    }
	}
    }

    /**
     * Malt die aktuelle Map in der Mitte des Bildschirms
     */
    public static void drawGame(Graphics g) {
	// DRAW COUNTDOWN ETC

	// MAP
	int xOffset = GraphicsHandler.getWidth() - (GameData.FIELD_DIMENSION * GameData.MAP_DIMENSION);
	int yOffset = GameData.MAP_SIDE_BORDER;
	for (int x = GameData.MAP_DIMENSION - 1; x >= 0; x--) {
	    for (int y = GameData.MAP_DIMENSION - 1; y >= 0; y--) {
		map[x][y].drawField(g, (x * GameData.FIELD_DIMENSION) + (xOffset / 2),
			(y * GameData.FIELD_DIMENSION) + (yOffset / 2), map[x][y].getContent());
	    }
	}
    }

    /**
     * Fuellt das Map Array mit den Map-Spezifischen Feldern
     * 
     * @param MapNumber die Nummer der ausgewaehlten Map
     */
    public static void updateMap(int MapNumber) {
	// SYNTAX: 1,1,BL:1,2,BL: ...
	// RESET MAP
	Game.fillMap();

	String mapData;
	switch (getMapNumber()) {
	case 1:
	    mapData = GameData.MAP_1;
	    break;
	case 2:
	    mapData = GameData.MAP_2;
	    break;
	case 3:
	    mapData = GameData.MAP_3;
	    break;
	default:
	    ConsoleHandler.print("Unknown mapnumber, cant load map!");
	    return;
	}
	// CHANGE NOT DEFAULT FIELDS
	String[] fieldData = mapData.split(":");
	for (String field : fieldData) {
	    String[] data = field.split(",");
	    int y = Integer.parseInt(data[0]);
	    int x = Integer.parseInt(data[1]);
	    FieldContent type = Field.getFieldTypeFromRepresentation(data[2]);
	    map[x][y].setContent(type);
	}
    }

    /**
     * Gibt das Field aus dem Array anhand der Bildkoordinaten zurueck
     * 
     * @param x zu ueberpruefende X-Koordinate
     * @param y zu ueberprüfende Y-Koordinate
     * @return gibt das Field Objekt zurueck, das zu der Koordinate gehoert
     */
    public static Field getFieldFromCoord(int x, int y) {
	Field field = null;
	int xOffset = GraphicsHandler.getWidth() - (GameData.FIELD_DIMENSION * GameData.MAP_DIMENSION);
	int yOffset = GameData.MAP_SIDE_BORDER;
	for (int j = GameData.MAP_DIMENSION - 1; j >= 0; j--) {
	    for (int i = GameData.MAP_DIMENSION - 1; i >= 0; i--) {
		if (x >= (i * GameData.FIELD_DIMENSION) + (xOffset / 2)
			&& y >= (j * GameData.FIELD_DIMENSION) + (yOffset / 2)
			&& x < (i * GameData.FIELD_DIMENSION) + (xOffset / 2) + GameData.FIELD_DIMENSION
			&& y < (j * GameData.FIELD_DIMENSION) + (yOffset / 2) + GameData.FIELD_DIMENSION) {
		    field = map[i][j];
		}
	    }
	}
	return field;
    }

    /**
     * Gibt das Field aus dem Array anhand der Mapkoordinaten zurueck
     * 
     * @param x X-Koordinate der Map
     * @param y Y-Koordinate der Map
     * @return gibt das Field Objekt zurueck, das zu der Koordinate gehoert
     */
    public static Field getFieldFromMap(int x, int y) {
	return map[x][y];
    }

    /**
     * Malt den Hintergrund im Ingame Mapspezifisch
     * 
     * @param bgnumber, 1 = Gras, 2 = Wueste
     */
    public static void drawBackground(Graphics g, int bgnumber) {
	Color yellowColor = new Color(217, 212, 163);
	Color greenColor = new Color(110, 150, 42);
	Color grayColor = new Color(143, 90, 90);
	switch (bgnumber) {
	case 1:
	    g.setColor(greenColor);
	    g.fillRect(0, 0, GraphicsHandler.getWidth(), GraphicsHandler.getHeight());
	    break;
	case 2:
	    g.setColor(yellowColor);
	    g.fillRect(0, 0, GraphicsHandler.getWidth(), GraphicsHandler.getHeight());
	    break;
	default:
	    g.setColor(grayColor);
	    g.fillRect(0, 0, GraphicsHandler.getWidth(), GraphicsHandler.getHeight());
	    break;
	}

    }

    /**
     * Malt den Rechten Teil des Ingame Menues
     * 
     * @param map Map Nummer anhand derer Titel und Menue gestaltet werden
     */
    public static void drawRightPartOfMap(Graphics g, int map, int gameTime) {
	int xOffset = GraphicsHandler.getWidth() - (GameData.FIELD_DIMENSION * GameData.MAP_DIMENSION);
	int yStart = GameData.MAP_SIDE_BORDER;
	int xStart = (GameData.FIELD_DIMENSION * GameData.MAP_DIMENSION) + (xOffset / 2);

	switch (map) {
	case 1:
	    GraphicsHandler.drawCentralisedText(g, Color.BLACK, Settings.scaleValue(25f), GameData.MAP_1_NAME,
		    xStart + xOffset / 4, yStart + 50);
	    break;
	case 2:
	    GraphicsHandler.drawCentralisedText(g, Color.BLACK, Settings.scaleValue(25f), GameData.MAP_2_NAME,
		    xStart + xOffset / 4, yStart + 50);
	    break;
	case 3:
	    GraphicsHandler.drawCentralisedText(g, Color.BLACK, Settings.scaleValue(25f), GameData.MAP_3_NAME,
		    xStart + xOffset / 4, yStart + 50);
	    break;
	}
	GameCounter.drawCounter(g, xStart + xOffset / 4, yStart);
    }

    /**
     * Malt den Linken Teil des Ingame Menues
     * 
     * @param anzPlayer die Anzahl der teilnehmenden Spieler zur Berechnung der
     *                  Textaufteilung
     */
    public static void drawLeftPartOfMap(Graphics g, int anzPlayer) {
	int counter = 0;
	int gap = GraphicsHandler.getHeight() / (anzPlayer + (anzPlayer + 1));
	double resScale = (double) GraphicsHandler.getHeight() / 720.0;
	int xOffset = GraphicsHandler.getWidth() - (GameData.FIELD_DIMENSION * GameData.MAP_DIMENSION);

	for (Player i : PlayerHandler.getAllPlayer()) {
	    GraphicsHandler.drawCentralisedText(g, Color.BLACK, Settings.scaleValue(25f),
		    LanguageHandler.getLLB(LanguageBlockType.LB_INGAME_PLAYER).getContent() + " " + (i.getId() + 1)
			    + ":",
		    0 + (xOffset / 4), 0 + ((counter + (counter + 1)) * gap));
	    if (i.getName().length() <= 13) {
		GraphicsHandler.drawCentralisedText(g, Color.BLACK, Settings.scaleValue(25f), i.getName(),
			0 + (xOffset / 4), 0 + ((counter + (counter + 1)) * gap + (int) (25.0 * resScale)));
	    } else {
		GraphicsHandler.drawCentralisedText(g, Color.BLACK,
			Settings.scaleValue(25f - ((float) i.getName().length()) / 1.5f), i.getName(),
			0 + (xOffset / 4), 0 + ((counter + (counter + 1)) * gap + (int) (25.0 * resScale)));
	    }
	    switch (i.getSkin()) {
	    case 0:
		if (i.isDead()) {
		    g.drawImage(LobbyPlayer.skinSelection[0][0].getImage(), 0 + (int) (xOffset / 5.6),
			    0 + ((counter + (counter + 1)) * gap + (int) (40.0 * resScale)),
			    (int) (GameData.FIELD_DIMENSION * 2.1), (int) (GameData.FIELD_DIMENSION * 2.1), null);
		    g.drawImage(ImageHandler.getImage(ImageType.IMAGE_INGAME_WASTED).getImage(),
			    0 + (int) (xOffset / 5.6), 0 + ((counter + (counter + 1)) * gap + (int) (40.0 * resScale)),
			    (int) (GameData.FIELD_DIMENSION * 2.1), (int) (GameData.FIELD_DIMENSION * 2.1), null);
		} else {
		    g.drawImage(LobbyPlayer.skinSelection[0][AnimationData.lobby_walk].getImage(),
			    0 + (int) (xOffset / 5.6), 0 + ((counter + (counter + 1)) * gap + (int) (40.0 * resScale)),
			    (int) (GameData.FIELD_DIMENSION * 2.1), (int) (GameData.FIELD_DIMENSION * 2.1), null);
		}
		break;

	    case 1:
		if (i.isDead()) {
		    g.drawImage(LobbyPlayer.skinSelection[1][0].getImage(), 0 + (int) (xOffset / 5.6),
			    0 + ((counter + (counter + 1)) * gap + (int) (40.0 * resScale)),
			    (int) (GameData.FIELD_DIMENSION * 2.1), (int) (GameData.FIELD_DIMENSION * 2.1), null);
		    g.drawImage(ImageHandler.getImage(ImageType.IMAGE_INGAME_WASTED).getImage(),
			    0 + (int) (xOffset / 5.6), 0 + ((counter + (counter + 1)) * gap + (int) (40.0 * resScale)),
			    (int) (GameData.FIELD_DIMENSION * 2.1), (int) (GameData.FIELD_DIMENSION * 2.1), null);
		} else {
		    g.drawImage(LobbyPlayer.skinSelection[1][AnimationData.lobby_walk].getImage(),
			    0 + (int) (xOffset / 5.6), 0 + ((counter + (counter + 1)) * gap + (int) (40.0 * resScale)),
			    (int) (GameData.FIELD_DIMENSION * 2.1), (int) (GameData.FIELD_DIMENSION * 2.1), null);
		}
		break;

	    case 2:
		if (i.isDead()) {
		    g.drawImage(LobbyPlayer.skinSelection[2][0].getImage(), 0 + (int) (xOffset / 5.6),
			    0 + ((counter + (counter + 1)) * gap + (int) (40.0 * resScale)),
			    (int) (GameData.FIELD_DIMENSION * 2.1), (int) (GameData.FIELD_DIMENSION * 2.1), null);
		    g.drawImage(ImageHandler.getImage(ImageType.IMAGE_INGAME_WASTED).getImage(),
			    0 + (int) (xOffset / 5.6), 0 + ((counter + (counter + 1)) * gap + (int) (40.0 * resScale)),
			    (int) (GameData.FIELD_DIMENSION * 2.1), (int) (GameData.FIELD_DIMENSION * 2.1), null);
		} else {
		    g.drawImage(LobbyPlayer.skinSelection[2][AnimationData.lobby_walk].getImage(),
			    0 + (int) (xOffset / 5.6), 0 + ((counter + (counter + 1)) * gap + (int) (40.0 * resScale)),
			    (int) (GameData.FIELD_DIMENSION * 2.1), (int) (GameData.FIELD_DIMENSION * 2.1), null);
		}
		break;

	    case 3:
		if (i.isDead()) {
		    g.drawImage(LobbyPlayer.skinSelection[3][0].getImage(), 0 + (int) (xOffset / 5.6),
			    0 + ((counter + (counter + 1)) * gap + (int) (40.0 * resScale)),
			    (int) (GameData.FIELD_DIMENSION * 2.1), (int) (GameData.FIELD_DIMENSION * 2.1), null);
		    g.drawImage(ImageHandler.getImage(ImageType.IMAGE_INGAME_WASTED).getImage(),
			    0 + (int) (xOffset / 5.6), 0 + ((counter + (counter + 1)) * gap + (int) (40.0 * resScale)),
			    (int) (GameData.FIELD_DIMENSION * 2.1), (int) (GameData.FIELD_DIMENSION * 2.1), null);
		} else {
		    g.drawImage(LobbyPlayer.skinSelection[3][AnimationData.lobby_walk].getImage(),
			    0 + (int) (xOffset / 5.6), 0 + ((counter + (counter + 1)) * gap + (int) (40.0 * resScale)),
			    (int) (GameData.FIELD_DIMENSION * 2.1), (int) (GameData.FIELD_DIMENSION * 2.1), null);
		}
		break;

	    default:
		ConsoleHandler.print("Wrong Skin ID!", MessageType.GAME);
	    }

	    counter++;
	}
    }

    public static void ringOfDeath(int ring) {
	// NORD NACH SUED VORNE
	for (int i = 0; i < GameData.MAP_DIMENSION; i++) {
	    changeFieldContent(FieldContent.BORDER, ring, i);
	    // Toete ggf Player
	    if (PlayerHandler.getClientPlayer().getCurrentField() == Game.getFieldFromMap(ring, i)) {
		PlayerHandler.getClientPlayer().setDead(true);
	    }
	    for (Player j : PlayerHandler.getOpponentPlayers()) {
		if (j.getCurrentField() == Game.getFieldFromMap(ring, i)) {
		    j.setDead(true);
		}
	    }
	}
	// WEST NACH OST OBEN
	for (int i = 0; i < GameData.MAP_DIMENSION; i++) {
	    changeFieldContent(FieldContent.BORDER, i, ring);
	    if (PlayerHandler.getClientPlayer().getCurrentField() == Game.getFieldFromMap(i, ring)) {
		PlayerHandler.getClientPlayer().setDead(true);
	    }
	    for (Player j : PlayerHandler.getOpponentPlayers()) {
		if (j.getCurrentField() == Game.getFieldFromMap(i, ring)) {
		    j.setDead(true);
		}
	    }
	}
	// NORD NACH SUED HINTEN
	for (int i = 0; i < GameData.MAP_DIMENSION; i++) {
	    changeFieldContent(FieldContent.BORDER, (GameData.MAP_DIMENSION - (ring + 1)), i);
	    if (PlayerHandler.getClientPlayer().getCurrentField() == Game
		    .getFieldFromMap(GameData.MAP_DIMENSION - (ring + 1), i)) {
		PlayerHandler.getClientPlayer().setDead(true);
	    }
	    for (Player j : PlayerHandler.getOpponentPlayers()) {
		if (j.getCurrentField() == Game.getFieldFromMap(GameData.MAP_DIMENSION - (ring + 1), i)) {
		    j.setDead(true);
		}
	    }
	}
	// WEST NACH OST HINTEN
	for (int i = 0; i < GameData.MAP_DIMENSION; i++) {
	    changeFieldContent(FieldContent.BORDER, i, (GameData.MAP_DIMENSION - (ring + 1)));
	    if (PlayerHandler.getClientPlayer().getCurrentField() == Game.getFieldFromMap(i,
		    GameData.MAP_DIMENSION - (ring + 1))) {
		PlayerHandler.getClientPlayer().setDead(true);
	    }
	    for (Player j : PlayerHandler.getOpponentPlayers()) {
		if (j.getCurrentField() == Game.getFieldFromMap(i, GameData.MAP_DIMENSION - (ring + 1))) {
		    j.setDead(true);
		}
	    }
	}
    }

    public Field[][] getMap() {
	return map;
    }

    public static int getMapNumber() {
	return mapNumber;
    }

    public static boolean getGameOver() {
	return gameOver;
    }

    public static void setMapNumber(int mapNumber) {
	Game.mapNumber = mapNumber;
    }

    public static void changeFieldContent(FieldContent t, int x, int y) {
	map[x][y].setContent(t);
    }

    public static void addBomb(int r, int t, int ownerId) {
	placedBombs.add(new Bomb(r, t, ownerId));
    }

    public static void removeBomb(Bomb b) {
	placedBombs.remove(b);
    }

    public static ArrayList<Bomb> getPlacedBombs() {
	return placedBombs;
    }

    public static void checkIfAllDead() {
	int livingPlayers = 0;
	lastPlayer = -1;
	for (Player i : PlayerHandler.getAllPlayer()) {
	    if (!i.isDead()) {
		livingPlayers++;
		lastPlayer = i.getId();
	    }
	}

	// ConsoleHandler.print("Living Players: " + livingPlayers, MessageType.GAME);
	if (gameOver == false && livingPlayers <= 1) {
	    MenuAnimations.titleShakeAni(5, 12);
	    if (livingPlayers == 1) {
		ConsoleHandler.print("winner is player " + lastPlayer, MessageType.GAME);
	    } else {
		ConsoleHandler.print("no one survived, lastPlayer = " + lastPlayer, MessageType.GAME);
	    }
	    gameOver();
	    if (LobbyCreate.client.isHost()) {
		LobbyCreate.client.sendMessageToAllClients("209-");
	    }
	}

    }

    public static void gameOver() {
	if (!gameOver) {
	    gameOver = true;

	    checkIfAllDead();

	    switch (mapNumber) {
	    case 1:
		SoundHandler.reducePlayingSound(SoundType.MAP1, 4, false);
		break;

	    case 2:
		SoundHandler.reducePlayingSound(SoundType.MAP2, 4, false);
		break;

	    case 3:
		SoundHandler.reducePlayingSound(SoundType.MAP3, 4, false);
		break;

	    default:
		ConsoleHandler.print("No music track available for this map!", MessageType.GAME);
	    }

	    new Animation(10, 40) {
		@Override
		public void initValues() {
		    PlayerHandler.getClientPlayer().actionStop();
		    PlayerHandler.setMovable(false);
		    for (Player i : PlayerHandler.getAllPlayer()) {
			i.stopTimer();
		    }
		}

		@Override
		public void changeValues() {
		    if (getSteps() == 1) {
			for (Bomb i : placedBombs) {
			    i.stopTimer();
			}
			checkIfAllDead();
		    }
		}

		@Override
		public void finaliseValues() {
		    GraphicsHandler.switchToAftergameFromIngame();
		}
	    };
	}
    }

    public static void resetIngame() {
	placedBombs.clear();
	gameOver = false;
	PlayerHandler.resetPlayerHandler();
	GameCounter.resetGameCounter();
    }

    public static int getCountdown() {
	return countdown;
    }

    public static int getLastPlayer() {
	return lastPlayer;
    }

    public static void setCountdown(int c) {
	countdown = c;
    }
}