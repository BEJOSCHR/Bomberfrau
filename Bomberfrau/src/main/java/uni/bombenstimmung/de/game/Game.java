/*
 * Game
 *
 * Version 1.0
 * Author: Dennis und Mustafa
 *
 * Malt und verwaltet die Map
 */

package uni.bombenstimmung.de.game;

import java.awt.Graphics;
import java.util.ArrayList;

import uni.bombenstimmung.de.backend.console.*;
import uni.bombenstimmung.de.backend.graphics.GraphicsHandler;

public class Game {

	
    private static Field map[][] = new Field[GameData.MAP_DIMENSION][GameData.MAP_DIMENSION];	
    private static int mapNumber = 2;
    private static ArrayList<Bomb> placedBombs = new ArrayList<Bomb>();
	
    public static void fillMap() {
	for (int x = 0; x < GameData.MAP_DIMENSION; x++) {
	    for (int y = 0; y < GameData.MAP_DIMENSION; y++) {
		map[x][y] = new Field(x, y, FieldContent.EMPTY);
	    }
	}	
    }	
	
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
	
//    private static void resettMap() {
//	//DEFAULT
//	for(int x = 0 ; x < GameData.MAP_DIMENSION ; x++) {
//	    for(int y = 0 ; y < GameData.MAP_DIMENSION ; y++) {
//		map[x][y].setContent(FieldContent.EMPTY);
//	    }
//	}
//    }		
	
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
	
    public Field[][] getMap() {
	return map;
    }	
    
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
}