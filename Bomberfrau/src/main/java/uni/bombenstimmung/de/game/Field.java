/*	Diese Klasse ist von Dennis
 * 	Sie dient dazu, die einzelnen Felder und ihre Inhalte 
 * 	der Map zu verwalten. 
 */

package uni.bombenstimmung.de.game;

import java.awt.Graphics;

public class Field extends Entity {
    
    private static int width;
    private static int length;
    private FieldContent content;
    
    public Field(int w, int l, FieldContent c) {
	width = w;
	length = l;
	content = c;
    }
    
    public void drawField(Graphics g, int x, int y, int hoehe, int breite, FieldContent cont) {
	
    }
    
    public void setFieldSize() {
	
    }
    
    public void setContent (FieldContent t) {
	content = t;
    }
    
    public FieldContent getContent() {
	return content;
    }

}
