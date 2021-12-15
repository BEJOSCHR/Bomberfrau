package uni.bombenstimmung.de.game;

import java.awt.Graphics;

import uni.bombenstimmung.de.backend.console.*;
import uni.bombenstimmung.de.backend.graphics.*;
import uni.bombenstimmung.de.game.FieldContent;

public class Game {

	
private Field map[][] = new Field[GameData.MAP_DIMENSION][GameData.MAP_DIMENSION];	
private int mapNumber = 1;
	
	
	
	
	
public void drawGame(Graphics g) {
	
	//DRAW COUNTDOWN ETC
	
	//MAP
	for(int y = GameData.MAP_DIMENSION-1 ; y >= 0 ; y--) {
		for(int x = GameData.MAP_DIMENSION-1 ; x >= 0 ; x--) {
			this.map[x][y].draw(g);
		}
	}
}	
	
	
	
	
	
	
	
private void resettMap() {
		
		//DEFAULT
		for(int x = 0 ; x < GameData.MAP_DIMENSION ; x++) {
			for(int y = 0 ; y < GameData.MAP_DIMENSION ; y++) {
				this.map[x][y].changeType(FieldContent.EMPTY);
			}}
		}	
	
	
	
	
public void updateMap(int MapNumber) {
		
		//SYNTAX: 1,1,BL:1,2,BL: ...
		
		//RESETT MAP
		resettMap();
		
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
			FieldContent type = FieldContent.getFieldTypeFromRepresentation(data[2]);
			this.map[x][y].changeType(type);
		}
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
public Field[][] getMap() {
	return map;
}
	
	
	
	
	
}
