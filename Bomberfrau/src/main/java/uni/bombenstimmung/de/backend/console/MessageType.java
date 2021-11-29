/*
 * MessageType
 *
 * Version 1.0
 * Author: Benni
 *
 * Dient zur Einordnung der zu sendenen Message im ConoleHandler
 */
package uni.bombenstimmung.de.backend.console;

public enum MessageType {

	NONESPECIFIC,
	IMPORTANT,
	ERROR,
	
	MENU,
	LOBBY,
	GAME,
	AFTERGAME,
	BACKEND;
	
}
