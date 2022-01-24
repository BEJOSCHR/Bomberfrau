/*
 * Entity
 *
 * Version 1.0
 * Author: Dennis
 *
 * Abstrakte Klasse um die Position zu speichern
 */
package uni.bombenstimmung.de.game;

import java.awt.Point;

public abstract class Entity {

    public int xPosition;
    public int yPosition;

    public void setPosition(int x, int y) {
	xPosition = x;
	yPosition = y;
    }

    public Point getPosition() {
	Point back = new Point(xPosition, yPosition);
	return back;
    }

    public Entity() {

    }

}
