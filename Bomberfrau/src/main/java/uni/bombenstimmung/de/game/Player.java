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
import java.awt.Point;

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
    private int placedBombs;
    private int bombRadius;
    private PlayerButtonConfig currentButtonConfig;
    private boolean dead;
    private Field currentField;
    private Timer t;
    private int velX;
    private int velY;
    
    public Player(int id, String name, String ipAdress, boolean host, int skin, Point pos) {
	int xOffset = GraphicsHandler.getWidth()-(GameData.FIELD_DIMENSION*GameData.MAP_DIMENSION);
	int yOffset = GameData.MAP_SIDE_BORDER;
	this.id = id;
	this.name = name;
	this.ipAdress = ipAdress;
	this.host = host;
	this.skin = skin;
	this.movementSpeed = 1;
	this.maxBombs = 1;
	this.placedBombs = 0;
	this.bombRadius = 1;
	super.xPosition = (int)((pos.getX()*GameData.FIELD_DIMENSION)+(xOffset/2)+(GameData.FIELD_DIMENSION/2));
	super.yPosition = (int)((pos.getY()*GameData.FIELD_DIMENSION)+(yOffset/2)+(GameData.FIELD_DIMENSION/2));
	this.velX = 0;
	this.velY = 0;
	this.currentButtonConfig = new PlayerButtonConfig();
	this.dead = false;
	this.currentField = Game.getFieldFromCoord(xPosition, yPosition);
	this.t = new Timer(8, this);
	this.t.start();
	ConsoleHandler.print("Created Player. ID: " + id + ", Name: " + name + ", Pos(" + currentField.xPosition +
				", " + currentField.yPosition +")", MessageType.GAME);
    }
    
    /* Zustaendig fuer das kontinuierliche Aktualisieren der Player-Position inklusive Kollisionsabfrage. */
    public void actionPerformed(ActionEvent e) {
	Field tempField = this.currentField;
	this.currentField = Game.getFieldFromCoord((xPosition + (velX*8)), (yPosition + (velY*8)));
	if (this.currentField.getContent() == FieldContent.EMPTY || this.currentField.getContent() == FieldContent.BOMB) {
	    super.xPosition += this.velX;
	    super.yPosition += this.velY;
	} else {
	    this.currentField = tempField;
	}
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
	/* Bewegung zuruecksetzen im Todesfall. */
	if (dead == true) {
	    this.actionStop();
	    PlayerHandler.resetMovement();
	}
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
    
    /*
     * Es folgen Methoden zu Bewegungsaktionen. Hier werden jeweils die Velocities passend zur Aktion
     * angepasst. Der Velocity-Wert wird in actionPerformed kontinuierlich addiert.
     * =====================================================================================
     */
    
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
    
    /* ========= Ende des Blocks fuer Bewegungsmethoden. =========== */
    
    public void actionSetBomb() {
	Field temp = Game.getFieldFromCoord(xPosition, yPosition);
	if (placedBombs < maxBombs && temp.getContent() == FieldContent.EMPTY) {
	    Game.changeFieldContent(FieldContent.BOMB, temp.xPosition, temp.yPosition);
	    Game.addBomb(this.bombRadius, 3, this.id);
	    placedBombs++;
	    ConsoleHandler.print("Player ID: " + this.id + " placed Bomb at Pos(" + temp.xPosition + ", "
		    			+ temp.yPosition + ")", MessageType.GAME);
	}
    }
    
    public void increaseMaxBombs() {
	this.maxBombs++;
	ConsoleHandler.print("Player ID: " + id + ": New MaxBombs: " + this.maxBombs, MessageType.GAME);
    }
    
    public void decreaseMaxBombs() {
	if (this.maxBombs > 1) {
	    this.maxBombs--;
	}
	ConsoleHandler.print("Player ID: " + id + ": New MaxBombs: " + this.maxBombs, MessageType.GAME);
    }
    
    public void decreasePlacedBombs() {
	if (placedBombs > 0) {
	    placedBombs--;
	}
    }
}
