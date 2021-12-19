package uni.bombenstimmung.de.lobby;

import java.awt.event.KeyEvent;
import javax.swing.Timer;

import uni.bombenstimmung.de.backend.console.ConsoleHandler;
import uni.bombenstimmung.de.backend.console.MessageType;
import uni.bombenstimmung.de.backend.maa.MouseActionAreaHandler;

public class LobbyMovement {

	int zaehlerLobbyKeySelection = 0;
	static Timer timerClicked;
	static int timerZaehler = 0;
	
	public static void KeyEvents(int keyCode) {
		if (keyCode == KeyEvent.VK_RIGHT) {
			//timerManagement();
			OwnButtons.keyIsPressed(keyCode);
			Lobby_Create.setIncrementMap();
			
		}

		else if (keyCode == KeyEvent.VK_LEFT)
			OwnButtons.keyIsPressed(keyCode);
			Lobby_Create.setDecrementMap();
	}
	
	public static void timerManagement() {
		timerClicked = new Timer(500, ae -> whatTimerdoes());
		timerClicked.start();
	}
	
	public static void whatTimerdoes() {
		timerZaehler++;
		if (timerZaehler >= 2) {
			timerClicked.stop();
			timerZaehler = 0;
		}

	}
	public static int getTimerZaehler() {
		return timerZaehler;
	}
	
	public void setIncrementMap() {
		zaehlerLobbyKeySelection = (zaehlerLobbyKeySelection + 1)%3;
	}
	public void setDecrementMap() {
		if (zaehlerLobbyKeySelection == 0) {
			zaehlerLobbyKeySelection = 2;
		}
		else {
			zaehlerLobbyKeySelection = (zaehlerLobbyKeySelection - 1)%3;	
		}
}
}
