/*
 * Label
 *
 * Version 1.0
 * Author: Benni
 *
 * Das zentrale Darstellungs-Objekt, von dem alle "Draw-Kommandos" ausgehen
 */
package uni.bombenstimmung.de.backend.graphics;

import java.util.ConcurrentModificationException;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JLabel;

import uni.bombenstimmung.de.aftergame.DeadPlayerHandler;
import uni.bombenstimmung.de.backend.animation.AnimationData;
import uni.bombenstimmung.de.backend.console.ConsoleHandler;
import uni.bombenstimmung.de.backend.console.MessageType;
import uni.bombenstimmung.de.backend.images.ImageHandler;
import uni.bombenstimmung.de.backend.images.ImageType;
import uni.bombenstimmung.de.backend.language.LanguageBlockType;
import uni.bombenstimmung.de.backend.language.LanguageHandler;
import uni.bombenstimmung.de.backend.maa.MouseActionArea;
import uni.bombenstimmung.de.backend.maa.MouseActionAreaHandler;
import uni.bombenstimmung.de.game.Game;
import uni.bombenstimmung.de.game.GameCounter;
import uni.bombenstimmung.de.game.PlayerHandler;
import uni.bombenstimmung.de.lobby.LobbyCreate;
import uni.bombenstimmung.de.menu.Settings;

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

	this.setBounds(0, 0, Settings.getResWidth(), Settings.getResHeight());
	this.showFPS = Settings.getShowFps();
	this.setVisible(true);
	GraphicsHandler.getFrame().add(this, BorderLayout.CENTER);

    }

    /**
     * Die methode die dauerhaft aufgerufen wird vom {@link JLabel} und somit die
     * FPS representiert Enth�lt automatische FPS Limitierung Von hier aus werden
     * �ber das 'g' Komponent alle grafischen Methoden aufgerufen
     */
    @Override
    protected void paintComponent(Graphics g) {

	// MAX FPS GRENZE SCHAFFEN
	long now = System.currentTimeMillis();
	try {
	    if (nextRepaintDelay > now) {
		Thread.sleep(nextRepaintDelay - now);
	    }
	    nextRepaintDelay = now + 1000 / (maxFPS - 41);
	} catch (InterruptedException e) {
	}

	super.paintComponent(g);
	Graphics2D g2d = (Graphics2D) g;
	g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

	// (Reihenfolge der Aufrufe ist wichtig, sp�tere Aufrufe �berschreiben
	// fr�here)

	// BACKGROUND
	g.setColor(Color.WHITE);
	g.fillRect(0, 0, Settings.getResWidth(), Settings.getResHeight());

	// PARTS
	switch (GraphicsHandler.getDisplayType()) {
	case LOADINGSCREEN:
	    break;
	case INTRO:
	    g.drawImage(ImageHandler.getImage(ImageType.IMAGE_INTRO_PIC).getImage(),
		    (int) (Settings.getResWidth() * (1 - AnimationData.intro_zoom) / 2),
		    (int) (Settings.getResHeight() * (1 - AnimationData.intro_zoom) / 2),
		    (int) (Settings.getResWidth() * AnimationData.intro_zoom),
		    (int) (Settings.getResHeight() * AnimationData.intro_zoom), null);
	    double factor = 0.9;
	    if (Settings.getLangNr() == 1)
		factor = 0.85;
	    GraphicsHandler.drawCentralisedText(g, Color.GRAY, 20 + AnimationData.intro_skip_text,
		    LanguageHandler.getLLB(LanguageBlockType.LB_INTRO_SKIP).getContent(),
		    (int) (Settings.getResWidth() * factor), (int) (Settings.getResHeight() * 0.95));
	    break;
	case MENU:
	    g.drawImage(ImageHandler.getImage(ImageType.IMAGE_MENU_PIC).getImage(), 0, 0, Settings.getResWidth(),
		    Settings.getResHeight(), null);
	    // Titel pusliert
	    g.drawImage(ImageHandler.getImage(ImageType.IMAGE_MENU_TITLE).getImage(),
		    (int) (Settings.getResWidth() * 0.05), (int) (Settings.getResHeight() * 0.05),
		    (int) (2100 * Settings.getResWidth() / 3840 + 3 * AnimationData.title_Modifier),
		    (int) (700 * Settings.getResHeight() / 2160 + AnimationData.title_Modifier), null);

	    GraphicsHandler.drawRightText(g, Color.BLACK, 30, "Name:", (int) (Settings.getResWidth() * 0.48),
		    (int) (Settings.getResHeight() * 0.48));
	    if (!Settings.getCreateSelected())
		GraphicsHandler.drawRightText(g, Color.BLACK, 30, "IP:", (int) (Settings.getResWidth() * 0.48),
			(int) (Settings.getResHeight() * 0.58));
	    break;
	case OPTIONS:
	    g.drawImage(ImageHandler.getImage(ImageType.IMAGE_MENU_PIC).getImage(), 0, 0, Settings.getResWidth(),
		    Settings.getResHeight(), null);
	    GraphicsHandler.drawLeftText(g, Color.RED, (int) (30 * Settings.getFactor()),
		    LanguageHandler.getLLB(LanguageBlockType.LB_OPT_TXT1).getContent(),
		    (int) (Settings.getResWidth() * 0.1), (int) (Settings.getResHeight() * 0.13));
	    GraphicsHandler.drawLeftText(g, Color.RED, (int) (30 * Settings.getFactor()),
		    LanguageHandler.getLLB(LanguageBlockType.LB_OPT_TXT2).getContent(),
		    (int) (Settings.getResWidth() * 0.1), (int) (Settings.getResHeight() * 0.24));
	    GraphicsHandler.drawLeftText(g, Color.RED, (int) (30 * Settings.getFactor()),
		    LanguageHandler.getLLB(LanguageBlockType.LB_OPT_TXT3).getContent(),
		    (int) (Settings.getResWidth() * 0.1), (int) (Settings.getResHeight() * 0.34));
	    GraphicsHandler.drawLeftText(g, Color.RED, (int) (30 * Settings.getFactor()),
		    LanguageHandler.getLLB(LanguageBlockType.LB_OPT_TXT4).getContent(),
		    (int) (Settings.getResWidth() * 0.1), (int) (Settings.getResHeight() * 0.44));
	    GraphicsHandler.drawCentralisedText(g, Color.BLACK, (int) (30 * Settings.getFactor()),
		    LanguageHandler.getLLB(LanguageBlockType.LB_OPT_TXT5).getContent(),
		    (int) (Settings.getResWidth() * 0.541), (int) (Settings.getResHeight() * 0.51));
	    GraphicsHandler.drawLeftText(g, Color.RED, (int) (30 * Settings.getFactor()),
		    LanguageHandler.getLLB(LanguageBlockType.LB_OPT_TXT7).getContent(),
		    (int) (Settings.getResWidth() * 0.1), (int) (Settings.getResHeight() * 0.83));
	    break;

	case LOBBY:
	    LobbyCreate.drawScreen(g);
	    break;

	case INGAME:
	    Game.drawBackground(g, Game.getMapNumber());
	    GameCounter.drawCounterBackground(g);
	    Game.drawRightPartOfMap(g, Game.getMapNumber(), GameCounter.getGameTime());
	    Game.drawLeftPartOfMap(g, PlayerHandler.getPlayerAmount());
	    Game.drawGame(g);
	    PlayerHandler.drawPlayers(g);
	    if (PlayerHandler.getDebugKeysState()) {
		GraphicsHandler.drawCentralisedText(g, Color.WHITE, 10,
			"DisplayPos X: " + PlayerHandler.getClientPlayer().getPosition().getX() + " Y: "
				+ PlayerHandler.getClientPlayer().getPosition().getY(),
			200, 24);
		GraphicsHandler.drawCentralisedText(g, Color.WHITE, 10,
			"RealPos X: " + PlayerHandler.getClientPlayer().getRealPosX() + " Y: "
				+ PlayerHandler.getClientPlayer().getRealPosY(),
			200, 36);
		GraphicsHandler.drawCentralisedText(g, Color.WHITE, 10,
			"VelX: " + PlayerHandler.getClientPlayer().getVelX() + " VelY: "
				+ PlayerHandler.getClientPlayer().getVelY(),
			200, 48);
	    }
	    if (Game.getGameOver()) {
		if (Game.getLastPlayer() == -1) {
		    GraphicsHandler.drawCentralisedText(g, Color.BLACK, Settings.scaleValue(180),
			    LanguageHandler.getLLB(LanguageBlockType.LB_INGAME_NO_SURVIVORS).getContent(),
			    (int) (GraphicsHandler.getWidth() * 0.5) + AnimationData.title_posXModifier,
			    (int) (GraphicsHandler.getHeight() * 0.3) + AnimationData.title_posYModifier);
		    GraphicsHandler.drawCentralisedText(g, Color.BLACK, Settings.scaleValue(250),
			    LanguageHandler.getLLB(LanguageBlockType.LB_INGAME_GAMEOVER).getContent(),
			    (int) (GraphicsHandler.getWidth() * 0.5) + AnimationData.title_posXModifier,
			    (int) (GraphicsHandler.getHeight() * 0.7) + AnimationData.title_posYModifier);
		} else {
		    String name = PlayerHandler.getAllPlayer().get(Game.getLastPlayer()).getName();
		    GraphicsHandler.drawCentralisedText(g, Color.YELLOW,
			    Settings.scaleValue(500 * (1f - name.length() * 0.026f)), name,
			    (int) (GraphicsHandler.getWidth() * 0.5), (int) (GraphicsHandler.getHeight() * 0.3));
		    GraphicsHandler.drawCentralisedText(g, Color.YELLOW, Settings.scaleValue(250),
			    LanguageHandler.getLLB(LanguageBlockType.LB_INGAME_WINS).getContent(),
			    (int) (GraphicsHandler.getWidth() * 0.5), (int) (GraphicsHandler.getHeight() * 0.7));
		}
	    }
	    switch (Game.getCountdown()) {
	    case 1:
		GraphicsHandler.drawCentralisedText(g, Color.RED, Settings.scaleValue(800), "3",
			GraphicsHandler.getWidth() / 2, GraphicsHandler.getHeight() / 2);
		break;

	    case 2:
		GraphicsHandler.drawCentralisedText(g, Color.ORANGE, Settings.scaleValue(800), "2",
			GraphicsHandler.getWidth() / 2, GraphicsHandler.getHeight() / 2);
		break;

	    case 3:
		GraphicsHandler.drawCentralisedText(g, Color.YELLOW, Settings.scaleValue(800), "1",
			GraphicsHandler.getWidth() / 2, GraphicsHandler.getHeight() / 2);
		break;

	    case 4:
		GraphicsHandler.drawCentralisedText(g, Color.GREEN, Settings.scaleValue(800),
			LanguageHandler.getLLB(LanguageBlockType.LB_INGAME_GO).getContent(),
			GraphicsHandler.getWidth() / 2, GraphicsHandler.getHeight() / 2);
	    }
	    break;
	case AFTERGAME:
	    g.setColor(Color.WHITE);
	    Color gold = new Color(255, 204, 51);
	    String name;
	    GraphicsHandler.drawCentralisedText(g, Color.BLACK, Settings.scaleValue(100),
		    LanguageHandler.getLLB(LanguageBlockType.LB_AFTERGAME_TITEL).getContent(),
		    GraphicsHandler.getWidth() / 2 + AnimationData.title_posYModifier,
		    GraphicsHandler.getHeight() / 16 + AnimationData.title_posXModifier);
	    name = LanguageHandler.getLLB(LanguageBlockType.LB_AFTERGAME_NAME_1).getContent();
	    GraphicsHandler.drawLeftText(g, gold.darker(), Settings.scaleValue(75 - name.length()), name,
		    GraphicsHandler.getWidth() * 1 / 4, GraphicsHandler.getHeight() * 2 / 8);
	    name = LanguageHandler.getLLB(LanguageBlockType.LB_AFTERGAME_NAME_2).getContent();
	    GraphicsHandler.drawLeftText(g, gold.darker(), Settings.scaleValue(75 - name.length()), name,
		    GraphicsHandler.getWidth() * 1 / 4, GraphicsHandler.getHeight() * 3 / 8);
	    name = LanguageHandler.getLLB(LanguageBlockType.LB_AFTERGAME_NAME_3).getContent();
	    GraphicsHandler.drawLeftText(g, gold.darker(), Settings.scaleValue(75 - name.length()), name,
		    GraphicsHandler.getWidth() * 1 / 4, GraphicsHandler.getHeight() * 4 / 8);
	    name = LanguageHandler.getLLB(LanguageBlockType.LB_AFTERGAME_NAME_4).getContent();
	    GraphicsHandler.drawLeftText(g, gold.darker(), Settings.scaleValue(75 - name.length()), name,
		    GraphicsHandler.getWidth() * 1 / 4, GraphicsHandler.getHeight() * 5 / 8);
	    GraphicsHandler.drawLeftText(g, gold.darker(), Settings.scaleValue(70),
		    LanguageHandler.getLLB(LanguageBlockType.LB_AFTERGAME_SCORE_1).getContent(),
		    GraphicsHandler.getWidth() * 3 / 4, GraphicsHandler.getHeight() * 2 / 8);
	    GraphicsHandler.drawLeftText(g, gold.darker(), Settings.scaleValue(70),
		    LanguageHandler.getLLB(LanguageBlockType.LB_AFTERGAME_SCORE_2).getContent(),
		    GraphicsHandler.getWidth() * 3 / 4, GraphicsHandler.getHeight() * 3 / 8);
	    GraphicsHandler.drawLeftText(g, gold.darker(), Settings.scaleValue(70),
		    LanguageHandler.getLLB(LanguageBlockType.LB_AFTERGAME_SCORE_3).getContent(),
		    GraphicsHandler.getWidth() * 3 / 4, GraphicsHandler.getHeight() * 4 / 8);
	    GraphicsHandler.drawLeftText(g, gold.darker(), Settings.scaleValue(70),
		    LanguageHandler.getLLB(LanguageBlockType.LB_AFTERGAME_SCORE_4).getContent(),
		    GraphicsHandler.getWidth() * 3 / 4, GraphicsHandler.getHeight() * 5 / 8);
	    GraphicsHandler.drawLeftText(g, gold.darker(), Settings.scaleValue(70),
		    LanguageHandler.getLLB(LanguageBlockType.LB_AFTERGAME_SCORE_1).getContent(),
		    GraphicsHandler.getWidth() * 3 / 4, GraphicsHandler.getHeight() * 2 / 8);
	    GraphicsHandler.drawLeftText(g, Color.RED, Settings.scaleValue(30),
		    LanguageHandler.getLLB(LanguageBlockType.LB_AFTERGAME_PLUS_1).getContent(),
		    GraphicsHandler.getWidth() * 5 / 6, GraphicsHandler.getHeight() * 2 / 8);
	    GraphicsHandler.drawLeftText(g, Color.RED, Settings.scaleValue(30),
		    LanguageHandler.getLLB(LanguageBlockType.LB_AFTERGAME_PLUS_2).getContent(),
		    GraphicsHandler.getWidth() * 5 / 6, GraphicsHandler.getHeight() * 3 / 8);
	    GraphicsHandler.drawLeftText(g, Color.RED, Settings.scaleValue(30),
		    LanguageHandler.getLLB(LanguageBlockType.LB_AFTERGAME_PLUS_3).getContent(),
		    GraphicsHandler.getWidth() * 5 / 6, GraphicsHandler.getHeight() * 4 / 8);
	    GraphicsHandler.drawLeftText(g, Color.RED, Settings.scaleValue(30),
		    LanguageHandler.getLLB(LanguageBlockType.LB_AFTERGAME_PLUS_4).getContent(),
		    GraphicsHandler.getWidth() * 5 / 6, GraphicsHandler.getHeight() * 5 / 8);
	    DeadPlayerHandler.drawImages(g);
	    break;
	default:
	    ConsoleHandler.print("Illegal displayType! Can't draw for type '" + GraphicsHandler.getDisplayType() + "'",
		    MessageType.ERROR);
	    break;
	}

	// MAA
	try {
	    for (MouseActionArea maa : MouseActionAreaHandler.getMAAs()) {
		if (maa.isActiv()) {
		    maa.draw(g);
		}
	    }
	} catch (ConcurrentModificationException error) {
	}

	// DRAW FPS
	if (showFPS == true) {
	    g.setColor(Color.DARK_GRAY);
	    g.setFont(new Font("Arial", Font.BOLD, (int) (15 * Settings.getFactor())));
	    g.drawString("" + getCurrentFPSValue(), 0 + 3, 0 + 12);
	}

	// CALCULATE FPS
	calculateFPS();

	repaint();

    }

    /**
     * Berechnet und updatet die FPS
     */
    private void calculateFPS() {
	long currentTime = System.currentTimeMillis();
	if (currentTime > nextSecond) {
	    nextSecond += 1000;
	    framesInLastSecond = framesInCurrentSecond;
	    framesInCurrentSecond = 0;
	}
	framesInCurrentSecond++;
	displayedFPS = framesInLastSecond;
    }

    /**
     * Gibt die derzeitigen FPS an
     * 
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
