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

package uni.bombenstimmung.de.game;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Point;

import javax.swing.Timer;

import uni.bombenstimmung.de.backend.console.ConsoleHandler;
import uni.bombenstimmung.de.backend.console.MessageType;
import uni.bombenstimmung.de.backend.graphics.GraphicsHandler;
import uni.bombenstimmung.de.backend.serverconnection.host.ConnectedClient;
import uni.bombenstimmung.de.backend.sounds.SoundHandler;
import uni.bombenstimmung.de.backend.sounds.SoundType;
import uni.bombenstimmung.de.menu.Settings;

public class Player extends Entity implements ActionListener {

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
    /* Genauere Positionsdaten und Velocity-Daten des Players. */
    private double realPosX;
    private double realPosY;
    private double velX;
    private double velY;
    private double xHitbox;
    private double yHitbox;
    private int direction;
    private double speedFactor;
    private boolean playWallSound = true;
    private ConnectedClient connectedClient;
    private int deathTime;

    public Player(int id, String name, String ipAdress, boolean host, int skin, Point pos, ConnectedClient cC) {
	/* Offset-Variablen fuer Berechnung */
	int xOffset = GraphicsHandler.getWidth() - (GameData.FIELD_DIMENSION * GameData.MAP_DIMENSION);
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
	this.realPosX = (pos.getX() * GameData.FIELD_DIMENSION) + (xOffset / 2) + (GameData.FIELD_DIMENSION / 2);
	this.realPosY = (pos.getY() * GameData.FIELD_DIMENSION) + (yOffset / 2) + (GameData.FIELD_DIMENSION / 2);

	// Variablen der Oberklasse Entity. Werden als Bildschirmkoordinaten behandelt.
	super.xPosition = (int) (this.realPosX + 0.5);
	super.yPosition = (int) (this.realPosY + 0.5);

	this.velX = 0.0;
	this.velY = 0.0;
	this.currentButtonConfig = new PlayerButtonConfig(Settings.getMoveUp(), Settings.getMoveDown(),
		Settings.getMoveLeft(), Settings.getMoveRight(), Settings.getPlantBomb());
	this.dead = false;
	this.currentField = Game.getFieldFromCoord(xPosition, yPosition);
	this.xHitbox = (double) GraphicsHandler.getHeight() / 66.0;
	this.yHitbox = (double) GraphicsHandler.getHeight() / 36.0;
	this.direction = 0;
	this.speedFactor = 360.0;
	this.connectedClient = cC;
	this.deathTime = 0;
	this.t = new Timer(8, this);
	this.t.start();
	ConsoleHandler.print("Created Player. ID: " + id + ", Name: " + name + ", Pos(" + currentField.xPosition + ", "
		+ currentField.yPosition + ")", MessageType.GAME);
    }

    /*
     * Zustaendig fuer das kontinuierliche Aktualisieren der Player-Position
     * inklusive Kollisionsabfrage.
     */

