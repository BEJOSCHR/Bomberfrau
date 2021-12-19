/*
 * Label
 *
 * Version 1.0
 * Author: Benni
 *
 * Das zentrale Darstellungs-Objekt, von dem alle "Draw-Kommandos" ausgehen
 */
package uni.bombenstimmung.de.backend.graphics;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import uni.bombenstimmung.de.backend.animation.AnimationData;
import uni.bombenstimmung.de.backend.console.ConsoleHandler;
import uni.bombenstimmung.de.backend.console.MessageType;
import uni.bombenstimmung.de.backend.images.ImageHandler;
import uni.bombenstimmung.de.backend.images.ImageType;
import uni.bombenstimmung.de.backend.language.LanguageBlockType;
import uni.bombenstimmung.de.backend.language.LanguageHandler;
import uni.bombenstimmung.de.backend.maa.MouseActionArea;
import uni.bombenstimmung.de.backend.maa.MouseActionAreaHandler;
import uni.bombenstimmung.de.backend.sounds.SoundHandler;
import uni.bombenstimmung.de.backend.sounds.SoundType;
import uni.bombenstimmung.de.game.PlayerHandler;
import uni.bombenstimmung.de.game.Game;

@SuppressWarnings("serial")
public class Label extends JLabel {

	private int displayedFPS = 0;
	private long nextSecond = System.currentTimeMillis() + 1000;
	private int framesInCurrentSecond = 0;
	private int framesInLastSecond = 0;
	private long nextRepaintDelay = 0;
	private int maxFPS = 120;
	private boolean showFPS = true;
	
	public Label() {
		
		this.setBounds(0, 0, GraphicsHandler.getWidth(), GraphicsHandler.getHeight());
		this.setVisible(true);
		GraphicsHandler.getFrame().add(this, BorderLayout.CENTER);
		
	}
	
	/**
	 * Die methode die dauerhaft aufgerufen wird vom {@link JLabel} und somit die FPS representiert
	 * Enth�lt automatische FPS Limitierung
	 * Von hier aus werden �ber das 'g' Komponent alle grafischen Methoden aufgerufen
	 */
	@Override
	protected void paintComponent(Graphics g) {
		
		//MAX FPS GRENZE SCHAFFEN
		long now = System.currentTimeMillis();
		try {
		   if (nextRepaintDelay > now) {
			   Thread.sleep(nextRepaintDelay - now);
		   }
		   nextRepaintDelay = now + 1000/(maxFPS-44);
		} catch (InterruptedException e) { }
		
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		//(Reihenfolge der Aufrufe ist wichtig, sp�tere Aufrufe �berschreiben fr�here)
		
		//BACKGROUND
		g.setColor(Color.DARK_GRAY);
		g.fillRect(0, 0, GraphicsHandler.getWidth(), GraphicsHandler.getHeight());
		
		//PARTS
		switch(GraphicsHandler.getDisplayType()) {
		case INTRO:
		    	g.setColor(Color.WHITE);
			g.fillRect(0, 0, GraphicsHandler.getWidth(), GraphicsHandler.getHeight());
		    	//SoundHandler.playSound(SoundType.INTRO, false);
		    	//SoundHandler.playSound(SoundType.TEST_START, false);
		    	Image img = Toolkit.getDefaultToolkit().createImage("src\\main\\resources\\images\\logo_white.png");
		    	g.drawImage(img, 0, 0, null);
			GraphicsHandler.drawCentralisedText(g, Color.RED, 20+AnimationData.loadingScreen_textSizeModifier, LanguageHandler.getLLB(LanguageBlockType.LB_INTRO_SKIP).getContent(), (int)(GraphicsHandler.getWidth()*0.85), (int)(GraphicsHandler.getHeight()*0.95));
			break;
		case LOADINGSCREEN:
			GraphicsHandler.drawCentralisedText(g, Color.WHITE, 160, LanguageHandler.getLLB(LanguageBlockType.LB_LOADINGSCREEN_TITLE).getContent(), GraphicsHandler.getWidth()/2+AnimationData.loadingScreen_posXModifier, GraphicsHandler.getHeight()/2-30+AnimationData.loadingScreen_posYModifier);
			GraphicsHandler.drawCentralisedText(g, Color.WHITE, 15+AnimationData.loadingScreen_textSizeModifier, LanguageHandler.getLLB(LanguageBlockType.LB_LOADINGSCREEN_CONTINUE).getContent(), GraphicsHandler.getWidth()/2, GraphicsHandler.getHeight()/2+150);
			GraphicsHandler.drawCentralisedText(g, Color.WHITE, 12, LanguageHandler.getLLB(LanguageBlockType.LB_LOADINGSCREEN_AUTHOR).getContent(), GraphicsHandler.getWidth()/2, GraphicsHandler.getHeight()-30);
			GraphicsHandler.drawCentralisedText(g, Color.WHITE, 12, LanguageHandler.getLLB(LanguageBlockType.LB_LOADINGSCREEN_VERSION).getContent(), GraphicsHandler.getWidth()/2, GraphicsHandler.getHeight()-15);
			break;
		case MENU:
			GraphicsHandler.drawCentralisedText(g, Color.WHITE, 180, "MENU", GraphicsHandler.getWidth()/2, GraphicsHandler.getHeight()/2-30);
			break;
		case LOBBY:
			GraphicsHandler.drawCentralisedText(g, Color.WHITE, 180, "LOBBY", GraphicsHandler.getWidth()/2, GraphicsHandler.getHeight()/2-30);
			break;
		case INGAME:
			//GraphicsHandler.drawCentralisedText(g, Color.WHITE, 180, "INGAME", GraphicsHandler.getWidth()/2, GraphicsHandler.getHeight()/2-30);
		    	Game.fillMap();
		    	Game.updateMap(1);
		    	Game.drawGame(g);
		    	PlayerHandler.drawPlayers(g);
			break;
		case AFTERGAME:
			GraphicsHandler.drawCentralisedText(g, Color.WHITE, 180, "AFTERGAME", GraphicsHandler.getWidth()/2, GraphicsHandler.getHeight()/2-30);
			break;
		default:
			ConsoleHandler.print("Illegal displayType! Can't draw for type '"+GraphicsHandler.getDisplayType()+"'", MessageType.ERROR);
			break;
		}
		
		//MAA
		for(MouseActionArea maa : MouseActionAreaHandler.getMAAs()) {
			if(maa.isActiv()) {
				maa.draw(g);
			}
		}
		
		//DRAW FPS
		if(showFPS == true) {
			g.setColor(Color.DARK_GRAY);
			g.setFont(new Font("Arial", Font.BOLD, 10));
			g.drawString(""+getCurrentFPSValue(), 0+3, 0+12);
		}
		
		//CALCULATE FPS
		calculateFPS();
		
		repaint();
		
	}
	
	/**
	 * Berechnet und updatet die FPS
	 */
	private void calculateFPS() {
		long currentTime = System.currentTimeMillis();
		if(currentTime > nextSecond) {
			nextSecond += 1000;
			framesInLastSecond = framesInCurrentSecond;
			framesInCurrentSecond = 0;
		}
		framesInCurrentSecond++;
		displayedFPS = framesInLastSecond;
	}
	
	/**
	 * Gibt die derzeitigen FPS an
	 * @return {@link Integer}, die derzeitigen FPS
	 */
	public int getCurrentFPSValue() {
		return displayedFPS;
	}
	
	public void setShowFPS(boolean showFPS) {
		this.showFPS = showFPS;
	}
	public boolean isShowingFPS() {
		return showFPS;
	}
	
}
