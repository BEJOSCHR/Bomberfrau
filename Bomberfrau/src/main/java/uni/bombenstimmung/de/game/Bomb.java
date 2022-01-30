/*
 * Bomb
 *
 * Version 3.0
 * Author: Dennis
 *
 * Verwaltet die Bombe im Spiel
 */

package uni.bombenstimmung.de.game;

import java.awt.Graphics;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.ArrayList;

import javax.swing.Timer;

import uni.bombenstimmung.de.backend.console.ConsoleHandler;
import uni.bombenstimmung.de.backend.console.MessageType;
import uni.bombenstimmung.de.backend.graphics.GraphicsHandler;
import uni.bombenstimmung.de.backend.sounds.SoundHandler;
import uni.bombenstimmung.de.backend.sounds.SoundType;

public class Bomb implements ActionListener {

    private int radius;
    private int timer;
    private int ownerId;
    private Timer sysTimer;
    private int counter;
    private Field placedField;
    private ArrayList<Wall> targetedWalls;

    private boolean fuse = false;
    private boolean grow;
    private double scale;

    /**
     * Platziert die Bombe auf einem bestimmten Feld
     * 
     * @param r,       Radius der Explosion
     * @param t,       Zeit bis zur Ecplosion
     * @param ownerId, wer die Bombe legt
     */
    public Bomb(int r, int t, int ownerId) {
	this.radius = r;
	this.timer = t;
	this.counter = 0;
	this.ownerId = ownerId;
	if (PlayerHandler.getClientPlayer().getId() == this.ownerId) {
	    placedField = PlayerHandler.getClientPlayer().getCurrentField();
	} else {
	    for (Player i : PlayerHandler.getOpponentPlayers()) {
		if (i.getId() == this.ownerId) {
		    placedField = i.getCurrentField();
		}
	    }
	}
	this.targetedWalls = new ArrayList<Wall>();
	this.grow = false;
	this.scale = 1.0;
	sysTimer = new Timer(100, this);
	sysTimer.start();
    }

    /**
     * Bei Bombenlegung wird ein Zaehler gestartet und dieser wird hochgezaehlt
     * dann werden nacheinander die Explosion erzeugt und anschließend wieder 
     * die Felder frei und begehbar gemacht
     */
    public void actionPerformed(ActionEvent e) {
	this.counter++;

	if (grow) {
	    this.scale += 0.05;
	    if (scale >= 1.0) {
		this.grow = false;
	    }
	} else {
	    this.scale -= 0.05;
	    if (scale <= 0.75) {
		this.grow = true;
	    }
	}

	if (this.counter < this.timer) {
	    if (!fuse)
		SoundHandler.playSound(SoundType.FUSE, false);
	    fuse = true;
	} else if (this.counter == this.timer) {
	    fuse = false;
	    SoundHandler.playSound(SoundType.EXPLOSION, false);
	    this.explodeFire();
	    SoundHandler.stopSound(SoundType.FUSE);
	} else if (this.counter >= this.timer + 20) {
	    this.explode();
	}
    }

