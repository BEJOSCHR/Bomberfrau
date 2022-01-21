/*
 * DeadPlayerHandler.
 *
 * Version 1.0
 * Author: Alexej
 *
 * Verwalet die Punkte und die Plazierung der Player
 */
package uni.bombenstimmung.de.aftergame;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import uni.bombenstimmung.de.backend.console.ConsoleHandler;
import uni.bombenstimmung.de.backend.console.MessageType;
import uni.bombenstimmung.de.backend.graphics.GraphicsHandler;
import uni.bombenstimmung.de.backend.images.ImageHandler;
import uni.bombenstimmung.de.backend.images.ImageType;
import uni.bombenstimmung.de.backend.language.LanguageBlockType;
import uni.bombenstimmung.de.backend.language.LanguageHandler;
import uni.bombenstimmung.de.backend.serverconnection.host.ConnectedClient;


public class DeadPlayerHandler {
	private static ArrayList<DeadPlayer> allPlayer = new ArrayList<DeadPlayer>();
	//private static ArrayList<DeadPlayer> playerFromIngame = new ArrayList<DeadPlayer>();
	private static DeadPlayer clientPlayer;	//aktueller Player

	
	public static void setClientPlayer(int id, String name, String ipAdress, boolean host, int skin, ConnectedClient cC) {
	    clientPlayer = new DeadPlayer(id, name, ipAdress, host, skin, cC);
	}	
	
    	 /**
    	 * zur uebergabe von PlayerDaten an das Aftergame
    	 * @param id		ID des Players
    	 * @param name		Name des Players
    	 * @param ipAdress	IP-Adresse vom Player
    	 * @param host		ob Spieler Host ist
    	 * @param skin		Skin-ID des Players
    	 */
//	public static void addDeadPlayerFromIngame(int id, String name, String ipAdress, boolean host, int skin) {
//	    playerFromIngame.add(new DeadPlayer(id, name, ipAdress, host, skin));
//    	}

        /**
         * Player Datensatz hinzufügen oder einen bestehenden Datensatz anpassen.
         * @param id		ID des Players
         * @param name		Name des Players
         * @param deathTime	Todeszeitpunkt des Players
         */
	public static void addDeadPlayer(int id, String name, int deathTime) {
	    if (allPlayer.size() == id) {
		allPlayer.add(id, new DeadPlayer(id, name , deathTime));
		ConsoleHandler.print("new Player: " + id + " ,Name: "+ name + ", deathTime: " + deathTime, MessageType.AFTERGAME);
	    }
	    else if(allPlayer.size() > id) {
		allPlayer.get(id).setDeathPlayer(id, name, deathTime, allPlayer.get(id).getScore());
		ConsoleHandler.print("updated Player: " + id + " ,Name: "+ name + ", deathTime: " + deathTime, MessageType.AFTERGAME);
	    }
	    else {
		ConsoleHandler.print("addDeadPlayer: id is not allowed!", MessageType.AFTERGAME);
	    }
	}
	
        /**
         * Anpassung der PlayerDaten
         * @param id		ID des Players
         * @param name		Name des Players
         * @param deathTime	Todeszeitpunkt des Players
         * @param score		Punktzahl des Players
         */
	public static void updateDeadPlayer(String id, String name, String deathTime, String score) {
	    if (allPlayer.size() == Integer.parseInt(id)) {
		    allPlayer.add(Integer.parseInt(id), new DeadPlayer(Integer.parseInt(id), name, Integer.parseInt(deathTime), Integer.parseInt(score)));
	    }
	    else if(allPlayer.size() > Integer.parseInt(id)) {
		allPlayer.get(Integer.parseInt(id)).setDeathPlayer(Integer.parseInt(id), name, Integer.parseInt(deathTime), Integer.parseInt(score));
	    }
	}

