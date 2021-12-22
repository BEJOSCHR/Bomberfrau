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
import javax.swing.Timer;

import uni.bombenstimmung.de.backend.console.ConsoleHandler;
import uni.bombenstimmung.de.backend.console.MessageType;
import uni.bombenstimmung.de.backend.graphics.GraphicsHandler;

public class Bomb implements ActionListener{

    private int radius;
    private int timer;
    private int ownerId;
    private Timer sysTimer;
    private int counter;
    private Field placedField;
    
    
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
	sysTimer = new Timer(1000, this);
	sysTimer.start();
    }
    
    public void actionPerformed(ActionEvent e) {
	this.counter++;
	if (this.counter > this.timer) {
	    this.explode();
	}
    }
    
    public void explode() {
	// TODO: Auswirkung der Explosion auf andere Mitspieler und auf Upgrades
	sysTimer.stop();
	
	/* 
	 * Abfragen, ob sich ein Player oder eine Wall im Weg der Explosion befindet.
	 * Es wird die Stelle der Bombe und die Laenge des Explosionsstrahls
	 * in allen Himmelsrichtungen geprueft.
	 */
	for (int direction = 0; direction < 5; direction++) {
	    int ray = 1;
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
	    } else if (direction == 1) {	// Norden
		while (ray <= this.radius &&
			(Game.getFieldFromMap(this.placedField.xPosition, this.placedField.yPosition - ray).getContent()
			== FieldContent.EMPTY ||
			Game.getFieldFromMap(this.placedField.xPosition, this.placedField.yPosition - ray).getContent()
			== FieldContent.WALL)) {
		    if (Game.getFieldFromMap(this.placedField.xPosition, this.placedField.yPosition - ray).getContent()
			    	== FieldContent.WALL) {
			Game.changeFieldContent(FieldContent.EMPTY, this.placedField.xPosition, this.placedField.yPosition - ray);
			break;
		    } else if (PlayerHandler.getClientPlayer().getCurrentField() ==
			    Game.getFieldFromMap(this.placedField.xPosition, this.placedField.yPosition - ray)) {
			PlayerHandler.getClientPlayer().setDead(true);
		    }
		    for (Player i : PlayerHandler.getOpponentPlayers()) {
			if (i.getCurrentField() == Game.getFieldFromMap(this.placedField.xPosition, this.placedField.yPosition - ray)) {
			    i.setDead(true);
			}
		    }
		    ray++;
		}
	    } else if (direction == 2) {	// Osten
		while (ray <= this.radius &&
			(Game.getFieldFromMap(this.placedField.xPosition + ray, this.placedField.yPosition).getContent()
			== FieldContent.EMPTY ||
			Game.getFieldFromMap(this.placedField.xPosition + ray, this.placedField.yPosition).getContent()
			== FieldContent.WALL)) {
		    if (Game.getFieldFromMap(this.placedField.xPosition + ray, this.placedField.yPosition).getContent()
			    	== FieldContent.WALL) {
			Game.changeFieldContent(FieldContent.EMPTY, this.placedField.xPosition + ray, this.placedField.yPosition);
			break;
		    } else if (PlayerHandler.getClientPlayer().getCurrentField() ==
			    Game.getFieldFromMap(this.placedField.xPosition + ray, this.placedField.yPosition)) {
			PlayerHandler.getClientPlayer().setDead(true);
		    }
		    for (Player i : PlayerHandler.getOpponentPlayers()) {
			if (i.getCurrentField() == Game.getFieldFromMap(this.placedField.xPosition + ray, this.placedField.yPosition)) {
			    i.setDead(true);
			}
		    }
		    ray++;
		}
	    } else if (direction == 3) {	// Sueden
		while (ray <= this.radius &&
			(Game.getFieldFromMap(this.placedField.xPosition, this.placedField.yPosition + ray).getContent()
			== FieldContent.EMPTY ||
			Game.getFieldFromMap(this.placedField.xPosition, this.placedField.yPosition + ray).getContent()
			== FieldContent.WALL)) {
		    if (Game.getFieldFromMap(this.placedField.xPosition, this.placedField.yPosition + ray).getContent()
			    	== FieldContent.WALL) {
			Game.changeFieldContent(FieldContent.EMPTY, this.placedField.xPosition, this.placedField.yPosition + ray);
			break;
		    } else if (PlayerHandler.getClientPlayer().getCurrentField() ==
			    Game.getFieldFromMap(this.placedField.xPosition, this.placedField.yPosition + ray)) {
			PlayerHandler.getClientPlayer().setDead(true);
		    }
		    for (Player i : PlayerHandler.getOpponentPlayers()) {
			if (i.getCurrentField() == Game.getFieldFromMap(this.placedField.xPosition, this.placedField.yPosition + ray)) {
			    i.setDead(true);
			}
		    }
		    ray++;
		}
	    } else if (direction == 4) {	// Westen
		while (ray <= this.radius &&
			(Game.getFieldFromMap(this.placedField.xPosition - ray, this.placedField.yPosition).getContent()
			== FieldContent.EMPTY ||
			Game.getFieldFromMap(this.placedField.xPosition - ray, this.placedField.yPosition).getContent()
			== FieldContent.WALL)) {
		    if (Game.getFieldFromMap(this.placedField.xPosition - ray, this.placedField.yPosition).getContent()
			    	== FieldContent.WALL) {
			Game.changeFieldContent(FieldContent.EMPTY, this.placedField.xPosition - ray, this.placedField.yPosition);
			break;
		    } else if (PlayerHandler.getClientPlayer().getCurrentField() ==
			    Game.getFieldFromMap(this.placedField.xPosition - ray, this.placedField.yPosition)) {
			PlayerHandler.getClientPlayer().setDead(true);
		    }
		    for (Player i : PlayerHandler.getOpponentPlayers()) {
			if (i.getCurrentField() == Game.getFieldFromMap(this.placedField.xPosition - ray, this.placedField.yPosition)) {
			    i.setDead(true);
			}
		    }
		    ray++;
		}
	    }
	}
	/* Aendern des FieldContent auf EMPTY und Loeschen der Bombe. */
	if (PlayerHandler.getClientPlayer().getId() == this.ownerId) {
	    PlayerHandler.getClientPlayer().decreasePlacedBombs();
	    Game.changeFieldContent(FieldContent.EMPTY, placedField.xPosition, placedField.yPosition);
	    ConsoleHandler.print("Bomb from Player ID " + this.ownerId + " exploded at (" + placedField.xPosition +
		    			", " + placedField.yPosition + ")", MessageType.GAME);
	} else {
	    for (Player i : PlayerHandler.getOpponentPlayers()) {
		if (i.getId() == this.ownerId) {
		    Game.changeFieldContent(FieldContent.EMPTY, placedField.xPosition, placedField.yPosition);
		    ConsoleHandler.print("Bomb from Player ID " + this.ownerId + " exploded at (" + placedField.xPosition +
	    					", " + placedField.yPosition + ")", MessageType.GAME);
		}
	    }
	}
	Game.removeBomb(this);
    }
    
    public int getCounter() {
	return this.timer-this.counter;
    }
}
