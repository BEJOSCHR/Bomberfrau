/*
 * Player
 *
 * Version 0.4.1
 * 
 * Author: Christopher
 * 
 * Datum: 12.12.2021
 *
 * Enthält Informationen zum eigenen Player und initiiert Aktionen
 * bei Tastendruck.
 */

package uni.bombenstimmung.de.game;

import uni.bombenstimmung.de.backend.console.ConsoleHandler;
import uni.bombenstimmung.de.backend.console.MessageType;

public class Player {

    private int id;
    private int skin;
    private int movementSpeed;
    private int maxBombs;
    private String name;
//    private Entity position;
    private PlayerButtonConfig currentButtonConfig;
    private boolean dead;
//    private Field currentField;
    
    public Player(String name) {
	this.id = 0;
	this.skin = 0;
	this.movementSpeed = 1;
	this.maxBombs = 1;
	this.name = name;
	currentButtonConfig = new PlayerButtonConfig();
	this.dead = false;
	ConsoleHandler.print("Created Player. ID: " + id + ", Name: " + name, MessageType.GAME);
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
    
    public void setDead(boolean dead) {
	this.dead = dead;
    }
    
    public int getId() {
	return id;
    }
    
    public int getSkin() {
	return skin;
    }
    
    public int getMovementSpeed() {
	return movementSpeed;
    }
    
    public int getMaxBombs() {
	return maxBombs;
    }
    
    public String getName() {
	return name;
    }
    
//    public Entity getPosition() {
//	return position;
//    }
    
    public PlayerButtonConfig getCurrentButtonConfig() {
	return currentButtonConfig;
    }
    
    public boolean getDead() {
	return dead;
    }
    
    public void actionPlayer(int buttonPrompt) {
	
    }
}
