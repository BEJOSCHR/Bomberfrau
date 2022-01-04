/*
 * Animation
 *
 * Version 1.0
 * Author: Benni
 *
 * Alle zeitlich gesteuerten Bewegungen sind Animation und als solch ein Objekt anzulegen
 */
package uni.bombenstimmung.de.backend.animation;

public class Animation {

	private boolean finished = false;
	
	private int delay, totalSteps;
	private int ticks = 0, steps = 0;
	
	/**
	 * Representiert sich zeitlich verändernde Abläufe
	 * @param delay - Anzahl an Ticks die es braucht bis zum nächsten Step
	 * @param totalSteps - Die Anzahl an Steps bis die Animation automatisch beendet wird, wenn auf -1 gesetzt bricht die Animation niemals automatisch ab
	 */
	public Animation(int delay, int totalSteps) {
		
		this.delay = delay;
		this.totalSteps = totalSteps;
		
		AnimationHandler.addAnimation(this);
		
		initValues();
		
	}
	
	/**
	 * @Overwrite Wird immer beim Start dieser Animation aufgerufen und dient dazu die Parameter auf einen Startwert zu setzten
	 */
	public void initValues() { }
	
	/**
	 * Wird vom TickTimer jeden Tick aufgerufen
	 */
	public void tick() {
		
		ticks++;
		if(ticks >= delay) {
			ticks = 0;
			step();
		}
		
	}
	
	/**
	 * Wird aufgerufen wenn delay Ticks vergangen sind seid dem letzten Step.
	 * Ruft changeValues() auf wo dann die zeitlichen Parameter verändert werden können
	 */
	private void step() {
		
		changeValues();
		
		steps++;
		if(totalSteps != -1 && steps >= totalSteps) {
			finished(true);
		}
		
	}
	
	/**
	 * @Overwrite Wird jeden Step aufgerufen und dient dazu die zeitlichen Parameter zu ändern
	 */
	public void changeValues() { }
	
	/**
	 * Wird aufgerufen wenn die Animation beendet ist oder wenn sie vom {@link AnimationHandler} beendet wird.
	 * Ruft finaliseValues() auf in denen die Parameter final resettet werden können
	 * @param listRemove - Wenn true wird die Animation aus der Liste entfernt, wenn false nicht
	 */
	public void finished(boolean listRemove) {
		
		if(this.finished == true) { 
			return; 
		}

		this.finished = true;
		finaliseValues();
		
		if(listRemove == true) {
			AnimationHandler.removeAnimation(this);
		}
		
	}
	
	/**
	 * @Overwrite Wird beim finish dieser Animation aufgerufen und dient dazu die zeitlichen Parameter zu resetten bzw auf einen letzten Stand zu bringen
	 */
	public void finaliseValues() { }
	
	public boolean isFinished() {
		return finished;
	}
	public int getSteps() {
		return steps;
	}
	
}
