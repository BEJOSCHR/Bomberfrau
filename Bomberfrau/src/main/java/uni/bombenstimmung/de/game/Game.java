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
import uni.bombenstimmung.de.backend.console.*;
import uni.bombenstimmung.de.backend.graphics.GraphicsHandler;
import uni.bombenstimmung.de.backend.images.ImageHandler;
import uni.bombenstimmung.de.backend.images.ImageType;
import uni.bombenstimmung.de.backend.sounds.SoundHandler;
import uni.bombenstimmung.de.backend.sounds.SoundType;

public class Game {

    private static Field map[][] = new Field[GameData.MAP_DIMENSION][GameData.MAP_DIMENSION];	
    private static int mapNumber = 1;
    private static ArrayList<Bomb> placedBombs = new ArrayList<Bomb>();
    private static boolean gameOver = false;

    /**
     *  Füllt das Map Array mit leeren Feldern
     *  X baut auf von West nach Ost
     *  Y baut auf von Nord nach Süd
     */
    public static void fillMap() {
	for (int x = 0; x < GameData.MAP_DIMENSION; x++) {
	    for (int y = 0; y < GameData.MAP_DIMENSION; y++) {
		map[x][y] = new Field(x, y, FieldContent.EMPTY);
	    }
	}	
    }	

    /**
     * Malt die aktuelle Map
     * in der Mitte des Bildschirms
     */
    public static void drawGame(Graphics g) {
	//DRAW COUNTDOWN ETC
	
	//MAP
	int xOffset = GraphicsHandler.getWidth()-(GameData.FIELD_DIMENSION*GameData.MAP_DIMENSION);
	int yOffset = GameData.MAP_SIDE_BORDER;
	for(int x = GameData.MAP_DIMENSION-1 ; x >= 0 ; x--) {
	    for(int y = GameData.MAP_DIMENSION-1 ; y >= 0 ; y--) {
		map[x][y].drawField(g, (x*GameData.FIELD_DIMENSION)+(xOffset/2), (y*GameData.FIELD_DIMENSION)+(yOffset/2), map[x][y].getContent());
	    }
	}
    }			

