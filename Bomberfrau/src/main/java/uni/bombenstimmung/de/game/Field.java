/*	Diese Klasse ist von Dennis
 * 	Sie dient dazu, die einzelnen Felder und ihre Inhalte 
 * 	der Map zu verwalten. 
 */

package uni.bombenstimmung.de.game;

import java.awt.Graphics;
import java.awt.Color;

public class Field extends Entity {

    private FieldContent content;
    
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
    
    
    
    
    
    
    

