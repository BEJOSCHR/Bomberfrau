/*
 * Bomb
 *
 * Version 1.0
 * Author: Dennis
 *
 * Verwaltet die Bombe im Spiel
 */

package uni.bombenstimmung.de.game;

public class Bomb {

    private int radius;
    private int timer;
    private FieldContent type;
    
    public Bomb(int r, int t) {
	radius = r;
	timer = t;
    }
    
    public void explode() {
	
    }
    
    public void setBomb(int x, int y) {
	
    }
    
    public FieldContent getFieldContent() {
	return type;
    }
    
    public void setFieldContent(FieldContent t) {
	type = t;
    }

}
