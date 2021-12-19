/*
 * KeyHandler
 *
 * Version 1.0
 * Author: Benni
 *
 * Verwaltet alle Tastendr�cke �ber die jeweiligen Events
 */
package uni.bombenstimmung.de.backend.graphics.subhandler;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import uni.bombenstimmung.de.backend.graphics.DisplayType;
import uni.bombenstimmung.de.backend.graphics.GraphicsHandler;
import uni.bombenstimmung.de.backend.sounds.SoundCategory;
import uni.bombenstimmung.de.backend.sounds.SoundHandler;
import uni.bombenstimmung.de.backend.sounds.SoundType;
import uni.bombenstimmung.de.lobby.OwnButtons;

public class KeyHandler implements KeyListener {

	@Override
	public void keyTyped(KeyEvent e) {}

	@Override
	public void keyPressed(KeyEvent e) {
		
		int keyCode = e.getKeyCode();
		// LOBBY
		if(GraphicsHandler.getDisplayType() == DisplayType.LOBBY) {
			OwnButtons.keyIsPressed(keyCode);
		}
		
		if(keyCode == KeyEvent.VK_W) {
			//W
			
		}else if(keyCode == KeyEvent.VK_A) {
			//A
			
		}else if(keyCode == KeyEvent.VK_S) {
			//S
			
		}else if(keyCode == KeyEvent.VK_D) {
			//D
			
		}
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		
		int keyCode = e.getKeyCode();
		
		// LOBBY
		if(GraphicsHandler.getDisplayType() == DisplayType.LOBBY) {
		    OwnButtons.keyIsReleased(keyCode);
		}
		
		
		if(keyCode == KeyEvent.VK_W) {
			//W
			
		}else if(keyCode == KeyEvent.VK_A) {
			//A
			
		}else if(keyCode == KeyEvent.VK_S) {
			//S
			
		}else if(keyCode == KeyEvent.VK_D) {
			//D
			
		}else if(keyCode == KeyEvent.VK_T) {
			//T  TODO JUST TESTING REMOVE LATER
			SoundHandler.playSound(SoundType.TEST_START);
		}else if(keyCode == KeyEvent.VK_PLUS) {
			//+  TODO JUST TESTING REMOVE LATER
			SoundHandler.changeCategoryVolume(SoundCategory.TEST, 0.1D);
		}else if(keyCode == KeyEvent.VK_MINUS) {
			//-  TODO JUST TESTING REMOVE LATER
			SoundHandler.changeCategoryVolume(SoundCategory.TEST, -0.1D);
		}
		
	}

}
