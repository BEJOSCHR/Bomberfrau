/*
 * Menu
 *
 * Version 1.0
 * Author: Carsten
 *
 * Das Haupt- und Optionsmenü des Spieles
 */

package uni.bombenstimmung.de.menu;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.*;
import java.util.regex.*; // for PATTERN (IP address)

import javax.sound.sampled.FloatControl;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import uni.bombenstimmung.de.backend.animation.AnimationHandler;
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
    private static boolean isHost = true;
    private static JLabel name_info, ip_info;
    private static JTextField name_box, ip_box;
    private static JComboBox<String> comboboxReso, comboboxLang;
    private static JSlider sliderMusic, sliderSound;
    private static JTextField control_up, control_down, control_left, control_right, control_bomb;
    private static JCheckBox checkBoxFPS;
    private static MouseActionArea intro, start, options, exit, back;

    /**
     * correct pattern for IPv4 address - 4 times 0-255 without leading zeros.
     */
    private static final Pattern IPV4_PATTERN = Pattern
	    .compile("^(([0-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5])(\\.(?!$)|$)){4}$");
    // "^(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");

    /**
     * Getter für Lobby
     */
    public static boolean getIs_host() {
	return isHost;
    }

    /**
     * Erstellt Swingkomponenten für das Hauptmenü
     */
    public static void buildMenu() {

	create = new JRadioButton();
	create.setText(" " + LanguageHandler.getLLB(LanguageBlockType.LB_MENU_TXT1).getContent());
	create.setBounds((int) (Settings.getRes_width() * 0.32), (int) (Settings.getRes_height() * 0.485),
		(int) (Settings.getRes_width() / 10), (int) (Settings.getRes_height() / 20));
	create.setBackground(Color.WHITE);
	create.setFont(GraphicsHandler.usedFont(30));
	create.setFocusable(false);
	if (Settings.getCreate_selected()) {
	    create.setSelected(true);
	    isHost = true;
	}

	create.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		ConsoleHandler.print("remove ip box", MessageType.MENU);
		Settings.setCreate_selected(true);
		isHost = true;
		ConsoleHandler.print("isHost = " + isHost, MessageType.MENU);
		ip_box.setVisible(false);
		ip_info.setVisible(false);
	    }
	});

	join = new JRadioButton();
	join.setText(" " + LanguageHandler.getLLB(LanguageBlockType.LB_MENU_TXT2).getContent());
	join.setBounds((int) (Settings.getRes_width() * 0.32), (int) (Settings.getRes_height() * 0.528),
		(int) (Settings.getRes_width() / 9), (int) (Settings.getRes_height() / 20));
	join.setBackground(Color.WHITE);
	join.setFont(GraphicsHandler.usedFont(30));
	join.setFocusable(false);
	if (!Settings.getCreate_selected()) {
	    join.setSelected(true);
	    isHost = false;
	}

	join.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		ConsoleHandler.print("add ip box", MessageType.MENU);
		Settings.setCreate_selected(false);
		isHost = false;
		ConsoleHandler.print("isHost = " + isHost, MessageType.MENU);
		ip_box.setVisible(true);
		ip_info.setVisible(true);
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
	name_info.setBounds((int) (Settings.getRes_width() * 0.52), (int) (Settings.getRes_height() * 0.37),
		(int) (Settings.getRes_width() / 5), (int) (Settings.getRes_height() / 12));

	name_box = new JTextField(Settings.getUser_name());
	name_box.setHorizontalAlignment(JTextField.CENTER);
	name_box.setFont(
		GraphicsHandler.usedFont(40).deriveFont(40f * (1f - 0.022f * (name_box.getText().length() - 8))));
	name_box.setBounds((int) (Settings.getRes_width() * 0.52), (int) (Settings.getRes_height() * 0.44),
		(int) (Settings.getRes_width() / 5), (int) (Settings.getRes_height() / 12));
	name_box.addKeyListener(new KeyListener() {
	    public void keyPressed(KeyEvent e) {
	    };

	    public void keyTyped(KeyEvent e) {
	    };

	    public void keyReleased(KeyEvent e) {
		String txt = name_box.getText();
		if (!txt.isEmpty()) {
		    name_info.setText("");
		    name_info.repaint();
		}
		if (txt.length() > MAX_NAME_LENGTH) {
		    // name_box.setText(txt.substring(0, MAX_NAME_LENGTH));
		    name_box.setText(Settings.getUser_name());
		    name_info.setText(LanguageHandler.getLLB(LanguageBlockType.LB_MENU_INFO1).getContent());
		    name_info.repaint();
		} else {
		    Settings.setUser_name(txt);
		    name_box.setFont(GraphicsHandler.usedFont(40)
			    .deriveFont(44f * Settings.getFactor() * (1f - 0.022f * (txt.length() - 7))));
		}
	    }
	});
	name_box.addFocusListener(new FocusListener() {

	    @Override
	    public void focusLost(FocusEvent e) {
	    }

	    @Override
	    public void focusGained(FocusEvent e) {
		if (name_box.getText().equals("?"))
		    name_box.setText("");
	    }
	});

	// JTextField ip_box = new JTextField("0.0.0.0");
	// ip_box = new JTextField(prop.getProperty("ip_address"));
	ip_box = new JTextField(Settings.getIp());
	ip_box.setHorizontalAlignment(JTextField.CENTER);
	ip_box.setFont(GraphicsHandler.usedFont((int) (40 * Settings.getFactor())));
	if (Settings.getCreate_selected())
	    ip_box.setVisible(false);
	else
	    ip_box.setVisible(true);
	ip_box.setBounds((int) (Settings.getRes_width() * 0.52), (int) (Settings.getRes_height() * 0.54),
		(int) (Settings.getRes_width() / 5), (int) (Settings.getRes_height() / 12));
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
		Settings.setIp(txt);
		if (!checkIp(false)) {
		    ip_info.setText(LanguageHandler.getLLB(LanguageBlockType.LB_MENU_INFO2).getContent());
		    ip_info.repaint();
		}

	    }
	});

	ip_info = new JLabel("");
	ip_info.setHorizontalAlignment(JLabel.CENTER);
	if (Settings.getCreate_selected())
	    ip_info.setVisible(false);
	if (!checkIp(false))
	    ip_info.setText(LanguageHandler.getLLB(LanguageBlockType.LB_MENU_INFO2).getContent());
	ip_info.setBackground(Color.WHITE);
	ip_info.setForeground(Color.RED);
	ip_info.setFont(GraphicsHandler.usedFont(20));
	ip_info.setBounds((int) (Settings.getRes_width() * 0.52), (int) (Settings.getRes_height() * 0.6),
		(int) (Settings.getRes_width() / 5), (int) (Settings.getRes_height() / 12));

	GraphicsHandler.getLabel().add(create);
	GraphicsHandler.getLabel().add(join);
	GraphicsHandler.getLabel().add(name_box);
	GraphicsHandler.getLabel().add(name_info);
	GraphicsHandler.getLabel().add(ip_box);
	GraphicsHandler.getLabel().add(ip_info);
    }

    /**
     * Erstellt Swingkomponenten für das Optionsmenü
     */
    public static void buildOptions() {

	// Der "factor" passt alle Grössen und Positionen der Auflösung an
	Settings.setResolution(Settings.getRes_nr());
	Settings.setFactor((float) (Settings.getRes_height()) / Settings.getRes_height_max());

	String[] res = { " " + LanguageHandler.getLLB(LanguageBlockType.LB_OPT_FULLSCREEN).getContent(),
		" HD    1280 x 720", " WSXGA 1600 x 900", " FHD   1920 x 1080", " WQHD  2560 x 1440",
		" UHD   3840 x 2160" };

	comboboxReso = new JComboBox<>(res);
	comboboxReso.setBackground(Color.WHITE);
	comboboxReso.setFont(new Font("Consolas", Font.BOLD, (int) (40 * Settings.getFactor())));
	comboboxReso.setBounds((int) (Settings.getRes_width() * 0.28), (int) (Settings.getRes_height() * 0.1),
		(int) (Settings.getRes_width() / 4.5), (int) (Settings.getRes_height() / 18));
	comboboxReso.setSelectedItem(res[Settings.getRes_nr()]);

	comboboxReso.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {

		int t = (int) comboboxReso.getSelectedIndex();
		t = Settings.setResolution(t);
		Settings.setFactor((float) (Settings.getRes_height()) / Settings.getRes_height_max());

		GraphicsHandler.getFrame().setSize(Settings.getRes_width(), Settings.getRes_height());
		GraphicsHandler.getFrame().setLocation((Settings.getRes_width_max() - Settings.getRes_width()) / 2,
			(Settings.getRes_height_max() - Settings.getRes_height()) / 2);
		comboboxReso.setFont(new Font("Consolas", Font.BOLD, (int) (40 * Settings.getFactor())));
		comboboxReso.setBounds((int) (Settings.getRes_width() * 0.28), (int) (Settings.getRes_height() * 0.1),
			(int) (Settings.getRes_width() / 4.5), (int) (Settings.getRes_height() / 18));
		sliderMusic.setBounds((int) (Settings.getRes_width() * 0.28), (int) (Settings.getRes_height() * 0.2),
			(int) (Settings.getRes_width() / 4.5), (int) (Settings.getRes_height() / 12));
		sliderSound.setBounds((int) (Settings.getRes_width() * 0.28), (int) (Settings.getRes_height() * 0.3),
			(int) (Settings.getRes_width() / 4.5), (int) (Settings.getRes_height() / 12));

		control_up.setFont(GraphicsHandler
			.usedFont((int) ((46 - 3 * control_up.getText().length()) * Settings.getFactor())));
		control_down.setFont(GraphicsHandler
			.usedFont((int) ((46 - 3 * control_down.getText().length()) * Settings.getFactor())));
		control_left.setFont(GraphicsHandler
			.usedFont((int) ((46 - 3 * control_left.getText().length()) * Settings.getFactor())));
		control_right.setFont(GraphicsHandler
			.usedFont((int) ((46 - 3 * control_right.getText().length()) * Settings.getFactor())));
		control_bomb.setFont(GraphicsHandler
			.usedFont((int) ((46 - 3 * control_bomb.getText().length()) * Settings.getFactor())));

		control_up.setBounds((int) (Settings.getRes_width() * 0.33), (int) (Settings.getRes_height() * 0.44),
			(int) (Settings.getRes_width() / 12), (int) (Settings.getRes_height() / 16));
		control_down.setBounds((int) (Settings.getRes_width() * 0.33), (int) (Settings.getRes_height() * 0.64),
			(int) (Settings.getRes_width() / 12), (int) (Settings.getRes_height() / 16));
		control_left.setBounds((int) (Settings.getRes_width() * 0.28), (int) (Settings.getRes_height() * 0.54),
			(int) (Settings.getRes_width() / 12), (int) (Settings.getRes_height() / 16));
		control_right.setBounds((int) (Settings.getRes_width() * 0.38), (int) (Settings.getRes_height() * 0.54),
			(int) (Settings.getRes_width() / 12), (int) (Settings.getRes_height() / 16));
		control_bomb.setBounds((int) (Settings.getRes_width() * 0.5), (int) (Settings.getRes_height() * 0.54),
			(int) (Settings.getRes_width() / 12), (int) (Settings.getRes_height() / 16));
		checkBoxFPS.setFont(GraphicsHandler.usedFont((int) (30f * Settings.getFactor())));
		checkBoxFPS.setBounds((int) (Settings.getRes_width() * 0.1), (int) (Settings.getRes_height() * 0.7),
			(int) (Settings.getRes_width() / 5), (int) (Settings.getRes_height() / 12));
		comboboxLang.setFont(new Font("Consolas", Font.BOLD, (int) (40 * Settings.getFactor())));
		comboboxLang.setBounds((int) (Settings.getRes_width() * 0.28), (int) (Settings.getRes_height() * 0.81),
			(int) (Settings.getRes_width() / 7), (int) (Settings.getRes_height() / 18));
		start.remove();
		options.remove();
		exit.remove();
		back.remove();
		initMaaMainmenu();
		initMaaOptions();
		GraphicsHandler.getLabel().repaint();

		comboboxReso.setSelectedItem(res[t]);
		Settings.setRes_nr(t);
		GraphicsHandler.setMoveable();
	    }
	});

	sliderMusic = new JSlider(JSlider.HORIZONTAL, 0, 100, Settings.getIni_VolMusic());
	sliderMusic.setBackground(Color.WHITE);
	sliderMusic.setBounds((int) (Settings.getRes_width() * 0.28), (int) (Settings.getRes_height() * 0.2),
		(int) (Settings.getRes_width() / 4.5), (int) (Settings.getRes_height() / 12));
	sliderMusic.setMinorTickSpacing(5);
	sliderMusic.setMajorTickSpacing(10);
	sliderMusic.setPaintTicks(true);
	sliderMusic.setPaintLabels(true);
	sliderMusic.setSnapToTicks(true);
	sliderMusic.addChangeListener(new ChangeListener() {
	    public void stateChanged(ChangeEvent event) {
		Settings.setVol_music(sliderMusic.getValue());
		ConsoleHandler.print("Music Volume = " + sliderMusic.getValue(), MessageType.MENU);

		// zuerst wird die laufende Musik angepasst
		FloatControl volume = (FloatControl) SoundHandler.getSound(SoundType.MENU).getClip()
			.getControl(FloatControl.Type.MASTER_GAIN);
		float vol = VolumeIntToFloat(sliderMusic.getValue());
		ConsoleHandler.print("Music Volume2 = " + vol, MessageType.MENU);
		volume.setValue(vol);

		// danach wird die 'Music Category' Lautstärke angepasst
		SoundHandler.setCategoryVolume(SoundCategory.MENU_MUSIC, -0.2D + sliderMusic.getValue() / 125);
		SoundHandler.setCategoryVolume(SoundCategory.INGAME_MUSIC, -0.2D + sliderMusic.getValue() / 125);

	    }
	});

	sliderSound = new JSlider(JSlider.HORIZONTAL, 0, 100, Settings.getIni_VolSound());
	sliderSound.setBackground(Color.WHITE);
	sliderSound.setBounds((int) (Settings.getRes_width() * 0.28), (int) (Settings.getRes_height() * 0.3),
		(int) (Settings.getRes_width() / 4.5), (int) (Settings.getRes_height() / 12));
	sliderSound.setMinorTickSpacing(5);
	sliderSound.setMajorTickSpacing(10);
	sliderSound.setPaintTicks(true);
	sliderSound.setPaintLabels(true);
	sliderSound.setSnapToTicks(true);
	sliderSound.addMouseListener(new MouseListener() {

	    @Override
	    public void mousePressed(MouseEvent e) {
		ConsoleHandler.print("mousePressed", MessageType.MENU);
		SoundHandler.playSound2(SoundType.OPTIONS, true);
		ConsoleHandler.print("Sound gestartet", MessageType.MENU);
		FloatControl volume = (FloatControl) SoundHandler.getSound(SoundType.OPTIONS).getClip()
			.getControl(FloatControl.Type.MASTER_GAIN);
		float vol = VolumeIntToFloat(sliderSound.getValue());
		volume.setValue(vol);

	    }

	    @Override
	    public void mouseReleased(MouseEvent e) {
		ConsoleHandler.print("mouseReleased", MessageType.MENU);
		SoundHandler.getSound(SoundType.OPTIONS).getClip().stop();
		
		// am Ende wird die 'Sound Category' Lautstärke angepasst
		SoundHandler.setCategoryVolume(SoundCategory.MENU_MUSIC, -0.2D + sliderMusic.getValue() / 125);
		SoundHandler.setCategoryVolume(SoundCategory.INGAME_MUSIC, -0.2D + sliderMusic.getValue() / 125);

	    }

	    @Override
	    public void mouseExited(MouseEvent e) {
	    }

	    @Override
	    public void mouseEntered(MouseEvent e) {
	    }

	    @Override
	    public void mouseClicked(MouseEvent e) {
	    }
	});

	sliderSound.addChangeListener(new ChangeListener() {
	    public void stateChanged(ChangeEvent event) {
		Settings.setVol_sound(sliderSound.getValue());
		ConsoleHandler.print("Sound Volume = " + sliderSound.getValue(), MessageType.MENU);

		// währenddessen wird der laufende Sound angepasst
		FloatControl volume = (FloatControl) SoundHandler.getSound(SoundType.OPTIONS).getClip()
			.getControl(FloatControl.Type.MASTER_GAIN);
		float vol = VolumeIntToFloat(sliderSound.getValue());
		volume.setValue(vol);

	    }
	});

	control_up = new JTextField();
	control_up.setHorizontalAlignment(JTextField.CENTER);
	control_up.setText(codeToText(Settings.getMove_up()));
	control_up.setFont(
		GraphicsHandler.usedFont((int) ((46 - 3 * control_up.getText().length()) * Settings.getFactor())));
	control_up.setBounds((int) (Settings.getRes_width() * 0.33), (int) (Settings.getRes_height() * 0.44),
		(int) (Settings.getRes_width() / 12), (int) (Settings.getRes_height() / 16));
	control_up.addKeyListener(new KeyListener() {
	    public void keyReleased(KeyEvent e) {
//		ConsoleHandler.print("control_up: code = " + e.getKeyCode() + " , e.getKeyChar() = " + e.getKeyChar()
//			+ " , int = " + (int) (e.getKeyChar()), MessageType.MENU);
//		ConsoleHandler.print("control_up: code ext = " + e.getExtendedKeyCode(), MessageType.MENU);
//		ConsoleHandler.print("getKeyText = " + KeyEvent.getKeyText(e.getKeyCode())
//			+ ", getExtendedKeyCodeForChar = " + KeyEvent.getExtendedKeyCodeForChar(e.getKeyCode()),
//			MessageType.MENU);
		Settings.setMove_up(e.getKeyCode());
		control_up.setText(codeToText(Settings.getMove_up()));
		control_up.setFont(GraphicsHandler
			.usedFont((int) ((46 - 3 * control_up.getText().length()) * Settings.getFactor())));
	    };

	    public void keyTyped(KeyEvent e) {
		control_up.setText("");
	    };

	    public void keyPressed(KeyEvent e) {
	    };
	});

	control_down = new JTextField();
	control_down.setHorizontalAlignment(JTextField.CENTER);
	control_down.setText(codeToText(Settings.getMove_down()));
	control_down.setFont(
		GraphicsHandler.usedFont((int) ((46 - 3 * control_down.getText().length()) * Settings.getFactor())));
	control_down.setBounds((int) (Settings.getRes_width() * 0.33), (int) (Settings.getRes_height() * 0.64),
		(int) (Settings.getRes_width() / 12), (int) (Settings.getRes_height() / 16));
	control_down.addKeyListener(new KeyListener() {
	    public void keyReleased(KeyEvent e) {
		Settings.setMove_down(e.getKeyCode());
		control_down.setText(codeToText(e.getKeyCode()));
		control_down.setFont(GraphicsHandler
			.usedFont((int) ((46 - 3 * control_down.getText().length()) * Settings.getFactor())));
	    };

	    public void keyTyped(KeyEvent e) {
		control_down.setText("");
	    };

	    public void keyPressed(KeyEvent e) {
	    };
	});

	control_left = new JTextField();
	control_left.setHorizontalAlignment(JTextField.CENTER);
	control_left.setText(codeToText(Settings.getMove_left()));
	control_left.setFont(
		GraphicsHandler.usedFont((int) ((46 - 3 * control_left.getText().length()) * Settings.getFactor())));
	control_left.setBounds((int) (Settings.getRes_width() * 0.28), (int) (Settings.getRes_height() * 0.54),
		(int) (Settings.getRes_width() / 12), (int) (Settings.getRes_height() / 16));
	control_left.addKeyListener(new KeyListener() {
	    public void keyReleased(KeyEvent e) {
		Settings.setMove_left(e.getKeyCode());
		control_left.setText(codeToText(e.getKeyCode()));
		control_left.setFont(GraphicsHandler
			.usedFont((int) ((46 - 3 * control_left.getText().length()) * Settings.getFactor())));
	    };

	    public void keyTyped(KeyEvent e) {
		control_left.setText("");
	    };

	    public void keyPressed(KeyEvent e) {
	    };
	});

	control_right = new JTextField();
	control_right.setHorizontalAlignment(JTextField.CENTER);
	control_right.setText(codeToText(Settings.getMove_right()));
	control_right.setFont(
		GraphicsHandler.usedFont((int) ((46 - 3 * control_right.getText().length()) * Settings.getFactor())));
	control_right.setBounds((int) (Settings.getRes_width() * 0.38), (int) (Settings.getRes_height() * 0.54),
		(int) (Settings.getRes_width() / 12), (int) (Settings.getRes_height() / 16));
	control_right.addKeyListener(new KeyListener() {
	    public void keyReleased(KeyEvent e) {
		Settings.setMove_right(e.getKeyCode());
		control_right.setText(codeToText(e.getKeyCode()));
		control_right.setFont(GraphicsHandler
			.usedFont((int) ((46 - 3 * control_right.getText().length()) * Settings.getFactor())));
	    };

	    public void keyTyped(KeyEvent e) {
		control_right.setText("");
	    };

	    public void keyPressed(KeyEvent e) {
	    };
	});

	control_bomb = new JTextField();
	control_bomb.setHorizontalAlignment(JTextField.CENTER);
	control_bomb.setText(codeToText(Settings.getPlant_bomb()));
	control_bomb.setFont(
		GraphicsHandler.usedFont((int) ((46 - 3 * control_bomb.getText().length()) * Settings.getFactor())));
	control_bomb.setBounds((int) (Settings.getRes_width() * 0.5), (int) (Settings.getRes_height() * 0.54),
		(int) (Settings.getRes_width() / 12), (int) (Settings.getRes_height() / 16));
	control_bomb.addKeyListener(new KeyListener() {
	    public void keyReleased(KeyEvent e) {
		Settings.setPlant_bomb(e.getKeyCode());
		control_bomb.setText(codeToText(e.getKeyCode()));
		control_bomb.setFont(GraphicsHandler
			.usedFont((int) ((46 - 3 * control_bomb.getText().length()) * Settings.getFactor())));
	    };

	    public void keyTyped(KeyEvent e) {
		control_bomb.setText("");
	    };

	    public void keyPressed(KeyEvent e) {
	    };
	});

	checkBoxFPS = new JCheckBox();
	checkBoxFPS.setText(LanguageHandler.getLLB(LanguageBlockType.LB_OPT_TXT6).getContent() + "  ");
	checkBoxFPS.setFont(GraphicsHandler.usedFont((int) (30f * Settings.getFactor())));
	checkBoxFPS.setHorizontalTextPosition(SwingConstants.LEFT);
	checkBoxFPS.setBackground(Color.WHITE);
	checkBoxFPS.setForeground(Color.RED);
	checkBoxFPS.setFocusable(false);
	checkBoxFPS.setBounds((int) (Settings.getRes_width() * 0.1), (int) (Settings.getRes_height() * 0.7),
		(int) (Settings.getRes_width() / 5), (int) (Settings.getRes_height() / 12));
	checkBoxFPS.setSelected(Settings.getShow_fps());
	checkBoxFPS.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		// ConsoleHandler.print("Checkbox=" + checkBoxFPS.isSelected(),
		// MessageType.MENU);
		Settings.setShow_fps(checkBoxFPS.isSelected());
		if (Settings.getShow_fps())
		    GraphicsHandler.getLabel().setShowFPS(true);
		else
		    GraphicsHandler.getLabel().setShowFPS(false);
	    }
	});

	String[] lang = { " English", " Deutsch" };
	comboboxLang = new JComboBox<>(lang);
	comboboxLang.setFont(new Font("Consolas", Font.BOLD, (int) (40 * Settings.getFactor())));
	comboboxLang.setSelectedItem(lang[Settings.getLang_nr()]);
	comboboxLang.setBackground(Color.WHITE);
	comboboxLang.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		Settings.setLang_nr(comboboxLang.getSelectedIndex());
		if (Settings.getLang_nr() == 0)
		    LanguageHandler.setActiveLanguage(LanguageType.ENGLISH);
		if (Settings.getLang_nr() == 1)
		    LanguageHandler.setActiveLanguage(LanguageType.GERMAN);

		comboboxReso.insertItemAt(
			" " + LanguageHandler.getLLB(LanguageBlockType.LB_OPT_FULLSCREEN).getContent(), 0);
		comboboxReso.removeItemAt(1);
		if (Settings.getRes_nr() == 0)
		    comboboxReso.setSelectedIndex(0);
		control_up.setText(codeToText(Settings.getMove_up()));
		control_down.setText(codeToText(Settings.getMove_down()));
		control_left.setText(codeToText(Settings.getMove_left()));
		control_right.setText(codeToText(Settings.getMove_right()));
		control_bomb.setText(codeToText(Settings.getPlant_bomb()));
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
	comboboxLang.setBounds((int) (Settings.getRes_width() * 0.28), (int) (Settings.getRes_height() * 0.81),
		(int) (Settings.getRes_width() / 7), (int) (Settings.getRes_height() / 18));

    }

    /**
     * Setzt oder entfernt die Swing Komponenten des Hauptmenüs
     */
    public static void menuComponentsActive(Boolean bool) {
	if (bool) {
	    ConsoleHandler.print("adding Mainmenu Componenten", MessageType.MENU);
	    GraphicsHandler.getLabel().add(create);
	    GraphicsHandler.getLabel().add(join);
	    GraphicsHandler.getLabel().add(name_box);
	    GraphicsHandler.getLabel().add(name_info);
	    if (!Settings.getCreate_selected()) {
		GraphicsHandler.getLabel().add(ip_box);
		GraphicsHandler.getLabel().add(ip_info);
	    }

	} else {
	    ConsoleHandler.print("removing Mainmenü Components", MessageType.MENU);
	    GraphicsHandler.getLabel().remove(create);
	    GraphicsHandler.getLabel().remove(join);
	    GraphicsHandler.getLabel().remove(name_box);
	    GraphicsHandler.getLabel().remove(name_info);
	    if (!Settings.getCreate_selected()) {
		GraphicsHandler.getLabel().remove(ip_box);
		GraphicsHandler.getLabel().remove(ip_info);
	    }
	}

    }

    /**
     * Setzt oder entfernt die Swing Komponenten des Optionsmenüs
     */
    public static void optionsComponentsActive(Boolean bool) {
	if (bool) {
	    ConsoleHandler.print("adding Optionsmenu Components", MessageType.MENU);
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
	    ConsoleHandler.print("removing Optionsmenu Components", MessageType.MENU);
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
     * INIT MAA MENÜ
     *****************************************************************************************************************/

    /**
     * MAA des Intros - Vollbild Klick zum Überspringen
     */
    public static void initMaaIntro() {

	intro = new MouseActionArea(-1, -1, Settings.getRes_width() + 1, Settings.getRes_height() + 1,
		MouseActionAreaType.MAA_INTRO, "", 1, Color.WHITE, Color.WHITE) {
	    @Override
	    public void performAction_LEFT_RELEASE() {
		// ConsoleHandler.print("Wechsel vom Intro zu Menü per Klick",
		// MessageType.MENU);
		AnimationHandler.stopAllAnimations();
		intro.remove();
		// GraphicsHandler.switchToMenuFromIntro();
	    }

	    @Override
	    public boolean isActiv() {
		return GraphicsHandler.getDisplayType() == DisplayType.INTRO;
	    }
	};
    }

    /**
     * Knöpfe/MAAs des Hauptmenüs: - Start: Übergang in die Lobby nach Überprüfung
     * von Namen und IP Adresse - Options: Übergang zu den Optionen - Exit: Beenden
     * des Spieles
     */
    public static void initMaaMainmenu() {

	start = new MouseActionArea((int) (Settings.getRes_width() * 0.1), (int) (Settings.getRes_height() * 0.49),
		(int) (Settings.getRes_width() * 0.2), (int) (Settings.getRes_height() * 0.085),
		MouseActionAreaType.MAA_MENU_BTN1, "START", (int) (50 * Settings.getFactor()), Color.RED, Color.BLUE) {
	    @Override
	    public void performAction_LEFT_RELEASE() {

		boolean ok = true;
		if (!checkName(true))
		    ok = false;
		else if (join.isSelected() && !checkIp(true))
		    ok = false;

		if (ok) {
		    Settings.setUser_name(name_box.getText());
		    if (!checkIp(false))
			ip_box.setText("0.0.0.0");
		    Settings.setIp(ip_box.getText());
		    Settings.saveIni();
		    // SoundHandler.reduceAllSounds();
		    GraphicsHandler.switchToLobbyFromMenu();
		}

		ConsoleHandler.print("Switching from Menu to Lobby ...", MessageType.MENU);
	    }

	    @Override
	    public boolean isActiv() {
		return GraphicsHandler.getDisplayType() == DisplayType.MENU;
	    }
	};

	options = new MouseActionArea((int) (Settings.getRes_width() * 0.23), (int) (Settings.getRes_height() * 0.65),
		(int) (Settings.getRes_width() * 0.2), (int) (Settings.getRes_height() * 0.085),
		MouseActionAreaType.MAA_MENU_BTN2, LanguageHandler.getLLB(LanguageBlockType.LB_MENU_BTN2).getContent(),
		(int) (50 * Settings.getFactor()), Color.RED, Color.BLUE) {
	    @Override
	    public void performAction_LEFT_RELEASE() {
		if (!checkName(false))
		    Settings.setUser_name("?");
		if (!checkIp(false))
		    Settings.setIp("0.0.0.0");
		Settings.saveIni();
		GraphicsHandler.switchToOptionsFromMenu();
	    }

	    @Override
	    public boolean isActiv() {
		return GraphicsHandler.getDisplayType() == DisplayType.MENU;
	    }
	};

	exit = new MouseActionArea((int) (Settings.getRes_width() * 0.36), (int) (Settings.getRes_height() * 0.81),
		(int) (Settings.getRes_width() * 0.2), (int) (Settings.getRes_height() * 0.085),
		MouseActionAreaType.MAA_MENU_BTN3, LanguageHandler.getLLB(LanguageBlockType.LB_MENU_BTN3).getContent(),
		(int) (50 * Settings.getFactor()), Color.RED, Color.BLUE) {
	    @Override
	    public void performAction_LEFT_RELEASE() {
		if (!checkName(false))
		    Settings.setUser_name("?");
		if (!checkIp(false))
		    Settings.setIp("0.0.0.0");
		Settings.saveIni();
		ConsoleHandler.print("Quiting game ...", MessageType.MENU);

		// SoundHandler.stopAllSounds();
		// SoundHandler.reduceLastPlayedSound();
		GraphicsHandler.shutdownProgram();
	    }

	    @Override
	    public boolean isActiv() {
		return GraphicsHandler.getDisplayType() == DisplayType.MENU;
	    }
	};

    }

    /**
     * Knöpfe/MAAs des Optionsmenüs - Back: zurück ins Hauptmenü
     */
    public static void initMaaOptions() {

	back = new MouseActionArea((int) (Settings.getRes_width() * 0.55), (int) (Settings.getRes_height() * 0.85),
		(int) (Settings.getRes_width() * 0.16), (int) (Settings.getRes_height() * 0.065),
		MouseActionAreaType.MAA_OPTIONS_BTN,
		"" + LanguageHandler.getLLB(LanguageBlockType.LB_OPTIONS_BTN).getContent(),
		(int) (30 * Settings.getFactor()), Color.RED, Color.BLUE) {
	    @Override
	    public void performAction_LEFT_RELEASE() {
		if (!checkName(false))
		    Settings.setUser_name("?");
		if (!checkIp(false))
		    Settings.setIp("0.0.0.0");
		Settings.saveIni();
		GraphicsHandler.switchToMenuFromOptions();
	    }

	    @Override
	    public boolean isActiv() {
		return GraphicsHandler.getDisplayType() == DisplayType.OPTIONS;
	    }
	};
    }

    /*****************************************************************************************************************
     * HILFSMETHODEN
     *****************************************************************************************************************/

    /**
     * Rechnet linearen Werte zwischen min u. max (für Lautstärken) in Float um
     */
    public static float VolumeIntToFloat(int i) {
	float vol, min = -40F, max = -6F;
	// Für Werte von -36F (fast aus) bis -6F (laut)
	vol = (float)(min + (max-min)*Math.log10(1+i*0.09));
	if (vol == min) vol = -80F;
	if (vol > 6F) vol = 6F;
	return vol;
    }
    
    /**
     * Überprüfung des eingegebenen Namens false, wenn leer oder "?"
     */
    private static Boolean checkName(Boolean ausgabe) {
	boolean check = true;
	name_box.setText(name_box.getText().trim());
	if ((name_box.getText().isEmpty()) || (name_box.getText().equals("?"))) {
	    if (ausgabe)
		Panes.InfoPane(null, LanguageHandler.getLLB(LanguageBlockType.LB_MSG_BAD_NAME).getContent(), "OK");
	    name_box.setText("");
	    name_box.requestFocus();
	    check = false;
	}
	return check;
    }

    /**
     * Überprüfung des Musters der eingegebenen IP-Adresse false, wenn nicht
     * folgendes Muster vorliegt: x.x.x.x mit x von 0 bis 255
     */
    private static boolean isValideIp(final String ip) {
	return IPV4_PATTERN.matcher(ip).matches();
    }

    /**
     * Überprüfung der eingegebenen IP-Adresse false, wenn leer oder ungültiges
     * Muster
     */
    private static Boolean checkIp(Boolean ausgabe) {
	boolean check = true;
	if ((ip_box.getText().isEmpty()) || (!isValideIp(ip_box.getText()))) {
	    if (ausgabe)
		Panes.InfoPane(null, LanguageHandler.getLLB(LanguageBlockType.LB_MSG_BAD_IP).getContent(), "OK");
	    check = false;
	    ip_box.requestFocus();
	}
	return check;
    }

    /**
     * Ersetzt Zeichen der Steuerungen wandelt Kleinbuchstaben in große um was weder
     * Buchstabe noch Zahl ist wird durch ein passendes Wort ersetzt
     */
    private static String codeToText(int i) {

	// ConsoleHandler.print("codeToText: i = " + i, MessageType.MENU);
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
	} catch (InterruptedException iex) {
	}
    }

}