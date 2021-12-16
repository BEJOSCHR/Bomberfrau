/*	Diese Klasse ist von Dennis
 * 	Sie dient dazu, die einzelnen Felder und ihre Inhalte 
 * 	der Map zu verwalten. 
 */

package uni.bombenstimmung.de.game;

import java.awt.Graphics;

import uni.bombenstimmung.de.backend.console.ConsoleHandler;
import uni.bombenstimmung.de.backend.images.ImageHandler;
import uni.bombenstimmung.de.backend.images.ImageType;

import java.awt.Color;

public class Field extends Entity {

    private FieldContent content;
    
    public Field(int x, int y, FieldContent cont) {
	super.xPosition = x;
	super.yPosition = y;
	content = cont;
    }
    
    public static String getFieldTypeRepresentation(FieldContent type) {
	switch(type) {
		case BLOCK:
			return "BL";
		case BORDER:
			return "BO";
		case EMPTY:
			return "EM";
		case WALL:
			return "WA";
		default:
			ConsoleHandler.print("Unknown fieldtype '"+type+"'!");
			return "##";
	}
    }
    
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
    
    public void drawField(Graphics g, int x, int y, FieldContent cont) {
	switch (cont) {
	    case WALL:
		g.setColor(Color.GRAY);
		g.fillRect(x,y, x+GameData.FIELD_DIMENSION, y+GameData.FIELD_DIMENSION);
		break;
	    case BORDER:
		g.setColor(Color.MAGENTA);
		g.fillRect(x,y,x+GameData.FIELD_DIMENSION, y+GameData.FIELD_DIMENSION);
		break;
	    case EMPTY:
		//g.drawImage(ImageHandler.getImage(ImageType.IMAGE_INGAME_GRAS),x,y,x+GameData.FIELD_DIMENSION,y+GameData.FIELD_DIMENSION, Color.RED);
		//g.setBackground(ImageHandler.getImage(ImageType.IMAGE_INGAME_GRAS));
		g.setColor(Color.GREEN);
		g.fillRect(x,y,x+GameData.FIELD_DIMENSION, y+GameData.FIELD_DIMENSION);
		break;
	    case BLOCK:
		g.setColor(Color.BLACK);
		g.fillRect(x,y,x+GameData.FIELD_DIMENSION, y+GameData.FIELD_DIMENSION);
		break;
	    default:
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
    
    
    
    
    
    
    

