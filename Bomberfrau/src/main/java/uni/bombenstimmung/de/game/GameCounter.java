package uni.bombenstimmung.de.game;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

public class GameCounter implements ActionListener{
    
    private static int gametime = 25;
    private static int ringOfDeathNumber = 0;
    private Timer gameTimer;
    
    public GameCounter() {
	gameTimer = new Timer(1000,this);
    }
    
    public void actionPerformed(ActionEvent e) {
	GameCounter.gametime--;
	if (gametime == 0 && (ringOfDeathNumber < 6)) {
	    ringOfDeathNumber++;
	    Game.ringOfDeath(ringOfDeathNumber);
	    gametime = 10;
	}
    }
    
    public static int getGameTime() {
	return gametime;
    }
    
    public void startCounter() {
	gameTimer.start();
    }
    
    public static void setRingOfDeathNumber(int up) {
	ringOfDeathNumber = up;
    }
    
    public static int getRingOfDeathNumber() {
	return ringOfDeathNumber;
    }
}
