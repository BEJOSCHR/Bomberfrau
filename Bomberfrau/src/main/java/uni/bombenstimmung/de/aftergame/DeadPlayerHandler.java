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

import uni.bombenstimmung.de.backend.animation.AnimationData;
import uni.bombenstimmung.de.backend.console.ConsoleHandler;
import uni.bombenstimmung.de.backend.console.MessageType;
import uni.bombenstimmung.de.backend.graphics.GraphicsHandler;
import uni.bombenstimmung.de.backend.language.LanguageBlockType;
import uni.bombenstimmung.de.backend.language.LanguageHandler;
import uni.bombenstimmung.de.backend.serverconnection.host.ConnectedClient;
import uni.bombenstimmung.de.lobby.LobbyPlayer;
import uni.bombenstimmung.de.menu.Settings;


public class DeadPlayerHandler {
	private static ArrayList<DeadPlayer> allPlayer = new ArrayList<DeadPlayer>();
	private static ArrayList<DeadPlayer> ranking = new ArrayList<DeadPlayer>();
	private static DeadPlayer clientPlayer;	//aktueller Player

	
	public static void setClientPlayer(int id, String name, String ipAdress, boolean host, int skin, ConnectedClient cC) {
	    clientPlayer = new DeadPlayer(id, name, ipAdress, host, skin, cC);
	}	

