/*
 * LanguageBlockHandler
 *
 * Version 1.0
 * Author: Benni
 *
 * Verwaltet alle SprachBlöcke (laden, ausgeben, verändern...)
 */
package uni.bombenstimmung.de.backend.language;

import java.util.ArrayList;
import java.util.List;

import uni.bombenstimmung.de.backend.console.ConsoleHandler;
import uni.bombenstimmung.de.backend.console.MessageType;
import uni.bombenstimmung.de.main.BomberfrauMain;

public class LanguageHandler {

	private static List<LoadedLanguageBlock> languageBlocks = new ArrayList<LoadedLanguageBlock>();
	private static LanguageType activeLanguage = LanguageType.ENGLISH;
	
	/**
	 * Wird am Start aufgerufen und initialisiert alle SprachBlöcke
	 * @see LoadedLanguageBlock
	 */
	public static void initLLBs() {
		
		String[] loadingScreen_Title = {"B O M B E R F R A U", "B O M B E R F R A U"};
		new LoadedLanguageBlock(LanguageBlockType.LB_LOADINGSCREEN_TITLE, loadingScreen_Title);
		String[] loadingScreen_Continue = {"<< Click to continue >>", "<< Klicke zum starten >>"};
		new LoadedLanguageBlock(LanguageBlockType.LB_LOADINGSCREEN_CONTINUE, loadingScreen_Continue);
		String[] loadingScreen_Author = {BomberfrauMain.AUTHOR, BomberfrauMain.AUTHOR};
		new LoadedLanguageBlock(LanguageBlockType.LB_LOADINGSCREEN_AUTHOR, loadingScreen_Author);
		String[] loadingScreen_Version = {BomberfrauMain.VERSION, BomberfrauMain.VERSION};
		new LoadedLanguageBlock(LanguageBlockType.LB_LOADINGSCREEN_VERSION, loadingScreen_Version);
		
		String[] intro_Skip = {"<<  Click to skip  >>", "<< Klicken zum Überspringen >>"};
		new LoadedLanguageBlock(LanguageBlockType.LB_INTRO_SKIP, intro_Skip);
		
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
	 * Gibt den LLB zum angegebenen Type zurück (Wenn richtig geladen gibt es für jeden Type ienen LLB)
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
