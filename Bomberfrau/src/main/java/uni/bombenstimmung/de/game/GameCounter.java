/*
 * GameCounter
 *
 * Version 1.0
 * Author: Dennis
 *
 * Malt und verwaltet den Counter und den Ring of Death
 */

package uni.bombenstimmung.de.game;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Graphics;
import java.awt.Color;

import javax.swing.Timer;

import uni.bombenstimmung.de.backend.graphics.GraphicsHandler;
import uni.bombenstimmung.de.backend.language.LanguageBlockType;
import uni.bombenstimmung.de.backend.language.LanguageHandler;
import uni.bombenstimmung.de.menu.Settings;

public class GameCounter implements ActionListener{
    
    private static int gametime = 240;
    private static int ringOfDeathNumber = 0;
    private Timer gameTimer;
    private static int clock = 0;
    
    public GameCounter() {
	gameTimer = new Timer(1000,this);
    }
    
    public void actionPerformed(ActionEvent e) {
	    if(Game.getGameOver() == true) {
		gameTimer.stop();
		gametime = 0;
	    }else {
		if (gametime == 0 && (ringOfDeathNumber < 6)) {
		    ringOfDeathNumber++;
			Game.ringOfDeath(ringOfDeathNumber);
			gametime = 10;
		}else {
		    GameCounter.gametime--;
		}
	    }
	    clock++;
    }
    
    public static int getClock() {
	return clock;
    }
    
    public static int getGameTime() {
	return gametime;
    }
    
    public void startCounter() {
	gameTimer.start();
    }
    
    public void stopCounter() {
	gameTimer.stop();
    }
    
    public void setGameTime(int t) {
	gametime = t;
    }
    
    public static void setRingOfDeathNumber(int up) {
	ringOfDeathNumber = up;
    }
    
    public static int getRingOfDeathNumber() {
	return ringOfDeathNumber;
    }
    
    public static void drawCounter(Graphics g, int w, int h) {
	if (ringOfDeathNumber == 0) {
	    GraphicsHandler.drawCentralisedText(g, Color.RED, Settings.scaleValue(30f), LanguageHandler.getLLB(LanguageBlockType.LB_INGAME_TIME).getContent() + ": " + gametime, w, h+200);
	} else {
	    GraphicsHandler.drawCentralisedText(g, Color.RED, Settings.scaleValue(30f), LanguageHandler.getLLB(LanguageBlockType.LB_INGAME_ROD1).getContent(), w, h+200);
	    GraphicsHandler.drawCentralisedText(g, Color.RED, Settings.scaleValue(30f), LanguageHandler.getLLB(LanguageBlockType.LB_INGAME_ROD2).getContent(), w, h+250);
	    GraphicsHandler.drawCentralisedText(g, Color.RED, Settings.scaleValue(30f), LanguageHandler.getLLB(LanguageBlockType.LB_INGAME_ROD3).getContent(), w, h+300);
	}
    }
    
    public static void drawCounterBackground(Graphics g) {
	int xOffset = GraphicsHandler.getWidth()-(GameData.FIELD_DIMENSION*GameData.MAP_DIMENSION);
	int yStart = GameData.MAP_SIDE_BORDER;
	int xStart = (GameData.FIELD_DIMENSION*GameData.MAP_DIMENSION)+(xOffset/2);
	
	int rectWidth = xOffset/2 - 50;
	
	g.setColor(Color.BLACK);
	g.fillRect(xStart+25, yStart+150, rectWidth, 200);
    }
    
    public static void resetGameCounter() {
	gametime = 240;
	ringOfDeathNumber = 0;
	clock = 0;
    }
}
