/*
* Settings
*
* Version 1.0
* Author: Carsten
*
* Verwaltet die Einstellungen und die dazugeh�rige ini-Datei
*/
package uni.bombenstimmung.de.menu;

import java.io.File;
import java.io.*;
import java.util.*;

import javax.swing.JOptionPane;

import uni.bombenstimmung.de.backend.console.ConsoleHandler;
import uni.bombenstimmung.de.backend.console.MessageType;

import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;

public class Settings {

	private static final String INI = "save.ini";

        public static boolean show_fps;
        public static boolean create_selected;
        public static boolean boxes_main_valid;
        public static boolean ende;
        public static String user_name, language;
        public static int lang_nr;
        public static String ip;
        public static Properties prop;

        // resolution
        public static int res_nr;
        public static int res_width;
        public static int res_height;
        public static int res_width_max;
        public static int res_height_max;
        // volumes
        public static int vol_music;
        public static int vol_sound;
        // controls
        public static int move_left;
        public static int move_up;
        public static int move_right;
        public static int move_down;
        public static int plant_bomb;
        // special keys
        public static int pause;
        public static int cancel;

	public static Float factor;
        

        public static int setResolution(int i) {
            switch (i) {
                case 0:
                    res_width = res_width_max;
                    res_height  = res_height_max;
                    break;
                case 1:
                    res_width = 1280;
                    res_height  = 720;
                    break;
                case 2:
                    res_width = 1600;
                    res_height  = 900;
                    break;
                case 3:
                    res_width = 1920;
                    res_height  = 1080;
                    break;
                case 4:
                    res_width = 2560;
                    res_height  = 1440;
                    break;
                case 5:
                    res_width = 3840;
                    res_height  = 2160;
                    break;
            }
          
            if (res_width > res_width_max) {
                  res_width  = res_width_max;
                  res_height = res_height_max;
                  res_nr = 0;
                  if (lang_nr==0)
                      JOptionPane.showMessageDialog(null,"resolution to high - switching to lower one for fullscreen.","resolution", JOptionPane.INFORMATION_MESSAGE);
                  if (lang_nr==1)
                      JOptionPane.showMessageDialog(null,"Die Aufl�sung ist zu gro� - Wechsel zu Vollbild.","Aufl�sung", JOptionPane.INFORMATION_MESSAGE);
                  ConsoleHandler.print("resolution to high - switching to lower one for fullscreen", MessageType.MENU);
                  return 0;
            }
            else return i;
	}
        
    
	/**
	*  Gibt alle Werte aus der ini-Datei im Terminal aus
	*/
	public static void iniValuesToTerminal() {
        	  try{
                        //prop = new Properties();
                        prop.load(new FileInputStream(INI));
        	    	ConsoleHandler.print("<lang_nr>    = " + prop.getProperty("lang_nr"), MessageType.MENU);
        	    	ConsoleHandler.print("<res_nr>     = " + prop.getProperty("res_nr"), MessageType.MENU);
        	    	ConsoleHandler.print("<res_width>  = " + prop.getProperty("res_width"), MessageType.MENU);
        	    	ConsoleHandler.print("<res_height> = " + prop.getProperty("res_height"), MessageType.MENU);
        	    	ConsoleHandler.print("<user_name>  = " + prop.getProperty("user_name"), MessageType.MENU);
        	    	ConsoleHandler.print("<vol_music>  = " + prop.getProperty("vol_music"), MessageType.MENU);
        	    	ConsoleHandler.print("<vol_sound>  = " + prop.getProperty("vol_sound"), MessageType.MENU);
        	    	ConsoleHandler.print("<move_left>  = code " + prop.getProperty("move_left"), MessageType.MENU);
        	    	ConsoleHandler.print("<move_up>    = code " + prop.getProperty("move_up"), MessageType.MENU);
        	    	ConsoleHandler.print("<move_right> = code " + prop.getProperty("move_right"), MessageType.MENU);
        	    	ConsoleHandler.print("<move_down>  = code " + prop.getProperty("move_down"), MessageType.MENU);
        	    	ConsoleHandler.print("<plant_bomb> = code " + prop.getProperty("plant_bomb"), MessageType.MENU);
        	    	ConsoleHandler.print("<pause>      = code " + prop.getProperty("pause"), MessageType.MENU);
        	    	ConsoleHandler.print("<cancel>     = code " + prop.getProperty("cancel"), MessageType.MENU);
        	    	ConsoleHandler.print("<ip_address> = " + prop.getProperty("ip_address"), MessageType.MENU);
        	    	ConsoleHandler.print("<show_fps>   = " + prop.getProperty("show_fps"), MessageType.MENU);
	          }
	          catch( IOException ex ){
        	        ConsoleHandler.print("catched: " + ex, MessageType.MENU);
        	        ConsoleHandler.print("message: error in reading " + INI + "\n" + ex.getMessage(), MessageType.MENU);
        	        ex.printStackTrace();
	          }
	}


