/*
 * PlayerMoveButtonConfig
 *
 * Version 0.1
 * 
 * Author: Christopher
 * 
 * Datum: 02.12.2021
 *
 * Spielsteuerung. Tastenzuweisungen können konfiguriert und
 * Aktionen bei Tastendruck definiert werden.
 */

package uni.bombenstimmung.de.game;

import uni.bombenstimmung.de.backend.console.ConsoleHandler;
import uni.bombenstimmung.de.backend.console.MessageType;

public class PlayerMoveButtonConfig {
    
    int up;
    int down;
    int left;
    int right;
    int setBomb;
    
    public void reconfigFull(int up, int down, int left, int right,
	    		     int setBomb) {
	this.up = up;
	this.down = down;
	this.left = left;
	this.right = right;
	this.setBomb = setBomb;
    }
    
    public void reconfigSpecific(int id, int newButton) {
	switch (id) {
	case 0:
	    this.up = newButton;
	    break;
	    
	case 1:
	    this.down = newButton;
	    break;
	    
	case 2:
	    this.left = newButton;
	    break;
	    
	case 3:
	    this.right = newButton;
	    break;
	    
	case 4:
	    this.setBomb = newButton;
	    break;
	    
	default:
	    ConsoleHandler.print("Invalid ID for button reconfiguration!", MessageType.GAME);
	}
    }
    
    public void actionUp() {
	
    }
    
    public void actionDown() {
	
    }
    
    public void actionLeft() {
	
    }
    
    public void actionRight() {
	
    }
    
    public void actionSetBomb() {
	
    }
}
