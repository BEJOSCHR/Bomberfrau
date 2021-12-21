package uni.bombenstimmung.de.lobby;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JTextField;

import uni.bombenstimmung.de.backend.graphics.DisplayType;
import uni.bombenstimmung.de.backend.graphics.GraphicsHandler;

public class TextField extends JTextField{

	private static List<JTextField> textfields = new ArrayList<JTextField>(); 
	
	/**
	 * Inititalisiert alle Textfields
	 */
	public static void initTextField() {
		
		new JTextField("enter the text", 16);
		
	}
	
	
	
	public boolean isActiv() {
		return GraphicsHandler.getDisplayType() == DisplayType.LOADINGSCREEN;
	}
	
}
