/*
 * Bomb
 *
 * Version 1.0
 * Author: Dennis
 *
 * Verwaltet die Bombe im Spiel
 */

package uni.bombenstimmung.de.game;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.ArrayList;

import javax.swing.Timer;

import uni.bombenstimmung.de.backend.console.ConsoleHandler;
import uni.bombenstimmung.de.backend.console.MessageType;

public class Bomb implements ActionListener{

    private int radius;
    private int timer;
    private int ownerId;
    private Timer sysTimer;
    private int counter;
    private Field placedField;
    private ArrayList<Wall> targetedWalls;
    
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
	sysTimer = new Timer(1000, this);
	sysTimer.start();
    }
    
    /**
     * Bei Bombenlegung wird hochgezaehlt bis zur Explosion
     * Es wird einmal die Explosin mit Feuer erzeugt und einmal 
     * die Felder wieder leer gemacht
     */
    public void actionPerformed(ActionEvent e) {
	this.counter++;
	if (this.counter == this.timer) {
	    this.explodeFire();
	} else if (this.counter >= this.timer + 2) {
	    this.explode();
	}
    }
    
    /**
     * Tastet die Felder in allen Himmelsrichtungen ab, und malt die Explosion, prueft
     * ob Spieler im Radius der Explosion liegen. 
     */
    public void explodeFire() {
	//sysTimer.stop();
	
	for(int direction = 0; direction < 5; direction++) {
	    int r = 1;
	    if (direction == 0) {	// Stelle der Bombe
		if (PlayerHandler.getClientPlayer().getCurrentField() ==
		Game.getFieldFromMap(this.placedField.xPosition, this.placedField.yPosition)) {
		    	PlayerHandler.getClientPlayer().setDead(true);
		}
		for (Player i : PlayerHandler.getOpponentPlayers()) {
		    if (i.getCurrentField() == Game.getFieldFromMap(this.placedField.xPosition, this.placedField.yPosition)) {
			i.setDead(true);
		    }
		}
	    } else if (direction == 1) { //SUEDEN
		while (r <= this.radius && (Game.getFieldFromMap(this.placedField.xPosition, this.placedField.yPosition + r).getContent() == FieldContent.WALL || 
			Game.getFieldFromMap(this.placedField.xPosition, this.placedField.yPosition + r).getContent() == FieldContent.EMPTY || 
			Game.getFieldFromMap(this.placedField.xPosition, this.placedField.yPosition + r).getContent() == FieldContent.UPGRADE_ITEM_BOMB ||
			Game.getFieldFromMap(this.placedField.xPosition, this.placedField.yPosition + r).getContent() == FieldContent.UPGRADE_ITEM_SHOE ||
			Game.getFieldFromMap(this.placedField.xPosition, this.placedField.yPosition + r).getContent() == FieldContent.UPGRADE_ITEM_FIRE)) {
			/* Erzeugen eines Wall-Objekts fuer Drop-Moeglichkeit eines Upgrades. */
		    if (Game.getFieldFromMap(this.placedField.xPosition, this.placedField.yPosition + r).getContent() == FieldContent.WALL) {
			    targetedWalls.add(new Wall(Game.getFieldFromMap(this.placedField.xPosition, this.placedField.yPosition + r)));
		    }
		    if (PlayerHandler.getClientPlayer().getCurrentField() ==
				Game.getFieldFromMap(this.placedField.xPosition, this.placedField.yPosition + r)) {
			    	PlayerHandler.getClientPlayer().setDead(true);
		    }
		    for (Player i : PlayerHandler.getOpponentPlayers()) {
	    	    	if (i.getCurrentField() == Game.getFieldFromMap(this.placedField.xPosition, this.placedField.yPosition + r)) {
	    	    	    i.setDead(true);
	    	    	}
		    }
		    if ((Game.getFieldFromMap(this.placedField.xPosition, this.placedField.yPosition + (r+1)).getContent() == FieldContent.BORDER ||
				Game.getFieldFromMap(this.placedField.xPosition, this.placedField.yPosition + (r+1)).getContent() == FieldContent.BLOCK ||
				Game.getFieldFromMap(this.placedField.xPosition, this.placedField.yPosition + (r+1)).getContent() == FieldContent.BOMB) ||
				(this.radius - r == 0)) {
			    		Game.changeFieldContent(FieldContent.EXPLOSION3_S, this.placedField.xPosition, this.placedField.yPosition + r);
			    		r++;
			    		break;
		    }else {
			    Game.changeFieldContent(FieldContent.EXPLOSION2_NS, this.placedField.xPosition, this.placedField.yPosition + r);
		    }
		    r++;
		}
	    } else if(direction == 2) { //NORDEN
		while (r <= this.radius && (Game.getFieldFromMap(this.placedField.xPosition, this.placedField.yPosition - r).getContent() == FieldContent.WALL || 
			Game.getFieldFromMap(this.placedField.xPosition, this.placedField.yPosition - r).getContent() == FieldContent.EMPTY ||
			Game.getFieldFromMap(this.placedField.xPosition, this.placedField.yPosition - r).getContent() == FieldContent.UPGRADE_ITEM_BOMB ||
			Game.getFieldFromMap(this.placedField.xPosition, this.placedField.yPosition - r).getContent() == FieldContent.UPGRADE_ITEM_SHOE ||
			Game.getFieldFromMap(this.placedField.xPosition, this.placedField.yPosition - r).getContent() == FieldContent.UPGRADE_ITEM_FIRE)) {
		    	/* Erzeugen eines Wall-Objekts fuer Drop-Moeglichkeit eines Upgrades. */
			if (Game.getFieldFromMap(this.placedField.xPosition, this.placedField.yPosition - r).getContent() == FieldContent.WALL) {
			    targetedWalls.add(new Wall(Game.getFieldFromMap(this.placedField.xPosition, this.placedField.yPosition - r)));
			}
			if (PlayerHandler.getClientPlayer().getCurrentField() ==
				    Game.getFieldFromMap(this.placedField.xPosition, this.placedField.yPosition - r)) {
					PlayerHandler.getClientPlayer().setDead(true);
			}
			for (Player i : PlayerHandler.getOpponentPlayers()) {
	    	    		if (i.getCurrentField() == Game.getFieldFromMap(this.placedField.xPosition, this.placedField.yPosition - r)) {
	    	    		    i.setDead(true);
	    	    		}
	    		}
			if ((Game.getFieldFromMap(this.placedField.xPosition, this.placedField.yPosition - (r+1)).getContent() == FieldContent.BORDER ||
				Game.getFieldFromMap(this.placedField.xPosition, this.placedField.yPosition - (r+1)).getContent() == FieldContent.BLOCK ||
				Game.getFieldFromMap(this.placedField.xPosition, this.placedField.yPosition - (r+1)).getContent() == FieldContent.BOMB) ||
				(this.radius - r == 0)) {
			    		Game.changeFieldContent(FieldContent.EXPLOSION3_N, this.placedField.xPosition, this.placedField.yPosition - r);
			    		r++;
			    		break;
			}else {
				    Game.changeFieldContent(FieldContent.EXPLOSION2_NS, this.placedField.xPosition, this.placedField.yPosition - r);
				}
			r++;
		}
	    } else if (direction == 3) { // OSTEN
		while (r <= this.radius && (Game.getFieldFromMap(this.placedField.xPosition + r, this.placedField.yPosition).getContent() == FieldContent.WALL || 
			Game.getFieldFromMap(this.placedField.xPosition + r, this.placedField.yPosition).getContent() == FieldContent.EMPTY ||
			Game.getFieldFromMap(this.placedField.xPosition + r, this.placedField.yPosition).getContent() == FieldContent.UPGRADE_ITEM_BOMB ||
			Game.getFieldFromMap(this.placedField.xPosition + r, this.placedField.yPosition).getContent() == FieldContent.UPGRADE_ITEM_SHOE ||
			Game.getFieldFromMap(this.placedField.xPosition + r, this.placedField.yPosition).getContent() == FieldContent.UPGRADE_ITEM_FIRE)) {
		    	/* Erzeugen eines Wall-Objekts fuer Drop-Moeglichkeit eines Upgrades. */
			if (Game.getFieldFromMap(this.placedField.xPosition + r, this.placedField.yPosition).getContent() == FieldContent.WALL) {
			    targetedWalls.add(new Wall(Game.getFieldFromMap(this.placedField.xPosition + r, this.placedField.yPosition)));
			}
			if (PlayerHandler.getClientPlayer().getCurrentField() ==
				    Game.getFieldFromMap(this.placedField.xPosition + r, this.placedField.yPosition)) {
					PlayerHandler.getClientPlayer().setDead(true);
			}
			for (Player i : PlayerHandler.getOpponentPlayers()) {
	    	    		if (i.getCurrentField() == Game.getFieldFromMap(this.placedField.xPosition + r, this.placedField.yPosition)) {
	    	    		    i.setDead(true);
	    	    		}
	    		}
			if ((Game.getFieldFromMap(this.placedField.xPosition + (r+1), this.placedField.yPosition).getContent() == FieldContent.BORDER ||
				Game.getFieldFromMap(this.placedField.xPosition + (r+1), this.placedField.yPosition).getContent() == FieldContent.BLOCK ||
				Game.getFieldFromMap(this.placedField.xPosition + (r+1), this.placedField.yPosition).getContent() == FieldContent.BOMB) ||
				(this.radius - r == 0)) {
			    		Game.changeFieldContent(FieldContent.EXPLOSION3_O, this.placedField.xPosition + r, this.placedField.yPosition);
			    		r++;
			    		break;
			}else{
			    Game.changeFieldContent(FieldContent.EXPLOSION2, this.placedField.xPosition + r, this.placedField.yPosition);
			}
			r++;
		}
	    } else if (direction == 4) { // WESTEN
		while (r <= this.radius && (Game.getFieldFromMap(this.placedField.xPosition - r, this.placedField.yPosition).getContent() == FieldContent.WALL || 
			Game.getFieldFromMap(this.placedField.xPosition - r, this.placedField.yPosition).getContent() == FieldContent.EMPTY ||
			Game.getFieldFromMap(this.placedField.xPosition - r, this.placedField.yPosition).getContent() == FieldContent.UPGRADE_ITEM_BOMB ||
			Game.getFieldFromMap(this.placedField.xPosition - r, this.placedField.yPosition).getContent() == FieldContent.UPGRADE_ITEM_SHOE ||
			Game.getFieldFromMap(this.placedField.xPosition - r, this.placedField.yPosition).getContent() == FieldContent.UPGRADE_ITEM_FIRE)) {
		    	/* Erzeugen eines Wall-Objekts fuer Drop-Moeglichkeit eines Upgrades. */
			if (Game.getFieldFromMap(this.placedField.xPosition - r, this.placedField.yPosition).getContent() == FieldContent.WALL) {
			    targetedWalls.add(new Wall(Game.getFieldFromMap(this.placedField.xPosition - r, this.placedField.yPosition)));
			}
			if (PlayerHandler.getClientPlayer().getCurrentField() ==
				    Game.getFieldFromMap(this.placedField.xPosition - r, this.placedField.yPosition)) {
					PlayerHandler.getClientPlayer().setDead(true);
			}
			for (Player i : PlayerHandler.getOpponentPlayers()) {
	    	    		if (i.getCurrentField() == Game.getFieldFromMap(this.placedField.xPosition - r, this.placedField.yPosition)) {
	    	    		    i.setDead(true);
	    	    		}
	    		}
			if ((Game.getFieldFromMap(this.placedField.xPosition - (r+1), this.placedField.yPosition).getContent() == FieldContent.BORDER ||
				Game.getFieldFromMap(this.placedField.xPosition - (r+1), this.placedField.yPosition).getContent() == FieldContent.BLOCK ||
					Game.getFieldFromMap(this.placedField.xPosition - (r+1), this.placedField.yPosition).getContent() == FieldContent.BOMB) ||
				(this.radius - r == 0)) {
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
	/* Aendern des FieldContent auf EMPTY und Loeschen der Bombe. */
	if (PlayerHandler.getClientPlayer().getId() == this.ownerId) {
	    PlayerHandler.getClientPlayer().decreasePlacedBombs();
	    Game.changeFieldContent(FieldContent.EXPLOSION1, placedField.xPosition, placedField.yPosition);
	    ConsoleHandler.print("Bomb from Player ID " + this.ownerId + " exploded at (" + placedField.xPosition +
		    			", " + placedField.yPosition + ")", MessageType.GAME);
	} else {
	    for (Player i : PlayerHandler.getOpponentPlayers()) {
		if (i.getId() == this.ownerId) {
		    Game.changeFieldContent(FieldContent.EXPLOSION1, placedField.xPosition, placedField.yPosition);
		    ConsoleHandler.print("Bomb from Player ID " + this.ownerId + " exploded at (" + placedField.xPosition +
	    					", " + placedField.yPosition + ")", MessageType.GAME);
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
		for (Player i : PlayerHandler.getOpponentPlayers()) {
		    if (i.getCurrentField() == Game.getFieldFromMap(this.placedField.xPosition, this.placedField.yPosition)) {
			i.setDead(true);
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
	/* Dekrement der gelegten Bombe bei Players */
	if (PlayerHandler.getClientPlayer().getId() == this.ownerId) {
	    PlayerHandler.getClientPlayer().decreasePlacedBombs();
	} else {
	    for (Player i : PlayerHandler.getOpponentPlayers()) {
		if (i.getId() == this.ownerId) {
		    // TODO: Ist das hier notwendig? Das wird eigentlich bei den anderen Clients berechnet und mit eigenem Client synchronisiert.
		    i.decreasePlacedBombs();
		}
	    }
	}
	/* Aufrufen von destroyed-Methode aller erzeugten Wall-Objekte. */
	Game.changeFieldContent(FieldContent.EMPTY, placedField.xPosition, placedField.yPosition);
	for (Wall i : this.targetedWalls) {
	    i.destroyed();
	}
	ConsoleHandler.print("Bomb from Player ID " + this.ownerId + " exploded at (" + placedField.xPosition +
			", " + placedField.yPosition + ")", MessageType.GAME);
	Game.removeBomb(this);
    }
    
    public int getCounter() {
	return this.timer-this.counter;
    }
    
    public ArrayList<Wall> getTargetedWalls() {
	return targetedWalls;
    }
}