    public void actionPerformed(ActionEvent e) {
	if (this == PlayerHandler.getClientPlayer()) {
	    boolean block = false;
	    Field tempField;
	    /*
	     * tempField stellt die Hitbox dar. Es wird das vorliegende Field in
	     * Bewegungsrichtung bestimmt und fuer die Abfrage, ob das Field betretbar ist,
	     * weiterverwendet.
	     */
	    switch (this.direction) {
	    case 0: // ohne Bewegung
		playWallSound = true;
		tempField = Game.getFieldFromCoord(super.xPosition, super.yPosition);
		break;
	    case 1: // hoch
		block = this.isPlayerHittingCorner();
		tempField = Game.getFieldFromCoord(super.xPosition,
			(int) (((double) super.yPosition - this.yHitbox) + 0.5));
		break;
	    case 2: // runter
		block = this.isPlayerHittingCorner();
		tempField = Game.getFieldFromCoord(super.xPosition,
			(int) (((double) super.yPosition + this.yHitbox) + 0.5));
		break;
	    case 3: // links
		block = this.isPlayerHittingCorner();
		tempField = Game.getFieldFromCoord((int) (((double) super.xPosition - this.xHitbox) + 0.5),
			super.yPosition);
		break;
	    case 4: // rechts
		block = this.isPlayerHittingCorner();
		tempField = Game.getFieldFromCoord((int) (((double) super.xPosition + this.xHitbox) + 0.5),
			super.yPosition);
		break;
	    default:
		tempField = null;
		ConsoleHandler.print("Invalid Direction ID!", MessageType.GAME);
	    }
	    if (block == false && (tempField.getContent() != FieldContent.WALL
		    && tempField.getContent() != FieldContent.BLOCK && tempField.getContent() != FieldContent.BORDER
		    && (tempField.getContent() != FieldContent.BOMB || (tempField.getContent() == FieldContent.BOMB
			    && this.currentField.getContent() == FieldContent.BOMB
			    && this.currentField == tempField)))) {
		this.realPosX += this.velX;
		this.realPosY += this.velY;
	    } else {
		// Abfrage, damit Tod in RoD nicht Dauerton ergibt
		if (playWallSound && !this.dead && !Game.getGameOver() && this == PlayerHandler.getClientPlayer())
		    SoundHandler.playSound(SoundType.WALL, false);
		playWallSound = false;
	    }
	}

	super.xPosition = (int) (this.realPosX + 0.5);
	super.yPosition = (int) (this.realPosY + 0.5);
	this.currentField = Game.getFieldFromCoord(xPosition, yPosition);

	// ConnectedClient-Nachricht fuer Aktualisierung der Player-Position bei
	// Bewegung
	if (this.direction != 0 && this == PlayerHandler.getClientPlayer()) {
	    if (this.connectedClient.isHost()) {
		this.connectedClient.sendMessageToAllClients("202-" + this.id + "-" + super.xPosition + "-"
			+ super.yPosition + "-" + GraphicsHandler.getHeight() + "-" + this.direction);
	    } else {
		this.connectedClient.sendMessage(this.connectedClient.getSession(),
			"203-" + this.id + "-" + super.xPosition + "-" + super.yPosition + "-"
				+ GraphicsHandler.getHeight() + "-" + this.direction);
	    }
	}

	/* Abfrage, ob sich Player in Explosion befindet. Falls ja, dann tot. */
	if (this == PlayerHandler.getClientPlayer() && (this.currentField.getContent() == FieldContent.EXPLOSION1
		|| this.currentField.getContent() == FieldContent.EXPLOSION2
		|| this.currentField.getContent() == FieldContent.EXPLOSION2_NS
		|| this.currentField.getContent() == FieldContent.EXPLOSION3_N
		|| this.currentField.getContent() == FieldContent.EXPLOSION3_S
		|| this.currentField.getContent() == FieldContent.EXPLOSION3_W
		|| this.currentField.getContent() == FieldContent.EXPLOSION3_O)) {
	    this.setDead(true);
	}
	/*
	 * Abfrage, ob Player ueber Upgrade laeuft. Falls ja, aufsammeln und passende
	 * Upgrade-Methode ausfuehren.
	 */
	if (this.currentField.getContent() == FieldContent.UPGRADE_ITEM_BOMB) {
	    this.increaseMaxBombs();
	    SoundHandler.playSound(SoundType.ITEM, false);

	    Game.changeFieldContent(FieldContent.EMPTY, this.currentField.xPosition, this.currentField.yPosition);
	}
	if (this.currentField.getContent() == FieldContent.UPGRADE_ITEM_FIRE) {
	    this.increaseBombRadius();
	    SoundHandler.playSound(SoundType.ITEM, false);
	    Game.changeFieldContent(FieldContent.EMPTY, this.currentField.xPosition, this.currentField.yPosition);
	}
	if (this.currentField.getContent() == FieldContent.UPGRADE_ITEM_SHOE) {
	    this.increaseMovementSpeed();
	    SoundHandler.playSound(SoundType.ITEM, false);
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
	    if (this == PlayerHandler.getClientPlayer()) {
		PlayerHandler.resetMovement();
	    }
	}
	if (this.dead == false && dead == true) {
	    this.dead = dead;
	    SoundHandler.playSound(SoundType.DYING, false);
	    this.deathTime = GameCounter.getClock();
	    ConsoleHandler.print("RIP Player " + this.id + ". She died at " + this.deathTime + " seconds. T.T",
		    MessageType.GAME);
	    this.t.stop();
	    // ConnectedClient-Nachricht fuer Player-Tod
	    if (this.connectedClient.isHost() && this == PlayerHandler.getClientPlayer()) {
		this.connectedClient.sendMessageToAllClients("206-" + this.id);
		this.connectedClient.sendMessageToAllClients("206-" + this.id);
		this.connectedClient.sendMessageToAllClients("206-" + this.id);
	    } else if (!this.connectedClient.isHost() && this == PlayerHandler.getClientPlayer()) {
		this.connectedClient.sendMessage(this.connectedClient.getSession(), "207-" + this.id);
		this.connectedClient.sendMessage(this.connectedClient.getSession(), "207-" + this.id);
		this.connectedClient.sendMessage(this.connectedClient.getSession(), "207-" + this.id);
	    }
	} else if (this.dead == true && dead == false) {
	    this.t.start();
	}
	this.dead = dead;
	if (this.connectedClient.isHost()) {
	    Game.checkIfAllDead();
	}
    }

