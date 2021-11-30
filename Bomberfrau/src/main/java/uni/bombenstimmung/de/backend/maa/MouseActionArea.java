/*
 * MouseActionArea
 *
 * Version 1.0
 * Author: Benni
 *
 * Enthält jeweils alle Daten zu einem erstellten Button und representiert diesen auch (Draw, Click, Hover...)
 */
package uni.bombenstimmung.de.backend.maa;

import java.util.ConcurrentModificationException;

import java.awt.Color;
import java.awt.Graphics;

import uni.bombenstimmung.de.backend.graphics.GraphicsHandler;

public class MouseActionArea {

	private int x = 0, y = 0, width = 0, height = 0;
	private MouseActionAreaType type;
	private String displayText = "missing";
	private int textSize = 20;
	private Color standardColor = Color.WHITE;
	private Color hoverColor = Color.WHITE;
	
	/**
	 * Die Representation eines Buttons
	 * @param x - Die X-Koordinate in der obenren linken Ecke
	 * @param y - Die Y-Koordinate in der obenren linken Ecke
	 * @param width - Die Breite der Area
	 * @param height - Die Höhe der Area
	 * @param type - Der Type der diese Area von den anderen Unterscheidet bzw zum identifizieren genutzt wird
	 * @param displayText - Der dargestellte Text in der MAA (mittig)
	 * @param textSize - Die Schriftgröße des Textes
	 * @param standardColor - Die Farbe, die der Rand der Area standardmäßig hat
	 * @param hoverColor - Die Farbe, die der Rand der Area beim Hovern hat
	 **/
	public MouseActionArea(int x, int y, int width, int height, MouseActionAreaType type, String displayText, int textSize, Color standardColor, Color hoverColor) {
		
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.type = type;
		this.displayText = displayText;
		this.textSize = textSize;
		this.standardColor = standardColor;
		this.hoverColor = hoverColor;
		
		//MAKES SURE THAT CONCURRENT ERRORS DONT PREVENT ADDING
		while(!MouseActionAreaHandler.getMAAs().contains(this)) {
			try{
				MouseActionAreaHandler.getMAAs().add(this);
			}catch(ConcurrentModificationException error) {}
		}
		
	}
	

	/**
	 * Überprüft ob sich bestimmte Koordinaten in der Area dieser MAA befinden
	 * @param mouseX - int - Die X-Koordinate
	 * @param mouseY - int - Die Y-Koordinate
	 * @return boolean - True wenn sich die Koordinaten in der Area befindet, false wenn nicht (Auch false wenn nicht aktive!)
	 **/
	public boolean checkArea(int mouseX, int mouseY) {
		
		if(isActiv() == true) {
			if(mouseX >= x && mouseX <= x+width  &&  mouseY >= y && mouseY <= y+height) {
				return true;
			}else {
				return false;
			}
		}else {
			return false;
		}
		
	}
	

	/**
	 * Überprüft ob die Mause in der Area ist (Also über die Area hovered)
	 * @return boolean - Den selben status von checkArea() nur mit den Mouse-Koorinaten als Input
	 **/
	public boolean isHovered() {
		return checkArea(MouseHandler.getMouseX(), MouseHandler.getMouseY());
	}
	

	/**
	 * Überprüft ob diese Area gerade aktiv ist.
	 * (Soll überschrieben/verändert werden wenn eine neue MAA erzeugt wird (@Overwrite))
	 * @return boolean - True wenn die Aktivierungsbedingung erfüllt ist, sont false (Wenn nicht überschrieben/verändert dann standardmäßig true)
	 **/
	public boolean isActiv() {
		return true;
	}
	

	/**
	 * Wird aufgerufen wenn diese MAA mit der LINKEN Taste GEDRÜCKT wird - LEFT PRESS
	 * (Soll überschrieben/verändert werden wenn eine neue MAA erzeugt wird (@Overwrite))
	 **/
	public void performAction_LEFT_PRESS() { }

	/**
	 * Wird aufgerufen wenn diese MAA mit der RECHTEN Taste GEDRÜCKT wird - RIGHT PRESS
	 * (Soll überschrieben/verändert werden wenn eine neue MAA erzeugt wird (@Overwrite))
	 **/
	public void performAction_RIGHT_PRESS() { }

	/**
	 * Wird aufgerufen wenn diese MAA mit der LINKEN Taste LOSGELASSEN wird - LEFT RELEASE
	 * (Soll überschrieben/verändert werden wenn eine neue MAA erzeugt wird (@Overwrite))
	 **/
	public void performAction_LEFT_RELEASE() { }

	/**
	 * Wird aufgerufen wenn diese MAA mit der RECHTEN Taste LOSGELASSEN wird - RIGHT RELEASE
	 * (Soll überschrieben/verändert werden wenn eine neue MAA erzeugt wird (@Overwrite))
	 **/
	public void performAction_RIGHT_RELEASE() { }
	

	/**
	 * Der graphische Darstellungsteil jeder MAA (Wird nur aufgerufen wenn die MAA aktiv ist)
	 * Standardmäßig wird der Rand der MAA in der jeweiligen Farbe (Abhängig ob gehovered oder nicht) mit dem dispplaytext dargestellt
	 * Custom veränderungen können in der drawCustomParts methode überschrieben/verändert (@Overwrite) werden
	 * @param g - {@link Graphics} - Der Grafik-Parameter
	 **/
	public void draw(Graphics g) { 
		
		if(isHovered()) {
			g.setColor(hoverColor);
			g.drawRect(x, y, width, height);
			GraphicsHandler.drawCentralisedText(g, hoverColor, textSize, displayText, x+width/2, y+height/2); 
		}else {
			g.setColor(standardColor);
			g.drawRect(x, y, width, height);
			GraphicsHandler.drawCentralisedText(g, standardColor, textSize, displayText, x+width/2, y+height/2); 
		}
		drawCustomParts(g);
		
	}
	
	/**
	 * Stellt zusätzliche grafische Veränderungen dar
	 * (Soll überschrieben/verändert werden wenn eine neue MAA erzeugt wird (@Overwrite))
	 * @param g - {@link Graphics} - Der Grafik-Parameter
	 */
	public void drawCustomParts(Graphics g) {}
	

	/**
	 * Removes/Delete this actionArea
	 **/
	public void remove() {
		
		//MAKES SURE THAT CONCURRENT ERRORS DONT PREVENT REMOVING
		while(MouseActionAreaHandler.getMAAs().contains(this)) {
			try{
				MouseActionAreaHandler.getMAAs().remove(this);
			}catch(ConcurrentModificationException error) {}
		}
		
	}
	
	public MouseActionAreaType getType() {
		return type;
	}
	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}
	
}
