/*
 * Player
 *
 * Version 0.1
 * 
 * Author: Christopher
 * 
 * Datum: 02.12.2021
 *
 * Enthält Informationen zum eigenen Player und initiiert Aktionen
 * bei Tastendruck.
 */

package uni.bombenstimmung.de.game;

public class Player {

    int id, skin, movementSpeed, maxBombs;
    String name;
    Entity position;
    PlayerMoveButtonConfig currentButtonConfig;
    boolean dead;
    Field currentField;
    
    public Player(String name) {
	this.name = name;
    }
    
    public void setId(int id) {
	this.id = id;
    }
    
    public void setMovementSpeed(int movementSpeed) {
	this.movementSpeed = movementSpeed;
    }
    
    public void setMaxBombs(int maxBombs) {
	this.maxBombs = maxBombs;
    }
    
    public void setSkin(int skin) {
	this.skin = skin;
    }
    
    public void setDeath(boolean dead) {
	this.dead = dead;
    }
    
    public int getId() {
	return id;
    }
    
    public Entity getPosition() {
	return position;
    }
    
    public int getSkin() {
	return skin;
    }
    
    public void actionPlayer(int buttonPrompt) {
	
    }
}
