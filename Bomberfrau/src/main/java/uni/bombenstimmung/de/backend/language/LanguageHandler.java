/*
 * LanguageBlockHandler
 *
 * Version 1.0
 * Author: Benni
 *
 * Verwaltet alle SprachBlï¿½cke (laden, ausgeben, verï¿½ndern...)
 */
package uni.bombenstimmung.de.backend.language;

import java.util.ArrayList;
import java.util.List;

import uni.bombenstimmung.de.backend.console.ConsoleHandler;
import uni.bombenstimmung.de.backend.console.MessageType;
import uni.bombenstimmung.de.menu.Menu;

public class LanguageHandler {

	private static List<LoadedLanguageBlock> languageBlocks = new ArrayList<LoadedLanguageBlock>();
	private static LanguageType activeLanguage = LanguageType.ENGLISH;
	
	/**
	 * Wird am Start aufgerufen und initialisiert alle SprachBlöcke
	 * @see LoadedLanguageBlock
	 */
	public static void initLLBs() {

		String[] intro_skip = {"<<  Click to skip  >>", "<< Klicken zum Ueberspringen >>"};
//		String[] intro_skip = {"<<  Click to skip  >>", "<< Klicken zum Überspringen >>"};
//		String[] intro_skip = {"<<  Click to skip  >>", "<< Klicken zum \u00DCberspringen >>"};
		new LoadedLanguageBlock(LanguageBlockType.LB_INTRO_SKIP, intro_skip);

		String[] menu_txt1 = {"Create", "Erstellen"};
		new LoadedLanguageBlock(LanguageBlockType.LB_MENU_TXT1, menu_txt1);
		String[] menu_txt2 = {"Join", "Beitreten"};
		new LoadedLanguageBlock(LanguageBlockType.LB_MENU_TXT2, menu_txt2);
		String[] menu_info1 = {"min. " + Menu.MIN_NAME_LENGTH + " signs", "min. " + Menu.MIN_NAME_LENGTH + " Zeichen"};
		new LoadedLanguageBlock(LanguageBlockType.LB_MENU_INFO1, menu_info1);
		String[] menu_info2 = {"max. " + Menu.MAX_NAME_LENGTH + " signs", "max. " + Menu.MAX_NAME_LENGTH + " Zeichen"};
		new LoadedLanguageBlock(LanguageBlockType.LB_MENU_INFO2, menu_info2);
		String[] menu_info3 = {"invalid IP", "ungueltige IP"};
//		String[] menu_info3 = {"invalid IP", "ung\u00FCltige IP"};
		new LoadedLanguageBlock(LanguageBlockType.LB_MENU_INFO3, menu_info3);
		String[] menu_btn2 = {"OPTIONS", "OPTIONEN"};
		new LoadedLanguageBlock(LanguageBlockType.LB_MENU_BTN2, menu_btn2);
		String[] menu_btn3 = {"EXIT", "ENDE"};
		new LoadedLanguageBlock(LanguageBlockType.LB_MENU_BTN3, menu_btn3);
		
//		String[] options_txt1 = {"Resolution:", "Aufloesung:"};
		String[] options_txt1 = {"Resolution:", "Auflösung:"};
//		String[] options_txt1 = {"Resolution:", "Aufl\u00F6sung:"};
		new LoadedLanguageBlock(LanguageBlockType.LB_OPT_TXT1, options_txt1);
		String[] options_fullscreen = {"Fullscreen", "Vollbild"};
		new LoadedLanguageBlock(LanguageBlockType.LB_OPT_FULLSCREEN, options_fullscreen);
//		String[] options_txt2 = {"Volume Music:", "Lautstaerke Musik:"};
		String[] options_txt2 = {"Volume Music:", "Lautstärke Musik:"};
//		String[] options_txt2 = {"Volume Music:", "Lautst\u00E4rke Musik:"};
		new LoadedLanguageBlock(LanguageBlockType.LB_OPT_TXT2, options_txt2);
//		String[] options_txt3 = {"Volume Sound:", "Lautstaerke Sound:"};
		String[] options_txt3 = {"Volume Sound:", "Lautstärke Sound:"};
//		String[] options_txt3 = {"Volume Sound:", "Lautst\u00E4rke Sound:"};
		new LoadedLanguageBlock(LanguageBlockType.LB_OPT_TXT3, options_txt3);
		String[] options_txt4 = {"Controls:", "Steuerung:"};
		new LoadedLanguageBlock(LanguageBlockType.LB_OPT_TXT4, options_txt4);
		String[] options_txt5 = {"Bomb", "Bombe"};
		new LoadedLanguageBlock(LanguageBlockType.LB_OPT_TXT5, options_txt5);
		String[] options_txt6 = {"show FPS", "FPS anzeigen"};
		new LoadedLanguageBlock(LanguageBlockType.LB_OPT_TXT6, options_txt6);
		String[] options_txt7 = {"Language:", "Sprache:"};
		new LoadedLanguageBlock(LanguageBlockType.LB_OPT_TXT7, options_txt7);
//		String[] options_btn = {"BACK", "ZURUECK"};
		String[] options_btn = {"BACK", "ZURÜCK"};
//		String[] options_btn = {"BACK", "ZUR\u00DCCK"};
		new LoadedLanguageBlock(LanguageBlockType.LB_OPTIONS_BTN, options_btn);

		String[] key_up = {"Up", "Hoch"};
		new LoadedLanguageBlock(LanguageBlockType.LB_KEY_UP, key_up);
		String[] key_down = {"Down", "Runter"};
		new LoadedLanguageBlock(LanguageBlockType.LB_KEY_DOWN, key_down);
		String[] key_left = {"Left", "Links"};
		new LoadedLanguageBlock(LanguageBlockType.LB_KEY_LEFT, key_left);
		String[] key_right = {"Right", "Rechts"};
		new LoadedLanguageBlock(LanguageBlockType.LB_KEY_RIGHT, key_right);

		String[] bad_name = {"Please enter a valid name before starting ...", "Bitte vor dem Start einen gueltigen Namen eingeben ..."};
//		String[] bad_name = {"Please enter a valid name before starting ...", "Bitte vor dem Start einen gültigen Namen eingeben ..."};
//		String[] bad_name = {"Please enter a valid name before starting ...", "Bitte vor dem Start einen g\u00FCltigen Namen eingeben ..."};
		new LoadedLanguageBlock(LanguageBlockType.LB_MSG_BAD_NAME, bad_name);
		String[] bad_ip = {"Please enter an IP before starting ...", "Bitte vor dem Start eine IP eingeben ..."};
		new LoadedLanguageBlock(LanguageBlockType.LB_MSG_BAD_IP, bad_ip);
		String[] bad_res = {"resolution to high - switching to fullscreen.", "Die Aufloesung ist zu gross - Wechsel zu Vollbild."};
//		String[] bad_res = {"resolution to high - switching to fullscreen.", "Die Auflösung ist zu groß - Wechsel zu Vollbild."};
//		String[] bad_res = {"resolution to high - switching to fullscreen.", "Die Aufl\u00F6sung ist zu gro\u00DF - Wechsel zu Vollbild."};
		new LoadedLanguageBlock(LanguageBlockType.LB_MSG_BAD_RESOLUTION, bad_res);
		
		String[] ready = {"READY?", "BEREIT?"};
		new LoadedLanguageBlock(LanguageBlockType.LB_LOBBY_READY, ready);
		
		String[] aftergame_Titel = {"A F T E R G A M E", "A F T E R G A M E"};
		new LoadedLanguageBlock(LanguageBlockType.LB_AFTERGAME_TITEL, aftergame_Titel);
		String[] aftergame_Ranking = {"",""};
		new LoadedLanguageBlock(LanguageBlockType.LB_AFTERGAME_SCORE_1, aftergame_Ranking);
		new LoadedLanguageBlock(LanguageBlockType.LB_AFTERGAME_SCORE_2, aftergame_Ranking);
		new LoadedLanguageBlock(LanguageBlockType.LB_AFTERGAME_SCORE_3, aftergame_Ranking);
		new LoadedLanguageBlock(LanguageBlockType.LB_AFTERGAME_SCORE_4, aftergame_Ranking);
		new LoadedLanguageBlock(LanguageBlockType.LB_AFTERGAME_NAME_1, aftergame_Ranking);
		new LoadedLanguageBlock(LanguageBlockType.LB_AFTERGAME_NAME_2, aftergame_Ranking);
		new LoadedLanguageBlock(LanguageBlockType.LB_AFTERGAME_NAME_3, aftergame_Ranking);
		new LoadedLanguageBlock(LanguageBlockType.LB_AFTERGAME_NAME_4, aftergame_Ranking);
		
		String[] aftergame_bt1 = {"BACK TO MENU", "ZURUECK ZUM MENUE"};
//		String[] aftergame_bt1 = {"BACK TO MENU", "ZURÜCK ZUM MENÜ"};
//		String[] aftergame_bt1 = {"BACK TO MENU", "ZUR\u00DCCK ZUM MEN\u00DC"};
		new LoadedLanguageBlock(LanguageBlockType.LB_AFTERGAME_BT1, aftergame_bt1);
		String[] aftergame_bt2 = {"REPEAT", "WIEDERHOLEN"};
		new LoadedLanguageBlock(LanguageBlockType.LB_AFTERGAME_BT2, aftergame_bt2);
		String[] aftergame_bt3 = {"EXIT", "ENDE"};
		new LoadedLanguageBlock(LanguageBlockType.LB_AFTERGAME_BT3, aftergame_bt3);
		
		ConsoleHandler.print("Initialised LanguageBlocks ("+languageBlocks.size()+")", MessageType.BACKEND);	
	}
	
	/**
	 * Fügt den LLB zur languageBlocks Liste hinzu (Wird eigentlich nur aus einem LLB aufgerufen)
	 * @param llb - Der {@link LoadedLanguageBlock} der hinzugefügt werden soll
	 */
	public static void addLLB(LoadedLanguageBlock llb) {
		
		languageBlocks.add(llb);
		
	}
	
	/**
	 * Gibt den LLB zum angegebenen Type zurück (Wenn richtig geladen gibt es für jeden Type einen LLB)
	 * @param type - Der {@link LanguageBlockType} zu dem der gesuchte LLB gehört
	 * @return Den {@link LoadedLanguageBlock} der durch den Type identifiziert wird, falls keiner gefunden wird null
	 */
	public static LoadedLanguageBlock getLLB(LanguageBlockType type) {
		
		for(LoadedLanguageBlock llb : languageBlocks) {
			if(llb.getType() == type) {
				return llb;
			}
		}
		
		return null;
	}
	
	public static void setActiveLanguage(LanguageType activeLanguage) {
		LanguageHandler.activeLanguage = activeLanguage;
	}
	public static LanguageType getActiveLanguage() {
		return activeLanguage;
	}
}
