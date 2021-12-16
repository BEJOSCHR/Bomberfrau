package uni.bombenstimmung.de.game;

import java.awt.Graphics;

import uni.bombenstimmung.de.backend.console.*;
import uni.bombenstimmung.de.backend.graphics.*;
import uni.bombenstimmung.de.game.FieldContent;

public class Game {

	
private static Field map[][] = new Field[GameData.MAP_DIMENSION][GameData.MAP_DIMENSION];	
private static int mapNumber = 1;
	
public static void fillMap() {
    for (int x = 0; x < GameData.MAP_DIMENSION; x++) {
	for (int y = 0; y < GameData.MAP_DIMENSION; y++) {
	    map[x][y] = new Field(x,y,FieldContent.EMPTY);
	}
    }	
}
	
	
	
public static void drawGame(Graphics g) {
	
	//DRAW COUNTDOWN ETC
	
	//MAP
	for(int y = GameData.MAP_DIMENSION-1 ; y >= 0 ; y--) {
		for(int x = GameData.MAP_DIMENSION-1 ; x >= 0 ; x--) {
		    	//Wir brauchen hier noch ne Methode zum String auslesen für die Map
			map[x][y].drawField(g,x*GameData.FIELD_DIMENSION,y*GameData.FIELD_DIMENSION,FieldContent.EMPTY);
		}
	}
}	
	
	
	
	
	
	
	
private static void resettMap() {
		
		//DEFAULT
		for(int x = 0 ; x < GameData.MAP_DIMENSION ; x++) {
			for(int y = 0 ; y < GameData.MAP_DIMENSION ; y++) {
				map[x][y].setContent(FieldContent.EMPTY);
			}}
		}	
	
	
	
	
public static void updateMap(int MapNumber) {
		
		//SYNTAX: 1,1,BL:1,2,BL: ...
		
		//RESETT MAP
		Game.resettMap();
		
		String mapData;
		switch(mapNumber) {
		case 1:
			mapData = GameData.MAP_1;
			break;
		default:
			ConsoleHandler.print("Unknown mapnumber, cant load map!");
			return;
		}
		
		//CHANGE NOT DEFAULT FIELDS
		String[] fieldData = mapData.split(":");
		for(String field : fieldData) {
			String[] data = field.split(",");
			int x = Integer.parseInt(data[0]);
			int y = Integer.parseInt(data[1]);
			FieldContent type = Field.getFieldTypeFromRepresentation(data[2]);
			map[x][y].setContent(type);
		}
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
public Field[][] getMap() {
	return map;
}
	
	
	
	
	
}
