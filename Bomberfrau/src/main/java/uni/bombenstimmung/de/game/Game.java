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

import uni.bombenstimmung.de.backend.console.*;
import uni.bombenstimmung.de.backend.graphics.GraphicsHandler;
import uni.bombenstimmung.de.backend.images.ImageHandler;
import uni.bombenstimmung.de.backend.images.ImageType;

public class Game {

    private static Field map[][] = new Field[GameData.MAP_DIMENSION][GameData.MAP_DIMENSION];	
    private static int mapNumber = 2;
    private static ArrayList<Bomb> placedBombs = new ArrayList<Bomb>();

    /**
     *  Füllt das Map Array mit leeren Feldern
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
		    g.setColor(greenColor);
		    g.fillRect(0,0,GraphicsHandler.getWidth(), GraphicsHandler.getHeight());
		    break;
	}
	
    }
    
    /**
     * Malt den Rechten Teil des Ingame Menüs
     * @param map, Map Nummer anhand derer Titel und Menü gestaltet werden
     */
    public static void drawRightPartOfMap(Graphics g, int map) {
	int xOffset = GraphicsHandler.getWidth()-(GameData.FIELD_DIMENSION*GameData.MAP_DIMENSION);
	int yStart = GameData.MAP_SIDE_BORDER;
	int xStart = (GameData.FIELD_DIMENSION*GameData.MAP_DIMENSION)+(xOffset/2);

	switch(map) {
		case 1:
		    GraphicsHandler.drawCentralisedText(g, Color.BLACK, 40, GameData.MAP_1_NAME, xStart+xOffset/4, yStart+50);
		    break;
		case 2:
		    GraphicsHandler.drawCentralisedText(g, Color.BLACK, 40, GameData.MAP_2_NAME, xStart+xOffset/4, yStart+50);
		    break;
	}
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
	    GraphicsHandler.drawCentralisedText(g, Color.BLACK, 30, "Spielerin " + (i.getId()+1) + " :" + i.getName() , 0+(xOffset/4), 0+((counter+(counter+1))*gap));
	    if(i.getDead()) {
		g.drawImage(ImageHandler.getImage(ImageType.INGAME_SKIN_01_WASTED).getImage(), 0+(xOffset/8), 0+((counter+(counter+1))*gap+20), null);
	    } else {
		g.drawImage(ImageHandler.getImage(ImageType.INGAME_SKIN_01).getImage(), 0+(xOffset/8), 0+((counter+(counter+1))*gap+20), null);
	    }
	    counter++;
	}
    }

    public Field[][] getMap() {
	return map;
    }	
    
    public static int getMapNumber() {
	return mapNumber;
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
    
    public static ArrayList<Bomb> getPLacedBombs() {
	return placedBombs;
    }
}