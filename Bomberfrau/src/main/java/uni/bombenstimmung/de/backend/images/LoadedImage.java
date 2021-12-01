/*
 * LoadedImage
 *
 * Version 1.0
 * Author: Benni
 *
 * Enth�lt jeweils alle Daten zu einem geladenene Bild
 */
package uni.bombenstimmung.de.backend.images;

import java.io.IOException;

import java.awt.Image;

import javax.imageio.ImageIO;

import uni.bombenstimmung.de.backend.console.ConsoleHandler;
import uni.bombenstimmung.de.backend.console.MessageType;
import uni.bombenstimmung.de.main.BomberfrauMain;

public class LoadedImage {

	private Image image = null;
	private ImageType type;
	
	/**
	 * Repr�sentiert ein Image
	 * @param name - Der Name des Images (zb: foler/name.png wobei images als ordner immer automatisch davor steht)
	 * @param type - Der Type dem das Image zugeordnet wird (und �ber das es identifziert wird)
	 */
	public LoadedImage(String name, ImageType type) {
		
		this.type = type;
		boolean success = loadImage(name);
		
		if(success == true) {
			ImageHandler.addImage(this);
		}
		
	}
	/**
	 * Repr�sentiert ein Image (Wird automatisch auf die angegebene Gr��en zugeschnitten)
	 * @param name - Der Name des Images (zb: foler/name.png wobei images als ordner immer automatisch davor steht)
	 * @param type - Der Type dem das Image zugeordnet wird (und �ber das es identifziert wird)
	 * @param width - Die Ziel-Breite des Images nach dem laden und zuschneiden
	 * @param height - Die Ziel-H�he des Images nach dem laden und zuschneiden
	 */
	public LoadedImage(String name, ImageType type, int width, int height) {
		
		this.type = type;
		boolean success = loadImage(name, width, height);
		
		if(success == true) {
			ImageHandler.addImage(this);
		}
		
	}
	
	/**
	 * L�d das Image mit dem PATH
	 * @param name - Der Name der Ziel-Image-Datei
	 * @return true wenn das laden erfolgreich war, sonst false
	 */
	private boolean loadImage(String name) {
		try {
			this.image = ImageIO.read(BomberfrauMain.class.getClassLoader().getResourceAsStream(ImageHandler.PATH+"Bomberman_Icon.png"));
			return true;
		} catch (IOException e) {
			ConsoleHandler.print("Can't load/find image '"+name+"' [NO SCALE]!", MessageType.ERROR);
			return false;
		}
	}
	/**
	 * L�d das Image mit dem PATH und skalliert es auf die angegebenen Ma�e
	 * @param name - Der Name der Ziel-Image-Datei
	 * @param width - Die Ziel-Breite des Images nach dem laden und zuschneiden
	 * @param height - Die Ziel-H�he des Images nach dem laden und zuschneiden
	 * @return true wenn das laden erfolgreich war, sonst false
	 */
	private boolean loadImage(String name, int width, int height) {
		try {
			this.image = ImageIO.read(BomberfrauMain.class.getClassLoader().getResourceAsStream(ImageHandler.PATH+"Bomberman_Icon.png")).getScaledInstance(width, height, Image.SCALE_SMOOTH);
			return true;
		} catch (IOException e) {
			ConsoleHandler.print("Can't load/find image '"+name+"' [SCALE]!", MessageType.ERROR);
			return false;
		}
	}
	
	public Image getImage() {
		return image;
	}
	public ImageType getType() {
		return type;
	}
	
}
