/*
 * Bomb
 *
 * Version 2.0
 * Author: Dennis
 *
 * Verwaltet die Bombe im Spiel
 */

package uni.bombenstimmung.de.game;

import java.awt.Graphics;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.ArrayList;

import javax.swing.Timer;

import uni.bombenstimmung.de.backend.console.ConsoleHandler;
import uni.bombenstimmung.de.backend.console.MessageType;
import uni.bombenstimmung.de.backend.graphics.GraphicsHandler;
import uni.bombenstimmung.de.backend.sounds.SoundHandler;
import uni.bombenstimmung.de.backend.sounds.SoundType;

public class Bomb implements ActionListener{

    private int radius;
    private int timer;
    private int ownerId;
    private Timer sysTimer;
    private int counter;
    private Field placedField;
    private ArrayList<Wall> targetedWalls;
    
    private boolean fuse = false;
    private boolean grow;
    private double scale;
    
    /**
     * Platziert die Bombe auf einem bestimmten Feld
     * @param r, Radius der Explosion
     * @param t, Zeit bis zur Ecplosion
     * @param ownerId, wer die Bombe legt
     */
    public Bomb(int r, int t, int ownerId) {
	this.radius = r;
	this.timer = t;
	this.counter = 0;
	this.ownerId = ownerId;
	if (PlayerHandler.getClientPlayer().getId() == this.ownerId) {
	    placedField = PlayerHandler.getClientPlayer().getCurrentField();
	} else {
	    for (Player i : PlayerHandler.getOpponentPlayers()) {
		if (i.getId() == this.ownerId) {
		    placedField = i.getCurrentField();
		}
	    }
	}
	this.targetedWalls = new ArrayList<Wall>();
	this.grow = false;
	this.scale = 1.0;
	sysTimer = new Timer(100, this);
	sysTimer.start();
    }
    
    /**
     * Bei Bombenlegung wird hochgezaehlt bis zur Explosion
     * Es wird einmal die Explosin mit Feuer erzeugt und einmal 
     * die Felder wieder leer gemacht
     */
    public void actionPerformed(ActionEvent e) {
	this.counter++;
	
	if (grow) {
	    this.scale += 0.05;
	    if (scale >= 1.0) {
		this.grow = false;
	    }
	} else {
	    this.scale -= 0.05;
	    if (scale <= 0.75) {
		this.grow = true;
	    }
	}
	
	if (this.counter < this.timer) {
	    if (!fuse) SoundHandler.playSound2(SoundType.FUSE, false);
	    fuse = true;
	}
	else if (this.counter == this.timer) {
	    fuse = false;
	    SoundHandler.playSound2(SoundType.EXPLOSION, false);
	    this.explodeFire();
	    SoundHandler.stopSound(SoundType.FUSE);
	} else if (this.counter >= this.timer + 20) {
	    this.explode();
	}
    }
    
