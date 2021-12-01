/*
 * ImageHandler
 *
 * Version 1.0
 * Author: Benni
 *
 * Verwaltet alle Images (laden, ausgeben, verändern...)
 */
package uni.bombenstimmung.de.backend.images;

import java.util.ArrayList;
import java.util.List;

public class ImageHandler {

	public static final String PATH = "images/";
	private static List<LoadedImage> images = new ArrayList<LoadedImage>();
	
	/**
	 * Wird am start aufgerufen und läd alle Images
	 */
	public static void initImages() {
		
		//TODO ADD IMAGES TO LOAD HERE
		//EXAMPLE: new LoadedImage("test123.png", ImageType.IMAGE_MENU_XXX);
		
	}
	
	/**
	 * Added das angegebene Image zur images Liste (Wiord eigentlich nur aus den LoadedImages aufgerufen)
	 * @param img - Das zu addende Image
	 * @see LoadedImage
	 */
	public static void addImage(LoadedImage img) {
		
		images.add(img);
		
	}
	
	/**
	 * Gibt das Bild zum zugehörigen Type zurück (Wenn richtig geladen gibt es für jeden Type ein Image)
	 * @param type - Der Type der gesucht wird
	 * @see ImageType
	 * @return Das {@link LoadedImage} das zum Type gehört, wenn keins gefunden wird dann null
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
