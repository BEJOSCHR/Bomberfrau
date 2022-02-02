/*
 * LanguageType
 *
 * Version 1.0
 * Author: Benni
 *
 * Dient zur Identifizierung der derzeitig ausgewählten Sprache (in den Menu-Einstellungen)
 */
package uni.bombenstimmung.de.backend.language;

public enum LanguageType {

    ENGLISH(0), GERMAN(1);

    private final int pos;

    LanguageType(int pos) {

	this.pos = pos;

    }

    public int getPos() {
	return pos;
    }

}
