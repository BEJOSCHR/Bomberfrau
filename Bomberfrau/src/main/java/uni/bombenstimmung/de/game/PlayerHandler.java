/*
 * PlayerHandler
 *
 * Version 0.4.1
 * 
 * Author: Christopher
 * 
 * Datum: 12.12.2021
 *
 * Verwaltet die erzeugten Player und deren Steuerung
 */

package uni.bombenstimmung.de.game;

import uni.bombenstimmung.de.backend.console.ConsoleHandler;
import uni.bombenstimmung.de.backend.console.MessageType;

public class PlayerHandler {
    
    static Player clientPlayer = new Player("Bob");
    static boolean buttonPressed = false;
    
    public static void handleKeyEventPressed(int keyCode) {
	if (clientPlayer.getDead() == false && buttonPressed == false) {
	    if (keyCode == clientPlayer.getCurrentButtonConfig().getUp()) {
		ConsoleHandler.print("Client presses Button 'up'", MessageType.GAME);
		// TODO: Aktion hoch Start
		buttonPressed = true;
	    } else if (keyCode == clientPlayer.getCurrentButtonConfig().getDown()) {
		ConsoleHandler.print("Client presses Button 'down'", MessageType.GAME);
		// TODO: Aktion runter Start
		buttonPressed = true;
	    } else if (keyCode == clientPlayer.getCurrentButtonConfig().getLeft()) {
		ConsoleHandler.print("Client presses Button 'left'", MessageType.GAME);
		// TODO: Aktion links Start
		buttonPressed = true;
	    } else if (keyCode == clientPlayer.getCurrentButtonConfig().getRight()) {
		ConsoleHandler.print("Client presses Button 'right'", MessageType.GAME);
		// TODO: Aktion rechts Start
		buttonPressed = true;
	    } else if (keyCode == clientPlayer.getCurrentButtonConfig().getSetBomb()) {
		ConsoleHandler.print("Client presses Button 'setBomb'", MessageType.GAME);
		// TODO: Aktion Bombe legen Start
		buttonPressed = true;
	    }
	}
    }
    
    public static void handleKeyEventReleased(int keyCode) {
	if (clientPlayer.getDead() == false && buttonPressed == true) {
	    if (keyCode == clientPlayer.getCurrentButtonConfig().getUp()) {
		ConsoleHandler.print("Client released Button 'up'", MessageType.GAME);
		// TODO: Aktion hoch Stop
		buttonPressed = false;
	    } else if (keyCode == clientPlayer.getCurrentButtonConfig().getDown()) {
		ConsoleHandler.print("Client released Button 'down'", MessageType.GAME);
		// TODO: Aktion runter Stop
		buttonPressed = false;
	    } else if (keyCode == clientPlayer.getCurrentButtonConfig().getLeft()) {
		ConsoleHandler.print("Client released Button 'left'", MessageType.GAME);
		// TODO: Aktion links Stop
		buttonPressed = false;
	    } else if (keyCode == clientPlayer.getCurrentButtonConfig().getRight()) {
		ConsoleHandler.print("Client released Button 'right'", MessageType.GAME);
		// TODO: Aktion rechts Stop
		buttonPressed = false;
	    } else if (keyCode == clientPlayer.getCurrentButtonConfig().getSetBomb()) {
		ConsoleHandler.print("Client released Button 'setBomb'", MessageType.GAME);
		// TODO: Aktion Bombe legen Stop
		buttonPressed = false;
	    }
	}
    }

}