    /**
     * Füllt das Map Array mit den Map-Spezifischen Feldern
     * @param MapNumber, die Nummer der ausgewählten Map
     */
    public static void updateMap(int MapNumber) {	
	//SYNTAX: 1,1,BL:1,2,BL: ...
	//RESETT MAP
	Game.fillMap();
	
	String mapData;
	switch(getMapNumber()) {
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
	//CHANGE NOT DEFAULT FIELDS
	String[] fieldData = mapData.split(":");
	for(String field : fieldData) {
		String[] data = field.split(",");
		int y = Integer.parseInt(data[0]);
		int x = Integer.parseInt(data[1]);
		FieldContent type = Field.getFieldTypeFromRepresentation(data[2]);
		map[x][y].setContent(type);
	}	
    }
	
    
    /**
     * Gibt das Field aus dem Array anhand der Bildkoordinaten zurück
     * @param x, zu überprüfende X-Koordinate
     * @param y, zu überprüfende Y-Koordinate
     * @return, gibt das Field Objekt zurück, das zu der Koordinate gehört
     */
    public static Field getFieldFromCoord(int x, int y) {
	Field field = null;
	int xOffset = GraphicsHandler.getWidth()-(GameData.FIELD_DIMENSION*GameData.MAP_DIMENSION);
	int yOffset = GameData.MAP_SIDE_BORDER;
	for (int j = GameData.MAP_DIMENSION-1 ; j >= 0 ; j--) {
	    for (int i = GameData.MAP_DIMENSION-1 ; i >= 0 ; i--) {
		if (x >= (i*GameData.FIELD_DIMENSION)+(xOffset/2) &&
			y >= (j*GameData.FIELD_DIMENSION)+(yOffset/2) &&
			x < (i*GameData.FIELD_DIMENSION)+(xOffset/2)+GameData.FIELD_DIMENSION &&
			y < (j*GameData.FIELD_DIMENSION)+(yOffset/2)+GameData.FIELD_DIMENSION) {
		    field = map[i][j];
		}
	    }
	}
	return field;
    }
    
    /**
     * Gibt das Field aus dem Array anhand der Mapkoordinaten zurück
     * @param x	X-Koordinate der Map
     * @param y	Y-Koordinate der Map
     * @return	gibt das Field Objekt zurück, das zu der Koordinate gehört
     */
    public static Field getFieldFromMap(int x, int y) {
	return map[x][y];
    }
    
    /**
     * Malt den Hintergrund im Ingame Mapspezifisch
     * @param bgnumber, 1 = Gras, 2 = Wüste
     */
    public static void drawBackground(Graphics g, int bgnumber) {
	Color yellowColor = new Color(217, 212, 163);
	Color greenColor = new Color(110, 150, 42);
	Color grayColor = new Color(143,90,90);
	switch(bgnumber) {
		case 1:
		    //FIX Bild für Gras ist anscheinend zu klein...
		    g.setColor(greenColor);
		    g.fillRect(0,0,GraphicsHandler.getWidth(), GraphicsHandler.getHeight());
		    break;
		case 2:
		    g.setColor(yellowColor);
		    g.fillRect(0,0,GraphicsHandler.getWidth(), GraphicsHandler.getHeight());
		    break;
		default:
		    g.setColor(grayColor);
		    g.fillRect(0,0,GraphicsHandler.getWidth(), GraphicsHandler.getHeight());
		    break;
	}
	
    }
    
    /**
     * Malt den Rechten Teil des Ingame Menüs
     * @param map, Map Nummer anhand derer Titel und Menü gestaltet werden
     */
    public static void drawRightPartOfMap(Graphics g, int map, int gameTime) {
	int xOffset = GraphicsHandler.getWidth()-(GameData.FIELD_DIMENSION*GameData.MAP_DIMENSION);
	int yStart = GameData.MAP_SIDE_BORDER;
	int xStart = (GameData.FIELD_DIMENSION*GameData.MAP_DIMENSION)+(xOffset/2);

	switch(map) {
		case 1:
		    GraphicsHandler.drawCentralisedText(g, Color.BLACK, 30, GameData.MAP_1_NAME, xStart+xOffset/4, yStart+50);
		    break;
		case 2:
		    GraphicsHandler.drawCentralisedText(g, Color.BLACK, 30, GameData.MAP_2_NAME, xStart+xOffset/4, yStart+50);
		    break;
	}
	GameCounter.drawCounter(g, xStart+xOffset/4, yStart);
    }
    
    /**
     * Malt den Linken Teil des Ingame Menüs
     * @param anzPlayer, die Anzahl der teilnehmenden Spieler zur Berechnung der Textaufteilung
     */
    public static void drawLeftPartOfMap(Graphics g, int anzPlayer) {
	int counter = 0;
	int gap = GraphicsHandler.getHeight()/(anzPlayer+(anzPlayer+1));
	int xOffset = GraphicsHandler.getWidth()-(GameData.FIELD_DIMENSION*GameData.MAP_DIMENSION);
	
	for(Player i : PlayerHandler.getAllPlayer()) {
	    GraphicsHandler.drawCentralisedText(g, Color.BLACK, 30, "Spielerin " + (i.getId()+1) + ": " + i.getName() , 0+(xOffset/4), 0+((counter+(counter+1))*gap));
	    if(i.getDead()) {
		g.drawImage(ImageHandler.getImage(ImageType.INGAME_SKIN_01_WASTED).getImage(), 0+(xOffset/8), 0+((counter+(counter+1))*gap+20), GameData.FIELD_DIMENSION*3, GameData.FIELD_DIMENSION*3, null);
	    } else {
		g.drawImage(ImageHandler.getImage(ImageType.INGAME_SKIN_01).getImage(), 0+(xOffset/8), 0+((counter+(counter+1))*gap+20), GameData.FIELD_DIMENSION*3, GameData.FIELD_DIMENSION*3, null);
	    }
	    counter++;
	}
    }
    
    public static void ringOfDeath(int ring) {
	//NORD NACH SUED VORNE
	for(int i = 0; i < GameData.MAP_DIMENSION; i++) {
	    changeFieldContent(FieldContent.BORDER, ring, i);
	    //Toete ggf Player
	    if (PlayerHandler.getClientPlayer().getCurrentField() ==
		Game.getFieldFromMap(ring, i)) {
		    	PlayerHandler.getClientPlayer().setDead(true);
	    }
	    for (Player j : PlayerHandler.getOpponentPlayers()) {
		if (j.getCurrentField() == Game.getFieldFromMap(ring, i)) {
		    j.setDead(true);
		}
	    }
	}
	//WEST NACH OST OBEN
	for(int i = 0; i < GameData.MAP_DIMENSION; i++) {
	    changeFieldContent(FieldContent.BORDER, i, ring);
	    if (PlayerHandler.getClientPlayer().getCurrentField() ==
		Game.getFieldFromMap(i, ring)) {
		  	PlayerHandler.getClientPlayer().setDead(true);
	    }
	    for (Player j : PlayerHandler.getOpponentPlayers()) {
		if (j.getCurrentField() == Game.getFieldFromMap(i, ring)) {
			    j.setDead(true);
		}
	    }
	}
	//NORD NACH SUED HINTEN
	for(int i = 0; i < GameData.MAP_DIMENSION; i++) {
	    changeFieldContent(FieldContent.BORDER, (GameData.MAP_DIMENSION - (ring+1)), i);
	    if (PlayerHandler.getClientPlayer().getCurrentField() ==
		Game.getFieldFromMap(GameData.MAP_DIMENSION - (ring+1), i)) {
		  	PlayerHandler.getClientPlayer().setDead(true);
	    }
	    for (Player j : PlayerHandler.getOpponentPlayers()) {
		if (j.getCurrentField() == Game.getFieldFromMap(GameData.MAP_DIMENSION - (ring+1), i)) {
			    j.setDead(true);
		}
	    }
	}
	//WEST NACH OST HINTEN
	for(int i = 0; i < GameData.MAP_DIMENSION; i++) {
	    changeFieldContent(FieldContent.BORDER, i, (GameData.MAP_DIMENSION - (ring+1)));
	    if (PlayerHandler.getClientPlayer().getCurrentField() ==
		Game.getFieldFromMap(i, GameData.MAP_DIMENSION - (ring+1))) {
		  	PlayerHandler.getClientPlayer().setDead(true);
	    }
	    for (Player j : PlayerHandler.getOpponentPlayers()) {
		if (j.getCurrentField() == Game.getFieldFromMap(i, GameData.MAP_DIMENSION - (ring+1) )) {
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
	for (Player i : PlayerHandler.getAllPlayer()) {
	    if (!i.getDead()) {
		livingPlayers++;
	    }
	}
	ConsoleHandler.print("Living Players: " + livingPlayers, MessageType.GAME);
	if (gameOver == false && livingPlayers <= 1) {
	    gameOver();
	}
    }
    
    public static void gameOver() {
	switch (mapNumber) {
	case 1:
	    SoundHandler.reducePlayingSound(SoundType.MAP1, 2, false);
	    break;
	    
	case 2:
	    SoundHandler.reducePlayingSound(SoundType.MAP2, 2, false);
	    break;
	    
	case 3:
	    SoundHandler.reducePlayingSound(SoundType.MAP3, 2, false);
	    break;
	    
	default:
	    ConsoleHandler.print("No music track available for this map!", MessageType.GAME);
	}

	new Animation(400, 1) {
	    @Override
	    public void initValues() {
		PlayerHandler.getClientPlayer().actionStop();
		for (Player i : PlayerHandler.getAllPlayer()) {
		    i.stopTimer();
		}
		for (Bomb i : placedBombs) {
		    i.stopTimer();
		}
		gameOver = true;
	    }
	    
	    @Override
	    public void finaliseValues() {
		GraphicsHandler.switchToAftergameFromIngame();
	    }
	};
    }
    
    public static void resetIngame() {
	placedBombs.clear();
	gameOver = false;
	PlayerHandler.resetPlayerHandler();
	GameCounter.resetGameCounter();
    }
}