	/**
	*  Speichert die ini-Datei mit den aktuellen Werten 
	*/
        public static void saveIni() {
              try{
                    //prop = new Properties();
                    prop.load(new FileInputStream(INI));
            
                    prop.setProperty("lang_nr", String.valueOf(lang_nr));
                    prop.setProperty("res_nr", String.valueOf(res_nr));
                    prop.setProperty("res_width", String.valueOf(res_width));
                    prop.setProperty("res_height", String.valueOf(res_height));
                    prop.setProperty("user_name", user_name);
                    prop.setProperty("vol_music", String.valueOf(vol_music));
                    prop.setProperty("vol_sound", String.valueOf(vol_sound));
                    prop.setProperty("move_left", String.valueOf(move_left));
                    prop.setProperty("move_up", String.valueOf(move_up));
                    prop.setProperty("move_right", String.valueOf(move_right));
                    prop.setProperty("move_down", String.valueOf(move_down));
                    prop.setProperty("plant_bomb", String.valueOf(plant_bomb));
                    prop.setProperty("pause", String.valueOf(pause));
                    prop.setProperty("cancel", String.valueOf(cancel));
                    prop.setProperty("ip_address", ip);
                    prop.setProperty("show_fps", String.valueOf(show_fps));
                    
                    // saving current settings
                    prop.store(new FileOutputStream(INI)," Bomberfrau Settings");
                    ConsoleHandler.print(INI + " has been saved", MessageType.MENU);
                  }
                  catch( IOException ex ){
                    ConsoleHandler.print("catched: " + ex, MessageType.MENU);
                    ConsoleHandler.print("message: error in saving " + INI + "\n" + ex.getMessage(), MessageType.MENU);
                    ex.printStackTrace();
              }
        }
    
    