	/**
     	* Punkte für die Partie bestimmen und Plazierung anpassen.
     	*/
	public static void calculateScore() {
	    ArrayList<DeadPlayer> Ranking = allPlayer;

	    //sortieren nach deathTime
	    Collections.sort(Ranking, new Comparator<DeadPlayer>() {
		public int compare(DeadPlayer p1, DeadPlayer p2) {
		    return Integer.valueOf(p1.getDeathTime()).compareTo(p2.getDeathTime());
		}
	    });

	    //Punktevergabe für die besten drei Player
	    for(int i = 0; i < Ranking.size(); i++) {
		switch(i) {
		case 0: Ranking.get(0).addScore((Ranking.size()-1)*100);
		break;
		case 1: Ranking.get(1).addScore((Ranking.size()-2)*100);
		break;
		case 2: Ranking.get(2).addScore((Ranking.size()-3)*100);
		break;
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
		String[] aftergame_name = {Ranking.get(i).getRanking()+ ": " + Ranking.get(i).getName(), Ranking.get(i).getRanking()+ ": " + Ranking.get(i).getName()};
		String[] aftergame_score = {"" + Ranking.get(i).getScore(), "" + Ranking.get(i).getScore()};
		switch(i) {
		case 0: LanguageHandler.getLLB(LanguageBlockType.LB_AFTERGAME_SCORE_1).setLanguageContent(aftergame_score); 
			LanguageHandler.getLLB(LanguageBlockType.LB_AFTERGAME_NAME_1).setLanguageContent(aftergame_name); 
		break;
		case 1: LanguageHandler.getLLB(LanguageBlockType.LB_AFTERGAME_SCORE_2).setLanguageContent(aftergame_score);
			LanguageHandler.getLLB(LanguageBlockType.LB_AFTERGAME_NAME_2).setLanguageContent(aftergame_name); 
		break;
		case 2: LanguageHandler.getLLB(LanguageBlockType.LB_AFTERGAME_SCORE_3).setLanguageContent(aftergame_score);
			LanguageHandler.getLLB(LanguageBlockType.LB_AFTERGAME_NAME_3).setLanguageContent(aftergame_name); 
		break;
		case 3: LanguageHandler.getLLB(LanguageBlockType.LB_AFTERGAME_SCORE_4).setLanguageContent(aftergame_score);
			LanguageHandler.getLLB(LanguageBlockType.LB_AFTERGAME_NAME_4).setLanguageContent(aftergame_name); 
		break;
		}
	    }

	    //auf urspruengliche Sortierung zurueksetzen und AllPlayer updaten
	    Collections.sort(Ranking, new Comparator<DeadPlayer>() {
		public int compare(DeadPlayer p1, DeadPlayer p2) {
		    return Integer.valueOf(p1.getId()).compareTo(p2.getId());
		}
	    });
	    
	    allPlayer = Ranking;
	}
	
	public static void drawImages(Graphics g, int x) {
	    for(int i = 0; i < DeadPlayerHandler.getAllDeadPlayer().size(); i++) {
		switch(i) {
		case 0: g.drawImage(ImageHandler.getImage(ImageType.IMAGE_AFTERGAME_1).getImage(), GraphicsHandler.getWidth()*1/8, GraphicsHandler.getHeight()*2/8-35, 80, 80, null);
		break;
		case 1: g.drawImage(ImageHandler.getImage(ImageType.IMAGE_AFTERGAME_2).getImage(), GraphicsHandler.getWidth()*1/8, GraphicsHandler.getHeight()*3/8-35, 80, 80, null);
		break;
		case 2: g.drawImage(ImageHandler.getImage(ImageType.IMAGE_AFTERGAME_2).getImage(), GraphicsHandler.getWidth()*1/8, GraphicsHandler.getHeight()*4/8-35, 80, 80, null);
		break;
		case 3: g.drawImage(ImageHandler.getImage(ImageType.IMAGE_AFTERGAME_2).getImage(), GraphicsHandler.getWidth()*1/8, GraphicsHandler.getHeight()*5/8-35, 80, 80, null);
		break;
		}
	    }
	}
	
	public static ArrayList<DeadPlayer> getAllDeadPlayer(){
	    return allPlayer;
	}
	    
	public static DeadPlayer getClientPlayer() {
	    return clientPlayer;
	}

}