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
		
		//LOBBY PICS
		// Arrow Location: https://pixabay.com/illustrations/arrow-choose-select-next-button-1217949/
		new LoadedImage("Lobby/arrow-Left.png", ImageType.IMAGE_LOBBY_ARROW_LEFT, 50, 50);
		new LoadedImage("Lobby/arrow-Left.png", ImageType.IMAGE_LOBBY_ARROW_LEFT_BIGGER, 55, 55);
		new LoadedImage("Lobby/arrow-Right.png", ImageType.IMAGE_LOBBY_ARROW_RIGHT, 50, 50);
		new LoadedImage("Lobby/arrow-Right.png", ImageType.IMAGE_LOBBY_ARROW_RIGHT_BIGGER, 55, 55);
		new LoadedImage("Lobby/MapSelection_Platzhalter_1.jpg", ImageType.IMAGE_LOBBY_MAPSELECTION_PLATZHALTER_1, 100, 100);
		new LoadedImage("Lobby/MapSelection_Platzhalter_2.png", ImageType.IMAGE_LOBBY_MAPSELECTION_PLATZHALTER_2, 100, 100);
		new LoadedImage("Lobby/MapSelection_Platzhalter_3.png", ImageType.IMAGE_LOBBY_MAPSELECTION_PLATZHALTER_3, 100, 100);
		
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
