/*
 * Menu
 *
 * Version 1.0
 * Author: Carsten
 *
 * Das Haupt- und Optionsmen� des Spieles
 */

package uni.bombenstimmung.de.menu;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;
import java.util.regex.*; // for PATTERN (IP address)

import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import uni.bombenstimmung.de.backend.animation.Animation;
import uni.bombenstimmung.de.backend.animation.AnimationData;
import uni.bombenstimmung.de.backend.console.ConsoleHandler;
import uni.bombenstimmung.de.backend.console.MessageType;
import uni.bombenstimmung.de.backend.graphics.DisplayType;
import uni.bombenstimmung.de.backend.graphics.GraphicsHandler;
import uni.bombenstimmung.de.backend.language.LanguageBlockType;
import uni.bombenstimmung.de.backend.language.LanguageHandler;
import uni.bombenstimmung.de.backend.language.LanguageType;
import uni.bombenstimmung.de.backend.maa.MouseActionArea;
import uni.bombenstimmung.de.backend.maa.MouseActionAreaType;
import uni.bombenstimmung.de.backend.sounds.SoundCategory;
import uni.bombenstimmung.de.backend.sounds.SoundHandler;
import uni.bombenstimmung.de.backend.sounds.SoundType;

public class Menu {

    public static final int MAX_NAME_LENGTH = 30;
    private static JRadioButton create, join;
    private static JLabel name_info, ip_info;
    private static JTextField name_box, ip_box;
    private static JComboBox<String> comboboxReso, comboboxLang;
    private static JSlider sliderMusic, sliderSound;
    private static JTextField control_up, control_down, control_left, control_right, control_bomb;
    private static JCheckBox checkBoxFPS;
    private static MouseActionArea intro, start, options, exit, back;
    private static Boolean grow = true;

    /**
     * correct pattern for IPv4 address - 4 times 0-255 without leading zeros.
     */
    private static final Pattern IPV4_PATTERN = Pattern
	    .compile("^(([0-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5])(\\.(?!$)|$)){4}$");
    // "^(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");

