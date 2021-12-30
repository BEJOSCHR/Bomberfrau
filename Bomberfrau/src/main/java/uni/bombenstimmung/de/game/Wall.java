/*	Diese Klasse ist von Dennis
 * 	Sie dient dazu, die zerstörbare Wall zu
 * 	verwalten.
 */

package uni.bombenstimmung.de.game;

import java.lang.Math;

public class Wall {
    private Field field;
    private double randNr;
    java.util.Random random = new java.util.Random();
	int tmp = random.nextInt(2) + 1;
    
    public Wall(Field field) {
	this.field = field;
	this.randNr = Math.random();
	this.tmp = random.nextInt(3) + 1;
	
    }
    
    public void destroyed() {
	if (randNr <= 0.15) {
		if (tmp == 1) {
			Game.changeFieldContent(FieldContent.UPGRADE_ITEM_BOMB, this.field.xPosition, this.field.yPosition);
		}
		if (tmp == 2) {
			Game.changeFieldContent(FieldContent.UPGRADE_ITEM_FIRE, this.field.xPosition, this.field.yPosition);
		}
		if (tmp == 3) {
			Game.changeFieldContent(FieldContent.UPGRADE_ITEM_SHOE, this.field.xPosition, this.field.yPosition);
		}
	} else {
	    Game.changeFieldContent(FieldContent.EMPTY, this.field.xPosition, this.field.yPosition);
	}
	this.field = null;
	this.randNr = 0.0;
    }
}