    /**
     * Ist zustaendig fuer die Feuer Explosion und tastet die Felder in die vier
     * Himmelsrichtungen Norden, Sueden, Westen und Osten ab. Malt dann die Explosion und prueft
     * auf moegliche Kettenreaktionen die anschließend gezuendet werden.
     */
    public void explodeFire() {

	this.counter = this.timer;

	for (int direction = 0; direction < 5; direction++) {
	    int r = 1;
	    /* Hier wird die Stelle an der die Bombe gelegt wurder geprueft */
	    if (direction == 0) {
		/* Stirbt der eigene Spieler */
		if (PlayerHandler.getClientPlayer().getCurrentField() == Game
			.getFieldFromMap(this.placedField.xPosition, this.placedField.yPosition)) {
		    PlayerHandler.getClientPlayer().setDead(true);
		}
		/* Stirbt ein anderer Spieler */
		for (Player player : PlayerHandler.getOpponentPlayers()) {
		    if (player.getCurrentField() == Game.getFieldFromMap(this.placedField.xPosition,
			    this.placedField.yPosition)) {
			player.setDead(true);
		    }
		}
		/* Die Explosion wird gemalt */
		Game.changeFieldContent(FieldContent.EXPLOSION1, this.placedField.xPosition,
			this.placedField.yPosition);
	    } else if (direction == 1) {
		/* Hier wird der Sueden ueberprueft */
		while (r <= this.radius
			&& (Game.getFieldFromMap(this.placedField.xPosition, this.placedField.yPosition + r)
				.getContent() == FieldContent.WALL
				|| Game.getFieldFromMap(this.placedField.xPosition, this.placedField.yPosition + r)
					.getContent() == FieldContent.EMPTY
				|| Game.getFieldFromMap(this.placedField.xPosition, this.placedField.yPosition + r)
					.getContent() == FieldContent.BOMB
				|| Game.getFieldFromMap(this.placedField.xPosition, this.placedField.yPosition + r)
					.getContent() == FieldContent.UPGRADE_ITEM_BOMB
				|| Game.getFieldFromMap(this.placedField.xPosition, this.placedField.yPosition + r)
					.getContent() == FieldContent.UPGRADE_ITEM_SHOE
				|| Game.getFieldFromMap(this.placedField.xPosition, this.placedField.yPosition + r)
					.getContent() == FieldContent.UPGRADE_ITEM_FIRE
				|| Game.getFieldFromMap(this.placedField.xPosition, this.placedField.yPosition + r)
					.getContent() == FieldContent.EXPLOSION1
				|| Game.getFieldFromMap(this.placedField.xPosition, this.placedField.yPosition + r)
					.getContent() == FieldContent.EXPLOSION2
				|| Game.getFieldFromMap(this.placedField.xPosition, this.placedField.yPosition + r)
					.getContent() == FieldContent.EXPLOSION3_W
				|| Game.getFieldFromMap(this.placedField.xPosition, this.placedField.yPosition + r)
					.getContent() == FieldContent.EXPLOSION3_O)) {
		    /* Erzeugen eines Wall-Objekts fuer Drop-Moeglichkeit eines Upgrades. */
		    if (Game.getFieldFromMap(this.placedField.xPosition, this.placedField.yPosition + r)
			    .getContent() == FieldContent.WALL) {
			targetedWalls.add(new Wall(
				Game.getFieldFromMap(this.placedField.xPosition, this.placedField.yPosition + r)));
			Game.changeFieldContent(FieldContent.EXPLOSION3_S, this.placedField.xPosition,
				this.placedField.yPosition + r);
			break;
		    }
		    /* Stirbt der eigene Spieler */
		    if (PlayerHandler.getClientPlayer().getCurrentField() == Game
			    .getFieldFromMap(this.placedField.xPosition, this.placedField.yPosition + r)) {
			PlayerHandler.getClientPlayer().setDead(true);
		    }
		    /* Stirbt ein anderer Spieler */
		    for (Player player : PlayerHandler.getOpponentPlayers()) {
			if (player.getCurrentField() == Game.getFieldFromMap(this.placedField.xPosition,
				this.placedField.yPosition + r)) {
			    player.setDead(true);
			}
		    }
		    /* Ist untendran eine Bombe, wird eine Kettenreaktion ausgeloest */
		    if (Game.getFieldFromMap(this.placedField.xPosition, this.placedField.yPosition + r)
			    .getContent() == FieldContent.BOMB) {
			chainreaction(this.placedField.xPosition, this.placedField.yPosition + r);
			break;
		    }
		    /* Die erste If-Abfrage prueft auf andere Feuerstrahlen und malt ggf Kreuzungen */
		    if(Game.getFieldFromMap(this.placedField.xPosition, this.placedField.yPosition + r)
				.getContent() == FieldContent.EXPLOSION1
			|| Game.getFieldFromMap(this.placedField.xPosition, this.placedField.yPosition + r)
				.getContent() == FieldContent.EXPLOSION2
			|| Game.getFieldFromMap(this.placedField.xPosition, this.placedField.yPosition + r)
				.getContent() == FieldContent.EXPLOSION3_W
			|| Game.getFieldFromMap(this.placedField.xPosition, this.placedField.yPosition + r)
				.getContent() == FieldContent.EXPLOSION3_O
			|| Game.getFieldFromMap(this.placedField.xPosition, this.placedField.yPosition + r)
				.getContent() == FieldContent.EXPLOSION3_S
			|| Game.getFieldFromMap(this.placedField.xPosition, this.placedField.yPosition + r)
				.getContent() == FieldContent.EXPLOSION2_NS) {
			Game.changeFieldContent(FieldContent.EXPLOSION1, this.placedField.xPosition, this.placedField.yPosition + r);
			r++;
			break;
			/* Die Abzweigung schaut ob ein Rand im naechsten Feld ist und malt die entsprechende Flamme */
		    } else if ((Game.getFieldFromMap(this.placedField.xPosition, this.placedField.yPosition + (r + 1))
			    .getContent() == FieldContent.BORDER
			    || Game.getFieldFromMap(this.placedField.xPosition, this.placedField.yPosition + (r + 1))
				    .getContent() == FieldContent.BLOCK)
			    || (Game.getFieldFromMap(this.placedField.xPosition, this.placedField.yPosition + (r + 1))
				    .getContent() == FieldContent.BOMB && (this.radius - r == 0))
			    || (this.radius - r == 0)) {
			Game.changeFieldContent(FieldContent.EXPLOSION3_S, this.placedField.xPosition,
				this.placedField.yPosition + r);
			r++;
			break;
			/* Wenn kein Sonderfall eintritt, wird eine normale Flamme nach unten gemalt */
		    } else {
			Game.changeFieldContent(FieldContent.EXPLOSION2_NS, this.placedField.xPosition,
				this.placedField.yPosition + r);
		    }
		    r++;
		}
	    } else if (direction == 2) {
		// Stellen noerdlich der Bombe werden ueberprueft auf Spieler, und Explosion
		// wird eingefuegt
		while (r <= this.radius
			&& (Game.getFieldFromMap(this.placedField.xPosition, this.placedField.yPosition - r)
				.getContent() == FieldContent.WALL
				|| Game.getFieldFromMap(this.placedField.xPosition, this.placedField.yPosition - r)
					.getContent() == FieldContent.EMPTY
				|| Game.getFieldFromMap(this.placedField.xPosition, this.placedField.yPosition - r)
					.getContent() == FieldContent.BOMB
				|| Game.getFieldFromMap(this.placedField.xPosition, this.placedField.yPosition - r)
					.getContent() == FieldContent.UPGRADE_ITEM_BOMB
				|| Game.getFieldFromMap(this.placedField.xPosition, this.placedField.yPosition - r)
					.getContent() == FieldContent.UPGRADE_ITEM_SHOE
				|| Game.getFieldFromMap(this.placedField.xPosition, this.placedField.yPosition - r)
					.getContent() == FieldContent.UPGRADE_ITEM_FIRE
				|| Game.getFieldFromMap(this.placedField.xPosition, this.placedField.yPosition - r)
					.getContent() == FieldContent.EXPLOSION1
				|| Game.getFieldFromMap(this.placedField.xPosition, this.placedField.yPosition - r)
					.getContent() == FieldContent.EXPLOSION2
				|| Game.getFieldFromMap(this.placedField.xPosition, this.placedField.yPosition - r)
					.getContent() == FieldContent.EXPLOSION3_W
				|| Game.getFieldFromMap(this.placedField.xPosition, this.placedField.yPosition - r)
					.getContent() == FieldContent.EXPLOSION3_O)) {
		    /* Erzeugen eines Wall-Objekts fuer Drop-Moeglichkeit eines Upgrades. */
		    if (Game.getFieldFromMap(this.placedField.xPosition, this.placedField.yPosition - r)
			    .getContent() == FieldContent.WALL) {
			targetedWalls.add(new Wall(
				Game.getFieldFromMap(this.placedField.xPosition, this.placedField.yPosition - r)));
			Game.changeFieldContent(FieldContent.EXPLOSION3_N, this.placedField.xPosition,
				this.placedField.yPosition - r);
			break;
		    }
		    /* Stirbt der eigene Spieler */
		    if (PlayerHandler.getClientPlayer().getCurrentField() == Game
			    .getFieldFromMap(this.placedField.xPosition, this.placedField.yPosition - r)) {
			PlayerHandler.getClientPlayer().setDead(true);
		    }		
		    /* Stirbt ein anderer Spieler */
		    for (Player player : PlayerHandler.getOpponentPlayers()) {
			if (player.getCurrentField() == Game.getFieldFromMap(this.placedField.xPosition,
				this.placedField.yPosition - r)) {
			    player.setDead(true);
			}
		    }
		    /* Ist obendran eine Bombe, wird eine Kettenreaktion ausgeloest */
		    if (Game.getFieldFromMap(this.placedField.xPosition, this.placedField.yPosition - r)
			    .getContent() == FieldContent.BOMB) {
			chainreaction(this.placedField.xPosition, this.placedField.yPosition - r);
			break;
		    }
		    /* Die erste If-Abfrage prueft auf andere Feuerstrahlen und malt ggf Kreuzungen */
		    if(Game.getFieldFromMap(this.placedField.xPosition , this.placedField.yPosition - r)
				.getContent() == FieldContent.EXPLOSION1
			|| Game.getFieldFromMap(this.placedField.xPosition, this.placedField.yPosition - r)
				.getContent() == FieldContent.EXPLOSION2
			|| Game.getFieldFromMap(this.placedField.xPosition, this.placedField.yPosition - r)
				.getContent() == FieldContent.EXPLOSION2_NS
			|| Game.getFieldFromMap(this.placedField.xPosition, this.placedField.yPosition - r)
				.getContent() == FieldContent.EXPLOSION3_W
			|| Game.getFieldFromMap(this.placedField.xPosition, this.placedField.yPosition - r)
				.getContent() == FieldContent.EXPLOSION3_O
			|| Game.getFieldFromMap(this.placedField.xPosition, this.placedField.yPosition - r)
				.getContent() == FieldContent.EXPLOSION3_S) {
			Game.changeFieldContent(FieldContent.EXPLOSION1, this.placedField.xPosition, this.placedField.yPosition - r);
			r++;
			break;
			/* Die Abzweigung schaut ob ein Rand im naechsten Feld ist und malt die entsprechende Flamme */
		    } else if ((Game.getFieldFromMap(this.placedField.xPosition, this.placedField.yPosition - (r + 1))
			    .getContent() == FieldContent.BORDER
			    || Game.getFieldFromMap(this.placedField.xPosition, this.placedField.yPosition - (r + 1))
				    .getContent() == FieldContent.BLOCK)
			    || (Game.getFieldFromMap(this.placedField.xPosition, this.placedField.yPosition - (r + 1))
				    .getContent() == FieldContent.BOMB && (this.radius - r == 0))
			    || (this.radius - r == 0)) {
			Game.changeFieldContent(FieldContent.EXPLOSION3_N, this.placedField.xPosition,
				this.placedField.yPosition - r);
			r++;
			break;
			/* Wenn kein Sonderfall eintritt, wird eine normale Flamme nach unten gemalt */
		    } else {
			Game.changeFieldContent(FieldContent.EXPLOSION2_NS, this.placedField.xPosition,
				this.placedField.yPosition - r);
		    }
		    r++;
		}
	    } else if (direction == 3) {
		// Stellen oestlich der Bombe werden ueberprueft auf Spieler, und Explosion wird
		// eingefuegt
		while (r <= this.radius
			&& (Game.getFieldFromMap(this.placedField.xPosition + r, this.placedField.yPosition)
				.getContent() == FieldContent.WALL
				|| Game.getFieldFromMap(this.placedField.xPosition + r, this.placedField.yPosition)
					.getContent() == FieldContent.EMPTY
				|| Game.getFieldFromMap(this.placedField.xPosition + r, this.placedField.yPosition)
					.getContent() == FieldContent.BOMB
				|| Game.getFieldFromMap(this.placedField.xPosition + r, this.placedField.yPosition)
					.getContent() == FieldContent.UPGRADE_ITEM_BOMB
				|| Game.getFieldFromMap(this.placedField.xPosition + r, this.placedField.yPosition)
					.getContent() == FieldContent.UPGRADE_ITEM_SHOE
				|| Game.getFieldFromMap(this.placedField.xPosition + r, this.placedField.yPosition)
					.getContent() == FieldContent.UPGRADE_ITEM_FIRE
				|| Game.getFieldFromMap(this.placedField.xPosition + r, this.placedField.yPosition)
					.getContent() == FieldContent.EXPLOSION1
				|| Game.getFieldFromMap(this.placedField.xPosition + r, this.placedField.yPosition)
					.getContent() == FieldContent.EXPLOSION2_NS
				|| Game.getFieldFromMap(this.placedField.xPosition + r, this.placedField.yPosition)
					.getContent() == FieldContent.EXPLOSION3_N
				|| Game.getFieldFromMap(this.placedField.xPosition + r, this.placedField.yPosition)
					.getContent() == FieldContent.EXPLOSION3_S)) {
		    /* Erzeugen eines Wall-Objekts fuer Drop-Moeglichkeit eines Upgrades. */
		    if (Game.getFieldFromMap(this.placedField.xPosition + r, this.placedField.yPosition)
			    .getContent() == FieldContent.WALL) {
			targetedWalls.add(new Wall(
				Game.getFieldFromMap(this.placedField.xPosition + r, this.placedField.yPosition)));
			Game.changeFieldContent(FieldContent.EXPLOSION3_O, this.placedField.xPosition + r,
				this.placedField.yPosition);
			break;
		    }
		    /* Stirbt der eigene Spieler */
		    if (PlayerHandler.getClientPlayer().getCurrentField() == Game
			    .getFieldFromMap(this.placedField.xPosition + r, this.placedField.yPosition)) {
			PlayerHandler.getClientPlayer().setDead(true);
		    }		
		    /* Stirbt ein anderer Spieler */
		    for (Player player : PlayerHandler.getOpponentPlayers()) {
			if (player.getCurrentField() == Game.getFieldFromMap(this.placedField.xPosition + r,
				this.placedField.yPosition)) {
			    player.setDead(true);
			}
		    }
		    /* Ist nebenan eine Bombe, wird eine Kettenreaktion ausgeloest */
		    if (Game.getFieldFromMap(this.placedField.xPosition + r, this.placedField.yPosition)
			    .getContent() == FieldContent.BOMB) {
			chainreaction(this.placedField.xPosition + r, this.placedField.yPosition);
			// b.setCounter(b.getTimer());
			break;
		    }
		    /* Die erste If-Abfrage prueft auf andere Feuerstrahlen und malt ggf Kreuzungen */
		    if(Game.getFieldFromMap(this.placedField.xPosition + r, this.placedField.yPosition)
					.getContent() == FieldContent.EXPLOSION1
				|| Game.getFieldFromMap(this.placedField.xPosition + r, this.placedField.yPosition)
					.getContent() == FieldContent.EXPLOSION2_NS
				|| Game.getFieldFromMap(this.placedField.xPosition + r, this.placedField.yPosition)
					.getContent() == FieldContent.EXPLOSION2
				|| Game.getFieldFromMap(this.placedField.xPosition + r, this.placedField.yPosition)
					.getContent() == FieldContent.EXPLOSION3_N
				|| Game.getFieldFromMap(this.placedField.xPosition + r, this.placedField.yPosition)
					.getContent() == FieldContent.EXPLOSION3_S
				|| Game.getFieldFromMap(this.placedField.xPosition + r, this.placedField.yPosition)
					.getContent() == FieldContent.EXPLOSION3_W){
			Game.changeFieldContent(FieldContent.EXPLOSION1, this.placedField.xPosition + r, this.placedField.yPosition);
			r++;
			break;
			/* Die Abzweigung schaut ob ein Rand im naechsten Feld ist und malt die entsprechende Flamme */
		    } else if ((Game.getFieldFromMap(this.placedField.xPosition + (r + 1), this.placedField.yPosition)
			    .getContent() == FieldContent.BORDER
			    || Game.getFieldFromMap(this.placedField.xPosition + (r + 1), this.placedField.yPosition)
				    .getContent() == FieldContent.BLOCK)
			    || (Game.getFieldFromMap(this.placedField.xPosition + (r + 1), this.placedField.yPosition)
				    .getContent() == FieldContent.BOMB && (this.radius - r == 0))
			    || (this.radius - r == 0)) {
			Game.changeFieldContent(FieldContent.EXPLOSION3_O, this.placedField.xPosition + r,
				this.placedField.yPosition);
			r++;
			break;
			/* Wenn kein Sonderfall eintritt, wird eine normale Flamme nach unten gemalt */
		    } else {
			Game.changeFieldContent(FieldContent.EXPLOSION2, this.placedField.xPosition + r,
				this.placedField.yPosition);
		    }
		    r++;
		}
	    } else if (direction == 4) {
		// Stellen westliche der Bombe werden ueberprueft auf Spieler, und Explosion
		// wird eingefuegt
		while (r <= this.radius
			&& (Game.getFieldFromMap(this.placedField.xPosition - r, this.placedField.yPosition)
				.getContent() == FieldContent.WALL
				|| Game.getFieldFromMap(this.placedField.xPosition - r, this.placedField.yPosition)
					.getContent() == FieldContent.EMPTY
				|| Game.getFieldFromMap(this.placedField.xPosition - r, this.placedField.yPosition)
					.getContent() == FieldContent.BOMB
				|| Game.getFieldFromMap(this.placedField.xPosition - r, this.placedField.yPosition)
					.getContent() == FieldContent.UPGRADE_ITEM_BOMB
				|| Game.getFieldFromMap(this.placedField.xPosition - r, this.placedField.yPosition)
					.getContent() == FieldContent.UPGRADE_ITEM_SHOE
				|| Game.getFieldFromMap(this.placedField.xPosition - r, this.placedField.yPosition)
					.getContent() == FieldContent.UPGRADE_ITEM_FIRE
				|| Game.getFieldFromMap(this.placedField.xPosition - r, this.placedField.yPosition)
					.getContent() == FieldContent.EXPLOSION1
				|| Game.getFieldFromMap(this.placedField.xPosition - r, this.placedField.yPosition)
					.getContent() == FieldContent.EXPLOSION2_NS
				|| Game.getFieldFromMap(this.placedField.xPosition - r, this.placedField.yPosition)
					.getContent() == FieldContent.EXPLOSION3_N
				|| Game.getFieldFromMap(this.placedField.xPosition - r, this.placedField.yPosition)
					.getContent() == FieldContent.EXPLOSION3_S)) {
		    /* Erzeugen eines Wall-Objekts fuer Drop-Moeglichkeit eines Upgrades. */
		    if (Game.getFieldFromMap(this.placedField.xPosition - r, this.placedField.yPosition)
			    .getContent() == FieldContent.WALL) {
			targetedWalls.add(new Wall(
				Game.getFieldFromMap(this.placedField.xPosition - r, this.placedField.yPosition)));
			Game.changeFieldContent(FieldContent.EXPLOSION3_W, this.placedField.xPosition - r,
				this.placedField.yPosition);
			break;
		    }
		    /* Stirbt der eigene Spieler */
		    if (PlayerHandler.getClientPlayer().getCurrentField() == Game
			    .getFieldFromMap(this.placedField.xPosition - r, this.placedField.yPosition)) {
			PlayerHandler.getClientPlayer().setDead(true);
		    }
		    /* Stirbt ein anderer Spieler */
		    for (Player player : PlayerHandler.getOpponentPlayers()) {
			if (player.getCurrentField() == Game.getFieldFromMap(this.placedField.xPosition - r,
				this.placedField.yPosition)) {
			    player.setDead(true);
			}
		    }
		    /* Ist nebenan eine Bombe, wird eine Kettenreaktion ausgeloest */
		    if (Game.getFieldFromMap(this.placedField.xPosition - r, this.placedField.yPosition)
			    .getContent() == FieldContent.BOMB) {
			chainreaction(this.placedField.xPosition - r, this.placedField.yPosition);
			break;
		    }
		    /* Die erste If-Abfrage prueft auf andere Feuerstrahlen und malt ggf Kreuzungen */
		    if(Game.getFieldFromMap(this.placedField.xPosition - r, this.placedField.yPosition)
				.getContent() == FieldContent.EXPLOSION1
			|| Game.getFieldFromMap(this.placedField.xPosition - r, this.placedField.yPosition)
				.getContent() == FieldContent.EXPLOSION2_NS
			|| Game.getFieldFromMap(this.placedField.xPosition - r, this.placedField.yPosition)
					.getContent() == FieldContent.EXPLOSION2
			|| Game.getFieldFromMap(this.placedField.xPosition - r, this.placedField.yPosition)
				.getContent() == FieldContent.EXPLOSION3_N
			|| Game.getFieldFromMap(this.placedField.xPosition - r, this.placedField.yPosition)
				.getContent() == FieldContent.EXPLOSION3_S
			|| Game.getFieldFromMap(this.placedField.xPosition - r, this.placedField.yPosition)
				.getContent() == FieldContent.EXPLOSION3_O){
			Game.changeFieldContent(FieldContent.EXPLOSION1, this.placedField.xPosition - r, this.placedField.yPosition);
			r++;
			break;
			/* Die Abzweigung schaut ob ein Rand im naechsten Feld ist und malt die entsprechende Flamme */
		    } else if ((Game.getFieldFromMap(this.placedField.xPosition - (r + 1), this.placedField.yPosition)
			    .getContent() == FieldContent.BORDER
			    || Game.getFieldFromMap(this.placedField.xPosition - (r + 1), this.placedField.yPosition)
				    .getContent() == FieldContent.BLOCK)
			    || (Game.getFieldFromMap(this.placedField.xPosition - (r + 1), this.placedField.yPosition)
				    .getContent() == FieldContent.BOMB && (this.radius - r == 0))
			    || (this.radius - r == 0)) {
				Game.changeFieldContent(FieldContent.EXPLOSION3_W, this.placedField.xPosition - r,
					this.placedField.yPosition);
				r++;
			break;
			/* Wenn kein Sonderfall eintritt, wird eine normale Flamme nach unten gemalt */
		    } else {
			Game.changeFieldContent(FieldContent.EXPLOSION2, this.placedField.xPosition - r,
				this.placedField.yPosition);
		    }
		    r++;
		}
	    }
	}
	/* Die Anzahl der Bomben die von einem Spieler gelegt werden koennen, wird reduziert */
	/* Fuer den eigenen Spieler */
	if (PlayerHandler.getClientPlayer().getId() == this.ownerId) {
	    PlayerHandler.getClientPlayer().decreasePlacedBombs();
	} else {
	    /* Fuer die anderen Spieler */
	    for (Player player : PlayerHandler.getOpponentPlayers()) {
		if (player.getId() == this.ownerId) {
		    player.decreasePlacedBombs();
		}
	    }
	}
    }