    /**
     * Tastet die Felder in allen Himmelsrichtungen ab, und malt die Explosion, prueft
     * ob Spieler im Radius der Explosion liegen. 
     */
    public void explodeFire() {
	
	for(int direction = 0; direction < 5; direction++) {
	    int r = 1;
	    //Stelle der Bombe wird ueberprueft auf Spieler, und Explosion wird eingefuegt
	    if (direction == 0) {	
		if (PlayerHandler.getClientPlayer().getCurrentField() ==
		Game.getFieldFromMap(this.placedField.xPosition, this.placedField.yPosition)) {
		    	PlayerHandler.getClientPlayer().setDead(true);
		}
		for (Player player : PlayerHandler.getOpponentPlayers()) {
		    if (player.getCurrentField() == Game.getFieldFromMap(this.placedField.xPosition, this.placedField.yPosition)) {
			player.setDead(true);
		    }
		}
		Game.changeFieldContent(FieldContent.EXPLOSION1, this.placedField.xPosition, this.placedField.yPosition);
	    } else if (direction == 1) {
		//Stellen suedlich der Bombe wird ueberprueft auf Spieler, und Explosion wird eingefuegt
		while (r <= this.radius && (Game.getFieldFromMap(this.placedField.xPosition, this.placedField.yPosition + r).getContent() == FieldContent.WALL || 
			Game.getFieldFromMap(this.placedField.xPosition, this.placedField.yPosition + r).getContent() == FieldContent.EMPTY || 
			Game.getFieldFromMap(this.placedField.xPosition, this.placedField.yPosition + r).getContent() == FieldContent.BOMB ||
			Game.getFieldFromMap(this.placedField.xPosition, this.placedField.yPosition + r).getContent() == FieldContent.UPGRADE_ITEM_BOMB ||
			Game.getFieldFromMap(this.placedField.xPosition, this.placedField.yPosition + r).getContent() == FieldContent.UPGRADE_ITEM_SHOE ||
			Game.getFieldFromMap(this.placedField.xPosition, this.placedField.yPosition + r).getContent() == FieldContent.UPGRADE_ITEM_FIRE)) {
			/* Erzeugen eines Wall-Objekts fuer Drop-Moeglichkeit eines Upgrades. */
		    if (Game.getFieldFromMap(this.placedField.xPosition, this.placedField.yPosition + r).getContent() == FieldContent.WALL) {
			    targetedWalls.add(new Wall(Game.getFieldFromMap(this.placedField.xPosition, this.placedField.yPosition + r)));
			    Game.changeFieldContent(FieldContent.EXPLOSION3_S, this.placedField.xPosition, this.placedField.yPosition + r);
			    break;
		    }
		    if(Game.getFieldFromMap(this.placedField.xPosition, this.placedField.yPosition + r).getContent() == FieldContent.EXPLOSION1 || 
				Game.getFieldFromMap(this.placedField.xPosition, this.placedField.yPosition + r).getContent() == FieldContent.EXPLOSION2_NS ||
				Game.getFieldFromMap(this.placedField.xPosition, this.placedField.yPosition + r).getContent() == FieldContent.EXPLOSION3_N ) {
				break;
			    }
		    if (PlayerHandler.getClientPlayer().getCurrentField() ==
				Game.getFieldFromMap(this.placedField.xPosition, this.placedField.yPosition + r)) {
			    	PlayerHandler.getClientPlayer().setDead(true);
		    }
		    for (Player player : PlayerHandler.getOpponentPlayers()) {
	    	    	if (player.getCurrentField() == Game.getFieldFromMap(this.placedField.xPosition, this.placedField.yPosition + r)) {
	    	    	    player.setDead(true);
	    	    	}
		    }
		    if (Game.getFieldFromMap(this.placedField.xPosition, this.placedField.yPosition + r).getContent() == FieldContent.BOMB) {
			chainreaction(this.placedField.xPosition, this.placedField.yPosition + r);
			break;
		    }
		    if ((Game.getFieldFromMap(this.placedField.xPosition, this.placedField.yPosition + (r+1)).getContent() == FieldContent.BORDER ||
				Game.getFieldFromMap(this.placedField.xPosition, this.placedField.yPosition + (r+1)).getContent() == FieldContent.BLOCK) ||
				(Game.getFieldFromMap(this.placedField.xPosition, this.placedField.yPosition + (r+1)).getContent() == FieldContent.BOMB &&
				(this.radius - r == 0)) || (this.radius - r == 0)) {
			    		Game.changeFieldContent(FieldContent.EXPLOSION3_S, this.placedField.xPosition, this.placedField.yPosition + r);
			    		r++;
			    		break;
		
		    } else {
			    Game.changeFieldContent(FieldContent.EXPLOSION2_NS, this.placedField.xPosition, this.placedField.yPosition + r);
		    }
		    r++;
		}
	    } else if(direction == 2) {
		//Stellen noerdlich der Bombe werden ueberprueft auf Spieler, und Explosion wird eingefuegt
		while (r <= this.radius && (Game.getFieldFromMap(this.placedField.xPosition, this.placedField.yPosition - r).getContent() == FieldContent.WALL || 
			Game.getFieldFromMap(this.placedField.xPosition, this.placedField.yPosition - r).getContent() == FieldContent.EMPTY ||
			Game.getFieldFromMap(this.placedField.xPosition, this.placedField.yPosition - r).getContent() == FieldContent.BOMB ||
			Game.getFieldFromMap(this.placedField.xPosition, this.placedField.yPosition - r).getContent() == FieldContent.UPGRADE_ITEM_BOMB ||
			Game.getFieldFromMap(this.placedField.xPosition, this.placedField.yPosition - r).getContent() == FieldContent.UPGRADE_ITEM_SHOE ||
			Game.getFieldFromMap(this.placedField.xPosition, this.placedField.yPosition - r).getContent() == FieldContent.UPGRADE_ITEM_FIRE)) {
		    	/* Erzeugen eines Wall-Objekts fuer Drop-Moeglichkeit eines Upgrades. */
			if (Game.getFieldFromMap(this.placedField.xPosition, this.placedField.yPosition - r).getContent() == FieldContent.WALL) {
			    targetedWalls.add(new Wall(Game.getFieldFromMap(this.placedField.xPosition, this.placedField.yPosition - r)));
			    Game.changeFieldContent(FieldContent.EXPLOSION3_N, this.placedField.xPosition, this.placedField.yPosition - r);
			    break;
			}
			if(Game.getFieldFromMap(this.placedField.xPosition, this.placedField.yPosition - r).getContent() == FieldContent.EXPLOSION1 || 
				Game.getFieldFromMap(this.placedField.xPosition, this.placedField.yPosition - r).getContent() == FieldContent.EXPLOSION2_NS ||
				Game.getFieldFromMap(this.placedField.xPosition, this.placedField.yPosition - r).getContent() == FieldContent.EXPLOSION3_S ) {
				break;
			}
			if (PlayerHandler.getClientPlayer().getCurrentField() ==
				    Game.getFieldFromMap(this.placedField.xPosition, this.placedField.yPosition - r)) {
					PlayerHandler.getClientPlayer().setDead(true);
			}
			for (Player player : PlayerHandler.getOpponentPlayers()) {
	    	    		if (player.getCurrentField() == Game.getFieldFromMap(this.placedField.xPosition, this.placedField.yPosition - r)) {
	    	    		    player.setDead(true);
	    	    		}
	    		}
			if (Game.getFieldFromMap(this.placedField.xPosition, this.placedField.yPosition - r).getContent() == FieldContent.BOMB) {
				chainreaction(this.placedField.xPosition, this.placedField.yPosition - r);
				break;
			}
			if ((Game.getFieldFromMap(this.placedField.xPosition, this.placedField.yPosition - (r+1)).getContent() == FieldContent.BORDER ||
				Game.getFieldFromMap(this.placedField.xPosition, this.placedField.yPosition - (r+1)).getContent() == FieldContent.BLOCK) ||
				(Game.getFieldFromMap(this.placedField.xPosition, this.placedField.yPosition - (r+1)).getContent() == FieldContent.BOMB &&
				(this.radius - r == 0)) || (this.radius - r == 0)){
			    		Game.changeFieldContent(FieldContent.EXPLOSION3_N, this.placedField.xPosition, this.placedField.yPosition - r);
			    		r++;
			    		break;
			}else {
				    Game.changeFieldContent(FieldContent.EXPLOSION2_NS, this.placedField.xPosition, this.placedField.yPosition - r);
				}
			r++;
		}
	    } else if (direction == 3) {
		//Stellen oestlich der Bombe werden ueberprueft auf Spieler, und Explosion wird eingefuegt
		while (r <= this.radius && (Game.getFieldFromMap(this.placedField.xPosition + r, this.placedField.yPosition).getContent() == FieldContent.WALL || 
			Game.getFieldFromMap(this.placedField.xPosition + r, this.placedField.yPosition).getContent() == FieldContent.EMPTY ||
			Game.getFieldFromMap(this.placedField.xPosition + r, this.placedField.yPosition).getContent() == FieldContent.BOMB ||
			Game.getFieldFromMap(this.placedField.xPosition + r, this.placedField.yPosition).getContent() == FieldContent.UPGRADE_ITEM_BOMB ||
			Game.getFieldFromMap(this.placedField.xPosition + r, this.placedField.yPosition).getContent() == FieldContent.UPGRADE_ITEM_SHOE ||
			Game.getFieldFromMap(this.placedField.xPosition + r, this.placedField.yPosition).getContent() == FieldContent.UPGRADE_ITEM_FIRE)) {
		    	/* Erzeugen eines Wall-Objekts fuer Drop-Moeglichkeit eines Upgrades. */
			if (Game.getFieldFromMap(this.placedField.xPosition + r, this.placedField.yPosition).getContent() == FieldContent.WALL) {
			    targetedWalls.add(new Wall(Game.getFieldFromMap(this.placedField.xPosition + r, this.placedField.yPosition)));
			    Game.changeFieldContent(FieldContent.EXPLOSION3_O, this.placedField.xPosition + r, this.placedField.yPosition);
			    break;
			}
			if(Game.getFieldFromMap(this.placedField.xPosition + r, this.placedField.yPosition).getContent() == FieldContent.EXPLOSION1 || 
				Game.getFieldFromMap(this.placedField.xPosition + r, this.placedField.yPosition).getContent() == FieldContent.EXPLOSION2 ||
				Game.getFieldFromMap(this.placedField.xPosition + r, this.placedField.yPosition).getContent() == FieldContent.EXPLOSION3_W ) {
				break;
			}
			if (PlayerHandler.getClientPlayer().getCurrentField() ==
				    Game.getFieldFromMap(this.placedField.xPosition + r, this.placedField.yPosition)) {
					PlayerHandler.getClientPlayer().setDead(true);
			}
			for (Player player : PlayerHandler.getOpponentPlayers()) {
	    	    		if (player.getCurrentField() == Game.getFieldFromMap(this.placedField.xPosition + r, this.placedField.yPosition)) {
	    	    		    player.setDead(true);
	    	    		}
	    		}
			if (Game.getFieldFromMap(this.placedField.xPosition + r, this.placedField.yPosition).getContent() == FieldContent.BOMB) {
				chainreaction(this.placedField.xPosition + r, this.placedField.yPosition);
				//b.setCounter(b.getTimer());
				break;
			    }
			if ((Game.getFieldFromMap(this.placedField.xPosition + (r+1), this.placedField.yPosition).getContent() == FieldContent.BORDER ||
				Game.getFieldFromMap(this.placedField.xPosition + (r+1), this.placedField.yPosition).getContent() == FieldContent.BLOCK) ||
				(Game.getFieldFromMap(this.placedField.xPosition + (r+1), this.placedField.yPosition).getContent() == FieldContent.BOMB &&
				(this.radius - r == 0)) || (this.radius - r == 0)) {
			    		Game.changeFieldContent(FieldContent.EXPLOSION3_O, this.placedField.xPosition + r, this.placedField.yPosition);
			    		r++;
			    		break;
			}else{
			    Game.changeFieldContent(FieldContent.EXPLOSION2, this.placedField.xPosition + r, this.placedField.yPosition);
			}
			r++;
		}
	    } else if (direction == 4) {
		//Stellen westliche der Bombe werden ueberprueft auf Spieler, und Explosion wird eingefuegt
		while (r <= this.radius && (Game.getFieldFromMap(this.placedField.xPosition - r, this.placedField.yPosition).getContent() == FieldContent.WALL || 
			Game.getFieldFromMap(this.placedField.xPosition - r, this.placedField.yPosition).getContent() == FieldContent.EMPTY ||
			Game.getFieldFromMap(this.placedField.xPosition - r, this.placedField.yPosition).getContent() == FieldContent.BOMB ||
			Game.getFieldFromMap(this.placedField.xPosition - r, this.placedField.yPosition).getContent() == FieldContent.UPGRADE_ITEM_BOMB ||
			Game.getFieldFromMap(this.placedField.xPosition - r, this.placedField.yPosition).getContent() == FieldContent.UPGRADE_ITEM_SHOE ||
			Game.getFieldFromMap(this.placedField.xPosition - r, this.placedField.yPosition).getContent() == FieldContent.UPGRADE_ITEM_FIRE)) {
		    	/* Erzeugen eines Wall-Objekts fuer Drop-Moeglichkeit eines Upgrades. */
			if (Game.getFieldFromMap(this.placedField.xPosition - r, this.placedField.yPosition).getContent() == FieldContent.WALL) {
			    targetedWalls.add(new Wall(Game.getFieldFromMap(this.placedField.xPosition - r, this.placedField.yPosition)));
			    Game.changeFieldContent(FieldContent.EXPLOSION3_W, this.placedField.xPosition - r, this.placedField.yPosition);
			    break;
			}
			if(Game.getFieldFromMap(this.placedField.xPosition - r, this.placedField.yPosition).getContent() == FieldContent.EXPLOSION1 || 
				Game.getFieldFromMap(this.placedField.xPosition - r, this.placedField.yPosition).getContent() == FieldContent.EXPLOSION2 ||
				Game.getFieldFromMap(this.placedField.xPosition - r, this.placedField.yPosition).getContent() == FieldContent.EXPLOSION3_O ) {
				break;
			}
			if (PlayerHandler.getClientPlayer().getCurrentField() ==
				    Game.getFieldFromMap(this.placedField.xPosition - r, this.placedField.yPosition)) {
					PlayerHandler.getClientPlayer().setDead(true);
			}
			for (Player player : PlayerHandler.getOpponentPlayers()) {
	    	    		if (player.getCurrentField() == Game.getFieldFromMap(this.placedField.xPosition - r, this.placedField.yPosition)) {
	    	    		    player.setDead(true);
	    	    		}
	    		}
			if (Game.getFieldFromMap(this.placedField.xPosition - r, this.placedField.yPosition).getContent() == FieldContent.BOMB) {
				chainreaction(this.placedField.xPosition - r, this.placedField.yPosition);
				break;
			    }
			if ((Game.getFieldFromMap(this.placedField.xPosition - (r+1), this.placedField.yPosition).getContent() == FieldContent.BORDER ||
				Game.getFieldFromMap(this.placedField.xPosition - (r+1), this.placedField.yPosition).getContent() == FieldContent.BLOCK) ||
				(Game.getFieldFromMap(this.placedField.xPosition - (r+1), this.placedField.yPosition).getContent() == FieldContent.BOMB &&
				(this.radius - r == 0)) || (this.radius - r == 0)) {
			    		Game.changeFieldContent(FieldContent.EXPLOSION3_W, this.placedField.xPosition - r, this.placedField.yPosition);
			    		r++;
			    		break;
			}else {
				    Game.changeFieldContent(FieldContent.EXPLOSION2, this.placedField.xPosition - r, this.placedField.yPosition);
			}
			r++;
		}
	    }
	}
	/* Dekrement der gelegten Bombe bei Players & Aendern des FieldContent auf EXPLOSION1. */
	if (PlayerHandler.getClientPlayer().getId() == this.ownerId) {
	    PlayerHandler.getClientPlayer().decreasePlacedBombs();
	    Game.changeFieldContent(FieldContent.EXPLOSION1, placedField.xPosition, placedField.yPosition);
	} else {
	    for (Player player : PlayerHandler.getOpponentPlayers()) {
		if (player.getId() == this.ownerId) {
		    player.decreasePlacedBombs();
		    Game.changeFieldContent(FieldContent.EXPLOSION1, placedField.xPosition, placedField.yPosition);
		}
	    }
	}
    }
    
