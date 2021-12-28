/*
 * Player
 *
 * Version 0.4.1
 * 
 * Author: Christopher
 * 
 * Datum: 28.12.2021
 *
 * Diese Klasse enthaelt Funktionen und Variablen, die mit einem Player direkt zusammenhaengen.
 * Darunter fallen Informationen ueber den aktuellen Zustand des Players, sowie Aktionen,
 * welche der Player bei Aufrufen direkt ausfuehrt.
 */

// TODO: Laufgeschwindigkeit implementieren

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
	/* Offset-Variablen fuer Berechnung*/
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
	this.bombRadius = 5;
	
	/* Variablen der Oberklasse Entity. */
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
    
    // TODO: Work in Progress: Bugfixing in Kollisionsabfrage
    
    public void actionPerformed(ActionEvent e) {
	//boolean block = false;
	Field tempField;
	/*if (this.velX == 0) {
	    tempField = Game.getFieldFromCoord((xPosition + (velY*8)), (yPosition + (velY*8)));
	    if (tempField.getContent() != FieldContent.EMPTY && tempField.getContent() != FieldContent.BOMB
		&& tempField.getContent() != FieldContent.UPGRADE) {
		block = true;
	    }
	    tempField = Game.getFieldFromCoord((xPosition - (velY*8)), (yPosition + (velY*8)));
	    if (tempField.getContent() != FieldContent.EMPTY && tempField.getContent() != FieldContent.BOMB
		&& tempField.getContent() != FieldContent.UPGRADE) {
		block = true;
	    }
	} else if (this.velY == 0) {
	    tempField = Game.getFieldFromCoord((xPosition + (velX*8)), (yPosition + (velX*8)));
	    if (tempField.getContent() != FieldContent.EMPTY && tempField.getContent() != FieldContent.BOMB
		&& tempField.getContent() != FieldContent.UPGRADE) {
		block = true;
	    }
	    tempField = Game.getFieldFromCoord((xPosition + (velX*8)), (yPosition - (velX*8)));
	    if (tempField.getContent() != FieldContent.EMPTY && tempField.getContent() != FieldContent.BOMB
		&& tempField.getContent() != FieldContent.UPGRADE) {
		block = true;
	    }
	}*/
	
	/* 
	 * tempField stellt die Hitbox dar. Es wird das vorliegende Field in Bewegungsrichtung bestimmt
	 * und fuer die Abfrage, ob das Field betretbar ist, weiterverwendet.
	 */
	tempField = Game.getFieldFromCoord((xPosition + (velX*8)), (yPosition + (velY*8)));
	if (/*block == false && (*/tempField.getContent() != FieldContent.WALL && tempField.getContent() != FieldContent.BLOCK
		&& tempField.getContent() != FieldContent.BORDER/*)*/) {
	    super.xPosition += this.velX;
	    super.yPosition += this.velY;
	    this.currentField = Game.getFieldFromCoord(xPosition, yPosition);
	}
	/* Abfrage, ob sich Player in Explosion befindet. Falls ja, dann tot. */
	if (this.currentField.getContent() == FieldContent.EXPLOSION1 || this.currentField.getContent() == FieldContent.EXPLOSION2 ||
	    this.currentField.getContent() == FieldContent.EXPLOSION2_NS || this.currentField.getContent() == FieldContent.EXPLOSION3_N ||
	    this.currentField.getContent() == FieldContent.EXPLOSION3_S || this.currentField.getContent() == FieldContent.EXPLOSION3_W ||
	    this.currentField.getContent() == FieldContent.EXPLOSION3_O) {
	    this.setDead(true);
	}
	/* Abfrage, ob Player ueber Upgrade laeuft. Falls ja, aufsammeln und passende Upgrade-Methode ausfuehren.*/
	if (this.currentField.getContent() == FieldContent.UPGRADE_ITEM_BOMB) {
	    this.increaseMaxBombs();
	    Game.changeFieldContent(FieldContent.EMPTY, this.currentField.xPosition, this.currentField.yPosition);
	}
	if (this.currentField.getContent() == FieldContent.UPGRADE_ITEM_FIRE) {
	    this.increaseBombRadius();
	    Game.changeFieldContent(FieldContent.EMPTY, this.currentField.xPosition, this.currentField.yPosition);
	}
	if (this.currentField.getContent() == FieldContent.UPGRADE_ITEM_SHOE) {
	    this.increaseMovementSpeed();
	    Game.changeFieldContent(FieldContent.EMPTY, this.currentField.xPosition, this.currentField.yPosition);
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
	this.velX = 0;
	this.velY = -(int)((double)GraphicsHandler.getHeight()/500.0);
    }
    
    public void actionDown() {
	this.velX = 0;
	this.velY = (int)((double)GraphicsHandler.getHeight()/500.0);
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
    
    /**
     * Platzieren der Bombe. Fragt ab, ob die aktuelle Position EMPTY ist.
     * Falls ja, dann aendert sich der FieldContent zu BOMB und ein Bomb-Objekt
     * wird in die static ArrayList 'placedBombs' hinzugefuegt.
     */
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
    
    public void increaseBombRadius() {
	if (this.bombRadius < GameData.MAP_DIMENSION) {
	    this.bombRadius++;
	}
	ConsoleHandler.print("Player ID: " + id + ": New Bomb Radius: " + this.bombRadius, MessageType.GAME);
    }
    
    public void increaseMovementSpeed() {
	if (this.movementSpeed < 5) {
	    this.movementSpeed++;
	}
	ConsoleHandler.print("Player ID: " + id + ": New Movement Speed: " + this.movementSpeed, MessageType.GAME);
    }
}
