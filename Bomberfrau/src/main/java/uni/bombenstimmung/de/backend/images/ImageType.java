/*
 * ImageType
 *
 * Version 1.0
 * Author: Benni
 *
 * Dient zur Identifizierung jedes LoadedImage Objektes �ber den ImageHandler
 */
package uni.bombenstimmung.de.backend.images;

public enum ImageType {

    // INTRO / MENU
    IMAGE_INTRO_PIC, IMAGE_MENU_PIC, IMAGE_MENU_TITLE, IMAGE_OPTIONS_PIC,

    // INGAME
    IMAGE_INGAME_GRAS, IMAGE_INGAME_WALL, IMAGE_INGAME_BLOCK, IMAGE_INGAME_BORDER,

    // LOBBY
    IMAGE_LOBBY_ARROW_RIGHT, IMAGE_LOBBY_ARROW_RIGHT_BIGGER, IMAGE_LOBBY_ARROW_LEFT_BIGGER, IMAGE_LOBBY_ARROW_LEFT,
    IMAGE_LOBBY_MAP_1, IMAGE_LOBBY_MAP_2, IMAGE_LOBBY_MAP_3, IMAGE_LOBBY_SKIN_0, IMAGE_LOBBY_SKIN_0_0,
    IMAGE_LOBBY_SKIN_0_1, IMAGE_LOBBY_SKIN_1, IMAGE_LOBBY_SKIN_1_0, IMAGE_LOBBY_SKIN_1_1, IMAGE_LOBBY_SKIN_2,
    IMAGE_LOBBY_SKIN_2_0, IMAGE_LOBBY_SKIN_2_1, IMAGE_LOBBY_SKIN_3, IMAGE_LOBBY_SKIN_3_0, IMAGE_LOBBY_SKIN_3_1,
    IMAGE_LOBBY_CROWN, IMAGE_LOBBY_CHECKMARK, IMAGE_LOBBY_CROSS,

    // INGAME
    IMAGE_INGAME_YELLOWGRAS, IMAGE_INGAME_BOMB, IMAGE_INGAME_LIGHTTILE, IMAGE_INGAME_LAVA_BLOCK,
    IMAGE_INGAME_LAVA_FLOOR, IMAGE_INGAME_LAVA_WALL, INGAME_SKIN_01, INGAME_SKIN_01_WASTED, IMAGE_INGAME_WASTED,
    IMAGE_INGAME_BOMB_EX1, IMAGE_INGAME_BOMB_EX2, IMAGE_INGAME_BOMB_EX3, IMAGE_INGAME_UPGRADE_TEMP,
    IMAGE_INGAME_UPGRADE_ITEM_BOMB, IMAGE_INGAME_UPGRADE_ITEM_SHOE, IMAGE_INGAME_UPGRADE_ITEM_FIRE,
    IMAGE_INGAME_BOMB_EX2_NS, IMAGE_INGAME_BOMB_EX3_O, IMAGE_INGAME_BOMB_EX3_W, IMAGE_INGAME_BOMB_EX3_S,
    IMAGE_INGAME_BOMB_EX3_N, IMAGE_INGAME_CHARACTER_IDLE, IMAGE_INGAME_CHARACTER_WALK_1, IMAGE_INGAME_CHARACTER_WALK_2,
    IMAGE_INGAME_CHARACTER_WALK_NORTH_1, IMAGE_INGAME_CHARACTER_WALK_NORTH_IDLE,
    IMAGE_INGAME_CHARACTER_WALK_SIDE_LEFT_1, IMAGE_INGAME_CHARACTER_WALK_SIDE_LEFT_IDLE,
    IMAGE_INGAME_CHARACTER_WALK_SIDE_RIGHT_1, IMAGE_INGAME_CHARACTER_WALK_SIDE_RIGHT_IDLE, IMAGE_INGAME_CHARACTER_DEAD,
    IMAGE_INGAME_SKELETON_IDLE, IMAGE_INGAME_SKELETON_WALK_1, IMAGE_INGAME_SKELETON_WALK_2,
    IMAGE_INGAME_SKELETON_LEFT_1, IMAGE_INGAME_SKELETON_LEFT_2, IMAGE_INGAME_SKELETON_LEFT_IDLE,
    IMAGE_INGAME_SKELETON_NORTH_1, IMAGE_INGAME_SKELETON_NORTH_2, IMAGE_INGAME_SKELETON_NORTH_IDLE,
    IMAGE_INGAME_SKELETON_RIGHT_1, IMAGE_INGAME_SKELETON_RIGHT_2, IMAGE_INGAME_SKELETON_RIGHT_IDLE,
    IMAGE_INGAME_SKELETON_DEAD, IMAGE_INGAME_RED_IDLE, IMAGE_INGAME_RED_WALK_1, IMAGE_INGAME_RED_WALK_2,
    IMAGE_INGAME_RED_LEFT_1, IMAGE_INGAME_RED_LEFT_2, IMAGE_INGAME_RED_LEFT_IDLE, IMAGE_INGAME_RED_NORTH_1,
    IMAGE_INGAME_RED_NORTH_2, IMAGE_INGAME_RED_NORTH_IDLE, IMAGE_INGAME_RED_RIGHT_1, IMAGE_INGAME_RED_RIGHT_2,
    IMAGE_INGAME_RED_RIGHT_IDLE, IMAGE_INGAME_RED_DEAD, IMAGE_INGAME_DEMON_IDLE, IMAGE_INGAME_DEMON_WALK_1,
    IMAGE_INGAME_DEMON_WALK_2, IMAGE_INGAME_DEMON_LEFT_1, IMAGE_INGAME_DEMON_LEFT_2, IMAGE_INGAME_DEMON_LEFT_IDLE,
    IMAGE_INGAME_DEMON_NORTH_1, IMAGE_INGAME_DEMON_NORTH_2, IMAGE_INGAME_DEMON_NORTH_IDLE, IMAGE_INGAME_DEMON_RIGHT_1,
    IMAGE_INGAME_DEMON_RIGHT_2, IMAGE_INGAME_DEMON_RIGHT_IDLE, IMAGE_INGAME_DEMON_DEAD,

    // LOBBY UND INGAME
    IMAGE_INGAME_ICON, IMAGE_INGAME_ICON_BLUE, IMAGE_INGAME_ICON_GELB, IMAGE_INGAME_ICON_PINK,

    // AFTERGAME
    IMAGE_AFTERGAME_1, IMAGE_AFTERGAME_2, IMAGE_AFTERGAME_3, IMAGE_AFTERGAME_4;

}