        /**
         * Player Datensatz hinzufügen oder einen bestehenden Datensatz anpassen.
         * @param id		ID des Players
         * @param name		Name des Players
         * @param deathTime	Todeszeitpunkt des Players
         */
	public static void addDeadPlayer(int id, String name, int deathTime, int skin) {
	    if (allPlayer.size() == id) {
		allPlayer.add(id, new DeadPlayer(id, name , deathTime, skin));
		ConsoleHandler.print("new Player: " + id + " ,Name: "+ name + ", deathTime: " + deathTime, MessageType.AFTERGAME);
	    }
	    else if(allPlayer.size() > id) {
		allPlayer.get(id).setDeathPlayer(id, name, deathTime, allPlayer.get(id).getScore(), skin);
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
	public static void updateDeadPlayer(String id, String name, String deathTime, String score, String skin) {
	    if (allPlayer.size() == Integer.parseInt(id)) {
		    allPlayer.add(Integer.parseInt(id), new DeadPlayer(Integer.parseInt(id), name, Integer.parseInt(deathTime), Integer.parseInt(score), Integer.parseInt(skin)));
	    }
	    else if(allPlayer.size() > Integer.parseInt(id)) {
		allPlayer.get(Integer.parseInt(id)).setDeathPlayer(Integer.parseInt(id), name, Integer.parseInt(deathTime), Integer.parseInt(score), Integer.parseInt(skin));
	    }
	}

	/**
     	* Punkte für die Partie bestimmen und Plazierung anpassen.
     	*/
	public static void calculateScore() {
	    ranking = allPlayer;

	    //sortieren nach deathTime
	    Collections.sort(ranking, new Comparator<DeadPlayer>() {
		public int compare(DeadPlayer p1, DeadPlayer p2) {
		    return Integer.valueOf(p1.getDeathTime()).compareTo(p2.getDeathTime());
		}
	    });

	    //Punktevergabe für die besten drei Player
	    for(int i = 0; i < ranking.size(); i++) {
		switch(i) {
		case 0: ranking.get(0).addScore((ranking.size()-1)*100);
		break;
		case 1: ranking.get(1).addScore((ranking.size()-2)*100);
		break;
		case 2: ranking.get(2).addScore((ranking.size()-3)*100);
		break;
		}
	    }

	    //DeadPlayer sortieren nach Score
	    Collections.sort(ranking, new Comparator<DeadPlayer>() {
		public int compare(DeadPlayer p1, DeadPlayer p2) {
		    return Integer.valueOf(p2.getScore()).compareTo(p1.getScore());
		}
	    });

	    //Ranking zuweisen
	    for(int i = 0; i < ranking.size(); i++) {
		ranking.get(i).setRanking(i+1);
	    }

	    //Ergebnisanzeige Aftergame
	    for(int i = 0; i < ranking.size(); i++) {
		String[] aftergame_name = {ranking.get(i).getRanking()+ ": " + ranking.get(i).getName(), ranking.get(i).getRanking()+ ": " + ranking.get(i).getName()};
		String[] aftergame_score = {"" + ranking.get(i).getScore(), "" + ranking.get(i).getScore()};
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
	    Collections.sort(ranking, new Comparator<DeadPlayer>() {
		public int compare(DeadPlayer p1, DeadPlayer p2) {
		    return Integer.valueOf(p1.getId()).compareTo(p2.getId());
		}
	    });    
	    allPlayer = ranking;
	    
	    Collections.sort(ranking, new Comparator<DeadPlayer>() {
		public int compare(DeadPlayer p1, DeadPlayer p2) {
		    return Integer.valueOf(p1.getRanking()).compareTo(p2.getRanking());
		}
	    });

	}
	
	public static void drawImages(Graphics g) {
	    for(int i = 0; i < ranking.size(); i++) {
		switch(i) {
		case 0: 
		    switch(ranking.get(i).getSkin()) {
		    case 0:
			g.drawImage(LobbyPlayer.skinSelection[0][AnimationData.lobby_walk].getImage(), GraphicsHandler.getWidth()*1/8, GraphicsHandler.getHeight()*2/8-Settings.scaleValue(65), Settings.scaleValue(140), Settings.scaleValue(140), null);
			break;
		    case 1:
			g.drawImage(LobbyPlayer.skinSelection[1][AnimationData.lobby_walk].getImage(), GraphicsHandler.getWidth()*1/8, GraphicsHandler.getHeight()*2/8-Settings.scaleValue(65), Settings.scaleValue(140), Settings.scaleValue(140), null);
			break;
		    case 2:
			g.drawImage(LobbyPlayer.skinSelection[2][AnimationData.lobby_walk].getImage(), GraphicsHandler.getWidth()*1/8, GraphicsHandler.getHeight()*2/8-Settings.scaleValue(65), Settings.scaleValue(140), Settings.scaleValue(140), null);
			break;
		    case 3:
			g.drawImage(LobbyPlayer.skinSelection[3][AnimationData.lobby_walk].getImage(), GraphicsHandler.getWidth()*1/8, GraphicsHandler.getHeight()*2/8-Settings.scaleValue(65), Settings.scaleValue(140), Settings.scaleValue(140), null);
			break;
		    }
		break;
		case 1:
		    switch(ranking.get(i).getSkin()) {
		    case 0:
			g.drawImage(LobbyPlayer.skinSelection[0][AnimationData.lobby_walk].getImage(), GraphicsHandler.getWidth()*1/8, GraphicsHandler.getHeight()*3/8-Settings.scaleValue(65), Settings.scaleValue(140), Settings.scaleValue(140), null);
			break;
		    case 1:
			g.drawImage(LobbyPlayer.skinSelection[1][AnimationData.lobby_walk].getImage(), GraphicsHandler.getWidth()*1/8, GraphicsHandler.getHeight()*3/8-Settings.scaleValue(65), Settings.scaleValue(140), Settings.scaleValue(140), null);
			break;
		    case 2:
			g.drawImage(LobbyPlayer.skinSelection[2][AnimationData.lobby_walk].getImage(), GraphicsHandler.getWidth()*1/8, GraphicsHandler.getHeight()*3/8-Settings.scaleValue(65), Settings.scaleValue(140), Settings.scaleValue(140), null);
			break;
		    case 3:
			g.drawImage(LobbyPlayer.skinSelection[3][AnimationData.lobby_walk].getImage(), GraphicsHandler.getWidth()*1/8, GraphicsHandler.getHeight()*3/8-Settings.scaleValue(65), Settings.scaleValue(140), Settings.scaleValue(140), null);
			break;
		    }
		break;
		case 2:
		    switch(ranking.get(i).getSkin()) {
		    case 0:
			g.drawImage(LobbyPlayer.skinSelection[0][AnimationData.lobby_walk].getImage(), GraphicsHandler.getWidth()*1/8, GraphicsHandler.getHeight()*4/8-Settings.scaleValue(65), Settings.scaleValue(140), Settings.scaleValue(140), null);
			break;
		    case 1:
			g.drawImage(LobbyPlayer.skinSelection[1][AnimationData.lobby_walk].getImage(), GraphicsHandler.getWidth()*1/8, GraphicsHandler.getHeight()*4/8-Settings.scaleValue(65), Settings.scaleValue(140), Settings.scaleValue(140), null);
			break;
		    case 2:
			g.drawImage(LobbyPlayer.skinSelection[2][AnimationData.lobby_walk].getImage(), GraphicsHandler.getWidth()*1/8, GraphicsHandler.getHeight()*4/8-Settings.scaleValue(65), Settings.scaleValue(140), Settings.scaleValue(140), null);
			break;
		    case 3:
			g.drawImage(LobbyPlayer.skinSelection[3][AnimationData.lobby_walk].getImage(), GraphicsHandler.getWidth()*1/8, GraphicsHandler.getHeight()*4/8-Settings.scaleValue(65), Settings.scaleValue(140), Settings.scaleValue(140), null);
			break;
		    }
		break;
		case 3:
		    switch(ranking.get(i).getSkin()) {
		    case 0:
			g.drawImage(LobbyPlayer.skinSelection[0][AnimationData.lobby_walk].getImage(), GraphicsHandler.getWidth()*1/8, GraphicsHandler.getHeight()*5/8-Settings.scaleValue(65), Settings.scaleValue(140), Settings.scaleValue(140), null);
			break;
		    case 1:
			g.drawImage(LobbyPlayer.skinSelection[1][AnimationData.lobby_walk].getImage(), GraphicsHandler.getWidth()*1/8, GraphicsHandler.getHeight()*5/8-Settings.scaleValue(65), Settings.scaleValue(140), Settings.scaleValue(140), null);
			break;
		    case 2:
			g.drawImage(LobbyPlayer.skinSelection[2][AnimationData.lobby_walk].getImage(), GraphicsHandler.getWidth()*1/8, GraphicsHandler.getHeight()*5/8-Settings.scaleValue(65), Settings.scaleValue(140), Settings.scaleValue(140), null);
			break;
		    case 3:
			g.drawImage(LobbyPlayer.skinSelection[3][AnimationData.lobby_walk].getImage(), GraphicsHandler.getWidth()*1/8, GraphicsHandler.getHeight()*5/8-Settings.scaleValue(65), Settings.scaleValue(140), Settings.scaleValue(140), null);
			break;
		    }
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