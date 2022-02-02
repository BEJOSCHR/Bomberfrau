/*
 * WindoHandler
 *
 * Version 1.0
 * Author: Benni
 *
 * Verwaltet alle Events des Fensters (Frame) über die jeweiligen Events
 */
package uni.bombenstimmung.de.backend.graphics.subhandler;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import uni.bombenstimmung.de.backend.graphics.GraphicsHandler;

public class WindowHandler implements WindowListener {

    @Override
    public void windowOpened(WindowEvent e) {

    }

    @Override
    public void windowClosing(WindowEvent e) {

	GraphicsHandler.shutdownProgram();

    }

    @Override
    public void windowClosed(WindowEvent e) {

    }

    @Override
    public void windowIconified(WindowEvent e) {

    }

    @Override
    public void windowDeiconified(WindowEvent e) {

    }

    @Override
    public void windowActivated(WindowEvent e) {

    }

    @Override
    public void windowDeactivated(WindowEvent e) {

    }

}
