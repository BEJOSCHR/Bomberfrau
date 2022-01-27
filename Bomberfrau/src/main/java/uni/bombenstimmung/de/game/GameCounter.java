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

import uni.bombenstimmung.de.backend.console.ConsoleHandler;
import uni.bombenstimmung.de.backend.console.MessageType;
import uni.bombenstimmung.de.backend.graphics.GraphicsHandler;
import uni.bombenstimmung.de.backend.language.LanguageBlockType;
import uni.bombenstimmung.de.backend.language.LanguageHandler;
import uni.bombenstimmung.de.menu.Settings;

public class GameCounter implements ActionListener {

    private static int gametime = 240;
    private static int ringOfDeathNumber = 0;
    private Timer gameTimer;
    private static int clock = 0;

    public GameCounter() {
	gameTimer = new Timer(1000, this);
    }

    public void actionPerformed(ActionEvent e) {
	if (Game.getGameOver() == true) {
	    gameTimer.stop();
	    gametime = 0;
	} else {
	    if (gametime == 0 && (ringOfDeathNumber < 6)) {
		ringOfDeathNumber++;
		Game.ringOfDeath(ringOfDeathNumber);
		gametime = 10;
	    } else {
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
	double resScale = (double) GraphicsHandler.getHeight() / 720.0;
	if (ringOfDeathNumber == 0) {
	    GraphicsHandler.drawCentralisedText(g, Color.BLACK, Settings.scaleValue(20f),
		    LanguageHandler.getLLB(LanguageBlockType.LB_INGAME_TIME).getContent() + ": " + gametime, w,
		    h + (int) (250 * resScale));
	} else {
	    GraphicsHandler.drawCentralisedText(g, new Color(204, 0, 0), Settings.scaleValue(20f),
		    LanguageHandler.getLLB(LanguageBlockType.LB_INGAME_ROD1).getContent(), w,
		    h + (int) (200 * resScale));
	    GraphicsHandler.drawCentralisedText(g, new Color(204, 0, 0), Settings.scaleValue(20f),
		    LanguageHandler.getLLB(LanguageBlockType.LB_INGAME_ROD2).getContent(), w,
		    h + (int) (250 * resScale));
	    GraphicsHandler.drawCentralisedText(g, new Color(204, 0, 0), Settings.scaleValue(20f),
		    LanguageHandler.getLLB(LanguageBlockType.LB_INGAME_ROD3).getContent(), w,
		    h + (int) (300 * resScale));
	}
    }

    public static void drawCounterBackground(Graphics g) {
	double resScale = (double) GraphicsHandler.getHeight() / 720.0;
	int xOffset = GraphicsHandler.getWidth() - (GameData.FIELD_DIMENSION * GameData.MAP_DIMENSION);
	int yStart = GameData.MAP_SIDE_BORDER;
	int xStart = (GameData.FIELD_DIMENSION * GameData.MAP_DIMENSION) + (xOffset / 2);

	int rectWidth = xOffset / 2 - 50;

	switch (Game.getMapNumber()) {
	case 1:
	    g.setColor(new Color(130, 170, 62));
	    break;
	    
	case 2:
	    g.setColor(new Color(237, 232, 183));
	    break;
	    
	case 3:
	    g.setColor(new Color(163, 110, 110));
	    break;
	    
	default:
	    ConsoleHandler.print("Wrong MapNumber for drawCounterBackground!", MessageType.GAME);
	}
	
	if (ringOfDeathNumber == 0) {
	    g.fillRect(xStart + 25, yStart + (int) (220 * resScale), rectWidth, (int) (60 * resScale));
	    g.setColor(Color.BLACK);
	    g.drawRect(xStart + 24, yStart + (int) (220 * resScale) - 1, rectWidth, (int) (60 * resScale));
	    g.drawRect(xStart + 25, yStart + (int) (220 * resScale), rectWidth, (int) (60 * resScale));
	    g.drawRect(xStart + 26, yStart + (int) (220 * resScale) + 1, rectWidth, (int) (60 * resScale));
	} else {
	    g.fillRect(xStart + 25, yStart + (int) (150 * resScale), rectWidth, (int) (200 * resScale));
	    g.setColor(Color.BLACK);
	    g.drawRect(xStart + 24, yStart + (int) (150 * resScale) - 1, rectWidth, (int) (200 * resScale));
	    g.drawRect(xStart + 25, yStart + (int) (150 * resScale), rectWidth, (int) (200 * resScale));
	    g.drawRect(xStart + 26, yStart + (int) (150 * resScale) + 1, rectWidth, (int) (200 * resScale));
	}
    }

    public static void resetGameCounter() {
	gametime = 60;
	ringOfDeathNumber = 0;
	clock = 0;
    }
}
