/*
 * LoadedSound
 *
 * Version 1.0
 * Author: Benni
 *
 * Enthält jeweils alle Daten zu einem geladenene Sound
 */
package uni.bombenstimmung.de.backend.sounds;

import java.io.IOException;
import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import uni.bombenstimmung.de.backend.console.ConsoleHandler;
import uni.bombenstimmung.de.backend.console.MessageType;
import uni.bombenstimmung.de.main.BomberfrauMain;

public class LoadedSound {

	private Clip clip = null;
	private SoundType type;
	private SoundCategory category;
	private double volume = 0.02D;
	private double categoryVolume = 0.00D;
	
	/**
	 * Repräsentiert eine Sound
	 * @param name - Der Name des Sounds (zb: foler/name.wav wobei sounds als ordner immer automatisch davor steht)
	 * @param type - Der Type dem der Sound zugeordnet wird (und über den er identifziert wird)
	 * @param category - Die {@link SoundCategory} die der Sound zugeordnet wird
	 * @param volume - Die Lautstärke des Sounds [0.02 bis 0.1 sind ganz gute Lautstärken]
	 */
	public LoadedSound(String name, SoundType type, SoundCategory category, double volume) {
		
		this.type = type;
		this.category = category;
		this.volume = volume;
		boolean success = loadSound(name);
		
		if(success == true) {
			SoundHandler.addSound(this);
		}
		
	}
	/**
	 * Läd den Sound mit dem PATH
	 * @param name - Der Name der Ziel-Sound-Datei
	 * @return true wenn das laden erfolgreich war, sonst false
	 */
	private boolean loadSound(String name) {
		try {
			URL SoundURL = BomberfrauMain.class.getClassLoader().getResource(SoundHandler.PATH+name);
			AudioInputStream InStream = AudioSystem.getAudioInputStream(SoundURL);
			Clip clip = AudioSystem.getClip();
			clip.setFramePosition(0);
			clip.open(InStream);
			this.clip = clip;
			return true;
		}catch (IOException e) {
			ConsoleHandler.print("Can't load/find sound '"+name+"'!", MessageType.ERROR);
			return false;
		}catch (UnsupportedAudioFileException | LineUnavailableException e) {
			ConsoleHandler.print("Error while loading/finding sound '"+name+"':", MessageType.ERROR);
			e.printStackTrace();
			return false;
		}
	}
	
	public Clip getClip() {
		return clip;
	}
	public SoundType getType() {
		return type;
	}
	public SoundCategory getCategory() {
		return category;
	}
	public double getVolume() {
		return volume+categoryVolume;
	}
	public double getCategoryVolume() {
		return categoryVolume;
	}
	public void setCategoryVolume(double categoryVolume) {
		this.categoryVolume = categoryVolume;
	}
	
}
