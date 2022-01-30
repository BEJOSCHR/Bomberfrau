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
    private static ArrayList<DeadPlayer> allDeadPlayer = new ArrayList<DeadPlayer>();
    private static DeadPlayer clientPlayer; // aktueller Player

    /**
     * Player Datensatz hinzufügen oder einen bestehenden Datensatz anpassen.
     * 
     * @param id        ID des Players
     * @param name      Name des Players
     * @param deathTime Todeszeitpunkt des Players
     */
    public static void addDeadPlayer(int id, String name, int deathTime, int skin) {
	if (deathTime == 0)
	    deathTime = Integer.MAX_VALUE;
	if (allDeadPlayer.size() == id) {
	    allDeadPlayer.add(id, new DeadPlayer(id, name, deathTime, skin));
	    ConsoleHandler.print("new Player: " + id + " ,Name: " + name + ", deathTime: " + deathTime,
		    MessageType.AFTERGAME);
	} else if (allDeadPlayer.size() > id) {
	    allDeadPlayer.get(id).setDeadPlayer(id, name, deathTime, allDeadPlayer.get(id).getScore(), skin,
		    allDeadPlayer.get(id).getRanking(), allDeadPlayer.get(id).getOldScore());
	    ConsoleHandler.print("updated Player: " + id + " ,Name: " + name + ", deathTime: " + deathTime,
		    MessageType.AFTERGAME);
	} else {
	    ConsoleHandler.print("addDeadPlayer: id is not allowed!", MessageType.AFTERGAME);
	}
    }

    /**
     * Anpassung der PlayerDaten
     * 
     * @param id        ID des Players
     * @param name      Name des Players
     * @param deathTime Todeszeitpunkt des Players
     * @param score     Punktzahl des Players
     */
    public static void updateDeadPlayer(String id, String name, String deathTime, String score, String skin,
	    String rank, String oldScore) {
	if (allDeadPlayer.size() == Integer.parseInt(id)) {
	    allDeadPlayer.add(Integer.parseInt(id),
		    new DeadPlayer(Integer.parseInt(id), name, Integer.parseInt(deathTime), Integer.parseInt(score),
			    Integer.parseInt(skin), Integer.parseInt(rank), Integer.parseInt(oldScore)));
	} else if (allDeadPlayer.size() > Integer.parseInt(id)) {
	    allDeadPlayer.get(Integer.parseInt(id)).setDeadPlayer(Integer.parseInt(id), name,
		    Integer.parseInt(deathTime), Integer.parseInt(score), Integer.parseInt(skin),
		    Integer.parseInt(rank), Integer.parseInt(oldScore));
	}
    }

    public static void setClientPlayer(int id, String name, String ipAdress, boolean host, int skin,
	    ConnectedClient cC) {
	clientPlayer = new DeadPlayer(id, name, ipAdress, host, skin, cC);
    }

    /**
     * Punkte für die Partie bestimmen und Plazierung anpassen.
     */
    public static void calculateScore() {

	// sortieren nach deathTime
	Collections.sort(allDeadPlayer, new Comparator<DeadPlayer>() {
	    public int compare(DeadPlayer p1, DeadPlayer p2) {
		return Integer.valueOf(p2.getDeathTime()).compareTo(p1.getDeathTime());
	    }
	});

	// Punktevergabe für die besten drei Player
	for (int i = 0; i < allDeadPlayer.size(); i++) {
	    switch (i) {
	    case 0:
		allDeadPlayer.get(0).addScore((allDeadPlayer.size() - 1) * 100);
		break;
	    case 1:
		allDeadPlayer.get(1).addScore((allDeadPlayer.size() - 2) * 100);
		break;
	    case 2:
		allDeadPlayer.get(2).addScore((allDeadPlayer.size() - 3) * 100);
		break;
	    }
	}

	// DeadPlayer sortieren nach Score
	Collections.sort(allDeadPlayer, new Comparator<DeadPlayer>() {
	    public int compare(DeadPlayer p1, DeadPlayer p2) {
		return Integer.valueOf(p2.getScore()).compareTo(p1.getScore());
	    }
	});

	// Ranking zuweisen
	for (int i = 0; i < allDeadPlayer.size(); i++) {
	    allDeadPlayer.get(i).setRanking(i + 1);
	}

	Collections.sort(allDeadPlayer, new Comparator<DeadPlayer>() {
	    public int compare(DeadPlayer p1, DeadPlayer p2) {
		return Integer.valueOf(p1.getRanking()).compareTo(p2.getRanking());
	    }
	});

	// auf urspruengliche Sortierung zurueksetzen
	Collections.sort(allDeadPlayer, new Comparator<DeadPlayer>() {
	    public int compare(DeadPlayer p1, DeadPlayer p2) {
		return Integer.valueOf(p1.getId()).compareTo(p2.getId());
	    }
	});

	// Host uebermittelt aktuelle Punktzahl an die Clients
	for (int i = 0; i < DeadPlayerHandler.getAllDeadPlayer().size(); i++) {
	    DeadPlayerHandler.getClientPlayer().getCC()
		    .sendMessageToAllClients("601-" + i + "-" + DeadPlayerHandler.getAllDeadPlayer().get(i).getName()
			    + "-" + DeadPlayerHandler.getAllDeadPlayer().get(i).getDeathTime() + "-"
			    + DeadPlayerHandler.getAllDeadPlayer().get(i).getScore() + "-"
			    + DeadPlayerHandler.getAllDeadPlayer().get(i).getSkin() + "-"
			    + DeadPlayerHandler.getAllDeadPlayer().get(i).getRanking() + "-"
			    + DeadPlayerHandler.getAllDeadPlayer().get(i).getOldScore());
	}

	showResult();
	DeadPlayerHandler.getClientPlayer().getCC().sendMessageToAllClients("603-");

    }

    /**
     * Punktestand anzeigen
     */
    public static void showResult() {
	// Ergebnisanzeige Aftergame
	for (int i = 0; i < allDeadPlayer.size(); i++) {
	    String[] aftergame_name = { allDeadPlayer.get(i).getRanking() + ": " + allDeadPlayer.get(i).getName(),
		    allDeadPlayer.get(i).getRanking() + ": " + allDeadPlayer.get(i).getName() };
	    String[] aftergame_score = { "" + allDeadPlayer.get(i).getScore(), "" + allDeadPlayer.get(i).getScore() };
	    int difference = (allDeadPlayer.get(i).getScore()) - (allDeadPlayer.get(i).getOldScore());
	    String[] plus = { "+" + difference, "+" + difference };

	    switch (allDeadPlayer.get(i).getRanking()) {
	    case 1:
		LanguageHandler.getLLB(LanguageBlockType.LB_AFTERGAME_SCORE_1).setLanguageContent(aftergame_score);
		LanguageHandler.getLLB(LanguageBlockType.LB_AFTERGAME_NAME_1).setLanguageContent(aftergame_name);
		LanguageHandler.getLLB(LanguageBlockType.LB_AFTERGAME_PLUS_1).setLanguageContent(plus);
		break;
	    case 2:
		LanguageHandler.getLLB(LanguageBlockType.LB_AFTERGAME_SCORE_2).setLanguageContent(aftergame_score);
		LanguageHandler.getLLB(LanguageBlockType.LB_AFTERGAME_NAME_2).setLanguageContent(aftergame_name);
		LanguageHandler.getLLB(LanguageBlockType.LB_AFTERGAME_PLUS_2).setLanguageContent(plus);
		break;
	    case 3:
		LanguageHandler.getLLB(LanguageBlockType.LB_AFTERGAME_SCORE_3).setLanguageContent(aftergame_score);
		LanguageHandler.getLLB(LanguageBlockType.LB_AFTERGAME_NAME_3).setLanguageContent(aftergame_name);
		LanguageHandler.getLLB(LanguageBlockType.LB_AFTERGAME_PLUS_3).setLanguageContent(plus);
		break;
	    case 4:
		LanguageHandler.getLLB(LanguageBlockType.LB_AFTERGAME_SCORE_4).setLanguageContent(aftergame_score);
		LanguageHandler.getLLB(LanguageBlockType.LB_AFTERGAME_NAME_4).setLanguageContent(aftergame_name);
		LanguageHandler.getLLB(LanguageBlockType.LB_AFTERGAME_PLUS_4).setLanguageContent(plus);
		break;
	    }
	}
    }

    /**
     * Skins anzeigen
     */
    public static void drawImages(Graphics g) {
	for (int i = 0; i < allDeadPlayer.size(); i++) {
	    switch (allDeadPlayer.get(i).getRanking()) {
	    case 1:
		switch (allDeadPlayer.get(i).getSkin()) {
		case 0:
		    g.drawImage(LobbyPlayer.skinSelection[0][AnimationData.lobby_walk].getImage(),
			    GraphicsHandler.getWidth() * 1 / 8,
			    GraphicsHandler.getHeight() * 2 / 8 - Settings.scaleValue(65), Settings.scaleValue(140),
			    Settings.scaleValue(140), null);
		    break;
		case 1:
		    g.drawImage(LobbyPlayer.skinSelection[1][AnimationData.lobby_walk].getImage(),
			    GraphicsHandler.getWidth() * 1 / 8,
			    GraphicsHandler.getHeight() * 2 / 8 - Settings.scaleValue(65), Settings.scaleValue(140),
			    Settings.scaleValue(140), null);
		    break;
		case 2:
		    g.drawImage(LobbyPlayer.skinSelection[2][AnimationData.lobby_walk].getImage(),
			    GraphicsHandler.getWidth() * 1 / 8,
			    GraphicsHandler.getHeight() * 2 / 8 - Settings.scaleValue(65), Settings.scaleValue(140),
			    Settings.scaleValue(140), null);
		    break;
		case 3:
		    g.drawImage(LobbyPlayer.skinSelection[3][AnimationData.lobby_walk].getImage(),
			    GraphicsHandler.getWidth() * 1 / 8,
			    GraphicsHandler.getHeight() * 2 / 8 - Settings.scaleValue(65), Settings.scaleValue(140),
			    Settings.scaleValue(140), null);
		    break;
		}
		break;
	    case 2:
		switch (allDeadPlayer.get(i).getSkin()) {
		case 0:
		    g.drawImage(LobbyPlayer.skinSelection[0][AnimationData.lobby_walk].getImage(),
			    GraphicsHandler.getWidth() * 1 / 8,
			    GraphicsHandler.getHeight() * 3 / 8 - Settings.scaleValue(65), Settings.scaleValue(140),
			    Settings.scaleValue(140), null);
		    break;
		case 1:
		    g.drawImage(LobbyPlayer.skinSelection[1][AnimationData.lobby_walk].getImage(),
			    GraphicsHandler.getWidth() * 1 / 8,
			    GraphicsHandler.getHeight() * 3 / 8 - Settings.scaleValue(65), Settings.scaleValue(140),
			    Settings.scaleValue(140), null);
		    break;
		case 2:
		    g.drawImage(LobbyPlayer.skinSelection[2][AnimationData.lobby_walk].getImage(),
			    GraphicsHandler.getWidth() * 1 / 8,
			    GraphicsHandler.getHeight() * 3 / 8 - Settings.scaleValue(65), Settings.scaleValue(140),
			    Settings.scaleValue(140), null);
		    break;
		case 3:
		    g.drawImage(LobbyPlayer.skinSelection[3][AnimationData.lobby_walk].getImage(),
			    GraphicsHandler.getWidth() * 1 / 8,
			    GraphicsHandler.getHeight() * 3 / 8 - Settings.scaleValue(65), Settings.scaleValue(140),
			    Settings.scaleValue(140), null);
		    break;
		}
		break;
	    case 3:
		switch (allDeadPlayer.get(i).getSkin()) {
		case 0:
		    g.drawImage(LobbyPlayer.skinSelection[0][AnimationData.lobby_walk].getImage(),
			    GraphicsHandler.getWidth() * 1 / 8,
			    GraphicsHandler.getHeight() * 4 / 8 - Settings.scaleValue(65), Settings.scaleValue(140),
			    Settings.scaleValue(140), null);
		    break;
		case 1:
		    g.drawImage(LobbyPlayer.skinSelection[1][AnimationData.lobby_walk].getImage(),
			    GraphicsHandler.getWidth() * 1 / 8,
			    GraphicsHandler.getHeight() * 4 / 8 - Settings.scaleValue(65), Settings.scaleValue(140),
			    Settings.scaleValue(140), null);
		    break;
		case 2:
		    g.drawImage(LobbyPlayer.skinSelection[2][AnimationData.lobby_walk].getImage(),
			    GraphicsHandler.getWidth() * 1 / 8,
			    GraphicsHandler.getHeight() * 4 / 8 - Settings.scaleValue(65), Settings.scaleValue(140),
			    Settings.scaleValue(140), null);
		    break;
		case 3:
		    g.drawImage(LobbyPlayer.skinSelection[3][AnimationData.lobby_walk].getImage(),
			    GraphicsHandler.getWidth() * 1 / 8,
			    GraphicsHandler.getHeight() * 4 / 8 - Settings.scaleValue(65), Settings.scaleValue(140),
			    Settings.scaleValue(140), null);
		    break;
		}
		break;
	    case 4:
		switch (allDeadPlayer.get(i).getSkin()) {
		case 0:
		    g.drawImage(LobbyPlayer.skinSelection[0][AnimationData.lobby_walk].getImage(),
			    GraphicsHandler.getWidth() * 1 / 8,
			    GraphicsHandler.getHeight() * 5 / 8 - Settings.scaleValue(65), Settings.scaleValue(140),
			    Settings.scaleValue(140), null);
		    break;
		case 1:
		    g.drawImage(LobbyPlayer.skinSelection[1][AnimationData.lobby_walk].getImage(),
			    GraphicsHandler.getWidth() * 1 / 8,
			    GraphicsHandler.getHeight() * 5 / 8 - Settings.scaleValue(65), Settings.scaleValue(140),
			    Settings.scaleValue(140), null);
		    break;
		case 2:
		    g.drawImage(LobbyPlayer.skinSelection[2][AnimationData.lobby_walk].getImage(),
			    GraphicsHandler.getWidth() * 1 / 8,
			    GraphicsHandler.getHeight() * 5 / 8 - Settings.scaleValue(65), Settings.scaleValue(140),
			    Settings.scaleValue(140), null);
		    break;
		case 3:
		    g.drawImage(LobbyPlayer.skinSelection[3][AnimationData.lobby_walk].getImage(),
			    GraphicsHandler.getWidth() * 1 / 8,
			    GraphicsHandler.getHeight() * 5 / 8 - Settings.scaleValue(65), Settings.scaleValue(140),
			    Settings.scaleValue(140), null);
		    break;
		}
		break;
	    }
	}
    }

    public static ArrayList<DeadPlayer> getAllDeadPlayer() {
	return allDeadPlayer;
    }

    public static DeadPlayer getClientPlayer() {
	return clientPlayer;
    }

    public static void resetDeadPlayerHandler() {
	allDeadPlayer.clear();
	String[] x = { "", "" };
	LanguageHandler.getLLB(LanguageBlockType.LB_AFTERGAME_SCORE_1).setLanguageContent(x);
	LanguageHandler.getLLB(LanguageBlockType.LB_AFTERGAME_NAME_1).setLanguageContent(x);
	LanguageHandler.getLLB(LanguageBlockType.LB_AFTERGAME_SCORE_2).setLanguageContent(x);
	LanguageHandler.getLLB(LanguageBlockType.LB_AFTERGAME_NAME_2).setLanguageContent(x);
	LanguageHandler.getLLB(LanguageBlockType.LB_AFTERGAME_SCORE_3).setLanguageContent(x);
	LanguageHandler.getLLB(LanguageBlockType.LB_AFTERGAME_NAME_3).setLanguageContent(x);
	LanguageHandler.getLLB(LanguageBlockType.LB_AFTERGAME_SCORE_4).setLanguageContent(x);
	LanguageHandler.getLLB(LanguageBlockType.LB_AFTERGAME_NAME_4).setLanguageContent(x);
    }

    public static void removeDeadPlayer(String id) {
	for (int i = 0; i < allDeadPlayer.size(); i++) {
	    if (allDeadPlayer.get(i).getId() == Integer.parseInt(id)) {
		allDeadPlayer.remove(i);
	    }
	}

    }

}