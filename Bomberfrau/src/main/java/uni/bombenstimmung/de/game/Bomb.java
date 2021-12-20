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
	    placedField = Game.getFieldFromCoord(PlayerHandler.getClientPlayer().xPosition, PlayerHandler.getClientPlayer().yPosition);
	    
	} else {
	    for (Player i : PlayerHandler.getOpponentPlayers()) {
		if (i.getId() == this.ownerId) {
		    placedField = Game.getFieldFromCoord(i.xPosition, i.yPosition);
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
	// TODO: Explosionsradius und Auswirkungen auf Player
	sysTimer.stop();
	if (PlayerHandler.getClientPlayer().getId() == this.ownerId) {
	    PlayerHandler.getClientPlayer().decreasePlacedBombs();
	    Game.changeFieldContent(FieldContent.EMPTY, placedField.xPosition, placedField.yPosition);
	    ConsoleHandler.print("Bomb from Player ID " + this.ownerId + " exploded at (" + placedField.xPosition +
		    			", " + placedField.yPosition + ")", MessageType.GAME);
	    Game.removeBomb(this);
	} else {
	    for (Player i : PlayerHandler.getOpponentPlayers()) {
		if (i.getId() == this.ownerId) {
		    Game.changeFieldContent(FieldContent.EMPTY, placedField.xPosition, placedField.yPosition);
		    ConsoleHandler.print("Bomb from Player ID " + this.ownerId + " exploded at (" + placedField.xPosition +
	    					", " + placedField.yPosition + ")", MessageType.GAME);
		    Game.removeBomb(this);
		}
	    }
	}
    }
}
