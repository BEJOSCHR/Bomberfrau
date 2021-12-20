/*	Diese Klasse ist von Dennis und wurde von Mustafa überarbeitet
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
		case BLOCK: //wand_gray.png wird angezeigt 
			return "BL";
		case BORDER: // wand_orange.png wird angezeigt 
			return "BO";
		case EMPTY: //freier Space wo dann Gras Tile gezeigt wird
			return "EM";
		case WALL: //Box.png, die Sachen die zerstört werden können 
			return "WA";
		case YELLOWGRAS: 
			return "YG";
		case LIGHTTILE:
			return "LT";
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
		case "YG":
			return FieldContent.YELLOWGRAS;
		case "LT":
			return FieldContent.LIGHTTILE;
		default:
		    ConsoleHandler.print("Unknown fieldtype representation '"+representation+"'!");
		    return FieldContent.EMPTY;
	}
    }
    
    public void drawField(Graphics g, int x, int y, FieldContent cont) {
	switch (cont) {
	    case WALL:
		g.drawImage(ImageHandler.getImage(ImageType.IMAGE_INGAME_WALL).getImage(), x, y, null);
		break;
	    case BORDER:
		g.drawImage(ImageHandler.getImage(ImageType.IMAGE_INGAME_BORDER).getImage(), x, y, null);
		break;
	    case EMPTY:
		g.drawImage(ImageHandler.getImage(ImageType.IMAGE_INGAME_GRAS).getImage(), x, y, null);
		break;
	    case BLOCK:
		g.drawImage(ImageHandler.getImage(ImageType.IMAGE_INGAME_BLOCK).getImage(), x, y, null);
		break;
	    case YELLOWGRAS:
	    g.drawImage(ImageHandler.getImage(ImageType.IMAGE_INGAME_YELLOWGRAS).getImage(), x, y, null);
	    break;
	    default:
	    case LIGHTTILE:
	    g.drawImage(ImageHandler.getImage(ImageType.IMAGE_INGAME_LIGHTTILE).getImage(), x, y, null);	
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
    
    
    
    
    
    
    

