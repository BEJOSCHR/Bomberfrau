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

import uni.bombenstimmung.de.backend.serverconnection.host.ConnectedClient;

public class DeadPlayer {
	private int id;
	private String name;
	
	private String ipAdress;
	private boolean host;
	private int skin;
	
	private ConnectedClient connectedClient;
	
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
	
	public DeadPlayer(int id, String name, String ipAdress, boolean host, int skin) {
		this.id = id;
		this.name = name;
		this.ipAdress = ipAdress;
		this.host = host;
		this.skin = skin;		
	    }
	public boolean isHost() {
	    return host;
	}

	public DeadPlayer(int id, String name, String ipAdress, boolean host, int skin, ConnectedClient cC) {
		this.id = id;
		this.name = name;
		this.ipAdress = ipAdress;
		this.host = host;
		this.skin = skin;
		this.connectedClient = cC;
	}
	
	public void setDeathPlayer(int id, String name, int deathTime, int score) {
		this.id = id;
		this.name = name;
		this.deathTime = deathTime;
		this.score = score;
		this.ranking = 0;
		//this.preferredMapID = 0;
	}
	

	public int getDeathTime() {
		return deathTime;
	}
	
	public int getScore() {
		return score;
	}
	
	public void setScore(int score) {
	    	this.score = score;
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
	
	public void setName(String name) {
		this.name = name;
	}
	
	public int getId() {
		return id;
	}
	
	public String getIp() {
		return ipAdress;
	}
	
	public ConnectedClient getCC() {
	    return connectedClient;
	}
	
}