    /**
     * Erstellt Swingkomponenten f�r das Hauptmen�
     */
    public static void buildMenu() {

	create = new JRadioButton();
	create.setText(" " + LanguageHandler.getLLB(LanguageBlockType.LB_MENU_TXT1).getContent());
	create.setBounds((int) (Settings.res_width * 0.32), (int) (Settings.res_height * 0.485),
		(int) (Settings.res_width / 10), (int) (Settings.res_height / 20));
	create.setBackground(Color.WHITE);
	create.setFont(GraphicsHandler.usedFont(30));
	create.setFocusable(false);
	if (Settings.create_selected)
	    create.setSelected(true);

	create.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		ConsoleHandler.print("remove ip box", MessageType.MENU);
		Settings.create_selected = true;
		// GraphicsHandler.getLabel().remove(ip_box);
		ip_box.setVisible(false);
		ip_info.setVisible(false);
	    }
	});

	join = new JRadioButton();
	join.setText(" " + LanguageHandler.getLLB(LanguageBlockType.LB_MENU_TXT2).getContent());
	join.setBounds((int) (Settings.res_width * 0.32), (int) (Settings.res_height * 0.528),
		(int) (Settings.res_width / 10), (int) (Settings.res_height / 20));
	join.setBackground(Color.WHITE);
	join.setFont(GraphicsHandler.usedFont(30));
	join.setFocusable(false);
	if (!Settings.create_selected)
	    join.setSelected(true);

	join.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		ConsoleHandler.print("add ip box", MessageType.MENU);
		Settings.create_selected = false;
		// GraphicsHandler.getLabel().add(ip_box);
		ip_box.setVisible(true);
		ip_info.setVisible(true);
		// repaint();
	    }
	});

	ButtonGroup btngroup = new ButtonGroup();
	btngroup.add(create);
	btngroup.add(join);

	name_info = new JLabel("");
	name_info.setBackground(Color.WHITE);
	name_info.setForeground(Color.RED);
	name_info.setFont(GraphicsHandler.usedFont(20));
	name_info.setHorizontalAlignment(JLabel.CENTER);
	name_info.setBounds((int) (Settings.res_width * 0.52), (int) (Settings.res_height * 0.37),
		(int) (Settings.res_width / 5), (int) (Settings.res_height / 12));

	name_box = new JTextField(Settings.user_name);
	name_box.setHorizontalAlignment(JTextField.CENTER);
	name_box.setFont(
		GraphicsHandler.usedFont(40).deriveFont(40f * (1f - 0.022f * (name_box.getText().length() - 8))));
	name_box.setBounds((int) (Settings.res_width * 0.52), (int) (Settings.res_height * 0.44),
		(int) (Settings.res_width / 5), (int) (Settings.res_height / 12));
	name_box.addKeyListener(new KeyListener() {
	    public void keyPressed(KeyEvent e) {
	    };

	    public void keyTyped(KeyEvent e) {
	    };

	    public void keyReleased(KeyEvent e) {
		String txt = name_box.getText();
		// System.out.println("name_box text length = " + name_box.getText().length());
		if (!txt.isEmpty()) {
		    name_info.setText("");
		    name_info.repaint();
		}
		if (txt.length() > MAX_NAME_LENGTH) {
		    // name_box.setText(txt.substring(0, MAX_NAME_LENGTH));
		    name_box.setText(Settings.user_name);
		    name_info.setText(LanguageHandler.getLLB(LanguageBlockType.LB_MENU_INFO1).getContent());
		    name_info.repaint();
		} else {
		    Settings.user_name = txt;
		    name_box.setFont(GraphicsHandler.usedFont(40)
			    .deriveFont(44f * Settings.factor * (1f - 0.022f * (txt.length() - 7))));
		}
	    }
	});

	// JTextField ip_box = new JTextField("0.0.0.0");
	// ip_box = new JTextField(prop.getProperty("ip_address"));
	ip_box = new JTextField(Settings.ip);
	ip_box.setHorizontalAlignment(JTextField.CENTER);
	ip_box.setFont(GraphicsHandler.usedFont((int) (40 * Settings.factor)));
	if (Settings.create_selected)
	    ip_box.setVisible(false);
	else
	    ip_box.setVisible(true);
	ip_box.setBounds((int) (Settings.res_width * 0.52), (int) (Settings.res_height * 0.54),
		(int) (Settings.res_width / 5), (int) (Settings.res_height / 12));
	ip_box.addKeyListener(new KeyListener() {
	    public void keyPressed(KeyEvent e) {
	    };

	    public void keyTyped(KeyEvent e) {
	    };

	    public void keyReleased(KeyEvent e) {
		String txt = ip_box.getText();
		if (!txt.isEmpty()) {
		    ip_info.setText("");
		    ip_info.repaint();
		}
		Settings.ip = txt;
		if (!checkIp(false)) {
		    ip_info.setText(LanguageHandler.getLLB(LanguageBlockType.LB_MENU_INFO2).getContent());
		    ip_info.repaint();
		}

	    }
	});

	ip_info = new JLabel("");
	ip_info.setHorizontalAlignment(JLabel.CENTER);
	if (Settings.create_selected)
	    ip_info.setVisible(false);
	if (!checkIp(false))
	    ip_info.setText(LanguageHandler.getLLB(LanguageBlockType.LB_MENU_INFO2).getContent());
	ip_info.setBackground(Color.WHITE);
	ip_info.setForeground(Color.RED);
	ip_info.setFont(GraphicsHandler.usedFont(20));
	ip_info.setBounds((int) (Settings.res_width * 0.52), (int) (Settings.res_height * 0.6),
		(int) (Settings.res_width / 5), (int) (Settings.res_height / 12));

	GraphicsHandler.getLabel().add(create);
	GraphicsHandler.getLabel().add(join);
	GraphicsHandler.getLabel().add(name_box);
	GraphicsHandler.getLabel().add(name_info);
	GraphicsHandler.getLabel().add(ip_box);
	GraphicsHandler.getLabel().add(ip_info);
    }

    /**
     * Erstellt Swingkomponenten f�r das Optionsmen�
     */
    public static void buildOptions() {

	// Der "factor" passt alle Gr�ssen und Positionen der Aufl�sung an
	Settings.setResolution(Settings.res_nr);
	Settings.factor = (float) (Settings.res_height) / Settings.res_height_max;

	String[] res = { " " + LanguageHandler.getLLB(LanguageBlockType.LB_OPT_FULLSCREEN).getContent(),
		" HD    1280 x 720", " WSXGA 1600 x 900", " FHD   1920 x 1080", " WQHD  2560 x 1440",
		" UHD   3840 x 2160" };

	comboboxReso = new JComboBox<>(res);
	comboboxReso.setBackground(Color.WHITE);
	comboboxReso.setFont(new Font("Consolas", Font.BOLD, (int) (40 * Settings.factor)));
	comboboxReso.setBounds((int) (Settings.res_width * 0.28), (int) (Settings.res_height * 0.1),
		(int) (Settings.res_width / 4.5), (int) (Settings.res_height / 18));
	comboboxReso.setSelectedItem(res[Settings.res_nr]);

	comboboxReso.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		int t = (int) comboboxReso.getSelectedIndex();
		t = Settings.setResolution(t);
		Settings.factor = (float) (Settings.res_height) / Settings.res_height_max;

		GraphicsHandler.getFrame().setSize(Settings.res_width, Settings.res_height);
		GraphicsHandler.getFrame().setLocation((Settings.res_width_max - Settings.res_width) / 2,
			(Settings.res_height_max - Settings.res_height) / 2);
		comboboxReso.setFont(new Font("Consolas", Font.BOLD, (int) (40 * Settings.factor)));
		comboboxReso.setBounds((int) (Settings.res_width * 0.28), (int) (Settings.res_height * 0.1),
			(int) (Settings.res_width / 4.5), (int) (Settings.res_height / 18));
		sliderMusic.setBounds((int) (Settings.res_width * 0.28), (int) (Settings.res_height * 0.2),
			(int) (Settings.res_width / 4.5), (int) (Settings.res_height / 12));
		sliderSound.setBounds((int) (Settings.res_width * 0.28), (int) (Settings.res_height * 0.3),
			(int) (Settings.res_width / 4.5), (int) (Settings.res_height / 12));

		control_up.setFont(
			GraphicsHandler.usedFont((int) ((46 - 3 * control_up.getText().length()) * Settings.factor)));
		control_down.setFont(
			GraphicsHandler.usedFont((int) ((46 - 3 * control_down.getText().length()) * Settings.factor)));
		control_left.setFont(
			GraphicsHandler.usedFont((int) ((46 - 3 * control_left.getText().length()) * Settings.factor)));
		control_right.setFont(GraphicsHandler
			.usedFont((int) ((46 - 3 * control_right.getText().length()) * Settings.factor)));
		control_bomb.setFont(
			GraphicsHandler.usedFont((int) ((46 - 3 * control_bomb.getText().length()) * Settings.factor)));

		control_up.setBounds((int) (Settings.res_width * 0.33), (int) (Settings.res_height * 0.44),
			(int) (Settings.res_width / 12), (int) (Settings.res_height / 16));
		control_down.setBounds((int) (Settings.res_width * 0.33), (int) (Settings.res_height * 0.64),
			(int) (Settings.res_width / 12), (int) (Settings.res_height / 16));
		control_left.setBounds((int) (Settings.res_width * 0.28), (int) (Settings.res_height * 0.54),
			(int) (Settings.res_width / 12), (int) (Settings.res_height / 16));
		control_right.setBounds((int) (Settings.res_width * 0.38), (int) (Settings.res_height * 0.54),
			(int) (Settings.res_width / 12), (int) (Settings.res_height / 16));
		control_bomb.setBounds((int) (Settings.res_width * 0.5), (int) (Settings.res_height * 0.54),
			(int) (Settings.res_width / 12), (int) (Settings.res_height / 16));
		checkBoxFPS.setFont(GraphicsHandler.usedFont((int) (30f * Settings.factor)));
		checkBoxFPS.setBounds((int) (Settings.res_width * 0.1), (int) (Settings.res_height * 0.7),
			(int) (Settings.res_width / 5), (int) (Settings.res_height / 12));
		comboboxLang.setFont(new Font("Consolas", Font.BOLD, (int) (40 * Settings.factor)));
		comboboxLang.setBounds((int) (Settings.res_width * 0.28), (int) (Settings.res_height * 0.81),
			(int) (Settings.res_width / 7), (int) (Settings.res_height / 18));
		start.remove();
		options.remove();
		exit.remove();
		back.remove();
		initMaaMainmenu();
		initMaaOptions();
		GraphicsHandler.getLabel().repaint();

		comboboxReso.setSelectedItem(res[t]);
		Settings.res_nr = t;
	    }
	});

	sliderMusic = new JSlider(JSlider.HORIZONTAL, 0, 100, Settings.vol_music);
	sliderMusic.setBackground(Color.WHITE);
	sliderMusic.setBounds((int) (Settings.res_width * 0.28), (int) (Settings.res_height * 0.2),
		(int) (Settings.res_width / 4.5), (int) (Settings.res_height / 12));
	sliderMusic.setMinorTickSpacing(5);
	sliderMusic.setMajorTickSpacing(10);
	sliderMusic.setPaintTicks(true);
	sliderMusic.setPaintLabels(true);
	sliderMusic.setSnapToTicks(true);
	sliderMusic.addChangeListener(new ChangeListener() {
	    public void stateChanged(ChangeEvent event) {
		Settings.vol_music = sliderMusic.getValue();
		System.out.println("Music Volume = " + Settings.vol_music);
		FloatControl volume = (FloatControl) SoundHandler.clip.getControl(FloatControl.Type.MASTER_GAIN);
		// float vol = (Settings.vol_music-50)/2f;
		System.out.println("Music Volume2 = " + Settings.vol_music);
		float vol = (31f * (Settings.vol_music / 100f) - 25f);
		System.out.println("Music Volume3 = " + vol);
		volume.setValue(vol);
	    }
	});

	sliderSound = new JSlider(JSlider.HORIZONTAL, 0, 100, Settings.vol_sound);
	sliderSound.setBackground(Color.WHITE);
	sliderSound.setBounds((int) (Settings.res_width * 0.28), (int) (Settings.res_height * 0.3),
		(int) (Settings.res_width / 4.5), (int) (Settings.res_height / 12));
	sliderSound.setMinorTickSpacing(5);
	sliderSound.setMajorTickSpacing(10);
	sliderSound.setPaintTicks(true);
	sliderSound.setPaintLabels(true);
	sliderSound.setSnapToTicks(true);
	sliderSound.addChangeListener(new ChangeListener() {
	    public void stateChanged(ChangeEvent event) {
		Settings.vol_sound = sliderSound.getValue();
		ConsoleHandler.print("Settings.vol_sound-88)/2 = " + (Settings.vol_sound - 88) / 2D, MessageType.MENU);
		SoundHandler.playSound(SoundType.OPTIONS, false);
		FloatControl volume = (FloatControl) SoundHandler.getSound(SoundType.OPTIONS).getClip()
			.getControl(FloatControl.Type.MASTER_GAIN);
		float vol = (31f * (Settings.vol_sound / 100f) - 25f);
		volume.setValue(vol);
		System.out.println("Sound Volume = " + Settings.vol_sound);
	    }
	});

	control_up = new JTextField();
	control_up.setHorizontalAlignment(JTextField.CENTER);
	control_up.setText(codeToText(Settings.move_up));
	control_up
		.setFont(GraphicsHandler.usedFont((int) ((46 - 3 * control_up.getText().length()) * Settings.factor)));
	control_up.setBounds((int) (Settings.res_width * 0.33), (int) (Settings.res_height * 0.44),
		(int) (Settings.res_width / 12), (int) (Settings.res_height / 16));
	control_up.addKeyListener(new KeyListener() {
	    public void keyReleased(KeyEvent e) {
		// System.out.println("control_up: code = " + e.getKeyCode() + " ,
		// e.getKeyChar() = " + e.getKeyChar() + " , int = " + (int)(e.getKeyChar()));
		// System.out.println("control_up: code ext = " + e.getExtendedKeyCode());
		// System.out.println("getKeyText = " + KeyEvent.getKeyText(e.getKeyCode()) + "
		// , getExtendedKeyCodeForChar = " +
		// KeyEvent.getExtendedKeyCodeForChar(e.getKeyCode()) );
		Settings.move_up = e.getKeyCode();
		control_up.setText(codeToText(Settings.move_up));
		control_up.setFont(
			GraphicsHandler.usedFont((int) ((46 - 3 * control_up.getText().length()) * Settings.factor)));
	    };

	    public void keyTyped(KeyEvent e) {
		control_up.setText("");
	    };

	    public void keyPressed(KeyEvent e) {
	    };
	});

	control_down = new JTextField();
	control_down.setHorizontalAlignment(JTextField.CENTER);
	control_down.setText(codeToText(Settings.move_down));
	control_down.setFont(
		GraphicsHandler.usedFont((int) ((46 - 3 * control_down.getText().length()) * Settings.factor)));
	control_down.setBounds((int) (Settings.res_width * 0.33), (int) (Settings.res_height * 0.64),
		(int) (Settings.res_width / 12), (int) (Settings.res_height / 16));
	control_down.addKeyListener(new KeyListener() {
	    public void keyReleased(KeyEvent e) {
		Settings.move_down = e.getKeyCode();
		control_down.setText(codeToText(e.getKeyCode()));
		control_down.setFont(
			GraphicsHandler.usedFont((int) ((46 - 3 * control_down.getText().length()) * Settings.factor)));
	    };

	    public void keyTyped(KeyEvent e) {
		control_down.setText("");
	    };

	    public void keyPressed(KeyEvent e) {
	    };
	});

	control_left = new JTextField();
	control_left.setHorizontalAlignment(JTextField.CENTER);
	control_left.setText(codeToText(Settings.move_left));
	control_left.setFont(
		GraphicsHandler.usedFont((int) ((46 - 3 * control_left.getText().length()) * Settings.factor)));
	control_left.setBounds((int) (Settings.res_width * 0.28), (int) (Settings.res_height * 0.54),
		(int) (Settings.res_width / 12), (int) (Settings.res_height / 16));
	control_left.addKeyListener(new KeyListener() {
	    public void keyReleased(KeyEvent e) {
		Settings.move_left = e.getKeyCode();
		control_left.setText(codeToText(e.getKeyCode()));
		control_left.setFont(
			GraphicsHandler.usedFont((int) ((46 - 3 * control_left.getText().length()) * Settings.factor)));
	    };

	    public void keyTyped(KeyEvent e) {
		control_left.setText("");
	    };

	    public void keyPressed(KeyEvent e) {
	    };
	});

	control_right = new JTextField();
	control_right.setHorizontalAlignment(JTextField.CENTER);
	control_right.setText(codeToText(Settings.move_right));
	control_right.setFont(
		GraphicsHandler.usedFont((int) ((46 - 3 * control_right.getText().length()) * Settings.factor)));
	control_right.setBounds((int) (Settings.res_width * 0.38), (int) (Settings.res_height * 0.54),
		(int) (Settings.res_width / 12), (int) (Settings.res_height / 16));
	control_right.addKeyListener(new KeyListener() {
	    public void keyReleased(KeyEvent e) {
		Settings.move_right = e.getKeyCode();
		control_right.setText(codeToText(e.getKeyCode()));
		control_right.setFont(GraphicsHandler
			.usedFont((int) ((46 - 3 * control_right.getText().length()) * Settings.factor)));
	    };

	    public void keyTyped(KeyEvent e) {
		control_right.setText("");
	    };

	    public void keyPressed(KeyEvent e) {
	    };
	});

	control_bomb = new JTextField();
	control_bomb.setHorizontalAlignment(JTextField.CENTER);
	control_bomb.setText(codeToText(Settings.plant_bomb));
	control_bomb.setFont(
		GraphicsHandler.usedFont((int) ((46 - 3 * control_bomb.getText().length()) * Settings.factor)));
	control_bomb.setBounds((int) (Settings.res_width * 0.5), (int) (Settings.res_height * 0.54),
		(int) (Settings.res_width / 12), (int) (Settings.res_height / 16));
	control_bomb.addKeyListener(new KeyListener() {
	    public void keyReleased(KeyEvent e) {
		Settings.plant_bomb = e.getKeyCode();
		control_bomb.setText(codeToText(e.getKeyCode()));
		control_bomb.setFont(
			GraphicsHandler.usedFont((int) ((46 - 3 * control_bomb.getText().length()) * Settings.factor)));
	    };

	    public void keyTyped(KeyEvent e) {
		control_bomb.setText("");
	    };

	    public void keyPressed(KeyEvent e) {
	    };
	});

	checkBoxFPS = new JCheckBox();
	checkBoxFPS.setText(LanguageHandler.getLLB(LanguageBlockType.LB_OPT_TXT6).getContent() + "  ");
	checkBoxFPS.setFont(GraphicsHandler.usedFont((int) (30f * Settings.factor)));
	checkBoxFPS.setHorizontalTextPosition(SwingConstants.LEFT);
	checkBoxFPS.setBackground(Color.WHITE);
	checkBoxFPS.setForeground(Color.RED);
	checkBoxFPS.setFocusable(false);
	checkBoxFPS.setBounds((int) (Settings.res_width * 0.1), (int) (Settings.res_height * 0.7),
		(int) (Settings.res_width / 5), (int) (Settings.res_height / 12));
	checkBoxFPS.setSelected(Settings.show_fps);
	checkBoxFPS.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		// System.out.println("Checkbox=" + checkBoxFPS.isSelected());
		Settings.show_fps = checkBoxFPS.isSelected();
		if (Settings.show_fps)
		    GraphicsHandler.getLabel().setShowFPS(true);
		else
		    GraphicsHandler.getLabel().setShowFPS(false);
	    }
	});

	String[] lang = { " English", " Deutsch" };
	comboboxLang = new JComboBox<>(lang);
	comboboxLang.setFont(new Font("Consolas", Font.BOLD, (int) (40 * Settings.factor)));
	comboboxLang.setSelectedItem(lang[Settings.lang_nr]);
	comboboxLang.setBackground(Color.WHITE);
	comboboxLang.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		Settings.lang_nr = comboboxLang.getSelectedIndex();
		if (Settings.lang_nr == 0)
		    LanguageHandler.setActiveLanguage(LanguageType.ENGLISH);
		if (Settings.lang_nr == 1)
		    LanguageHandler.setActiveLanguage(LanguageType.GERMAN);

		comboboxReso.insertItemAt(
			" " + LanguageHandler.getLLB(LanguageBlockType.LB_OPT_FULLSCREEN).getContent(), 0);
		comboboxReso.removeItemAt(1);
		if (Settings.res_nr == 0)
		    comboboxReso.setSelectedIndex(0);
		control_up.setText(codeToText(Settings.move_up));
		control_down.setText(codeToText(Settings.move_down));
		control_left.setText(codeToText(Settings.move_left));
		control_right.setText(codeToText(Settings.move_right));
		control_bomb.setText(codeToText(Settings.plant_bomb));
		checkBoxFPS.setText(LanguageHandler.getLLB(LanguageBlockType.LB_OPT_TXT6).getContent() + "  ");
		start.remove();
		options.remove();
		exit.remove();
		back.remove();
		initMaaMainmenu();
		initMaaOptions();
		GraphicsHandler.getLabel().repaint();
	    }
	});
	comboboxLang.setBounds((int) (Settings.res_width * 0.28), (int) (Settings.res_height * 0.81),
		(int) (Settings.res_width / 7), (int) (Settings.res_height / 18));

    }

    /**
     * Setzt oder entfernt die Swing Komponenten des Hauptmen�s
     */
    public static void menuComponentsActive(Boolean bool) {
	if (bool) {
	    GraphicsHandler.getLabel().add(create);
	    GraphicsHandler.getLabel().add(join);
	    GraphicsHandler.getLabel().add(name_box);
	    // name_info.setText("");
	    GraphicsHandler.getLabel().add(name_info);
	    if (!Settings.create_selected) {
		GraphicsHandler.getLabel().add(ip_box);
		GraphicsHandler.getLabel().add(ip_info);
	    }

	} else {
	    GraphicsHandler.getLabel().remove(create);
	    GraphicsHandler.getLabel().remove(join);
	    GraphicsHandler.getLabel().remove(name_box);
	    GraphicsHandler.getLabel().remove(name_info);
	    if (!Settings.create_selected) {
		GraphicsHandler.getLabel().remove(ip_box);
		GraphicsHandler.getLabel().remove(ip_info);
	    }
	}

    }

    /**
     * Setzt oder entfernt die Swing Komponenten des Optionsmen�s
     */
    public static void optionsComponentsActive(Boolean bool) {
	if (bool) {
	    GraphicsHandler.getLabel().add(comboboxReso);
	    GraphicsHandler.getLabel().add(sliderMusic);
	    GraphicsHandler.getLabel().add(sliderSound);
	    GraphicsHandler.getLabel().add(control_up);
	    GraphicsHandler.getLabel().add(control_down);
	    GraphicsHandler.getLabel().add(control_left);
	    GraphicsHandler.getLabel().add(control_right);
	    GraphicsHandler.getLabel().add(control_bomb);
	    GraphicsHandler.getLabel().add(checkBoxFPS);
	    GraphicsHandler.getLabel().add(comboboxLang);
	} else {
	    GraphicsHandler.getLabel().remove(comboboxReso);
	    GraphicsHandler.getLabel().remove(sliderMusic);
	    GraphicsHandler.getLabel().remove(sliderSound);
	    GraphicsHandler.getLabel().remove(control_up);
	    GraphicsHandler.getLabel().remove(control_down);
	    GraphicsHandler.getLabel().remove(control_left);
	    GraphicsHandler.getLabel().remove(control_right);
	    GraphicsHandler.getLabel().remove(control_bomb);
	    GraphicsHandler.getLabel().remove(checkBoxFPS);
	    GraphicsHandler.getLabel().remove(comboboxLang);
	}
    }

    /*****************************************************************************************************************
     * INIT MAA MEN�
     *****************************************************************************************************************/

    /**
     * MAA des Intros - Vollbild Klick zum �berspringen
     */
    public static void initMaaIntro() {

	intro = new MouseActionArea(-1, -1, Settings.res_width + 1, Settings.res_height + 1,
		MouseActionAreaType.MAA_INTRO, "", 1, Color.WHITE, Color.WHITE) {
	    @Override
	    public void performAction_LEFT_RELEASE() {
		intro.remove();
		GraphicsHandler.switchToMenuFromIntro();
	    }

	    @Override
	    public boolean isActiv() {
		return GraphicsHandler.getDisplayType() == DisplayType.INTRO;
	    }
	};
    }

    /**
     * Kn�pfe/MAAs des Hauptmen�s: - Start: �bergang in die Lobby nach �berpr�fung
     * von Namen und IP Adresse - Options: �bergang zu den Optionen - Exit: Beenden
     * des Spieles
     */
    public static void initMaaMainmenu() {

	start = new MouseActionArea((int) (Settings.res_width * 0.1), (int) (Settings.res_height * 0.49),
		(int) (Settings.res_width * 0.2), (int) (Settings.res_height * 0.085),
		MouseActionAreaType.MAA_MENU_BTN1, "START", (int) (50 * Settings.factor), Color.RED, Color.BLUE) {
	    @Override
	    public void performAction_LEFT_RELEASE() {

		boolean ok = true;
		if (!checkName())
		    ok = false;
		else if (join.isSelected() && !checkIp(true))
		    ok = false;

		if (join.isSelected() && ok) {
		    try {
			Settings.user_name = name_box.getText();
			Settings.prop.setProperty("user_name", Settings.user_name);
			Settings.ip = ip_box.getText();
			Settings.prop.setProperty("ip_address", Settings.ip);
			// saving current settings
			Settings.prop.store(new FileOutputStream("save.ini"), " Bomberfrau Settings");
			ConsoleHandler.print(
				"Name \"" + Settings.user_name + "\" + IP \"" + Settings.ip + "\" saved in save.ini",
				MessageType.MENU);
		    } catch (IOException ex) {
		    }

		    // SoundHandler.reduceAllSounds();
		    GraphicsHandler.switchToLobbyFromMenu();
		    Panes.InfoPane(null, "Name \"" + Settings.user_name + "\" + IP \"" + Settings.ip
			    + "\" saved in save.ini. Now switching to Lobby", "OK");
		    // JOptionPane.showMessageDialog(null,"Name \"" + Settings.user_name + "\" + IP
		    // \"" + Settings.ip + "\" saved in save.ini\nNow switching to Lobby","to be
		    // continued ...", JOptionPane.PLAIN_MESSAGE);
		}

		if (create.isSelected() && ok) {
		    try {
			Settings.user_name = name_box.getText();
			Settings.prop.setProperty("user_name", Settings.user_name);
			// saving current settings
			Settings.prop.store(new FileOutputStream("save.ini"), " Bomberfrau Settings");
			ConsoleHandler.print("Name \"" + Settings.user_name + "\" saved in save.ini", MessageType.MENU);
		    } catch (IOException ex) {
		    }

		    // SoundHandler.reduceAllSounds();
		    System.out.println("Switching to Lobby");
		    GraphicsHandler.switchToLobbyFromMenu();
		    Panes.InfoPane(null,
			    "Name \"" + Settings.user_name + "\" saved in save.ini. Now switching to Lobby", "OK");
		    // JOptionPane.showMessageDialog(null,"Name \"" + Settings.user_name + "\" saved
		    // in save.ini\nNow switching to Lobby","to be continued ...",
		    // JOptionPane.PLAIN_MESSAGE);
		}

		ConsoleHandler.print("Switching from Menu to Lobby ...", MessageType.MENU);
	    }

	    @Override
	    public boolean isActiv() {
		return GraphicsHandler.getDisplayType() == DisplayType.MENU;
	    }
	};

	options = new MouseActionArea((int) (Settings.res_width * 0.23), (int) (Settings.res_height * 0.67),
		(int) (Settings.res_width * 0.2), (int) (Settings.res_height * 0.085),
		MouseActionAreaType.MAA_MENU_BTN2, LanguageHandler.getLLB(LanguageBlockType.LB_MENU_BTN2).getContent(),
		(int) (50 * Settings.factor), Color.RED, Color.BLUE) {
	    @Override
	    public void performAction_LEFT_RELEASE() {
		GraphicsHandler.switchToOptionsFromMenu();
	    }

	    @Override
	    public boolean isActiv() {
		return GraphicsHandler.getDisplayType() == DisplayType.MENU;
	    }
	};

	exit = new MouseActionArea((int) (Settings.res_width * 0.36), (int) (Settings.res_height * 0.8),
		(int) (Settings.res_width * 0.2), (int) (Settings.res_height * 0.085),
		MouseActionAreaType.MAA_MENU_BTN3, LanguageHandler.getLLB(LanguageBlockType.LB_MENU_BTN3).getContent(),
		(int) (50 * Settings.factor), Color.RED, Color.BLUE) {
	    @Override
	    public void performAction_LEFT_RELEASE() {
		if (!(name_box.getText().equals(Settings.user_name)) || !(ip_box.getText().equals(Settings.ip))) {
		    int n = JOptionPane.showConfirmDialog(null, "Do you want to save name + IP", "save settings",
			    JOptionPane.YES_NO_OPTION);
		    if (n == 0) {
			if (!checkName() || !checkIp(true)) {
			    return;
			}
			Settings.user_name = name_box.getText();
			Settings.ip = ip_box.getText();
			Settings.saveIni();
			ConsoleHandler.print("quit with saving", MessageType.MENU);
		    } else {
			ConsoleHandler.print("quit without saving", MessageType.MENU);
		    }
		}
		ConsoleHandler.print("Quiting game ...", MessageType.MENU);
		// SoundHandler.stopAllSounds();
		SoundHandler.reduceAllSounds();
		GraphicsHandler.shutdownProgram();
	    }

	    @Override
	    public boolean isActiv() {
		return GraphicsHandler.getDisplayType() == DisplayType.MENU;
	    }
	};

    }

    /**
     * Kn�pfe/MAAs des Optionsmen�s - Back: zur�ck ins Hauptmen�
     */
    public static void initMaaOptions() {

	back = new MouseActionArea((int) (Settings.res_width * 0.55), (int) (Settings.res_height * 0.85),
		(int) (Settings.res_width * 0.16), (int) (Settings.res_height * 0.065),
		MouseActionAreaType.MAA_OPTIONS_BTN,
		"" + LanguageHandler.getLLB(LanguageBlockType.LB_OPTIONS_BTN).getContent(),
		(int) (30 * Settings.factor), Color.RED, Color.BLUE) {
	    @Override
	    public void performAction_LEFT_RELEASE() {
		Settings.saveIni();
		GraphicsHandler.switchToMenuFromOptions();
	    }

	    @Override
	    public boolean isActiv() {
		return GraphicsHandler.getDisplayType() == DisplayType.OPTIONS;
	    }
	};
    }

    /**
     * MAA der tempor�ren Test-Lobby - Vollbild Klick f�r R�ckkehr zum Hauptmen�
     */
    public static void initMaaLobby() {

	new MouseActionArea(-1, -1, Settings.res_width + 1, Settings.res_height + 1, MouseActionAreaType.MAA_LOBBY, "",
		1, Color.WHITE, Color.WHITE) {
	    @Override
	    public void performAction_LEFT_RELEASE() {
		ConsoleHandler.print("Switched to 'MENU' from 'LOBBY'!", MessageType.LOBBY);
		GraphicsHandler.switchToMenuFromLobby();
	    }

	    @Override
	    public boolean isActiv() {
		return GraphicsHandler.getDisplayType() == DisplayType.LOBBY;
	    }
	};
    }

    /*****************************************************************************************************************
     * HILFSMETHODEN
     *****************************************************************************************************************/

    /**
     * �berpr�fung des eingegebenen Namens false, wenn leer oder "?"
     */
    private static Boolean checkName() {
	boolean check = true;
	// System.out.println("name_box.getText() = " + name_box.getText());
	if ((name_box.getText().isEmpty()) || (name_box.getText().equals("?"))) {
	    Panes.InfoPane(null, LanguageHandler.getLLB(LanguageBlockType.LB_MSG_BAD_NAME).getContent(), "OK");
	    name_box.setText(Settings.prop.getProperty("user_name"));
	    ;
	    check = false;
	}
	return check;
    }

    /**
     * �berpr�fung des Musters der eingegebenen IP-Adresse false, wenn nicht
     * folgendes Muster vorliegt: x.x.x.x mit x von 0 bis 255
     */
    private static boolean isValideIp(final String ip) {
	return IPV4_PATTERN.matcher(ip).matches();
    }

    /**
     * �berpr�fung der eingegebenen IP-Adresse false, wenn leer oder ung�ltiges
     * Muster
     */
    private static Boolean checkIp(Boolean ausgabe) {
	boolean check = true;
	if ((ip_box.getText().isEmpty()) || (!isValideIp(ip_box.getText()))) {
	    if (ausgabe)
		Panes.InfoPane(null, LanguageHandler.getLLB(LanguageBlockType.LB_MSG_BAD_IP).getContent(), "OK");
	    // ip_box.setText(Settings.prop.getProperty("ip_address"));;
	    check = false;
	}
	return check;
    }

    /**
     * Ersetzt Zeichen der Steuerungen wandelt Kleinbuchstaben in gro�e um was weder
     * Buchstabe noch Zahl ist wird durch ein passendes Wort ersetzt
     */
    private static String codeToText(int i) {

	// System.out.println("codeToText: i = " + i)
	if (i == 37)
	    return LanguageHandler.getLLB(LanguageBlockType.LB_KEY_LEFT).getContent();
	if (i == 38)
	    return LanguageHandler.getLLB(LanguageBlockType.LB_KEY_UP).getContent();
	if (i == 39)
	    return LanguageHandler.getLLB(LanguageBlockType.LB_KEY_RIGHT).getContent();
	if (i == 40)
	    return LanguageHandler.getLLB(LanguageBlockType.LB_KEY_DOWN).getContent();

	return (KeyEvent.getKeyText(i));
    }

    /**
     * Methode zum Warten in Millisekunden
     */
    public static void sleep(long millis) {
	try {
	    Thread.sleep(millis);
	} catch (InterruptedException ignored) {
	}
    }

    /*****************************************************************************************************************
     * ANIMATIONEN
     *****************************************************************************************************************/

    /**
     * Animation Intro Bild Zooming
     */
    public static void introAnimation() {
	new Animation(1, 1250) {
	    @Override
	    public void initValues() {
		AnimationData.intro_zoom = 0;
	    }

	    @Override
	    public void changeValues() {
		if (getSteps() % 3 == 0) {
		    if ((getSteps() > 50) && (getSteps() <= 350))
			AnimationData.intro_zoom += 0.01;
		    if ((getSteps() >= 750) && (getSteps() < 1050))
			AnimationData.intro_zoom -= 0.01;
		}
	    }

	    @Override
	    public void finaliseValues() {
		AnimationData.intro_zoom = 0;
		GraphicsHandler.switchToMenuFromIntro();
	    }
	};

    }

    /**
     * Animation Intro Text "click to skip"
     */
    public static void introTextAni() {
	new Animation(60, -1) {
	    @Override
	    public void initValues() {
		AnimationData.intro_skip_text = 0;
	    }

	    @Override
	    public void changeValues() {
		if (getSteps() % 2 == 0) {
		    AnimationData.intro_skip_text = 3;
		} else {
		    AnimationData.intro_skip_text = 0;
		}
	    }

	    @Override
	    public void finaliseValues() {
		AnimationData.intro_skip_text = 0;
	    }
	};

    }
    
    /**
     * Animation Title Text Pulsing
     */
    public static void titlePulseAni() {
	
	new Animation(1, -1) {
	    @Override
	    public void initValues() {
		AnimationData.title_Modifier = 0;
	    }

	    @Override
	    public void changeValues() {
		if (AnimationData.title_Modifier > 30) grow = false;
		if (AnimationData.title_Modifier == 0) grow = true;
		if (getSteps() % 2 == 0) {
		    if (grow)
			AnimationData.title_Modifier += 1;
		    else 
        		AnimationData.title_Modifier -= 1;
		}
	    }

	    @Override
	    public void finaliseValues() {
		AnimationData.title_Modifier = 0;
	    }
	};

    }

    /**
     * Animation Title Text Shaking
     */
    public static void titleShakeAni() {
	Random r = new Random();
	new Animation(10, -1) {
		@Override
		public void initValues() {
			AnimationData.title_posXModifier = 0;
			AnimationData.title_posYModifier = 0;
		}
		@Override
		public void changeValues() {
			if(getSteps()%2 == 0) {
				int stepSize = 1;
				AnimationData.title_posXModifier += r.nextInt(stepSize*2)-stepSize;
				AnimationData.title_posYModifier += r.nextInt(stepSize*2)-stepSize;
				int limit = 10;
				if(AnimationData.title_posXModifier < -limit) { AnimationData.title_posXModifier = -limit; }
				else if(AnimationData.title_posXModifier > limit) { AnimationData.title_posXModifier = limit; }
				if(AnimationData.title_posYModifier < -limit) { AnimationData.title_posYModifier = -limit; }
				else if(AnimationData.title_posYModifier > limit) { AnimationData.title_posYModifier = limit; }
			}else {
				AnimationData.title_posXModifier = 0;
				AnimationData.title_posYModifier = 0;
			}
		}
		@Override
		public void finaliseValues() {
			AnimationData.title_posXModifier = 0;
			AnimationData.title_posYModifier = 0;
		}
	};

    }
}