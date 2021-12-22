
/*	Diese Klasse ist von Dennis und wurde von Mustafa überarbeitet
 * 	Sie dient dazu, die einzelnen Felder und ihre Inhalte 
 * 	der Map zu verwalten. 
=======
/*
 * Field
 *
 * Version 1.0
 * Author: Dennis
 *
 * Verwaltet die einzelnen Felder einer Map
>>>>>>> branch 'InGame' of git@github.com:BEJOSCHR/Bomberfrau.git
 */
package uni.bombenstimmung.de.game;

import java.awt.Graphics;
import java.awt.Color;

import uni.bombenstimmung.de.backend.console.ConsoleHandler;
import uni.bombenstimmung.de.backend.graphics.GraphicsHandler;
import uni.bombenstimmung.de.backend.images.ImageHandler;
import uni.bombenstimmung.de.backend.images.ImageType;

public class Field extends Entity {

    private FieldContent content;
    
    /**
     * Erzeugt ein Feld
     * @param x, Startpunkt X
     * @param y, Startpunkt Y
     * @param cont, Typ des Feldes
     */
    public Field(int x, int y, FieldContent cont) {
	super.xPosition = x;
	super.yPosition = y;
	content = cont;
    }
    
    /**
     * Gibt einen String zurück, der den Typ des Feldes repräsentiert
     * @param type, FieldContent eingeben
     */
    public static String getFieldTypeRepresentation(FieldContent type) {
	switch(type) { 
		case BLOCK:
			return "BL";
		case BORDER: // wand_orange.png wird angezeigt 
			return "BO";
		case EMPTY: //freier Space wo dann Gras Tile gezeigt wird
			return "EM";
		case WALL: //Box.png, die Sachen die zerstört werden können 
			return "WA";
		default:
			ConsoleHandler.print("Unknown fieldtype '"+type+"'!");
			return "##";
	}
    }
    
    
    /**
     * Wandelt einen String in einen FieldContent um
     * @param representation, String eingabe (BL,BO,EM,WA)
     */
    public static FieldContent getFieldTypeFromRepresentation(String representation) {
	
	switch(representation) {
		case "BL":
			return FieldContent.BLOCK;
		case "BO":
		    return FieldContent.BORDER;
		case "EM":
		    return FieldContent.EMPTY;
		case "WA":
		    return FieldContent.WALL;
		default:
		    ConsoleHandler.print("Unknown fieldtype representation '"+representation+"'!");
		    return FieldContent.EMPTY;
	}
    }
    
    /**
     * Malt das spezifische Feld an den angegebenen Koordinaten auf der Map
     * @param x, X-Koordinate
     * @param y, Y-Koordinate
     * @param cont, FieldContent
     */
    public void drawField(Graphics g, int x, int y, FieldContent cont) {
	switch (cont) {
		case BLOCK:
		g.drawImage(ImageHandler.getImage(ImageType.IMAGE_INGAME_BLOCK).getImage(), x, y, null);
		break;
	    case WALL:
		g.drawImage(ImageHandler.getImage(ImageType.IMAGE_INGAME_WALL).getImage(), x, y, null);
		break;
	    case BORDER:
		g.drawImage(ImageHandler.getImage(ImageType.IMAGE_INGAME_BORDER).getImage(), x, y, null);
		break;
	    case BOMB:
		g.drawImage(ImageHandler.getImage(ImageType.IMAGE_INGAME_BOMB).getImage(), x, y, null);
		for(Bomb i : Game.getPLacedBombs()) {
		    int count = i.getCounter();
		    GraphicsHandler.drawCentralisedText(g, Color.RED, 30, count + "", x+(GameData.FIELD_DIMENSION/2), y+(GameData.FIELD_DIMENSION/2));
		}
		break;
	    case EMPTY:
		if (Game.getMapNumber() == 1) {
		    g.drawImage(ImageHandler.getImage(ImageType.IMAGE_INGAME_GRAS).getImage(), x, y, null);
		} else if (Game.getMapNumber() == 2) {
		    g.drawImage(ImageHandler.getImage(ImageType.IMAGE_INGAME_YELLOWGRAS).getImage(), x, y, null);
		}
		break;
	  
	}
    }
    
    public void setContent (FieldContent t) {
	content = t;
    }
    
    public FieldContent getContent() {
	return content;
    }
    
}
    
    
    
    
    
    
    

