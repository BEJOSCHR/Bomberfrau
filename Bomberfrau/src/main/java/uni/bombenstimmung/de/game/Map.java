/*
 * inGame
 *
 * Version 1.0
 * Author: Mustafa
 *
 * Map Basis aufgelegt
 */



package uni.bombenstimmung.de.game;


public class Map {

	int id;
	
	int mapHeight;
	int mapWidth;
	int playerCount;
	
	Timer timer; 
	
	public void setId(int newId) {
		this.id = newId;
	}
	
	public void setMapHeight(int newMapHeight) {
		this.mapHeight = newMapHeight;
		
	}
	public void setMapWidth(int newMapWidth) {
		this.mapHeight = newMapWidth;
		
	}
	public void setPlayerCount(int newPlayerCount) {
		this.playerCount = newPlayerCount;
		
	}
	
	public int getId() {
		return id;
	}
	public int getMapHeight() {
		return mapHeight;
	}
	public int getMapWidth() {
		return mapWidth;
	}
	public int getPlayerCount() {
		return playerCount;
	}
	
	
	
	
}

