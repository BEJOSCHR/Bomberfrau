/*	Diese Klasse ist von Dennis
 * 	Sie dient dazu, die zerstörbare Wall zu
 * 	verwalten.
 */

package uni.bombenstimmung.de.game;

import java.lang.Math;

public class Wall {
    private Field field;
    private double randNr;
    
    public Wall(Field field) {
	this.field = field;
	this.randNr = Math.random();
    }
    
    public void destroyed() {
	if (randNr <= 0.2) {
	    Game.changeFieldContent(FieldContent.UPGRADE, this.field.xPosition, this.field.yPosition);
	} else {
	    Game.changeFieldContent(FieldContent.EMPTY, this.field.xPosition, this.field.yPosition);
	}
	this.field = null;
	this.randNr = 0.0;
    }
}