    /**
     * Muss nach explodeFire() aufgerufen werden um die mit Feuer bemalten Felder wieder leer zu machen
     */
    public void explode() {
	sysTimer.stop();
	
	for(int direction = 0; direction < 5; direction++) {
	    int r = 1;
	    if (direction == 0) {	// Stelle der Bombe
		if (PlayerHandler.getClientPlayer().getCurrentField() ==
		Game.getFieldFromMap(this.placedField.xPosition, this.placedField.yPosition)) {
		    	PlayerHandler.getClientPlayer().setDead(true);
		}
		for (Player player : PlayerHandler.getOpponentPlayers()) {
		    if (player.getCurrentField() == Game.getFieldFromMap(this.placedField.xPosition, this.placedField.yPosition)) {
			player.setDead(true);
		    }
		}
	    } else if(direction == 1) { //SUEDEN
		    while (r <= this.radius && (Game.getFieldFromMap(this.placedField.xPosition, this.placedField.yPosition + r).getContent() == FieldContent.EXPLOSION2_NS || 
			Game.getFieldFromMap(this.placedField.xPosition, this.placedField.yPosition + r).getContent() == FieldContent.EXPLOSION3_S)) {
				Game.changeFieldContent(FieldContent.EMPTY,this.placedField.xPosition, this.placedField.yPosition + r);
				r++;
				
		    }
	    } else if(direction == 2) { //NORDEN
		while (r <= this.radius && (Game.getFieldFromMap(this.placedField.xPosition, this.placedField.yPosition - r).getContent() == FieldContent.EXPLOSION2_NS || 
			Game.getFieldFromMap(this.placedField.xPosition, this.placedField.yPosition - r).getContent() == FieldContent.EXPLOSION3_N)) {
				Game.changeFieldContent(FieldContent.EMPTY,this.placedField.xPosition, this.placedField.yPosition - r);
				r++;
		}
	    } else if (direction == 3) { // OSTEN
		while (r <= this.radius && (Game.getFieldFromMap(this.placedField.xPosition + r, this.placedField.yPosition).getContent() == FieldContent.EXPLOSION2 || 
			Game.getFieldFromMap(this.placedField.xPosition + r, this.placedField.yPosition).getContent() == FieldContent.EXPLOSION3_O)) {
				Game.changeFieldContent(FieldContent.EMPTY,this.placedField.xPosition + r, this.placedField.yPosition);
				r++;
		}
	    } else if (direction == 4) { // WESTEN
		while (r <= this.radius && (Game.getFieldFromMap(this.placedField.xPosition - r, this.placedField.yPosition).getContent() == FieldContent.EXPLOSION2 || 
			Game.getFieldFromMap(this.placedField.xPosition - r, this.placedField.yPosition).getContent() == FieldContent.EXPLOSION3_W)) {
				Game.changeFieldContent(FieldContent.EMPTY,this.placedField.xPosition - r, this.placedField.yPosition);
				r++;
		}
	    }
	}
	/* Aufrufen von destroyed-Methode aller erzeugten Wall-Objekte. */
	Game.changeFieldContent(FieldContent.EMPTY, placedField.xPosition, placedField.yPosition);
	for (Wall wall : this.targetedWalls) {
	    wall.destroyed();
	}
	ConsoleHandler.print("Bomb from Player ID " + this.ownerId + " exploded at (" + placedField.xPosition +
			", " + placedField.yPosition + ")", MessageType.GAME);
	this.targetedWalls.clear();
	Game.removeBomb(this);
    }
    
