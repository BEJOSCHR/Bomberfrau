/*
 * SoundHandler
 *
 * Version 1.0
 * Author: Benni
 *
 * Verwaltet alle Sounds (laden, ausgeben, ver�ndern...)
 */
package uni.bombenstimmung.de.backend.sounds;

import java.util.ArrayList;
import java.util.List;

import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

import uni.bombenstimmung.de.backend.console.ConsoleHandler;
import uni.bombenstimmung.de.backend.console.MessageType;

public class SoundHandler {

	public static final String PATH = "sounds/";
	private static List<LoadedSound> sounds = new ArrayList<LoadedSound>();
	public static Clip lastPlayedClip;
	
	/**
	 * Wird am Start aufgerufen und l�d alle Sounds
	 */
	public static void initSounds() {
		
		//TODO ADD SOUND TO LOAD HERE
		//EXAMPLE: new LoadedSound("test123.wav", SoundType.SOUND_MENU_XXX, SoundCategory.SOUND_EFFECT, 0.02D);
		
		new LoadedSound("logo_opener.wav", SoundType.INTRO, SoundCategory.MENU_MUSIC, 0.15D);
		new LoadedSound("menu.wav", SoundType.MENU, SoundCategory.MENU_MUSIC, 0.15D);
		new LoadedSound("sound.wav", SoundType.OPTIONS, SoundCategory.MENU_SOUND, 0.15D);
		
		ConsoleHandler.print("Loaded sounds ("+sounds.size()+")", MessageType.BACKEND);
		
	}
	
	/**
	 * Spielt den Sound ab, der zum �bergebenene Type geh�rt
	 * @param type - Der {@link SoundType} der den Sound identifziert
	 */
	public static void playSound(SoundType type, Boolean loop) {
		
		LoadedSound sound = getSound(type);
		Clip clip = sound.getClip();
		FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
		float value = 20f * (float) Math.log10(sound.getVolume());
		if(value < -70) { value = -70; } //MINIMUM VOLUME
		else if(value > 6) { value = 6; } //MAXIMUM VOLUME
		gainControl.setValue(value);
		clip.setFramePosition(0);
		if (loop) clip.loop(Clip.LOOP_CONTINUOUSLY);
		clip.start();
		lastPlayedClip = clip;	
	}
	
	/**
	 * Spielt den Sound ab, der zum �bergebenene Type geh�rt
	 * @param type - Der {@link SoundType} der den Sound identifziert
	 * @param value - Lautst�rke [-70F bis 6F]
	 */
	public static void playSound(SoundType type, Boolean loop, float value) {
		
		LoadedSound sound = getSound(type);
		Clip clip = sound.getClip();
		FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
		if(value < -70) { value = -70; } //MINIMUM VOLUME
		else if(value > 6) { value = 6; } //MAXIMUM VOLUME
		gainControl.setValue(value);
		clip.setFramePosition(0);
		if (loop) clip.loop(Clip.LOOP_CONTINUOUSLY);
		clip.start();
		lastPlayedClip = clip;	
	}
	
	/**
	 * Stoppt alle Sounds die gerade laufen
	 */
	public static void stopAllSounds() {
	        for(LoadedSound sounds : sounds) {
	        	sounds.getClip().stop();
	        }
	}
	
	/**
	 * Reduziert die Lautst�rke des gerade laufenden Clips kontinuierlich bis zur Stille
	 */
	public static void reduceLastPlayedSound() {
	    	FloatControl volume = (FloatControl) lastPlayedClip.getControl(FloatControl.Type.MASTER_GAIN);
                try {
                    float vol = volume.getValue();
                    while (vol>-60) {
                        vol-=1.5f; 
                        volume.setValue(vol);
                        Thread.sleep(100);
                    }
                    Thread.sleep(500);
                    lastPlayedClip.stop();
                }
                catch (InterruptedException ex) {}
	}
                
	/**
	 * Passt den Sound bei allen {@link LoadedSound} an, die zu dieser Category geh�ren
	 * @param category- Die Category die ver�ndert werden soll
	 * @param volumeModifier - Der modifier [0.02D hei�t also das das volume um diesen wert erh�ht wird, -0.02D das er geringer wird]
	 * @see SoundCategory, {@link LoadedSound}
	 */
	public static void changeCategoryVolume(SoundCategory category, double volumeModifier) {
		
		for(LoadedSound sound : getSoundCategory(category)) {
			sound.setCategoryVolume(sound.getCategoryVolume()+volumeModifier);
		}
		
	}

	/**
	 * Passt den Sound bei allen {@link LoadedSound} an, die zu dieser Category geh�ren
	 * @param category- Die Category die ver�ndert werden soll
	 * @param volume - Die gew�nschte Lautst�rke
	 * @see SoundCategory, {@link LoadedSound}
	 */
	public static void setCategoryVolume(SoundCategory category, double volume) {
		
		for(LoadedSound sound : getSoundCategory(category)) {
			sound.setCategoryVolume(volume);
		}
		
	}
	
	
	/**
	 * Added den angegebenen Sound zur sounds Liste (Wird eigentlich nur aus den LoadedSounds aufgerufen)
	 * @param sound - Der zu addende Sound
	 * @see LoadedSounds
	 */
	public static void addSound(LoadedSound sound) {
		
		sounds.add(sound);
		
	}
	
	/**
	 * Gibt den Sound zum zugeh�rigen Type zur�ck (Wenn richtig geladen gibt es f�r jeden Type einen Sound)
	 * @param type - Der Type der gesucht wird
	 * @see SoundType
	 * @return Der {@link LoadedSound} der zum Type geh�rt, wenn keiner gefunden wird dann null
	 */
	public static LoadedSound getSound(SoundType type) {
		
		for(LoadedSound sound : sounds) {
			if(sound.getType() == type) {
				return sound;
			}
		}
		
		return null;
	}
	
	/**
	 * Gibt die Sounds zur zugeh�rigen Category zur�ck (Wenn richtig geladen gibt es f�r jede Category einen Sound)
	 * @param category - Die Category die gesucht wird
	 * @see SoundCategory
	 * @return Liste von {@link LoadedSound} die zur Category geh�ren, wenn keiner gefunden wird dann eine Leere Liste
	 */
	public static List<LoadedSound> getSoundCategory(SoundCategory category) {
		
		List<LoadedSound> output = new ArrayList<LoadedSound>();
		for(LoadedSound sound : sounds) {
			if(sound.getCategory() == category) {
				output.add(sound);
			}
		}
		
		return output;
	}
	
}
