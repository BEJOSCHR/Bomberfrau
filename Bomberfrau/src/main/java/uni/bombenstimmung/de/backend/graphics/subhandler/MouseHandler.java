/*
 * MouseHandler
 *
 * Version 1.0
 * Author: Benni
 *
 * Verwaltet alle Mausklicks bzw Mausraddrehungen bzw Mausbewegungen über die jeweiligen Events
 */
package uni.bombenstimmung.de.backend.graphics.subhandler;

import java.util.ArrayList;
import java.util.List;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import uni.bombenstimmung.de.backend.maa.MouseActionArea;
import uni.bombenstimmung.de.backend.maa.MouseActionAreaHandler;

public class MouseHandler implements MouseMotionListener, MouseListener, MouseWheelListener {

    private static int mouseX = 0, mouseY = 0;

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

	int buttonCode = e.getButton();
	if (buttonCode == MouseEvent.BUTTON1) {
	    // LEFT
	    List<MouseActionArea> clickedMAAs = new ArrayList<MouseActionArea>();
	    for (MouseActionArea maa : MouseActionAreaHandler.getMAAs()) {
		if (maa.isActiv() && maa.isHovered()) {
		    clickedMAAs.add(maa);
		}
	    }
	    for (MouseActionArea clickedMAA : clickedMAAs) {
		clickedMAA.performAction_LEFT_PRESS();
	    }
	} else if (buttonCode == MouseEvent.BUTTON3) {
	    // RIGHT
	    List<MouseActionArea> clickedMAAs = new ArrayList<MouseActionArea>();
	    for (MouseActionArea maa : MouseActionAreaHandler.getMAAs()) {
		if (maa.isActiv() && maa.isHovered()) {
		    clickedMAAs.add(maa);
		}
	    }
	    for (MouseActionArea clickedMAA : clickedMAAs) {
		clickedMAA.performAction_RIGHT_PRESS();
	    }
	}

    }

    @Override
    public void mouseReleased(MouseEvent e) {

	int buttonCode = e.getButton();
	if (buttonCode == MouseEvent.BUTTON1) {
	    // LEFT
	    List<MouseActionArea> clickedMAAs = new ArrayList<MouseActionArea>();
	    for (MouseActionArea maa : MouseActionAreaHandler.getMAAs()) {
		if (maa.isActiv() && maa.isHovered()) {
		    clickedMAAs.add(maa);
		}
	    }
	    for (MouseActionArea clickedMAA : clickedMAAs) {
		clickedMAA.performAction_LEFT_RELEASE();
	    }
	} else if (buttonCode == MouseEvent.BUTTON3) {
	    // RIGHT
	    List<MouseActionArea> clickedMAAs = new ArrayList<MouseActionArea>();
	    for (MouseActionArea maa : MouseActionAreaHandler.getMAAs()) {
		if (maa.isActiv() && maa.isHovered()) {
		    clickedMAAs.add(maa);
		}
	    }
	    for (MouseActionArea clickedMAA : clickedMAAs) {
		clickedMAA.performAction_RIGHT_RELEASE();
	    }
	}

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {

	mouseX = e.getX();
	mouseY = e.getY();

    }

    public static int getMouseX() {
	return mouseX;
    }

    public static int getMouseY() {
	return mouseY;
    }

}
