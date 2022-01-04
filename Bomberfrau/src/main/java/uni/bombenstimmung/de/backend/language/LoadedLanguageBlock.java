/*
 * LoadedLanguageBlock
 *
 * Version 1.0
 * Author: Benni
 *
 * Enth�lt jeweils alle Daten zu einem geladenene SprachBlock
 */
package uni.bombenstimmung.de.backend.language;

public class LoadedLanguageBlock {

	private LanguageBlockType type;
	private String[] languageContent = new String[LanguageType.values().length];
	
	/**
	 * Repr�sentiert einen SprachBlock, wichtig bei der Initialisierung von Content ist die Reihenfole, siehe daf�r {@link LanguageType}!
	 * @param type - Der Type der diesen LBB identifiziert
	 * @param content - Die eigentlichen Sprachdaten f�r die jeweilige Sprache [0=ENGLISH, 1=GERMAN, ...]
	 * @see LanguageBlockType, {@link LanguageType}
	 */
	public LoadedLanguageBlock(LanguageBlockType type, String[] content) {
		
		this.type = type;
		this.languageContent = content;
		
		LanguageHandler.addLLB(this);
		
	}
	
	public String getContent() {
		return languageContent[LanguageHandler.getActiveLanguage().getPos()];
	}
	public LanguageBlockType getType() {
		return type;
	}
	
}
