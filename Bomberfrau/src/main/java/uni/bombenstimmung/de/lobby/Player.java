package uni.bombenstimmung.de.lobby;


import uni.bombenstimmung.de.backend.console.ConsoleHandler;
import uni.bombenstimmung.de.backend.console.MessageType;


public class Player {
	private String ip;
	private String name;
	static int idZaehler = 0;
	private int id;
	private int skin = 0;
	private boolean isHost;
	
	// Konstruktor mit IP
	public Player(String name, String ip) {
		this.ip = ip;
		this.name = name;
		id = idZaehler;
		idZaehler ++;
		isHost = false;
		
		ConsoleHandler.print("Created Player. ID: " + id + ", Name: " + name, MessageType.LOBBY);
	}
	// Konstruktor ohne IP. Also nur den namen // Hier handelt es sich um den Host
	public Player(String name) {
		this.name = name;
		id = idZaehler;
		idZaehler ++;
		isHost = true;
		ConsoleHandler.print("Created Player. ID: " + id + ", Name: " + name, MessageType.LOBBY);
		//Lobby_Create lobby = new Lobby_Create(nameOfPlayer);
		
	}
	
	public void setSkin(int skin) {
		this.skin = skin;
	}
	
    public int getSkin() {
    	return skin;
    }
    public String getName() {
    	return name;
    }
    public int getId() {
    	return id;
    }
    
    @Override public String toString() {
    	return "Player ID: " + id + " Name: " + name + " IP-Adress: " + ip;
    }
}