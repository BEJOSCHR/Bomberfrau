/*
 * KeyHandler
 *
 * Version 1.0
 * Author: Benni
 *
 * Verwaltet alle Tastendrücke über die jeweiligen Events
 */
package uni.bombenstimmung.de.backend.graphics.subhandler;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {

	@Override
	public void keyTyped(KeyEvent e) {}

	@Override
	public void keyPressed(KeyEvent e) {
		
		int keyCode = e.getKeyCode();
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

}
