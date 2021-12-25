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
	    	new LoadedImage("Grass_background.png", ImageType.IMAGE_INGAME_GRAS, GameData.FIELD_DIMENSION, GameData.FIELD_DIMENSION);
	    	new LoadedImage("Box.png", ImageType.IMAGE_INGAME_WALL, GameData.FIELD_DIMENSION, GameData.FIELD_DIMENSION);
	    	new LoadedImage("Wand_gray.png", ImageType.IMAGE_INGAME_BLOCK, GameData.FIELD_DIMENSION, GameData.FIELD_DIMENSION);
	    	new LoadedImage("wand_orange.png", ImageType.IMAGE_INGAME_BORDER, GameData.FIELD_DIMENSION, GameData.FIELD_DIMENSION);
	    	new LoadedImage("Yellow_background.png", ImageType.IMAGE_INGAME_YELLOWGRAS, GameData.FIELD_DIMENSION, GameData.FIELD_DIMENSION);
	    	new LoadedImage("bomb.png", ImageType.IMAGE_INGAME_BOMB, GameData.FIELD_DIMENSION-5, GameData.FIELD_DIMENSION-5);
	    	ConsoleHandler.print("Loaded images ("+images.size()+")", MessageType.BACKEND);
	    	new LoadedImage("lightTileBackground.png", ImageType.IMAGE_INGAME_LIGHTTILE, GameData.FIELD_DIMENSION, GameData.FIELD_DIMENSION);
	    	new LoadedImage("easteregg.png", ImageType.INGAME_SKIN_01, GameData.FIELD_DIMENSION*3, GameData.FIELD_DIMENSION*3);
	    	new LoadedImage("easteregg_Wasted.png", ImageType.INGAME_SKIN_01_WASTED, GameData.FIELD_DIMENSION*3, GameData.FIELD_DIMENSION*3);
	    	new LoadedImage("BombExplosion1.png", ImageType.IMAGE_INGAME_BOMB_EX1, GameData.FIELD_DIMENSION, GameData.FIELD_DIMENSION);
	    	new LoadedImage("BombExplosion2.png", ImageType.IMAGE_INGAME_BOMB_EX2, GameData.FIELD_DIMENSION, GameData.FIELD_DIMENSION);
	    	new LoadedImage("BombExplosion3.png", ImageType.IMAGE_INGAME_BOMB_EX3, GameData.FIELD_DIMENSION, GameData.FIELD_DIMENSION);
	    	new LoadedImage("upgrade_temp.png", ImageType.IMAGE_INGAME_UPGRADE_TEMP, GameData.FIELD_DIMENSION, GameData.FIELD_DIMENSION);
	    	new LoadedImage("item_bomb.png", ImageType.IMAGE_INGAME_UPGRADE_ITEM_BOMB, GameData.FIELD_DIMENSION, GameData.FIELD_DIMENSION);
	    	new LoadedImage("item_flame.png", ImageType.IMAGE_INGAME_UPGRADE_ITEM_FIRE, GameData.FIELD_DIMENSION, GameData.FIELD_DIMENSION);
	    	new LoadedImage("item_shoe.png", ImageType.IMAGE_INGAME_UPGRADE_ITEM_SHOE, GameData.FIELD_DIMENSION, GameData.FIELD_DIMENSION);
	    	new LoadedImage("tile_env3_block.png", ImageType.IMAGE_INGAME_LAVA_BLOCK, GameData.FIELD_DIMENSION, GameData.FIELD_DIMENSION);
	    	new LoadedImage("tile_env3_floor.png", ImageType.IMAGE_INGAME_LAVA_FLOOR, GameData.FIELD_DIMENSION, GameData.FIELD_DIMENSION);
	    	new LoadedImage("tile_env3_wall.png", ImageType.IMAGE_INGAME_LAVA_WALL, GameData.FIELD_DIMENSION, GameData.FIELD_DIMENSION);
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
