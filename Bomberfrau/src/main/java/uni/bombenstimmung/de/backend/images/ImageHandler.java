/*
 * ImageHandler
 *
 * Version 1.0
 * Author: Benni
 *
 * Verwaltet alle Images (laden, ausgeben, ver�ndern...)
 */
package uni.bombenstimmung.de.backend.images;

import java.util.ArrayList;
import java.util.List;

import uni.bombenstimmung.de.backend.console.ConsoleHandler;
import uni.bombenstimmung.de.backend.console.MessageType;

public class ImageHandler {

	public static final String PATH = "images/";
	private static List<LoadedImage> images = new ArrayList<LoadedImage>();
	
	/**
	 * Wird am start aufgerufen und l�d alle Images
	 */
	public static void initImages() {
		
		//TODO ADD IMAGES TO LOAD HERE
		//EXAMPLE: new LoadedImage("test123.png", ImageType.IMAGE_MENU_XXX);

	    	new LoadedImage("logo_white.png", ImageType.IMAGE_INTRO_PIC);
	    	new LoadedImage("menu.png", ImageType.IMAGE_MENU_PIC);
	    	new LoadedImage("menutitle.png", ImageType.IMAGE_MENU_TITLE);
	    	
	    	new LoadedImage("Grass_background.png", ImageType.IMAGE_INGAME_GRAS, GameData.FIELD_DIMENSION, GameData.FIELD_DIMENSION);
	    	new LoadedImage("Box.png", ImageType.IMAGE_INGAME_WALL, GameData.FIELD_DIMENSION, GameData.FIELD_DIMENSION);
	    	new LoadedImage("Wand_gray.png", ImageType.IMAGE_INGAME_BLOCK, GameData.FIELD_DIMENSION, GameData.FIELD_DIMENSION);
	    	new LoadedImage("wand_orange.png", ImageType.IMAGE_INGAME_BORDER, GameData.FIELD_DIMENSION, GameData.FIELD_DIMENSION);
		
		//LOBBY PICS
		// Arrow Location: https://pixabay.com/illustrations/arrow-choose-select-next-button-1217949/
		new LoadedImage("Lobby/arrow-Left.png", ImageType.IMAGE_LOBBY_ARROW_LEFT, 50, 50);
		new LoadedImage("Lobby/arrow-Left.png", ImageType.IMAGE_LOBBY_ARROW_LEFT_BIGGER, 55, 55);
		new LoadedImage("Lobby/arrow-Right.png", ImageType.IMAGE_LOBBY_ARROW_RIGHT, 50, 50);
		new LoadedImage("Lobby/arrow-Right.png", ImageType.IMAGE_LOBBY_ARROW_RIGHT_BIGGER, 55, 55);
		new LoadedImage("Lobby/MapSelection_Platzhalter_1.png", ImageType.IMAGE_LOBBY_MAPSELECTION_PLATZHALTER_1, 200, 200);
		new LoadedImage("Lobby/MapSelection_Platzhalter_2.png", ImageType.IMAGE_LOBBY_MAPSELECTION_PLATZHALTER_2, 200, 200);
		new LoadedImage("Lobby/MapSelection_Platzhalter_3.png", ImageType.IMAGE_LOBBY_MAPSELECTION_PLATZHALTER_3, 200, 200);
		
		new LoadedImage("Lobby/SkinSelection_Platzhalter_1.png", ImageType.IMAGE_LOBBY_SKINSELECTION_PLATZHALTER_1, 200, 200);
		new LoadedImage("Lobby/SkinSelection_Platzhalter_2.jpg", ImageType.IMAGE_LOBBY_SKINSELECTION_PLATZHALTER_2, 200, 200);
		new LoadedImage("Lobby/SkinSelection_Platzhalter_3.jpg", ImageType.IMAGE_LOBBY_SKINSELECTION_PLATZHALTER_3, 200, 200);
		
		new LoadedImage("Lobby/checkmark.png", ImageType.IMAGE_LOBBY_CHECKMARK, 70, 70);
		
		//https://www.pngwing.com/en/free-png-zekql/download?width=107
		new LoadedImage("Lobby/Crown.png", ImageType.IMAGE_LOBBY_CROWN, 25, 25);
		
		ConsoleHandler.print("Loaded images ("+images.size()+")", MessageType.BACKEND);
		
	}
	
	/**
	 * Added das angegebene Image zur images Liste (Wird eigentlich nur aus den LoadedImages aufgerufen)
	 * @param img - Das zu addende Image
	 * @see LoadedImage
	 */
	public static void addImage(LoadedImage img) {
		
		images.add(img);
		
	}
	
	/**
	 * Gibt das Bild zum zugeh�rigen Type zur�ck (Wenn richtig geladen gibt es f�r jeden Type ein Image)
	 * @param type - Der Type der gesucht wird
	 * @see ImageType
	 * @return Das {@link LoadedImage} das zum Type geh�rt, wenn keins gefunden wird dann null
	 */
	public static LoadedImage getImage(ImageType type) {
		
		for(LoadedImage img : images) {
		    	if(img.getType() == type) {
				return img;
			}
		}
		
		return null;
	}
	
}
