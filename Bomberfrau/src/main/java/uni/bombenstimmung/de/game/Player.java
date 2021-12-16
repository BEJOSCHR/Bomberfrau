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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import uni.bombenstimmung.de.backend.console.ConsoleHandler;
import uni.bombenstimmung.de.backend.console.MessageType;
import uni.bombenstimmung.de.backend.graphics.GraphicsHandler;

public class Player extends Entity implements ActionListener{

    private int id;
    private String name;
    private String ipAdress;
    private boolean host;
    private int skin;
    private int movementSpeed;
    private int maxBombs;
    private PlayerButtonConfig currentButtonConfig;
    private boolean dead;
    private Field currentField;
    private Timer t;
    private int velX;
    private int velY;
    
    public Player(String name) {
	this.id = 0;
	this.name = name;
	this.ipAdress = "localhost";
	this.host = true;
	this.skin = 0;
	this.movementSpeed = 1;
	this.maxBombs = 1;
	this.xPosition = GraphicsHandler.getWidth()/2;
	this.yPosition = GraphicsHandler.getHeight()/2;
	this.velX = 0;
	this.velY = 0;
	this.currentButtonConfig = new PlayerButtonConfig();
	this.dead = false;
	this.currentField = null;
	this.t = new Timer(8, this);
	this.t.start();
	ConsoleHandler.print("Created Player. ID: " + id + ", Name: " + name, MessageType.GAME);
    }
    
    public Player(int id, String name, String ipAdress, boolean host, int skin) {
	this.id = id;
	this.name = name;
	this.ipAdress = ipAdress;
	this.host = host;
	this.skin = skin;
	this.movementSpeed = 1;
	this.maxBombs = 1;
	this.xPosition = GraphicsHandler.getWidth()/2;
	this.yPosition = GraphicsHandler.getHeight()/2;
	this.velX = 0;
	this.velY = 0;
	this.currentButtonConfig = new PlayerButtonConfig();
	this.dead = false;
	this.currentField = null;
	this.t = new Timer(8, this);
	this.t.start();
	ConsoleHandler.print("Created Player. ID: " + id + ", Name: " + name, MessageType.GAME);
    }
    
    public void actionPerformed(ActionEvent e) {
	this.xPosition += this.velX;
	this.yPosition += this.velY;
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
    
    public String getIpAdress() {
	return ipAdress;
    }
    
    public boolean getHost() {
	return host;
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
    
    public PlayerButtonConfig getCurrentButtonConfig() {
	return currentButtonConfig;
    }
    
    public boolean getDead() {
	return dead;
    }
    
    public Field getCurrentField() {
	return currentField;
    }
    
    public void actionUp() {
	this.velY = -(int)((double)GraphicsHandler.getHeight()/500.0);
	this.velX = 0;
    }
    
    public void actionDown() {
	this.velY = (int)((double)GraphicsHandler.getHeight()/500.0);
	this.velX = 0;
    }
    
    public void actionLeft() {
	this.velX = -(int)((double)GraphicsHandler.getWidth()/890.0);
	this.velY = 0;
    }
    
    public void actionRight() {
	this.velX = (int)((double)GraphicsHandler.getWidth()/890.0);
	this.velY = 0;
    }
    
    public void actionStop() {
	this.velX = 0;
	this.velY = 0;
    }
}
