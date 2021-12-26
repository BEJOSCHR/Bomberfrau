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
import java.awt.RenderingHints;

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

import uni.bombenstimmung.de.game.PlayerHandler;
import uni.bombenstimmung.de.menu.Settings;
import uni.bombenstimmung.de.game.Game;

@SuppressWarnings("serial")
public class Label extends JLabel {

	private int displayedFPS = 0;
	private long nextSecond = System.currentTimeMillis() + 1000;
	private int framesInCurrentSecond = 0;
	private int framesInLastSecond = 0;
	private long nextRepaintDelay = 0;
	private int maxFPS = 120;
	private boolean showFPS;
	
	public Label() {
		
		this.setBounds(0, 0, Settings.res_width, Settings.res_height);
		this.showFPS = Settings.show_fps;
		this.setVisible(true);
		GraphicsHandler.getFrame().add(this, BorderLayout.CENTER);
		
	}
	
	/**
	 * Die methode die dauerhaft aufgerufen wird vom {@link JLabel} und somit die FPS representiert
	 * Enthï¿½lt automatische FPS Limitierung
	 * Von hier aus werden ï¿½ber das 'g' Komponent alle grafischen Methoden aufgerufen
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
		
		//(Reihenfolge der Aufrufe ist wichtig, spï¿½tere Aufrufe ï¿½berschreiben frï¿½here)
		
		//BACKGROUND
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, Settings.res_width, Settings.res_height);
		
		//PARTS
		switch(GraphicsHandler.getDisplayType()) {
		case LOADINGSCREEN:
			break;
		case INTRO:
		    	g.drawImage(ImageHandler.getImage(ImageType.IMAGE_INTRO_PIC).getImage(), (int)(Settings.res_width*(1-AnimationData.intro_zoom)/2), (int)(Settings.res_height*(1-AnimationData.intro_zoom)/2),
		    		(int)(Settings.res_width*AnimationData.intro_zoom), (int)(Settings.res_height*AnimationData.intro_zoom), null);
		    	double factor = 0.9;
		    	if (Settings.lang_nr == 1) factor = 0.85;
		    	GraphicsHandler.drawCentralisedText(g, Color.GRAY, 20+AnimationData.intro_skip_text, LanguageHandler.getLLB(LanguageBlockType.LB_INTRO_SKIP).getContent(),
		    		(int)(Settings.res_width*factor), (int)(Settings.res_height*0.95));
		    	break;
		case MENU:
		    	g.drawImage(ImageHandler.getImage(ImageType.IMAGE_MENU_PIC).getImage(), 0, 0, Settings.res_width, Settings.res_height, null);			
		    	GraphicsHandler.drawRightText(g, Color.BLACK, (int)(30*Settings.factor), "Name:", (int)(Settings.res_width*0.5), (int)(Settings.res_height*0.48));
		    	if (!Settings.create_selected)
		    		GraphicsHandler.drawRightText(g, Color.BLACK, 30,   "IP:", (int)(Settings.res_width*0.5), (int)(Settings.res_height*0.58));
		    	break;
		case OPTIONS:
		    	g.drawImage(ImageHandler.getImage(ImageType.IMAGE_OPTIONS_PIC).getImage(), 0, 0, Settings.res_width, Settings.res_height, null);
		    	GraphicsHandler.drawLeftText(g, Color.RED, (int)(30*Settings.factor), LanguageHandler.getLLB(LanguageBlockType.LB_OPT_TXT1).getContent(), (int)(Settings.res_width*0.1), (int)(Settings.res_height*0.13));
		    	GraphicsHandler.drawLeftText(g, Color.RED, (int)(30*Settings.factor), LanguageHandler.getLLB(LanguageBlockType.LB_OPT_TXT2).getContent(), (int)(Settings.res_width*0.1), (int)(Settings.res_height*0.24));
		    	GraphicsHandler.drawLeftText(g, Color.RED, (int)(30*Settings.factor), LanguageHandler.getLLB(LanguageBlockType.LB_OPT_TXT3).getContent(), (int)(Settings.res_width*0.1), (int)(Settings.res_height*0.34));
		    	GraphicsHandler.drawLeftText(g, Color.RED, (int)(30*Settings.factor), LanguageHandler.getLLB(LanguageBlockType.LB_OPT_TXT4).getContent(), (int)(Settings.res_width*0.1), (int)(Settings.res_height*0.44));
		    	GraphicsHandler.drawCentralisedText(g, Color.BLACK, (int)(30*Settings.factor), LanguageHandler.getLLB(LanguageBlockType.LB_OPT_TXT5).getContent(), (int)(Settings.res_width*0.541), (int)(Settings.res_height*0.51));
		    	GraphicsHandler.drawLeftText(g, Color.RED, (int)(30*Settings.factor), LanguageHandler.getLLB(LanguageBlockType.LB_OPT_TXT7).getContent(), (int)(Settings.res_width*0.1), (int)(Settings.res_height*0.83));
			break;
		case LOBBY:
			GraphicsHandler.drawCentralisedText(g, Color.RED, 180, "LOBBY", Settings.res_width/2, Settings.res_height/2-30);
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
			g.setFont(new Font("Arial", Font.BOLD, (int)(15*Settings.factor)));
			//g.drawString(""+getCurrentFPSValue(), 0+3, 0+12);
			
			// schöner durch ersetzen führender Nuller durch Leerzeichen
			g.drawString(""+("  ").repeat(3-String.valueOf(getCurrentFPSValue()).length())+getCurrentFPSValue(), 0+3, 0+12);
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
