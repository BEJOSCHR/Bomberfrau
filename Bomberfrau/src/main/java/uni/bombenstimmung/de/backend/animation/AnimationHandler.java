/*
 * AnimationHandler
 *
 * Version 1.0
 * Author: Benni
 *
 * Verwaltet alle Animations (vorallem die Ticks) und startet diese
 */
package uni.bombenstimmung.de.backend.animation;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import uni.bombenstimmung.de.backend.console.ConsoleHandler;
import uni.bombenstimmung.de.backend.console.MessageType;

public class AnimationHandler {

	private static List<Animation> runningAnimations = new ArrayList<Animation>();
	private static Timer tickTimer = null;
	
	/**
	 * Startet den TickTimer, wenn er nicht schon läuft
	 */
	public static void startTickTimer() {
		
		if(tickTimer == null) {
			tickTimer = new Timer();
			tickTimer.scheduleAtFixedRate(new TimerTask() {
				@Override
				public void run() {
					
					for(Animation animation : runningAnimations) {
						animation.tick();
					}
					
				}
			}, 0, 10);
			ConsoleHandler.print("Started TickTimer!", MessageType.BACKEND);
		}else {
			ConsoleHandler.print("Couldn't init/start TickTimer, because there is already one running!", MessageType.IMPORTANT);
		}
		
	}
	
	/**
	 * Stoppt den TickTimer, wenn er läuft
	 */
	public static void stopTickTimer() {
		
		if(tickTimer != null) {
			tickTimer.cancel();
			tickTimer = null;
			ConsoleHandler.print("Stopped TickTimer!", MessageType.BACKEND);
		}else {
			ConsoleHandler.print("Couldn't stop TickTimer, because there is no one running!", MessageType.IMPORTANT);
		}
		
	}
	
	/**
	 * Fügt die übergebenen Animation zur runningAnimation Liste hinzu (Wird eigentlich nur aus der Animation aufgerufen)
	 * @param animation - Die Animation die hinzugefügt werden soll
	 */
	public static void addAnimation(Animation animation) {
		
		//TO PREVENT CONCURRENT BLOCKING
		while(!runningAnimations.contains(animation)) {
			try {
				runningAnimations.add(animation);
			}catch(ConcurrentModificationException error) {}

		}
		
	}
	
	/**
	 * Entfernt die {@link Animation} aus der liste
	 * @param animation - Die Animation die entfernt werden soll
	 */
	public static void removeAnimation(Animation animation) {
		
		//TO PREVENT CONCURRENT BLOCKING
		while(runningAnimations.contains(animation)) {
			try {
				runningAnimations.remove(animation);
			}catch(ConcurrentModificationException error) {}

		}
		
	}
	
	/**
	 * Stoppt alle derzeit laufenden Animationen (Nützlich bei Szenene wechseln bei denen dauerhafte Animationen enden sollen)
	 */
	public static void stopAllAnimations() {
		
		try {
			for(Animation animation : runningAnimations) {
				animation.finished(false);
			}
		}catch(ConcurrentModificationException error) {}
		runningAnimations.clear();
		
	}
	
}