	/**
	*  Pr�ft anfangs die Existenz der ini-Datei.
	*  Ist sie vorhanden, werden die Werte ausgelesen.
	*  Falls nicht, wird eine Datei mit Standard-Werten erstellt. 
	*/
        public static void initIni() {
            

              // checking current used monitor resolution (Windows 10 only ???)
              // isn't really used because needed value gives Toolkit, see next section
              GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
              GraphicsDevice gd = env.getDefaultScreenDevice();
              DisplayMode dm = gd.getDisplayMode();
              res_width_max = dm.getWidth();
              res_height_max = dm.getHeight();
              ConsoleHandler.print("current resolution: " + res_width_max + " x " + res_height_max, MessageType.MENU);
    
              // checking current resolution (used by Swing ???) via Toolkit
              res_width_max = Toolkit.getDefaultToolkit().getScreenSize().width;
              res_height_max = Toolkit.getDefaultToolkit().getScreenSize().height;
              ConsoleHandler.print("Toolkit resolution: " + res_width_max + " x " + res_height_max, MessageType.MENU); 
     
              try {
                    File save_ini = new File(INI);
                    if (save_ini.createNewFile())
                        ConsoleHandler.print("New " + INI + " has been created.", MessageType.MENU);
                    else
                        ConsoleHandler.print(INI + " already exists.", MessageType.MENU);
              }
              catch (Exception e) {
                    ConsoleHandler.print("catched: " + e, MessageType.MENU);
                    ConsoleHandler.print("message: " + e.getMessage(), MessageType.MENU);
                    e.printStackTrace();
              }
            
              try{
            	    prop = new Properties();
                    prop.load(new FileInputStream(INI));

                    // checking if all values exist
                    // otherwise setting them to standard values
                    if (prop.getProperty("lang_nr")==null) {
                      ConsoleHandler.print("<lang_nr> is empty ... setting value 0", MessageType.MENU);
                      prop.setProperty("lang_nr", "0");
                    }
                    lang_nr = Integer.parseInt(prop.getProperty("lang_nr"));
                
                    if (prop.getProperty("res_nr")==null) {
                      ConsoleHandler.print("<res_nr> is empty ... setting value 0", MessageType.MENU);
                      prop.setProperty("res_nr", "0");
                    }
                    res_nr = Integer.parseInt(prop.getProperty("res_nr"));
                
                    if (prop.getProperty("res_width")==null) {
                      ConsoleHandler.print("<res_width> is empty ... setting value to fit fullscreen", MessageType.MENU);
                      //prop.setProperty("res_width", "1920");
                      prop.setProperty("res_width", String.valueOf(res_width_max));
                    }
                    res_width = Integer.parseInt(prop.getProperty("res_width"));
                
                    if (prop.getProperty("res_height")==null) {
                      ConsoleHandler.print("<res_height> is empty ... setting value to fit fullscreen", MessageType.MENU);
                      //prop.setProperty("res_height", "1080");
                      prop.setProperty("res_height", String.valueOf(res_height_max));
                    }
                    res_height = Integer.parseInt(prop.getProperty("res_height"));
                
                    if (prop.getProperty("user_name")==null) {
                      ConsoleHandler.print("<user_name> is empty ... setting value ?", MessageType.MENU);
                      prop.setProperty("user_name", "?");
                    }
                    user_name = prop.getProperty("user_name");
                
                    if (prop.getProperty("vol_music")==null) {
                      ConsoleHandler.print("<vol_music> is empty ... setting value 50", MessageType.MENU);
                      prop.setProperty("vol_music", "50");
                    }
                    vol_music = Integer.parseInt(prop.getProperty("vol_music"));
                    
                    if (prop.getProperty("vol_sound")==null) {
                      ConsoleHandler.print("<vol_sound> is empty ... setting value 50", MessageType.MENU);
                      prop.setProperty("vol_sound", "50");
                    }
                    vol_sound = Integer.parseInt(prop.getProperty("vol_sound"));
                    
                    if (prop.getProperty("move_left")==null) {
                      ConsoleHandler.print("<move_left> is empty ... setting value 37 (Code for Left)", MessageType.MENU);
                      prop.setProperty("move_left", "37");
                    }
                    move_left = Integer.parseInt(prop.getProperty("move_left"));
                    
                    if (prop.getProperty("move_up")==null) {
                      ConsoleHandler.print("<move_up> is empty ... setting value 38 (Code for Up)", MessageType.MENU);
                      prop.setProperty("move_up", "38");
                    }
                    move_up = Integer.parseInt(prop.getProperty("move_up"));
                    
                    if (prop.getProperty("move_right")==null) {
                      ConsoleHandler.print("<move_right> is empty ... setting value 39 (Code for Right)", MessageType.MENU);
                      prop.setProperty("move_right", "39");
                    }
                    move_right = Integer.parseInt(prop.getProperty("move_right"));
                    
                    if (prop.getProperty("move_down")==null) {
                      ConsoleHandler.print("<move_down> is empty ... setting value 40 (Code for Down)", MessageType.MENU);
                      prop.setProperty("move_down", "40");
                    }
                    move_down = Integer.parseInt(prop.getProperty("move_down"));
                    
                    if (prop.getProperty("plant_bomb")==null) {
                      ConsoleHandler.print("<plant_bomb> is empty ... setting value 32 (Code for Space)", MessageType.MENU);
                      prop.setProperty("plant_bomb", "32");
                    }
                    plant_bomb = Integer.parseInt(prop.getProperty("plant_bomb"));
                    
                    if (prop.getProperty("pause")==null) {
                      ConsoleHandler.print("<pause> is empty ... setting value 80 (Code for p)", MessageType.MENU);
                      prop.setProperty("pause", "80");
                    }
                    pause = Integer.parseInt(prop.getProperty("pause"));
                    
                    if (prop.getProperty("cancel")==null) {
                      ConsoleHandler.print("<cancel> is empty ... setting value 27 (Code for ESC)", MessageType.MENU);
                      prop.setProperty("cancel", "27");
                    }
                    cancel = Integer.parseInt(prop.getProperty("cancel"));
                
                    if (prop.getProperty("ip_address")==null) {
                      ConsoleHandler.print("<ip_address> is empty ... setting value 0.0.0.0", MessageType.MENU);
                      prop.setProperty("ip_address", "0.0.0.0");
                    }
                    ip = prop.getProperty("ip_address");
                
                    if (prop.getProperty("show_fps")==null) {
                      ConsoleHandler.print("<show_fps> is empty ... setting value false", MessageType.MENU);
                      prop.setProperty("show_fps", "false");
                    }
                    show_fps = Boolean.valueOf(prop.getProperty("show_fps"));
                    
                    
                    // saving current settings
                    saveIni();
              }
              catch( IOException ex ){
        	    ConsoleHandler.print("catched: " + ex, MessageType.MENU);
        	    ConsoleHandler.print("message: " + ex.getMessage(), MessageType.MENU);
                    ex.printStackTrace();
              }
              factor = (float)(Settings.res_height)/Settings.res_height_max;
        }

    
}