    public void setConnectedClient(ConnectedClient cC) {
	this.connectedClient = cC;
    }

    /**
     * Setzt die Bildschirmkoordinate des Players neu. Diese Methode ist
     * hauptsaechlich fuer ConnectedClient gedacht.
     * 
     * @param xPos neue X Bildschirmkoordinate
     * @param yPos neue Y Bildschirmkoordinate
     */
    public void setDisplayCoordinates(int xPos, int yPos, int dir) {
	this.realPosX = xPos;
	this.realPosY = yPos;
	this.direction = dir;
    }

    public void setBombRadius(int bR) {
	this.bombRadius = bR;
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

    public boolean isDead() {
	return dead;
    }

    public Field getCurrentField() {
	return currentField;
    }

    public double getVelX() {
	return this.velX;
    }

    public double getVelY() {
	return this.velY;
    }

    public double getRealPosX() {
	return realPosX;
    }

    public double getRealPosY() {
	return realPosY;
    }

    public double getXHitbox() {
	return xHitbox;
    }

    public double getYHitbox() {
	return yHitbox;
    }

    public int getDeathTime() {
	return this.deathTime;
    }

    public int getDirection() {
	return this.direction;
    }

    /*
     * Es folgen Methoden zu Bewegungsaktionen. Hier werden jeweils die Velocities
     * passend zur Aktion angepasst. Der Velocity-Wert wird in actionPerformed
     * kontinuierlich addiert.
     * =============================================================================
     */

    public void actionUp() {
	this.direction = 1;
	this.velX = 0;
	this.velY = -((double) GraphicsHandler.getHeight() / this.speedFactor);
    }

    public void actionDown() {
	this.direction = 2;
	this.velX = 0;
	this.velY = (double) GraphicsHandler.getHeight() / this.speedFactor;
    }

    public void actionLeft() {
	this.direction = 3;
	this.velX = -((double) GraphicsHandler.getHeight() / this.speedFactor);
	this.velY = 0;
    }

    public void actionRight() {
	this.direction = 4;
	this.velX = (double) GraphicsHandler.getHeight() / this.speedFactor;
	this.velY = 0;
    }

    public void actionStop() {
	this.direction = 0;
	this.velX = 0;
	this.velY = 0;
	if (this == PlayerHandler.getClientPlayer()) {
	    if (this.connectedClient.isHost()) {
		this.connectedClient.sendMessageToAllClients("202-" + this.id + "-" + super.xPosition + "-"
			+ super.yPosition + "-" + GraphicsHandler.getHeight() + "-" + this.direction);
	    } else {
		this.connectedClient.sendMessage(this.connectedClient.getSession(),
			"203-" + this.id + "-" + super.xPosition + "-" + super.yPosition + "-"
				+ GraphicsHandler.getHeight() + "-" + this.direction);
	    }
	}
    }

    /* ========= Ende des Blocks fuer Bewegungsmethoden. =========== */

    /**
     * Platzieren der Bombe. Fragt ab, ob die aktuelle Position EMPTY ist. Falls ja,
     * dann aendert sich der FieldContent zu BOMB und ein Bomb-Objekt wird in die
     * static ArrayList 'placedBombs' hinzugefuegt.
     */
    public void actionPlantBomb() {
	Field temp = Game.getFieldFromCoord(xPosition, yPosition);
	if (placedBombs < maxBombs && temp.getContent() == FieldContent.EMPTY) {
	    Game.changeFieldContent(FieldContent.BOMB, temp.xPosition, temp.yPosition);
	    Game.addBomb(this.bombRadius, 30, this.id);
	    placedBombs++;
	    ConsoleHandler.print(
		    "Player ID: " + this.id + " placed Bomb at Pos(" + temp.xPosition + ", " + temp.yPosition + ")",
		    MessageType.GAME);
	    if (this.connectedClient.isHost()) {
		this.connectedClient.sendMessageToAllClients("204-" + this.id);
		this.connectedClient.sendMessageToAllClients("204-" + this.id);
		this.connectedClient.sendMessageToAllClients("204-" + this.id);
	    } else if (this.id != 0) {
		this.connectedClient.sendMessage(this.connectedClient.getSession(), "205-" + this.id);
		this.connectedClient.sendMessage(this.connectedClient.getSession(), "205-" + this.id);
		this.connectedClient.sendMessage(this.connectedClient.getSession(), "205-" + this.id);
	    }
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
	if (this.placedBombs > 0) {
	    this.placedBombs--;
	}
    }

    public void increaseBombRadius() {
	if (this.bombRadius < GameData.MAP_DIMENSION) {
	    this.bombRadius++;
	}
	ConsoleHandler.print("Player ID: " + id + ": New Bomb Radius: " + this.bombRadius, MessageType.GAME);
    }

    public void decreaseBombRadius() {
	if (this.bombRadius > 1) {
	    this.bombRadius--;
	}
	ConsoleHandler.print("Player ID: " + id + ": New Bomb Radius: " + this.bombRadius, MessageType.GAME);
    }

    /*
     * Bei Erhoehen der Laufgeschwindigkeit wird der SpeedFactor erhoeht. Dieser
     * wird dann fuer die Berechnung der Velocity bei den Action-Methoden
     * herangeholt. Auch wird die Action-Methode der passenden Richtung einmal
     * aufgerufen, um den neuen Velocity Wert zu berechnen.
     */
    public void increaseMovementSpeed() {
	if (this.movementSpeed < 5) {
	    this.movementSpeed++;

	    /*
	     * Berechnen des Speed Factors: Bildschirmhoehe / gewuenschter Pixel-Shift =
	     * SpeedFactor Bsp: 1080 / 3 = 360 (Standardlaufgeschwindigkeit) Man kann
	     * natuerlich auch andere Bildschirmhoehen und Pixel-Shift werte benutzen. Dies
	     * wird nicht im Programm, sondern vorher als statischer Wert zum Einfuegen
	     * berechnet. Da es ein Faktor ist, bewegt man sich immer noch bei jeder
	     * Aufloesung gleich schnell.
	     */

	    switch (movementSpeed) {
	    case 1:
		this.speedFactor = 360.0; // 1080 / 3
		break;
	    case 2:
		this.speedFactor = 308.6; // 1080 / 3.5
		break;
	    case 3:
		this.speedFactor = 270.0; // 1080 / 4
		break;
	    case 4:
		this.speedFactor = 240.0; // 1080 / 4,5
		break;
	    case 5:
		this.speedFactor = 216.0; // 1080 / 5
	    }

	    switch (this.direction) {
	    case 0:
		this.actionStop();
		break;
	    case 1:
		this.actionUp();
		break;
	    case 2:
		this.actionDown();
		break;
	    case 3:
		this.actionLeft();
		break;
	    case 4:
		this.actionRight();
	    }
	}
	ConsoleHandler.print("Player ID: " + id + ": New Movement Speed: " + this.movementSpeed, MessageType.GAME);
    }

    /*
     * Gleiche Prozedur wie bei increaseMovementSpeed, nur wird die Geschwindigkeit
     * stattdessen verringert.
     */
    public void decreaseMovementSpeed() {
	if (this.movementSpeed > 1) {
	    this.movementSpeed--;

	    switch (movementSpeed) {
	    case 1:
		this.speedFactor = 360.0; // 1080 / 3
		break;
	    case 2:
		this.speedFactor = 308.6; // 1080 / 3.5
		break;
	    case 3:
		this.speedFactor = 270.0; // 1080 / 4
		break;
	    case 4:
		this.speedFactor = 240.0; // 1080 / 4,5
		break;
	    case 5:
		this.speedFactor = 216.0; // 1080 / 5
	    }

	    switch (this.direction) {
	    case 0:
		this.actionStop();
		break;
	    case 1:
		this.actionUp();
		break;
	    case 2:
		this.actionDown();
		break;
	    case 3:
		this.actionLeft();
		break;
	    case 4:
		this.actionRight();
	    }
	}
	ConsoleHandler.print("Player ID: " + id + ": New Movement Speed: " + this.movementSpeed, MessageType.GAME);
    }

    /**
     * Dies ist eine Corner-Detection. In dieser Methode wird ermittelt, ob sich
     * vorne links oder vorne rechts vom Player ein nicht begehbares Feld befindet.
     * Ist dies der Fall wird der Player bei Druecken der selbigen Richtungstaste um
     * die blockierende Ecke gefuehrt.
     * 
     * @return Boolean, ob die Ecke den Player blockiert oder nicht.
     */
    public boolean isPlayerHittingCorner() {
	boolean block = false;
	Field tempField_l = null;
	Field tempField_fl = null;
	Field tempField_r = null;
	Field tempField_fr = null;
	Field tempField = null;
	double xCornerHitbox = this.xHitbox * 0.9;
	double yCornerHitbox = this.yHitbox * 0.9;

	switch (this.direction) {
	case 1: // hoch

	    tempField_l = Game.getFieldFromCoord((int) (((double) super.xPosition - xCornerHitbox) + 0.5),
		    super.yPosition);
	    tempField_fl = Game.getFieldFromCoord((int) (((double) super.xPosition - xCornerHitbox) + 0.5),
		    (int) (((double) super.yPosition - yCornerHitbox) + 0.5));
	    tempField_r = Game.getFieldFromCoord((int) (((double) super.xPosition + xCornerHitbox) + 0.5),
		    super.yPosition);
	    tempField_fr = Game.getFieldFromCoord((int) (((double) super.xPosition + xCornerHitbox) + 0.5),
		    (int) (((double) super.yPosition - yCornerHitbox) + 0.5));

	    if ((tempField_l.getContent() != FieldContent.WALL && tempField_l.getContent() != FieldContent.BLOCK
		    && tempField_l.getContent() != FieldContent.BORDER)
		    && (tempField_fl.getContent() == FieldContent.WALL
			    || tempField_fl.getContent() == FieldContent.BLOCK
			    || tempField_fl.getContent() == FieldContent.BORDER)) {
		tempField = Game.getFieldFromCoord((int) (((double) super.xPosition + this.xHitbox) + 0.5),
			super.yPosition);
		if (tempField.getContent() != FieldContent.WALL && tempField.getContent() != FieldContent.BLOCK
			&& tempField.getContent() != FieldContent.BORDER
			&& (tempField.getContent() != FieldContent.BOMB || (tempField.getContent() == FieldContent.BOMB
				&& this.currentField.getContent() == FieldContent.BOMB
				&& this.currentField == tempField))) {
		    this.realPosX += (this.xHitbox * 0.1);
		} else {
		    // this.realPosX -= (this.xHitbox * 0.1);
		}
		block = true;
	    }

	    if ((tempField_r.getContent() != FieldContent.WALL && tempField_r.getContent() != FieldContent.BLOCK
		    && tempField_r.getContent() != FieldContent.BORDER)
		    && (tempField_fr.getContent() == FieldContent.WALL
			    || tempField_fr.getContent() == FieldContent.BLOCK
			    || tempField_fr.getContent() == FieldContent.BORDER)) {
		tempField = Game.getFieldFromCoord((int) (((double) super.xPosition - this.xHitbox) + 0.5),
			super.yPosition);
		if (tempField.getContent() != FieldContent.WALL && tempField.getContent() != FieldContent.BLOCK
			&& tempField.getContent() != FieldContent.BORDER
			&& (tempField.getContent() != FieldContent.BOMB || (tempField.getContent() == FieldContent.BOMB
				&& this.currentField.getContent() == FieldContent.BOMB
				&& this.currentField == tempField))) {
		    this.realPosX -= (this.xHitbox * 0.1);
		} else {
		    // this.realPosX += (this.xHitbox * 0.1);
		}
		block = true;
	    }

	    break;
	case 2: // runter

	    tempField_l = Game.getFieldFromCoord((int) (((double) super.xPosition + xCornerHitbox) + 0.5),
		    super.yPosition);
	    tempField_fl = Game.getFieldFromCoord((int) (((double) super.xPosition + xCornerHitbox) + 0.5),
		    (int) (((double) super.yPosition + yCornerHitbox) + 0.5));
	    tempField_r = Game.getFieldFromCoord((int) (((double) super.xPosition - xCornerHitbox) + 0.5),
		    super.yPosition);
	    tempField_fr = Game.getFieldFromCoord((int) (((double) super.xPosition - xCornerHitbox) + 0.5),
		    (int) (((double) super.yPosition + yCornerHitbox) + 0.5));

	    if ((tempField_l.getContent() != FieldContent.WALL && tempField_l.getContent() != FieldContent.BLOCK
		    && tempField_l.getContent() != FieldContent.BORDER)
		    && (tempField_fl.getContent() == FieldContent.WALL
			    || tempField_fl.getContent() == FieldContent.BLOCK
			    || tempField_fl.getContent() == FieldContent.BORDER)) {
		tempField = Game.getFieldFromCoord((int) (((double) super.xPosition - this.xHitbox) + 0.5),
			super.yPosition);
		if (tempField.getContent() != FieldContent.WALL && tempField.getContent() != FieldContent.BLOCK
			&& tempField.getContent() != FieldContent.BORDER
			&& (tempField.getContent() != FieldContent.BOMB || (tempField.getContent() == FieldContent.BOMB
				&& this.currentField.getContent() == FieldContent.BOMB
				&& this.currentField == tempField))) {
		    this.realPosX -= (this.xHitbox * 0.1);
		} else {
		    // this.realPosX += (this.xHitbox * 0.1);
		}
		block = true;
	    }

	    if ((tempField_r.getContent() != FieldContent.WALL && tempField_r.getContent() != FieldContent.BLOCK
		    && tempField_r.getContent() != FieldContent.BORDER)
		    && (tempField_fr.getContent() == FieldContent.WALL
			    || tempField_fr.getContent() == FieldContent.BLOCK
			    || tempField_fr.getContent() == FieldContent.BORDER)) {
		tempField = Game.getFieldFromCoord((int) (((double) super.xPosition + this.xHitbox) + 0.5),
			super.yPosition);
		if (tempField.getContent() != FieldContent.WALL && tempField.getContent() != FieldContent.BLOCK
			&& tempField.getContent() != FieldContent.BORDER
			&& (tempField.getContent() != FieldContent.BOMB || (tempField.getContent() == FieldContent.BOMB
				&& this.currentField.getContent() == FieldContent.BOMB
				&& this.currentField == tempField))) {
		    this.realPosX += (this.xHitbox * 0.1);
		} else {
		    // this.realPosX -= (this.xHitbox * 0.1);
		}
		block = true;
	    }

	    break;
	case 3: // links

	    tempField_l = Game.getFieldFromCoord(super.xPosition,
		    (int) (((double) super.yPosition + yCornerHitbox) + 0.5));
	    tempField_fl = Game.getFieldFromCoord((int) (((double) super.xPosition - xCornerHitbox) + 0.5),
		    (int) (((double) super.yPosition + yCornerHitbox) + 0.5));
	    tempField_r = Game.getFieldFromCoord(super.xPosition,
		    (int) (((double) super.yPosition - yCornerHitbox) + 0.5));
	    tempField_fr = Game.getFieldFromCoord((int) (((double) super.xPosition - xCornerHitbox) + 0.5),
		    (int) (((double) super.yPosition - yCornerHitbox) + 0.5));

	    if ((tempField_l.getContent() != FieldContent.WALL && tempField_l.getContent() != FieldContent.BLOCK
		    && tempField_l.getContent() != FieldContent.BORDER)
		    && (tempField_fl.getContent() == FieldContent.WALL
			    || tempField_fl.getContent() == FieldContent.BLOCK
			    || tempField_fl.getContent() == FieldContent.BORDER)) {
		tempField = Game.getFieldFromCoord(super.xPosition,
			(int) (((double) super.yPosition - this.yHitbox) + 0.5));
		if (tempField.getContent() != FieldContent.WALL && tempField.getContent() != FieldContent.BLOCK
			&& tempField.getContent() != FieldContent.BORDER
			&& (tempField.getContent() != FieldContent.BOMB || (tempField.getContent() == FieldContent.BOMB
				&& this.currentField.getContent() == FieldContent.BOMB
				&& this.currentField == tempField))) {
		    this.realPosY -= (this.yHitbox * 0.1);
		} else {
		    // this.realPosY += (this.yHitbox * 0.1);
		}
		block = true;
	    }

	    if ((tempField_r.getContent() != FieldContent.WALL && tempField_r.getContent() != FieldContent.BLOCK
		    && tempField_r.getContent() != FieldContent.BORDER)
		    && (tempField_fr.getContent() == FieldContent.WALL
			    || tempField_fr.getContent() == FieldContent.BLOCK
			    || tempField_fr.getContent() == FieldContent.BORDER)) {
		tempField = Game.getFieldFromCoord(super.xPosition,
			(int) (((double) super.yPosition + this.yHitbox) + 0.5));
		if (tempField.getContent() != FieldContent.WALL && tempField.getContent() != FieldContent.BLOCK
			&& tempField.getContent() != FieldContent.BORDER
			&& (tempField.getContent() != FieldContent.BOMB || (tempField.getContent() == FieldContent.BOMB
				&& this.currentField.getContent() == FieldContent.BOMB
				&& this.currentField == tempField))) {
		    this.realPosY += (this.yHitbox * 0.1);
		} else {
		    // this.realPosY -= (this.yHitbox * 0.1);
		}
		block = true;
	    }

	    break;
	case 4: // rechts

	    tempField_l = Game.getFieldFromCoord(super.xPosition,
		    (int) (((double) super.yPosition - yCornerHitbox) + 0.5));
	    tempField_fl = Game.getFieldFromCoord((int) (((double) super.xPosition + xCornerHitbox) + 0.5),
		    (int) (((double) super.yPosition - yCornerHitbox) + 0.5));
	    tempField_r = Game.getFieldFromCoord(super.xPosition,
		    (int) (((double) super.yPosition + yCornerHitbox) + 0.5));
	    tempField_fr = Game.getFieldFromCoord((int) (((double) super.xPosition + xCornerHitbox) + 0.5),
		    (int) (((double) super.yPosition + yCornerHitbox) + 0.5));

	    if ((tempField_l.getContent() != FieldContent.WALL && tempField_l.getContent() != FieldContent.BLOCK
		    && tempField_l.getContent() != FieldContent.BORDER)
		    && (tempField_fl.getContent() == FieldContent.WALL
			    || tempField_fl.getContent() == FieldContent.BLOCK
			    || tempField_fl.getContent() == FieldContent.BORDER)) {
		tempField = Game.getFieldFromCoord(super.xPosition,
			(int) (((double) super.yPosition + this.yHitbox) + 0.5));
		if (tempField.getContent() != FieldContent.WALL && tempField.getContent() != FieldContent.BLOCK
			&& tempField.getContent() != FieldContent.BORDER
			&& (tempField.getContent() != FieldContent.BOMB || (tempField.getContent() == FieldContent.BOMB
				&& this.currentField.getContent() == FieldContent.BOMB
				&& this.currentField == tempField))) {
		    this.realPosY += (this.yHitbox * 0.1);
		} else {
		    // this.realPosY -= (this.yHitbox * 0.1);
		}
		block = true;
	    }

	    if ((tempField_r.getContent() != FieldContent.WALL && tempField_r.getContent() != FieldContent.BLOCK
		    && tempField_r.getContent() != FieldContent.BORDER)
		    && (tempField_fr.getContent() == FieldContent.WALL
			    || tempField_fr.getContent() == FieldContent.BLOCK
			    || tempField_fr.getContent() == FieldContent.BORDER)) {
		tempField = Game.getFieldFromCoord(super.xPosition,
			(int) (((double) super.yPosition - this.yHitbox) + 0.5));
		if (tempField.getContent() != FieldContent.WALL && tempField.getContent() != FieldContent.BLOCK
			&& tempField.getContent() != FieldContent.BORDER
			&& (tempField.getContent() != FieldContent.BOMB || (tempField.getContent() == FieldContent.BOMB
				&& this.currentField.getContent() == FieldContent.BOMB
				&& this.currentField == tempField))) {
		    this.realPosY -= (this.yHitbox * 0.1);
		} else {
		    // this.realPosY += (this.yHitbox * 0.1);
		}
		block = true;
	    }
	}
	return block;
    }

    public void printPlayerInfo() {
	ConsoleHandler.print("===============================================", MessageType.GAME);
	ConsoleHandler.print("Player ID: " + this.id, MessageType.GAME);
	ConsoleHandler.print("Skin: " + this.skin, MessageType.GAME);
	ConsoleHandler.print("Dead: " + this.dead, MessageType.GAME);
    }

    public void stopTimer() {
	this.t.stop();
    }

    public ConnectedClient getConnectedClient() {
	return connectedClient;
    }
}
