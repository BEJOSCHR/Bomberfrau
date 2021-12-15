/*	Diese Klasse ist von Dennis
 * 	Sie dient dazu, die einzelnen Felder und ihre Inhalte 
 * 	der Map zu verwalten. 
 */

package uni.bombenstimmung.de.game;

import uni.bombenstimmung.de.backend.graphics.*;

public class Field extends Entity {
    
    private int width;
    private int length;
    private FieldContent type;
    
    
    public Field(int w, int l, FieldContent c) {
	width = w;
	length = l;
	type = c;
    }
    
    public void setContent (FieldContent t) {
	type = t;
    }
    
    public FieldContent getContent() {
	return type;
    }

    
    public void changeType(FieldContent newType) {
		
   this.type = newType;
	}
    
    }
    
    
    
    
    
    
    

