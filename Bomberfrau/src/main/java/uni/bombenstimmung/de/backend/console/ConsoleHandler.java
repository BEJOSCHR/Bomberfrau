/*
 * ConsoleHandler
 *
 * Version 1.0
 * Author: Benni
 *
 * Verwaltet alle Ein- und Augaben zur bzw von der Console
 */
package uni.bombenstimmung.de.backend.console;

import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

import uni.bombenstimmung.de.backend.graphics.GraphicsHandler;

public class ConsoleHandler {
	
	private static Timer inputScanner = null;
	private static MessageType currentDebugType = MessageType.NONESPECIFIC;
	
	/**
	 * Gibt die angegebene message via print(message, type) aus (MessageType = NONESPECIFIC)
	 * @param message - Die Message die ausgegeben werden soll
	 */
	public static void print(String message) {
		
		print(message, MessageType.NONESPECIFIC);
		
	}
	
	/**
	 * Gibt die angegebene message mit dem angegebenen type aus, wenn der Type dem derzeitigen debugType entspricht oder sich über diesen hinweg setzen kann. 
	 * So wird ERROR immer ausgegeben und IMPORTANT wird nur nicht ausgegeben wenn currentDebugType = ERROR ist
	 * @param message - Die Message die ausgegeben werden soll
	 * @param type - Der {@link MessageType} der Nachricht, der die zugehörigkeit der message defeniert
	 */
	public static void print(String message, MessageType type) {
		
		if(currentDebugType == MessageType.NONESPECIFIC) {
			//PRINT EVERYTHING
			System.out.println(type+": "+message);
		}else if(currentDebugType == MessageType.IMPORTANT) {
			//IMPORTANT + ERROR
			if(type == MessageType.IMPORTANT || type == MessageType.ERROR) {
				System.out.println(type+": "+message);
			}
		}else if(currentDebugType == MessageType.ERROR) {
			//ONLY ERROR
			if(type == MessageType.ERROR) {
				System.out.println(type+": "+message);
			}
		}else {
			//IF MATCHING TYPES
			if(type == currentDebugType) {
				System.out.println(type+": "+message);
			}
		}
		
	}
	
	/**
	 * Startet den Scanner der auf Eingaben in der Console wartet und diese weiterleitet.
	 * Wenn bereits ein Scanner gestartete wurde wird kein weiterer gestartet
	 */
	public static void startInputScanner() {
		
		if(inputScanner == null) {
			inputScanner = new Timer();
			inputScanner.scheduleAtFixedRate(new TimerTask() {
				@Override
				public void run() {
					
					@SuppressWarnings("resource") //DARF NICHT GESCHLOSSEN WERDEN!
					Scanner consoleInput = new Scanner(System.in);
					
					if(consoleInput.hasNextLine()) {
						String[] input = consoleInput.nextLine().split(" ");
						handleInput(input);
					}
					
				}
			}, 0, 60);
			ConsoleHandler.print("Started ConsoleInputScanner!", MessageType.BACKEND);
		}else {
			ConsoleHandler.print("Couldn't init/start Inputscanner, because there is already one running!", MessageType.IMPORTANT);
		}
		
	}
	
	/**
	 * Stoppt den Scanner der auf Eingaben in der Console wartet und diese weiterleitet, falls dieser läuft.
	 */
	public static void stopInputScanner() {
		
		if(inputScanner != null) {
			inputScanner.cancel();
			inputScanner = null;
			ConsoleHandler.print("Stopped ConsoleInputScanner!", MessageType.BACKEND);
		}else {
			ConsoleHandler.print("Couldn't stop Inputscanner, because there is no one running!", MessageType.IMPORTANT);
		}
		
	}
	
	/**
	 * Verarbeitet die Eingaben die der InputScanner ausgelesen hat
	 * @param input - Die Liste der einegegebenen Commandos (Aufgeteilt an " ")
	 */
	private static void handleInput(String[] input) {
		
		switch(input[0]) {
		case "exit":
			ConsoleHandler.print("Shutting down...", MessageType.IMPORTANT);
			GraphicsHandler.shutdownProgram();
			break;
		case "debugType":
			if(input.length >= 2) {
				try {
					MessageType newType = MessageType.valueOf(input[1]);
					switchDebugType(newType);
					ConsoleHandler.print("Switched DebugType to: "+currentDebugType, MessageType.IMPORTANT);
				}catch(IllegalArgumentException error) {
					String allDebugTypes = "";
					for(MessageType type : MessageType.values()) {
						allDebugTypes += ", "+type.toString();
					}
					allDebugTypes = allDebugTypes.substring(2);
					ConsoleHandler.print("Choose type from: "+allDebugTypes, MessageType.IMPORTANT);
				}
			}else {
				ConsoleHandler.print("Use: debugType [newType] - Current: "+currentDebugType, MessageType.IMPORTANT);
			}
			break;
		case "help":
		default:
			ConsoleHandler.print("Choose input: [help,exit,debugType]", MessageType.IMPORTANT);
			break;
		}
		
	}
	
	/**
	 * Ändert den derzeitigen DebugType, abhängig davon werden manche ausgeben vor der ausgabe blockiert
	 * @param newType - Der neue currentDebugType
	 * @see {@link MessageType}
	 */
	public static void switchDebugType(MessageType newType) {
		
		currentDebugType = newType;
		
	}
	
}
