package uni.bombenstimmung.de.menu;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import uni.bombenstimmung.de.backend.graphics.GraphicsHandler;

public class Panes {

    
    public static void OptionPane(String txt1, String txt2, String btn1_txt, String btn2_txt)
    {
        JDialog d = new JDialog((java.awt.Frame)null, txt1, true);
        d.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        d.setPreferredSize(new Dimension( (int)(txt2.length()*22*Settings.factor), (int)(Settings.res_height/5*Settings.factor)));

        JPanel panel = new JPanel();
        panel.setBackground(Color.ORANGE);
        
        JButton btn1 = new JButton(btn1_txt);
        btn1.setFont(GraphicsHandler.usedFont((int)(15*Settings.factor)));
        btn1.setFocusable(false);
        btn1.setForeground(Color.WHITE);
        btn1.setBackground(Color.RED);
        btn1.addActionListener(new ActionListener() { 
        	public void actionPerformed(ActionEvent e) { 
        	    	System.out.println("Knopf1 gedrückt");
     	    		d.dispose(); 
        	} 
        } );
        panel.add(btn1);

        
        if (!btn2_txt.isEmpty()) {
            JButton btn2 = new JButton(btn2_txt);
            btn2.setFont(GraphicsHandler.usedFont((int)(15*Settings.factor)));
            btn2.setFocusable(false);
            btn2.setForeground(Color.WHITE);
            btn2.setBackground(Color.RED);
            btn2.addActionListener(new ActionListener() { 
                	public void actionPerformed(ActionEvent e) { 
                	    	System.out.println("Knopf2 gedrückt");
                	    	d.dispose(); 
                	} 
            } );
            panel.add(Box.createRigidArea(new Dimension(40, 0)));
            panel.add(btn2);
        }
        

        JLabel label = new JLabel(txt2);
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setFont(GraphicsHandler.usedFont((int)(30*Settings.factor)));
        label.setBackground(Color.ORANGE);
        label.setForeground(Color.WHITE);
        label.setOpaque(true);
        
        d.setLayout(new BorderLayout());
        d.add(panel, BorderLayout.SOUTH);
        d.add(label, BorderLayout.CENTER);

        d.getRootPane().setBorder( BorderFactory.createLineBorder(Color.RED, (int)(20*Settings.factor) , true) );
        //d.getRootPane().setBorder(new BevelBorder(BevelBorder.RAISED, Color.red, Color.pink));
        
        d.setUndecorated(true);
        d.pack();
        d.setLocationRelativeTo(null);
        d.setVisible(true);
    }
    

    public static void InfoPane(String txt1, String txt2, String btn1_txt) {
    	OptionPane(txt1, txt2, btn1_txt, "");
    }
    

}