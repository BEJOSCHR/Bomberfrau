/*	Dieses Enum ist von Dennis und dient dazu
 * 	den Feldern der Map Zustände zuzuordnen
 */

package uni.bombenstimmung.de.game;

import uni.bombenstimmung.de.backend.console.*;

public enum FieldContent {
    WALL,
    BOMB,
    BORDER,
    UPGRADE,
    EMPTY,
    BLOCK;
	
	
	
public static String getFieldTypeRepresentation(FieldContent type) {
		
		switch(type) {
		case BLOCK:
			return "BL";
		case BORDER:
			return "BO";
		case EMPTY:
			return "EM";
		case WALL:
			return "WA";
		default:
			ConsoleHandler.print("Unknown fieldtype '"+type+"'!");
			return "##";
		}
		
	}


public static FieldContent getFieldTypeFromRepresentation(String representation) {
	
	switch(representation) {
	case "BL":
		return BLOCK;
	case "BO":
		return BORDER;
	case "EM":
		return EMPTY;
	case "WA":
		return WALL;
	default:
		ConsoleHandler.print("Unknown fieldtype representation '"+representation+"'!");
		return EMPTY;
	}
	
}
}
