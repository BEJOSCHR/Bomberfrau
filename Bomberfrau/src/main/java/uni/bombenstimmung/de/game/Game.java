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
import java.awt.Color;

import uni.bombenstimmung.de.backend.console.*;
import uni.bombenstimmung.de.backend.graphics.GraphicsHandler;

public class Game {

	
    private static Field map[][] = new Field[GameData.MAP_DIMENSION][GameData.MAP_DIMENSION];	
    private static int mapNumber = 2;
	
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
    
    public static void drawRightPartOfMap(Graphics g, int map) {
	int xOffset = GraphicsHandler.getWidth()-(GameData.FIELD_DIMENSION*GameData.MAP_DIMENSION);
	int yStart = GameData.MAP_SIDE_BORDER;
	int xStart = (GameData.FIELD_DIMENSION*GameData.MAP_DIMENSION)+(xOffset/2);

	switch(map) {
		case 1:
		    GraphicsHandler.drawCentralisedText(g, Color.BLACK, 40, "Rumble in the Jungle", xStart+xOffset/4, yStart+50);
		    break;
		case 2:
		    GraphicsHandler.drawCentralisedText(g, Color.BLACK, 40, "Opertaion Desert", xStart+xOffset/4, yStart+50);
		    break;
	}
    }
    
    public static void drawLeftPartOfMap(Graphics g, int anzPlayer) {
	int gap = GraphicsHandler.getHeight()/(anzPlayer+(anzPlayer+1));
	int xOffset = GraphicsHandler.getWidth()-(GameData.FIELD_DIMENSION*GameData.MAP_DIMENSION);
	
	for(int i = 0; i < anzPlayer; i++) {
	    GraphicsHandler.drawCentralisedText(g, Color.BLACK, 30, "Spielerin " + (i+1) + " : Mia Julia", 0+(xOffset/4), 0+((i+(i+1))*gap));
	}
    }
}