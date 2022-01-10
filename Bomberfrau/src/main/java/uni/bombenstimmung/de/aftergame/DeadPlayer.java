/*
 * DeadPlayer
 *
 * Version 1.0
 * 
 * Author: Alexej
 * 
 *
 * speicherung von Player Atributen
 */
 
package uni.bombenstimmung.de.aftergame;

public class DeadPlayer {
	private int id;
	private String name;
	private int deathTime;
	private int score = 0;
	private int ranking = 0;
	//private int preferredMapID = 0;
	
	
	//Konstruktor
	public DeadPlayer(int id, String name, int deathTime) {
		this.id = id;
		this.name = name;
		this.deathTime = deathTime;
	}
	
	public void updateDeathPlayer(int id, String name, int deathTime) {
		this.id = id;
		this.name = name;
		this.deathTime = deathTime;
		this.score = 0;
		this.ranking = 0;
		//this.preferredMapID = 0;
	}

	public int getDeathTime() {
		return deathTime;
	}
	
	public int getScore() {
		return score;
	}


	public void addScore(int score) {
		this.score += score;
	}


	public int getRanking() {
		return ranking;
	}


	public void setRanking(int ranking) {
		this.ranking = ranking;
	}


	public String getName() {
		return name;
	}
	
	public int getId() {
		return id;
	}
	
}