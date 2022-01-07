/*
 * SoundHandler
 *
 * Version 1.0
 * Author: Benni
 *
 * Verwaltet alle Sounds (laden, ausgeben, verändern...)
 */
package uni.bombenstimmung.de.backend.sounds;

import java.util.ArrayList;
import java.util.List;

import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

import uni.bombenstimmung.de.backend.console.ConsoleHandler;
import uni.bombenstimmung.de.backend.console.MessageType;
import uni.bombenstimmung.de.menu.Menu;
import uni.bombenstimmung.de.menu.Settings;

public class SoundHandler {

	public static final String PATH = "sounds/";
	private static List<LoadedSound> sounds = new ArrayList<LoadedSound>();
	public static Clip lastPlayedClip;
	
	/**
	 * Wird am Start aufgerufen und läd alle Sounds
	 */
	public static void initSounds() {
		
		//TODO ADD SOUND TO LOAD HERE
		//EXAMPLE: new LoadedSound("test123.wav", SoundType.SOUND_MENU_XXX, SoundCategory.SOUND_EFFECT, 0.02D);
		
		new LoadedSound("menu/logo_opener.wav", SoundType.INTRO, SoundCategory.MENU_MUSIC, 0.2D);
		new LoadedSound("menu/menu.wav", SoundType.MENU, SoundCategory.MENU_MUSIC, 0.2D);
		new LoadedSound("menu/sound.wav", SoundType.OPTIONS, SoundCategory.MENU_SOUND, 0.2D);

		new LoadedSound("ingame/fuse.wav", SoundType.FUSE, SoundCategory.INGAME_SOUNDS, 0.2D);
		new LoadedSound("ingame/explosion.wav", SoundType.EXPLOSION, SoundCategory.INGAME_SOUNDS, 0.2D);
		new LoadedSound("ingame/item.wav", SoundType.ITEM, SoundCategory.INGAME_SOUNDS, 0.2D);
		new LoadedSound("ingame/wall.wav", SoundType.WALL, SoundCategory.INGAME_SOUNDS, 0.2D);
		new LoadedSound("ingame/dying.wav", SoundType.DYING, SoundCategory.INGAME_SOUNDS, 0.2D);
		
		ConsoleHandler.print("Loaded sounds ("+sounds.size()+")", MessageType.BACKEND);
		
	}
	
	/**
	 * Spielt den Sound ab, der zum übergebenene Type gehört
	 * @param type - Der {@link SoundType} der den Sound identifziert
	 * @param loop - Clip in Schleife wiederholen oder nicht
	 */
	public static void playSound(SoundType type, boolean loop) {
		
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
	 * Spielt den Sound ab, der zum übergebenene Type gehört
	 * @param type - Der {@link SoundType} der den Sound identifziert
	 * @param loop - Clip in Schleife wiederholen oder nicht
	 */
	public static void playSound2(SoundType type, boolean loop) {
		float vol = -80F;
		
		ConsoleHandler.print("playing sound '" + type + "'", MessageType.BACKEND);
		LoadedSound sound = getSound(type);
		Clip clip = sound.getClip();
		FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
		if (sound.getCategory() == SoundCategory.MENU_MUSIC)
			vol = Menu.VolumeIntToFloat(Settings.getIni_VolMusic());
		if (sound.getCategory() == SoundCategory.MENU_SOUND)
			vol = Menu.VolumeIntToFloat(Settings.getIni_VolSound());
		if (sound.getCategory() == SoundCategory.INGAME_MUSIC)
			vol = Menu.VolumeIntToFloat(Settings.getIni_VolMusic());
		if (sound.getCategory() == SoundCategory.INGAME_SOUNDS)
			vol = Menu.VolumeIntToFloat(Settings.getIni_VolSound());
		gainControl.setValue(vol);
		clip.setFramePosition(0);
		if (loop) clip.loop(Clip.LOOP_CONTINUOUSLY);
		clip.start();
		lastPlayedClip = clip;	
	}
	
	/**
	 * Spielt den Sound ab, der zum übergebenene Type gehört
	 * @param type - Der {@link SoundType} der den Sound identifziert
	 * @param loop - Clip in Schleife wiederholen oder nicht
	 * @param value - Lautstärke [-70F bis 6F]
	 */
	public static void playSound(SoundType type, boolean loop, float value) {
		
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
	 * Stopt den Sound, der zum übergebenene Type gehört
	 * @param type - Der {@link SoundType} der den Sound identifziert
	 */
	public static void stopSound(SoundType type) {
	    LoadedSound sound = getSound(type);
	    Clip clip = sound.getClip();
	    clip.stop();
	    ConsoleHandler.print("stoppe Sound " + type, MessageType.BACKEND);
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
	 * Reduziert die Lautstärke des gerade laufenden Clips kontinuierlich bis zur Stille
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
	 * Passt den Sound bei allen {@link LoadedSound} an, die zu dieser Category gehören
	 * @param category- Die Category die verändert werden soll
	 * @param volumeModifier - Der modifier [0.02D heißt also das das volume um diesen wert erhöht wird, -0.02D das er geringer wird]
	 * @see SoundCategory, {@link LoadedSound}
	 */
	public static void changeCategoryVolume(SoundCategory category, double volumeModifier) {
		
		for(LoadedSound sound : getSoundCategory(category)) {
			sound.setCategoryVolume(sound.getCategoryVolume()+volumeModifier);
		}
		
	}

	/**
	 * Passt den Sound bei allen {@link LoadedSound} an, die zu dieser Category gehören
	 * @param category- Die Category die verändert werden soll
	 * @param volume - Die gewünschte Lautstärke
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
	 * Gibt den Sound zum zugehörigen Type zurück (Wenn richtig geladen gibt es für jeden Type einen Sound)
	 * @param type - Der Type der gesucht wird
	 * @see SoundType
	 * @return Der {@link LoadedSound} der zum Type gehört, wenn keiner gefunden wird dann null
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
	 * Gibt die Sounds zur zugehörigen Category zurück (Wenn richtig geladen gibt es für jede Category einen Sound)
	 * @param category - Die Category die gesucht wird
	 * @see SoundCategory
	 * @return Liste von {@link LoadedSound} die zur Category gehören, wenn keiner gefunden wird dann eine Leere Liste
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
