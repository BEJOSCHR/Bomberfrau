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
	 * Wird am Start aufgerufen und l�d alle Images
	 */
	public static void initImages() {
		
		//TODO ADD IMAGES TO LOAD HERE
		//EXAMPLE: new LoadedImage("test123.png", ImageType.IMAGE_MENU_XXX);

	    	new LoadedImage("Menu/logo.png", ImageType.IMAGE_INTRO_PIC);
	    	new LoadedImage("Menu/menu.png", ImageType.IMAGE_MENU_PIC);
	    	new LoadedImage("Menu/menutitle.png", ImageType.IMAGE_MENU_TITLE);
	    	
		//LOBBY PICS
		// Arrow Location: https://pixabay.com/illustrations/arrow-choose-select-next-button-1217949/
		new LoadedImage("Lobby/arrow-Left.png", ImageType.IMAGE_LOBBY_ARROW_LEFT);
		new LoadedImage("Lobby/arrow-Right.png", ImageType.IMAGE_LOBBY_ARROW_RIGHT);
		new LoadedImage("Lobby/Map_1.png", ImageType.IMAGE_LOBBY_MAP_1);
		new LoadedImage("Lobby/Map_2.png", ImageType.IMAGE_LOBBY_MAP_2);
		new LoadedImage("Lobby/Map_3.png", ImageType.IMAGE_LOBBY_MAP_3);
		
		new LoadedImage("Lobby/Skin_0.png", ImageType.IMAGE_LOBBY_SKIN_0);
		new LoadedImage("Lobby/Skin_1.png", ImageType.IMAGE_LOBBY_SKIN_1);
		new LoadedImage("Lobby/Skin_2.png", ImageType.IMAGE_LOBBY_SKIN_2);
		new LoadedImage("Lobby/Skin_3.png", ImageType.IMAGE_LOBBY_SKIN_3);
		//https://toppng.com/photo/99910/green-check-mark
		new LoadedImage("Lobby/checkmark.png", ImageType.IMAGE_LOBBY_CHECKMARK);
		
		// https://toppng.com/photo/205568/red-cross-transparent-png-red-cross-image-transparent-background
		new LoadedImage("Lobby/Red_Cross.png", ImageType.IMAGE_LOBBY_CROSS);
		//https://www.pngwing.com/en/free-png-zekql/download?width=107
		new LoadedImage("Lobby/Crown.png", ImageType.IMAGE_LOBBY_CROWN);
		
		
		//LOBBY UND INGAME SHARING ICONS
		new LoadedImage("Bomberman_Icon.png", ImageType.IMAGE_INGAME_ICON, 200, 200);
		new LoadedImage("Bomberman-icon_blau.png", ImageType.IMAGE_INGAME_ICON_BLUE, 200, 200);
		new LoadedImage("Bomberman-icon_gelb.png", ImageType.IMAGE_INGAME_ICON_GELB, 200, 200);
		new LoadedImage("Bomberman-icon_pink.png", ImageType.IMAGE_INGAME_ICON_PINK, 200, 200);
		
		//INGAME
		new LoadedImage("Ingame/Grass_background.png", ImageType.IMAGE_INGAME_GRAS, GameData.FIELD_DIMENSION, GameData.FIELD_DIMENSION);
	    	new LoadedImage("Ingame/Box.png", ImageType.IMAGE_INGAME_WALL, GameData.FIELD_DIMENSION, GameData.FIELD_DIMENSION);
	    	new LoadedImage("Ingame/Wand_gray.png", ImageType.IMAGE_INGAME_BLOCK, GameData.FIELD_DIMENSION, GameData.FIELD_DIMENSION);
	    	new LoadedImage("Ingame/wand_orange.png", ImageType.IMAGE_INGAME_BORDER, GameData.FIELD_DIMENSION, GameData.FIELD_DIMENSION);
	    	new LoadedImage("Ingame/Yellow_background.png", ImageType.IMAGE_INGAME_YELLOWGRAS, GameData.FIELD_DIMENSION, GameData.FIELD_DIMENSION);
	    	new LoadedImage("Ingame/bomb.png", ImageType.IMAGE_INGAME_BOMB, GameData.FIELD_DIMENSION-5, GameData.FIELD_DIMENSION-5);
	    	new LoadedImage("Ingame/lightTileBackground.png", ImageType.IMAGE_INGAME_LIGHTTILE, GameData.FIELD_DIMENSION, GameData.FIELD_DIMENSION);
	    	new LoadedImage("Ingame/new_skin_left.png", ImageType.INGAME_SKIN_01, (int)(GameData.FIELD_DIMENSION*2.1), (int)(GameData.FIELD_DIMENSION*2.1));
	    	new LoadedImage("Ingame/new_skin_left_wasted.png", ImageType.INGAME_SKIN_01_WASTED, (int)(GameData.FIELD_DIMENSION*2.1), (int)(GameData.FIELD_DIMENSION*2.1));
	    	new LoadedImage("Ingame/ingame_wasted.png", ImageType.IMAGE_INGAME_WASTED, (int)(GameData.FIELD_DIMENSION*2.1), (int)(GameData.FIELD_DIMENSION*2.1));
	    	new LoadedImage("Ingame/flame1.png", ImageType.IMAGE_INGAME_BOMB_EX1, GameData.FIELD_DIMENSION, GameData.FIELD_DIMENSION);
	    	new LoadedImage("Ingame/flame1_horizontal.png", ImageType.IMAGE_INGAME_BOMB_EX2, GameData.FIELD_DIMENSION, GameData.FIELD_DIMENSION);
	    	new LoadedImage("Ingame/flame1_vertical.png", ImageType.IMAGE_INGAME_BOMB_EX2_NS, GameData.FIELD_DIMENSION, GameData.FIELD_DIMENSION);
	    	new LoadedImage("Ingame/flame1_right.png", ImageType.IMAGE_INGAME_BOMB_EX3_O, GameData.FIELD_DIMENSION, GameData.FIELD_DIMENSION);
	    	new LoadedImage("Ingame/flame1_left.png", ImageType.IMAGE_INGAME_BOMB_EX3_W, GameData.FIELD_DIMENSION, GameData.FIELD_DIMENSION);
	    	new LoadedImage("Ingame/flame1_down.png", ImageType.IMAGE_INGAME_BOMB_EX3_S, GameData.FIELD_DIMENSION, GameData.FIELD_DIMENSION);
	    	new LoadedImage("Ingame/flame1_up.png", ImageType.IMAGE_INGAME_BOMB_EX3_N, GameData.FIELD_DIMENSION, GameData.FIELD_DIMENSION);
	    	new LoadedImage("Ingame/upgrade_temp.png", ImageType.IMAGE_INGAME_UPGRADE_TEMP, GameData.FIELD_DIMENSION, GameData.FIELD_DIMENSION);
	    	new LoadedImage("Ingame/item_bomb.png", ImageType.IMAGE_INGAME_UPGRADE_ITEM_BOMB, GameData.FIELD_DIMENSION, GameData.FIELD_DIMENSION);
	    	new LoadedImage("Ingame/item_flame.png", ImageType.IMAGE_INGAME_UPGRADE_ITEM_FIRE, GameData.FIELD_DIMENSION, GameData.FIELD_DIMENSION);
	    	new LoadedImage("Ingame/item_shoe.png", ImageType.IMAGE_INGAME_UPGRADE_ITEM_SHOE, GameData.FIELD_DIMENSION, GameData.FIELD_DIMENSION);
	    	new LoadedImage("Ingame/tile_env3_block.png", ImageType.IMAGE_INGAME_LAVA_BLOCK, GameData.FIELD_DIMENSION, GameData.FIELD_DIMENSION);
	    	new LoadedImage("Ingame/tile_env3_floor.png", ImageType.IMAGE_INGAME_LAVA_FLOOR, GameData.FIELD_DIMENSION, GameData.FIELD_DIMENSION);
	    	new LoadedImage("Ingame/tile_env3_wall.png", ImageType.IMAGE_INGAME_LAVA_WALL, GameData.FIELD_DIMENSION, GameData.FIELD_DIMENSION);
	    	//Character 1
	    	new LoadedImage("Ingame/Characters/Priest_Dead.png", ImageType.IMAGE_INGAME_CHARACTER_DEAD, GameData.FIELD_DIMENSION, GameData.FIELD_DIMENSION);
	    	new LoadedImage("Ingame/Characters/Priest_Idle.png", ImageType.IMAGE_INGAME_CHARACTER_IDLE, GameData.FIELD_DIMENSION, GameData.FIELD_DIMENSION);
		new LoadedImage("Ingame/Characters/Priest_Walk_1.png", ImageType.IMAGE_INGAME_CHARACTER_WALK_1, GameData.FIELD_DIMENSION, GameData.FIELD_DIMENSION);
		new LoadedImage("Ingame/Characters/Priest_Walk_2.png", ImageType.IMAGE_INGAME_CHARACTER_WALK_2, GameData.FIELD_DIMENSION, GameData.FIELD_DIMENSION);
		new LoadedImage("Ingame/Characters/Priest_Walk_Side_Left_1.png", ImageType.IMAGE_INGAME_CHARACTER_WALK_SIDE_LEFT_1, GameData.FIELD_DIMENSION, GameData.FIELD_DIMENSION);
		new LoadedImage("Ingame/Characters/Priest_Walk_Side_Left_Idle.png", ImageType.IMAGE_INGAME_CHARACTER_WALK_SIDE_LEFT_IDLE, GameData.FIELD_DIMENSION, GameData.FIELD_DIMENSION);
		new LoadedImage("Ingame/Characters/Priest_Walk_Side_Right_1.png", ImageType.IMAGE_INGAME_CHARACTER_WALK_SIDE_RIGHT_1, GameData.FIELD_DIMENSION, GameData.FIELD_DIMENSION);
		new LoadedImage("Ingame/Characters/Priest_Walk_Side_Right_Idle.png", ImageType.IMAGE_INGAME_CHARACTER_WALK_SIDE_RIGHT_IDLE, GameData.FIELD_DIMENSION, GameData.FIELD_DIMENSION);
		new LoadedImage("Ingame/Characters/Priest_Walk_North_1.png", ImageType.IMAGE_INGAME_CHARACTER_WALK_NORTH_1, GameData.FIELD_DIMENSION, GameData.FIELD_DIMENSION);
		new LoadedImage("Ingame/Characters/Priest_Walk_North_Idle.png", ImageType.IMAGE_INGAME_CHARACTER_WALK_NORTH_IDLE, GameData.FIELD_DIMENSION, GameData.FIELD_DIMENSION);
		//Character 2
		new LoadedImage("Ingame/Characters/Skeleton_Dead.png", ImageType.IMAGE_INGAME_SKELETON_DEAD, GameData.FIELD_DIMENSION, GameData.FIELD_DIMENSION);
		new LoadedImage("Ingame/Characters/Skeleton_Idle.png", ImageType.IMAGE_INGAME_SKELETON_IDLE, GameData.FIELD_DIMENSION, GameData.FIELD_DIMENSION);
		new LoadedImage("Ingame/Characters/Skeleton_Walk_1.png", ImageType.IMAGE_INGAME_SKELETON_WALK_1, GameData.FIELD_DIMENSION, GameData.FIELD_DIMENSION);
		new LoadedImage("Ingame/Characters/Skeleton_Walk_2.png", ImageType.IMAGE_INGAME_SKELETON_WALK_2, GameData.FIELD_DIMENSION, GameData.FIELD_DIMENSION);
		new LoadedImage("Ingame/Characters/Skeleton_Walk_Left_1.png", ImageType.IMAGE_INGAME_SKELETON_LEFT_1, GameData.FIELD_DIMENSION, GameData.FIELD_DIMENSION);
		new LoadedImage("Ingame/Characters/Skeleton_Walk_Left_2.png", ImageType.IMAGE_INGAME_SKELETON_LEFT_2, GameData.FIELD_DIMENSION, GameData.FIELD_DIMENSION);
		new LoadedImage("Ingame/Characters/Skeleton_Walk_Left_Idle.png", ImageType.IMAGE_INGAME_SKELETON_LEFT_IDLE, GameData.FIELD_DIMENSION, GameData.FIELD_DIMENSION);
		new LoadedImage("Ingame/Characters/Skeleton_Walk_North_1.png", ImageType.IMAGE_INGAME_SKELETON_NORTH_1, GameData.FIELD_DIMENSION, GameData.FIELD_DIMENSION);
		new LoadedImage("Ingame/Characters/Skeleton_Walk_North_2.png", ImageType.IMAGE_INGAME_SKELETON_NORTH_2, GameData.FIELD_DIMENSION, GameData.FIELD_DIMENSION);
		new LoadedImage("Ingame/Characters/Skeleton_Walk_North_Idle.png", ImageType.IMAGE_INGAME_SKELETON_NORTH_IDLE, GameData.FIELD_DIMENSION, GameData.FIELD_DIMENSION);
		new LoadedImage("Ingame/Characters/Skeleton_Walk_Right_1.png", ImageType.IMAGE_INGAME_SKELETON_RIGHT_1, GameData.FIELD_DIMENSION, GameData.FIELD_DIMENSION);
		new LoadedImage("Ingame/Characters/Skeleton_Walk_Right_2.png", ImageType.IMAGE_INGAME_SKELETON_RIGHT_2, GameData.FIELD_DIMENSION, GameData.FIELD_DIMENSION);
		new LoadedImage("Ingame/Characters/Skeleton_Walk_Right_Idle.png", ImageType.IMAGE_INGAME_SKELETON_RIGHT_IDLE, GameData.FIELD_DIMENSION, GameData.FIELD_DIMENSION);
		//Character 3
		new LoadedImage("Ingame/Characters/Red_Dead.png", ImageType.IMAGE_INGAME_RED_DEAD, GameData.FIELD_DIMENSION, GameData.FIELD_DIMENSION);
		new LoadedImage("Ingame/Characters/Red_Walk_Idle.png", ImageType.IMAGE_INGAME_RED_IDLE, GameData.FIELD_DIMENSION, GameData.FIELD_DIMENSION);
		new LoadedImage("Ingame/Characters/Red_Walk_1.png", ImageType.IMAGE_INGAME_RED_WALK_1, GameData.FIELD_DIMENSION, GameData.FIELD_DIMENSION);
		new LoadedImage("Ingame/Characters/Red_Walk_2.png", ImageType.IMAGE_INGAME_RED_WALK_2, GameData.FIELD_DIMENSION, GameData.FIELD_DIMENSION);
		new LoadedImage("Ingame/Characters/Red_Walk_Left_1.png", ImageType.IMAGE_INGAME_RED_LEFT_1, GameData.FIELD_DIMENSION, GameData.FIELD_DIMENSION);
		new LoadedImage("Ingame/Characters/Red_Walk_Left_2.png", ImageType.IMAGE_INGAME_RED_LEFT_2, GameData.FIELD_DIMENSION, GameData.FIELD_DIMENSION);
		new LoadedImage("Ingame/Characters/Red_Walk_Left_Idle.png", ImageType.IMAGE_INGAME_RED_LEFT_IDLE, GameData.FIELD_DIMENSION, GameData.FIELD_DIMENSION);
		new LoadedImage("Ingame/Characters/Red_Walk_North_1.png", ImageType.IMAGE_INGAME_RED_NORTH_1, GameData.FIELD_DIMENSION, GameData.FIELD_DIMENSION);
		new LoadedImage("Ingame/Characters/Red_Walk_North_2.png", ImageType.IMAGE_INGAME_RED_NORTH_2, GameData.FIELD_DIMENSION, GameData.FIELD_DIMENSION);
		new LoadedImage("Ingame/Characters/Red_Walk_North_Idle.png", ImageType.IMAGE_INGAME_RED_NORTH_IDLE, GameData.FIELD_DIMENSION, GameData.FIELD_DIMENSION);
		new LoadedImage("Ingame/Characters/Red_Walk_Right_1.png", ImageType.IMAGE_INGAME_RED_RIGHT_1, GameData.FIELD_DIMENSION, GameData.FIELD_DIMENSION);
		new LoadedImage("Ingame/Characters/Red_Walk_Right_2.png", ImageType.IMAGE_INGAME_RED_RIGHT_2, GameData.FIELD_DIMENSION, GameData.FIELD_DIMENSION);
		new LoadedImage("Ingame/Characters/Red_Walk_Right_Idle.png", ImageType.IMAGE_INGAME_RED_RIGHT_IDLE, GameData.FIELD_DIMENSION, GameData.FIELD_DIMENSION);
		//Character 4
		new LoadedImage("Ingame/Characters/Demon_Dead.png", ImageType.IMAGE_INGAME_DEMON_DEAD, GameData.FIELD_DIMENSION, GameData.FIELD_DIMENSION);
		new LoadedImage("Ingame/Characters/Demon_Walk_Idle.png", ImageType.IMAGE_INGAME_DEMON_IDLE, GameData.FIELD_DIMENSION, GameData.FIELD_DIMENSION);
		new LoadedImage("Ingame/Characters/Demon_Walk_1.png", ImageType.IMAGE_INGAME_DEMON_WALK_1, GameData.FIELD_DIMENSION, GameData.FIELD_DIMENSION);
		new LoadedImage("Ingame/Characters/Demon_Walk_2.png", ImageType.IMAGE_INGAME_DEMON_WALK_2, GameData.FIELD_DIMENSION, GameData.FIELD_DIMENSION);
		new LoadedImage("Ingame/Characters/Demon_Walk_Left_1.png", ImageType.IMAGE_INGAME_DEMON_LEFT_1, GameData.FIELD_DIMENSION, GameData.FIELD_DIMENSION);
		new LoadedImage("Ingame/Characters/Demon_Walk_Left_2.png", ImageType.IMAGE_INGAME_DEMON_LEFT_2, GameData.FIELD_DIMENSION, GameData.FIELD_DIMENSION);
		new LoadedImage("Ingame/Characters/Demon_Walk_Left_Idle.png", ImageType.IMAGE_INGAME_DEMON_LEFT_IDLE, GameData.FIELD_DIMENSION, GameData.FIELD_DIMENSION);
		new LoadedImage("Ingame/Characters/Demon_Walk_North_1.png", ImageType.IMAGE_INGAME_DEMON_NORTH_1, GameData.FIELD_DIMENSION, GameData.FIELD_DIMENSION);
		new LoadedImage("Ingame/Characters/Demon_Walk_North_2.png", ImageType.IMAGE_INGAME_DEMON_NORTH_2, GameData.FIELD_DIMENSION, GameData.FIELD_DIMENSION);
		new LoadedImage("Ingame/Characters/Demon_Walk_North_Idle.png", ImageType.IMAGE_INGAME_DEMON_NORTH_IDLE, GameData.FIELD_DIMENSION, GameData.FIELD_DIMENSION);
		new LoadedImage("Ingame/Characters/Demon_Walk_Right_1.png", ImageType.IMAGE_INGAME_DEMON_RIGHT_1, GameData.FIELD_DIMENSION, GameData.FIELD_DIMENSION);
		new LoadedImage("Ingame/Characters/Demon_Walk_Right_2.png", ImageType.IMAGE_INGAME_DEMON_RIGHT_2, GameData.FIELD_DIMENSION, GameData.FIELD_DIMENSION);
		new LoadedImage("Ingame/Characters/Demon_Walk_Right_Idle.png", ImageType.IMAGE_INGAME_DEMON_RIGHT_IDLE, GameData.FIELD_DIMENSION, GameData.FIELD_DIMENSION);
		
		//AFTERGAME
		new LoadedImage("Ingame/new_skin_left.png", ImageType.IMAGE_AFTERGAME_1, 200, 200);
		new LoadedImage("Ingame/new_skin_left_wasted.png", ImageType.IMAGE_AFTERGAME_2, 200, 200);
		
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
