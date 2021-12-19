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
import uni.bombenstimmung.de.game.GameData;

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
	    	new LoadedImage("mainmenu.png", ImageType.IMAGE_MENU_PIC);
	    	new LoadedImage("optionsmenu.png", ImageType.IMAGE_OPTIONS_PIC);
	    	
	    	new LoadedImage("Grass_background.png", ImageType.IMAGE_INGAME_GRAS, GameData.FIELD_DIMENSION, GameData.FIELD_DIMENSION);
	    	new LoadedImage("Box.png", ImageType.IMAGE_INGAME_WALL, GameData.FIELD_DIMENSION, GameData.FIELD_DIMENSION);
	    	new LoadedImage("Wand_gray.png", ImageType.IMAGE_INGAME_BLOCK, GameData.FIELD_DIMENSION, GameData.FIELD_DIMENSION);
	    	new LoadedImage("wand_orange.png", ImageType.IMAGE_INGAME_BORDER, GameData.FIELD_DIMENSION, GameData.FIELD_DIMENSION);
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
