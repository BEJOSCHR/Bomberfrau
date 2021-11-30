/*
 * inGame
 *
 * Version 1.0
 * Author: Mustafa
 *
 * Timer erstellt
 */


package uni.bombenstimmung.de.game;

public class Timer {

	int minutes;
	int seconds;
	boolean deathRing;
	
	public int startCountdown() {
		return minutes;
		
	}
	
	public boolean activateDeathRing() {
		return deathRing;
	}
	
}
