/*
 * PlayerButtonConfig
 *
 * Version 0.4.1
 * 
 * Author: Christopher
 * 
 * Datum: 08.12.2021
 *
 * Spielsteuerung. Tastenzuweisungen können konfiguriert und
 * Aktionen bei Tastendruck definiert werden.
 */

package uni.bombenstimmung.de.game;

import java.awt.event.KeyEvent;

import uni.bombenstimmung.de.backend.console.ConsoleHandler;
import uni.bombenstimmung.de.backend.console.MessageType;

public class PlayerButtonConfig {
    
    private int up;
    private int down;
    private int left;
    private int right;
    private int setBomb;
    
    /* Standard ButtonConfig Initialisierung. */
    public PlayerButtonConfig() {
	this.up = KeyEvent.VK_W;
	this.down = KeyEvent.VK_S;
	this.left = KeyEvent.VK_A;
	this.right = KeyEvent.VK_D;
	this.setBomb = KeyEvent.VK_SPACE;
    }
    
    /* Individuelle ButtonConfig Initialisierung. */
    public PlayerButtonConfig(int up, int down, int left, int right,
		     		int setBomb) {
	this.up = up;
	this.down = down;
	this.left = left;
	this.right = right;
	this.setBomb = setBomb;
    }
    
    public int getUp() {
	return up;
    }
    
    public int getDown() {
	return down;
    }
    
    public int getLeft() {
	return left;
    }
    
    public int getRight() {
	return right;
    }
    
    public int getSetBomb() {
	return setBomb;
    }
    
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
}
