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
import uni.bombenstimmung.de.backend.serverconnection.host.ConnectedClient;
import uni.bombenstimmung.de.backend.sounds.SoundCategory;
import uni.bombenstimmung.de.backend.sounds.SoundHandler;
import uni.bombenstimmung.de.backend.sounds.SoundType;
import uni.bombenstimmung.de.lobby.LobbyCreate;

public class Menu {

    private static final int MIN_NAME_LENGTH = 1;
    private static final int MAX_NAME_LENGTH = 30;

    private static boolean isHost = true;
    private static JRadioButton create, join;
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
     * Getter
     */
    public static int getMinNameLength() {
	return MIN_NAME_LENGTH;
    }

    public static int getMaxNameLength() {
	return MAX_NAME_LENGTH;
    }

    public static boolean getIsHost() {
	return isHost;
    }

    /**
     * Erstellt Swingkomponenten für das Hauptmenü
     */
    public static void buildMenu() {

	create = new JRadioButton();
	create.setText(" " + LanguageHandler.getLLB(LanguageBlockType.LB_MENU_TXT1).getContent());
	create.setBounds((int) (Settings.getResWidth() * 0.3), (int) (Settings.getResHeight() * 0.485),
		(int) (Settings.getResWidth() / 10), (int) (Settings.getResHeight() / 20));
	create.setBackground(Color.WHITE);
	create.setFont(GraphicsHandler.usedFont(30));
	create.setFocusable(false);
	if (Settings.getCreateSelected()) {
	    create.setSelected(true);
	    isHost = true;
	}

	create.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		ConsoleHandler.print("remove ip box", MessageType.MENU);
		Settings.setCreateSelected(true);
		isHost = true;
		ConsoleHandler.print("isHost = " + isHost, MessageType.MENU);
		ip_box.setVisible(false);
		ip_info.setVisible(false);
	    }
	});

	join = new JRadioButton();
	join.setText(" " + LanguageHandler.getLLB(LanguageBlockType.LB_MENU_TXT2).getContent());
	join.setBounds((int) (Settings.getResWidth() * 0.3), (int) (Settings.getResHeight() * 0.528),
		(int) (Settings.getResWidth() / 9), (int) (Settings.getResHeight() / 20));
	join.setBackground(Color.WHITE);
	join.setFont(GraphicsHandler.usedFont(30));
	join.setFocusable(false);
	if (!Settings.getCreateSelected()) {
	    join.setSelected(true);
	    isHost = false;
	}

	join.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		ConsoleHandler.print("add ip box", MessageType.MENU);
		Settings.setCreateSelected(false);
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
	name_info.setBounds((int) (Settings.getResWidth() * 0.5), (int) (Settings.getResHeight() * 0.37),
		(int) (Settings.getResWidth() / 4.7), (int) (Settings.getResHeight() / 12));

	name_box = new JTextField(Settings.getUserName());
	name_box.setHorizontalAlignment(JTextField.CENTER);
	name_box.setFont(GraphicsHandler.usedFont(40)
		.deriveFont(42f * Settings.getFactor() * (1f - 0.022f * (name_box.getText().length() - 8))));
	name_box.setBounds((int) (Settings.getResWidth() * 0.5), (int) (Settings.getResHeight() * 0.44),
		(int) (Settings.getResWidth() / 4.7), (int) (Settings.getResHeight() / 12));
	name_box.addKeyListener(new KeyListener() {
	    public void keyPressed(KeyEvent e) {
	    };

	    public void keyTyped(KeyEvent e) {
	    };

	    public void keyReleased(KeyEvent e) {
		// String txt = name_box.getText();
		String txt = name_box.getText().replace("-", "");
		name_box.setText(txt);
		// ConsoleHandler.print("txt = " + txt, MessageType.MENU);
		if (!txt.isEmpty()) {
		    name_info.setText("");
		    name_info.repaint();
		}
		if ((txt.length() < MIN_NAME_LENGTH) && (txt != "?")) {
		    name_info.setText(LanguageHandler.getLLB(LanguageBlockType.LB_MENU_INFO1).getContent());
		    if (getMinNameLength() == 1)
			name_info.setText(name_info.getText().replace("signs", "sign"));
		    name_info.repaint();
		} else if (txt.length() > MAX_NAME_LENGTH) {
		    name_box.setText(Settings.getUserName());
		    name_info.setText(LanguageHandler.getLLB(LanguageBlockType.LB_MENU_INFO2).getContent());
		    name_info.repaint();
		} else {
		    name_info.setText("");
		    Settings.setUserName(txt);
		    name_box.setFont(GraphicsHandler.usedFont(40)
			    .deriveFont(42f * Settings.getFactor() * (1f - 0.022f * (txt.length() - 8))));
		}
	    }
	});
	name_box.addFocusListener(new FocusListener() {

	    @Override
	    public void focusLost(FocusEvent e) {
	    }

	    @Override
	    public void focusGained(FocusEvent e) {
		if (name_box.getText().equals("?")) {
		    name_box.setText("");
		    name_info.setText(LanguageHandler.getLLB(LanguageBlockType.LB_MENU_INFO1).getContent());
		    if (getMinNameLength() == 1)
			name_info.setText(name_info.getText().replace("signs", "sign"));
		    name_info.repaint();
		}
		name_box.setFont(GraphicsHandler.usedFont(40)
			.deriveFont(42f * Settings.getFactor() * (1f - 0.022f * (name_box.getText().length() - 8))));
		name_box.repaint();
	    }
	});

	// JTextField ip_box = new JTextField("0.0.0.0");
	ip_box = new JTextField(Settings.getIp());
	ip_box.setHorizontalAlignment(JTextField.CENTER);
	ip_box.setFont(GraphicsHandler.usedFont((int) (40 * Settings.getFactor())));
	if (Settings.getCreateSelected())
	    ip_box.setVisible(false);
	else
	    ip_box.setVisible(true);
	ip_box.setBounds((int) (Settings.getResWidth() * 0.5), (int) (Settings.getResHeight() * 0.54),
		(int) (Settings.getResWidth() / 4.7), (int) (Settings.getResHeight() / 12));
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
		    ip_info.setText(LanguageHandler.getLLB(LanguageBlockType.LB_MENU_INFO3).getContent());
		    ip_info.repaint();
		}

	    }
	});

	ip_box.addFocusListener(new FocusListener() {

	    @Override
	    public void focusLost(FocusEvent e) {
	    }

	    @Override
	    public void focusGained(FocusEvent e) {
		if (ip_box.getText().equals("0.0.0.0"))
		    ip_box.setText("");
	    }
	});

	ip_info = new JLabel("");
	ip_info.setHorizontalAlignment(JLabel.CENTER);
	if (Settings.getCreateSelected())
	    ip_info.setVisible(false);
	if (!checkIp(false))
	    ip_info.setText(LanguageHandler.getLLB(LanguageBlockType.LB_MENU_INFO2).getContent());
	ip_info.setBackground(Color.WHITE);
	ip_info.setForeground(Color.RED);
	ip_info.setFont(GraphicsHandler.usedFont(20));
	ip_info.setBounds((int) (Settings.getResWidth() * 0.52), (int) (Settings.getResHeight() * 0.6),
		(int) (Settings.getResWidth() / 5), (int) (Settings.getResHeight() / 12));

	GraphicsHandler.getLabel().add(create);
	GraphicsHandler.getLabel().add(join);
	GraphicsHandler.getLabel().add(name_box);
	GraphicsHandler.getLabel().add(name_info);
	GraphicsHandler.getLabel().add(ip_box);
	GraphicsHandler.getLabel().add(ip_info);
	name_box.setFont(GraphicsHandler.usedFont(40)
		.deriveFont(42f * Settings.getFactor() * (1f - 0.022f * (name_box.getText().length() - 8))));
	name_box.repaint();
    }

    /**
     * Erstellt Swingkomponenten fï¿½r das Optionsmenï¿½
     */
    public static void buildOptions() {

	// Der "factor" passt alle Größen und Positionen der Auflösung an
	Settings.setResolution(Settings.getResNr());
	Settings.setFactor((float) (Settings.getResHeight()) / Settings.getResHeightMax());

	String[] res = { " " + LanguageHandler.getLLB(LanguageBlockType.LB_OPT_FULLSCREEN).getContent(),
		" HD    1280 x 720", " WSXGA 1600 x 900", " FHD   1920 x 1080", " WQHD  2560 x 1440",
		" UHD   3840 x 2160" };

	comboboxReso = new JComboBox<>(res);
	comboboxReso.setBackground(Color.WHITE);
	comboboxReso.setFont(new Font("Consolas", Font.BOLD, (int) (40 * Settings.getFactor())));
	comboboxReso.setBounds((int) (Settings.getResWidth() * 0.28), (int) (Settings.getResHeight() * 0.1),
		(int) (Settings.getResWidth() / 4.5), (int) (Settings.getResHeight() / 18));
	comboboxReso.setSelectedItem(res[Settings.getResNr()]);

	comboboxReso.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {

		int t = (int) comboboxReso.getSelectedIndex();
		t = Settings.setResolution(t);
		Settings.setFactor((float) (Settings.getResHeight()) / Settings.getResHeightMax());

		GraphicsHandler.getFrame().setSize(Settings.getResWidth(), Settings.getResHeight());
		GraphicsHandler.getFrame().setLocation((Settings.getResWidthMax() - Settings.getResWidth()) / 2,
			(Settings.getResHeightMax() - Settings.getResHeight()) / 2);
		comboboxReso.setFont(new Font("Consolas", Font.BOLD, Settings.scaleValue(40)));
		comboboxReso.setBounds((int) (Settings.getResWidth() * 0.28), (int) (Settings.getResHeight() * 0.1),
			(int) (Settings.getResWidth() / 4.5), (int) (Settings.getResHeight() / 18));
		sliderMusic.setBounds((int) (Settings.getResWidth() * 0.28), (int) (Settings.getResHeight() * 0.2),
			(int) (Settings.getResWidth() / 4.5), (int) (Settings.getResHeight() / 12));
		sliderSound.setBounds((int) (Settings.getResWidth() * 0.28), (int) (Settings.getResHeight() * 0.3),
			(int) (Settings.getResWidth() / 4.5), (int) (Settings.getResHeight() / 12));

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

		control_up.setBounds((int) (Settings.getResWidth() * 0.33), (int) (Settings.getResHeight() * 0.44),
			(int) (Settings.getResWidth() / 12), (int) (Settings.getResHeight() / 16));
		control_down.setBounds((int) (Settings.getResWidth() * 0.33), (int) (Settings.getResHeight() * 0.64),
			(int) (Settings.getResWidth() / 12), (int) (Settings.getResHeight() / 16));
		control_left.setBounds((int) (Settings.getResWidth() * 0.28), (int) (Settings.getResHeight() * 0.54),
			(int) (Settings.getResWidth() / 12), (int) (Settings.getResHeight() / 16));
		control_right.setBounds((int) (Settings.getResWidth() * 0.38), (int) (Settings.getResHeight() * 0.54),
			(int) (Settings.getResWidth() / 12), (int) (Settings.getResHeight() / 16));
		control_bomb.setBounds((int) (Settings.getResWidth() * 0.5), (int) (Settings.getResHeight() * 0.54),
			(int) (Settings.getResWidth() / 12), (int) (Settings.getResHeight() / 16));
		checkBoxFPS.setFont(GraphicsHandler.usedFont((int) (30f * Settings.getFactor())));
		checkBoxFPS.setBounds((int) (Settings.getResWidth() * 0.1), (int) (Settings.getResHeight() * 0.7),
			(int) (Settings.getResWidth() / 5), (int) (Settings.getResHeight() / 12));
		comboboxLang.setFont(new Font("Consolas", Font.BOLD, (int) (40 * Settings.getFactor())));
		comboboxLang.setBounds((int) (Settings.getResWidth() * 0.28), (int) (Settings.getResHeight() * 0.81),
			(int) (Settings.getResWidth() / 7), (int) (Settings.getResHeight() / 18));
		start.remove();
		options.remove();
		exit.remove();
		back.remove();
		initMaaMainmenu();
		initMaaOptions();
		GraphicsHandler.getLabel().repaint();

		comboboxReso.setSelectedItem(res[t]);
		Settings.setResNr(t);
		GraphicsHandler.setMoveable();
	    }
	});

	sliderMusic = new JSlider(JSlider.HORIZONTAL, 0, 100, Settings.getIniVolMusic());
	sliderMusic.setBackground(Color.WHITE);
	sliderMusic.setBounds((int) (Settings.getResWidth() * 0.28), (int) (Settings.getResHeight() * 0.2),
		(int) (Settings.getResWidth() / 4.5), (int) (Settings.getResHeight() / 12));
	sliderMusic.setMinorTickSpacing(5);
	sliderMusic.setMajorTickSpacing(10);
	sliderMusic.setPaintTicks(true);
	sliderMusic.setPaintLabels(true);
	sliderMusic.setSnapToTicks(true);
	sliderMusic.addChangeListener(new ChangeListener() {
	    public void stateChanged(ChangeEvent event) {
		Settings.setVolMusic(sliderMusic.getValue());
		ConsoleHandler.print("Music Volume = " + sliderMusic.getValue(), MessageType.MENU);

		// zuerst wird die laufende Musik angepasst
		FloatControl volume = (FloatControl) SoundHandler.getSound(SoundType.MENU).getClip()
			.getControl(FloatControl.Type.MASTER_GAIN);
		float vol = volumeIntToFloat(sliderMusic.getValue());
		ConsoleHandler.print("Music Volume2 = " + vol, MessageType.MENU);
		volume.setValue(vol);

		// danach wird die 'Music Category' Lautstï¿½rke angepasst
		SoundHandler.setCategoryVolume(SoundCategory.MENU_MUSIC, -0.2D + sliderMusic.getValue() / 125);
		SoundHandler.setCategoryVolume(SoundCategory.INGAME_MUSIC, -0.2D + sliderMusic.getValue() / 125);

	    }
	});

	sliderSound = new JSlider(JSlider.HORIZONTAL, 0, 100, Settings.getIniVolSound());
	sliderSound.setBackground(Color.WHITE);
	sliderSound.setBounds((int) (Settings.getResWidth() * 0.28), (int) (Settings.getResHeight() * 0.3),
		(int) (Settings.getResWidth() / 4.5), (int) (Settings.getResHeight() / 12));
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
		float vol = volumeIntToFloat(sliderSound.getValue());
		volume.setValue(vol);

	    }

	    @Override
	    public void mouseReleased(MouseEvent e) {
		ConsoleHandler.print("mouseReleased", MessageType.MENU);
		SoundHandler.getSound(SoundType.OPTIONS).getClip().stop();

		// am Ende wird die 'Sound Category' Lautstï¿½rke angepasst
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
		Settings.setVolSound(sliderSound.getValue());
		ConsoleHandler.print("Sound Volume = " + sliderSound.getValue(), MessageType.MENU);

		// währenddessen wird der laufende Sound angepasst
		FloatControl volume = (FloatControl) SoundHandler.getSound(SoundType.OPTIONS).getClip()
			.getControl(FloatControl.Type.MASTER_GAIN);
		float vol = volumeIntToFloat(sliderSound.getValue());
		volume.setValue(vol);

	    }
	});

	control_up = new JTextField();
	control_up.setHorizontalAlignment(JTextField.CENTER);
	control_up.setText(codeToText(Settings.getMoveUp()));
	control_up.setFont(
		GraphicsHandler.usedFont((int) ((46 - 3 * control_up.getText().length()) * Settings.getFactor())));
	control_up.setBounds((int) (Settings.getResWidth() * 0.33), (int) (Settings.getResHeight() * 0.44),
		(int) (Settings.getResWidth() / 12), (int) (Settings.getResHeight() / 16));
	control_up.addKeyListener(new KeyListener() {
	    public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == 0) {
		    Panes.infoPane(null, LanguageHandler.getLLB(LanguageBlockType.LB_OPT_INFO1).getContent(), "OK");
		} else {
		    Settings.setMoveUp(e.getKeyCode());
		}
		control_up.setText(codeToText(Settings.getMoveUp()));
		control_up.setFont(GraphicsHandler
			.usedFont((int) ((46 - 3 * control_up.getText().length()) * Settings.getFactor())));
		control_up.requestFocus();
	    };

	    public void keyTyped(KeyEvent e) {
		control_up.setText("");
	    };

	    public void keyPressed(KeyEvent e) {
	    };
	});

	control_down = new JTextField();
	control_down.setHorizontalAlignment(JTextField.CENTER);
	control_down.setText(codeToText(Settings.getMoveDown()));
	control_down.setFont(
		GraphicsHandler.usedFont((int) ((46 - 3 * control_down.getText().length()) * Settings.getFactor())));
	control_down.setBounds((int) (Settings.getResWidth() * 0.33), (int) (Settings.getResHeight() * 0.64),
		(int) (Settings.getResWidth() / 12), (int) (Settings.getResHeight() / 16));
	control_down.addKeyListener(new KeyListener() {
	    public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == 0) {
		    Panes.infoPane(null, LanguageHandler.getLLB(LanguageBlockType.LB_OPT_INFO1).getContent(), "OK");
		} else {
		    Settings.setMoveDown(e.getKeyCode());
		}
		control_down.setText(codeToText(Settings.getMoveDown()));
		control_down.setFont(GraphicsHandler
			.usedFont((int) ((46 - 3 * control_down.getText().length()) * Settings.getFactor())));
		control_down.requestFocus();
	    };

	    public void keyTyped(KeyEvent e) {
		control_down.setText("");
	    };

	    public void keyPressed(KeyEvent e) {
	    };
	});

	control_left = new JTextField();
	control_left.setHorizontalAlignment(JTextField.CENTER);
	control_left.setText(codeToText(Settings.getMoveLeft()));
	control_left.setFont(
		GraphicsHandler.usedFont((int) ((46 - 3 * control_left.getText().length()) * Settings.getFactor())));
	control_left.setBounds((int) (Settings.getResWidth() * 0.28), (int) (Settings.getResHeight() * 0.54),
		(int) (Settings.getResWidth() / 12), (int) (Settings.getResHeight() / 16));
	control_left.addKeyListener(new KeyListener() {
	    public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == 0) {
		    Panes.infoPane(null, LanguageHandler.getLLB(LanguageBlockType.LB_OPT_INFO1).getContent(), "OK");
		} else {
		    Settings.setMoveLeft(e.getKeyCode());
		}
		control_left.setText(codeToText(Settings.getMoveLeft()));
		control_left.setFont(GraphicsHandler
			.usedFont((int) ((46 - 3 * control_left.getText().length()) * Settings.getFactor())));
		control_left.requestFocus();
	    };

	    public void keyTyped(KeyEvent e) {
		control_left.setText("");
	    };

	    public void keyPressed(KeyEvent e) {
	    };
	});

	control_right = new JTextField();
	control_right.setHorizontalAlignment(JTextField.CENTER);
	control_right.setText(codeToText(Settings.getMoveRight()));
	control_right.setFont(
		GraphicsHandler.usedFont((int) ((46 - 3 * control_right.getText().length()) * Settings.getFactor())));
	control_right.setBounds((int) (Settings.getResWidth() * 0.38), (int) (Settings.getResHeight() * 0.54),
		(int) (Settings.getResWidth() / 12), (int) (Settings.getResHeight() / 16));
	control_right.addKeyListener(new KeyListener() {
	    public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == 0) {
		    Panes.infoPane(null, LanguageHandler.getLLB(LanguageBlockType.LB_OPT_INFO1).getContent(), "OK");
		} else {
		    Settings.setMoveRight(e.getKeyCode());
		}
		control_right.setText(codeToText(Settings.getMoveRight()));
		control_right.setFont(GraphicsHandler
			.usedFont((int) ((46 - 3 * control_right.getText().length()) * Settings.getFactor())));
		control_right.requestFocus();
	    };

	    public void keyTyped(KeyEvent e) {
		control_right.setText("");
	    };

	    public void keyPressed(KeyEvent e) {
	    };
	});

	control_bomb = new JTextField();
	control_bomb.setHorizontalAlignment(JTextField.CENTER);
	control_bomb.setText(codeToText(Settings.getPlantBomb()));
	control_bomb.setFont(
		GraphicsHandler.usedFont((int) ((46 - 3 * control_bomb.getText().length()) * Settings.getFactor())));
	control_bomb.setBounds((int) (Settings.getResWidth() * 0.5), (int) (Settings.getResHeight() * 0.54),
		(int) (Settings.getResWidth() / 12), (int) (Settings.getResHeight() / 16));
	control_bomb.addKeyListener(new KeyListener() {
	    public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == 0) {
		    Panes.infoPane(null, LanguageHandler.getLLB(LanguageBlockType.LB_OPT_INFO1).getContent(), "OK");
		} else {
		    Settings.setPlantBomb(e.getKeyCode());
		}
		control_bomb.setText(codeToText(Settings.getPlantBomb()));
		control_bomb.setFont(GraphicsHandler
			.usedFont((int) ((46 - 3 * control_bomb.getText().length()) * Settings.getFactor())));
		control_bomb.requestFocus();
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
	checkBoxFPS.setBounds((int) (Settings.getResWidth() * 0.1), (int) (Settings.getResHeight() * 0.7),
		(int) (Settings.getResWidth() / 5), (int) (Settings.getResHeight() / 12));
	checkBoxFPS.setSelected(Settings.getShowFps());
	checkBoxFPS.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		Settings.setShowFps(checkBoxFPS.isSelected());
		if (Settings.getShowFps())
		    GraphicsHandler.getLabel().setShowFPS(true);
		else
		    GraphicsHandler.getLabel().setShowFPS(false);
	    }
	});

	String[] lang = { " English", " Deutsch" };
	comboboxLang = new JComboBox<>(lang);
	comboboxLang.setFont(new Font("Consolas", Font.BOLD, (int) (40 * Settings.getFactor())));
	comboboxLang.setSelectedItem(lang[Settings.getLangNr()]);
	comboboxLang.setBackground(Color.WHITE);
	comboboxLang.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		Settings.setLangNr(comboboxLang.getSelectedIndex());
		if (Settings.getLangNr() == 0)
		    LanguageHandler.setActiveLanguage(LanguageType.ENGLISH);
		if (Settings.getLangNr() == 1)
		    LanguageHandler.setActiveLanguage(LanguageType.GERMAN);

		comboboxReso.insertItemAt(
			" " + LanguageHandler.getLLB(LanguageBlockType.LB_OPT_FULLSCREEN).getContent(), 0);
		comboboxReso.removeItemAt(1);
		if (Settings.getResNr() == 0)
		    comboboxReso.setSelectedIndex(0);
		control_up.setText(codeToText(Settings.getMoveUp()));
		control_down.setText(codeToText(Settings.getMoveDown()));
		control_left.setText(codeToText(Settings.getMoveLeft()));
		control_right.setText(codeToText(Settings.getMoveRight()));
		control_bomb.setText(codeToText(Settings.getPlantBomb()));
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
	comboboxLang.setBounds((int) (Settings.getResWidth() * 0.28), (int) (Settings.getResHeight() * 0.81),
		(int) (Settings.getResWidth() / 7), (int) (Settings.getResHeight() / 18));

    }

    /**
     * Setzt oder entfernt die Swing Komponenten des Hauptmenüs
     */
    public static void menuComponentsActive(boolean choice) {
	if (choice) {
	    ConsoleHandler.print("adding Mainmenu Componenten", MessageType.MENU);
	    GraphicsHandler.getLabel().add(create);
	    GraphicsHandler.getLabel().add(join);
	    GraphicsHandler.getLabel().add(name_box);
	    GraphicsHandler.getLabel().add(name_info);
	    if (!Settings.getCreateSelected()) {
		GraphicsHandler.getLabel().add(ip_box);
		GraphicsHandler.getLabel().add(ip_info);
	    }

	} else {
	    ConsoleHandler.print("removing Mainmenu Components", MessageType.MENU);
	    GraphicsHandler.getLabel().remove(create);
	    GraphicsHandler.getLabel().remove(join);
	    GraphicsHandler.getLabel().remove(name_box);
	    GraphicsHandler.getLabel().remove(name_info);
	    if (!Settings.getCreateSelected()) {
		GraphicsHandler.getLabel().remove(ip_box);
		GraphicsHandler.getLabel().remove(ip_info);
	    }
	}
	Menu.sleep(50);

    }

    /**
     * Setzt oder entfernt die Swing Komponenten des Optionsmenüs
     */
    public static void optionsComponentsActive(Boolean choice) {
	if (choice) {
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
	Menu.sleep(50);
    }

    /*****************************************************************************************************************
     * INIT MAA MENÜ
     *****************************************************************************************************************/

    /**
     * MAA des Intros - Vollbild Klick zum Überspringen
     */
    public static void initMaaIntro() {

	intro = new MouseActionArea(-1, -1, Settings.getResWidth() + 1, Settings.getResHeight() + 1,
		MouseActionAreaType.MAA_INTRO, "", 1, Color.WHITE, Color.WHITE) {
	    @Override
	    public void performAction_LEFT_RELEASE() {
		AnimationHandler.stopAllAnimations();
		intro.remove();
	    }

	    @Override
	    public boolean isActiv() {
		return GraphicsHandler.getDisplayType() == DisplayType.INTRO;
	    }
	};
    }

    /**
     * <<<<<<< HEAD Knöpfe/MAAs des Hauptmenüs: - Start: Übergang in die Lobby nach
     * Überprüfung von Namen und IP Adresse - Options: Übergang zu den Optionen -
     * Exit: Beenden des Spieles ======= Knï¿½pfe/MAAs des Hauptmenï¿½s: - Start:
     * ï¿½bergang in die Lobby nach ï¿½berprï¿½fung von Namen und IP Adresse -
     * Options: ï¿½bergang zu den Optionen - Exit: Beenden des Spieles >>>>>>>
     * refs/remotes/origin/main
     */
    public static void initMaaMainmenu() {

	start = new MouseActionArea((int) (Settings.getResWidth() * 0.08), (int) (Settings.getResHeight() * 0.49),
		(int) (Settings.getResWidth() * 0.2), (int) (Settings.getResHeight() * 0.085),
		MouseActionAreaType.MAA_MENU_BTN1, "START", (int) (50 * Settings.getFactor()), Color.RED, Color.BLUE) {
	    @Override
	    public void performAction_LEFT_RELEASE() {

		boolean ok = true;
		if (!checkName(true))
		    ok = false;
		else if (join.isSelected() && !checkIp(true))
		    ok = false;

//		// Testet Join IP vor der Lobby
//		if (!getIsHost()) {
//		    LobbyCreate.client = new ConnectedClient(false, Settings.getIp());
//		    ConsoleHandler.print("Testing to join IP " + Settings.getIp(), MessageType.MENU);
//		    Menu.sleep(300);
//		    if (LobbyCreate.client.getId() == -1) {
//			Panes.infoPane(null, LanguageHandler.getLLB(LanguageBlockType.LB_LOBBY_FULL).getContent(),
//				"OK");
//			ok = false;
//			LobbyCreate.client.getConnector().dispose();
//			LobbyCreate.client = null;
//		    }
//
//		}
		
		if (ok) {
		    Settings.setUserName(name_box.getText());
		    if (!checkIp(false))
			ip_box.setText("0.0.0.0");
		    Settings.setIp(ip_box.getText());
		    Settings.saveIni();
		    ConsoleHandler.print("Switching from Menu to Lobby ...", MessageType.MENU);
		    GraphicsHandler.switchToLobbyFromMenu();
		}
	    }

	    @Override
	    public boolean isActiv() {
		return GraphicsHandler.getDisplayType() == DisplayType.MENU;
	    }
	};

	options = new MouseActionArea((int) (Settings.getResWidth() * 0.22), (int) (Settings.getResHeight() * 0.65),
		(int) (Settings.getResWidth() * 0.2), (int) (Settings.getResHeight() * 0.085),
		MouseActionAreaType.MAA_MENU_BTN2, LanguageHandler.getLLB(LanguageBlockType.LB_MENU_BTN2).getContent(),
		(int) (50 * Settings.getFactor()), Color.RED, Color.BLUE) {
	    @Override
	    public void performAction_LEFT_RELEASE() {
		if (!checkName(false))
		    Settings.setUserName("?");
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

	exit = new MouseActionArea((int) (Settings.getResWidth() * 0.36), (int) (Settings.getResHeight() * 0.81),
		(int) (Settings.getResWidth() * 0.2), (int) (Settings.getResHeight() * 0.085),
		MouseActionAreaType.MAA_MENU_BTN3, LanguageHandler.getLLB(LanguageBlockType.LB_MENU_BTN3).getContent(),
		(int) (50 * Settings.getFactor()), Color.RED, Color.BLUE) {
	    @Override
	    public void performAction_LEFT_RELEASE() {
		if (!checkName(false))
		    Settings.setUserName("?");
		if (!checkIp(false))
		    Settings.setIp("0.0.0.0");
		Settings.saveIni();
		ConsoleHandler.print("Quiting game ...", MessageType.MENU);

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

	back = new MouseActionArea((int) (Settings.getResWidth() * 0.55), (int) (Settings.getResHeight() * 0.85),
		(int) (Settings.getResWidth() * 0.16), (int) (Settings.getResHeight() * 0.065),
		MouseActionAreaType.MAA_OPTIONS_BTN,
		"" + LanguageHandler.getLLB(LanguageBlockType.LB_OPTIONS_BTN).getContent(),
		(int) (30 * Settings.getFactor()), Color.RED, Color.BLUE) {
	    @Override
	    public void performAction_LEFT_RELEASE() {
		if (!checkName(false))
		    Settings.setUserName("?");
		if (!checkIp(false))
		    Settings.setIp("0.0.0.0");
		Settings.saveIni();
		if (Settings.getMoveUp() == 0 || Settings.getMoveDown() == 0 || Settings.getMoveLeft() == 0
			|| Settings.getMoveRight() == 0 || Settings.getPlantBomb() == 0) {
		    Panes.infoPane(null, LanguageHandler.getLLB(LanguageBlockType.LB_OPT_INFO2).getContent(), "OK");
		} else {
		    GraphicsHandler.switchToMenuFromOptions();
		}
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
    public static float volumeIntToFloat(int i) {
	float vol, min = -40F, max = -6F;
	// für Werte von -36F (fast aus) bis -6F (laut)
	vol = (float) (min + (max - min) * Math.log10(1 + i * 0.09));
	if (vol == min)
	    vol = -80F;
	if (vol > 6F)
	    vol = 6F;
	return vol;
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

    /**
     * Überprüfung des eingegebenen Namens false, wenn leer oder "?"
     */
    private static boolean checkName(boolean ausgabe) {
	boolean check = true;
	name_box.setText(name_box.getText().trim());
	if ((name_box.getText().isEmpty()) || (name_box.getText().equals("?"))
		|| (name_box.getText().length() < MIN_NAME_LENGTH)) {
	    if (ausgabe)
		Panes.infoPane(null, LanguageHandler.getLLB(LanguageBlockType.LB_MSG_BAD_NAME).getContent(), "OK");
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
    private static boolean checkIp(boolean ausgabe) {
	boolean check = true;
	if ((ip_box.getText().isEmpty()) || (!isValideIp(ip_box.getText()))) {
	    if (ausgabe)
		Panes.infoPane(null, LanguageHandler.getLLB(LanguageBlockType.LB_MSG_BAD_IP).getContent(), "OK");
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

}