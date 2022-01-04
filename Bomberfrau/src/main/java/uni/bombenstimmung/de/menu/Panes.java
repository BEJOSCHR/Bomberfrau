/*
 * Panes
 *
 * Version 1.0
 * Author: Carsten
 *
 * selbstgemachte Abfrage- und Info-Fenster
 */

package uni.bombenstimmung.de.menu;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import uni.bombenstimmung.de.backend.graphics.GraphicsHandler;

public class Panes {

    /**
     * Erstellt ein selbstgemachtes Info Fenster
     */
    public static void InfoPane(String txt1, String txt2, String btn_txt) {
	JDialog d = new JDialog((java.awt.Frame) null, txt1, true);
	d.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
	d.setPreferredSize(new Dimension((int) (txt2.length() * 22 * Settings.getFactor()),
		(int) (Settings.getRes_height() / 5 * Settings.getFactor())));

	JPanel panel = new JPanel();
	panel.setBackground(Color.ORANGE);

	JButton btn = new JButton(btn_txt);
	btn.setFont(GraphicsHandler.usedFont((int) (15 * Settings.getFactor())));
	btn.setFocusable(true);
	btn.setForeground(Color.WHITE);
	btn.setBackground(Color.RED);
	btn.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		//ConsoleHandler.print("button 1 pressed", MessageType.MENU);
		d.dispose();
	    }
	});
	panel.add(btn);
	btn.requestFocus(true);
        d.getRootPane().setDefaultButton(btn);

	JLabel label = new JLabel(txt2);
	label.setHorizontalAlignment(JLabel.CENTER);
	label.setFont(GraphicsHandler.usedFont((int) (30 * Settings.getFactor())));
	label.setBackground(Color.ORANGE);
	label.setForeground(Color.WHITE);
	label.setOpaque(true);

	d.setLayout(new BorderLayout());
	d.add(panel, BorderLayout.SOUTH);
	d.add(label, BorderLayout.CENTER);

	d.getRootPane().setBorder(BorderFactory.createLineBorder(Color.RED, (int) (20 * Settings.getFactor()), true));

	d.setUndecorated(true);
	d.pack();
	d.setLocationRelativeTo(null);
	d.setVisible(true);
    }

}