    public int getCounter() {
	return this.timer-this.counter;
    }
    
    public double getScale() {
	return scale;
    }
    
    public void setCounter(int c) {
	this.counter = c;
    }
    
    public Field getPlacedField() {
	return placedField;
    }
    
    public void chainreaction(int x, int y) {
	ArrayList<Bomb> placed = Game.getPlacedBombs();
	for (Bomb bomb : placed) {
		if (bomb.getPlacedField().xPosition == x && bomb.getPlacedField().yPosition == y) {
			   bomb.explodeFire();
		}
	}
    }
    
    public int getTimer() {
	return this.timer;
    }
    
    public ArrayList<Wall> getTargetedWalls() {
	return targetedWalls;
    }
    
    public void drawCounter(Graphics g) {
	if (this.getCounter() > 0) {
	    int xOffset = GraphicsHandler.getWidth()-(GameData.FIELD_DIMENSION*GameData.MAP_DIMENSION);
		int yOffset = GameData.MAP_SIDE_BORDER;
		GraphicsHandler.drawCentralisedText(g, Color.RED, 30, this.getCounter() + "", (this.placedField.xPosition*GameData.FIELD_DIMENSION)+(xOffset/2)+(GameData.FIELD_DIMENSION/2), (this.placedField.yPosition*GameData.FIELD_DIMENSION)+(yOffset/2)+(GameData.FIELD_DIMENSION/2));
	}
    }
    
    public void stopTimer() {
	this.sysTimer.stop();
    }
}
