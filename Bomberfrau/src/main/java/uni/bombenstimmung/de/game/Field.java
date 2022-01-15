
/*	Diese Klasse ist von Dennis und wurde von Mustafa überarbeitet
 * 	Sie dient dazu, die einzelnen Felder und ihre Inhalte 
 * 	der Map zu verwalten. 
=======
/*
 * Field
 *
 * Version 1.0
 * Author: Dennis
 *
 * Verwaltet die einzelnen Felder einer Map
>>>>>>> branch 'InGame' of git@github.com:BEJOSCHR/Bomberfrau.git
 */
package uni.bombenstimmung.de.game;

import java.awt.Graphics;

import uni.bombenstimmung.de.backend.console.ConsoleHandler;
import uni.bombenstimmung.de.backend.console.MessageType;
import uni.bombenstimmung.de.backend.images.ImageHandler;
import uni.bombenstimmung.de.backend.images.ImageType;

public class Field extends Entity {

    private FieldContent content;
    
    /**
     * Erzeugt ein Feld
     * @param x, Startpunkt X
     * @param y, Startpunkt Y
     * @param cont, Typ des Feldes
     */
    public Field(int x, int y, FieldContent cont) {
	super.xPosition = x;
	super.yPosition = y;
	content = cont;
    }
    
    /**
     * Gibt einen String zurück, der den Typ des Feldes repräsentiert
     * @param type, FieldContent eingeben
     */
    public static String getFieldTypeRepresentation(FieldContent type) {
	switch(type) { 
		case BLOCK:
			return "BL";
		case BORDER: // wand_orange.png wird angezeigt 
			return "BO";
		case EMPTY: //freier Space wo dann Gras Tile gezeigt wird
			return "EM";
		case WALL: //Box.png, die Sachen die zerstört werden können 
			return "WA";
		default:
			ConsoleHandler.print("Unknown fieldtype '"+type+"'!");
			return "##";
	}
    }
    
    
    /**
     * Wandelt einen String in einen FieldContent um
     * @param representation, String eingabe (BL,BO,EM,WA)
     * Wichtig für den Aufbau der Map
     */
    public static FieldContent getFieldTypeFromRepresentation(String representation) {
	
	switch(representation) {
		case "BL":
			return FieldContent.BLOCK;
		case "BO":
		    return FieldContent.BORDER;
		case "EM":
		    return FieldContent.EMPTY;
		case "WA":
		    return FieldContent.WALL;
		default:
		    ConsoleHandler.print("Unknown fieldtype representation '"+representation+"'!");
		    return FieldContent.EMPTY;
	}
    }
    
