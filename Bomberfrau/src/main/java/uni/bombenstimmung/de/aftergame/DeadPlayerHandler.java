/*
 * DeadPlayerHandler.
 *
 * Version 1.0
 * Author: Alexej
 *
 * Verwalet die Punkte und die Plazierung der Player
 */
package uni.bombenstimmung.de.aftergame;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import uni.bombenstimmung.de.backend.console.ConsoleHandler;
import uni.bombenstimmung.de.backend.console.MessageType;
import uni.bombenstimmung.de.backend.language.LanguageBlockType;
import uni.bombenstimmung.de.backend.language.LanguageHandler;

import uni.bombenstimmung.de.backend.serverconnection.host.ConnectedClient;
import uni.bombenstimmung.de.game.Player;
import uni.bombenstimmung.de.lobby.LobbyCreate;

public class DeadPlayerHandler {
	private static ArrayList<DeadPlayer> allPlayer = new ArrayList<DeadPlayer>();
	
	private static ArrayList<DeadPlayer> playerFromIngame = new ArrayList<DeadPlayer>();

	public static void generateDummyDeadPlayer() {
		addDeadPlayer(0, "A", 45);
		addDeadPlayer(1, "B", 978);
		addDeadPlayer(2, "C", 325);
		addDeadPlayer(3, "D", 84);
	}
	
    	 /**
    	     * zur uebergabe von PlayerDaten an das Aftergame
    	     * @param id	ID des Players
    	     * @param name	Name des Player
    	     * @param ipAdress	IP-Adresse zugehoerig zu dem Player
    	     * @param host	Boolean, ob dieser Player der Host des Spiels ist
    	     * @param skin	Skin-ID des Players
    	     * @param cC	ConnectedClient
    	     */
	public static void addDeadPlayerFromIngame(int id, String name, String ipAdress, boolean host, int skin, ConnectedClient cC) {
	    playerFromIngame.add(new DeadPlayer(id, name, ipAdress, host, skin, cC));
    	}

    /**
     * Player Datensatz hinzufügen oder einen bestehenden Datensatz anpassen.
     * @param id	ID des Players
     * @param name	Name des Players
     * @param deathTime	Todeszeitpunkt des Players
     */
	public static void addDeadPlayer(int id, String name, int deathTime) {
		if (allPlayer.size() == id) {
			allPlayer.add(id, new DeadPlayer(id, name , deathTime));
			ConsoleHandler.print("new Player: " + id + " ,Name: "+ name + ", deathTime: " + deathTime, MessageType.AFTERGAME);
		}else if(allPlayer.size() > id) {
			allPlayer.get(id).setDeathPlayer(id, name, deathTime, allPlayer.get(id).getScore());
			ConsoleHandler.print("updated Player: " + id + " ,Name: "+ name + ", deathTime: " + deathTime, MessageType.AFTERGAME);
		}
		else {
			ConsoleHandler.print("addDeadPlayer: id is not allowed!", MessageType.AFTERGAME);
		}
	}
	
	public static void updateDeadPlayer(String id, String name, String deathTime, String score) {
	    	allPlayer.get(Integer.parseInt(id)).setDeathPlayer(Integer.parseInt(id), name, Integer.parseInt(deathTime), Integer.parseInt(score));
	}

    /**
     * Punkte für die Partie bestimmen und Plazierung anpassen.
     */
	public static void calculateScore() {
		ArrayList<DeadPlayer> Ranking = allPlayer;

		//DeadPlayerPlayer sortieren nach deathTime
		Collections.sort(Ranking, new Comparator<DeadPlayer>() {
			public int compare(DeadPlayer p1, DeadPlayer p2) {
				return Integer.valueOf(p1.getDeathTime()).compareTo(p2.getDeathTime());
			}
		});

		//Punktevergabe für die besten drei Player
		for(int i = 0; i < Ranking.size(); i++) {
			switch(i) {
			case 0: Ranking.get(0).addScore((Ranking.size()-1)*100); break;
			case 1: Ranking.get(1).addScore((Ranking.size()-2)*100); break;
			case 2: Ranking.get(2).addScore((Ranking.size()-3)*100); break;
			}
		}

		//DeadPlayer sortieren nach Score
		Collections.sort(Ranking, new Comparator<DeadPlayer>() {
			public int compare(DeadPlayer p1, DeadPlayer p2) {
				return Integer.valueOf(p2.getScore()).compareTo(p1.getScore());
			}
		});

		//Ranking zuweisen
		for(int i = 0; i < Ranking.size(); i++) {
			Ranking.get(i).setRanking(i+1);
		}

		//Ergebnisanzeige Aftergame
		for(int i = 0; i < Ranking.size(); i++) {
			String[] aftergame_Ranking = {Ranking.get(i).getRanking()+ ": " + Ranking.get(i).getName() + "    Score: " + Ranking.get(i).getScore(), Ranking.get(i).getRanking()+ ": " + Ranking.get(i).getName() + "    Score: " + Ranking.get(i).getScore()};
			switch(i) {
			case 0: LanguageHandler.getLLB(LanguageBlockType.LB_AFTERGAME_RANKING_1).setLanguageContent(aftergame_Ranking); break;
			case 1: LanguageHandler.getLLB(LanguageBlockType.LB_AFTERGAME_RANKING_2).setLanguageContent(aftergame_Ranking); break;
			case 2: LanguageHandler.getLLB(LanguageBlockType.LB_AFTERGAME_RANKING_3).setLanguageContent(aftergame_Ranking); break;
			case 3: LanguageHandler.getLLB(LanguageBlockType.LB_AFTERGAME_RANKING_4).setLanguageContent(aftergame_Ranking); break;
			}
		}

		//auf urspruengliche Sortierung zurueksetzen und AllPlayer updaten
		Collections.sort(Ranking, new Comparator<DeadPlayer>() {
			public int compare(DeadPlayer p1, DeadPlayer p2) {
				return Integer.valueOf(p1.getId()).compareTo(p2.getId());
			}
		});
		allPlayer = Ranking;
		
//		client.sendMessage(client.getSession(), "601-Hallo");
		
//		LobbyCreate.client.sendMessage(LobbyCreate.client.getSession(), "601-Hallo");
		//LobbyCreate.client.sendMessageToAllClients("601-");
//		LobbyCreate.client.sendMessageToAllClients("601-");
//		System.out.println("Test :" + LobbyCreate.client.getSession());

	}
	
	    public static ArrayList<DeadPlayer> getAllDeadPlayer(){
		return allPlayer;
	    }
	    
	    public static ArrayList<DeadPlayer> getPlayerFromIngame(){
		return playerFromIngame;
	    }

}