    /**
     * Wird nach dem malen der Flammen aufgerufen und entfernt entsprechende wieder
     */
    public void explode() {
	sysTimer.stop();

	for (int direction = 0; direction < 5; direction++) {
	    int r = 1;
	    /* Stelle der Bombe wird EMPTY gesetzt */
	    if (direction == 0) {
		if (PlayerHandler.getClientPlayer().getCurrentField() == Game
			.getFieldFromMap(this.placedField.xPosition, this.placedField.yPosition)) {
		    PlayerHandler.getClientPlayer().setDead(true);
		}
		for (Player player : PlayerHandler.getOpponentPlayers()) {
		    if (player.getCurrentField() == Game.getFieldFromMap(this.placedField.xPosition,
			    this.placedField.yPosition)) {
			player.setDead(true);
		    }
		}
		/* Sueden der Bombe wird EMPTY gesetzt */
	    } else if (direction == 1) {
		while (r <= this.radius
			&& (Game.getFieldFromMap(this.placedField.xPosition, this.placedField.yPosition + r)
				.getContent() == FieldContent.EXPLOSION2_NS
				|| Game.getFieldFromMap(this.placedField.xPosition, this.placedField.yPosition + r)
					.getContent() == FieldContent.EXPLOSION1
				|| Game.getFieldFromMap(this.placedField.xPosition, this.placedField.yPosition + r)
					.getContent() == FieldContent.EXPLOSION2
				|| Game.getFieldFromMap(this.placedField.xPosition, this.placedField.yPosition + r)
					.getContent() == FieldContent.EXPLOSION3_N
				|| Game.getFieldFromMap(this.placedField.xPosition, this.placedField.yPosition + r)
					.getContent() == FieldContent.EXPLOSION3_W
				|| Game.getFieldFromMap(this.placedField.xPosition, this.placedField.yPosition + r)
					.getContent() == FieldContent.EXPLOSION3_O
				|| Game.getFieldFromMap(this.placedField.xPosition, this.placedField.yPosition + r)
					.getContent() == FieldContent.EXPLOSION3_S
				|| Game.getFieldFromMap(this.placedField.xPosition, this.placedField.yPosition + r)
					.getContent() == FieldContent.EMPTY)) {
		    Game.changeFieldContent(FieldContent.EMPTY, this.placedField.xPosition,
			    this.placedField.yPosition + r);
		    r++;

		}
		/* Norden der Bombe wird EMPTY gesetzt */
	    } else if (direction == 2) {
		while (r <= this.radius
			&& (Game.getFieldFromMap(this.placedField.xPosition, this.placedField.yPosition - r)
				.getContent() == FieldContent.EXPLOSION2_NS
				|| Game.getFieldFromMap(this.placedField.xPosition, this.placedField.yPosition - r)
					.getContent() == FieldContent.EXPLOSION1
				|| Game.getFieldFromMap(this.placedField.xPosition, this.placedField.yPosition - r)
					.getContent() == FieldContent.EXPLOSION2
				|| Game.getFieldFromMap(this.placedField.xPosition, this.placedField.yPosition - r)
					.getContent() == FieldContent.EXPLOSION3_N
				|| Game.getFieldFromMap(this.placedField.xPosition, this.placedField.yPosition - r)
					.getContent() == FieldContent.EXPLOSION3_W
				|| Game.getFieldFromMap(this.placedField.xPosition, this.placedField.yPosition - r)
					.getContent() == FieldContent.EXPLOSION3_O
				|| Game.getFieldFromMap(this.placedField.xPosition, this.placedField.yPosition - r)
					.getContent() == FieldContent.EXPLOSION3_S
				|| Game.getFieldFromMap(this.placedField.xPosition, this.placedField.yPosition - r)
					.getContent() == FieldContent.EMPTY)) {
		    Game.changeFieldContent(FieldContent.EMPTY, this.placedField.xPosition,
			    this.placedField.yPosition - r);
		    r++;
		}
		/* Osten der Bombe wird EMPTY gesetzt */
	    } else if (direction == 3) {
		while (r <= this.radius
			&& (Game.getFieldFromMap(this.placedField.xPosition + r, this.placedField.yPosition)
				.getContent() == FieldContent.EXPLOSION2_NS
				|| Game.getFieldFromMap(this.placedField.xPosition + r, this.placedField.yPosition)
					.getContent() == FieldContent.EXPLOSION1
				|| Game.getFieldFromMap(this.placedField.xPosition + r, this.placedField.yPosition)
					.getContent() == FieldContent.EXPLOSION2
				|| Game.getFieldFromMap(this.placedField.xPosition + r, this.placedField.yPosition)
					.getContent() == FieldContent.EXPLOSION3_N
				|| Game.getFieldFromMap(this.placedField.xPosition + r, this.placedField.yPosition)
					.getContent() == FieldContent.EXPLOSION3_W
				|| Game.getFieldFromMap(this.placedField.xPosition + r, this.placedField.yPosition)
					.getContent() == FieldContent.EXPLOSION3_O
				|| Game.getFieldFromMap(this.placedField.xPosition + r, this.placedField.yPosition)
					.getContent() == FieldContent.EXPLOSION3_S
				|| Game.getFieldFromMap(this.placedField.xPosition + r, this.placedField.yPosition)
					.getContent() == FieldContent.EMPTY)) {
		    Game.changeFieldContent(FieldContent.EMPTY, this.placedField.xPosition + r,
			    this.placedField.yPosition);
		    r++;
		}
		/* Westen der Bombe wird Empty gesetzt */
	    } else if (direction == 4) {
		while (r <= this.radius
			&& (Game.getFieldFromMap(this.placedField.xPosition - r, this.placedField.yPosition)
				.getContent() == FieldContent.EXPLOSION2_NS
				|| Game.getFieldFromMap(this.placedField.xPosition - r, this.placedField.yPosition)
					.getContent() == FieldContent.EXPLOSION1
				|| Game.getFieldFromMap(this.placedField.xPosition - r, this.placedField.yPosition)
					.getContent() == FieldContent.EXPLOSION2
				|| Game.getFieldFromMap(this.placedField.xPosition - r, this.placedField.yPosition)
					.getContent() == FieldContent.EXPLOSION3_N
				|| Game.getFieldFromMap(this.placedField.xPosition - r, this.placedField.yPosition)
					.getContent() == FieldContent.EXPLOSION3_W
				|| Game.getFieldFromMap(this.placedField.xPosition - r, this.placedField.yPosition)
					.getContent() == FieldContent.EXPLOSION3_O
				|| Game.getFieldFromMap(this.placedField.xPosition - r, this.placedField.yPosition)
					.getContent() == FieldContent.EXPLOSION3_S
				|| Game.getFieldFromMap(this.placedField.xPosition - r, this.placedField.yPosition)
					.getContent() == FieldContent.EMPTY)) {
		    Game.changeFieldContent(FieldContent.EMPTY, this.placedField.xPosition - r,
			    this.placedField.yPosition);
		    r++;
		}
	    }
	}
	/* Aufrufen von destroyed-Methode aller erzeugten Wall-Objekte. */
	Game.changeFieldContent(FieldContent.EMPTY, placedField.xPosition, placedField.yPosition);
	for (Wall wall : this.targetedWalls) {
	    wall.destroyed();
	}
	ConsoleHandler.print("Bomb from Player ID " + this.ownerId + " exploded at (" + placedField.xPosition + ", "
		+ placedField.yPosition + ")", MessageType.GAME);
	this.targetedWalls.clear();
	Game.removeBomb(this);
    }

    public int getCounter() {
	return this.timer - this.counter;
    }

    public double getScale() {
	return scale;
    }

    public void setCounter(int c) {
	this.counter = c;
    }

    public Field getPlacedField() {
	return placedField;
    }

    /**
     * Laesst eine Bombe vorzeitig explodieren, wenn diese z.B. von einer anderen Bombe getroffen wird
     * @param x, X-Position die auf eine Bombe geprueft werden soll
     * @param y, Y-Position die auf eine Bombe geprueft werden soll
     */
    public void chainreaction(int x, int y) {
	ArrayList<Bomb> placed = Game.getPlacedBombs();
	for (Bomb bomb : placed) {
	    if (bomb.getPlacedField().xPosition == x && bomb.getPlacedField().yPosition == y) {
		bomb.explodeFire();
	    }
	}
    }

    public int getTimer() {
	return this.timer;
    }

    public ArrayList<Wall> getTargetedWalls() {
	return targetedWalls;
    }

    public void stopTimer() {
	this.sysTimer.stop();
    }
}