    /**
     * Malt das spezifische Feld an den angegebenen Koordinaten auf der Map
     * @param x, X-Koordinate
     * @param y, Y-Koordinate
     * @param cont, FieldContent
     */
    public void drawField(Graphics g, int x, int y, FieldContent cont) {
	switch (cont) {
		case BLOCK:
		g.drawImage(ImageHandler.getImage(ImageType.IMAGE_INGAME_BLOCK).getImage(), x, y, GameData.FIELD_DIMENSION, GameData.FIELD_DIMENSION, null);
		break;
	    case WALL:
		if (Game.getMapNumber() == 3) {
			g.drawImage(ImageHandler.getImage(ImageType.IMAGE_INGAME_LAVA_WALL).getImage(), x, y, GameData.FIELD_DIMENSION, GameData.FIELD_DIMENSION, null);
		}
	    	g.drawImage(ImageHandler.getImage(ImageType.IMAGE_INGAME_WALL).getImage(), x, y, GameData.FIELD_DIMENSION, GameData.FIELD_DIMENSION, null);
		break;
	    case BORDER:
	    	if (Game.getMapNumber() == 3) {
				g.drawImage(ImageHandler.getImage(ImageType.IMAGE_INGAME_LAVA_BLOCK).getImage(), x, y, GameData.FIELD_DIMENSION, GameData.FIELD_DIMENSION, null);
	    	} else {
	    		g.drawImage(ImageHandler.getImage(ImageType.IMAGE_INGAME_BORDER).getImage(), x, y, GameData.FIELD_DIMENSION, GameData.FIELD_DIMENSION, null);
	    	}
		break;
	    case BOMB:
		if (Game.getMapNumber() == 1) {
		    g.drawImage(ImageHandler.getImage(ImageType.IMAGE_INGAME_GRAS).getImage(), x, y, GameData.FIELD_DIMENSION, GameData.FIELD_DIMENSION, null);
		} 	else if (Game.getMapNumber() == 2) {
		    	g.drawImage(ImageHandler.getImage(ImageType.IMAGE_INGAME_YELLOWGRAS).getImage(), x, y, GameData.FIELD_DIMENSION, GameData.FIELD_DIMENSION, null);
		}	else if (Game.getMapNumber() == 3) {
			g.drawImage(ImageHandler.getImage(ImageType.IMAGE_INGAME_LAVA_FLOOR).getImage(), x, y, GameData.FIELD_DIMENSION, GameData.FIELD_DIMENSION, null);
		}
		try {
		    for(Bomb i : Game.getPlacedBombs()) {
			    if (this == i.getPlacedField()) {
				g.drawImage(ImageHandler.getImage(ImageType.IMAGE_INGAME_BOMB).getImage(), x + 4 + (int)((1 - i.getScale()) * 16D), y + 4 + (int)((1 - i.getScale()) * 16D), (int)((GameData.FIELD_DIMENSION-5) * i.getScale()), (int)((GameData.FIELD_DIMENSION-5) * i.getScale()), null);
			    }
		    }
		}
		catch (Exception ex) {
		    ConsoleHandler.print("Fehler beim Malen der Bomben!!!", MessageType.GAME);
		}
		break;
	// Spezifische Grafiken für die Himmelsrichtungen der Explosion
	    case EXPLOSION1:
		if (Game.getMapNumber() == 1) {
		    g.drawImage(ImageHandler.getImage(ImageType.IMAGE_INGAME_GRAS).getImage(), x, y, GameData.FIELD_DIMENSION, GameData.FIELD_DIMENSION, null);
		} 	else if (Game.getMapNumber() == 2) {
		    	g.drawImage(ImageHandler.getImage(ImageType.IMAGE_INGAME_YELLOWGRAS).getImage(), x, y, GameData.FIELD_DIMENSION, GameData.FIELD_DIMENSION, null);
		}	else if (Game.getMapNumber() == 3) {
					g.drawImage(ImageHandler.getImage(ImageType.IMAGE_INGAME_LAVA_FLOOR).getImage(), x, y, GameData.FIELD_DIMENSION, GameData.FIELD_DIMENSION, null);
				}
		g.drawImage(ImageHandler.getImage(ImageType.IMAGE_INGAME_BOMB_EX1).getImage(), x, y, GameData.FIELD_DIMENSION, GameData.FIELD_DIMENSION, null);
		break;
	    case EXPLOSION2:
		if (Game.getMapNumber() == 1) {
		    g.drawImage(ImageHandler.getImage(ImageType.IMAGE_INGAME_GRAS).getImage(), x, y, GameData.FIELD_DIMENSION, GameData.FIELD_DIMENSION, null);
		} 	else if (Game.getMapNumber() == 2) {
		    	g.drawImage(ImageHandler.getImage(ImageType.IMAGE_INGAME_YELLOWGRAS).getImage(), x, y, GameData.FIELD_DIMENSION, GameData.FIELD_DIMENSION, null);
		}	else if (Game.getMapNumber() == 3) {
					g.drawImage(ImageHandler.getImage(ImageType.IMAGE_INGAME_LAVA_FLOOR).getImage(), x, y, GameData.FIELD_DIMENSION, GameData.FIELD_DIMENSION, null);
				}
		g.drawImage(ImageHandler.getImage(ImageType.IMAGE_INGAME_BOMB_EX2).getImage(), x, y, GameData.FIELD_DIMENSION, GameData.FIELD_DIMENSION, null);
		break;
	    case EXPLOSION2_NS:
		if (Game.getMapNumber() == 1) {
		    g.drawImage(ImageHandler.getImage(ImageType.IMAGE_INGAME_GRAS).getImage(), x, y, GameData.FIELD_DIMENSION, GameData.FIELD_DIMENSION, null);
		} 	else if (Game.getMapNumber() == 2) {
		    	g.drawImage(ImageHandler.getImage(ImageType.IMAGE_INGAME_YELLOWGRAS).getImage(), x, y, GameData.FIELD_DIMENSION, GameData.FIELD_DIMENSION, null);
		}	else if (Game.getMapNumber() == 3) {
					g.drawImage(ImageHandler.getImage(ImageType.IMAGE_INGAME_LAVA_FLOOR).getImage(), x, y, GameData.FIELD_DIMENSION, GameData.FIELD_DIMENSION, null);
				}
		g.drawImage(ImageHandler.getImage(ImageType.IMAGE_INGAME_BOMB_EX2_NS).getImage(), x, y, GameData.FIELD_DIMENSION, GameData.FIELD_DIMENSION, null);
		break;
	    case EXPLOSION3_N:
		if (Game.getMapNumber() == 1) {
		    g.drawImage(ImageHandler.getImage(ImageType.IMAGE_INGAME_GRAS).getImage(), x, y, GameData.FIELD_DIMENSION, GameData.FIELD_DIMENSION, null);
		} 	else if (Game.getMapNumber() == 2) {
		    	g.drawImage(ImageHandler.getImage(ImageType.IMAGE_INGAME_YELLOWGRAS).getImage(), x, y, GameData.FIELD_DIMENSION, GameData.FIELD_DIMENSION, null);
		}	else if (Game.getMapNumber() == 3) {
					g.drawImage(ImageHandler.getImage(ImageType.IMAGE_INGAME_LAVA_FLOOR).getImage(), x, y, GameData.FIELD_DIMENSION, GameData.FIELD_DIMENSION, null);
				}
		g.drawImage(ImageHandler.getImage(ImageType.IMAGE_INGAME_BOMB_EX3_N).getImage(), x, y, GameData.FIELD_DIMENSION, GameData.FIELD_DIMENSION, null);
		break;
	    case EXPLOSION3_S:
		if (Game.getMapNumber() == 1) {
		    g.drawImage(ImageHandler.getImage(ImageType.IMAGE_INGAME_GRAS).getImage(), x, y, GameData.FIELD_DIMENSION, GameData.FIELD_DIMENSION, null);
		} 	else if (Game.getMapNumber() == 2) {
		    	g.drawImage(ImageHandler.getImage(ImageType.IMAGE_INGAME_YELLOWGRAS).getImage(), x, y, GameData.FIELD_DIMENSION, GameData.FIELD_DIMENSION, null);
		}	else if (Game.getMapNumber() == 3) {
					g.drawImage(ImageHandler.getImage(ImageType.IMAGE_INGAME_LAVA_FLOOR).getImage(), x, y, GameData.FIELD_DIMENSION, GameData.FIELD_DIMENSION, null);
				}
		g.drawImage(ImageHandler.getImage(ImageType.IMAGE_INGAME_BOMB_EX3_S).getImage(), x, y, GameData.FIELD_DIMENSION, GameData.FIELD_DIMENSION, null);
		break;
	    case EXPLOSION3_W:
		if (Game.getMapNumber() == 1) {
		    g.drawImage(ImageHandler.getImage(ImageType.IMAGE_INGAME_GRAS).getImage(), x, y, GameData.FIELD_DIMENSION, GameData.FIELD_DIMENSION, null);
		} 	else if (Game.getMapNumber() == 2) {
		    	g.drawImage(ImageHandler.getImage(ImageType.IMAGE_INGAME_YELLOWGRAS).getImage(), x, y, GameData.FIELD_DIMENSION, GameData.FIELD_DIMENSION, null);
		}	else if (Game.getMapNumber() == 3) {
					g.drawImage(ImageHandler.getImage(ImageType.IMAGE_INGAME_LAVA_FLOOR).getImage(), x, y, GameData.FIELD_DIMENSION, GameData.FIELD_DIMENSION, null);
				}
		g.drawImage(ImageHandler.getImage(ImageType.IMAGE_INGAME_BOMB_EX3_W).getImage(), x, y, GameData.FIELD_DIMENSION, GameData.FIELD_DIMENSION, null);
		break;
	    case EXPLOSION3_O:
		if (Game.getMapNumber() == 1) {
		    g.drawImage(ImageHandler.getImage(ImageType.IMAGE_INGAME_GRAS).getImage(), x, y, GameData.FIELD_DIMENSION, GameData.FIELD_DIMENSION, null);
		} 	else if (Game.getMapNumber() == 2) {
		    	g.drawImage(ImageHandler.getImage(ImageType.IMAGE_INGAME_YELLOWGRAS).getImage(), x, y, GameData.FIELD_DIMENSION, GameData.FIELD_DIMENSION, null);
		}	else if (Game.getMapNumber() == 3) {
					g.drawImage(ImageHandler.getImage(ImageType.IMAGE_INGAME_LAVA_FLOOR).getImage(), x, y, GameData.FIELD_DIMENSION, GameData.FIELD_DIMENSION, null);
				}
		g.drawImage(ImageHandler.getImage(ImageType.IMAGE_INGAME_BOMB_EX3_O).getImage(), x, y, GameData.FIELD_DIMENSION, GameData.FIELD_DIMENSION, null);
		break;
	    case EMPTY:
		if (Game.getMapNumber() == 1) {
		    g.drawImage(ImageHandler.getImage(ImageType.IMAGE_INGAME_GRAS).getImage(), x, y, GameData.FIELD_DIMENSION, GameData.FIELD_DIMENSION, null);
		} 	else if (Game.getMapNumber() == 2) {
		    	g.drawImage(ImageHandler.getImage(ImageType.IMAGE_INGAME_YELLOWGRAS).getImage(), x, y, GameData.FIELD_DIMENSION, GameData.FIELD_DIMENSION, null);
		}	else if (Game.getMapNumber() == 3) {
					g.drawImage(ImageHandler.getImage(ImageType.IMAGE_INGAME_LAVA_FLOOR).getImage(), x, y, GameData.FIELD_DIMENSION, GameData.FIELD_DIMENSION, null);
				}
		break;
	    case UPGRADE_ITEM_BOMB:
		g.drawImage(ImageHandler.getImage(ImageType.IMAGE_INGAME_UPGRADE_ITEM_BOMB).getImage(), x, y, GameData.FIELD_DIMENSION, GameData.FIELD_DIMENSION, null);
		break;
	    case UPGRADE_ITEM_FIRE:
		g.drawImage(ImageHandler.getImage(ImageType.IMAGE_INGAME_UPGRADE_ITEM_FIRE).getImage(), x, y, GameData.FIELD_DIMENSION, GameData.FIELD_DIMENSION, null);
		break;
	    case UPGRADE_ITEM_SHOE:
		g.drawImage(ImageHandler.getImage(ImageType.IMAGE_INGAME_UPGRADE_ITEM_SHOE).getImage(), x, y, GameData.FIELD_DIMENSION, GameData.FIELD_DIMENSION, null);
		break;
	  
	}
    }
    
    public void setContent (FieldContent t) {
	content = t;
    }
    
    public FieldContent getContent() {
	return content;
    }
    
}
